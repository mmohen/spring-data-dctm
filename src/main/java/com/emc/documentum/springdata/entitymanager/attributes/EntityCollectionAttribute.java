package com.emc.documentum.springdata.entitymanager.attributes;

import java.util.List;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class EntityCollectionAttribute<T> extends IterableAttribute<T> {

  public EntityCollectionAttribute(String name) {
    super(name);
  }

  @Override
  public void setValue(IDfSysObject dctmObject, List<Object> valueToSet) throws DfException {

  }

  @Override
  public T getValue(Object o) throws DfException {
    return null;
  }
}
