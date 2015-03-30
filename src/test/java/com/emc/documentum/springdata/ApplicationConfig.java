package com.emc.documentum.springdata;

import org.springframework.context.annotation.Configuration;

import com.emc.documentum.springdata.repository.config.AbstractDctmConfiguration;
import com.emc.documentum.springdata.repository.config.EnableDctmRepositories;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@Configuration
@EnableDctmRepositories
public class ApplicationConfig extends AbstractDctmConfiguration{
}
