package com.emc.documentum.springdata.repository.support;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class QueryDslDctmRepository<T, ID extends Serializable> extends SimpleDctmRepository<T, ID> implements QueryDslPredicateExecutor<T> {

  public QueryDslDctmRepository(DctmEntityInformation<T, ID> dctmEntity) {
    super(dctmEntity);
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
