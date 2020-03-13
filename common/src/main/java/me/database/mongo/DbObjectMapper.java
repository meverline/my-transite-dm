package me.database.mongo;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class DbObjectMapper {
	
	private static Log log = LogFactory.getLog(IDocumentDao.class);

	public enum FIELD {
		db_id, 
		db_name, 
		className;
	}
	
	public static boolean isPrimativeType(Class<?> type) {

		boolean rtn = false;

		if (type == String.class || type == Long.TYPE || type == Integer.TYPE || type == Boolean.TYPE
				|| type == Float.TYPE || type == Short.TYPE || type == Double.TYPE || type == Character.TYPE
				|| type == Byte.TYPE ) {
			rtn = true;
		}
		return rtn;
	}

	
	public static BasicDBObject encode(Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		BasicDBObject rtn = new BasicDBObject();
		
		Class<?> cls = obj.getClass();
		rtn.append(FIELD.className.toString(), obj.getClass().getName());
		for (Method mth : cls.getDeclaredMethods()) {
			for (Annotation man : mth.getAnnotations()) {
				if (man.annotationType() == JsonGetter.class) {
					JsonGetter setter = JsonGetter.class.cast(man);
					
					System.out.println(setter.value() + " " + mth.getReturnType() );
					if (List.class.isAssignableFrom(mth.getReturnType())) {
						List<?> list = List.class.cast(mth.invoke(obj));

						BasicDBList dbList = new BasicDBList();
						for (Object item : list) {
							if ( item.getClass().isPrimitive() || item.getClass() == String.class ) {
								dbList.add(item);
							} else {
								dbList.add(DbObjectMapper.encode(item));
							}
						}
						System.out.print(dbList.size());
						System.out.print(" ");
						System.out.println(list.size());
						rtn.append(setter.value(), dbList);
					} else if (mth.getReturnType().isArray()) {
						rtn.append(setter.value(), mth.invoke(obj));
					} else if ( ! DbObjectMapper.isPrimativeType(mth.getReturnType())) {
						rtn.append(setter.value(), DbObjectMapper.encode(mth.invoke(obj)));
					} else {
						try {
							rtn.append(setter.value(), mth.invoke(obj));
						} catch( Exception ex) {
							log.error(mth.getName() + " " + ex.getLocalizedMessage());
							throw ex;
						}
					}
				}
			}
		}
		return rtn;
	}
	
	public static Object decode(Map<String, Object> map) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException {
		
		String className = (String)map.get(FIELD.className.toString());
		Object obj = map.getClass().getClassLoader().loadClass(className).newInstance();
		Class<?> cls = obj.getClass();
		
		for (Method mth : cls.getDeclaredMethods()) {
			for (Annotation man : mth.getAnnotations()) {
				if (man.annotationType() == JsonSetter.class) {
					JsonSetter setter = JsonSetter.class.cast(man);
					
					Parameter[] parms = mth.getParameters();
					
					System.out.println(setter.value()+ " " + parms[0].getType()  );
					if (List.class.isAssignableFrom(parms[0].getType())) {
						List<Object> list = new ArrayList<>();

						BasicDBList dbList = (BasicDBList) map.get(setter.value());
						for (Object item : dbList) {
						   if ( item.getClass().isPrimitive() || item.getClass() == String.class) {
							   list.add(item);
						   } else {
							   list.add(DbObjectMapper.decode((BasicDBObject) item));
						   }
						}
						System.out.print(dbList.size());
						System.out.print(" ");
						System.out.println(list.size());
						mth.invoke(obj, list);
					} else if (parms[0].getType().isArray()) {
						mth.invoke(obj, map.get(setter.value()));
					} else if ( ! DbObjectMapper.isPrimativeType(parms[0].getType())) {
						@SuppressWarnings("unchecked")
						Map<String, Object> objMap = (Map<String,Object>) map.get(setter.value());
						Object theObj = DbObjectMapper.decode(objMap);
						mth.invoke(obj, theObj);
					} else {
						mth.invoke(obj, map.get(setter.value()));
					}
				}
			}
		}
		return obj;
	}
	
	public static BasicDBObject toMongoObject(Map<String, Object> data) {
		BasicDBObject rtn = new BasicDBObject();

		for (Entry<String, Object> entry : data.entrySet()) {

			if (entry.getValue() == null) {
				DbObjectMapper.log.warn("toMongoObject entry value is null: " + entry.getKey());
			} else {
				if (DbObjectMapper.isPrimativeType(entry.getValue().getClass())) {
					rtn.append(entry.getKey(), entry.getValue());
				} else if (entry.getValue().getClass().isArray()) {
					rtn.append(entry.getKey(), entry.getValue());
				} else if (List.class.isAssignableFrom(entry.getValue().getClass())) {
					List<?> list = List.class.cast(entry.getValue());

					BasicDBList dbList = new BasicDBList();
					for (Object item : list) {
						if (IDocument.class.isAssignableFrom(item.getClass())) {
							try {
								dbList.add(DbObjectMapper.encode(IDocument.class.cast(item)));
							} catch (Exception ex) {
								DbObjectMapper.log.error("Unable to encode " + item.getClass().getName() + ": " + ex.getLocalizedMessage());
							}
						} else if (DbObjectMapper.isPrimativeType(item.getClass())) {
							dbList.add(item);
						}
					}
					rtn.append(entry.getKey(), dbList);

				} else if (IDocument.class.isAssignableFrom(entry.getValue().getClass())) {
					try {
						rtn.append(entry.getKey(), DbObjectMapper.encode(IDocument.class.cast(entry.getValue())));
					} catch (Exception ex) {
						DbObjectMapper.log.error("Unable to encode " + entry.getValue().getClass().getName() + ": " + ex.getLocalizedMessage());
					}
				}
			}
		}
		return rtn;
	}
}
