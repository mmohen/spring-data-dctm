package com.emc.documentum.springdata.entitymanager.mapping;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.util.Assert;

import com.emc.documentum.springdata.entitymanager.attributes.Attribute;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeFactory;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;

public class MappingHandler {
	
	private final Class<?> entityClass;

	public <T> MappingHandler(T objectToSave) {
		this(objectToSave.getClass());
			
	}
	
	public MappingHandler(Class<?> entityClass) {
		Assert.notNull(entityClass, "No class parameter provided, entity collection can't be determined!" );
		this.entityClass = entityClass;
	}

	public ArrayList<AttributeType> getAttributeMappings() {
        EntityField entityField;
        Attribute<?> attribute;
        String attributeName;

		ArrayList<AttributeType> mapping = new ArrayList<AttributeType>();
		Field[] fields = this.entityClass.getDeclaredFields();
		if(fields.length == 0) {
			throw new InvalidDataAccessApiUsageException(
					"No fields to map for the given class!");
		}
		

		for (Field f: fields) {
		   f.setAccessible(true);
		   Class<?> type = f.getType();
            attributeName = getEntityFieldName(f);
            attribute = AttributeFactory.getAttribute(type, attributeName);
		   AttributeType attributeType = new AttributeType(f.getName(), attribute);
		   mapping.add(attributeType);  
		}
		return mapping;
	}


    private String getEntityFieldName(Field f) {
        EntityField entityField;
        String attributeName;
        if (f.isAnnotationPresent(EntityField.class)) {
            entityField = f.getAnnotation(EntityField.class);
            if (entityField != null) {
                attributeName = entityField.value();
            }
            else {
                attributeName = f.getName();
            }
        }
        else {
            attributeName = f.getName();
        }
        return attributeName;
    }

}
