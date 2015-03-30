package com.emc.documentum.springdata.entitymanager.attributes;

public class Attribute<T> {

    public String name;

    public Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public T getValue(Object o) {
        return null;
    }
}
