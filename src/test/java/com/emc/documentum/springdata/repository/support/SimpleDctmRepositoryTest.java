package com.emc.documentum.springdata.repository.support;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    System.out.println("Deleting objects: ");
    Iterable<Person> createdObjects = personRepository.findAll();
    for (Person createdObject : createdObjects) {
      System.out.println(createdObject);
    }
    personRepository.delete(createdObjects);
  }

  @Test
  public void testSaveSingleObject() throws Exception {
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    System.out.println("Trying to save: " + bruceWayne);
    Person savedBruceWayne = personRepository.save(bruceWayne);
    System.out.println("Saved: " + savedBruceWayne);
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
    System.out.println("Saved the following objects to the repo: ");
    for (Person savedObject : savedObjects) {
      System.out.println(savedObject);
      createdObjectsList.add(savedObject);
    }

    assertArrayEquals("They are different people", objectsForInsertion.toArray(), createdObjectsList.toArray());
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
    System.out.println("Saved the following objects to the repo: ");
    for (Person savedObject : savedObjects) {
      System.out.println(savedObject);
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
    System.out.println("Trying to save: " + bruceWayne);
    Person savedBruceWayne = personRepository.save(bruceWayne);
    System.out.println("Saved: " + savedBruceWayne);

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
    System.out.println("Saved the following people: ");
    for (Person person : savedPeople) {
      System.out.println(person);
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
    System.out.println("Saved the following people: ");
    for (Person person : savedPeople) {
      System.out.println(person);
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