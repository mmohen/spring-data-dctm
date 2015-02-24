package com.emc.documentum.springdata.core;

import com.emc.documentum.springdata.entitymanager.EntityPersistanceManager;
import com.emc.documentum.springdata.entitymanager.EntityTypeHandler;


import org.springframework.util.Assert;


public class DSTemplate implements IDSOperations {
	
	private final Documentum documentum;

	
	 public DSTemplate(Documentum documentum){
		 this.documentum = documentum;	 
	 }
	 
	 
	 public void insert(Object objectToSave) {
		 String repoObjectName = getRepositoryObjectName(objectToSave);
		 
		 EntityPersistanceManager entityPersitanceManager = new EntityPersistanceManager(documentum);
		 entityPersitanceManager.createObject(repoObjectName, objectToSave);
		 
		 
//		 ensureNotIterable(objectToSave);
//		 insert(objectToSave, determineEntityRepositoryName(objectToSave));
	 }
	 
	 
	 private <T> String getRepositoryObjectName(T obj) {
		Assert.notNull(obj);
		 
		return determineRepositoryName(obj.getClass());
		

	}

	private String determineRepositoryName(Class<?> entityClass) {
		
		EntityTypeHandler entityTypeManager = new EntityTypeHandler(); // TODO: inject the class 
		
		return entityTypeManager.getEntityObjectName(entityClass);
	
	}
		 
	 



//	 public void insert(Object objectToSave, String repositoryName) {
//		 ensureNotIterable(objectToSave);
//		 doInsert(repositoryName, objectToSave, this.mongoConverter);
//	 }

//		protected void ensureNotIterable(Object o) {
//			if (null != o) {
//				if (o.getClass().isArray() || ITERABLE_CLASSES.contains(o.getClass().getName())) {
//					throw new IllegalArgumentException("Cannot use a collection here.");
//				}
//			}
//		}
//		
//		private <T> String determineEntityRepositoryName(T obj) {
//			if (null != obj) {
//				return determineRepositoryName(obj.getClass());
//			}
//			return null;
//		}
//
//		String determineRepositoryName(Class<?> entityClass) {
//
//			if (entityClass == null) {
//				throw new InvalidDataAccessApiUsageException(
//						"No class parameter provided, entity collection can't be determined!");
//			}
//
//			MongoPersistentEntity<?> entity = mappingContext.getPersistentEntity(entityClass);
//			if (entity == null) {
//				throw new InvalidDataAccessApiUsageException("No Persitent Entity information found for the class "
//						+ entityClass.getName());
//			}
//			return entity.getCollection();
//		}
//		
		
//		public void insert(Collection<? extends Object> batchToSave, Class<?> entityClass) {
//			doInsertBatch(determineCollectionName(entityClass), batchToSave, this.mongoConverter);
//		}
//
//		public void insert(Collection<? extends Object> batchToSave, String collectionName) {
//			doInsertBatch(collectionName, batchToSave, this.mongoConverter);
//		}
//
//		public void insertAll(Collection<? extends Object> objectsToSave) {
//			doInsertAll(objectsToSave, this.mongoConverter);
//		}

//		protected <T> void doInsertAll(Collection<? extends T> listToSave, MongoWriter<T> writer) {
//			Map<String, List<T>> objs = new HashMap<String, List<T>>();
//
//			for (T o : listToSave) {
//
//				MongoPersistentEntity<?> entity = mappingContext.getPersistentEntity(o.getClass());
//				if (entity == null) {
//					throw new InvalidDataAccessApiUsageException("No Persitent Entity information found for the class "
//							+ o.getClass().getName());
//				}
//				String collection = entity.getCollection();
//
//				List<T> objList = objs.get(collection);
//				if (null == objList) {
//					objList = new ArrayList<T>();
//					objs.put(collection, objList);
//				}
//				objList.add(o);
//
//			}
//
//			for (Map.Entry<String, List<T>> entry : objs.entrySet()) {
//				doInsertBatch(entry.getKey(), entry.getValue(), this.mongoConverter);
//			}
//		}

//		protected <T> void doInsertBatch(String collectionName, Collection<? extends T> batchToSave, MongoWriter<T> writer) {
//
//			Assert.notNull(writer);
//
//			List<DBObject> dbObjectList = new ArrayList<DBObject>();
//			for (T o : batchToSave) {
//
//				initializeVersionProperty(o);
//				BasicDBObject dbDoc = new BasicDBObject();
//
//				maybeEmitEvent(new BeforeConvertEvent<T>(o));
//				writer.write(o, dbDoc);
//
//				maybeEmitEvent(new BeforeSaveEvent<T>(o, dbDoc));
//				dbObjectList.add(dbDoc);
//			}
//			List<ObjectId> ids = insertDBObjectList(collectionName, dbObjectList);
//			int i = 0;
//			for (T obj : batchToSave) {
//				if (i < ids.size()) {
//					populateIdIfNecessary(obj, ids.get(i));
//					maybeEmitEvent(new AfterSaveEvent<T>(obj, dbObjectList.get(i)));
//				}
//				i++;
//			}
//		}
	  

}
