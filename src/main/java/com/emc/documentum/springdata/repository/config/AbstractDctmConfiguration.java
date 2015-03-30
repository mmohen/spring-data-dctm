package com.emc.documentum.springdata.repository.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@Configuration
@EnableDctmRepositories
@ComponentScan(basePackages = {"com.emc.documentum.springdata"})//TODO: Make this a little more concise
public abstract class AbstractDctmConfiguration {
  //Add the conversion service
}
