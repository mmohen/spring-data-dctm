package com.emc.documentum.springdata.entitymanager.mapping;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.annotation.Id;
import org.springframework.util.Assert;

import com.emc.documentum.springdata.core.GenericCache;
import com.emc.documentum.springdata.entitymanager.attributes.Attribute;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeFactory;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;

public class MappingHandler {

    private final Class<?> entityClass;
    private String idField;
    
	private final GenericCache cache;

    public <T> MappingHandler(T objectToSave) {
        this(objectToSave.getClass());

    }

    public MappingHandler(Class<?> entityClass) {
        Assert.notNull(entityClass, "No class parameter provided, entity collection can't be determined!");
        this.entityClass = entityClass;
        cache = new GenericCache();
    }
    
	public Class<?> getEntityClass() {
		return this.entityClass;
	}

	public String getIdField() {
		if(idField == null && cache.getEntry(this.entityClass) != null)
		{
			idField = getIDFromCache();
		}
		return idField;
	}
	

    private String getIDFromCache() {
    	ArrayList<AttributeType> mapping =  (ArrayList<AttributeType>) cache.getEntry(this.entityClass);
    	
    	for(AttributeType attributeType : mapping ){
    		if(attributeType.getAttribute().getName() == "r_object_id"){
    			return attributeType.getFieldName();
    		}
    	}
    	
		return null;
	}

	public ArrayList<AttributeType> getAttributeMappings() {

        if (cache.getEntry(this.entityClass) == null) {

            return setAttributeMappingInCache();
        } else {

            return (ArrayList<AttributeType>) cache.getEntry(this.entityClass);
        }

    }

    public ArrayList<AttributeType> setAttributeMappingInCache() {

        Attribute<?> attribute;
        String attributeName;

        ArrayList<AttributeType> mapping = new ArrayList<AttributeType>();
        Field[] fields = this.entityClass.getDeclaredFields();
        if (fields.length == 0) {
            throw new InvalidDataAccessApiUsageException(
                    "No fields to map for the given class!");
        }

        for (Field f : fields) {
            f.setAccessible(true);
            Class<?> type = f.getType();
            attributeName = getEntityFieldName(f);
            attribute = AttributeFactory.getAttribute(type, attributeName);
            AttributeType attributeType = new AttributeType(f.getName(), attribute);
            mapping.add(attributeType);
        }
        cache.setEntry(this.entityClass, mapping);
        return mapping;
    }


    private String getEntityFieldName(Field f) {
        EntityField entityField;
        String attributeName;

        if (f.isAnnotationPresent(Id.class)) {
        	
        	idField = f.getName();
            attributeName = "r_object_id";
        } else if (f.isAnnotationPresent(EntityField.class)) {
            entityField = f.getAnnotation(EntityField.class);
            if (entityField != null) {
                attributeName = entityField.value();
            } else {
                attributeName = f.getName();
            }
        } else {
            attributeName = f.getName();
        }
        return attributeName;
    }

}
