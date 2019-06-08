package me.database.mongo;

import java.util.List;
import java.util.Map;

import me.transit.dao.query.tuple.IQueryTuple;

public interface IDocumnetDao {

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#add(me.database.mongo.IDocument, java.lang.String)
	 */
	void add(IDocument document, String collection);

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#add(me.database.mongo.IDocument)
	 */
	void add(IDocument document);

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#add(java.util.Map, java.lang.String)
	 */
	void add(Map<String, Object> data, String collection);

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#add(java.util.Map)
	 */
	void add(Map<String, Object> data);

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#find(java.util.List)
	 */
	List<IDocument> find(List<IQueryTuple> tupleList);

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#find(java.util.List, java.lang.String)
	 */
	List<IDocument> find(List<IQueryTuple> tupleList, String collection);

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#size()
	 */
	long size();

	/* (non-Javadoc)
	 * @see me.database.mongo.IDocumentDao#size(java.lang.String)
	 */
	long size(String collection);

}