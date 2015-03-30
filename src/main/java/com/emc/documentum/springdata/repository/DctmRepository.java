package com.emc.documentum.springdata.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */

@NoRepositoryBean
public interface DctmRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {

  <S extends T> S save(S entity);

  <S extends T> Iterable<S> save(Iterable<S> entities);

  T findOne(ID id);

  boolean exists(ID id);

  Iterable<T> findAll();

  Iterable<T> findAll(Iterable<ID> ids);

  long count();

  void delete(ID id);

  void delete(T entity);

  void delete(Iterable<? extends T> entities);

  void deleteAll();
}
