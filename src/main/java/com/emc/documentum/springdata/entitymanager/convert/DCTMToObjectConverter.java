package com.emc.documentum.springdata.entitymanager.convert;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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

  public static final String SELECT_RELATION_QUERY = "select * from dm_relation where relation_name=\'%s\' and parent_id=\'%s\'";
  @Autowired
  MappingHandler mappingHandler;

  public DCTMToObjectConverter() {}

  public void convert(IDfTypedObject dctmObject, Object objectToReturn, ArrayList<AttributeType> mapping) throws DfException {
    for (AttributeType attributeType : mapping) {
      try {
        if (!attributeType.isRelation()) {
          getValue(dctmObject, objectToReturn, attributeType);
        }
      } catch (Exception e) {
        String msg = String.format(
            "Conversion failed for Object of class %s. " + "Exception: %s, %s.", objectToReturn.getClass(), e.getClass(), e.getMessage());
        throw new DfException(msg, e);
      }
    }
    for (AttributeType attributeType : mapping) {
      if (attributeType.isRelation()) {
        getRelatedObjects(dctmObject, objectToReturn, attributeType);
      }
    }
  }

  //TODO: get*** method should return something, change method name.
  @SuppressWarnings("unchecked")
  private void getRelatedObjects(IDfTypedObject dctmObject, Object objectToReturn, AttributeType attributeType) throws DfException {
    try {
      String objectId = dctmObject.getString("r_object_id");
      String relationQuery = String.format(SELECT_RELATION_QUERY, attributeType.getRelationName(), objectId);
      IDfQuery query = new DfQuery(relationQuery);
      IDfCollection relations = query.execute(dctmObject.getSession(), 0);

      setRelatedObjects(dctmObject, objectToReturn, attributeType, relations);

    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      throw new DfException(e);
    }
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

  @SuppressWarnings("unchecked")
  private void setChild(IDfTypedObject dctmObject, Object objectToReturn, AttributeType attributeType, IDfCollection children)
      throws DfException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

    if(children.next()) {
      IDfTypedObject child = children.getTypedObject();
      IDfPersistentObject childObject = dctmObject.getSession().getObject(new DfId(child.getString("child_id")));
      Object relatedEntityInstance = attributeType.getRelatedEntityClass().newInstance();

      convert(childObject, relatedEntityInstance, mappingHandler.getAttributeMappings(relatedEntityInstance));

      PropertyUtils.setSimpleProperty(objectToReturn, attributeType.getFieldName(), relatedEntityInstance);
    }
  }

  @SuppressWarnings("unchecked")
  private void setChildren(IDfTypedObject dctmObject, Object objectToReturn, AttributeType attributeType, IDfCollection children)
      throws DfException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

    List childrenList = new ArrayList();

    while (children.next()) {
      IDfTypedObject child = children.getTypedObject();
      IDfPersistentObject childObject = dctmObject.getSession().getObject(new DfId(child.getString("child_id")));
      Object relatedEntityInstance = attributeType.getRelatedEntityClass().newInstance();
      convert(childObject, relatedEntityInstance, mappingHandler.getAttributeMappings(relatedEntityInstance));
      childrenList.add(relatedEntityInstance);
    }

    PropertyUtils.setSimpleProperty(objectToReturn, attributeType.getFieldName(), childrenList);
  }

  private void getValue(IDfTypedObject dctmObject, Object objectToReturn, AttributeType fieldType)
      throws DfException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    Attribute<?> attribute = fieldType.getAttribute();
    PropertyUtils.setSimpleProperty(objectToReturn, fieldType.getFieldName(), attribute.getValue(dctmObject));
  }
}
