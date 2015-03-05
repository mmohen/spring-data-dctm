package com.emc.documentum.springdata.core;

import java.util.ArrayList;
import java.util.List;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.emc.documentum.springdata.entitymanager.EntityPersistenceManager;
import com.emc.documentum.springdata.entitymanager.EntityTypeHandler;
import com.emc.documentum.springdata.entitymanager.mapping.MappingHandler;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;



import com.emc.documentum.springdata.entitymanager.convert.DCTMToObjectConverter;

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

    public <T> List<T> findAll(Class<T> entityClass) throws DfException, InstantiationException, IllegalAccessException {

    	Assert.notNull(entityClass);
        String repoObjectName = getRepositoryName(entityClass);

        EntityPersistenceManager entityPersistenceManager = new EntityPersistenceManager(documentum); // TODO: inject the class
        return entityPersistenceManager.findAllObjects(entityClass, repoObjectName);

    }

    public <T> T findById(String id, Class<T> entityClass) throws DfException, InstantiationException, IllegalAccessException {

    	Assert.notNull(id);
    	Assert.notNull(entityClass);
        return findById(id, entityClass, getRepositoryName(entityClass));

    }

    public <T> T findById(String id, Class<T> entityClass, String repoObjectName) throws DfException, InstantiationException, IllegalAccessException {

        try {
            IDfSession session = documentum.getSession();
            ArrayList<AttributeType> mapping = new MappingHandler(entityClass).getAttributeMappings();
            DfId dfid = new DfId(id);
            IDfSysObject dctmObject =  (IDfSysObject) session.getObject(dfid);
            T objectInstance = entityClass.newInstance();
            DCTMToObjectConverter objectConverter = new DCTMToObjectConverter(objectInstance, dctmObject);
            objectConverter.convert(mapping);
            return objectInstance;

        } catch (DfException e) {
            String msg = String.format("Exception occured for object with Id: %s class %s. Exception: %s, %s.", id, entityClass, e.getClass(), e.getMessage());
            throw new DfException(msg, e);
        }

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
