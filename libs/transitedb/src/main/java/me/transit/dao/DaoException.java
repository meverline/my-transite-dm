package me.transit.dao;

public class DaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DaoException(String msg, Exception ex) 
	{
		super(msg, ex);
	}
}
