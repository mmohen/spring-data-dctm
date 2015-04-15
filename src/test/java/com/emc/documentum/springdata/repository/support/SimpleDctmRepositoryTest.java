package com.emc.documentum.springdata.repository.support;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.emc.documentum.springdata.ApplicationConfig;
import com.emc.documentum.springdata.Person;
import com.emc.documentum.springdata.PersonRepository;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.log.AutowiredLogger;
import com.google.common.collect.Lists;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class SimpleDctmRepositoryTest {

  //Somehow IntelliJ cribs about this but the runtime manages to autowire these correctly
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private Documentum documentum;
  @AutowiredLogger
  private Logger logger = Logger.getLogger(SimpleDctmRepository.class);



  @PostConstruct
  public void setupDocumentum() {
    UserCredentials credentials = new UserCredentials("dmadmin", "password");
    String docBase = "FPIRepo";
    documentum.setDocBase(docBase);
    documentum.setCredentials(credentials);
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void cleanUp() {
    logger.info("Deleting objects: ");
    Iterable<Person> createdObjects = personRepository.findAll();
    for (Person createdObject : createdObjects) {
      logger.info(createdObject);
    }
    personRepository.delete(createdObjects);
  }

  @Test
  public void testSaveSingleObject() throws Exception {
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    logger.info("Trying to save: " + bruceWayne);
    Person savedBruceWayne = personRepository.save(bruceWayne);
    logger.info("Saved: " + savedBruceWayne);
    assertEquals("They are different people", bruceWayne, savedBruceWayne);
  }

  @Test
  public void testSaveCollection() throws Exception {
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person peterParker = new Person("Peter Parker", 19, "male");
    Person barbaraGordon = new Person("Barbara Gordon", 28, "female");
    List<Person> objectsForInsertion = Arrays.asList(bruceWayne, peterParker, barbaraGordon);

    Iterable<Person> savedObjects = personRepository.save(objectsForInsertion);
    List<Person> createdObjectsList = new LinkedList<>();
    logger.info("Saved the following objects to the repo: ");
    for (Person savedObject : savedObjects) {
      logger.info(savedObject);
      createdObjectsList.add(savedObject);
    }

    assertArrayEquals("They are different people", objectsForInsertion.toArray(), createdObjectsList.toArray());
  }

  @Test
  public void testUpdate() throws Exception {
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    logger.info("Trying to save: " + bruceWayne);
    Person savedBruceWayne = personRepository.save(bruceWayne);
    logger.info("Saved: " + savedBruceWayne);
    assertEquals("They are different people", bruceWayne, savedBruceWayne);

    bruceWayne.setName("Batman");
    String originalId = bruceWayne.get_id();

    personRepository.save(bruceWayne);
    Person savedPerson = personRepository.findOne(originalId);
    assertEquals("New object created", savedPerson.getName(), "Batman");
  }

  @Test
  public void testUpdateCollection() throws Exception {
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person peterParker = new Person("Peter Parker", 19, "male");
    Person barbaraGordon = new Person("Barbara Gordon", 28, "female");
    List<Person> objectsForInsertion = Arrays.asList(bruceWayne, peterParker, barbaraGordon);

    Iterable<Person> savedObjects = personRepository.save(objectsForInsertion);
    List<Person> createdObjectsList = new LinkedList<>();
    logger.info("Saved the following objects to the repo: ");
    for (Person savedObject : savedObjects) {
      logger.info(savedObject);
      createdObjectsList.add(savedObject);
    }

    assertArrayEquals("They are different people", objectsForInsertion.toArray(), createdObjectsList.toArray());

    String updatedName = "UPDATED NAME";

    for (Person savedObject : savedObjects) {
      savedObject.setName(updatedName);
    }
    Iterable<Person> updatedPersons = personRepository.save(savedObjects);
    for (Person updatedPerson : updatedPersons) {
      assertEquals("Name not set correctly", updatedName, updatedPerson.getName());
    }
  }

  @Test
  public void testFindOne() throws Exception {
    //Create collection to save
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person peterParker = new Person("Peter Parker", 19, "male");
    Person barbaraGordon = new Person("Barbara Gordon", 28, "female");
    List<Person> objectsForInsertion = Arrays.asList(bruceWayne, peterParker, barbaraGordon);

    //Do save
    Iterable<Person> savedObjects = personRepository.save(objectsForInsertion);

    //Check
    logger.info("Saved the following objects to the repo: ");
    for (Person savedObject : savedObjects) {
      logger.info(savedObject);
    }

    //Find
    Person foundPerson = personRepository.findOne(savedObjects.iterator().next().get_id());
    assertNotNull(foundPerson);
    boolean found = false;

    for (Person savedObject : savedObjects) {
      if(savedObject.equals(foundPerson)) {
        found = true;
        break;
      }
    }
    assertTrue("Intended person not found", found);
  }

  @Test
  public void testExists() throws Exception {
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    logger.info("Trying to save: " + bruceWayne);
    Person savedBruceWayne = personRepository.save(bruceWayne);
    logger.info("Saved: " + savedBruceWayne);

    assertTrue("Person doesn't exist", personRepository.exists(savedBruceWayne.get_id()));
  }

  @Test// TODO: Not sure how to reliably test this without depending on the table to be initially empty and only one thread running at a time
  public void testFindAll() throws Exception {
    //Create collection to save
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person peterParker = new Person("Peter Parker", 19, "male");
    Person barbaraGordon = new Person("Barbara Gordon", 28, "female");
    List<Person> objectsForInsertion = Arrays.asList(bruceWayne, peterParker, barbaraGordon);

    //Do save
    Iterable<Person> savedPeople = personRepository.save(objectsForInsertion);
    logger.info("Saved the following people: ");
    for (Person person : savedPeople) {
      logger.info(person);
    }

    //Add for cleanup
    Iterable<Person> everyone = personRepository.findAll();

    for (Person person : everyone) {
      assertTrue(objectsForInsertion.contains(person));
    }
  }

  @Test
  public void testFindAllByListOfIds() throws Exception {
    //Create collection to save
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person peterParker = new Person("Peter Parker", 19, "male");
    Person barbaraGordon = new Person("Barbara Gordon", 28, "female");
    List<Person> objectsForInsertion = Arrays.asList(bruceWayne, peterParker, barbaraGordon);

    //Do save
    Iterable<Person> savedPeople = personRepository.save(objectsForInsertion);
    List<String> savedIds = new LinkedList<>();
    logger.info("Saved the following people: ");
    for (Person person : savedPeople) {
      logger.info(person);
      savedIds.add(person.get_id());
    }

    //Add for cleanup
    Iterable<Person> foundObjects = personRepository.findAll(savedIds);
    int foundCount = 0;
    for (Person person : foundObjects) {
      foundCount++;
      assertTrue(objectsForInsertion.contains(person));
    }
    assertEquals("Size mismatch", foundCount, objectsForInsertion.size());
  }

  @Test
  public void testCount() throws Exception {
  //Create collection to save
  Person bruceWayne = new Person("Bruce Wayne", 35, "male");
  Person peterParker = new Person("Peter Parker", 19, "male");
  Person barbaraGordon = new Person("Barbara Gordon", 28, "female");
  List<Person> objectsForInsertion = Arrays.asList(bruceWayne, peterParker, barbaraGordon);

  //Do save
  personRepository.save(objectsForInsertion);
  long foundCount = personRepository.count();
      
  assertEquals("Count mismatch", foundCount, objectsForInsertion.size());

  }

  @Test
  public void testDeleteById() throws Exception {
    //Create collection to save
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person peterParker = new Person("Peter Parker", 19, "male");
    Person barbaraGordon = new Person("Barbara Gordon", 28, "female");
    List<Person> objectsForInsertion = Arrays.asList(bruceWayne, peterParker, barbaraGordon);

    //Do save
    Iterable<Person> savedPeople = personRepository.save(objectsForInsertion);
    String personToDeleteId = savedPeople.iterator().next().get_id();

    personRepository.delete(personToDeleteId);
    assertNull("Person found unexpectedly", personRepository.findOne(personToDeleteId));
  }

  @Test
  public void testDeleteByObject() throws Exception {
    //Create collection to save
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person peterParker = new Person("Peter Parker", 19, "male");
    Person barbaraGordon = new Person("Barbara Gordon", 28, "female");
    List<Person> objectsForInsertion = Arrays.asList(bruceWayne, peterParker, barbaraGordon);

    //Do save
    Iterable<Person> savedPeople = personRepository.save(objectsForInsertion);
    Person personToDelete = savedPeople.iterator().next();

    personRepository.delete(personToDelete);
    assertNull("Person found unexpectedly", personRepository.findOne(personToDelete.get_id()));
  }

  @Test
  public void testDeleteAll() throws Exception {

  }

  @Test
  public void testDeleteCollectionOfPeople() throws Exception {
    //Create collection to save
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person peterParker = new Person("Peter Parker", 19, "male");
    Person barbaraGordon = new Person("Barbara Gordon", 28, "female");
    List<Person> objectsForInsertion = Arrays.asList(bruceWayne, peterParker, barbaraGordon);

    //Do save
    Iterable<Person> savedPeople = personRepository.save(objectsForInsertion);
    List<String> savedPeopleIds = new LinkedList<>();
    for (Person person : savedPeople) {
      savedPeopleIds.add(person.get_id());
    }

    personRepository.delete(savedPeople);
    Iterable<Person> persons = personRepository.findAll(savedPeopleIds);
    assertFalse("Person found unexpectedly", persons.iterator().hasNext());
  }
}