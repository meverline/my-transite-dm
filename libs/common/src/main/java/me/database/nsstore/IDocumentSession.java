package me.database.nsstore;

import java.util.List;

import me.transit.dao.query.tuple.IQueryTuple;

public abstract class IDocumentSession {

	public final static String HOST = "host";
	public final static String PORT = "port";
	public final static String DATABASE = "database";

	public final static String COLLECTION = "schedules";
	public final static String TRANSITEDOC = "transiteDoc";

	/**
	 * Add document to the named collection. 
	 * @param document
	 */
	public abstract void add(IDocument document, String collection);

	/**
	 * Add document object to default collection
	 * @param document
	 */
	public abstract void add(IDocument document);

	/**
	 * Find an object in the collection used the given query used the default collection.
	 * @param tupleList the query tuple to used
	 * @return List of document objects 
	 */
	public abstract List<AbstractDocument> find(List<IQueryTuple> tupleList);

	/**
	 *
	 * @param tupleList
	 * @param collection
	 * @return
	 */
	public abstract List<AbstractDocument> find(List<IQueryTuple> tupleList, String collection);

	/**
	 * Return the size of the default collection 
	 * @return The size of the default collection 
	 */
	public abstract long size();

	/**
	 * Return the size of a given collection. 
	 * @param collection The collection 
	 * @return The object
	 */
	public abstract long size(String collection);
	
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

}