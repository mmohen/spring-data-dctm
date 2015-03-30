package com.emc.documentum.springdata.entitymanager.attributes;

import java.util.List;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;

public abstract class IterableAttribute<T> extends Attribute<T> {
	
	public IterableAttribute(String name) {
	       super(name);
	}
	 
	public abstract void setValue(IDfSysObject dctmObject, List<Object> valueToSet) throws DfException;

}
