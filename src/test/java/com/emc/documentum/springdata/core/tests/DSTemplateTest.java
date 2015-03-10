package com.emc.documentum.springdata.core.tests;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
	public void testUpdate() throws DfException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		p = new Person("Adam",22,"Female");
		template.create(p);
		p.setGender("Male");
		Person updatedPerson = template.update(p);
		assertEquals(updatedPerson.getGender(), "Male");
	
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
	
	@Test (expected = DfException.class)
	public void testFindByIdThrowsBadIDException() throws DfException, InstantiationException, IllegalAccessException {
			
			template.findById("this id doesn't exist", Person.class);	
	}
	
	@Test
	public void testFindAll() throws InstantiationException, IllegalAccessException, DfException{
		List<Person> list1 = template.findAll(Person.class);
		p = new Person("Rohan",22,"Male");
		template.create(p);
		List<Person> list2 = template.findAll(Person.class);	
		assertEquals(list1.size() + 1,  list2.size());
	}
	
	@Test 
	public void testDeleteObject() throws DfException {
		p = new Person("Alisha",22,"Female");
		Person insertedPerson = template.create(p);
		List<Person> list1 = template.findAll(Person.class);
		String id = insertedPerson.get_id();
		String deletedPersonId = template.delete(insertedPerson);
		List<Person> list2 = template.findAll(Person.class);
		assertEquals(id, deletedPersonId);
		assertEquals(list1.size() - 1, list2.size());
	}
	
	

}
