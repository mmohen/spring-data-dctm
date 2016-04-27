package com.emc.documentum.springdata.repository;

import com.emc.documentum.springdata.ApplicationConfig;
import com.emc.documentum.springdata.core.Documentum;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;

/**
 * Created by mukheg on 4/20/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public abstract class AbstractTest {

    @Autowired
    protected Documentum documentum;

    @PostConstruct
    public void setupDocumentum() {
        UserCredentials credentials = new UserCredentials("dmadmin", "demo.demo");
        String docBase = "corp";
        documentum.setDocBase(docBase);
        documentum.setCredentials(credentials);
    }
}
