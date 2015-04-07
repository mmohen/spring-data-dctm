package com.emc.documentum.springdata.repository.support;

import java.util.Collections;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;

import com.emc.documentum.springdata.entitymanager.mapping.DctmEntity;
import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntities;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.annotations.QueryTransient;
import com.mysema.query.apt.AbstractQuerydslProcessor;
import com.mysema.query.apt.Configuration;
import com.mysema.query.apt.DefaultConfiguration;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
@SupportedAnnotationTypes({"com.emc.documentum.springdata.entitymanager.mapping.*", "com.mysema.query.annotations.*"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class DctmAnnotationProcessor extends AbstractQuerydslProcessor {
  @Override
  protected Configuration createConfiguration(RoundEnvironment roundEnvironment) {
    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running " + getClass().getSimpleName());
    DefaultConfiguration configuration = new DefaultConfiguration(roundEnvironment, processingEnv.getOptions(), Collections.<String> emptySet(),
                                                                  QueryEntities.class, DctmEntity.class, QuerySupertype.class,
                                                                  QueryEmbeddable.class, QueryEmbedded.class, QueryTransient.class);
    configuration.setUnknownAsEmbedded(true);
    return configuration;
  }
}
