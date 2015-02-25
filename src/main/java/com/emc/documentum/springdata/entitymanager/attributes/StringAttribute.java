package com.emc.documentum.springdata.entitymanager.attributes;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import com.documentum.fc.client.IDfSysObject;

public class StringAttribute extends Attribute<String>{

	public StringAttribute(String name) {
		super(name);
	}

	
	public String getValue(Object o){
		try {
			return (String) PropertyUtils.getSimpleProperty(o, name);
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	

}

