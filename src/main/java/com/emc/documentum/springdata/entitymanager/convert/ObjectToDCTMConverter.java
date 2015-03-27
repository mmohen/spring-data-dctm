package com.emc.documentum.springdata.entitymanager.convert;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD

import com.documentum.fc.common.DfValue;
import com.documentum.fc.common.IDfValue;
=======
>>>>>>> Added all ListTypes and SetValue Method for String, yet to test


import com.documentum.fc.common.DfValue;
import com.documentum.fc.common.IDfValue;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Controller;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.entitymanager.attributes.Attribute;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;
import com.emc.documentum.springdata.entitymanager.attributes.IterableAttribute;

@Controller
public class ObjectToDCTMConverter {

    public ObjectToDCTMConverter() {}

    public void convert(Object objectToSave, IDfSysObject dctmObject, ArrayList<AttributeType> mapping) throws DfException {
        for (AttributeType attributeType : mapping) {
            if (!attributeType.getAttribute().getName().equals( "r_object_id")) {
                try {
                    setValue(dctmObject, objectToSave, attributeType);
                } catch (Exception e) {
                    String msg = String.format("Conversion failed for Object of class %s. " + "Exception: %s, %s.",
                            objectToSave.getClass(), e.getClass(), e.getMessage());
                    throw new DfException(msg, e);
                }

            }
        }
    }

    private void setValue(IDfSysObject dctmObject, Object objectToSave, AttributeType fieldType)
            throws DfException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Object valueFromClass = PropertyUtils.getSimpleProperty(objectToSave, fieldType.getFieldName());
<<<<<<< HEAD
        
        Attribute<?> attribute = fieldType.getAttribute();
        
        if(attribute instanceof IterableAttribute<?> ) {
        	((IterableAttribute<?>) attribute).setValue(dctmObject, (List<Object>) valueFromClass); 	
=======
        Attribute<?> attribute = fieldType.getAttribute();
        
        if(attribute instanceof IterableAttribute<?> ) {
<<<<<<< HEAD
        	((IterableAttribute<?>) attribute).setValue(dctmObject, (List<Object>) valueFromClass);
        	
>>>>>>> Added all ListTypes and SetValue Method for String, yet to test
=======
        	((IterableAttribute<?>) attribute).setValue(dctmObject, (List<Object>) valueFromClass); 	
>>>>>>> Added test for repeating attributes
        }
        else {
        	IDfValue value = new DfValue(valueFromClass,fieldType.getAttribute().getDfAttributeType());
        	dctmObject.setValue(fieldType.getAttribute().getName(), value);
        }
    }

}
