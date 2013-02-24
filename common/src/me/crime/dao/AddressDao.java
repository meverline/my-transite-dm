package me.crime.dao;

import java.util.List;

import me.crime.database.Address;
import me.transit.dao.hibernate.HibernateDao;

public interface AddressDao extends HibernateDao<Address> {

	public Address loadAddress(String id);
	public List<Address> listAddressByLocation(String location);
	public List<Address> listAddressByLocation();
}
