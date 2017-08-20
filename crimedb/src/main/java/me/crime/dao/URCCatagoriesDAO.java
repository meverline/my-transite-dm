package me.crime.dao;

import java.util.HashSet;
import java.util.List;

import me.crime.database.URCCatagories;
import me.database.hibernate.HibernateDao;

public interface URCCatagoriesDAO extends HibernateDao<URCCatagories> {

	public URCCatagories findURCbyCatagory(String id);
	public  List<URCCatagories> listURCbyCrimeCatagory();
	public  HashSet<String> listUniqueURC();
}
