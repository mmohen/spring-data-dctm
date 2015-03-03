package com.emc.documentum.springdata.core;

import java.util.ArrayList;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.emc.documentum.springdata.entitymanager.EntityPersistanceManager;
import com.emc.documentum.springdata.entitymanager.EntityTypeHandler;
import com.emc.documentum.springdata.entitymanager.mapping.MappingHandler;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;



import org.springframework.util.Assert;


public class DSTemplate implements IDSOperations {
	
	private final Documentum documentum;

	
	 public DSTemplate(Documentum documentum){
		 this.documentum = documentum;	 
	 }
	 
	 
	 public void insert(Object objectToSave) throws DfException {
//		 ensureNotIterable(objectToSave);
		 
		 String repoObjectName = getRepositoryObjectName(objectToSave);
		 
		 EntityPersistanceManager entityPersitanceManager = new EntityPersistanceManager(documentum); // TODO: inject the class 
		 entityPersitanceManager.createObject(repoObjectName, objectToSave);
		 
//		 insert(objectToSave, determineEntityRepositoryName(objectToSave));
	 }
	 
	 public <T> List<T> findAll(Class<T> entityClass) throws DfException, InstantiationException, IllegalAccessException {
		 String repoObjectName = determineRepositoryName(entityClass);
		 
		 EntityPersistanceManager entityPersitanceManager = new EntityPersistanceManager(documentum); // TODO: inject the class 
		 return entityPersitanceManager.findAllObjects(entityClass, repoObjectName);
	
	 }
	 
	 
	 private <T> String getRepositoryObjectName(T obj) {
		Assert.notNull(obj);
		 
		return determineRepositoryName(obj.getClass());
		

	}

	private String determineRepositoryName(Class<?> entityClass) {
		
		EntityTypeHandler entityTypeManager = new EntityTypeHandler(); // TODO: inject the class 
		
		return entityTypeManager.getEntityObjectName(entityClass);
	
	}
		
	public Object findById(String id, Class<?> entityClass) throws DfException {
		
		return findById(id, entityClass, determineRepositoryName(entityClass));
	
	}
	
	public Object findById(String id, Class<?> entityClass, String repoObjectName) throws DfException{
		
		try {
			
			 IDfSession session = documentum.getSession();
			 ArrayList<AttributeType> mapping = new MappingHandler(entityClass).getAttributeMappings();
			 DfId dfid = new DfId(id);
			 IDfSysObject obj =  (IDfSysObject) session.getObject(dfid);

		     //ToDo: use converter to convert into DCTM object 
			 
		 } catch (DfException e) {
			 String msg = String.format("Exception occured for object with Id: %s class %s. Exception: %s, %s.", id, entityClass, e.getClass(), e.getMessage());
           throw new DfException(msg, e);
		}
		
		return null;
	
	}
	
	  

}
