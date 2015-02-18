package com.emc.documentum.springdata.core;

import org.apache.log4j.Logger;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.util.Assert;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfLoginInfo;

public class Documentum {
	
	static Logger log = Logger.getLogger(Documentum.class.getName());
	
	public IDfSessionManager sessionManager;
	private final UserCredentials credentials;

	public UserCredentials getCredentials() {
		return this.credentials;
	}

	public Documentum(UserCredentials credentials) throws DfException {
		this(credentials, null, null);
		
	}
	
	public Documentum(UserCredentials credentials, String docbrokerHost, String docbrokerPort) throws DfException {
		
		Assert.notNull(credentials);
		
		this.credentials = credentials;
		
		DfClientX clientX = new DfClientX();
        IDfClient client = clientX.getLocalClient();
        IDfTypedObject config = client.getClientConfig();
        
        if (docbrokerHost != null && docbrokerPort != null) {
        	config.setString("primary_host", docbrokerHost);
            config.setInt("primary_port", new Integer(docbrokerPort));
        }
        
        this.sessionManager = client.newSessionManager();
        IDfLoginInfo loginInfo= clientX.getLoginInfo();
        loginInfo.setUser(credentials.getUsername());
        loginInfo.setPassword(credentials.getPassword());
        sessionManager.setIdentity(IDfSessionManager.ALL_DOCBASES, loginInfo);
		}
	

	public IDfSession getSession(String docBase) {
		
		Assert.notNull(docBase);
		
		try {
			return this.sessionManager.getSession(docBase);
		}
		catch (DfException e) {
			// TODO Is this the best way to do this?S
			String msg = String.format("Session cannot be instantiated for user %s for docBase %s. Exception: %s, %s.\n", this.credentials.getUsername(), docBase, e.getClass(), e.getMessage());
			System.out.println(msg);
			e.printStackTrace();
			return null;
		}
				
	}
	
	public static void main(String[] args) throws DfException {
		
		log.debug("First Log Message by Megha");
		Documentum doc = new Documentum(new UserCredentials("dmadmin", "password"));
		IDfSession session = doc.getSession("FPIRepo");
		IDfSysObject object = (IDfSysObject) session.newObject("dm_sysobject");
		System.out.println(session.getDocbaseName());
        object.setTitle("My Title" );
        object.save();
        IDfSysObject myobject = (IDfSysObject) session.getObject(object.getObjectId());
        System.out.println(myobject.getTitle());		
		
	}

}
