package com.emc.documentum.springdata.repository.query;

import org.springframework.data.repository.query.RepositoryQuery;

import com.emc.documentum.springdata.core.DctmOperations;
import com.emc.documentum.springdata.repository.query.execution.CollectionExecution;
import com.emc.documentum.springdata.repository.query.execution.Execution;
import com.emc.documentum.springdata.repository.query.execution.ModifyingQuery;
import com.emc.documentum.springdata.repository.query.execution.SingleEntityExecution;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public abstract class AbstractDctmQuery implements RepositoryQuery {

  protected final DctmOperations dctmOperations;
  protected final DctmQueryMethod queryMethod;

  public AbstractDctmQuery(DctmOperations dctmOperations, DctmQueryMethod queryMethod) {
    this.dctmOperations = dctmOperations;
    this.queryMethod = queryMethod;
  }

  @Override
  public Object execute(Object[] parameters) {
    Execution execution = getExecution();
    DctmQuery query = createQuery(parameters);
    return execution.execute(query, dctmOperations, queryMethod.getEntityInformation().getJavaType());
  }

  protected abstract DctmQuery createQuery(Object[] parameters);

  private Execution getExecution() {
    if(queryMethod.isCollectionQuery()) {
      return new CollectionExecution();
    } else if(queryMethod.isModifyingQuery()) {
      return  new ModifyingQuery();
    } else {
      return new SingleEntityExecution();
    }
  }

  @Override
  public DctmQueryMethod getQueryMethod() {
    return queryMethod;
  }
}
