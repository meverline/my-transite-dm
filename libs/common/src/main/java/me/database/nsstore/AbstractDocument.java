package me.database.nsstore;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public abstract class AbstractDocument implements IDocument {
    
    private long docId = -1;
     
	/**
	 * @return the docId
	 */
    @Override
	@JsonGetter("_id")
	public long getDocId() {
		return docId;
	}
	/**
	 * @param docId the docId to set
	 */
    @Override
	@JsonSetter("_id")
	public void setDocId(long docId) {
		this.docId = docId;
	}

}
