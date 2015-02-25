package com.emc.documentum.springdata.core.convert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;


import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.entitymanager.attributes.Attribute;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;
import com.emc.documentum.springdata.entitymanager.attributes.IntAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.StringAttribute;

public class DCTMObjectConverter {
	
	public Object objectToSave;
	public IDfSysObject dctmObject;
	
	public DCTMObjectConverter(Object objectToSave, IDfSysObject dctmObject) {
		this.objectToSave = objectToSave;
		this.dctmObject = dctmObject;
	}

	public void convert(ArrayList<AttributeType> mapping) {
		for (AttributeType attributeType : mapping) {
			 try {
				 
				Field field = this.objectToSave.getClass().getDeclaredField(attributeType.getFieldName());
				
				setValue(dctmObject, objectToSave, attributeType);
			
				System.out.println("HERE!!!" );
				
				//dctmObject.set(attributeType.attribute.getName(), field.get(this));
				System.out.print(attributeType.getFieldName() + " : ");
				System.out.println(attributeType.attribute+ " , " + attributeType.attribute.getName()); 
			}
			catch(Exception e){
				e.printStackTrace();
			}
		
		
	}
		

}

	private void setValue(IDfSysObject dctmObject, Object objectToSave,
			AttributeType fieldType) throws DfException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		Attribute attributeType = fieldType.getAttribute();
		
		if(attributeType instanceof  StringAttribute){
			dctmObject.setString(fieldType.getAttribute().getName(), (String) PropertyUtils.getSimpleProperty(objectToSave, fieldType.getFieldName()) );
		} else if (attributeType instanceof IntAttribute){
			dctmObject.setInt(fieldType.getAttribute().getName(), (Integer)  PropertyUtils.getSimpleProperty(objectToSave, fieldType.getFieldName()));
		}
		
	}
}
