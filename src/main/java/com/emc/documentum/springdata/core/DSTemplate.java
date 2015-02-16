package com.emc.documentum.springdata.core;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfLoginInfo;

public class DSTemplate implements IDSOperations {
	
	 public static void main(String[] args) throws DfException {
	        
		 
		    DfClientX clientX = new DfClientX();

	        IDfClient client = clientX.getLocalClient();
	        
//	        IDfTypedObject config = client.getClientConfig();
//	        config.setString("primary_host", "10.31.157.9");
//	        config.setInt("primary_port", 1589);

	        IDfSessionManager sessionManager = client.newSessionManager();
	        IDfLoginInfo loginInfo= clientX.getLoginInfo();
	        loginInfo.setUser("dmadmin");
	        loginInfo.setPassword("password");

	        sessionManager.setIdentity(IDfSessionManager.ALL_DOCBASES, loginInfo);
	        
	        IDfSession session = sessionManager.getSession("FPIRepo");
	        IDfSysObject object = (IDfSysObject) session.newObject("dm_sysobject");
	        object.setTitle("Megha's Title" );
	        object.save();
	        
	        IDfSysObject myobject = (IDfSysObject) session.getObject(object.getObjectId());
	        
	        System.out.println(myobject.getTitle());

	        
	    }

}
