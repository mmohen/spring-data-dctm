package com.emc.documentum.springdata;

import com.emc.documentum.springdata.repository.DctmRepository;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public interface PersonRepository extends DctmRepository<Person, String> {
}
