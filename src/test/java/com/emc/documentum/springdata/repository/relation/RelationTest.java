package com.emc.documentum.springdata.repository.relation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfPersistentObject;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.emc.documentum.springdata.*;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.core.Loan;
import com.emc.documentum.springdata.log.AutowiredLogger;
import com.emc.documentum.springdata.repository.support.SimpleDctmRepository;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */

public class RelationTest {

  public static final String RELATION_QUERY = "select * from dm_relation where relation_name=\'%s\' and parent_id=\'%s\'";
  //Somehow IntelliJ cribs about this but the runtime manages to autowire these correctly
  @Autowired
  private PersonRepositoryWithRelation personRepository;
  @Autowired
  private LoanRepository loanRepository;
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

  public void cleanUp() {
    logger.info("Deleting objects: ");
    Iterable<Person> createdObjects = personRepository.findAll();
    for (Person createdObject : createdObjects) {
      logger.info(createdObject);
    }
    loanRepository.delete(loanRepository.findAll());
    personRepository.delete(createdObjects);
  }


  @Ignore("Test to test tests")
  public void testFindAll() throws  Exception {
    Iterable<Person> all = personRepository.findAll();
    System.out.println("=================");
    for (Person person : all) {
      System.out.println(person);
    }
    System.out.println("=================");
    System.out.println();
  }

  @Ignore
  public void testSaveObjectWithoutSavingRelation() {
    Person person = new Person("Peter Parker", 19, "male");
    personRepository.save(person);
    assertNotNull("Base object not saved " + person, person.get_id());
    Person personDctmObject = personRepository.findOne(person.get_id());
    assertEquals("Object not found in repository", person.get_id(), personDctmObject.get_id());
    assertNull(personDctmObject.getAddress());
    assertTrue(personDctmObject.getLoans().size() == 0);
  }

  @Ignore
  public void testSaveObjectWithOneToOneRelation() throws Exception {
    Person person = new Person("Peter Parker", 19, "male");
    Address address = new Address("SomeStreet", "SomeCity", "SomeCountry");
    person.setAddress(address);
    personRepository.save(person);
    assertNotNull("Related object not saved " + address, address.getId());
    assertNotNull("Base object not saved " + person, person.get_id());
    verifyOneToOneRelation(person, address);
  }

  @Ignore
  public void testUpdateOneToOneRelation() throws DfException {
    Person person = new Person("Peter Parker", 19, "male");
    Address address = new Address("SomeStreet", "SomeCity", "SomeCountry");
    personRepository.save(person);
    person.setAddress(address);
    personRepository.save(person);
    assertNotNull("Related object not saved " + address, address.getId());
    assertNotNull("Base object not saved " + person, person.get_id());
    verifyOneToOneRelation(person, address);
  }

  private void verifyOneToOneRelation(Person person, Address address) throws DfException {
    String queryString = String.format(RELATION_QUERY, "address", person.get_id());
    IDfQuery query = new DfQuery(queryString);
    IDfSession session = documentum.getSession();

    IDfCollection children = query.execute(session, 0);

    assertTrue("No related objects found", children.next());
    IDfTypedObject child = children.getTypedObject();
    IDfPersistentObject childObject = session.getObject(new DfId(child.getString("child_id")));
    assertTrue(address.getId().equalsIgnoreCase(childObject.getObjectId().toString()));
  }

  @Ignore
  public void testSaveObjectWithOneToManyRelation() throws Exception {
    Person person = new Person("Peter Parker", 19, "male");
    Loan loan = new Loan(100000);
    Loan loan2 = new Loan(200000);
    List<Loan> loans = Arrays.asList(loan, loan2);
    person.setLoans(loans);

    personRepository.save(person);
    assertNotNull("Related object not saved " + loan, loan.getLoanId());
    assertNotNull("Related object not saved " + loan2, loan2.getLoanId());
    assertNotNull("Base object not saved " + person, person.get_id());
    verifyOneToManyRelation(person, loans);
  }

  private void verifyOneToManyRelation(Person person, List<Loan> loans) throws DfException {
    String queryString = String.format(RELATION_QUERY, "liabilities", person.get_id());
    IDfQuery query = new DfQuery(queryString);
    IDfSession session = documentum.getSession();

    IDfCollection children = query.execute(session, 0);

    while(children.next()) {
      IDfTypedObject child = children.getTypedObject();
      IDfPersistentObject childObject = session.getObject(new DfId(child.getString("child_id")));

      boolean found = false;
      for (Loan loan : loans) {
        if(loan.getLoanId().equalsIgnoreCase(childObject.getObjectId().toString())) {
          found = true;
          break;
        }
      }
      if(!found){
        fail("Relation not created ");
      }
    }
  }
}
