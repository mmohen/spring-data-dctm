package com.emc.documentum.springdata.entitymanager.convert;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;

import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.entitymanager.attributes.Attribute;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;
import com.emc.documentum.springdata.entitymanager.attributes.BooleanAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.ByteAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.CharacterAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.DoubleAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.FloatAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.IntAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.LongAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.ShortAttribute;
import com.emc.documentum.springdata.entitymanager.attributes.StringAttribute;

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

        Attribute<?> attributeType = fieldType.getAttribute();
        Object valueFromDocumentum = null;

        if (attributeType instanceof StringAttribute) {
            valueFromDocumentum = dctmObject.getString(fieldType.getAttribute().getName());
        } else if (attributeType instanceof IntAttribute) {
            valueFromDocumentum = dctmObject.getInt(fieldType.getAttribute().getName());
        } else if (attributeType instanceof DoubleAttribute) {
            valueFromDocumentum = dctmObject.getDouble(fieldType.getAttribute().getName());
        } else if (attributeType instanceof LongAttribute) {
            valueFromDocumentum = dctmObject.getDouble(fieldType.getAttribute().getName());
        } else if (attributeType instanceof ShortAttribute) {
            valueFromDocumentum = dctmObject.getInt(fieldType.getAttribute().getName());
        } else if (attributeType instanceof FloatAttribute) {
            valueFromDocumentum = dctmObject.getDouble(fieldType.getAttribute().getName());
        } else if (attributeType instanceof ByteAttribute) {
            valueFromDocumentum = dctmObject.getInt(fieldType.getAttribute().getName());
        } else if (attributeType instanceof BooleanAttribute) {
            valueFromDocumentum = dctmObject.getBoolean(fieldType.getAttribute().getName());
        } else if (attributeType instanceof CharacterAttribute) {
            valueFromDocumentum = dctmObject.getString(fieldType.getAttribute().getName());
        }
        PropertyUtils.setSimpleProperty(objectToReturn, fieldType.getFieldName(), valueFromDocumentum);

    }

}
