package com.emc.documentum.springdata.entitymanager.attributes;

import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ramanwalia
 * Date: 20/03/15
 * Time: 10:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringListAttribute extends Attribute<List<String>> {


    public StringListAttribute(java.lang.String name) {
        super(name);
    }

    @Override
    public List<String> getValue(Object o) throws DfException {
        List<String> values = new ArrayList<String>();
        IDfTypedObject obj = (IDfTypedObject)o;
        int size = obj.getValueCount(name);

        for (int i = 0 ; i < size; i++)
            values.add(obj.getRepeatingString(name,i));

        return values;
    }
}
