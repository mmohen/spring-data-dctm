package com.emc.documentum.springdata.entitymanager.attributes;

import com.documentum.fc.common.DfException;

public abstract class Attribute<T> {

    protected int dfAttributeType;
    public String name;


    public int getDfAttributeType() {
        return dfAttributeType;
    }

    public Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


    public abstract T getValue(Object o) throws DfException;
}
