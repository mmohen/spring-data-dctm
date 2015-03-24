package com.emc.documentum.springdata.entitymanager.attributes;

import com.documentum.fc.common.DfException;

<<<<<<< HEAD
=======
/**
 * Created with IntelliJ IDEA.
 * User: ramanwalia
 * Date: 20/03/15
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
>>>>>>> Session Manager instantiated in default constructor
public class ObjectAttribute extends Attribute<Object> {

    public ObjectAttribute(String name) {
        super(name);
    }

    @Override
    public Object getValue(Object o) throws DfException {
        return null;
    }
}
