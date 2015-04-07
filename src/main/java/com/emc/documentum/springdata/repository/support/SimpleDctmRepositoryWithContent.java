package com.emc.documentum.springdata.repository.support;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.NoRepositoryBean;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.repository.DctmRepositoryWithContent;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@NoRepositoryBean
public class SimpleDctmRepositoryWithContent<T, ID extends Serializable> extends SimpleDctmRepository<T, ID> implements DctmRepositoryWithContent
                                                                                                                             <T, ID> {
  public SimpleDctmRepositoryWithContent(DctmEntityInformation<T, ID> dctmEntity, ApplicationContext applicationContext) {
    super(dctmEntity, applicationContext);
  }

  @Override
  public void setContent(T object, String contentType, String path) {
    try {
      dctmTemplate.setContent(object, contentType, path);
    } catch (DfException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getContent(T object, String path) {
    try {
      return dctmTemplate.getContent(object, path);
    } catch (DfException e) {
      e.printStackTrace();
    }
    return null;
  }
}
