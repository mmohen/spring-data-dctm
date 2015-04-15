package com.emc.documentum.springdata.entitymanager.attributes;

import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;

public class StringAttribute extends Attribute<String> {

    public StringAttribute(String name) {
        super(name);
        dfAttributeType = 2;
    }


    @Override
    public String getValue(Object o) throws DfException {
        return ((IDfTypedObject) o).getString(name);
    }
}

