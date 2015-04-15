package com.emc.documentum.springdata.repository.support;

import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.DctmOperations;
import com.emc.documentum.springdata.repository.query.DctmQueryMethod;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class DctmContentQuery implements RepositoryQuery {

  private final DctmOperations dctmOperations;
  private final DctmQueryMethod queryMethod;
  ContentQueryType contentQueryType;

  public DctmContentQuery(DctmOperations dctmOperations, DctmQueryMethod queryMethod) {
    this.dctmOperations = dctmOperations;
    this.queryMethod = queryMethod;

    if(queryMethod.getName().equalsIgnoreCase("setcontent")) {
      contentQueryType = ContentQueryType.SET;
    } else if(queryMethod.getName().equalsIgnoreCase("getcontent")) {
      contentQueryType = ContentQueryType.GET;
    } else {
      throw new IllegalStateException();
    }
  }

  //TODO: Smelly code
  @Override
  public Object execute(Object[] parameters) {
    try {
      switch(contentQueryType) {
        case GET:
          return dctmOperations.getContent(parameters[0], (String)parameters[1]);

        case SET:
          dctmOperations.setContent(parameters[0], (String)parameters[1], (String)parameters[2]);
          return "SAVED";

        default:
          throw new UnsupportedOperationException("Cannot possibly execute this query");
      }
    } catch (DfException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public QueryMethod getQueryMethod() {
    return queryMethod;
  }

  private enum ContentQueryType {GET, SET}
}
