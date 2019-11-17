package me.transit.parser.data.converters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import me.transit.parser.data.Blackboard;

public abstract class DataConverter {

	private Log log = LogFactory.getLog(getClass().getName());
	private Blackboard blackboard = null;
	private Class<?> returnType = null;
	
	/**
	 * 
	 * @return
	 */
	public abstract Class<?> getType();

	/**
	 * 
	 * @param data
	 * @return
	 */
	public abstract Object convert(String data);
		
	/**
	 * 
	 * @param obj
	 * @param meth
	 * @param data
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void save(Object obj, Method meth, String data)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object parameters[] = new Object[1];

		try {
			log.info(obj.getClass().getName() + " " + meth.getName() + " " + data);
			parameters[0] = this.convert(data);
			meth.invoke(obj, parameters);
		} catch (Exception e) {
			StringBuilder str = new StringBuilder();
			str.append(e.getLocalizedMessage());
			str.append(": ");
			str.append(meth.getName());
			str.append("( ");
			str.append(this.getClass().getName());
			str.append(") ");
			str.append(data);
			log.error(str.toString());
			throw e;
		}
	}

	public Blackboard getBlackboard() {
		return blackboard;
	}

	public void setBlackboard(Blackboard blackboard) {
		this.blackboard = blackboard;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	
	public abstract DataConverter clone();
	
}
