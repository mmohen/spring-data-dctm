package com.emc.documentum.springdata.repository.support;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.emc.documentum.springdata.Person;
import com.emc.documentum.springdata.PersonQueryDslRepository;
import com.emc.documentum.springdata.repository.AbstractTest;

public class StringBasedDctmQueryTest extends AbstractTest {

  @Autowired
  PersonQueryDslRepository personQueryDslRepository;

  @Before
  @After
  public void cleanUp() {
    System.out.println("Deleting objects: ");
    Iterable<Person> createdObjects = personQueryDslRepository.findAll();
    for (Person createdObject : createdObjects) {
      System.out.println(createdObject);
    }
    personQueryDslRepository.delete(createdObjects);
  }

  @Test
  public void testStringBasedQuery() throws Exception {
    Person wadeWilson = new Person("Wade Wilson", 73, "male");
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    List<Person> personsToSave = Arrays.asList(wadeWilson, bruceWayne);

    personQueryDslRepository.save(wadeWilson);
    personQueryDslRepository.save(bruceWayne);

    List<Person> foundPersons = personQueryDslRepository.findByAgeOrName("Bruce Wayne", 73);

    assertEquals("Incorrect Sizes found", 2, foundPersons.size());
    int foundCount = 0;
    System.out.println("===============");
    for (Person person : foundPersons) {
      for (Person foundPerson : personsToSave) {
        if(person.equals(foundPerson)) {
          foundCount++; break;
        }
      }
    }
    assertEquals("Some saved people not found", 2, foundCount);
    System.out.println("===============");
  }
}
