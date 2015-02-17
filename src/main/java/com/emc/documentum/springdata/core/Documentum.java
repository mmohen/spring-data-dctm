package com.emc.documentum.springdata.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.data.authentication.UserCredentials;
import org.springframework.util.Assert;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.DfAuthenticationException;
import com.documentum.fc.client.DfIdentityException;
import com.documentum.fc.client.DfPrincipalException;
import com.documentum.fc.client.DfServiceException;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfLoginInfo;

public class Documentum {
	
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
		catch (Exception e) {
			// TODO Is this the best way to do this?S
			String msg = String.format("Session cannot be instantiated for user %s for docBase %s. Exception: %s.", this.credentials.getUsername(), docBase, e.getMessage());
			System.out.println(msg);
			e.printStackTrace();
			return null;
		}
				
	}

	
	public static void main(String[] args) throws DfException {
		
		Documentum doc = new Documentum(new UserCredentials("dmadmin", "password"));
		IDfSysObject object = (IDfSysObject) doc.getSession("FPIRepo").newObject("dm_sysobject");
        object.setTitle("My Title" );
        object.save();
        IDfSysObject myobject = (IDfSysObject) doc.getSession("FPIRepo").getObject(object.getObjectId());
        System.out.println(myobject.getTitle());		
		
	}

}
