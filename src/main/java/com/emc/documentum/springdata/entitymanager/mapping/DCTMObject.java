package com.emc.documentum.springdata.entitymanager.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.annotation.Persistent;

/**
 * Identifies a domain object to be persisted to Documentum.
 */
@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface DCTMObject {

	String repository() default "";

	/**
	 * Defines the default language to be used with this document.
	 */
	String language() default "";	
}

