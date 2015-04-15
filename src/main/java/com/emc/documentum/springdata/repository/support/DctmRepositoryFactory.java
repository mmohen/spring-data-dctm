package com.emc.documentum.springdata.repository.support;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;

import com.emc.documentum.springdata.repository.DctmRepository;
import com.emc.documentum.springdata.repository.DctmRepositoryWithContent;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class DctmRepositoryFactory extends RepositoryFactorySupport {

  private final ApplicationContext applicationContext;

  public DctmRepositoryFactory(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public <T, ID extends Serializable> DctmEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
    return new SimpleDctmEntityInformation<>(domainClass);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Object getTargetRepository(RepositoryMetadata metadata) {
    DctmEntityInformation<?, Serializable> dctmEntityInformation = getEntityInformation(metadata.getDomainType());

    return getDctmRepository(metadata, dctmEntityInformation);
  }

  @SuppressWarnings("unchecked")
  private Object getDctmRepository(RepositoryMetadata metadata, DctmEntityInformation<?, Serializable> dctmEntityInformation) {
    Class<?> repositoryInterface = metadata.getRepositoryInterface();

    if(isQueryDslRepository(repositoryInterface)) {
      return DctmRepositoryWithContent.class.isAssignableFrom(repositoryInterface) ?
          new QueryDslDctmRepositoryWithContent<>(dctmEntityInformation, applicationContext)
          : new QueryDslDctmRepository<>(dctmEntityInformation, applicationContext);
    }
    else {
      return DctmRepositoryWithContent.class.isAssignableFrom(repositoryInterface) ?
          new SimpleDctmRepositoryWithContent<>(dctmEntityInformation, applicationContext)
          : new SimpleDctmRepository<>(dctmEntityInformation, applicationContext);
    }
  }

  @Override
  protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
    return isQueryDslRepository(metadata.getRepositoryInterface()) ? QueryDslDctmRepository.class : SimpleDctmRepository.class;
  }

  @Override
  protected QueryLookupStrategy getQueryLookupStrategy(QueryLookupStrategy.Key key, EvaluationContextProvider evaluationContextProvider) {
    return applicationContext.getBean(DctmQueryLookupStrategy.class);
  }

  private static boolean isQueryDslRepository(Class<?> repositoryInterface) {
    return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
  }
}
