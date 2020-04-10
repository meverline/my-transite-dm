package me.transit.json;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKBWriter;

public class GeometryToBase64String extends StdConverter<Geometry, String> {

	@Override
	public String convert(Geometry value) {
		WKBWriter geowritter = new WKBWriter();
		
		byte[] array = geowritter.write(value);
		return Base64.encodeBase64String(array);
	}

}
