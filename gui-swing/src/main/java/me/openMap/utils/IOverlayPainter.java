package me.openMap.utils;


public interface IOverlayPainter {

	/**
	 * 
	 * @return
	 */
	public boolean isDisplay();
	
	/**
	 * 
	 * @param display
	 */
	public void setDisplay(boolean display);
	
	/**
	 * 
	 * @param listener
	 */
	public void addOverlayChangedStateListener(OverlayChangedListener listener);
	
	/**
	 * 
	 * @param listener
	 */
	public void removeOverlayChangedStateListener(OverlayChangedListener listener);
	
	/**
	 * 
	 */
	public void fireOverlayChangeEvent();
}
