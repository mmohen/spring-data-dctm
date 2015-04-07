package com.emc.documentum.springdata.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.annotation.QueryAnnotation;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
//@Documented
@QueryAnnotation
public @interface Query {

  String value() default "";

  String countQuery() default "";
}