package com.emc.documentum.springdata.entitymanager;

import java.lang.reflect.Field;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.util.Assert;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.emc.documentum.springdata.core.DSTemplate;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.entitymanager.convert.DCTMToObjectConverter;
import com.emc.documentum.springdata.entitymanager.convert.ObjectToDCTMConverter;
import com.emc.documentum.springdata.entitymanager.mapping.MappingHandler;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;

public class EntityPersistenceManager {

    private final Documentum documentum;


    public EntityPersistenceManager(Documentum documentum) {
        this.documentum = documentum;
    }

    // whether to return object or id
    public <T> T createObject(String repoObjectName, T objectToSave) throws DfException {
        try {

            IDfSysObject dctmObject = (IDfSysObject) documentum.getSession().newObject(repoObjectName);
            MappingHandler mappingHandler = new MappingHandler(objectToSave);
            ArrayList<AttributeType> mapping = mappingHandler.getAttributeMappings();
            ObjectToDCTMConverter objectToDCTMConverter = new ObjectToDCTMConverter(objectToSave, dctmObject);
            objectToDCTMConverter.convert(mapping);
            dctmObject.save();
            
            String name = mappingHandler.getIdField();
            PropertyUtils.setSimpleProperty(objectToSave, name, dctmObject.getObjectId().getId());
//            DCTMToObjectConverter dctmToObjectConverter = new DCTMToObjectConverter(objectToSave, dctmObject);
//            dctmToObjectConverter.convert(mapping);
            return objectToSave;
//            return (IDfSysObject) documentum.getSession().getObject(dctmObject.getObjectId());

        } catch (Exception e) {
            String msg = String.format("Object cannot be created for class %s. Exception: %s, %s.", objectToSave.getClass(), e.getClass(), e.getMessage());
            throw new DfException(msg, e);
        }

    }
    
    public <T> String deleteObject(String repoObjectName, T objectToDelete) throws DfException {
        try {
        	IDfSession session = documentum.getSession();
        	MappingHandler mappingHandler = new MappingHandler(objectToDelete);
//            ArrayList<AttributeType> mapping = mappingHandler.getAttributeMappings();
            String idField = mappingHandler.getIdField();
            if (idField.equals(null))
            {
            	throw new DfException("Cannot find object to delete without ID");
            	
            }
            String id = (String) PropertyUtils.getSimpleProperty(objectToDelete, idField);
            System.out.println(id);
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
            ArrayList<AttributeType> mapping = new MappingHandler(entityClass).getAttributeMappings();
            List<T> list = new ArrayList<T>();

            IDfQuery query = new DfQuery();
            String dql = "select * from " + repoObjectName;    // TODO: create a DQL Builder, introspect mapping and make efficient dql
            query.setDQL(dql);
            IDfCollection coll = query.execute(session, 0);

            while (coll.next()) {
                T objectInstance = entityClass.newInstance();
                IDfTypedObject dctmObject = coll.getTypedObject();
                DCTMToObjectConverter objectConverter = new DCTMToObjectConverter(objectInstance, dctmObject);
                objectConverter.convert(mapping);
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
    
    public <T> Boolean checkIfIdNull(T objectToCheck) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	
    	MappingHandler mappingHandler = new MappingHandler(objectToCheck);
    	String idField = mappingHandler.getIdField();
    	Object idValue = PropertyUtils.getSimpleProperty(objectToCheck, idField);
    	return idValue.equals(null);
    }
    
    public <T> T update(T objectToUpdate) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, DfException {
    	
    	MappingHandler mappingHandler = new MappingHandler(objectToUpdate);
    	String idField = mappingHandler.getIdField();
    	Object valueFromClass = PropertyUtils.getSimpleProperty(objectToUpdate, idField);
    	
    	System.out.println("ID " + valueFromClass);
    	DfId dfid = new DfId((String) valueFromClass);
    	IDfSysObject dctmObject = (IDfSysObject) documentum.getSession().getObject(dfid);
    	
    	ArrayList<AttributeType> mapping = new MappingHandler(objectToUpdate).getAttributeMappings();
    	ObjectToDCTMConverter objectToDCTMConverter = new ObjectToDCTMConverter(objectToUpdate, dctmObject);
        objectToDCTMConverter.convert(mapping);
        dctmObject.save();
        
        DCTMToObjectConverter dctmToObjectConverter = new DCTMToObjectConverter(objectToUpdate, dctmObject);
        dctmToObjectConverter.convert(mapping);
        return objectToUpdate;
    
    }
    
	public static void main(String[] args) throws DfException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		DSTemplate template;
		Person p;
		Documentum doc;
		doc = new Documentum(new UserCredentials("dmadmin", "password"),"FPIRepo");
		template = new DSTemplate(doc);	
		p = new Person("Adam",22,"Female");
		Person s = template.create(p);
		p.setGender("Male");
		
		
    	System.out.println("UPDATE GENDER IS: " + s.getGender());
		Person updatedPerson = template.update(p);
		System.out.println("UPDATE GENDER IS: " + updatedPerson.getGender());
	
		
	}
    
    
    // TODO: have a custom exception class and throw that exception everywhere
    // TODO : Inject DCTMObjectConverter, MappingHander and Documentum
    
    // private method ************



}

