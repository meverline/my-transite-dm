package me.database.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import me.database.nsstore.AbstractDocument;
import me.database.nsstore.IDocument;
import me.database.nsstore.IDocumentSession;
import me.transit.dao.query.translator.mongo.*;
import me.transit.dao.query.tuple.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDocumentSession extends IDocumentSession {

	public final static String LOCALHOST = "localhost";

	private Log log = LogFactory.getLog(MongoDocumentSession.class);
	private final List<String> skipData = new ArrayList<String>();
	private final MongoClient _connection;
	private final MongoDatabase _transDoc;
	private final Map<String, MongoCollection<Document>> collectionMap = new HashMap<>();
	private final ObjectMapper mapper = new ObjectMapper();

	/**
	 *
	 * @param connection
	 * @param properties
	 * @throws UnknownHostException
	 */
	public MongoDocumentSession(MongoClient connection, Map<String,String> properties) throws UnknownHostException {
		this.addSkipField("_id");
		this.addSkipField("@class");

		if ( connection != null ) {
			_connection = connection;
		} else if ( properties.get(IDocumentSession.HOST).equals(LOCALHOST)) {
			_connection = MongoClients.create();
		} else {
			StringBuilder url = new StringBuilder();
			url.append(properties.get(IDocumentSession.HOST));
			url.append(":");
			url.append(properties.get(IDocumentSession.PORT));

			_connection = MongoClients.create(url.toString());
		}

		_transDoc = _connection.getDatabase( properties.get(IDocumentSession.DATABASE));
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	/**
	 *
	 * @param properties
	 * @throws UnknownHostException
	 */
	public MongoDocumentSession(Map<String,String> properties) throws UnknownHostException {
		this(null, properties);
	}

	/**
	 * 
	 * @param collection
	 * @return
	 */
	private MongoCollection<Document> getCollectoin(String collection) {

		if (!collectionMap.containsKey(collection)) {
			collectionMap.put(collection, _transDoc.getCollection(collection));
		}
		return collectionMap.get(collection);
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#add(me.database.mongo.IDocument, java.lang.String)
	 */
	@Override
	public void add(IDocument document, String collection) {
		if (document != null) {
			try {
				getCollectoin(collection).insertOne(this.toMongoObject(document));
			} catch (Exception e) {
				this.log.error("Unable to add " + document.getClass().getName() + ": " + e.getLocalizedMessage());
				this.log.error(e);
			}
		}
	}
	
	public void delete(IDocument document, String collection) {
		if (document != null) {
			try {
				getCollectoin(collection).deleteOne(this.toMongoObject(document));
			} catch (Exception e) {
				this.log.error("Unable to remove " + document.getClass().getName() + ": " + e.getLocalizedMessage());
				this.log.error(e);
			}
		}
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
	protected Document toMongoObject(IDocument document) {
		try {
			String [] ignoreFields = { "agency_name", "shape_id" };
			FilterProvider filters = new SimpleFilterProvider()
					.addFilter("agencyFilter", SimpleBeanPropertyFilter.serializeAllExcept(ignoreFields));

			return Document.parse(this.mapper.writer(filters).writeValueAsString(document));
		} catch (Exception e) {
			this.log.error("Unable to encode " + document.getClass().getName() + ": " + e.getLocalizedMessage());
			this.log.error(e);
			return null;
		}
	}

	protected boolean skipField(String field) {
		for (String item : this.skipData) {
			if (field.equals(item)) {
				return true;
			}
		}
		return false;
	}

	private IMongoQueryTranslator translatorFactory(IQueryTuple query) {
		IMongoQueryTranslator translator = null;

		if ( query instanceof CircleTuple ) {
			translator = new MongoCircleTuple(query);
		} else if ( query instanceof NumberTuple) {
			translator = new MongoNumberTuple(query);
		} else if ( query instanceof  PolygonBoxTuple ) {
			translator = new MongoPolygonTuple(query);
		} else if ( query instanceof  RectangleTuple ) {
			translator = new MongoRectagleTuple(query);
		} else if ( query instanceof  StringTuple) {
			translator = new MongoStringTuple(query);
		} else if ( query instanceof  TimeTuple ) {
			translator = new MongoTimeTuple(query);
		} else {
			throw new IllegalArgumentException("Unknown IQueryTuple type: " + query.getClass().getName());
		}
		return  translator;
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#find(java.util.List, java.lang.String)
	 */
	@Override
	public List<AbstractDocument> find(List<IQueryTuple> tupleList, String collection) {
		Document query = new Document();

		for (IQueryTuple tuple : tupleList) {
			this.translatorFactory(tuple).getDoucmentQuery(query);
		}

		List<AbstractDocument> rtn = new ArrayList<>();

		getCollectoin(collection).find(query).forEach(new Consumer<Document>() {
			@Override
			public void accept(Document item){
				try {
					Class<?> theClass = this.getClass().getClassLoader().loadClass((String) item.get("@class"));
					AbstractDocument obj = (AbstractDocument) mapper.readValue(item.toJson(), theClass);
					rtn.add(obj);
				} catch (Exception e) {
					log.error("Unable to decode: " + e.getLocalizedMessage());
				}
			}
		});

		return rtn;
	}

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumnetDao#size(java.lang.String)
	 */
	@Override
	public long size(String collection) {
		return getCollectoin(collection).countDocuments();
	}

}
