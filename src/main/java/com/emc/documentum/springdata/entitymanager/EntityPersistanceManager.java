package com.emc.documentum.springdata.entitymanager;

import java.util.ArrayList;

import com.emc.documentum.springdata.core.GenericCache;

import org.springframework.data.authentication.UserCredentials;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.entitymanager.convert.DCTMObjectConverter;
import com.emc.documentum.springdata.entitymanager.mapping.MappingHandler;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;
import com.emc.documentum.springdata.core.*;
import com.emc.documentum.springdata.core.tests.Person;

public class EntityPersistanceManager {
	
	private final Documentum documentum;

	
	 public EntityPersistanceManager(Documentum documentum){
		 this.documentum = documentum;	 
	 }
	 
	public <T> void createObject(String repoObjectName,T objectToSave) throws DfException{
		 try {
		
			 IDfSysObject dctmObject = (IDfSysObject) documentum.getSession().newObject(repoObjectName);
			 
			 ArrayList<AttributeType> mapping = new MappingHandler(objectToSave).getAttributeMappings();
			 DCTMObjectConverter dctmObjectConverter = new DCTMObjectConverter(objectToSave, dctmObject);
			 dctmObjectConverter.convert(mapping);
			 
			 dctmObject.save();
			 
		 } catch (DfException e) {
			 String msg = String.format("Object cannot be created for class %s. Exception: %s, %s.", objectToSave.getClass(), e.getClass(), e.getMessage());
             throw new DfException(msg, e);
		}
		  
	 }

	public static void main(String[] args) throws DfException {
		GenericCache a = new GenericCache();
		ArrayList<String> l = new ArrayList<String>();
		Documentum doc = new Documentum(new UserCredentials("dmadmin", "password"),"FPIRepo");
		DSTemplate x = new DSTemplate(doc);
		Person p = new Person("Rohan",22,"Male");
		x.insert(p);
		System.out.println("FIRST INSTANCE ADDED");
		x.insert(p);

	}


}
	
	


// TODO: have a custom exception class and throw that exception everywhere
// TODO : Inject DCTMObjectConverter, MappingHander and Documentum

