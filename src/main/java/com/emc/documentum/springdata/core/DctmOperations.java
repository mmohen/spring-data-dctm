package com.emc.documentum.springdata.core;

import java.util.List;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.repository.query.DctmQuery;

public interface DctmOperations {

  <T> T create(T objectToSave) throws DfException;

  <T> String delete(T objectToDelete) throws DfException;

  <ID> String deleteById(ID objectToDeleteId) throws DfException;

  <T> List<T> findAll(Class<T> entityClass) throws DfException;

  <T> List<T> find(DctmQuery query, Class<T> entityClass) throws DfException;

  <T> T findById(String id, Class<T> entityClass) throws DfException;

  <T> T update(T objectToUpdate) throws DfException;

  <T> String getRepositoryObjectName(T obj);

  String getRepositoryName(Class<?> entityClass);

  long count(Class<?> entityClass) throws DfException;

  <T> void setContent(T object, String contentType, String path) throws DfException;

  <T> String getContent(T object, String path) throws DfException;
}
