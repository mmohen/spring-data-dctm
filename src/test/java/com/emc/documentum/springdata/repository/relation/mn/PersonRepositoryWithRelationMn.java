package com.emc.documentum.springdata.repository.relation.mn;

import org.springframework.stereotype.Component;

import com.emc.documentum.springdata.repository.DctmRepository;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public interface PersonRepositoryWithRelationMn extends DctmRepository<Person, String> {
}
