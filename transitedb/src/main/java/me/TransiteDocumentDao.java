package me;

import java.net.UnknownHostException;

import me.database.mongo.DocumentDao;
import me.database.mongo.IDocumentDao;
import me.transit.database.Agency;
import me.transit.database.StopTime;

public class TransiteDocumentDao extends DocumentDao {

	
	TransiteDocumentDao() throws UnknownHostException
	{
		super(null);
		this.addSkipField(StopTime.LOCATION);
	}
	
    /**
     * 
     * @return
     * @throws UnknownHostException
     */
    public static synchronized IDocumentDao instance() throws UnknownHostException {
            if ( _theOne == null ) {
                    _theOne = new DocumentDao(null);
            }
            return _theOne;
    }
    
    protected void translatePrimative(Object value, String key, Object rtn)
    {
    	Object setValue = value;
        if ( key.equals(Agency.AGENCY) ) {
                setValue = new Agency(value.toString());
        } 
        super.translatePrimative(setValue, key, rtn);
    }
}
