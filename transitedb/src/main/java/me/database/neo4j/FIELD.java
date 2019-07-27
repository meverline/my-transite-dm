package me.database.neo4j;

public enum FIELD {
	db_id(true), 
	agency(true), 
	stop(true),
	trip(true),
	trip_headSign(true) ,
	db_name(true), 
	className(false),
	direction(false),
	route(true) , 
	coordinate(true) ;
	
	private boolean index;
	FIELD(boolean toIndex) { this.index = toIndex; }	
	public boolean isIndex() { return index; }

}
