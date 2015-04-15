package com.emc.documentum.springdata.repository.support;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.emc.documentum.springdata.repository.DctmRepositoryWithContent;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@NoRepositoryBean
public class QueryDslDctmRepositoryWithContent<T, ID extends Serializable> extends SimpleDctmRepositoryWithContent<T, ID>
    implements QueryDslPredicateExecutor<T> {

  public QueryDslDctmRepositoryWithContent(DctmEntityInformation<T, ID> dctmEntity, ApplicationContext applicationContext) {
    super(dctmEntity, applicationContext);
  }

  @Override
  public T findOne(Predicate predicate) {
    return null;
  }

  @Override
  public Iterable<T> findAll(Predicate predicate) {
    return null;
  }

  @Override
  public Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
    return null;
  }

  @Override
  public Page<T> findAll(Predicate predicate, Pageable pageable) {
    return null;
  }

  @Override
  public long count(Predicate predicate) {
    return 0;
  }
}
