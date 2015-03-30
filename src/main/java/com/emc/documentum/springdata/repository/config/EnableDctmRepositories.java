package com.emc.documentum.springdata.repository.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;

import com.emc.documentum.springdata.repository.support.DctmRepositoryFactoryBean;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DctmRepositoriesRegistrar.class)
public @interface EnableDctmRepositories {

  String[] value() default {};

  String[] basePackages() default {};

  Class<?>[] basePackageClasses() default {};

  Filter[] includeFilters() default {};

  Filter[] excludeFilters() default {};

  String repositoryImplementationPostfix() default "Impl";

  String namedQueriesLocation() default "";

  Key queryLookupStrategy() default Key.CREATE_IF_NOT_FOUND;

  Class<?> repositoryFactoryBeanClass() default DctmRepositoryFactoryBean.class;

//  String entityManagerFactoryRef() default "entityManagerFactory";

//  String transactionManagerRef() default "transactionManager";

  boolean considerNestedRepositories() default false;

  boolean enableDefaultTransactions() default false;
}
