package com.emc.documentum.springdata.core;

import org.springframework.data.annotation.TypeAlias;

import com.emc.documentum.springdata.core.mapping.DCTMObject;
import com.emc.documentum.springdata.core.mapping.Field;

@DCTMObject(repository="persons")
@TypeAlias("person")
public class Person {
	
	@Field("firstname")
	private String name;
    
	private Integer age;
    
	@Field("sex")
    private String gender;
    
	private Address address;
 }

