package com.emc.documentum.springdata.repository.query;

import org.springframework.data.domain.Sort;

import com.mysema.query.types.Predicate;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class DctmQuery {

  private Predicate predicate;

  public DctmQuery(Predicate predicate) {
    this.predicate = predicate;
  }

  public DctmQuery with(Sort sort) {
    //TODO Implement query modifiers for sort
    return this;
  }

  public String getPredicate() {
    //TODO: There has got to be a better way to do this, I should ask the mysema guys.
    return predicate.toString().replaceAll("\\|\\|", "OR").replaceAll("\\&\\&", "AND");
  }


}
