package me.database.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import me.transit.dao.query.tuple.IQueryTuple;

@SuppressWarnings("deprecation")
public class DocumentDao extends IDocumentDao {

	private Log log = LogFactory.getLog(IDocumentDao.class);
	private List<String> skipData = new ArrayList<String>();
	protected static DocumentDao _theOne = null;
	private static MongoClient _connection = null;
	private DB _transDoc = null;
	private Map<String, DBCollection> collectionMap = new HashMap<String, DBCollection>();

	/**
	 * 
	 * @param connection
	 * @throws UnknownHostException
	 */
	public DocumentDao(MongoClient connection) throws UnknownHostException {
		this.addSkipField("_id");
		this.addSkipField("@class");
		if (connection != null) {
			_connection = connection;
		} else if (_connection == null) {
			_connection = new MongoClient(IDocumentDao.LOCALHOST);
		}
		_transDoc = _connection.getDB(IDocumentDao.TRANSITEDOC);
		collectionMap.put(IDocumentDao.COLLECTION, _transDoc.getCollection(IDocumentDao.COLLECTION));
		
	}

	/**
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static synchronized IDocumentDao instance() throws UnknownHostException {
		if (_theOne == null) {
			_theOne = new DocumentDao(null);
		}
		return _theOne;
	}

	/**
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static synchronized IDocumentDao instance(MongoClient connection) throws UnknownHostException {
		_theOne = new DocumentDao(connection);
		return _theOne;
	}

	/**
	 * 
	 * @param collection
	 * @return
	 */
	private DBCollection getCollectoin(String collection) {

		if (!collectionMap.containsKey(collection)) {
			collectionMap.put(collection, _transDoc.getCollection(collection));
		}
		return collectionMap.get(collection);
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#add(me.database.mongo.IDocument, java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#add(me.database.mongo.IDocument, java.lang.String)
	 */
	@Override
	public void add(IDocument document, String collection) {
		if (document != null) {
			try {
				getCollectoin(collection).insert(DbObjectMapper.encode(document));
			} catch (Exception e) {
				this.log.error("Unable to encode " + document.getClass().getName() + ": " + e.getLocalizedMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#add(me.database.mongo.IDocument)
	 */
	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#add(me.database.mongo.IDocument)
	 */
	@Override
	public void add(IDocument document) {
		this.add(document, DocumentDao.COLLECTION);
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#add(java.util.Map, java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#add(java.util.Map, java.lang.String)
	 */
	@Override
	public void add(Map<String, Object> data, String collection) {
		if (data != null) {
			try {
				getCollectoin(collection).insert(DbObjectMapper.toMongoObject(data));
			} catch (Exception ex) {
				log.error(ex);
			}
		}
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#add(java.util.Map)
	 */
	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#add(java.util.Map)
	 */
	@Override
	public void add(Map<String, Object> data) {
		add(data, DocumentDao.COLLECTION);
	}


	protected void addSkipField(String field) {
		if (field != null) {
			this.skipData.add(field);
		}
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	protected BasicDBObject toMongoObject(IDocument document) {
		try {
			return DbObjectMapper.encode(document);
		} catch (Exception e) {
			this.log.error("Unable to encode " + document.getClass().getName() + ": " + e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * 
	 * @param fieldList
	 * @return
	 */
	public static String toDocField(List<String> fieldList) {
		StringBuilder queryField = new StringBuilder();

		for (String fld : fieldList) {
			if (queryField.length() > 0) {
				queryField.append(".");
			}
			queryField.append(fld);
		}
		return queryField.toString();
	}

	protected boolean skipField(String field) {
		for (String item : this.skipData) {
			if (field.equals(item)) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#find(java.util.List)
	 */
	@Override
	public List<IDocument> find(List<IQueryTuple> tupleList) {
		return find(tupleList, DocumentDao.COLLECTION);
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#find(java.util.List, java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#find(java.util.List, java.lang.String)
	 */
	@Override
	public List<IDocument> find(List<IQueryTuple> tupleList, String collection) {
		BasicDBObject query = new BasicDBObject();

		for (IQueryTuple tuple : tupleList) {
			tuple.getDoucmentQuery(query);
		}

		DBCursor results = getCollectoin(collection).find(query);

		List<IDocument> rtn = new ArrayList<IDocument>();

		log.info(query.toString() + " ---> " + results.count());
		while (results.hasNext()) {
			try {
				@SuppressWarnings("unchecked")
				Map<String, Object> item = (Map<String, Object>) results.next().toMap();
				IDocument obj = (IDocument) DbObjectMapper.decode(item);
				rtn.add(obj);
			} catch (Exception e) {
				this.log.error("Unable to decode: " + e.getLocalizedMessage());
			}
			
		}
		results.close();
		return rtn;
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#size()
	 */
	@Override
	public long size() {
		return size(DocumentDao.COLLECTION);
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#size(java.lang.String)
	 */
	@Override
	public long size(String collection) {
		return getCollectoin(collection).count();
	}

}
