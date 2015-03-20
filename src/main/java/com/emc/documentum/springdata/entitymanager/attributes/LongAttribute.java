package com.emc.documentum.springdata.entitymanager.attributes;

import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;

public class LongAttribute extends Attribute<Long> {

    public LongAttribute(String name) {
        super(name);
        dfAttributeType = 5;
    }

    @Override
    public Long getValue(Object o) throws DfException {
        return ((IDfTypedObject) o).getLong(name);

    }
}