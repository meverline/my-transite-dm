package me.crime.dao;

import java.util.List;

import me.crime.database.Crime;
import me.database.hibernate.HibernateDao;
import me.transit.dao.query.tuple.IQueryTuple;

public interface CrimeDao extends HibernateDao<Crime> {

	public Crime loadCrime(String id);
	public List<Crime> queryCrimes(IQueryTuple list);
	public List<Crime> listCrimes();
	public List<Crime> getNextCrimes(long lastMaxId, int maxResults);
	
}
