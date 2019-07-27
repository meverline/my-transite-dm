package me.database.mongo;

import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import me.transit.dao.query.tuple.IQueryTuple;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

public class DocumentDao extends IDocumentDao {

	private Log log = LogFactory.getLog(IDocumentDao.class);
	private List<String> skipData = new ArrayList<String>();
	protected static DocumentDao _theOne = null;
	private static Mongo _connection = null;
	private DB _transDoc = null;
	private Map<String, Map<String, Method>> methodMap = new HashMap<String, Map<String, Method>>();
	private Set<String> undefined = new HashSet<String>();
	private Map<String, DBCollection> collectionMap = new HashMap<String, DBCollection>();

	/**
	 * 
	 * @param connection
	 * @throws UnknownHostException
	 */
	@SuppressWarnings("deprecation")
	public DocumentDao(Mongo connection) throws UnknownHostException {
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
	public static synchronized IDocumentDao instance(Mongo connection) throws UnknownHostException {
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
			getCollectoin(collection).insert(this.toMongoObject(document));
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
				getCollectoin(collection).insert(this.toMongoObject(data));
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

	protected boolean isPrimativeType(Class<?> type) {

		boolean rtn = false;

		if (type == String.class || type == Long.class || type == Integer.class || type == Boolean.class
				|| type == Float.class || type == Short.class || type == Double.class || type == Character.class
				|| type == Byte.class) {
			rtn = true;
		}
		return rtn;
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
		return this.toMongoObject(document.toDocument());
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	protected BasicDBObject toMongoObject(Map<String, Object> data) {
		BasicDBObject rtn = new BasicDBObject();

		for (Entry<String, Object> entry : data.entrySet()) {

			if (entry.getValue() == null) {
				log.warn("toMongoObject entry value is null: " + entry.getKey());
			} else {
				if (this.isPrimativeType(entry.getValue().getClass())) {
					rtn.append(entry.getKey(), entry.getValue());
				} else if (entry.getValue().getClass().isArray()) {
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
					rtn.append(entry.getKey(), this.toMongoObject(IDocument.class.cast(entry.getValue())));
				}
			}
		}
		return rtn;
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

	/**
	 * 
	 * @param obj
	 * @param field
	 * @param value
	 * @return
	 */
	private boolean setValue(Object obj, String field, Object value) {
		String fieldName = field;
		if (field.equals("uuid")) {
			fieldName = field.toUpperCase();
		}
		boolean rtn = true;
		if (!methodMap.containsKey(obj.getClass().getName())) {
			methodMap.put(obj.getClass().getName(), new HashMap<String, Method>());
		}

		Map<String, Method> methods = methodMap.get(obj.getClass().getName());
		Method mth = null;
		try {

			if (methods.containsKey(fieldName)) {
				mth = methods.get(fieldName);
			} else {
				String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				for (Method m : obj.getClass().getMethods()) {
					if (m.getName().equals(methodName) && m.getParameterTypes().length == 1) {
						mth = m;
						break;
					}
				}

				if (mth != null) {
					methods.put(fieldName, mth);
				}
			}

			if (mth == null) {
				if (!undefined.contains(field)) {
					undefined.add(field);
					log.warn("Unable to setField: " + field + " : " + " no set method " + obj.getClass().getName() + " "
							+ value.toString());
				}
				rtn = false;
			} else {

				Class<?> type = mth.getParameterTypes()[0];
				if (type.isEnum()) {
					IDocument doc = IDocument.class.cast(obj);

					doc.handleEnum(fieldName.trim(), value);
				} else {
					Object args[] = new Object[1];
					args[0] = value;

					mth.invoke(obj, args);
				}
			}

		} catch (Exception e) {
			this.log.error(getClass().getName() + ".setValue: " + mth.getName() + ": " + obj.getClass().getName() + " "
					+ e + " " + value.toString() + " " + value.getClass());
			rtn = false;
		}
		return rtn;
	}

	/**
	 * 
	 * @param value
	 * @param key
	 * @param rtn
	 */
	@SuppressWarnings("unchecked")
	protected void translateList(Object value, String key, Object rtn) {

		BasicDBList dbList = BasicDBList.class.cast(value);
		@SuppressWarnings("rawtypes")
		List aList = new ArrayList();
		for (Object entry : dbList) {
			if (this.isPrimativeType(entry.getClass())) {
				aList.add(entry);
			} else if (entry instanceof BasicDBObject) {
				Object newValue = this.translateToDbObject(BasicDBObject.class.cast(entry));
				aList.add(newValue);
			}
		}
		this.setValue(rtn, key, aList);

	}

	/**
	 * 
	 * @param value
	 * @param key
	 * @param rtn
	 */
	protected void translatePrimative(Object value, String key, Object rtn) {
		this.setValue(rtn, key, value);
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	protected IDocument translateToDbObject(Map<String, Object> item) {
		IDocument rtn = null;
		String className = String.class.cast(item.get(IDocument.CLASS));

		try {
			rtn = IDocument.class.cast(this.getClass().getClassLoader().loadClass(className).newInstance());
			for (String key : item.keySet()) {
				Object value = item.get(key);

				if (!this.skipField(key)) {
					if (this.isPrimativeType(value.getClass())) {
						translatePrimative(value, key, rtn);
					} else if (value instanceof BasicDBObject) {
						this.setValue(rtn, key, this.translateToDbObject(BasicDBObject.class.cast(value)));
					} else if (value instanceof BasicDBList) {
						translateList(value, key, rtn);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			this.log.error(className + " " + e + " " + e.getLocalizedMessage());
		}
		return rtn;
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#find(java.util.List)
	 */
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
			@SuppressWarnings("unchecked")
			IDocument obj = this.translateToDbObject((Map<String, Object>) results.next().toMap());
			rtn.add(obj);
		}
		results.close();
		return rtn;
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#size()
	 */
	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#size()
	 */
	@Override
	public long size() {
		return size(DocumentDao.COLLECTION);
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#size(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#size(java.lang.String)
	 */
	@Override
	public long size(String collection) {
		return getCollectoin(collection).count();
	}

}