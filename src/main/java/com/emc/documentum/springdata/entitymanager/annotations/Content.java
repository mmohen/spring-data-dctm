package com.emc.documentum.springdata.entitymanager.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *  * Annotation to indicate Content for dn_document DCTMObject.
 *  
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Content {

    /**
     * The method to be performed on the content can either be "get" or "set".
     *
     */
	
}


