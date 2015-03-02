package com.emc.documentum.springdata.core;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class GenericCache {
	 
	 private static Cache< Object, Object > cache;
	 
	  public GenericCache() {
		  
		  createCache();
	  }

	private synchronized void createCache() {
		if(cache == null){
		  
		
			  cache =    CacheBuilder.newBuilder()
					    .concurrencyLevel(4)
					    .maximumSize(10000)
					    .expireAfterWrite(10, TimeUnit.MINUTES)
					    .build();
		  }
	}
	 
	  public Object getEntry( Object key ) {

		  try { 
			  
		  	return cache.getIfPresent( key );
		 
		  } catch (Exception e) {
			
			  return  null;
		 
		  }
		  
	  }
	  
	  public void setEntry(Object key, Object value){
	
		 cache.asMap().put(key, value);
	  }
}

//TODO: take all the concurrency properties for the user from a property file
