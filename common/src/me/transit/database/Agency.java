package me.transit.database;

import java.io.Serializable;

import me.transit.dao.mongo.IDocument;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Agency")
public interface Agency extends Serializable, IDocument {

	public static final String AGENCY = "agency";
	public static final String UUID = "uuid";

	/**
	 * @return the uuid
	 */
	public long getUUID();
	
	/**
	 * @return the uuid
	 */
	public void setUUID(long id);
	
	/**
	 * 
	 * @return
	 */
	public String getVersion();
	
	/**
	 * 
	 * @param id
	 */
	public void setVersion(String id);

	/**
	 * @return the name
	 */
	public String getName();

	/**
	 * @param name the name to set
	 */
	public void setName(String name);
	/**
	 * @return the url
	 */
	public String getUrl();

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url);

	/**
	 * @return the timezone
	 */
	public String getTimezone();

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone);

	/**
	 * @return the lang
	 */
	public String getLang();

	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang);

	/**
	 * @return the phone
	 */
	public String getPhone() ;

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone);

	/**
	 * @return the id
	 */
	public String getId() ;

	/**
	 * @param id the id to set
	 */
	public void setId(String id);
	
}
