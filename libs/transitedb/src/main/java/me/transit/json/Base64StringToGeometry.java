package me.transit.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.codec.binary.Base64;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;

@CommonsLog
public class Base64StringToGeometry extends StdConverter<String, Geometry> {

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
