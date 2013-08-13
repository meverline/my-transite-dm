package me.transit.dao.mongo;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class DocumentDao {
	
	public final static String COLLECTION = "schedules";

	private Log log = LogFactory.getLog(DocumentDao.class);
	private static DocumentDao _theOne = null;
	private static Mongo _connection = null;
	private DB _transDoc = null;
	
	/**
	 * 
	 * @throws UnknownHostException
	 */
	private DocumentDao() throws UnknownHostException
	{
		if ( _connection == null ) {
			_connection = new Mongo("localhost");
			_transDoc = _connection.getDB("transiteDoc");
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static synchronized DocumentDao instance() throws UnknownHostException {
		if ( _theOne == null ) {
			_theOne = new DocumentDao();
		}
		return _theOne;
	}

	/**
	 * 
	 * @param document
	 */
	public void add(IDocument document)
	{
		if ( document != null ) {
		    DBCollection collection = _transDoc.getCollection(document.getCollection());
		    collection.insert( this.toMongoObject(document));
		}
	}
	
	/**
	 * 
	 * @param data
	 * @param collectName
	 */
	public void add(Map<String,Object> data, String collectName)
	{
		if ( data != null ) {
		    DBCollection collection = _transDoc.getCollection(collectName);
		    collection.insert( this.toMongoObject(data));
		}
	}
	
	/**
	 * 
	 * @param data
	 * @param collectName
	 */
	public Map<String,Object> findDocumentBy(String field, String value)
	{
		// TODO:  find the doucment and return the map.
		return null;
	}
	
	private boolean isPrimativeType(Class<?> type ) {
		
		boolean rtn = false;
		
		if ( type == String.class || type == Long.class || type == Integer.class || 
		     type == Boolean.class || type == Float.class || type == Short.class || 
		     type == Double.class || type == Character.class || type == Byte.class) {
			rtn = true;
		}
		return rtn;
	}
	
	/**
	 * 
	 * @param document
	 * @return
	 */
	private BasicDBObject toMongoObject(IDocument document)
	{	  	
	  	return this.toMongoObject(document.toDocument());
	}
	
	/**
	 * 
	 * @param document
	 * @return
	 */
	private BasicDBObject toMongoObject(Map<String,Object> data)
	{
	  	BasicDBObject rtn = new BasicDBObject();
	  	
		for (Entry<String, Object> entry : data.entrySet()) {

			if (entry.getValue() == null) {
			   log.warn("toMongoObject entry value is null: " + entry.getKey());
			} else {
				if (this.isPrimativeType(entry.getValue().getClass())) {
					rtn.append(entry.getKey(), entry.getValue());
				} else if (List.class.isAssignableFrom(entry.getValue().getClass())) {
					List<?> list = List.class.cast(entry.getValue());

					BasicDBList dbList = new BasicDBList();
					for (Object item : list) {
						if (IDocument.class.isAssignableFrom(item.getClass())) {
							dbList.add(this.toMongoObject(IDocument.class.cast(item)));
						} else if (this.isPrimativeType(item.getClass())) {
							dbList.add(item);
						}
					}
					rtn.append(entry.getKey(), dbList);

				} else if (IDocument.class.isAssignableFrom(entry.getValue().getClass())) {
					rtn.append(entry.getKey(), 
							   this.toMongoObject(IDocument.class.cast(entry.getValue())));
				}
			}
		}
	  	return rtn;
	}
	

}
