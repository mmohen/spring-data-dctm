package com.emc.documentum.springdata.core;

import java.lang.reflect.InvocationTargetException;

import java.util.List;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.entitymanager.EntityPersistenceManager;
import com.emc.documentum.springdata.entitymanager.EntityTypeHandler;

import org.springframework.util.Assert;


public class DSTemplate implements IDSOperations {

    private final Documentum documentum;


    public DSTemplate(Documentum documentum){
        this.documentum = documentum;
    }

    public <T> T create(T objectToSave) throws DfException {
    	
    	Assert.notNull(objectToSave);

        String repoObjectName = getRepositoryObjectName(objectToSave);

        EntityPersistenceManager entityPersistenceManager = new EntityPersistenceManager(documentum); // TODO: inject the class
        return entityPersistenceManager.createObject(repoObjectName, objectToSave);

    }
    
    public <T> String delete(T objectToDelete) throws DfException {
    	
    	Assert.notNull(objectToDelete);

        String repoObjectName = getRepositoryObjectName(objectToDelete);

        EntityPersistenceManager entityPersistenceManager = new EntityPersistenceManager(documentum); // TODO: inject the class
        return entityPersistenceManager.deleteObject(repoObjectName, objectToDelete);

    }

    public <T> List<T> findAll(Class<T> entityClass) throws DfException{

    	Assert.notNull(entityClass);
        String repoObjectName = getRepositoryName(entityClass);

        EntityPersistenceManager entityPersistenceManager = new EntityPersistenceManager(documentum); // TODO: inject the class
        return entityPersistenceManager.findAllObjects(entityClass, repoObjectName);

    }

    public <T> T findById(String id, Class<T> entityClass) throws DfException, InstantiationException, IllegalAccessException {

    	Assert.notNull(id);
    	Assert.notNull(entityClass);
    	EntityPersistenceManager entityPersistenceManager = new EntityPersistenceManager(documentum);
    	return entityPersistenceManager.findById(id, entityClass);

    }

    public <T> T update(T objectToUpdate) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, DfException {
    	
    	EntityPersistenceManager entityPersistenceManager = new EntityPersistenceManager(documentum);
    	
    	return entityPersistenceManager.update(objectToUpdate);
    	
    }

    // private method ************

    private <T> String getRepositoryObjectName(T obj) {
        Assert.notNull(obj);

        return getRepositoryName(obj.getClass());
    }

    private String getRepositoryName(Class<?> entityClass) {
    	
    	Assert.notNull(entityClass);

        EntityTypeHandler entityTypeManager = new EntityTypeHandler(); // TODO: inject the class

        return entityTypeManager.getEntityObjectName(entityClass);

    }


}
