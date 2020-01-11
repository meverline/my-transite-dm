package me.transit.parser.data.savers;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import me.transit.parser.data.Blackboard;
import me.transit.parser.data.converters.DataConverter;
import me.transit.parser.data.converters.DataConverterFactory;

public class DataSaver {

	private final Method method;
	private final String fieldName;
	private final Blackboard parser;
	private final Class<?> returnType;
	private final String orgHeader;
	private DataConverter type;

	private Log log = LogFactory.getLog(DataSaver.class);

	
	public DataSaver(Method setMethod, String field, Blackboard parser, String header, DataConverterFactory factory) throws NoSuchMethodException {
		this.method = setMethod;
		this.fieldName = field;
		this.parser = parser;
		this.returnType = initReturnType(setMethod, factory);
		this.orgHeader = header;
	}
	
	public String getOrgHeader() {
		return this.orgHeader;
	}

	/**
	 * setMethod
	 * @return
	 */
	public String getField() {
		return fieldName;
	}
	
	/**
	 * @return the returnType
	 */
	protected Class<?> getReturnType() {
		return returnType;
	}
	
	/**
	 * @return the method
	 */
	protected Method getMethod() {
		return method;
	}

	/**
	 * @return the type
	 */
	protected DataConverter getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	protected void setType(DataConverter type) {
		this.type = type;
	}

	/**
	 * @return the parser
	 */
	protected Blackboard getBlackboard() {
		return parser;
	}

	/**
	 * @throws NoSuchMethodException
	 * 
	 */
	private Class<?> initReturnType(Method setMethod, DataConverterFactory factory) throws NoSuchMethodException {
		
		if ( setMethod.getParameterCount() == 0 ) {
			throw new NoSuchMethodException("Not a set method: " + setMethod.getName());
		}
		Parameter parm = (setMethod.getParameters())[0];
		DataConverter type = factory.getHandler(parm.getType());
		
	    if ( type == null && parm.getType().isEnum()) {
	    	type = factory.getHandler(Enum.class);
	    }
	    
	    if ( type == null && parm.getType().isPrimitive()) {
			throw new NoSuchMethodException("Unkown type: " + parm.getType());
		} else if ( type == null ){
			log.error("OBJECT: " + setMethod.getName() + " " + parm.getType().getName() + " " + parm.getType().getName());
			type = factory.getHandler(Object.class);
		}
	    setType(type);
		return parm.getType();

	}

	/**
	 * Save the data to a given object.
	 * 
	 * @param obj
	 * @param data
	 */
	public void save(Object obj, String data) {
		try {
			getType().setReturnType(getReturnType());
			getType().setBlackboard(getBlackboard());
			getType().save(obj, getMethod(), data);
		} catch (Exception e) {
			StringBuilder str = new StringBuilder();
			str.append(e.getLocalizedMessage());
			str.append(": ");
			str.append(this.getMethod().getName());
			str.append("( ");
			str.append(getType());
			str.append(") ");
			str.append(this.returnType.getName());
			str.append(" ");
			str.append(data);
			log.error(str.toString(), e);
		}
	}
	
}
