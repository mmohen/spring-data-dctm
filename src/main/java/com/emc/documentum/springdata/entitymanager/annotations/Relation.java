package com.emc.documentum.springdata.entitymanager.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: ramanwalia
 * Date: 20/03/15
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Relation {
  RelationshipType value() default RelationshipType.ONE_TO_ONE;

  String name();
}
