package com.emc.documentum.springdata.entitymanager.attributes;

import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;

public class BooleanAttribute extends Attribute<Boolean> {

    public BooleanAttribute(String name) {
        super(name);
        dfAttributeType = 0;
    }

    @Override
    public Boolean getValue(Object o) throws DfException {
        return ((IDfTypedObject) o).getBoolean(name);
    }
}

