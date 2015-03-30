package com.emc.documentum.springdata.repository.config;

import java.util.Locale;

import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;

import com.emc.documentum.springdata.repository.support.DctmRepositoryFactoryBean;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class DctmRepositoryConfigExtension extends RepositoryConfigurationExtensionSupport {

  @Override
  public String getModuleName() {
    return "DCTM";
  }

  @Override
  public String getRepositoryFactoryClassName() {
    return DctmRepositoryFactoryBean.class.getName();
  }

  @Override
  protected String getModulePrefix() {
    return getModuleName().toLowerCase(Locale.US);
  }
}
