package com.emc.documentum.springdata.entitymanager;

import java.util.ArrayList;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.core.convert.DCTMObjectConverter;
import com.emc.documentum.springdata.core.mapping.MappingHandler;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;

public class EntityPersistanceManager {
	
	private final Documentum documentum;

	
	 public EntityPersistanceManager(Documentum documentum){
		 this.documentum = documentum;	 
	 }
	 
	public <T> void createObject(String repoObjectName,T objectToSave){
		 try {
		
			 IDfSysObject dctmObject = (IDfSysObject) documentum.getSession().newObject(repoObjectName);
			 
			 ArrayList<AttributeType> mapping = new MappingHandler(objectToSave).getAttributeMappings();
			 DCTMObjectConverter dctmObjectConverter = new DCTMObjectConverter(objectToSave, dctmObject);
			 dctmObjectConverter.convert(mapping);
			 
			 dctmObject.save();
			 
		 } catch (DfException e) {
			 String msg = String.format("Object cannot be created for class %s. Exception: %s, %s.", objectToSave.getClass(), e.getClass(), e.getMessage());
			 System.out.println(msg);
			 e.printStackTrace();
		}
		  
	 }
	 
}

