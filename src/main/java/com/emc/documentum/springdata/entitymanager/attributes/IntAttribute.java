package com.emc.documentum.springdata.entitymanager.attributes;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

public class IntAttribute extends Attribute<Integer> {

    public IntAttribute(String name) {
        super(name);
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
