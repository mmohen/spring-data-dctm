package com.emc.documentum.springdata.entitymanager.attributes;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;

import java.util.ArrayList;
import java.util.List;

	
public class BooleanListAttribute extends IterableAttribute<List<Boolean>> {

	    public BooleanListAttribute(java.lang.String name) {
	        super(name);
	    }

	    @Override
	    public List<Boolean> getValue(Object o) throws DfException {
	        List<Boolean> values = new ArrayList<Boolean>();
	        IDfTypedObject obj = (IDfTypedObject) o;
	        int size = obj.getValueCount(name);

	        for (int i = 0 ; i < size; i++)
	            values.add(obj.getRepeatingBoolean(name,i));

	        return values;
	    }
	    
		@Override
		public void setValue(IDfSysObject dctmObject, List<Object> valueToSet) throws DfException {
			
			dctmObject.removeAll(name);
			
	         for (int i = 0; i < valueToSet.size(); i++) {
	             dctmObject.setRepeatingBoolean(name, i, (Boolean) valueToSet.get(i));
	         }
		}

}
