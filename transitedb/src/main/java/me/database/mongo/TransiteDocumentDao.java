package me.database.mongo;

import java.net.UnknownHostException;

import me.transit.database.Agency;
import me.transit.database.StopTime;
import me.transit.database.impl.AgencyImpl;

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
    public static synchronized DocumentDao instance() throws UnknownHostException {
            if ( _theOne == null ) {
                    _theOne = new DocumentDao(null);
            }
            return _theOne;
    }
    
    protected void translatePrimative(Object value, String key, Object rtn)
    {
    	Object setValue = value;
        if ( key.equals(Agency.AGENCY) ) {
                setValue = new AgencyImpl(value.toString());
        } 
        super.translatePrimative(setValue, key, rtn);
    }
}
