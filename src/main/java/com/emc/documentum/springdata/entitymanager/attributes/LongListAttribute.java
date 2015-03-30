package com.emc.documentum.springdata.entitymanager.attributes;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;

import java.util.ArrayList;
import java.util.List;
	
public class LongListAttribute extends IterableAttribute<List<Long>> {

	    public LongListAttribute(java.lang.String name) {
	        super(name);
	    }

	    @Override
	    public List<Long> getValue(Object o) throws DfException {
	        List<Long> values = new ArrayList<Long>();
	        IDfTypedObject obj = (IDfTypedObject) o;
	        int size = obj.getValueCount(name);

	        for (int i = 0 ; i < size; i++)
	            values.add(obj.getRepeatingLong(name,i));

	        return values;
	    }
	    
		@Override
		public void setValue(IDfSysObject dctmObject, List<Object> valueToSet) throws DfException {
			
			dctmObject.removeAll(name);
			
	         for (int i = 0; i < valueToSet.size(); i++) {
	        	 //TODO : no setRepeatingLong, this conversion works right?
	             dctmObject.setRepeatingDouble(name, i, (Double) valueToSet.get(i));
	         }
		}

}