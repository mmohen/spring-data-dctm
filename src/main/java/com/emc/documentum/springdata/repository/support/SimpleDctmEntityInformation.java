package com.emc.documentum.springdata.repository.support;

import java.io.Serializable;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class SimpleDctmEntityInformation <T, ID extends Serializable>  implements DctmEntityInformation<T, ID> {
//TODO: Adding fields & implementations on need basis

  private Class<T> javaType;

  public SimpleDctmEntityInformation(Class<T> javaType) {
    this.javaType = javaType;
  }

  @Override
  public String getDctmEntityName() {
    return null;
  }

  @Override
  public boolean isNew(T entity) {
    return false;
  }

  @Override
  public ID getId(T entity) {
    return null;
  }

  @Override
  public Class<ID> getIdType() {
    return null;
  }

  @Override
  public Class<T> getJavaType() {
    return javaType;
  }
}
