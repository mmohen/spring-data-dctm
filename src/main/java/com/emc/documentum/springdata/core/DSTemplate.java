package com.emc.documentum.springdata.core;

import java.util.List;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.entitymanager.EntityPersistanceManager;
import com.emc.documentum.springdata.entitymanager.EntityTypeHandler;




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
	  

}
