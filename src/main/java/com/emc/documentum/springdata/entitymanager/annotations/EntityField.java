package com.emc.documentum.springdata.entitymanager.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *  * Annotation to define custom metadata for DCTMObject fields.
 *  
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityField {

    /**
     * The key to be used to store the field inside the document.
     *
     * @return String
     */

    String value() default "";

    /**
     * The order in which various fields shall be stored. Has to be a positive integer.
     *
     *
     */

}


