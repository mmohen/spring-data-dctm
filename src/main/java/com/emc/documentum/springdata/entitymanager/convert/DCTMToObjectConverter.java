package com.emc.documentum.springdata.entitymanager.convert;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Controller;

import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.entitymanager.attributes.Attribute;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;

@Controller
public class DCTMToObjectConverter {

    public DCTMToObjectConverter() {}

    public void convert(IDfTypedObject dctmObject, Object objectToReturn, ArrayList<AttributeType> mapping) throws DfException {
        for (AttributeType attributeType : mapping) {
            try {
                getValue(dctmObject, objectToReturn, attributeType);
            } catch (Exception e) {
                String msg = String.format("Conversion failed for Object of class %s. " + "Exception: %s, %s.",
                        objectToReturn.getClass(), e.getClass(), e.getMessage());
                throw new DfException(msg, e);
            }
        }
    }

    // TODO : see if there is a better way of doing this
    private void getValue(IDfTypedObject dctmObject, Object objectToReturn, AttributeType fieldType)
            throws DfException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Attribute<?> attribute = fieldType.getAttribute();

        PropertyUtils.setSimpleProperty(objectToReturn, fieldType.getFieldName(), attribute.getValue(dctmObject));

    }

}
