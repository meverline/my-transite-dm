package me.database.nsstore;

import java.util.List;

import me.transit.dao.query.tuple.IQueryTuple;

public abstract class IDocumentSession {

	public final static String HOST = "host";
	public final static String PORT = "port";
	public final static String DATABASE = "database";

	/**
	 * Add document to the named collection. 
	 * @param document
	 */
	public abstract void add(IDocument document, String collection);

	/**
	 *
	 * @param tupleList
	 * @param collection
	 * @return
	 */
	public abstract List<AbstractDocument> find(List<IQueryTuple> tupleList, String collection);

	/**
	 * Return the size of a given collection. 
	 * @param collection The collection 
	 * @return The object
	 */
	public abstract long size(String collection);
	
}