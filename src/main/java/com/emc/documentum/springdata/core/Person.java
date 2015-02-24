package com.emc.documentum.springdata.core;

import org.springframework.data.annotation.TypeAlias;

import com.emc.documentum.springdata.core.mapping.DCTMObject;
import com.emc.documentum.springdata.core.mapping.EntityField;

@DCTMObject(repository="persons")
@TypeAlias("person")
public class Person {
	
	@EntityField("firstname")
	private String name;
    
	public Integer age;
    
	@EntityField("sex")
    private String gender;
    
	@EntityField("addr")
	public Address address;
	
	public Person(String name, Integer age, String gender)
	{
		this.name = name;
		this.age = age;
		this.gender = gender;
	}
	
 }

