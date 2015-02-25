package com.emc.documentum.springdata.core.convert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.authentication.UserCredentials;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.core.Person;
import com.emc.documentum.springdata.entitymanager.attributes.Attribute;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeFactory;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;
import com.emc.documentum.springdata.entitymanager.attributes.BooleanAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.ByteAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.CharacterAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.DoubleAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.FloatAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.IntAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.LongAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.ShortAttribute;
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
				setValue(dctmObject, objectToSave, attributeType);
			}
			catch(Exception e){
				String msg = String.format("Conversion failed for Object of class %s. " + "Exception: %s, %s.", 
						objectToSave.getClass(), e.getClass(), e.getMessage());
				System.out.println(msg);
				e.printStackTrace();
			}
	}
}

	private void setValue(IDfSysObject dctmObject, Object objectToSave, AttributeType fieldType) 
			throws DfException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		Attribute<?> attributeType = fieldType.getAttribute();
		Object valueFromClass = PropertyUtils.getSimpleProperty(objectToSave, fieldType.getFieldName());
		
		if(attributeType instanceof  StringAttribute){
			dctmObject.setString(fieldType.getAttribute().getName(), (String) valueFromClass);
		} 
		else if (attributeType instanceof IntAttribute){
			dctmObject.setInt(fieldType.getAttribute().getName(), (Integer) valueFromClass);
		} 
		else if (attributeType instanceof DoubleAttribute){
			dctmObject.setDouble(fieldType.getAttribute().getName(), (Double) valueFromClass);
		} 
		else if (attributeType instanceof LongAttribute){
			dctmObject.setDouble(fieldType.getAttribute().getName(), (Long) valueFromClass);
		} 
		else if (attributeType instanceof ShortAttribute){
			dctmObject.setInt(fieldType.getAttribute().getName(), (Short) valueFromClass);
		} 
		else if (attributeType instanceof FloatAttribute){
			dctmObject.setDouble(fieldType.getAttribute().getName(), (Float) valueFromClass);
		} 
		else if (attributeType instanceof ByteAttribute){
			dctmObject.setString(fieldType.getAttribute().getName(), (Byte) valueFromClass);
		} 
		else if (attributeType instanceof BooleanAttribute){
			dctmObject.setBoolean(fieldType.getAttribute().getName(), (Boolean) valueFromClass);
		} 
		else if (attributeType instanceof CharacterAttribute){
			dctmObject.setString(fieldType.getAttribute().getName(), (Character) valueFromClass);
		}
		
	}
	
}
