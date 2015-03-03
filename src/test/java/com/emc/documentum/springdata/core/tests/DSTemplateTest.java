package com.emc.documentum.springdata.core.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.authentication.UserCredentials;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.DSTemplate;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.core.GenericCache;

public class DSTemplateTest {

	
	static DSTemplate template;
	static Person p;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		ArrayList<String> arraylist = new ArrayList<String>();
		Documentum doc = new Documentum(new UserCredentials("dmadmin", "password"),"FPIRepo");
		template = new DSTemplate(doc);
		p = new Person("Rohan",22,"Male");
		
	}

	@Test
	public void testInsert() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindById() throws DfException {
		Object obj = template.findById(p.getId().toString(), Person.class);
		assertEquals(obj, p);
	}

}
