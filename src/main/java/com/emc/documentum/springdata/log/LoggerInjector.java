package com.emc.documentum.springdata.log;

/*
 * Copyright (c) 2015 EMC Corporation. All Rights Reserved.
 * EMC Confidential: Restricted Internal Distribution
 */

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class LoggerInjector implements BeanPostProcessor {
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {

    ReflectionUtils.FieldCallback fieldCallback = new ReflectionUtils.FieldCallback() {

      public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        ReflectionUtils.makeAccessible(field);
        if (field.getAnnotation(AutowiredLogger.class) != null && field.getDeclaringClass().equals(Logger.class)) {
          field.set(bean, Logger.getLogger(bean.getClass()));
        }
      }
    };

    ReflectionUtils.doWithFields(bean.getClass(), fieldCallback);
    return bean;
  }
}

