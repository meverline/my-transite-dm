package me.transit.dao.mongo;

import java.util.List;

public class JongoQueryBuilder {
	
	public static final String NOT = "$not";
	public static final String AND = "$and";
	public static final String OR = "$or";
	
	protected static final String BEGIN = "{ ";
	protected static final String END = " }";
	protected static final String SEPERATOR = " : ";
	
    private static final String GEOWITHIN = "$geoWithin ";
    private static final String GEOMETRY = "$geometry ";
    private static final String COORDINATES = "coordinates ";
    private static final String TYPE = "type ";
    private static final String ARRAY_START = "[ ";
    private static final String ARRAY_END = " ]";
    private static final String COMMA = " , ";   
	
	private StringBuilder query_ = new StringBuilder();
		
	/**
	 * 
	 * @param key
	 */
	public void start(String key) {
		query_.append( JongoQueryBuilder.BEGIN );
		query_.append(key);
		query_.append( JongoQueryBuilder.SEPERATOR );
		
	}

	/**
	 * 
	 */
	public void end() {
		query_.append( JongoQueryBuilder.END );
	}
	
	/**
	 * 
	 * @param value
	 */
    public void is(Object value) {
    	query_.append(value);
    }
    
    /**
     * 
     * @param op
     * @param value
     */
    public void addOpperand(String op, Number value) {
    	start(op);
			query_.append(value.toString());
  	    end();
    }
    
    /**
     * 
     * @param op
     * @param value
     */
    public void addOpperand(String op, String value) {
    	start(op);
		   query_.append(value.toString());
  	    end();
    }
    

    
    /**
     * 
     * @param points
     */
    public void within( List<Double []> points) {
    	start(JongoQueryBuilder.GEOWITHIN);
	    	start(JongoQueryBuilder.GEOMETRY);
		    	start(JongoQueryBuilder.TYPE);
		    	if ( points.size() == 2 ) {
		    		query_.append("Rectangle, ");
		    	} else {
		    		query_.append("Poloygon, ");
		    	}
		    	query_.append(JongoQueryBuilder.COORDINATES);
		    	query_.append(JongoQueryBuilder.SEPERATOR );
				
		    	for ( Double[] array : points) {
		    		query_.append(JongoQueryBuilder.ARRAY_START);
		    		query_.append(array[0]);
		    		query_.append(JongoQueryBuilder.COMMA);
		    		query_.append(array[1]);
		    		query_.append(JongoQueryBuilder.ARRAY_END);
		    	}
		    	end();
	    	end();
  	    end();
    }
    
    /**
     * 
     * @param point
     * @param distance
     */
    public void within( Double [] point, double distance) {
    	start(JongoQueryBuilder.GEOWITHIN);
	    	start(JongoQueryBuilder.GEOMETRY);
		    	start(JongoQueryBuilder.TYPE);
		    	query_.append("Point, ");
		    	query_.append(JongoQueryBuilder.COORDINATES);
		    	query_.append(JongoQueryBuilder.SEPERATOR );
				
		    	query_.append(JongoQueryBuilder.ARRAY_START);
				query_.append(point[0]);
				query_.append(JongoQueryBuilder.COMMA);
				query_.append(point[1]);
				query_.append(JongoQueryBuilder.ARRAY_END);
		      	end();
	    	end();
	    	query_.append(JongoQueryBuilder.COMMA);
	    	query_.append("$maxDistance ");
	    	query_.append(distance);
  	    end();
    }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return query_.toString();
	}
	
}
