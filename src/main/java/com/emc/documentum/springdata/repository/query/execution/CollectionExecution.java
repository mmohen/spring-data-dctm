package com.emc.documentum.springdata.repository.query.execution;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.DctmOperations;
import com.emc.documentum.springdata.repository.query.DctmQuery;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class CollectionExecution implements Execution{
  @Override
  public Object execute(DctmQuery query, DctmOperations dctmOperations, Class entityClass) {
    try {
      return dctmOperations.find(query, entityClass);
    } catch (DfException e) {
      e.printStackTrace();
    }
    return null;
  }
}
