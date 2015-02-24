package com.emc.documentum.springdata.entitymanager.attributes;

public class AttributeType {

	public String fieldName;
	public Attribute<?> attribute;
	
	public AttributeType(String fieldName, Attribute<?> attribute) {
		
		this.fieldName = fieldName;
		this.attribute = attribute;
		
	}

	public String getFieldName() {
		return fieldName;
	}

	public Attribute<?> getAttribute() {
		return attribute;
	}
	

}
