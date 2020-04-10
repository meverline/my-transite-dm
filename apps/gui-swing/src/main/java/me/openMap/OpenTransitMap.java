//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright 2009 by Russ Brasser, Mark Everline and Eric Franklin
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
package me.openMap;

import java.awt.BorderLayout;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import me.factory.DaoBeanFactory;
import me.openMap.command.ICommand;
import me.openMap.command.SearchCommand;
import me.openMap.models.CheckBoxTableCellRenderer;
import me.openMap.models.ColorEditor;
import me.openMap.models.ColorRenderer;
import me.openMap.models.MapViewer;
import me.openMap.models.RouteTableModel;
import me.openMap.models.StopTableModel;
import me.openMap.models.query.ISearchParameters;
import me.openMap.models.query.result.RouteQueryResult;
import me.openMap.models.query.result.TransitStopQueryResult;

/**
 * @author markeverline
 *
 */
public class OpenTransitMap extends JFrame {

	public static final long serialVersionUID = 0x1;

	private JPanel top_;
	private JMenuBar menuBar_ = null;;
	private MapViewer map_;
	private JLabel              searchResults_;
	private JTabbedPane tabbedPane_;
	private JTable stopList_;
	private JTable routeList_;
	private StopTableModel stopsTable_;
	private RouteTableModel routeTable_;
	private JCheckBox showStops_;
	private JCheckBox adaStops_;
	private JComboBox<Double> rangeBox_;

	private JToolBar toolBar_ = null;
	private List<ISearchParameters> searchMethods = new ArrayList<ISearchParameters>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		OpenTransitMap gui = new OpenTransitMap();
		gui.setVisible(true);
	}
	
	public OpenTransitMap()
	{
		//printClassPath();
		DaoBeanFactory.initilize();
		build();
		
		TransiteMapGuiXmlParser aparser = new TransiteMapGuiXmlParser(this);
		aparser.parse("config/gui/OpenTransitMapGui.xml");
		
		this.getContentPane().add(top_);
		this.pack();
		aparser.setSize(this);
	}
	
	public void printClassPath()
	{
		//Get the System Classloader
        ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();

        //Get the URLs
        URL[] urls = ((java.net.URLClassLoader)sysClassLoader).getURLs();

        for(int i=0; i< urls.length; i++)
        {
            System.out.println(urls[i].getFile());
        }       

	}
		
	/**
	 * @return the searchMethods
	 */
	public List<ISearchParameters> getSearchMethods() {
		return searchMethods;
	}
	
	/**
	 * @return the toolBar_
	 */
	public JToolBar getToolBar() {
		return toolBar_;
	}
	
	/**
	 * @return the tabbedPane_
	 */
	public JTabbedPane getTabbedPane() {
		return tabbedPane_;
	}
	
	/**
	 * @return the menuBar_
	 */
	public JMenuBar getApplicationMenuBar() {
		return menuBar_;
	}
	
    /**
     * 
     */
	public void build()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Transit");
		setSize(700, 700);
		
		top_ = new JPanel();
		top_.setLayout(new BorderLayout());
		
		toolBar_ = new JToolBar();
    	top_.add(toolBar_, BorderLayout.NORTH);
		
		menuBar_ = new JMenuBar();

		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		top_.add(pane, BorderLayout.CENTER);
		
		////////////////////////////////////////////////////////////////////
		
		JSplitPane primarySplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		primarySplit.setOneTouchExpandable(true);
		top_.add(primarySplit, BorderLayout.CENTER);
				
		JSplitPane mapResult = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mapResult.setOneTouchExpandable(true);
		primarySplit.setRightComponent(mapResult);
		
		////////////////////////////////////////////////////////////////////

		JPanel searchTab = new JPanel();
		primarySplit.setLeftComponent(new JScrollPane(searchTab));
		
		searchTab.setLayout( new BorderLayout());
		
		JButton searchButton = new JButton("Search");
		
		ICommand command = new SearchCommand();
		command.initilize(this);
		searchButton.addActionListener(command);
		
		searchTab.add(searchButton, BorderLayout.SOUTH);
		
		
		tabbedPane_ = new JTabbedPane();
		searchTab.add(tabbedPane_, BorderLayout.CENTER);
				
		////////////////////////////////////////////////////////////////////
		
		map_ = new MapViewer(this);
		mapResult.setTopComponent(map_);
				
		////////////////////////////////////////////////////////////////////
		
		JTabbedPane searchResults = new JTabbedPane();
		mapResult.setBottomComponent(searchResults);
		
		this.stopsTable_ = new StopTableModel();
		this.stopList_ = new JTable(this.stopsTable_);
		this.stopList_.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.stopList_.setColumnSelectionAllowed(false);
		
		TableColumn includeColumn = this.stopList_.getColumnModel().getColumn(0);
		includeColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
		includeColumn.setCellRenderer( new CheckBoxTableCellRenderer() );
			
		searchResults.addTab( "Stops", new JScrollPane(this.stopList_)); 
		
		JPanel routes = new JPanel();
		routes.setLayout(new BorderLayout());
	
		this.routeTable_ = new RouteTableModel(this);
		this.routeList_ = new JTable(this.routeTable_);
		this.routeList_.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		includeColumn = this.routeList_.getColumnModel().getColumn(RouteTableModel.DISPLAY);
		includeColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
		includeColumn.setCellRenderer( new CheckBoxTableCellRenderer() );
		
		includeColumn = this.routeList_.getColumnModel().getColumn(RouteTableModel.COLOR);
		includeColumn.setCellEditor(new ColorEditor());
		includeColumn.setCellRenderer( new ColorRenderer(true) );
		
		for ( int ndx =  RouteTableModel.AGENCY+1;  ndx < this.routeTable_.getColumnCount(); ndx++) {
			includeColumn = this.routeList_.getColumnModel().getColumn(ndx);
			includeColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
			includeColumn.setCellRenderer( new CheckBoxTableCellRenderer() );
		}
	
		routes.add( new JScrollPane(this.routeList_), BorderLayout.CENTER);
		
		searchResults.addTab( "Route", routes); 
	
		////////////////////////////////////////////////////////////////////
	
		mapResult.setResizeWeight(0.8);
		mapResult.setDividerLocation(0.8);
		
		setJMenuBar(menuBar_);
	}
	
	/**
	 * 
	 * @return
	 */
	public JPanel getTop() {
		return top_;
	}

	/**
	 * 
	 * @return
	 */
	public MapViewer getMap() {
		return map_;
	}

	/**
	 * 
	 * @param text
	 */
	public void setStatusMessage(String text)
	{
		searchResults_.setText(text);
	}
	
	/**
	 * 
	 * @return
	 */
	public ISearchParameters getSearchParameters()
	{
		return searchMethods.get(tabbedPane_.getSelectedIndex());
	}
	
	/**
	 * 
	 * @param node
	 */
	public void setSearchResults(List<TransitStopQueryResult> stops, List<RouteQueryResult> route)
	{
		this.stopsTable_.setTableData(stops);
		this.stopsTable_.fireTableDataChanged();
		
		this.routeTable_.setTableData(route);
		this.routeTable_.fireTableDataChanged();
	}
	
	/**
	 * 
	 * @return
	 */
	public StopTableModel getStopTableModel()
	{
		return stopsTable_;
	}
	
	/**
	 * @return the adaStops_
	 */
	public boolean isShowAdaStops() {
		return this.adaStops_.isSelected();
	}
	
	/**
	 * @return the adaStops_
	 */
	public double adaRangeInMiles() {
		return Double.class.cast(rangeBox_.getSelectedItem()).doubleValue();
	}

	/**
	 * @return the showStops_
	 */
	public boolean isShowStops() {
		return this.showStops_.isSelected();
	}

	/**
	 * @param showStops_ the showStops_ to set
	 */
	public void setShowStops(JCheckBox showStops_) {
		this.showStops_ = showStops_;
	}

	/**
	 * @param adaStops_ the adaStops_ to set
	 */
	public void setAdaStops(JCheckBox adaStops_) {
		this.adaStops_ = adaStops_;
	}

	/**
	 * @param rangeBox_ the rangeBox_ to set
	 */
	public void setRangeBox(JComboBox<Double> rangeBox_) {
		this.rangeBox_ = rangeBox_;
	}
		
}
