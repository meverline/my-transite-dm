package me.openMap.utils;

import java.util.EventListener;

public interface OverlayChangedListener extends EventListener {
	
	public void overlayChangedEvent(OveralyChangedEvent event);
}