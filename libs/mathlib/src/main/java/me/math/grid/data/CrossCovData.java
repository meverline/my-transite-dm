package me.math.grid.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import me.math.Vertex;

@JsonRootName(value = "CrossCovData")
public class CrossCovData {

	private double latitude = 0.0;
	private double longitude = 0.0;
    private int number = 0;
    private Vertex avgPoint;
    
    public CrossCovData() {
    }
    
    /**
     * 
     * @param pt
     */
    public CrossCovData(Vertex pt) {
 	   avgPoint = pt;
    }
     
    /**
     * 
     * @param pt
     */
    public void addPoint(Vertex pt) {
 	   number++;
 	   latitude += pt.getLatitudeDegress() - avgPoint.getLatitudeDegress();
       longitude += pt.getLongitudeDegress() - avgPoint.getLongitudeDegress();
    }
    
    /**
     * 
     * @return
     */
    public double crossCovariance() {
        latitude = latitude / ( number - 1.0);
        longitude = longitude / ( number - 1.0);
        return Math.abs((latitude * longitude) / ( number - 1.0));
    }

	/**
	 * @return the latitude
	 */
    @JsonGetter("latitude")
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
    @JsonSetter("latitude")
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
    @JsonGetter("longitude")
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
    @JsonSetter("longitude")
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the number
	 */
	@JsonGetter("number")
	public int getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	@JsonSetter("number")
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return the avgPoint
	 */
	@JsonGetter("avgPoint")
	public Vertex getAvgPoint() {
		return avgPoint;
	}

	/**
	 * @param avgPoint the avgPoint to set
	 */
	@JsonSetter("avgPoint")
	public void setAvgPoint(Vertex avgPoint) {
		this.avgPoint = avgPoint;
	}
    
    
    
}
