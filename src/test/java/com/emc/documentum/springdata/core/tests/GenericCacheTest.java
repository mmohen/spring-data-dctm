package com.emc.documentum.springdata.core.tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.emc.documentum.springdata.core.GenericCache;

public class GenericCacheTest {
	
	static GenericCache cache;

	@BeforeClass
	public static void setUpBeforeClass() {
		cache = new GenericCache();
	}

	@Test
	public void SetAndGetTest(){
		cache.setEntry("key", "value");
		assertEquals(cache.getEntry("key"), "value");
	}
	
	@Test
	public void TestToCheckCachePersistance(){

			if(cache.getEntry("Key2") == null){
				cache.setEntry("key2", "value");
			}
			
		assertEquals(cache.getEntry("key2"), "value");
	}
		
}
