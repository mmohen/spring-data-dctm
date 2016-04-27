package com.emc.documentum.springdata.repository.support;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
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
import com.emc.documentum.springdata.LoanRepository;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.core.Loan;
import com.emc.documentum.springdata.log.AutowiredLogger;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class DctmRepositoryWithContentTest {

  //Somehow IntelliJ cribs about this but the runtime manages to autowire these correctly
  @Autowired
  private LoanRepository loanRepository;
  @Autowired
  private Documentum documentum;
  @AutowiredLogger
  private Logger logger = Logger.getLogger(SimpleDctmRepository.class);

  private List<File> filesToClean = new LinkedList<>();

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
    Iterable<Loan> createdObjects = loanRepository.findAll();
    for (Loan createdObject : createdObjects) {
      logger.info(createdObject);
    }
    loanRepository.delete(createdObjects);
    for (File file : filesToClean) {
      if(!file.delete()) {
        System.out.println(String.format("Failed to delete: %s", file.getAbsolutePath()));
      }
    }
  }

  @Test
  public void testSetContentByObject() {
    Loan loan = new Loan(100000);
    Loan loanAsDctmObject = loanRepository.save(loan);
    System.out.println(String.format("Created loan {%s}", loanAsDctmObject));
    URL url = this.getClass().getResource("/sample.pdf");

    loanRepository.setContent(loan, "pdf", url.getPath());
    String path = loanRepository.getContent(loan, "returnedObject" + System.currentTimeMillis() + ".pdf");

    File returnedFile = new File(path);
    filesToClean.add(returnedFile);
    assertTrue(returnedFile.exists());
  }
}