package com.emc.documentum.springdata.core;

import org.apache.log4j.Logger;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfLoginInfo;

@Controller
public class Documentum {
	
	static Logger log = Logger.getLogger(Documentum.class.getName());
	
	public IDfSessionManager sessionManager;
	private final UserCredentials credentials;
	public String docBase;

	public UserCredentials getCredentials() {
		return this.credentials;
	}

	public Documentum() {
		this.credentials = new UserCredentials("dmadmin", "password");
		this.docBase = "FPIRepo";
		/// this is a default constructor
	}
	
	public Documentum(UserCredentials credentials, String docBase) throws DfException {
		this(credentials, docBase, null, null);
		
	}
	
	public Documentum(UserCredentials credentials, String docBase, String docbrokerHost,
			String docbrokerPort) throws DfException {
		
		Assert.notNull(credentials);
		Assert.notNull(docBase);
		
		this.credentials = credentials;
		this.docBase = docBase;
		
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
	

	public IDfSession getSession() throws DfException {	
		
		try {
			return this.sessionManager.getSession(this.docBase);
		}
		catch (Exception e) {
			String msg = String.format("Session cannot be instantiated for user %s for docBase %s. "
					+ "Exception: %s, %s.", this.credentials.getUsername(), docBase, e.getClass(), e.getMessage());
			throw new DfException(msg, e);
		}
				
	}
	
	public static void main(String[] args) throws DfException {
		
		log.debug("First Log Message");
		Documentum doc = new Documentum(new UserCredentials("dmadmin", "password"),"FPIRepo");
		IDfSession session = doc.getSession();
		IDfSysObject object = (IDfSysObject) session.newObject("dm_sysobject");
		System.out.println(session.getDocbaseName());
        object.setTitle("My Title" );
        object.save();
        IDfSysObject myobject = (IDfSysObject) session.getObject(object.getObjectId());
        System.out.println(myobject.getTitle());		
		
	}

}
