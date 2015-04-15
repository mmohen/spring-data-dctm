package com.emc.documentum.springdata.entitymanager.mapping;

import java.util.ArrayList;
import java.util.List;

import com.emc.documentum.springdata.entitymanager.annotations.Relation;
import com.emc.documentum.springdata.entitymanager.annotations.RelationshipType;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;

import junit.framework.TestCase;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class MappingHandlerTest {

  public void testGetAttributeMappings() throws Exception {
    MappingHandler handler = new MappingHandler();
    ArrayList<AttributeType> attributeMappings = handler.getAttributeMappings(LeftEntity.class);
    for (AttributeType attributeMapping : attributeMappings) {
      System.out.println(attributeMapping);
    }
  }

  private static final class LeftEntity {
    @Relation(name = "mn", value = RelationshipType.ONE_TO_MANY)
    private final List<RightEntity> rightEntity = new ArrayList<>();
  }

  private static final class RightEntity {
    @Relation(name = "mn", value= RelationshipType.ONE_TO_MANY)
    private final List<LeftEntity> leftEntity = new ArrayList<>();
  }
}