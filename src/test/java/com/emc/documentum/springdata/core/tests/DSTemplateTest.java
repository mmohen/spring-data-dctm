package com.emc.documentum.springdata.core.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.data.authentication.UserCredentials;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.DSTemplate;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.core.GenericCache;

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
		IDfSysObject insertedObject = template.insert(p);	
		assertEquals(insertedObject.getString("firstname"),  p.getName());
		assertEquals((Integer)insertedObject.getInt("age"), p.getAge());
		assertEquals(insertedObject.getString("sex"), p.getGender());
	
	}

	@Test
	public void testFindById() throws DfException, InstantiationException, IllegalAccessException {
	
		p = new Person("John",67,"Male");
		IDfSysObject insertedObject = template.insert(p);	
		Object obj = template.findById(insertedObject.getObjectId().toString(), Person.class);
		assertEquals(insertedObject.getString("firstname"),  ((Person) obj).getName());
		assertEquals((Integer)insertedObject.getInt("age"), ((Person) obj).getAge());
		assertEquals(insertedObject.getString("sex"), ((Person) obj).getGender());
	
	}
	
	@Test
	public void testFindByIdThrowsBadIDException() throws DfException, InstantiationException, IllegalAccessException {
	
		try {
				
			template.findById("this id doesn't exist", Person.class);
		
		}
		catch(Exception e) {
			
			expected.expect(DfException.class);
		
		}
	
	}

}
