package com.emc.documentum.springdata.entitymanager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.entitymanager.convert.DCTMToObjectConverter;
import com.emc.documentum.springdata.entitymanager.convert.ObjectToDCTMConverter;
import com.emc.documentum.springdata.entitymanager.mapping.MappingHandler;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;

public class EntityPersistenceManager {

    private final Documentum documentum;
   
    @Autowired
    private MappingHandler mappingHandler;
    private ObjectToDCTMConverter objectToDCTMConverter;
    private DCTMToObjectConverter dctmToObjectConverter;
    


    public EntityPersistenceManager(Documentum documentum) {
        this.documentum = documentum;
        mappingHandler = new MappingHandler();
        objectToDCTMConverter = new ObjectToDCTMConverter();
        dctmToObjectConverter = new DCTMToObjectConverter() ;
    }

    public <T> T createObject(String repoObjectName, T objectToSave) throws DfException {
        try {
            IDfSysObject dctmObject = (IDfSysObject) documentum.getSession().newObject(repoObjectName);
            ArrayList<AttributeType> mapping = mappingHandler.getAttributeMappings(objectToSave);
            objectToDCTMConverter.convert(objectToSave, dctmObject, mapping);
            dctmObject.save();
            
            String fieldName = mappingHandler.getIdField(objectToSave);
            PropertyUtils.setSimpleProperty(objectToSave, fieldName, dctmObject.getObjectId().getId());
            return objectToSave;

        } catch (Exception e) {
            String msg = String.format("Object cannot be created for class %s. Exception: %s, %s.", objectToSave.getClass(), e.getClass(), e.getMessage());
            throw new DfException(msg, e);
        }

    }
    
    public <T> String deleteObject(String repoObjectName, T objectToDelete) throws DfException {
        try {
        	IDfSession session = documentum.getSession();
            String idFieldName = mappingHandler.getIdField(objectToDelete);
            String id = (String) PropertyUtils.getSimpleProperty(objectToDelete, idFieldName);
            IDfSysObject dctmObjectToDelete = (IDfSysObject) session.getObject(new DfId(id));
            dctmObjectToDelete.destroy();
            return id;

        } catch (Exception e) {
            String msg = String.format("Object cannot be deleted of class %s. Exception: %s, %s.", objectToDelete.getClass(), e.getClass(), e.getMessage());
            throw new DfException(msg, e);
        }

    }

    public <T> List<T> findAllObjects(Class<T> entityClass, String repoObjectName) throws DfException {
        try {

            IDfSession session = documentum.getSession();
            ArrayList<AttributeType> mapping = mappingHandler.getAttributeMappings(entityClass);
            List<T> list = new ArrayList<T>();

            IDfQuery query = new DfQuery();
            String dql = "select * from " + repoObjectName;    // TODO: create a DQL Builder, introspect mapping and make efficient dql
            query.setDQL(dql);
            IDfCollection coll = query.execute(session, 0);

            while (coll.next()) {
                T objectInstance = entityClass.newInstance();
                IDfTypedObject dctmObject = coll.getTypedObject();
                dctmToObjectConverter.convert(dctmObject, objectInstance, mapping);
                list.add(objectInstance);
            }
            return list;

        } catch (Exception e) {
            String msg = String.format("Object cannot be created for class %s. Exception: %s, %s.", entityClass, e.getClass(), e.getMessage());
            throw new DfException(msg, e);
        }
    }


    public <T> T findById(String id, Class<T> entityClass) throws DfException, InstantiationException, IllegalAccessException {

        try {
            IDfSession session = documentum.getSession();
            ArrayList<AttributeType> mapping = mappingHandler.getAttributeMappings(entityClass);
            DfId dfid = new DfId(id);
            IDfSysObject dctmObject =  (IDfSysObject) session.getObject(dfid);
            T objectInstance = entityClass.newInstance();
            dctmToObjectConverter.convert(dctmObject, objectInstance, mapping);
            return objectInstance;

        } catch (DfException e) {
            String msg = String.format("Exception occured for object with Id: %s class %s. Exception: %s, %s.", id, entityClass, e.getClass(), e.getMessage());
            throw new DfException(msg, e);
        }

    }
    
    public <T> Boolean checkIfIdNull(T objectToCheck) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	
    	String idField = mappingHandler.getIdField(objectToCheck);
    	Object idValue = PropertyUtils.getSimpleProperty(objectToCheck, idField);
    	return idValue.equals(null);
    }
    
    public <T> T update(T objectToUpdate) throws DfException {
    	try {
        	String idField = mappingHandler.getIdField(objectToUpdate);
        	Object valueFromClass = PropertyUtils.getSimpleProperty(objectToUpdate, idField);
        	
        	DfId dfid = new DfId((String) valueFromClass);
        	IDfSysObject dctmObject = (IDfSysObject) documentum.getSession().getObject(dfid);
        	
        	ArrayList<AttributeType> mapping = mappingHandler.getAttributeMappings(objectToUpdate);
            objectToDCTMConverter.convert(objectToUpdate, dctmObject, mapping);
            dctmObject.save();
            dctmToObjectConverter.convert(dctmObject, objectToUpdate, mapping);
            return objectToUpdate;	
            
    	} catch (Exception e) {
            String msg = String.format("Object cannot be updated for class %s. Exception: %s, %s.", objectToUpdate.getClass(), e.getClass(), e.getMessage());
            throw new DfException(msg, e);
        }
    	
    
    }
    
    
    // TODO: have a custom exception class and throw that exception everywhere
    // TODO : Inject DCTMObjectConverter, MappingHander and Documentum

}

