package com.emc.documentum.springdata.entitymanager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.documentum.fc.client.DfIdNotFoundException;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
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

  public <T> T createObject(String repoObjectName, T objectToSave) throws DfException {
    T savedBaseObject = doSave(repoObjectName, objectToSave);

    List<AttributeType> relations = mappingHandler.getRelations(objectToSave);
    for (AttributeType relation : relations) {
      saveRelatedObject(objectToSave, relation);
    }

    return savedBaseObject;
  }

  @SuppressWarnings("unchecked")
  private <T> void saveRelatedObject(T baseObject, AttributeType relationAttribute) throws DfException {
    try {
      Object relations = PropertyUtils.getProperty(baseObject, relationAttribute.getFieldName());

      if (relations != null) {
        Collection relatedObjects = isCollection(relations) ? (Collection)relations : Arrays.asList(relations);

        for (Object relatedObject : relatedObjects) {
          Object relatedDctmObject = createObject(entityTypeHandler.getEntityObjectName(relatedObject.getClass()), relatedObject);

          IDfSysObject parentDctmObject = getDctmObject(baseObject);
          IDfSysObject childDctmObject = getDctmObject(relatedDctmObject);

          parentDctmObject.addChildRelative(relationAttribute.getRelationName(), childDctmObject.getObjectId(), "", true, "");
        }
      }
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new DfException(e);
    }
  }

  private boolean isCollection(Object relations) {
    return Collection.class.isAssignableFrom(relations.getClass());
  }

  private boolean isRelationPresent(IDfCollection childRelatives, IDfSysObject dctmObject) throws DfException {
    boolean isRelated = false;
    while (childRelatives.next()) {
      IDfTypedObject object = childRelatives.getTypedObject();
      if (object.getObjectId().equals(dctmObject.getObjectId())) {
        isRelated = true;
        break;
      }
    }
    return isRelated;
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
      IDfSysObject dctmObject = getDctmObject(objectToUpdate);
      ArrayList<AttributeType> mapping = mappingHandler.getAttributeMappings(objectToUpdate);
      objectToDctmConverter.convert(objectToUpdate, dctmObject, mapping);
      dctmObject.save();
      DCTMToObjectConverter.convert(dctmObject, objectToUpdate, mapping);
      return objectToUpdate;
    } catch (Exception e) {
      String msg = String.format(
          "Object cannot be updated for class %s. Exception: %s, %s.", objectToUpdate.getClass(), e.getClass(), e.getMessage()
      );
      throw new DfException(msg, e);
    }
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

      System.out.println("=======================================");
      System.out.println(dql);
      System.out.println("=======================================");
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

//    public <T> Boolean checkIfIdNull(T objectToCheck) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//    	
//    	String idField = mappingHandler.getIdField(objectToCheck);
//    	Object idValue = PropertyUtils.getSimpleProperty(objectToCheck, idField);
//    	return idValue.equals(null);
//    }

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
}

