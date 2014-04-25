package me.database.mongo;

import java.util.Map;

public interface IDocument {

    public final static String ID = "_id";
    public final static String CLASS = "@class";
    public final static String REF = "$ref";
    /**
     * 
     * @return
     */
    public Map<String,Object> toDocument();
    
    public void handleEnum(String key, Object value);
}
