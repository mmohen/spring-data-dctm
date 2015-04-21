package com.emc.documentum.springdata.entitymanager.convert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.emc.documentum.springdata.entitymanager.attributes.Attribute;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;
import com.emc.documentum.springdata.entitymanager.mapping.MappingHandler;

@Controller
public class DCTMToObjectConverter {

  public static final String SELECT_RELATION_QUERY =
      "select * from dm_relation where relation_name=\'%s\' and parent_id=\'%s\' or " + "child_id=\'%s\'";
  @Autowired
  MappingHandler mappingHandler;
  Map<String, Object> objectsBeingConverted = new HashMap<>();

  public DCTMToObjectConverter() {}

  @SuppressWarnings("unchecked")
  public void convert(IDfTypedObject dctmObject, Object objectToReturn, ArrayList<AttributeType> mapping) throws DfException {
    setNonRelationalAttributes(dctmObject, objectToReturn, mapping);

    setRelationalAttributes(dctmObject, objectToReturn, mapping);
  }

  private void setNonRelationalAttributes(IDfTypedObject dctmObject, Object objectToReturn, ArrayList<AttributeType> mapping) throws DfException {
    for (AttributeType attributeType : mapping) {
      try {
        if (!attributeType.isRelation()) {
          System.out.println(attributeType);
          getValue(dctmObject, objectToReturn, attributeType);
        }
      } catch (Exception e) {
        String msg = String.format(
            "Conversion failed for Object of class %s. " + "Exception: %s, %s.", objectToReturn.getClass(), e.getClass(), e.getMessage());
        System.out.println("Failed for ");
        throw new DfException(msg, e);
      }
    }
  }

  private void setRelationalAttributes(IDfTypedObject dctmObject, Object objectToReturn, ArrayList<AttributeType> mapping) throws DfException {

    if (objectsBeingConverted.get(getId(objectToReturn)) == null) {
      objectsBeingConverted.put(getId(objectToReturn), objectToReturn);
    }
    for (AttributeType attributeType : mapping) {
      if (attributeType.isRelation()) {
        populateRelatedObjects(dctmObject, objectToReturn, attributeType);
      }
    }
  }

  //TODO: get*** method should return something, change method name.
  @SuppressWarnings("unchecked")
  private void populateRelatedObjects(IDfTypedObject dctmObject, Object objectToReturn, AttributeType attributeType) throws DfException {
    try {
      IDfCollection relations = getRelationObjects(dctmObject, attributeType);
      setRelatedObjects(dctmObject, objectToReturn, attributeType, relations);
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      throw new DfException(e);
    }
  }

  private IDfCollection getRelationObjects(IDfTypedObject dctmObject, AttributeType attributeType) throws DfException {
    String objectId = dctmObject.getString("r_object_id");
    String relationQuery = String.format(SELECT_RELATION_QUERY, attributeType.getRelationName(), objectId, objectId);
    System.out.println(String.format("Executing query \r\n %s \r\n", relationQuery));

    IDfQuery query = new DfQuery(relationQuery);
    return query.execute(dctmObject.getSession(), 0);
  }

  private void setRelatedObjects(IDfTypedObject dctmObject, Object objectToReturn, AttributeType attributeType, IDfCollection relations)
      throws DfException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

    switch (attributeType.getRelationshipType()) {
      case ONE_TO_MANY:
        setChildren(dctmObject, objectToReturn, attributeType, relations);
        break;
      case ONE_TO_ONE:
        setChild(dctmObject, objectToReturn, attributeType, relations);
        break;
    }
  }

  //TODO: Fix code duplication in setChild and setChildren
  @SuppressWarnings("unchecked")
  private void setChild(IDfTypedObject dctmObject, Object objectToReturn, AttributeType attributeType, IDfCollection relation)
      throws DfException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

    if (relation.next()) {
      String relatedObjectId = getId(objectToReturn).equalsIgnoreCase(relation.getString("child_id")) ? relation.getString("parent_id") : relation
          .getString("child_id");

      if (objectsBeingConverted.get(relatedObjectId) == null) {
        IDfTypedObject child = relation.getTypedObject();
        IDfPersistentObject childObject = dctmObject.getSession().getObject(new DfId(child.getString("child_id")));
        Object relatedEntityInstance = attributeType.getRelatedEntityClass().newInstance();

        convert(childObject, relatedEntityInstance, mappingHandler.getAttributeMappings(relatedEntityInstance));
        PropertyUtils.setSimpleProperty(objectToReturn, attributeType.getFieldName(), relatedEntityInstance);
      } else {
        PropertyUtils.setSimpleProperty(objectToReturn, attributeType.getFieldName(), objectsBeingConverted.get(relatedObjectId));
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void setChildren(IDfTypedObject dctmObject, Object objectToReturn, AttributeType attributeType, IDfCollection relations)
      throws DfException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

    List childrenList = new ArrayList();

    while (relations.next()) {
      IDfTypedObject relation = relations.getTypedObject();
      String relatedObjectId = getId(objectToReturn).equalsIgnoreCase(relation.getString("child_id")) ? relation.getString("parent_id") : relation
          .getString("child_id");

      if (objectsBeingConverted.get(relatedObjectId) == null) {
        IDfPersistentObject childObject = dctmObject.getSession().getObject(new DfId(relatedObjectId));

        String childObjectType = childObject.getString("r_object_type");
        System.out.println(String.format("Child object type: {%s}", childObjectType));

        Object relatedEntityInstance = attributeType.getRelatedEntityClass().newInstance();
        convert(childObject, relatedEntityInstance, mappingHandler.getAttributeMappings(relatedEntityInstance));
        childrenList.add(relatedEntityInstance);
        objectsBeingConverted.put(relatedObjectId, relatedEntityInstance);
      } else {
        childrenList.add(objectsBeingConverted.get(relatedObjectId));
      }
    }

    PropertyUtils.setSimpleProperty(objectToReturn, attributeType.getFieldName(), childrenList);
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

  private void getValue(IDfTypedObject dctmObject, Object objectToReturn, AttributeType fieldType)
      throws DfException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Attribute<?> attribute = fieldType.getAttribute();
    System.out.println("Object is: " + dctmObject.getString("r_object_type"));
    Object attributeValue = attribute.getValue(dctmObject);
    PropertyUtils.setSimpleProperty(objectToReturn, fieldType.getFieldName(), attributeValue);
  }
}
