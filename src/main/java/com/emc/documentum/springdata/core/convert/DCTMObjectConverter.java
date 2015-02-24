package com.emc.documentum.springdata.core.convert;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.documentum.fc.client.IDfSysObject;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;

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
				String type = attributeType.getAttribute().getType().toString();
				
				if (type == "class java.lang.Integer")
				{
					System.out.println("Okay!");
				}
			
				System.out.println("HERE!!!" + type);
				
				//dctmObject.set(attributeType.attribute.getName(), field.get(this));
				System.out.print(attributeType.getFieldName() + " : ");
				System.out.println(attributeType.attribute.getType() + " , " + attributeType.attribute.getName()); 
			}
			catch(Exception e){
				System.out.println("here");
			}
		
		
	}
		

}
}
