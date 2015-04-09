package com.emc.documentum.springdata.entitymanager.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.GenericCache;
import com.emc.documentum.springdata.entitymanager.annotations.Content;
import com.emc.documentum.springdata.entitymanager.annotations.Relation;
import com.emc.documentum.springdata.entitymanager.annotations.RelationshipType;
import com.emc.documentum.springdata.entitymanager.attributes.Attribute;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeFactory;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;
import com.emc.documentum.springdata.entitymanager.attributes.TypeUtils;

@Component
public class MappingHandler {

  private final GenericCache cache;

  public MappingHandler() {
    cache = new GenericCache();
  }

  public <T> String getIdField(T objectOfEntityClass) throws DfException {
    return getIdField(objectOfEntityClass.getClass());
  }

  public String getIdField(Class<?> entityClass) throws DfException {
    Assert.notNull(entityClass, "No class parameter provided, entity collection can't be determined!");
    if (cache.getEntry(entityClass) == null) {
      setAttributeMappingInCache(entityClass);
    }
    return getIDFromCache(entityClass);
  }

  public <T> String getContentField(T objectOfEntityClass) throws DfException {
    return getContentField(objectOfEntityClass.getClass());
  }

  public String getContentField(Class<?> entityClass) throws DfException {
    Assert.notNull(entityClass, "No class parameter provided, entity collection can't be determined!");
    if (cache.getEntry(entityClass) == null) {
      setAttributeMappingInCache(entityClass);
    }
    return getIDFromCache(entityClass);
  }

  @SuppressWarnings("unchecked")
  private String getIDFromCache(Class<?> entityClass) {
    Assert.notNull(entityClass, "No class parameter provided, entity collection can't be determined!");
    ArrayList<AttributeType> mapping = (ArrayList<AttributeType>)cache.getEntry(entityClass);

    for (AttributeType attributeType : mapping) {
      if (attributeType.getAttribute().getName().equals("r_object_id")) {
        return attributeType.getFieldName();
      }
    }
    return null;
  }

  public <T> ArrayList<AttributeType> getAttributeMappings(T objectOfEntityClass) throws DfException {
    return getAttributeMappings(objectOfEntityClass.getClass());
  }

  @SuppressWarnings("unchecked")
  public ArrayList<AttributeType> getAttributeMappings(Class<?> entityClass) throws DfException {
    Assert.notNull(entityClass, "No class parameter provided, entity collection can't be determined!");

    return cache.getEntry(entityClass) == null ? setAttributeMappingInCache(entityClass) : (ArrayList<AttributeType>)cache.getEntry(entityClass);
  }

  public ArrayList<AttributeType> setAttributeMappingInCache(Class<?> entityClass) throws DfException {

    ArrayList<AttributeType> mapping = new ArrayList<>();
    Field[] fields = getDeclaredFields(entityClass);

    for (Field field : fields) {
      field.setAccessible(true);

      if(!isContentAttribute(field)) {
        if (isRelation(field)) {
          getAttributeMappings(getRelatedEntityClass(field));
        }
        addMapping(mapping, field);
      }
    }
    cache.setEntry(entityClass, mapping);
    return mapping;
  }

  private Field[] getDeclaredFields(Class<?> entityClass) throws DfException {
    Field[] fields = entityClass.getDeclaredFields();
    if (fields.length == 0) {
      throw new DfException("No fields to map for the given class!");
    }
    return fields;
  }

  private void addMapping(ArrayList<AttributeType> mapping, Field f) {
    String attributeName = getEntityFieldName(f);
    Attribute<?> attribute = AttributeFactory.getAttribute(f, attributeName);
    AttributeType attributeType = new AttributeType(f.getName(), attribute, isRelation(f),
                                                    isRelation(f) ? f.getAnnotation(Relation.class).name() : "", getRelationshipType(f),
                                                    getRelatedEntityClass(f));

    mapping.add(attributeType);
  }

  private RelationshipType getRelationshipType(Field field) {
    return isRelation(field) ? field.getAnnotation(Relation.class).value() : null;
  }

  private Class<?> getRelatedEntityClass(Field field) {
    if(!isRelation(field))
      return null;

    Class<?> fieldType = field.getType();
    Type type = TypeUtils.isCollection(fieldType) ? ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0] : field.getGenericType();
    return (Class)type;
  }

  private boolean isRelation(Field f) {
    return f.getAnnotation(Relation.class) != null;
  }

  private boolean isContentAttribute(Field f) {
    return f.getAnnotation(Content.class) != null;
  }

  private String getEntityFieldName(Field f) {
    DctmAttribute dctmAttribute;
    String attributeName;

    if (f.isAnnotationPresent(Id.class)) {
      attributeName = "r_object_id";
    } else if (f.isAnnotationPresent(DctmAttribute.class)) {
      dctmAttribute = f.getAnnotation(DctmAttribute.class);
      attributeName = dctmAttribute == null ? f.getName() : dctmAttribute.value();
    } else {
      attributeName = f.getName();
    }
    return attributeName;
  }

  @SuppressWarnings("unchecked")
  public <T> List<AttributeType> getRelations(T objectToSave) throws DfException {
    ArrayList<AttributeType> attributeMappings = getAttributeMappings(objectToSave);
    List relations = new ArrayList();

    for (AttributeType attributeType : attributeMappings) {
      if(attributeType.isRelation()) {
        relations.add(attributeType);
      }
    }
    return relations;
  }
}
