package me.openMap.command;

import java.awt.event.ActionEvent;
import java.util.List;

import me.openMap.OpenTransitMap;
import me.openMap.models.query.result.TransitStopQueryResult;

public class ShowAddActionListener implements ICommand {

	private OpenTransitMap top_ = null;
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		for ( TransitStopQueryResult result : top_.getStopTableModel().getTableData() ) {
			result.getOverlay().setAdaLimits( top_.isShowAdaStops());
			result.getOverlay().setAdaRangeMeters( top_.adaRangeInMiles() * 1609.344);
		}
		top_.getMap().redraw();

	}

	/*
	 * (non-Javadoc)
	 * @see me.openMap.command.ICommand#initilize(me.openMap.OpenTransitMap)
	 */
	@Override
	public void initilize(OpenTransitMap app) {
		top_  = app;
	}

	@Override
	public void paramenters(List<String> parameters) {
	}


}
