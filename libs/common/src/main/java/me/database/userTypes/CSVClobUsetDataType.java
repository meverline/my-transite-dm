package me.database.userTypes;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import me.database.CSVFieldType;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;


public class CSVClobUsetDataType implements UserType, ParameterizedType {

	protected static final String DEFAULT_CLASS_NAME = "csvClass";

	private Class<? extends CSVFieldType> csvClass;
	
	/**
	 * 
	 */
	public void setParameterValues(Properties parameters) {

		String enumClassName = parameters.getProperty(DEFAULT_CLASS_NAME);
		try {
			csvClass = Class.forName(enumClassName).asSubclass(
					CSVFieldType.class);
		} catch (ClassNotFoundException cfne) {
			throw new HibernateException("CSVFieldType class not found", cfne);
		}

		try {
			@SuppressWarnings("unused")
			Object obj = csvClass.newInstance();
		} catch (InstantiationException e) {
			throw new HibernateException("CSVFieldType unable to create", e);
		} catch (IllegalAccessException e) {
			throw new HibernateException("CSVFieldType unable to create", e);
		}

	}
	
	/**
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private CSVFieldType getObject() throws InstantiationException, IllegalAccessException
	{
		return csvClass.newInstance();
	}
	
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
			if (! List.class.isAssignableFrom(value.getClass())) {
				throw new HibernateException("CSVFieldType::nullSafeSet unable translate");
			}
			String str = toCSV( List.class.cast(value));
			st.setString(index, str);
		}
		return;
	}

	/**
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public Class returnedClass() {
		return csvClass;
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private List<CSVFieldType> fromCSV(String data) throws InstantiationException, IllegalAccessException
	{
		List<CSVFieldType> rtn = new ArrayList<CSVFieldType>();
	
		String lines[] = data.split("\n");
		for ( String aLine : lines) {
			CSVFieldType csv = this.getObject();
			
			csv.fromCSVLine(aLine);
			rtn.add(csv);
		}
		return rtn;
	}

	/**
	 * 
	 */
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		
		if ( String.class.isAssignableFrom(cached.getClass())) {
			return null;
		}	
		List<CSVFieldType> aList = null;
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
	private String toCSV(List aList) {
		StringBuilder builder = new StringBuilder();
		
		for ( Object obj : aList) {
			if ( CSVFieldType.class.isAssignableFrom(obj.getClass())) {
				CSVFieldType type = CSVFieldType.class.cast(obj);
				if ( builder.length() > 0 ) { builder.append("\n"); }
				builder.append( type.toCSVLine());
			} else {
				throw new HibernateException("Invalid obj: " +
											 obj.getClass().getSimpleName() + 
											 " is not " + 
											 CSVFieldType.class.getName());
			}
		}
		return builder.toString();
	}
	
	/**
	 * 
	 */
	public Serializable disassemble(Object value) throws HibernateException {
		
		if (!  List.class.isAssignableFrom(value.getClass())) {
			return null;
		}
		return toCSV( List.class.cast(value));
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

	public Object nullSafeGet(ResultSet arg0, String[] arg1, SharedSessionContractImplementor arg2, Object arg3)
			throws HibernateException, SQLException {
		return null;
	}

	public void nullSafeSet(PreparedStatement arg0, Object arg1, int arg2, SharedSessionContractImplementor arg3)
			throws HibernateException, SQLException {		
	}

}
