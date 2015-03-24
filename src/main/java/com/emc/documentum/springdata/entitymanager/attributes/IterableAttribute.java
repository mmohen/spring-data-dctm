package com.emc.documentum.springdata.entitymanager.attributes;

import org.apache.commons.beanutils.PropertyUtils;

public class IterableAttribute extends Attribute<Integer> {
	
	Class<?> iterableType;

    public IterableAttribute(String name, Class<?> iterableType) {
		super(name);
		this.iterableType = iterableType;
		// TODO Auto-generated constructor stub
	}

    @Override
    public Integer getValue(Object o) {
        try {
            return (Integer) PropertyUtils.getSimpleProperty(o, name);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}