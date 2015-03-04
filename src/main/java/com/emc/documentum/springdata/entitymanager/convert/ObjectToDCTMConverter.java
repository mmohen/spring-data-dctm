package com.emc.documentum.springdata.entitymanager.convert;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.commons.beanutils.PropertyUtils;

import com.documentum.fc.client.IDfSysObject;
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

public class ObjectToDCTMConverter {

    public Object objectToSave;
    public IDfSysObject dctmObject;

    public ObjectToDCTMConverter(Object objectToSave, IDfSysObject dctmObject) {
        this.objectToSave = objectToSave;
        this.dctmObject = dctmObject;
    }

    public void convert(ArrayList<AttributeType> mapping) throws DfException {
        for (AttributeType attributeType : mapping) {
            if (attributeType.getAttribute().getName() != "r_object_id") {
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

    // TODO : see if there is a better way of doing this
    private void setValue(IDfSysObject dctmObject, Object objectToSave, AttributeType fieldType)
            throws DfException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        Attribute<?> attributeType = fieldType.getAttribute();
        Object valueFromClass = PropertyUtils.getSimpleProperty(objectToSave, fieldType.getFieldName());

        if (attributeType instanceof StringAttribute) {
            dctmObject.setString(fieldType.getAttribute().getName(), (String) valueFromClass);
        } else if (attributeType instanceof IntAttribute) {
            dctmObject.setInt(fieldType.getAttribute().getName(), (Integer) valueFromClass);
        } else if (attributeType instanceof DoubleAttribute) {
            dctmObject.setDouble(fieldType.getAttribute().getName(), (Double) valueFromClass);
        } else if (attributeType instanceof LongAttribute) {
            dctmObject.setDouble(fieldType.getAttribute().getName(), (Long) valueFromClass);
        } else if (attributeType instanceof ShortAttribute) {
            dctmObject.setInt(fieldType.getAttribute().getName(), (Short) valueFromClass);
        } else if (attributeType instanceof FloatAttribute) {
            dctmObject.setDouble(fieldType.getAttribute().getName(), (Float) valueFromClass);
        } else if (attributeType instanceof ByteAttribute) {
            dctmObject.setInt(fieldType.getAttribute().getName(), (Byte) valueFromClass);
        } else if (attributeType instanceof BooleanAttribute) {
            dctmObject.setBoolean(fieldType.getAttribute().getName(), (Boolean) valueFromClass);
        } else if (attributeType instanceof CharacterAttribute) {
            dctmObject.setString(fieldType.getAttribute().getName(), (String) valueFromClass);
        }

    }

}
