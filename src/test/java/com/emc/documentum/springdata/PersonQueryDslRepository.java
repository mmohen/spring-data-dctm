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

  @Query("select * from persons where %s = \'%s\'")
  List<Person> findSomeone(String attribute, String value);

  @Query("select * from persons where firstname = \'%s\' or age = %s")
  List<Person> findByAgeOrName(String name, int value);

  @Query("select * from persons where %s = %s")
  List<Person> findSomeone(String attribute, int value);

  List<Person> findByNameAndAge(String name, int age);

  List<Person> findByAgeBetween(int begin, int end);
}
