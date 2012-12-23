package me.openMap.mapUtils;

import java.util.ArrayList;
import java.util.List;

import me.openMap.utils.IOverlayPainter;
import me.openMap.utils.OveralyChangedEvent;
import me.openMap.utils.OverlayChangedListener;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.painter.AbstractPainter;

public abstract class AbstractMapPainter <T extends JXMapViewer> extends AbstractPainter<T> implements IOverlayPainter {

	private List<OverlayChangedListener> listeners_ = new ArrayList<OverlayChangedListener>();
	private boolean display = false;
	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#isDisplay()
	 */
	@Override
	public boolean isDisplay() {
		return display;
	}

	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#setDisplay(boolean)
	 */
	@Override
	public void setDisplay(boolean display) {
		this.display = display;
	}

	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#addOverlayChangedStateListener(OverlayChangedListener)
	 */
	@Override
	public void addOverlayChangedStateListener(OverlayChangedListener listener)
	{
		if ( listener != null ) {
			listeners_.add(listener);
		}
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#removeOverlayChangedStateListener(OverlayChangedListener)
	 */
	@Override
	public void removeOverlayChangedStateListener(OverlayChangedListener listener)
	{
		if ( listener != null ) {
			listeners_.remove(listener);
		}
	}
	
	/* (non-Javadoc)
	 * @see me.openMap.utils.IOverlay#fireOverlayChangeEvent()
	 */
	@Override
	public void fireOverlayChangeEvent()
	{
		OveralyChangedEvent event = new OveralyChangedEvent(this);
		for ( OverlayChangedListener handler : listeners_)
		{
			handler.overlayChangedEvent(event);
		}
	}

}
