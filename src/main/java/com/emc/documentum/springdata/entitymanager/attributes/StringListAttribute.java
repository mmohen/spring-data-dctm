package com.emc.documentum.springdata.entitymanager.attributes;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;

import java.util.ArrayList;
import java.util.List;

public class StringListAttribute extends IterableAttribute<List<String>> {
<<<<<<< HEAD
=======

>>>>>>> Added all ListTypes and SetValue Method for String, yet to test

    public StringListAttribute(java.lang.String name) {
        super(name);
        dfAttributeType = 2;
    }

    @Override
    public List<String> getValue(Object o) throws DfException {
        List<String> values = new ArrayList<String>();
        IDfTypedObject obj = (IDfTypedObject) o;
        int size = obj.getValueCount(name);

        for (int i = 0 ; i < size; i++)
            values.add(obj.getRepeatingString(name,i));

        return values;
    }

	@Override
	public void setValue(IDfSysObject dctmObject, List<Object> valueToSet) throws DfException {
		
		dctmObject.removeAll(name);
		
         for (int i = 0; i < valueToSet.size(); i++) {
             dctmObject.setRepeatingString(name, i, (String) valueToSet.get(i));
         }
	}
}
