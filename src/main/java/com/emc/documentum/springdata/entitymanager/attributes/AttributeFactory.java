package com.emc.documentum.springdata.entitymanager.attributes;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class AttributeFactory {

    public static Attribute<?> getAttribute(Field field, String attributeName) {


        Class<?> type = field.getType();

        if (type == java.lang.String.class) {
            return new StringAttribute(attributeName);
        } else if (type == java.lang.Integer.class || type == int.class) {
            return new IntAttribute(attributeName);
        } else if (type == java.lang.Double.class || type == double.class) {
            return new DoubleAttribute(attributeName);
        } else if (type == java.lang.Long.class || type == long.class) {
            return new LongAttribute(attributeName);
        } else if (type == java.lang.Boolean.class || type == boolean.class) {
            return new BooleanAttribute(attributeName);
        } 
        
        
        else if (type == java.util.List.class && getParameterizedType(field) == java.lang.String.class ){
        	// TODO Will not work if field declared as Arraylist of any other extended class of List, use Collection.class.isAssignableFrom(f.getType()) ??
            return new StringListAttribute(attributeName);
        }
        else if (type == java.util.List.class && getParameterizedType(field) == java.lang.Integer.class ){
            return new IntListAttribute(attributeName);
        }
        else if (type == java.util.List.class && getParameterizedType(field) == java.lang.Double.class ){
            return new DoubleListAttribute(attributeName);
        }
        else if (type == java.util.List.class && getParameterizedType(field) == java.lang.Long.class ){
            return new LongListAttribute(attributeName);
        }
        else if (type == java.util.List.class && getParameterizedType(field) == java.lang.Boolean.class ){
            return new BooleanListAttribute(attributeName);
        }

        return null;
    }

    private static Type getParameterizedType(Field field) {
        field.setAccessible(true);
        return ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];

    }


}
