package com.emc.documentum.springdata.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
	
	public IDfSession session;
	private final UserCredentials credentials;
	
		
	public Documentum(UserCredentials credentials, String docBase) throws DfException {
		this(credentials, docBase, null, null);
		
	}
	
	public Documentum(UserCredentials credentials, String docBase, String host, String port) throws DfException {
		
		Assert.notNull(docBase);
		this.credentials = credentials == null ? UserCredentials.NO_CREDENTIALS : credentials;
		
		if (host == null) {
			host  = getDefaultHost(); 
//			System.out.println(host);
		}
		
		if (port == null) {
			port =  getDefaultPort();
//			System.out.println(port2);
		}
		
		
		DfClientX clientX = new DfClientX();
        IDfClient client = clientX.getLocalClient();
        IDfTypedObject config = client.getClientConfig();
        config.setString("primary_host", host);
        config.setInt("primary_port", new Integer(port));
        IDfSessionManager sessionManager = client.newSessionManager();
        
        IDfLoginInfo loginInfo= clientX.getLoginInfo();
        loginInfo.setUser(credentials.getUsername());
        loginInfo.setPassword(credentials.getPassword());
        sessionManager.setIdentity(IDfSessionManager.ALL_DOCBASES, loginInfo);
        this.session = sessionManager.getSession(docBase);
		
		}
	


	public IDfSession getSession() {
		return session;
	}

	private static final String getDefaultHost() {
		
		try (InputStream inStream = Documentum.class.getResourceAsStream("/dfc.properties"))	{
			Properties properties = new Properties();
			properties.load(inStream);
			String host = properties.getProperty("dfc.docbroker.host[0]");
			return host;
		}
		catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	private static final String getDefaultPort() {
		
		try (InputStream inStream = Documentum.class.getResourceAsStream("/dfc.properties"))	{
			Properties properties = new Properties();
			properties.load(inStream);
			String port = properties.getProperty("dfc.docbroker.port[0]");
			return port;
		}
		catch(IOException e){
			e.printStackTrace();
			return null;
		}			
	}
	
	public static void main(String[] args) throws DfException {
		
		Documentum doc = new Documentum(new UserCredentials("dmadmin", "password"), "FPIRepo");
		IDfSysObject object = (IDfSysObject) doc.getSession().newObject("dm_sysobject");
        object.setTitle("My Title" );
        object.save();
        IDfSysObject myobject = (IDfSysObject) doc.getSession().getObject(object.getObjectId());
        System.out.println(myobject.getTitle());		
		
	}

}
