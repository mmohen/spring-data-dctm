package com.emc.documentum.springdata.repository.relation.mn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.emc.documentum.springdata.ApplicationConfig;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.log.AutowiredLogger;
import com.emc.documentum.springdata.repository.support.SimpleDctmRepository;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class ManyToManyRelationTest {

  public static final String RELATION_QUERY = "select * from dm_relation where relation_name=\'%s\' and parent_id=\'%s\'";
  //Somehow IntelliJ cribs about this but the runtime manages to autowire these correctly
  @Autowired
  private PersonRepositoryWithRelationMn personRepositoryWithRelationMn;
  @Autowired
  private AddressRepository addressRepository;
  @Autowired
  private Documentum documentum;
  @AutowiredLogger
  private Logger logger = Logger.getLogger(SimpleDctmRepository.class);

  @PostConstruct
  public void setupDocumentum() {
    UserCredentials credentials = new UserCredentials("dmadmin", "demo.demo");
    String docBase = "corp";
    documentum.setDocBase(docBase);
    documentum.setCredentials(credentials);
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void cleanUp() {
    logger.info("Deleting objects: ");
    Iterable<Person> createdObjects = personRepositoryWithRelationMn.findAll();
    for (Person createdObject : createdObjects) {
      logger.info(createdObject);
    }

    addressRepository.delete(addressRepository.findAll());
    personRepositoryWithRelationMn.delete(createdObjects);
  }

  @Test
  public void noOp() {
//Just to delete stuff
  }

  @Test
  @Ignore("Test to test tests")
  public void testFindAll() throws  Exception {
    Iterable<Person> all = personRepositoryWithRelationMn.findAll();
    System.out.println("=================");
    for (Person person : all) {
      System.out.println(person);
    }
    System.out.println("=================");
    System.out.println();
  }



  public void testManyToManyRelation() throws  Exception {
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person alfredPennyworth = new Person("Alfred Pennyworth", 63, "male");

    Address wayneManor = new Address("Wayne Street", "Gotham City", "DC");
    Address batCave = new Address("Classified", "Classified", "Classified");
    Address starLabs = new Address("S.T.A.R. Labs", "Austin", "Texas");

    List<Address> bruceWayneAddresses = Arrays.asList(wayneManor, batCave, starLabs);
    bruceWayne.setAddress(bruceWayneAddresses);
    alfredPennyworth.setAddress(Arrays.asList(wayneManor, batCave));

    personRepositoryWithRelationMn.save(bruceWayne);
    personRepositoryWithRelationMn.save(alfredPennyworth);

    Person foundBruceWayne = personRepositoryWithRelationMn.findOne(bruceWayne.get_id());
    assertEquals("Some addresses not found", bruceWayneAddresses.size(), foundBruceWayne.getAddress().size());

    int foundCount = bruceWayneAddresses.size();
    for (Address bruceWayneAddress : bruceWayneAddresses) {
      for (Address address : foundBruceWayne.getAddress()) {
        if(bruceWayneAddress.equals(address)) {
          foundCount--;
          break;
        }
      }
    }

    assertEquals("Some Addresses not found", 0, foundCount);
    Address savedManor = addressRepository.findOne(wayneManor.getId());
    Address savedBatCave = addressRepository.findOne(batCave.getId());
    Address savedStarLabs = addressRepository.findOne(starLabs.getId());
    assertEquals("Incorrect resident count", 2, savedManor.getResidents().size());
    assertEquals("Incorrect resident count", 2, savedBatCave.getResidents().size());
    assertEquals("Incorrect resident count", 1, savedStarLabs.getResidents().size());
  }

  @Ignore
  public void testUpdateRelation() throws  Exception {
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person alfredPennyworth = new Person("Alfred Pennyworth", 63, "male");

    Address wayneManor = new Address("Wayne Street", "Gotham City", "DC");
    Address batCave = new Address("Classified", "Classified", "Classified");
    Address starLabs = new Address("S.T.A.R. Labs", "Austin", "Texas");

    List<Address> bruceWayneAddresses = Arrays.asList(wayneManor, batCave, starLabs);

    personRepositoryWithRelationMn.save(bruceWayne);
    personRepositoryWithRelationMn.save(alfredPennyworth);

    Person foundBruceWayne = personRepositoryWithRelationMn.findOne(bruceWayne.get_id());
    assertTrue(foundBruceWayne.getAddress() == null || foundBruceWayne.getAddress().size() == 0);

    bruceWayne.setAddress(bruceWayneAddresses);
    alfredPennyworth.setAddress(Arrays.asList(wayneManor, batCave));

    personRepositoryWithRelationMn.save(bruceWayne);
    personRepositoryWithRelationMn.save(alfredPennyworth);

    foundBruceWayne = personRepositoryWithRelationMn.findOne(bruceWayne.get_id());
    assertEquals("Some addresses not found", bruceWayneAddresses.size(), foundBruceWayne.getAddress().size());

    int foundCount = bruceWayneAddresses.size();
    for (Address bruceWayneAddress : bruceWayneAddresses) {
      for (Address address : foundBruceWayne.getAddress()) {
        if(bruceWayneAddress.equals(address)) {
          foundCount--;
          break;
        }
      }
    }

    assertEquals("Some Addresses not found", 0, foundCount);
    Address savedManor = addressRepository.findOne(wayneManor.getId());
    Address savedBatCave = addressRepository.findOne(batCave.getId());
    Address savedStarLabs = addressRepository.findOne(starLabs.getId());
    assertEquals("Incorrect resident count", 2, savedManor.getResidents().size());
    assertEquals("Incorrect resident count", 2, savedBatCave.getResidents().size());
    assertEquals("Incorrect resident count", 1, savedStarLabs.getResidents().size());
  }


  public void testManyToManyRelationReverse() {
    Person bruceWayne = new Person("Bruce Wayne", 35, "male");
    Person alfredPennyworth = new Person("Alfred Pennyworth", 63, "male");

    Address wayneManor = new Address("Wayne Street", "Gotham City", "DC");
    Address batCave = new Address("Classified", "Classified", "Classified");
    Address starLabs = new Address("S.T.A.R. Labs", "Austin", "Texas");

    List<Address> bruceWayneAddresses = Arrays.asList(wayneManor, batCave, starLabs);

    wayneManor.setResidents(Arrays.asList(bruceWayne, alfredPennyworth));
    batCave.setResidents(Arrays.asList(bruceWayne, alfredPennyworth));
    starLabs.setResidents(Arrays.asList(bruceWayne));
    addressRepository.save(Arrays.asList(wayneManor, batCave, starLabs));

    Person foundBruceWayne = personRepositoryWithRelationMn.findOne(bruceWayne.get_id());
    assertEquals("Some addresses not found", bruceWayneAddresses.size(), foundBruceWayne.getAddress().size());

    int foundCount = bruceWayneAddresses.size();
    for (Address bruceWayneAddress : bruceWayneAddresses) {
      for (Address address : foundBruceWayne.getAddress()) {
        if(bruceWayneAddress.equals(address)) {
          foundCount--;
          break;
        }
      }
    }

    assertEquals("Some Addresses not found", 0, foundCount);
    Address savedManor = addressRepository.findOne(wayneManor.getId());
    Address savedBatCave = addressRepository.findOne(batCave.getId());
    Address savedStarLabs = addressRepository.findOne(starLabs.getId());
    assertEquals("Incorrect resident count", 2, savedManor.getResidents().size());
    assertEquals("Incorrect resident count", 2, savedBatCave.getResidents().size());
    assertEquals("Incorrect resident count", 1, savedStarLabs.getResidents().size());
  }
}
