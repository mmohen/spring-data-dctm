package com.emc.documentum.springdata.entitymanager.attributes;

import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;

public class IntAttribute extends Attribute<Integer> {

    public IntAttribute(String name) {
        super(name);
        dfAttributeType = 1;
    }

    @Override
    public Integer getValue(Object o) throws DfException {
        return ((IDfTypedObject) o).getInt(name);
    }
}
