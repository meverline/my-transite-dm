package me.database.mongo;

import java.util.List;
import java.util.Map;

import me.transit.dao.query.tuple.IQueryTuple;

public interface IDocumentDao {

	String COLLECTION = "schedules";
	String TRANSITEDOC = "transiteDoc";
	String LOCALHOST = "localhost";

	/**
	 * Add document to the named collection. 
	 * @param document
	 */
	void add(IDocument document, String collection);

	/**
	 * Add document object to default collection
	 * @param document
	 */
	void add(IDocument document);

	/**
	 * Add data to a named collection.
	 * @param data
	 * @param collectName
	 */
	void add(Map<String, Object> data, String collection);

	/**
	 * Add map to the document to default collection.
	 * @param data
	 */
	void add(Map<String, Object> data);

	/**
	 * Find an object in the collection used the given query used the default collection.
	 * @param tupleList the query tuple to used
	 * @return List of document objects 
	 */
	List<IDocument> find(List<IQueryTuple> tupleList);

	/**
	 * Find an object in a given collection
	 * @param tupleList the query parameters. 
	 * @param collectName the collection name
	 * @return return a list of objects matching the query.
	 */
	List<IDocument> find(List<IQueryTuple> tupleList, String collection);

	/**
	 * Return the size of the default collection 
	 * @return The size of the default collection 
	 */
	long size();

	/**
	 * Return the size of a given collection. 
	 * @param collection The collection 
	 * @return The object
	 */
	long size(String collection);

}