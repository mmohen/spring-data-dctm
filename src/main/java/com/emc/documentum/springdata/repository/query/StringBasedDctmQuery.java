package com.emc.documentum.springdata.repository.query;

import javax.management.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.stereotype.Component;

import com.emc.documentum.springdata.core.DctmOperations;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */

/**
 * Used when the Query annotation is present
 */
@Component()
@Scope("prototype")
@Lazy
public class StringBasedDctmQuery extends AbstractDctmQuery {

  @Autowired
  public StringBasedDctmQuery(DctmOperations dctmOperations, DctmQueryMethod queryMethod) {
    super(dctmOperations, queryMethod);
  }

  @Override
  protected DctmQuery createQuery(Object[] parameters) {
    return null;
  }

}
