package me.transit.dao.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.database.RouteDocument;

import org.jongo.Jongo;
import org.jongo.MongoCollection;

import com.mongodb.MongoClient;

public class DocumentDao {
	
	public final static String COLLECTION = "schedules";
	public final static String TRANSITEDOC = "transiteDoc";
	public final static String LOCALHOST = "localhost";

	private static DocumentDao _theOne = null;
	private static MongoClient _connection = null;
	private Jongo _transDoc = null;
	private MongoCollection collection_ = null;
			
	/**
	 * 
	 * @throws UnknownHostException
	 */
	private DocumentDao() throws UnknownHostException
	{
		if ( _connection == null ) {
			_connection = new MongoClient();
		    _transDoc = new Jongo(_connection.getDB(DocumentDao.TRANSITEDOC));
			collection_ = _transDoc.getCollection(DocumentDao.COLLECTION);
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
	public void add(RouteDocument document)
	{
		if ( document != null ) {
		    collection_.insert( document );
		}
	}
			
	/**
	 * 
	 * @param fieldList
	 * @return
	 */
	public static String toDocField(List<String> fieldList)
	{
		StringBuilder queryField = new StringBuilder();
		
		for ( String fld : fieldList) {
			if ( queryField.length() > 0 ) { queryField.append("."); }
			queryField.append(fld);
		}
		return queryField.toString();
	}

	/**
	 * 
	 * @param data
	 * @param collectName
	 */
	public List<RouteDocument> find(List<IQueryTuple> tupleList)
	{
		JongoQueryBuilder query = new JongoQueryBuilder();
		
		for ( IQueryTuple tuple : tupleList) {
			tuple.getDoucmentQuery(query);
		}
		
		Iterable<RouteDocument> results = collection_.find(query.toString()).as(RouteDocument.class);		
		List<RouteDocument> rtn = new ArrayList<RouteDocument>( );
		
		int total = 0;
		
		for ( RouteDocument rd : results) {
			rtn.add(rd);
			total++;
		}

		System.out.println(query.toString() + " ---> " + total + " " + this.size());
		return rtn;
	}
	
	/**
	 * 
	 * @return
	 */
	public long size() {
		return collection_.count();
	}

}
