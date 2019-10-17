package me.transit.json;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

public class Base64StringToGeometry extends StdConverter<String, Geometry> {

	private final Log log = LogFactory.getLog(getClass().getName());
	
	@Override
	public Geometry convert(String value) {
		WKBReader georeader = new WKBReader();
		Geometry rtn = null;
		
		byte[] backToBytes = Base64.decodeBase64(value);
		try {
			rtn =  georeader.read(backToBytes);
		} catch (ParseException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return rtn;
	}

}
