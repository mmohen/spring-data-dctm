package com.emc.documentum.springdata.core;

import org.springframework.beans.factory.annotation.Autowired;

import com.documentum.fc.client.IDfDocument;
import com.documentum.fc.common.DfException;

public class Content {
	
	@Autowired
	private Documentum documentum;
	
	IDfDocument iDfDocument;
	
	public Content() throws DfException {
		iDfDocument = (IDfDocument) documentum.getSession().newObject("dm_document");
	}
	
	public void setObjectName(String docName) throws DfException {
		iDfDocument.setObjectName(docName);
	}
	
	public void setTitle(String title) throws DfException {
		iDfDocument.setTitle(title);
	}
	public void link(String path) throws DfException {
		iDfDocument.link(path);
	}
	public void setContentType(String contentType) throws DfException {
		iDfDocument.setContentType(contentType);
	}
	public void setFile(String contentFile) throws DfException{
		iDfDocument.setFile(contentFile);
	}
	public void save() throws DfException {
		iDfDocument.save();
	}	
	public String getObjectId() throws DfException {
		return iDfDocument.getObjectId().toString();
	}

}
