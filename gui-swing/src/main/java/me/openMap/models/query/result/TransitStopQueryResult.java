package me.openMap.models.query.result;

import me.openMap.utils.StopOverlay;
import me.transit.database.TransitStop;

public class TransitStopQueryResult {

	private TransitStop stop = null;
	private StopOverlay overlay = null;
	
	public TransitStopQueryResult(TransitStop aStop, StopOverlay aOverlay)
	{
		this.stop = aStop;
		this.overlay = aOverlay;
	}
	/**
	 * @return the stop
	 */
	public TransitStop getStop() {
		return stop;
	}
	/**
	 * @param stop the stop to set
	 */
	public void setStop(TransitStop stop) {
		this.stop = stop;
	}
	/**
	 * @return the overlay
	 */
	public StopOverlay getOverlay() {
		return overlay;
	}
	/**
	 * @param overlay the overlay to set
	 */
	public void setOverlay(StopOverlay overlay) {
		this.overlay = overlay;
	}
	
	
}
