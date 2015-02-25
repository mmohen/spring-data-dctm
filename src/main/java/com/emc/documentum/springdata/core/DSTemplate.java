package com.emc.documentum.springdata.core;

import com.emc.documentum.springdata.entitymanager.EntityPersistanceManager;
import com.emc.documentum.springdata.entitymanager.EntityTypeHandler;


import org.springframework.util.Assert;


public class DSTemplate implements IDSOperations {
	
	private final Documentum documentum;

	
	 public DSTemplate(Documentum documentum){
		 this.documentum = documentum;	 
	 }
	 
	 
	 public void insert(Object objectToSave) {
//		 ensureNotIterable(objectToSave);
		 
		 String repoObjectName = getRepositoryObjectName(objectToSave);
		 
		 EntityPersistanceManager entityPersitanceManager = new EntityPersistanceManager(documentum);
		 entityPersitanceManager.createObject(repoObjectName, objectToSave);
		 
//		 insert(objectToSave, determineEntityRepositoryName(objectToSave));
	 }
	 
	 
	 private <T> String getRepositoryObjectName(T obj) {
		Assert.notNull(obj);
		 
		return determineRepositoryName(obj.getClass());
		

	}

	private String determineRepositoryName(Class<?> entityClass) {
		
		EntityTypeHandler entityTypeManager = new EntityTypeHandler(); // TODO: inject the class 
		
		return entityTypeManager.getEntityObjectName(entityClass);
	
	}
	  

}
