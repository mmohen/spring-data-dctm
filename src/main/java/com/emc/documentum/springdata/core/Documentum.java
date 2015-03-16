package com.emc.documentum.springdata.core;

import org.apache.log4j.Logger;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfLoginInfo;

@Controller
public class Documentum {
	
	static Logger log = Logger.getLogger(Documentum.class.getName());
    private UserCredentials credentials;
    private String docBase;
    private IDfSessionManager sessionManager;




    public void setCredentials(UserCredentials credentials) {
        this.credentials = credentials;
    }



    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }


	public UserCredentials getCredentials() {
		return this.credentials;
	}

    public Documentum(){

    }


	public Documentum(UserCredentials credentials, String docBase, String docbrokerHost,
			String docbrokerPort) throws DfException {
		
		Assert.notNull(credentials);
		Assert.notNull(docBase);
		
		this.credentials = credentials;
		this.docBase = docBase;

        createSessionManager(credentials, docbrokerHost, docbrokerPort);
	}

    private void createSessionManager(UserCredentials credentials, String docbrokerHost, String docbrokerPort) throws DfException {
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
        log.info("Session Manager Created");
    }


    public IDfSession getSession() throws DfException {
        if (sessionManager == null){
            createSessionManager(this.credentials, null, null);
        }

		
		try {
			return this.sessionManager.getSession(this.docBase);
		}
		catch (Exception e) {
			String msg = String.format("Session cannot be instantiated for user %s for docBase %s. "
					+ "Exception: %s, %s.", this.credentials.getUsername(), docBase, e.getClass(), e.getMessage());
			throw new DfException(msg, e);
		}
				
	}
	

}
