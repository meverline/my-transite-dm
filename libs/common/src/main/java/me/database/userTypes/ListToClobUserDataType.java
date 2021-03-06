package me.database.userTypes;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

public class ListToClobUserDataType implements UserType {
		
	/**
	 * 
	 */
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor arg2, Object owner) throws HibernateException,
			SQLException {
		String value = rs.getString(names[0]);
		if ( value == null || value.trim().isEmpty() ) {
			return null;
		}
		Object rtn = null;
		try {
			rtn = this.fromCSV(value);
		}  catch (Exception e) {
			throw new HibernateException("CSVFieldType::nullSafeGet unable translate", e);
		}
		return rtn;
	}

	/**
	 * 
	 */
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor arg3) throws HibernateException, SQLException {
		if ( value == null ) {
			st.setNull(index, Types.VARCHAR);
		} else { 
			if (! HashSet.class.isAssignableFrom(value.getClass())) {
				throw new HibernateException("CSVFieldType::nullSafeSet unable translate");
			}
			String str = toCSV( HashSet.class.cast(value));
			st.setString(index, str);
		}
		return;
	}

	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Class returnedClass() {
		return List.class;
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private HashSet<Object> fromCSV(String data) throws InstantiationException, IllegalAccessException
	{
		HashSet<Object> rtn = new HashSet<Object>();
	
		String lines[] = data.split("\n");
		for ( String aLine : lines) {
			rtn.add(aLine);
		}
		return rtn;
	}

	/**
	 * 
	 */
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		
		if ( ! String.class.isAssignableFrom(cached.getClass())) {
			return null;
		}	
		HashSet<Object> aList = null;
		try {
			aList = fromCSV(String.class.cast(cached));
		} catch (Exception e) {
			throw new HibernateException("CSVFieldType::assemble unable translate", e);
		}
		return aList;
	}
	
	/**
	 * 
	 * @param aList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	private String toCSV(HashSet aList) {
		StringBuilder builder = new StringBuilder();
		
		for ( Object obj : aList) {
			if ( builder.length() > 0 ) { builder.append("\n"); }
			builder.append( obj.toString());
		}
		return builder.toString();
	}
	
	/**
	 * 
	 */
	public Serializable disassemble(Object value) throws HibernateException {
		
		if ( ! HashSet.class.isAssignableFrom(value.getClass())) {
			return null;
		}
		return toCSV( HashSet.class.cast(value));
	}

	/**
	 * 
	 */
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	/**
	 * 
	 */
	public boolean equals(Object x, Object y) throws HibernateException {
		return x == y;
	}

	/**
	 * 
	 */
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	/**
	 * 
	 */
	public boolean isMutable() {
		return false;
	}

	/**
	 * 
	 */
	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	/**
	 * 
	 */
	public int[] sqlTypes() {
		int types[] = { Types.CLOB };
		return types;
	}

	@Override
	public Object nullSafeGet(ResultSet arg0, String[] arg1, SharedSessionContractImplementor arg2, Object arg3)
			throws HibernateException, SQLException {
		return null;
	}

	@Override
	public void nullSafeSet(PreparedStatement arg0, Object arg1, int arg2, SharedSessionContractImplementor arg3)
			throws HibernateException, SQLException {		
	}

}
