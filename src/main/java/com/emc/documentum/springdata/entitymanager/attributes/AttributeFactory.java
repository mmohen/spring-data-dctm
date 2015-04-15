package com.emc.documentum.springdata.entitymanager.attributes;

import static com.emc.documentum.springdata.entitymanager.attributes.TypeUtils.isBoolean;
import static com.emc.documentum.springdata.entitymanager.attributes.TypeUtils.isCollection;
import static com.emc.documentum.springdata.entitymanager.attributes.TypeUtils.isDouble;
import static com.emc.documentum.springdata.entitymanager.attributes.TypeUtils.isInteger;
import static com.emc.documentum.springdata.entitymanager.attributes.TypeUtils.isLong;
import static com.emc.documentum.springdata.entitymanager.attributes.TypeUtils.isString;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.emc.documentum.springdata.entitymanager.annotations.Relation;


public class AttributeFactory {

    public static Attribute<?> getAttribute(Field field, String attributeName) {
        Class<?> type = field.getType();

        if(isRelation(field)) {
            return getAttributeAsRelation(field, attributeName);
        } else if (isString(type)) {
            return new StringAttribute(attributeName);
        } else if (isInteger(type)) {
            return new IntAttribute(attributeName);
        } else if (isDouble(type)) {
            return new DoubleAttribute(attributeName);
        } else if (isLong(type)) {
            return new LongAttribute(attributeName);
        } else if (isBoolean(type)) {
            return new BooleanAttribute(attributeName);
        } else if (isCollection(type) && isString(getParameterizedType(field))) {
            return new StringListAttribute(attributeName);
        } else if (isCollection(type) && isInteger(getParameterizedType(field))) {
            return new IntListAttribute(attributeName);
        } else if (isCollection(type) && isDouble(getParameterizedType(field))) {
            return new DoubleListAttribute(attributeName);
        } else if (isCollection(type) && isLong(getParameterizedType(field))) {
            return new LongListAttribute(attributeName);
        } else if (isCollection(type) && isBoolean(getParameterizedType(field))) {
            return new BooleanListAttribute(attributeName);
        }
        return null;
    }

    private static Attribute<?> getAttributeAsRelation(Field field, String attributeName) {
        if(isCollection(field.getType())) {
            return new EntityCollectionAttribute<>(attributeName);
        } else {
            return new EntityAttribute<>(attributeName);
        }
    }

    private static boolean isRelation(Field field) {
        return field.getAnnotation(Relation.class) != null;
    }

    private static Type getParameterizedType(Field field) {
        field.setAccessible(true);
        return ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    }
}
