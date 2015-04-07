package com.emc.documentum.springdata.repository;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@NoRepositoryBean
public interface DctmRepositoryWithContent <T, ID extends Serializable> extends DctmRepository<T, ID> {
  void setContent(T object, String contentType, String path);

  String getContent(T object, String path);
}
