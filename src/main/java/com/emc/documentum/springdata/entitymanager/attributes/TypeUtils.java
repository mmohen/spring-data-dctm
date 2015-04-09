package com.emc.documentum.springdata.entitymanager.attributes;

import java.lang.reflect.Type;
import java.util.Collection;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class TypeUtils {
  public static boolean isBoolean(Class<?> type) {return type == Boolean.class || type == boolean.class;}

  public static boolean isLong(Class<?> type) {return type == Long.class || type == long.class;}

  public static boolean isDouble(Class<?> type) {return type == Double.class || type == double.class;}

  public static boolean isInteger(Class<?> type) {return type == Integer.class || type == int.class;}

  public static boolean isString(Class<?> type) {return type == String.class;}

  public static boolean isCollection(Class<?> type) {return Collection.class.isAssignableFrom(type);}  
  
  public static boolean isBoolean(Type type) {return type == Boolean.class || type == boolean.class;}

  public static boolean isLong(Type type) {return type == Long.class || type == long.class;}

  public static boolean isDouble(Type type) {return type == Double.class || type == double.class;}

  public static boolean isInteger(Type type) {return type == Integer.class || type == int.class;}

  public static boolean isString(Type type) {return type == String.class;}

  public static boolean isCollection(Type type) {return Collection.class.isAssignableFrom((Class)type);}
}
