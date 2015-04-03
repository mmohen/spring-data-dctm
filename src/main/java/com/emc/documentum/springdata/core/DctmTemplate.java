package com.emc.documentum.springdata.core;

import java.util.List;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.entitymanager.EntityPersistenceManager;
import com.emc.documentum.springdata.entitymanager.EntityTypeHandler;

import com.emc.documentum.springdata.entitymanager.annotations.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;


@Controller
public class DctmTemplate implements DctmOperations {

  private final EntityTypeHandler entityTypeManager;
  private final EntityPersistenceManager entityPersistenceManager;

  @Autowired
  public DctmTemplate(EntityPersistenceManager entityPersistenceManager, EntityTypeHandler entityTypeManager) {
    this.entityPersistenceManager = entityPersistenceManager;
    this.entityTypeManager = entityTypeManager;
  }

  @Override
  public <T> T create(T objectToSave) throws DfException {
    Assert.notNull(objectToSave);
    String repoObjectName = getRepositoryObjectName(objectToSave);
    return entityPersistenceManager.createObject(repoObjectName, objectToSave);
  }

  @Override
  public <T> String delete(T objectToDelete) throws DfException {
    Assert.notNull(objectToDelete);
    String repoObjectName = getRepositoryObjectName(objectToDelete);
    return entityPersistenceManager.deleteObject(repoObjectName, objectToDelete);
  }

  @Override
  public <ID> String deleteById(ID objectToDeleteId) throws DfException {
    Assert.notNull(objectToDeleteId);
    return entityPersistenceManager.deleteObject(objectToDeleteId.toString());
  }

  @Override
  public <T> List<T> findAll(Class<T> entityClass) throws DfException {
    Assert.notNull(entityClass);
    String repoObjectName = getRepositoryName(entityClass);
    return entityPersistenceManager.findAllObjects(entityClass, repoObjectName);
  }

  @Override
  public <T> T findById(String id, Class<T> entityClass) throws DfException {
    Assert.notNull(id);
    Assert.notNull(entityClass);
    return entityPersistenceManager.findById(id, entityClass);
  }
  
  @Override
  public long count(Class<?> entityClass) throws DfException {
	Assert.notNull(entityClass);
	String repoObjectName = getRepositoryName(entityClass);
	return entityPersistenceManager.count(entityClass, repoObjectName);
  }
  

  @Override
  public <T> T update(T objectToUpdate) throws DfException {
    return entityPersistenceManager.update(objectToUpdate);
  }

  @Override
  public <T> String getRepositoryObjectName(T obj) {
    Assert.notNull(obj);
    return getRepositoryName(obj.getClass());
  }

  @Override
  public String getRepositoryName(Class<?> entityClass) {
    Assert.notNull(entityClass);
    return entityTypeManager.getEntityObjectName(entityClass);
  }


  public <T> void setContent(T object, String contentType, String path) throws DfException {
      entityPersistenceManager.setContent(object, contentType, path);
  }

  public <T> String getContent(T object, String path) throws DfException {
      return entityPersistenceManager.getContent(object, path);

  }

}
