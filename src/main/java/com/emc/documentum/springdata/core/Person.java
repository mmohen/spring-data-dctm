package com.emc.documentum.springdata.core;

import org.springframework.data.annotation.TypeAlias;

import com.emc.documentum.springdata.entitymanager.mapping.DCTMObject;
import com.emc.documentum.springdata.entitymanager.mapping.EntityField;

@DCTMObject(repository="persons")
@TypeAlias("person")
public class Person {
	
	@EntityField("firstname")
	private String name;

	public Integer age;
    
	@EntityField("sex")
    private String gender;
	
	public char type;
	public int test;
    
//	@EntityField("addr")
//	public Address address;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Person(String name, Integer age, String gender)
	{
		this.name = name;
		this.age = age;
		this.gender = gender;
	}
	
 }

