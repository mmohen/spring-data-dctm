package com.emc.documentum.springdata.core.tests;

import static org.junit.Assert.*;


import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.authentication.UserCredentials;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.DSTemplate;
import com.emc.documentum.springdata.core.Documentum;

public class DSTemplateTest {

	
	static DSTemplate template;
	static Person p;
	static Documentum doc;
	
	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		doc = new Documentum(new UserCredentials("dmadmin", "password"),"FPIRepo");
		template = new DSTemplate(doc);
		
	}

	@Test
	public void testInsert() throws DfException {
		
		p = new Person("Rohan",22,"Male");
		Person insertedPerson = template.create(p);
		assertEquals(insertedPerson.getName(),  p.getName());
		assertEquals(insertedPerson.getAge(), p.getAge());
		assertEquals(insertedPerson.getGender(), p.getGender());
	
	}

	@Test
	public void testFindById() throws DfException, InstantiationException, IllegalAccessException {
	
		p = new Person("John",67,"Male");
		Person insertedPerson = template.create(p);
		Person obj = template.findById(insertedPerson.get_id(), Person.class);
		assertEquals(obj.getName(),  p.getName());
		assertEquals(obj.getAge(), p.getAge());
		assertEquals(obj.getGender(), p.getGender());
	
	}
	
	@Test
	public void testFindByIdThrowsBadIDException() throws DfException, InstantiationException, IllegalAccessException {
	
		try {
				
			template.findById("this id doesn't exist", Person.class);
		
		}
		catch(DfException e) {
			
			expected.expect(DfException.class);
		
		}
	
	}

}
