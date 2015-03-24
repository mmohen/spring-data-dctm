package com.emc.documentum.springdata.entitymanager;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.emc.documentum.springdata.entitymanager.mapping.DctmEntity;


@Component
public class EntityTypeHandler {

    public String getEntityObjectName(Class<?> entityClass) {
        Assert.notNull(entityClass, "No class parameter provided, entity collection can't be determined!");
        return getEntityObjectNameFromClass(entityClass);
    }

    public String getEntityObjectNameFromClass(Class<?> type) {
        String fallback = type.getSimpleName();
        if (type.isAnnotationPresent(DctmEntity.class)) {
            DctmEntity dctmObject = AnnotationUtils.findAnnotation(type, DctmEntity.class);
            return dctmObject.repository();
        } else {
            return fallback;
        }
    }
}
