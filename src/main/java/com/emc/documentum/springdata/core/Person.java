package com.emc.documentum.springdata.core;

import org.springframework.data.annotation.TypeAlias;

import com.emc.documentum.springdata.core.mapping.DCTMObject;
import com.emc.documentum.springdata.core.mapping.Attribute;

@DCTMObject(repository="persons")
@TypeAlias("person")
public class Person {
	
	@Attribute("firstname")
	private String name;
    
	public Integer age;
    
	@Attribute("sex")
    private String gender;
    
	@Attribute("addr")
	public Address address;
	
	public Person(String name, Integer age, String gender)
	{
		this.name = name;
		this.age = age;
		this.gender = gender;
	}
	
 }

