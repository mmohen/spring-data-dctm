package com.emc.documentum.springdata.entitymanager.attributes;

public class Attribute<T> {

	public String name;
	T type;
	
	 public Attribute(T type, String name) {
		 this.type = type;
		 this.name = name;
		 }
	
	public T getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}

}
