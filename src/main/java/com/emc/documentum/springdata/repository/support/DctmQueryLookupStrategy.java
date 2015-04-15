package com.emc.documentum.springdata.repository.support;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.emc.documentum.springdata.core.DctmOperations;
import com.emc.documentum.springdata.entitymanager.mapping.MappingHandler;
import com.emc.documentum.springdata.repository.DctmRepositoryWithContent;
import com.emc.documentum.springdata.repository.Query;
import com.emc.documentum.springdata.repository.query.DctmQueryMethod;
import com.emc.documentum.springdata.repository.query.PartTreeDctmQuery;
import com.emc.documentum.springdata.repository.query.StringBasedDctmQuery;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@Component
public class DctmQueryLookupStrategy implements QueryLookupStrategy {
  @Autowired
  ApplicationContext applicationContext;

  Logger logger = Logger.getLogger(DctmQueryLookupStrategy.class);

  @Override
  public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, NamedQueries namedQueries) {
    DctmQueryMethod queryMethod = new DctmQueryMethod(method, metadata);

    DctmOperations dctmOperations = applicationContext.getBean(DctmOperations.class);


    if(DctmRepositoryWithContent.class.isAssignableFrom(metadata.getRepositoryInterface())) {
        if(isContentMethod(method)) {
          return new DctmContentQuery(dctmOperations, queryMethod);
        }
    }

    return method.getAnnotation(Query.class) == null ? new PartTreeDctmQuery(applicationContext.getBean(MappingHandler.class), dctmOperations,
                                                                             queryMethod) :
        new StringBasedDctmQuery(dctmOperations, queryMethod);
  }

  private boolean isContentMethod(Method method) {
    //TODO Refine this with annotations!!!
    return method.getName().equalsIgnoreCase("setcontent") || method.getName().equalsIgnoreCase("getcontent");
  }
}
