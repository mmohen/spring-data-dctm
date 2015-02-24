package com.emc.documentum.springdata.entitymanager;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.springframework.data.authentication.UserCredentials;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.DSTemplate;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.core.Person;
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
		
			 IDfSysObject dctmObject = (IDfSysObject) documentum.getSession().newObject("dm_sysobject");//repoObjectName);
			 
		 ArrayList<AttributeType> mapping = new MappingHandler(objectToSave).getAttributeMappings();
			DCTMObjectConverter dctmObjectConverter = new DCTMObjectConverter(objectToSave, dctmObject);
			dctmObjectConverter.convert(mapping);
			 
			 //dctmObject.save();
			 
			 
		 } catch (DfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
	 }
	 
	 public static void main(String[] args) throws DfException {
			
			Person p = new Person("Megha", new Integer(21),"Female");
			Documentum doc = new Documentum(new UserCredentials("dmadmin", "password"),"FPIRepo");
			DSTemplate template = new DSTemplate(doc);
			template.insert(p);
			
		}

	 
}

