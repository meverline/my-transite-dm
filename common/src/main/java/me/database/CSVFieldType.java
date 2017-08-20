package me.database;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public interface CSVFieldType {
	
	@XStreamOmitField
	public static final String COMMA = ",";
	
	public String toCSVLine();
	public void fromCSVLine(String line);

}
