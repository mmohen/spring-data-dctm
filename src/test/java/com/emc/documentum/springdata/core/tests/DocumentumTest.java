package com.emc.documentum.springdata.core.tests;

import static org.junit.Assert.*;

import com.emc.documentum.springdata.core.Application;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.authentication.UserCredentials;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.Documentum;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DocumentumTest {
	
	private UserCredentials credentials;
    @Autowired
	private  Documentum doc;
	private String docBase;
    @Autowired
    private Documentum docWithWrongCredentials;

	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	

    @Before
    public void setup(){
        credentials = new UserCredentials("dmadmin", "demo.demo");
        docBase = "corp";
        doc.setDocBase(docBase);
        doc.setCredentials(credentials);
    }

	@Test
	public void testGetCredentials() throws DfException {
		assertEquals(doc.getCredentials(),credentials);
		
	}
	
	
	@Test 
	public void testGetSession() throws DfException {
		
			IDfSession session = doc.getSession();
			assertEquals(session.getDocbaseName(), docBase);
	}
	
	/**
	 * DfServiceException raised if no host and port information given either as constructor
	 * arguments or in dfc.properties
	 * @throws DfException 
	 */
//	@Test
//	public void testGetSessionThrowsDfServiceException() throws DfException {
//	try {
//		Documentum docWithoutHostAndPort = new Documentum(credentials);
//		String docBase = "corp";
//		docWithoutHostAndPort.getSession(docBase);
//	}
//	catch(Exception e) {
//		
//		expected.expect(DfServiceException.class);
//	}
//	}
//	

	@Test
	public void testGetSessionThrowsDfIdentityException() throws DfException{
		
        String docBase = "corp";
        UserCredentials wrongCredentials = new UserCredentials("admin", "passwrd");
        docWithWrongCredentials.setCredentials(wrongCredentials);
        docWithWrongCredentials.setDocBase(docBase);
		docWithWrongCredentials.getSession();
	}

	@Test
	public void testDocumentumUserCredentialsStringString() throws DfException {
        String docBase = "corp";
		Documentum docCreatedWithPrimaryHostAndPort = new Documentum(credentials, docBase, "10.31.157.9", "1589" );
		IDfSession session = docCreatedWithPrimaryHostAndPort.getSession();
		assertEquals(session.getDocbaseName(), docBase);
	}


}
