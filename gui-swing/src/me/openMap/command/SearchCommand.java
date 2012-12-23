package me.openMap.command;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.List;

import me.openMap.OpenTransitMap;
import me.openMap.dialogs.ExceptionDialog;
import me.openMap.handlers.DataDisplayHandler;
import me.openMap.models.query.ISearchParameters;

public class SearchCommand implements ICommand {

	private OpenTransitMap top_ = null;

	/**
	 * 
	 * @param top
	 */
	public SearchCommand()
	{	
	}
	
	/*
	 * (non-Javadoc)
	 * @see me.openMap.command.ICommand#initilize(me.openMap.OpenTransitMap)
	 */
	@Override
	public void initilize(OpenTransitMap app) {
		top_  = app;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ISearchParameters parameters = top_.getSearchParameters();
		
		try {
			top_.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
			List<DataDisplayHandler> dataToDisplay = parameters.search();
			
			this.top_.getMap().clearOverlay();
			for ( DataDisplayHandler handler : dataToDisplay) {
				handler.processDataToGui(this.top_);
			}
	
			top_.getMap().done();
			top_.setCursor( Cursor.getDefaultCursor());
		} catch ( Exception ex ) {
			ExceptionDialog.show(this.top_, ex);
		}
	}
	
	@Override
	public void paramenters(List<String> parameters) {
	}

	
}
