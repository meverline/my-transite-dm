package org.transiteRepositry.server.request.utils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Cluster")
public class Cluster {

	@XStreamAlias("hiRange")
	private int hiRange;
	@XStreamAlias("lowRange")
	private int lowRange;
	
	/**
	 * @return the hiRange
	 */
	public int getHiRange() {
		return hiRange;
	}
	/**
	 * @param hiRange the hiRange to set
	 */
	public void setHiRange(int hiRange) {
		this.hiRange = hiRange;
	}
	/**
	 * @return the lowRange
	 */
	public int getLowRange() {
		return lowRange;
	}
	/**
	 * @param lowRange the lowRange to set
	 */
	public void setLowRange(int lowRange) {
		this.lowRange = lowRange;
	}

}
