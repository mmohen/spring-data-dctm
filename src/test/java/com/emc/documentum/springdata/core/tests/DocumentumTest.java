package com.emc.documentum.springdata.core.tests;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.boot.SpringApplication;
import org.springframework.data.authentication.UserCredentials;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.Documentum;

public class DocumentumTest {
	
	private static UserCredentials credentials;
	private static Documentum doc;
	static String docBase;

	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		credentials = new UserCredentials("dmadmin", "password");
        docBase = "FPIRepo";
        doc = new Documentum(credentials, docBase);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
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
//		String docBase = "FPIRepo";
//		docWithoutHostAndPort.getSession(docBase);
//	}
//	catch(Exception e) {
//		
//		expected.expect(DfServiceException.class);
//	}
//	}
//	

	@Test (expected = DfException.class)
	public void testGetSessionThrowsDfIdentityException() throws DfException{
		
        String docBase = "FPIRepo";
        UserCredentials wrongCredentials = new UserCredentials("admin", "passwrd");
		Documentum docWithWrongCredentials = new Documentum(wrongCredentials, docBase);
		docWithWrongCredentials.getSession();
	}

	@Test
	public void testDocumentumUserCredentialsStringString() throws DfException {
        String docBase = "FPIRepo";
		Documentum docCreatedWithPrimaryHostAndPort = new Documentum(credentials, docBase, "10.31.157.9", "1589" );
		IDfSession session = docCreatedWithPrimaryHostAndPort.getSession();
		assertEquals(session.getDocbaseName(), docBase);
	}


}
