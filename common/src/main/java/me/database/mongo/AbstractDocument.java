package me.database.mongo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public abstract class AbstractDocument implements IDocument {
    
    private String docId = null;
     
	/**
	 * @return the docId
	 */
    @Override
	@JsonGetter("_id")
	public String getDocId() {
		return docId;
	}
	/**
	 * @param docId the docId to set
	 */
    @Override
	@JsonSetter("_id")
	public void setDocId(String docId) {
		this.docId = docId;
	}

}
