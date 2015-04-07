package com.emc.documentum.springdata.repository.query;

import javax.management.Query;

import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.parser.PartTree;

import com.emc.documentum.springdata.core.DctmOperations;
import com.emc.documentum.springdata.entitymanager.mapping.MappingHandler;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class PartTreeDctmQuery extends AbstractDctmQuery {

  private final PartTree partTree;
  private final MappingHandler mappingHandler;

  public PartTreeDctmQuery(MappingHandler mappingHandler, DctmOperations dctmOperations, DctmQueryMethod queryMethod) {
    super(dctmOperations, queryMethod);
    this.mappingHandler = mappingHandler;
    String queryMethodName = queryMethod.getName();
    if(queryMethodName.equalsIgnoreCase("setContent") || queryMethodName.equalsIgnoreCase("getContent")) {
      queryMethodName = "content";
    }
    partTree = new PartTree(queryMethodName, queryMethod.getEntityInformation().getJavaType());
  }

  public DctmQuery createQuery(Object[] parameters) {
    DctmQueryCreator queryCreator = new DctmQueryCreator(mappingHandler, partTree, getQueryMethod(), new ParametersParameterAccessor(
        getQueryMethod().getParameters(), parameters));

    return queryCreator.createQuery();
  }
}
