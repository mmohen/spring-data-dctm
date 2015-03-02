package com.emc.documentum.springdata.core.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.authentication.UserCredentials;

import com.documentum.fc.common.DfException;
import com.emc.documentum.springdata.core.DSTemplate;
import com.emc.documentum.springdata.core.Documentum;
import com.emc.documentum.springdata.core.GenericCache;

public class GenericCacheTest {
	
	static GenericCache cache;
	Person p;
	DSTemplate template;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		cache = new GenericCache();
		ArrayList<String> arraylist = new ArrayList<String>();
		Documentum doc = new Documentum(new UserCredentials("dmadmin", "password"),"FPIRepo");
		DSTemplate template = new DSTemplate(doc);
		Person p = new Person("Rohan",22,"Male");

	}

	@Test
	public void SetAndGetTest() throws DfException {
		cache.setEntry("key", "value");
		assertEquals(cache.getEntry("key"), "value");
	}
	
	@Test
	public void TestToCheckCachePersistance() throws DfException {

			if(cache.getEntry("Key2") == null){
				cache.setEntry("key2", "value");
			}
			
		assertEquals(cache.getEntry("key2"), "value");
	}
		
}
