package com.emc.documentum.springdata.core;

import java.lang.reflect.InvocationTargetException;

import java.util.List;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.entitymanager.EntityPersistenceManager;
import com.emc.documentum.springdata.entitymanager.EntityTypeHandler;

import org.springframework.util.Assert;


public class DSTemplate implements IDSOperations {

    private final Documentum documentum;
    private EntityPersistenceManager entityPersistenceManager;


    public DSTemplate(Documentum documentum){
        this.documentum = documentum;
        entityPersistenceManager = new EntityPersistenceManager(this.documentum);
    }

    public <T> T create(T objectToSave) throws DfException {
    	
    	Assert.notNull(objectToSave);

        String repoObjectName = getRepositoryObjectName(objectToSave);
        return entityPersistenceManager.createObject(repoObjectName, objectToSave);

    }
    
    public <T> String delete(T objectToDelete) throws DfException {
    	
    	Assert.notNull(objectToDelete);

        String repoObjectName = getRepositoryObjectName(objectToDelete);
        return entityPersistenceManager.deleteObject(repoObjectName, objectToDelete);

    }

    public <T> List<T> findAll(Class<T> entityClass) throws DfException{

    	Assert.notNull(entityClass);
        String repoObjectName = getRepositoryName(entityClass);
        return entityPersistenceManager.findAllObjects(entityClass, repoObjectName);

    }

    public <T> T findById(String id, Class<T> entityClass) throws DfException, InstantiationException, IllegalAccessException {

    	Assert.notNull(id);
    	Assert.notNull(entityClass);
    	return entityPersistenceManager.findById(id, entityClass);

    }

    public <T> T update(T objectToUpdate) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, DfException {
    	
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
