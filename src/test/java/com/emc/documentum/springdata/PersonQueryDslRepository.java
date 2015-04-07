package com.emc.documentum.springdata;

import java.util.List;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.emc.documentum.springdata.repository.DctmRepository;
import com.emc.documentum.springdata.repository.Query;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public interface PersonQueryDslRepository extends DctmRepository<Person, String>, QueryDslPredicateExecutor<Person> {

  List<Person> findByName(String name);

  List<Person> findByNameOrGender(String name, String gender);

//  public long countByName(String name);

  @Query("select * from persons where p.attributes[?1] = ?2")
  List<Person> findSomeone(String attribute, String value);

  List<Person> findByNameAndAge(String name, int age);

  List<Person> findByAgeBetween(int begin, int end);
}
