package me.transit.database.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import me.database.mongo.IDocument;
import me.transit.database.Agency;
import me.transit.database.Route;
import me.transit.database.Trip;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("Route")
@Entity
@Table(name="tran_route")
public class RouteImpl extends TransitDateImpl implements Route {
		
	@XStreamOmitField
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias(Route.SHORTNAME)
	private String shortName = "";
	
	@XStreamAlias(Route.LONGNAME)
	private String longName = "";
	
	@XStreamAlias(Route.DESC)
	private String desc = "";
	
	@XStreamAlias(Route.TYPE)
	private RouteType type = RouteType.UNKOWN;
	
	@XStreamAlias("url")	
	private String url = "";
	
	@XStreamAlias("color")	
	private String color = "";
	
	@XStreamAlias("textColor")
	private String textColor = "";
	
	@XStreamImplicit(itemFieldName=Route.TRIPLIST)
	private List<Trip> trips = new ArrayList<Trip>();
	
	/**
	 * @return the shortName
	 */
	
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the longName
	 */
	@Column(name="LONG_NAME", nullable=false)
	public String getLongName() {
		return longName;
	}

	/**
	 * @param longName the longName to set
	 */
	public void setLongName(String longName) {
		this.longName = longName;
	}

	/**
	 * @return the desc
	 */
	@Column(name="DESCRIPTION")
	@Type(type = "text")
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the type
	 */
	@Column(name="TYPE", nullable=false)
	@Enumerated(EnumType.STRING) 
	public RouteType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(RouteType type) {
		this.type = type;
	}

	/**
	 * @return the url
	 */
	@Column(name="URL")
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the color
	 */
	@Column(name="COLOR")
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the textColor
	 */
	@Column(name="TEXT_COLOR")
	public String getTextColor() {
		return textColor;
	}

	/**
	 * @param textColor the textColor to set
	 */
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	
	/**
	 * @return the textColor
	 */
	@OneToMany(mappedBy="Route")
	@OrderBy("TRIP_INDX")
	public List<Trip> getTripList()
	{
		return this.trips;
	}

	/**
	 * @param textColor the textColor to set
	 */
	public void setTripList(List<Trip> list) {
		this.trips = list;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder( "Route: {" + super.toString() + "}");
		
		builder.append("shortName: " + this.getShortName());
		builder.append("\n");
		builder.append("longName: " + this.getLongName());
		builder.append("\n");
		builder.append("desc: " + this.getDesc());
		builder.append("\n");
		builder.append("type: " + this.getType());
		builder.append("\n");
		builder.append("url: " + this.getUrl());
		builder.append("\n");
		builder.append("color: " + this.getColor());
		builder.append("\n");
		builder.append("text Color: " + this.getTextColor());
		return builder.toString();
	}
		
	/**
	 * 
	 */
	public boolean valid() 
	{
		return true;
	}
	
    @Override
    public Map<String, Object> toDocument() {
            
            Map<String,Object> rtn = new HashMap<String,Object>();

            rtn.put(IDocument.CLASS, RouteImpl.class.getName());
            if ( this.getShortName().isEmpty()) {
            	rtn.put(IDocument.ID, this.getLongName() + ": " + this.getAgency().getName());
            } else {
            	rtn.put(IDocument.ID, this.getShortName() + ": " + this.getAgency().getName());
            }
            
            rtn.put( Agency.AGENCY, this.getAgency().getName());
            rtn.put( Agency.UUID, this.getUUID());
            rtn.put( Route.SHORTNAME, this.getShortName());
            rtn.put( Route.LONGNAME, this.getLongName());
            rtn.put( Route.DESC, this.getDesc());
            rtn.put( Route.TYPE, this.getType().name());
            return rtn;
    }
    
    @Override
    public void handleEnum(String key, Object value)
    {
            if ( key.equals(Route.TYPE) ) {
                    this.setType( Route.RouteType.valueOf(value.toString()));
            }
    }
	
}
