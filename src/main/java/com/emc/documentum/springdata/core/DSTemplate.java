package com.emc.documentum.springdata.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.documentum.fc.client.IDfSession;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.util.CollectionUtils;

public class DSTemplate implements IDSOperations {
	
	private final IDfSession documentumSession;
	private static final Collection<String> ITERABLE_CLASSES;

	static {

		Set<String> iterableClasses = new HashSet<String>();
		iterableClasses.add(List.class.getName());
		iterableClasses.add(Collection.class.getName());
		iterableClasses.add(Iterator.class.getName());

		ITERABLE_CLASSES = Collections.unmodifiableCollection(iterableClasses);
	}
	
	 public DSTemplate(IDfSession documentumSession){
		 this.documentumSession = documentumSession;	 
	 }
	 
	 public void insert(Object objectToSave) {
		 ensureNotIterable(objectToSave);
		 insert(objectToSave, determineEntityRepositoryName(objectToSave));
	 }


	 public void insert(Object objectToSave, String repositoryName) {
		 ensureNotIterable(objectToSave);
		 doInsert(repositoryName, objectToSave, this.mongoConverter);
	 }

		protected void ensureNotIterable(Object o) {
			if (null != o) {
				if (o.getClass().isArray() || ITERABLE_CLASSES.contains(o.getClass().getName())) {
					throw new IllegalArgumentException("Cannot use a collection here.");
				}
			}
		}
		
		private <T> String determineEntityRepositoryName(T obj) {
			if (null != obj) {
				return determineRepositoryName(obj.getClass());
			}
			return null;
		}

		String determineRepositoryName(Class<?> entityClass) {

			if (entityClass == null) {
				throw new InvalidDataAccessApiUsageException(
						"No class parameter provided, entity collection can't be determined!");
			}

			MongoPersistentEntity<?> entity = mappingContext.getPersistentEntity(entityClass);
			if (entity == null) {
				throw new InvalidDataAccessApiUsageException("No Persitent Entity information found for the class "
						+ entityClass.getName());
			}
			return entity.getCollection();
		}
		
		
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
