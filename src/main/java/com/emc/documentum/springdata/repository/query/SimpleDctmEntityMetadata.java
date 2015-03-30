package com.emc.documentum.springdata.repository.query;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class SimpleDctmEntityMetadata<T> implements DctmEntityMetadata<T> {

  private final Class<T> type;

  public SimpleDctmEntityMetadata(Class<T> type) {
    this.type = type;
  }

  @Override
  public String getDctmEntityName() {
    return null;
  }

  @Override
  public Class<T> getJavaType() {
    return type;
  }
}
