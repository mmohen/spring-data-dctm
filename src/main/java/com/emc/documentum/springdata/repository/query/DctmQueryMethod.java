package com.emc.documentum.springdata.repository.query;

import java.lang.reflect.Method;

import com.emc.documentum.springdata.repository.Query;
import org.springframework.data.repository.core.EntityMetadata;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;

import com.emc.documentum.springdata.repository.support.SimpleDctmEntityInformation;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class DctmQueryMethod extends QueryMethod {

  private final DctmEntityMetadata dctmEntityMetadata;
  private final Method method;

  /**
   * Creates a new {@link QueryMethod} from the given parameters. Looks up the correct query to use for following
   * invocations of the method given.
   *
   * @param method   must not be {@literal null}
   * @param metadata must not be {@literal null}
   */
  @SuppressWarnings("unchecked")
  public DctmQueryMethod(Method method, RepositoryMetadata metadata) {
    super(method, metadata);
    this.method = method;
    dctmEntityMetadata = new SimpleDctmEntityInformation(metadata.getDomainType());
  }

  @Override
  public DctmEntityMetadata getEntityInformation() {
    return dctmEntityMetadata;
  }

  public String getQuery() {
    return method.getAnnotation(Query.class).value();
  }
}
