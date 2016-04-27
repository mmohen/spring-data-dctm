package com.emc.documentum.springdata.repository.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.emc.documentum.springdata.ApplicationConfig;
import com.emc.documentum.springdata.Person;
import com.emc.documentum.springdata.PersonQueryDslRepository;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.log.AutowiredLogger;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class QueryDslDctmRepositoryTest {

  @Autowired
  PersonQueryDslRepository personQueryDslRepository;
  @Autowired
  private Documentum documentum;
  @AutowiredLogger
  private Logger logger = Logger.getLogger(SimpleDctmRepository.class);

  @After
  public void cleanUp() {
    logger.info("Deleting objects: ");
    Iterable<Person> createdObjects = personQueryDslRepository.findAll();
    for (Person createdObject : createdObjects) {
      logger.info(createdObject);
    }
    personQueryDslRepository.delete(createdObjects);
  }

  @PostConstruct
  public void setupDocumentum() {
    UserCredentials credentials = new UserCredentials("dmadmin", "demo.demo");
    String docBase = "corp";
    documentum.setDocBase(docBase);
    documentum.setCredentials(credentials);
  }

  @Test
  @Ignore("This is not the test you are looking for {Jedi hand wave}.")
  public void genericTest() {
/*
    QPerson person = new QPerson("Bruce");
    BooleanBuilder builder = new BooleanBuilder( );
    builder.and(person.name.eq("test")).and(person.name.eq("testsadfkljf;ajsdf"));
    System.out.println(builder.getValue());
*/
  }

  @Test
  public void testFindBySingleAttribute() throws Exception {
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    logger.info("Trying to save: " + bruceWayne);
    Person savedBruceWayne = personQueryDslRepository.save(bruceWayne);
    logger.info("Saved: " + savedBruceWayne);
    assertEquals("They are different people", bruceWayne, savedBruceWayne);

    List<Person> byName = personQueryDslRepository.findByName("Bruce Wayne");
    assertEquals(byName.get(0), bruceWayne);
  }

  @Test
  public void testFindByTwoAttributesUsingOr() throws Exception {
    //Create collection to save
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person peterParker = new Person("Peter Parker", 19, "male");
    Person barbaraGordon = new Person("Barbara Gordon", 28, "female");
    Map<String, Person> personMap = new HashMap<>();
    personMap.put("Bruce Wayne", bruceWayne);
    personMap.put("Barbara Gordon", barbaraGordon);

    List<Person> objectsForInsertion = Arrays.asList(bruceWayne, peterParker, barbaraGordon);

    //Do save
    personQueryDslRepository.save(objectsForInsertion);

    List<Person> personList = personQueryDslRepository.findByNameOrGender("Bruce Wayne", "female");
    assertEquals("Count mismatch", 2, personList.size());

    for (Person person : personList) {
      logger.info(String.format("Found: [%s]", person.toString()));
      assertNotNull(String.format("Couldn't find [%s]", person), personMap.get(person.getName()));
    }
  }

  @Test
  public void testFindByTwoAttributesUsingAnd() throws Exception {
    //Create collection to save
    Person bruceWayne = new Person("Bruce Wayne", 35, "male"); //The real Bruce Wayne
    Person thomasElliot = new Person("Bruce Wayne", 31, "male"); //Hush

    List<Person> objectsForInsertion = Arrays.asList(bruceWayne, thomasElliot);

    //Do save
    personQueryDslRepository.save(objectsForInsertion);
    List<Person> personList = personQueryDslRepository.findByNameAndAge("Bruce Wayne", 35);
    assertEquals("Count mismatch", 1, personList.size());
  }

  @Test
  @Ignore("Range queries not implemented")
  public void testFindInRange() throws Exception {
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person peterParker = new Person("Peter Parker", 19, "male");
    Person barbaraGordon = new Person("Barbara Gordon", 28, "female");
    Map<String, Person> personMap = new HashMap<>();
    personMap.put("Bruce Wayne", bruceWayne);
    personMap.put("Barbara Gordon", barbaraGordon);

    List<Person> objectsForInsertion = Arrays.asList(bruceWayne, peterParker, barbaraGordon);

    //Do save
    personQueryDslRepository.save(objectsForInsertion);

    List<Person> personList = personQueryDslRepository.findByAgeBetween(17, 20);
    assertEquals("Count mismatch", 2, personList.size());

    for (Person person : personList) {
      logger.info(String.format("Found: [%s]", person.toString()));
      assertNotNull(String.format("Unexpected person found [%s]", person), personMap.get(person.getName()));
    }
  }
}