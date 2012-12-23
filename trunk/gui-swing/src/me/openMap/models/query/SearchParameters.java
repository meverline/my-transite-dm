//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright © 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

/**
 *
 */
package me.openMap.models.query;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import me.openMap.OpenTransitMap;
import me.openMap.handlers.DataDisplayHandler;

/**
 * @author markeverline
 *
 */
public class SearchParameters extends AbstractSearchParameters {

	private static final long serialVersionUID = 1L;

	public SearchParameters(OpenTransitMap map) {
		setLayout(new BorderLayout());
		
	}
	
	@Override
	public void initilize(OpenTransitMap map, List<String> sampleList) {
		build(map);
	}

	public String getName() { return "Date/Time"; }

	private void build(OpenTransitMap map) {

		JPanel fields = new JPanel();
		fields.setLayout(new GridLayout(3, 2));
		add(fields, BorderLayout.NORTH);

		// 1. Start Date
		fields.add(new JLabel("Start Date"));

		fields.add( this.getStartDatePanel() );

		fields.add(new JLabel("End Date"));
		fields.add( this.getEndDatePanel() );

	}
	
	/* (non-Javadoc)
	 * @see me.openMap.models.query.ISearchParameters#search()
	 */
	@Override
	public List<DataDisplayHandler> search() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getResultType() {
		return "Date/Time";
	}

}
