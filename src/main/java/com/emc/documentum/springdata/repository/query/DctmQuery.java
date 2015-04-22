package com.emc.documentum.springdata.repository.query;

import org.springframework.data.domain.Sort;

import com.mysema.query.types.Predicate;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class DctmQuery {

  private final Predicate predicate;
  private final String queryString;
  private final boolean isCompleteQuery;

  public DctmQuery(String queryString) {
    this(queryString, null, true);
  }

  public DctmQuery(Predicate predicate) {
    this(null, predicate, false);
  }

  private DctmQuery(String queryString, Predicate predicate, boolean isCompleteQuery) {
    this.queryString = queryString;
    this.predicate = predicate;
    this.isCompleteQuery = isCompleteQuery;
  }

  public DctmQuery with(Sort sort) {
    //TODO Implement query modifiers for sort
    return this;
  }

  public String getPredicate() {
    //TODO: There has got to be a better way to do this, I should ask the mysema guys.
    return predicate.toString().replaceAll("\\|\\|", "OR").replaceAll("\\&\\&", "AND");
  }

  public boolean isCompleteQuery() {
    return isCompleteQuery;
  }

  public String getQueryString() {
    return queryString;
  }
}
