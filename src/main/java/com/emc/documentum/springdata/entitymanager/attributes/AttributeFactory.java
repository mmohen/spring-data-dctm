package com.emc.documentum.springdata.entitymanager.attributes;

public class AttributeFactory {

	public static Attribute<?> getAttribute(Class<?> type, String attributeName) {
		if(type == java.lang.String.class)
			return new StringAttribute(attributeName);
		else if (type == java.lang.Integer.class){
			return new IntAttribute( attributeName);
		}
		return null;
	}

}
