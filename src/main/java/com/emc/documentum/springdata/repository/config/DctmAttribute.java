package com.emc.documentum.springdata.repository.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
//TODO: Consider this on methods (setters). Although, setting the target to method will make it too generic to make the intent clear.
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DctmAttribute {
  String name();
}
