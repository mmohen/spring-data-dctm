package com.emc.documentum.springdata.repository.query.execution;

import com.emc.documentum.springdata.core.DctmOperations;
import com.emc.documentum.springdata.repository.query.DctmQuery;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class SingleEntityExecution implements Execution {
  @Override
  public Object execute(DctmQuery query, DctmOperations dctmOperations, Class entityClass) {
    return null;
  }
}
