package com.emc.documentum.springdata.repository.query;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.util.Assert;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.entitymanager.attributes.AttributeType;
import com.emc.documentum.springdata.entitymanager.mapping.MappingHandler;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.path.PathBuilder;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */
public class DctmQueryCreator extends AbstractQueryCreator<DctmQuery, Predicate> {

  private final PathBuilder pathBuilder;
  private final MappingHandler mappingHandler;
  private DctmQueryMethod queryMethod;

  @SuppressWarnings("unchecked")
  public DctmQueryCreator(MappingHandler mappingHandler, PartTree tree, DctmQueryMethod queryMethod, ParameterAccessor accessor) {
    super(tree, accessor);
    this.mappingHandler = mappingHandler;
    this.queryMethod = queryMethod;
    pathBuilder = new PathBuilder(queryMethod.getEntityInformation().getJavaType(), queryMethod.getEntityInformation().getDctmEntityName());
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Predicate create(Part part, Iterator<Object> iterator) {
    Object value = iterator.next();

    try {
      switch (part.getType()) {
        case SIMPLE_PROPERTY:
          if (part.getProperty().getType() == String.class) {
            value = escape((String)value);
          }
          return pathBuilder.get(getDctmAttributeName(part), part.getProperty().getType()).eq(value);

        case GREATER_THAN:
          return pathBuilder.getNumber(getDctmAttributeName(part), part.getProperty().getType()).gt(new ConstantImpl<>((Number)value));

        default:
          throw new UnsupportedOperationException(String.format("Unidentifiable part of the query {%s}", part.getType().name()));
      }
    } catch (DfException e) {
      e.printStackTrace();
      return null;
    }
  }

  private String getDctmAttributeName(Part part) throws DfException {
    ArrayList<AttributeType> attributeMappings = mappingHandler.getAttributeMappings(queryMethod.getEntityInformation().getJavaType());
    PropertyPath property = part.getProperty();
    for (AttributeType attributeMapping : attributeMappings) {
      if (attributeMapping.getFieldName().equalsIgnoreCase(property.getSegment())) {
        return attributeMapping.getAttribute().getName();
      }
    }
    return null;
  }

  private String escape(String property) {
    return "'" + property + "'";
  }

  @Override
  protected Predicate and(Part part, Predicate base, Iterator<Object> iterator) {
    if(base == null)
      return create(part, iterator);

    Predicate right = create(part, iterator);
    return ((BooleanOperation)base).and(right);
  }

  @Override
  protected Predicate or(Predicate base, Predicate criteria) {
    Assert.notNull(base);
    Assert.notNull(criteria);
    BooleanBuilder or = new BooleanBuilder(base).or(criteria);
    return or.getValue();
  }

  @Override
  protected DctmQuery complete(Predicate criteria, Sort sort) {
    DctmQuery dctmQuery = new DctmQuery(criteria);
    return dctmQuery.with(sort);
  }
}
