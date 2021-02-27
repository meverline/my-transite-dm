package me.database;

public interface CSVFieldType {
	
	String COMMA = ",";
	
	String toCSVLine();
	void fromCSVLine(String line);

}
