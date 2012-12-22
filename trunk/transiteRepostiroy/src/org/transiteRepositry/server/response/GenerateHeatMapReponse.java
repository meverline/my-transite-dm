package org.transiteRepositry.server.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("GenerateHeatMapReponse")
public class GenerateHeatMapReponse {

	private String url = null;
	
	/**
	 * 
	 */
	public GenerateHeatMapReponse() {}
	
	/**
	 * 
	 * @param points
	 */
	public GenerateHeatMapReponse(String url)
	{
		this.url = url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
