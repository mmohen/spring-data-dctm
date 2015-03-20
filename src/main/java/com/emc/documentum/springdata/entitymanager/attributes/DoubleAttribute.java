package com.emc.documentum.springdata.entitymanager.attributes;

import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;

public class DoubleAttribute extends Attribute<Double> {

    public DoubleAttribute(String name) {
        super(name);
        dfAttributeType = 5;
    }

    @Override
    public Double getValue(Object o) throws DfException {
        return ((IDfTypedObject) o).getDouble(name);

    }
}
