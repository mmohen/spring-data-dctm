package com.emc.documentum.springdata.repository.query;

import org.springframework.data.repository.core.EntityMetadata;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public interface DctmEntityMetadata<T> extends EntityMetadata<T> {
//TODO: Will be used with query DSL
  String getDctmEntityName();
}
