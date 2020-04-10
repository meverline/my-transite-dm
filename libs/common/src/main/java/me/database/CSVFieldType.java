package me.database;

public interface CSVFieldType {
	
	public static final String COMMA = ",";
	
	public String toCSVLine();
	public void fromCSVLine(String line);

}
