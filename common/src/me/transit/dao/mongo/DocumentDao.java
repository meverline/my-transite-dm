package me.transit.dao.mongo;

import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.StopTime;
import me.transit.database.impl.AgencyImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

public class DocumentDao {
        
        public final static String COLLECTION = "schedules";
        public final static String TRANSITEDOC = "transiteDoc";
        public final static String LOCALHOST = "localhost";

        private Log log = LogFactory.getLog(DocumentDao.class);
        private static DocumentDao _theOne = null;
        private static Mongo _connection = null;
        private DB _transDoc = null;
        DBCollection _collection = null;
        private Map<String, Map<String,Method>> methodMap = new HashMap<String,Map<String,Method>>();
        private Set<String> undefined = new HashSet<String>();
        
        /**
         * 
         * @throws UnknownHostException
         */
        private DocumentDao() throws UnknownHostException
        {
                if ( _connection == null ) {
                        _connection = new MongoClient(DocumentDao.LOCALHOST);
                        _transDoc = _connection.getDB(DocumentDao.TRANSITEDOC);
                        _collection = _transDoc.getCollection(DocumentDao.COLLECTION);
                }
        }
        
        /**
         * 
         * @return
         * @throws UnknownHostException
         */
        public static synchronized DocumentDao instance() throws UnknownHostException {
                if ( _theOne == null ) {
                        _theOne = new DocumentDao();
                }
                return _theOne;
        }

        /**
         * 
         * @param document
         */
        public void add(IDocument document)
        {
                if ( document != null ) {
                    _collection.insert( this.toMongoObject(document));
                }
        }
        
        /**
         * 
         * @param data
         * @param collectName
         */
        public void add(Map<String,Object> data)
        {
                if ( data != null ) {
                    _collection.insert( this.toMongoObject(data));
                }
        }
        

        
        private boolean isPrimativeType(Class<?> type ) {
                
                boolean rtn = false;
                
                if ( type == String.class || type == Long.class || type == Integer.class || 
                     type == Boolean.class || type == Float.class || type == Short.class || 
                     type == Double.class || type == Character.class || type == Byte.class) {
                        rtn = true;
                }
                return rtn;
        }
        
        /**
         * 
         * @param document
         * @return
         */
        private BasicDBObject toMongoObject(IDocument document)
        {               
                return this.toMongoObject(document.toDocument());
        }
        
        /**
         * 
         * @param document
         * @return
         */
        private BasicDBObject toMongoObject(Map<String,Object> data)
        {
                BasicDBObject rtn = new BasicDBObject();
                
                for (Entry<String, Object> entry : data.entrySet()) {

                        if (entry.getValue() == null) {
                           log.warn("toMongoObject entry value is null: " + entry.getKey());
                        } else {
                                if (this.isPrimativeType(entry.getValue().getClass())) {
                                        rtn.append(entry.getKey(), entry.getValue());
                                } else if ( entry.getValue().getClass().isArray() ) {
                                        rtn.append(entry.getKey(), entry.getValue());
                                } else if (List.class.isAssignableFrom(entry.getValue().getClass())) {
                                        List<?> list = List.class.cast(entry.getValue());

                                        BasicDBList dbList = new BasicDBList();
                                        for (Object item : list) {
                                                if (IDocument.class.isAssignableFrom(item.getClass())) {
                                                        dbList.add(this.toMongoObject(IDocument.class.cast(item)));
                                                } else if (this.isPrimativeType(item.getClass())) {
                                                        dbList.add(item);
                                                }
                                        }
                                        rtn.append(entry.getKey(), dbList);

                                } else if (IDocument.class.isAssignableFrom(entry.getValue().getClass())) {
                                        rtn.append(entry.getKey(), 
                                                           this.toMongoObject(IDocument.class.cast(entry.getValue())));
                                }
                        }
                }
                return rtn;
        }
        
        /**
         * 
         * @param fieldList
         * @return
         */
        public static String toDocField(List<String> fieldList)
        {
                StringBuilder queryField = new StringBuilder();
                
                for ( String fld : fieldList) {
                        if ( queryField.length() > 0 ) { queryField.append("."); }
                        queryField.append(fld);
                }
                return queryField.toString();
        }
        
        private boolean skipField(String field) {
                
                String [] data = { "_id", "@class", StopTime.LOCATION };
                
                for ( String item : data ) {
                        if ( field.equals(item) ) {
                                return true;
                        }
                }
                return false;
        }
        
        /**
         * 
         * @param obj
         * @param field
         * @param value
         * @return
         */
        private boolean setValue(Object obj, String field, Object value)
        {
                String fieldName = field;
                if ( field.equals("uuid") ) {
                        fieldName = field.toUpperCase();
                }
                boolean rtn = true;
                if ( ! methodMap.containsKey(obj.getClass().getName()) ) {
                        methodMap.put(obj.getClass().getName(), new HashMap<String, Method>());
                }
                
                Map<String, Method> methods = methodMap.get(obj.getClass().getName());
                Method mth = null;
                try {

                        if ( methods.containsKey(fieldName) ) {
                                mth = methods.get(fieldName);
                        } else {
                                String methodName = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
                                for ( Method m : obj.getClass().getMethods() ) {
                                        if ( m.getName().equals(methodName) && m.getParameterTypes().length == 1)  {
                                                mth = m;
                                                break;
                                        }
                                }
                           
                                if ( mth != null ) {
                                        methods.put(fieldName, mth);
                                }
                        }
                        
                        if ( mth == null ) {
                                if ( ! undefined.contains(field) ) {
                                  undefined.add(field);
                                  log.warn("Unable to setField: " + field + " : " + " no set method " + obj.getClass().getName() + " " + value.toString());
                                }
                                rtn = false;
                        } else {
                                
                                Class<?> type = mth.getParameterTypes()[0];
                                if ( type.isEnum() ) {
                                        IDocument doc = IDocument.class.cast(obj);
                                        
                                        doc.handleEnum(fieldName.trim(), value);
                                } else {
                                        Object args[] = new Object[1];
                                        args[0] = value;
                                        
                                        mth.invoke(obj, args);
                                }
                        }
                        
                } catch (Exception e) {
                        e.printStackTrace();
                        this.log.error( mth.getName() + " "+ obj.getClass().getName() + " " + e);
                        rtn = false;
                }
                return rtn;
        }
        
        /**
         * 
         * @param item
         * @return
         */
        @SuppressWarnings("unchecked")
        private Object translateToDbObject(Map<String, Object> item)
        {
                Object rtn = null;
                String className = String.class.cast(item.get(IDocument.CLASS));
                
                try {
                        rtn = this.getClass().getClassLoader().loadClass(className).newInstance();
                        for ( String key : item.keySet()) {
                                Object value = item.get(key);
                                
                                if ( ! this.skipField(key) ) {
                                        if ( this.isPrimativeType(value.getClass())) {
                                                Object setValue = value;
                                                if ( key.equals(Agency.AGENCY) ) {
                                                        setValue = new AgencyImpl(value.toString());
                                                } 
                                                this.setValue(rtn, key, setValue);
                                        } else if ( value instanceof BasicDBObject ) {
                                                Object newValue = this.translateToDbObject(BasicDBObject.class.cast(value));
                                                this.setValue(rtn, key, newValue);
                                        } else if ( value instanceof BasicDBList) {
                                                BasicDBList dbList = BasicDBList.class.cast(value);
                                                @SuppressWarnings("rawtypes")
                                                List aList = new ArrayList();
                                                for ( Object entry : dbList) {
                                                        if ( this.isPrimativeType(entry.getClass())) {
                                                                aList.add(entry);
                                                        } else if ( entry instanceof BasicDBObject ) {
                                                                Object newValue = this.translateToDbObject(BasicDBObject.class.cast(entry));
                                                                aList.add(newValue);
                                                        }
                                                }
                                                this.setValue(rtn, key, aList);
                                        }
                                }
                                
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        this.log.error(className + " " + e + " " + e.getLocalizedMessage());
                } 
                return rtn;
        }
        
        /**
         * 
         * @param data
         * @param collectName
         */
        public List<Route> find(List<IQueryTuple> tupleList)
        {
                BasicDBObject query = new BasicDBObject();
                
                for ( IQueryTuple tuple : tupleList) {
                        tuple.getDoucmentQuery(query);
                }
                               
                DBCursor results = _collection.find(query);
                                
                List<Route> rtn = new ArrayList<Route>( );
                
                log.info(query.toString() + " ---> " + results.count());
                while ( results.hasNext()) {                    
                        @SuppressWarnings("unchecked")
                        Object obj = this.translateToDbObject((Map<String,Object>)results.next().toMap());
                        if ( Route.class.isAssignableFrom(obj.getClass())) {
                                rtn.add( Route.class.cast(obj));
                        }
                }
                results.close();
                return rtn;
        }
        
        public long size() {
                return _collection.count();
        }

}