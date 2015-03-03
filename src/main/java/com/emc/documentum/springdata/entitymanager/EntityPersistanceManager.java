package com.emc.documentum.springdata.entitymanager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.authentication.UserCredentials;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.DSTemplate;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.entitymanager.convert.DCTMToObjectConverter;
import com.emc.documentum.springdata.entitymanager.convert.ObjectToDCTMConverter;
import com.emc.documentum.springdata.entitymanager.mapping.MappingHandler;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;
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
			 ObjectToDCTMConverter dctmObjectConverter = new ObjectToDCTMConverter(objectToSave, dctmObject);
			 dctmObjectConverter.convert(mapping);
			 
			 dctmObject.save();
			 
		 } catch (DfException e) {
			 String msg = String.format("Object cannot be created for class %s. Exception: %s, %s.", objectToSave.getClass(), e.getClass(), e.getMessage());
             throw new DfException(msg, e);
		}
		  
	 }
	
	public <T> List<T> findAllObjects(Class<?> entityClass, String repoObjectName) throws DfException, InstantiationException, IllegalAccessException {
		try {
			
			 IDfSession session = documentum.getSession();
			 ArrayList<AttributeType> mapping = new MappingHandler(entityClass).getAttributeMappings();
			 List<T> list = new ArrayList<T>();
			 
			 IDfQuery query = new DfQuery();
			 String dql = "select * from " + repoObjectName;    // TODO: create a DQL Builder, introspect mapping and make efficient dql
			 query.setDQL(dql);
			 IDfCollection coll = query.execute(session, 0);
			 
			 
			 //Object objectInstance = entityClass.new
			 
			 while ( coll.next() ) 
			 {
				 Object objectInstance = entityClass.newInstance();
				 IDfTypedObject dctmObject = coll.getTypedObject();
				 DCTMToObjectConverter objectConverter = new DCTMToObjectConverter(objectInstance, dctmObject);
				 objectConverter.convert(mapping);
				 list.add((T) objectInstance);
			 }
			return list;
			 
		 } catch (DfException e) {
			 String msg = String.format("Object cannot be created for class %s. Exception: %s, %s.", entityClass, e.getClass(), e.getMessage());
            throw new DfException(msg, e);
		}
	}

// TODO: have a custom exception class and throw that exception everywhere
// TODO : Inject DCTMObjectConverter, MappingHander and Documentum

public static void main(String[] args) throws DfException, InstantiationException, IllegalAccessException {
	
	Documentum doc = new Documentum(new UserCredentials("dmadmin", "password"),"FPIRepo");
	DSTemplate m = new DSTemplate(doc);
	List<Person> list = m.findAll(Person.class);
	System.out.println(list.size());
	for (Person temp : list) {
		System.out.println(temp.getName());
		System.out.println(temp.get_id());
	}
}

}

