package me.transit.dao.mongo;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class DocumentDao {

	private static Mongo _connection = null;
	private DB _transDoc = null;
	
	/**
	 * 
	 * @throws UnknownHostException
	 */
	public DocumentDao() throws UnknownHostException
	{
		if ( _connection == null ) {
			_connection = new Mongo("localhost");
			_transDoc = _connection.getDB("transDoc");
		}
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
	  	BasicDBObject rtn = new BasicDBObject();
	  	
	  	Map<String,Object> data = document.toDocument();
	  	for ( Entry<String,Object> entry : data.entrySet()) {
	  		
	  		if ( this.isPrimativeType(entry.getValue().getClass()) ) {
	  			rtn.append(entry.getKey(), entry.getValue());
	  		} else if (List.class.isAssignableFrom(entry.getValue().getClass()) ) {
	  			List<?> list = List.class.cast(entry.getValue());

				BasicDBList dbList = new BasicDBList();
				for (Object item : list) {
					if ( IDocument.class.isAssignableFrom(item.getClass()) ) {
						dbList.add(this.toMongoObject(IDocument.class.cast(item)));
					} else if ( this.isPrimativeType(item.getClass()) ) {
						dbList.add(item);
					}
				}
				rtn.append(entry.getKey(), dbList);
				
	  		} else if ( IDocument.class.isAssignableFrom(entry.getValue().getClass()) ) {
	  			rtn.append(entry.getKey(),this.toMongoObject(IDocument.class.cast(entry.getValue()))); 
	  		}
	  	}
	  	return rtn;
	}
	

}
