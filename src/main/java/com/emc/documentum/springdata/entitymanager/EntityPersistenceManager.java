package com.emc.documentum.springdata.entitymanager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.documentum.fc.client.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;

import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;
import com.emc.documentum.springdata.entitymanager.convert.DCTMToObjectConverter;
import com.emc.documentum.springdata.entitymanager.convert.ObjectToDCTMConverter;
import com.emc.documentum.springdata.entitymanager.mapping.MappingHandler;
import com.emc.documentum.springdata.repository.query.DctmQuery;

@Controller
public class EntityPersistenceManager {

  public static final String SELECT_RELATION_QUERY =
      "select * from dm_relation where relation_name=\'%s\' and (parent_id=\'%s\' and child_id=\'%s\') or (child_id = \'%s\' and parent_id = \'%s\')";
  private final Set objectsBeingSaved = new HashSet();
  private final Set<String> objectsBeingUpdated = new HashSet<>();
  private final Documentum documentum;
  private final MappingHandler mappingHandler;
  private final EntityTypeHandler entityTypeHandler;
  private final ObjectToDCTMConverter objectToDctmConverter;
  private final DCTMToObjectConverter DCTMToObjectConverter;

  @Autowired
  public EntityPersistenceManager(
      Documentum documentum, MappingHandler mappingHandler, EntityTypeHandler entityTypeHandler, ObjectToDCTMConverter objectToDctmConverter,
      DCTMToObjectConverter DCTMToObjectConverter
  ) {
    this.documentum = documentum;
    this.mappingHandler = mappingHandler;
    this.entityTypeHandler = entityTypeHandler;
    this.objectToDctmConverter = objectToDctmConverter;
    this.DCTMToObjectConverter = DCTMToObjectConverter;
  }

  @SuppressWarnings("unchecked")
  public <T> T createObject(String repoObjectName, T objectToSave) throws DfException {
    try {
      if (objectsBeingSaved.contains(objectToSave)) {
        return objectToSave;
      }

      objectsBeingSaved.add(objectToSave);
      if (isIdAvailable(objectToSave)) {
        return update(objectToSave);
      }

      T savedBaseObject = doSave(repoObjectName, objectToSave);
      Set<RelationshipInfo> relationshipMap = new HashSet<>();

      List<AttributeType> relations = mappingHandler.getRelations(objectToSave);
      for (AttributeType relation : relations) {
        saveRelatedObjects(objectToSave, relation, relationshipMap);
      }

      saveRelationship(relationshipMap);
      return savedBaseObject;
    } catch (Exception e) {
      throw new DfException(e);
    } finally {
      objectsBeingSaved.remove(objectToSave);
    }
  }

  private <T> boolean isIdAvailable(T objectToSave) throws DfException {
    Field[] fields = mappingHandler.getFields(objectToSave.getClass());
    boolean isIdAvailable = false;
    for (Field field : fields) {
      field.setAccessible(true);
      if (field.getAnnotation(Id.class) != null) {
        isIdAvailable = ReflectionUtils.getField(field, objectToSave) != null;
      }
    }

    return isIdAvailable;
  }

  private void saveRelationship(Set<RelationshipInfo> relationshipMap) throws DfException {
    for (RelationshipInfo relationshipInfo : relationshipMap) {
      IDfSysObject parent = relationshipInfo.parentObject;
      parent.addChildRelative(relationshipInfo.relationshipName, relationshipInfo.childObject.getObjectId(), "", true, "");
    }
  }

  @SuppressWarnings("unchecked")
  private <T> void saveRelatedObjects(T baseObject, AttributeType relationAttribute, Set<RelationshipInfo> relationships) throws DfException {
    try {
      Object relations = PropertyUtils.getProperty(baseObject, relationAttribute.getFieldName());

      if (relations != null) {
        Collection relatedObjects = isCollection(relations) ? (Collection)relations : Collections.singletonList(relations);

        for (Object relatedObject : relatedObjects) {
          if (relatedObject == null) { //TODO: improve this, PropertyUtils.getProperty() returns a weird list if the field is null.
            continue;
          }
          Object relatedDctmObject = createObject(entityTypeHandler.getEntityObjectName(relatedObject.getClass()), relatedObject);

          IDfSysObject parentDctmObject = getDctmObject(baseObject);
          IDfSysObject childDctmObject = getDctmObject(relatedDctmObject);

          relationships.add(new RelationshipInfo(relationAttribute.getRelationName(), parentDctmObject, childDctmObject));
        }
      }
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new DfException(e);
    }
  }

  private boolean isCollection(Object relations) {
    return Collection.class.isAssignableFrom(relations.getClass());
  }

  private <T> T doSave(String repoObjectName, T objectToSave) throws DfException {
    System.out.println("Saving object: " + objectToSave);
    try {
      IDfSysObject dctmObject = (IDfSysObject)documentum.getSession().newObject(repoObjectName);
      ArrayList<AttributeType> mapping = mappingHandler.getAttributeMappings(objectToSave);
      objectToDctmConverter.convert(objectToSave, dctmObject, mapping);
      dctmObject.save();

      String fieldName = mappingHandler.getIdField(objectToSave);
      PropertyUtils.setSimpleProperty(objectToSave, fieldName, dctmObject.getObjectId().getId());
      return objectToSave;

    } catch (Exception e) {
      String msg = String.format("Object cannot be created for class %s. Exception: %s, %s.", objectToSave.getClass(), e.getClass(), e.getMessage());
      throw new DfException(msg, e);
    }
  }

  public <T> T update(T objectToUpdate) throws DfException {
    try {
      String id = getId(objectToUpdate);
      if (objectsBeingUpdated.contains(id)) {
        return objectToUpdate;
      } else {
        objectsBeingUpdated.add(id);
        IDfSysObject dctmObject = getDctmObject(objectToUpdate);
        ArrayList<AttributeType> mapping = mappingHandler.getAttributeMappings(objectToUpdate);
        objectToDctmConverter.convert(objectToUpdate, dctmObject, mapping);
        dctmObject.save();
        updateRelatedObjects(objectToUpdate);
        DCTMToObjectConverter.convert(dctmObject, objectToUpdate, mapping);
        return objectToUpdate;
      }
    } catch (Exception e) {
      String msg = String.format(
          "Object cannot be updated for class %s. Exception: %s, %s.", objectToUpdate.getClass(), e.getClass(), e.getMessage()
      );
      throw new DfException(msg, e);
    }
  }

  private <T> void updateRelatedObjects(T dctmObject) throws DfException {
    List<AttributeType> relations = mappingHandler.getRelations(dctmObject);

    try {
      for (AttributeType relation : relations) {
        switch (relation.getRelationshipType()) {
          case ONE_TO_MANY:
            updateChildren(dctmObject, relation);
            break;
          case ONE_TO_ONE:
            updateChild(dctmObject, relation);
            break;
        }
        PropertyUtils.getProperty(dctmObject, relation.getFieldName());

//        IDfSession session = documentum.getSession();
//        IDfQuery query = new DfQuery(String.format(SELECT_RELATION_QUERY, relation.getRelationName(), "", "", "", ""));
//        query.execute(session, 0);
      }
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new DfException(e);
    }
  }

  private <T> void updateChild(T dctmObject, AttributeType relation)
      throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, DfException {
    Object relatedObject = PropertyUtils.getProperty(dctmObject, relation.getFieldName());
    if (relatedObject == null) {
      return;
    }
    createRelatedObject(relatedObject);
    createRelations(dctmObject, Collections.singletonList(getId(relatedObject)), relation.getRelationName());
  }

  private <T> void updateChildren(T dctmObject, AttributeType relation)
      throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, DfException {
    List relatedObjects = (List)PropertyUtils.getProperty(dctmObject, relation.getFieldName());

    if (relatedObjects == null || relatedObjects.size() == 0) {
      return;
    }

    List<String> objectIdToRelate = new ArrayList<>();
    for (Object relatedObject : relatedObjects) {
      createRelatedObject(relatedObject);
      objectIdToRelate.add(getId(relatedObject));
    }

    createRelations(dctmObject, objectIdToRelate, relation.getRelationName());
  }

  private void createRelatedObject(Object relatedObject) throws DfException {
    if (isIdAvailable(relatedObject)) {
      update(relatedObject);
    } else {
      createObject(entityTypeHandler.getEntityObjectName(relatedObject.getClass()), relatedObject);
    }
  }

  private <T> void createRelations(T dctmObject, List<String> objectIdToRelate, String relationName) throws DfException {
    String parentId = getId(dctmObject);
    for (String childId : objectIdToRelate) {
      if (!isRelated(parentId, childId, relationName)) {
        IDfPersistentObject parentObject = documentum.getSession().getObject(new DfId(parentId));
        parentObject.addChildRelative(relationName, new DfId(childId), "", true, "");
      }
    }
  }

  //TODO: Optimize this, too many queries, Map<RelationName, Map<ParentId, Set<ChildId>>>
  private boolean isRelated(String parentId, String childId, String relationName) throws DfException {
    IDfQuery query = new DfQuery();
    String queryString = String.format(SELECT_RELATION_QUERY, relationName, parentId, childId, childId, parentId);
    query.setDQL(queryString);
    IDfCollection relations = query.execute(documentum.getSession(), 0);
    return relations.next(); //DFC version of hasNext();
  }

  private <T> String getId(T objectToSave) throws DfException {
    Field[] fields = mappingHandler.getFields(objectToSave.getClass());
    String id = "";
    for (Field field : fields) {
      field.setAccessible(true);
      if (field.getAnnotation(Id.class) != null) {
        id = (String)ReflectionUtils.getField(field, objectToSave);
      }
    }
    return id;
  }

  public <T> String deleteObject(String repoObjectName, T objectToDelete) throws DfException {
    try {
      String idFieldName = mappingHandler.getIdField(objectToDelete);
      String id = (String)PropertyUtils.getSimpleProperty(objectToDelete, idFieldName);
      return deleteObject(id);
    } catch (Exception e) {
      String msg = String.format("Object cannot be deleted of class %s. Exception: %s, %s.", objectToDelete.getClass(), e.getClass(), e.getMessage());
      throw new DfException(msg, e);
    }
  }

  public <T> String deleteObject(String id) throws DfException {
    try {
      IDfSession session = documentum.getSession();
      IDfSysObject dctmObjectToDelete = (IDfSysObject)session.getObject(new DfId(id));
      dctmObjectToDelete.destroy();
      return id;
    } catch (DfException e) {
      String msg = String.format("Object with id {%s} could not be deleted. Exception: %s, %s.", id, e.getClass(), e.getMessage());
      throw new DfException(msg, e);
    }
  }

  public <T> List<T> findAllObjects(Class<T> entityClass, String repoObjectName) throws DfException {
    try {

      IDfSession session = documentum.getSession();
      ArrayList<AttributeType> mapping = mappingHandler.getAttributeMappings(entityClass);
      List<T> list = new ArrayList<>();

      IDfQuery query = new DfQuery();
      String dql = "select * from " + repoObjectName;    // TODO: create a DQL Builder, introspect mapping and make efficient dql
      query.setDQL(dql);
      IDfCollection coll = query.execute(session, 0);

      while (coll.next()) {
        T objectInstance = entityClass.newInstance();
        IDfTypedObject dctmObject = coll.getTypedObject();
        DCTMToObjectConverter.convert(dctmObject, objectInstance, mapping);
        list.add(objectInstance);
      }
      return list;

    } catch (Exception e) {
      String msg = String.format("Objects cannot be found for class %s. Exception: %s, %s.", entityClass, e.getClass(), e.getMessage());
      throw new DfException(msg, e);
    }
  }

  public <T> List<T> find(Class<T> entityClass, String repoObjectName, DctmQuery dctmQuery) throws DfException {
    try {

      IDfSession session = documentum.getSession();
      ArrayList<AttributeType> mapping = mappingHandler.getAttributeMappings(entityClass);
      List<T> list = new ArrayList<>();

      IDfQuery query = new DfQuery();
      String selectClause = "select * from ";
      String whereClause = "where " + dctmQuery.getPredicate();
      String dql = String.format("%s %s %s", selectClause, repoObjectName, whereClause);

      System.out.println(dql);
      query.setDQL(dql);

      IDfCollection coll = query.execute(session, 0);

      while (coll.next()) {
        T objectInstance = entityClass.newInstance();
        IDfTypedObject dctmObject = coll.getTypedObject();
        DCTMToObjectConverter.convert(dctmObject, objectInstance, mapping);
        list.add(objectInstance);
      }
      return list;

    } catch (Exception e) {
      String msg = String.format("Objects cannot be found for class %s. Exception: %s, %s.", entityClass, e.getClass(), e.getMessage());
      throw new DfException(msg, e);
    }
  }


  public <T> T findById(String id, Class<T> entityClass) throws DfException {

    try {
      IDfSession session = documentum.getSession();
      ArrayList<AttributeType> mapping = mappingHandler.getAttributeMappings(entityClass);
      DfId dfid = new DfId(id);
      IDfSysObject dctmObject = (IDfSysObject)session.getObject(dfid);
      T objectInstance = entityClass.newInstance();
      DCTMToObjectConverter.convert(dctmObject, objectInstance, mapping);
      return objectInstance;
    } catch (DfIdNotFoundException dfId) {
      return null;
    } catch (Exception e) {
      String msg = String.format(
          "Exception occurred for object with Id: %s class %s. Exception: %s, %s.", id, entityClass, e.getClass(), e.getMessage()
      );
      throw new DfException(msg, e);
    }
  }

  public <T> void setContent(T object, String contentType, String path) throws DfException {
    try {
      IDfSysObject dctmObject = getDctmObject(object);
      dctmObject.setContentType(contentType);
      dctmObject.setFile(path);
      dctmObject.save();
    } catch (Exception e) {
      String msg = String.format(
          "Object cannot be updated for class %s. Exception: %s, %s.", object.getClass(), e.getClass(), e.getMessage()
      );
      throw new DfException(msg, e);
    }
  }

  public <T> String getContent(T object, String filePath) throws DfException {
    try {
      IDfSysObject dctmObject = getDctmObject(object);
      dctmObject.getFile(filePath);
      return filePath;
    } catch (Exception e) {
      String msg = String.format("Content cannot be fetched for class %s. Exception: %s, %s.", object.getClass(), e.getClass(), e.getMessage());
      throw new DfException(msg, e);
    }
  }

  private <T> IDfSysObject getDctmObject(T object) throws DfException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    String idField = mappingHandler.getIdField(object);
    Object valueFromClass = PropertyUtils.getSimpleProperty(object, idField);
    DfId dfid = new DfId((String)valueFromClass);
    return (IDfSysObject)documentum.getSession().getObject(dfid);
  }


  public long count(Class<?> entityClass, String repoObjectName) throws DfException {
    try {
      IDfSession session = documentum.getSession();
      IDfQuery query = new DfQuery();
      String dql = "select count(*) as object_count from " + repoObjectName;    // TODO: create a DQL Builder
      query.setDQL(dql);
      IDfCollection coll = query.execute(session, IDfQuery.DF_READ_QUERY);
      coll.next();
      return coll.getTypedObject().getLong("object_count");
    } catch (Exception e) {
      String msg = String.format("Objects count be found for class %s. Exception: %s, %s.", entityClass, e.getClass(), e.getMessage());
      throw new DfException(msg, e);
    }
  }

  private static final class RelationshipInfo {
    private final String relationshipName;
    private final IDfSysObject parentObject;
    private final IDfSysObject childObject;

    private RelationshipInfo(String relationshipName, IDfSysObject parentObject, IDfSysObject childObject) {
      this.relationshipName = relationshipName;
      this.parentObject = parentObject;
      this.childObject = childObject;
    }
  }
}

