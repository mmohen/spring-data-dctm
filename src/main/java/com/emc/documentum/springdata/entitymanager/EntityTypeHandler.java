package com.emc.documentum.springdata.entitymanager;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.emc.documentum.springdata.entitymanager.mapping.DCTMObject;

@Component
public class EntityTypeHandler {

    public String getEntityObjectName(Class<?> entityClass) {
        Assert.notNull(entityClass, "No class parameter provided, entity collection can't be determined!");


        return getEntityObjectNameFromClass(entityClass);

    }

    public String getEntityObjectNameFromClass(Class<?> type) {

        String fallback = type.getSimpleName();
        if (type.isAnnotationPresent(DCTMObject.class)) {
            DCTMObject dCTMObject = AnnotationUtils.findAnnotation(type, DCTMObject.class);
            return dCTMObject.repository();
        } else {
            return fallback;
        }
    }
}
