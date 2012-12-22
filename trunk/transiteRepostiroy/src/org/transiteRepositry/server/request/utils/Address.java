package org.transiteRepositry.server.request.utils;

import me.utils.AddressToCoordinate;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.vividsolutions.jts.geom.Point;

@XStreamAlias("Address")
public class Address {

	@XStreamAlias("number")
	private int number;
	@XStreamAlias("street")
	private String street;
	@XStreamAlias("state")
	private String state;
	@XStreamAlias("city")
	private String city;
	@XStreamAlias("zipCode")
	private int zipCode;
	
	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}
	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the zipCode
	 */
	public int getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}
	
	public Point geoCode()
	{
		AddressToCoordinate coder = new AddressToCoordinate();
		
		return coder.geoCode(getNumber(), 
							 getStreet(), 
							 getCity(), 
							 getState(), 
							 getZipCode());
	}
	
	

}
