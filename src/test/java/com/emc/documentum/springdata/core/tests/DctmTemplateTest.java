package com.emc.documentum.springdata.core.tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.Application;
import com.emc.documentum.springdata.core.DctmTemplate;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DctmTemplateTest {

	private Person p;

	@Autowired
	private Documentum dctm;
	
	@Autowired
	private DctmTemplate template;

	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

    @Before
    public void setUp()
    {
        dctm.setCredentials(new UserCredentials("dmadmin", "password"));
        dctm.setDocBase("FPIRepo");

    }

//	@Test
	public void testSpringConfiguration() throws DfException{
		Person p = new Person("rohan",21,"male");
		System.out.println(p.getName());
		System.out.println(template.findAll(Person.class).size());
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
	public void testUpdate() throws DfException{

		p = new Person("Adam",22,"Female");
		template.create(p);
		p.setGender("Male");
		Person updatedPerson = template.update(p);
		assertEquals(updatedPerson.getGender(), "Male");
	
	}
	
	@Test
	public void testFindById() throws DfException {
	
		p = new Person("John",67,"Male");
		Person insertedPerson = template.create(p);
		Person obj = template.findById(insertedPerson.get_id(), Person.class);
		assertEquals(obj.getName(),  p.getName());
		assertEquals(obj.getAge(), p.getAge());
		assertEquals(obj.getGender(), p.getGender());
	
	}
	
	@Test
	public void testForRepeatingAttributes() throws DfException {
		
		p = new Person("John",67,"Male");
        p.getAccountNumbers().add(new Double(1979869469));
        p.getAccountNumbers().add(new Double(1979869468));
		Person insertedPerson = template.create(p);
		Person obj = template.findById(insertedPerson.get_id(), Person.class);
		obj.getAccountNumbers().add(new Double(1979869467));
		obj.getHobbies().add("Reading");
		obj.getHobbies().add("Dancing");
		Person updatedObj = template.update(obj);
		assertEquals(updatedObj.getAccountNumbers().size(),3);
		assertEquals(updatedObj.getHobbies().size(),2);
	}
	
	
	@Test (expected = DfException.class)
	public void testFindByIdThrowsBadIDException() throws DfException {
			
			template.findById("this id doesn't exist", Person.class);	
	}
	
	@Test
	public void testFindAll() throws DfException{
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
