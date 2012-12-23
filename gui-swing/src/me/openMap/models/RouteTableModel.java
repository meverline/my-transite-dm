package me.openMap.models;

import java.awt.Color;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import me.factory.DaoBeanFactory;
import me.openMap.OpenTransitMap;
import me.openMap.models.query.result.RouteQueryResult;
import me.openMap.utils.RouteOverlay;
import me.transit.dao.DaoException;
import me.transit.dao.RouteDao;
import me.transit.database.Route;
import me.transit.database.ServiceDate;
import me.transit.database.Trip;

public class RouteTableModel extends AbstractTableModel {
	
	private enum ColorToString {
		
		RED(Color.RED),
		BLUE(Color.BLUE),
		YELLOW(Color.YELLOW),
		GREEN(Color.GREEN),
		ORANGE(Color.ORANGE),
		BLACK(Color.BLACK),
		CYAN(Color.CYAN),
		DARK_GRAY(Color.DARK_GRAY),
		GRAY(Color.GRAY),
		LIGHT_GRAY(Color.LIGHT_GRAY),
		MANGENTA(Color.MAGENTA),
		PINK(Color.PINK),
		WHITE(Color.WHITE),
		SILVER(Color.LIGHT_GRAY);
		
		private Color clr = null;
		
		ColorToString(Color clr) {
			this.clr = clr;
		}
		
		public Color getColor() { return clr; }
	}
	
	public static final int DISPLAY = 0;
	public static final int COLOR = 1;
	public static final int ROUTE_NAME = 2;
	public static final int LONG_NAME = 3;
	public static final int ROUTETYPE = 4;
	public static final int AGENCY = 5;
	
	private static final long serialVersionUID = 1L;
	private List<RouteQueryResult> tableData_ = null; 
	private List<DisplayCell> flags = new ArrayList<DisplayCell>();
	private OpenTransitMap top_ = null;
	
	private Class<?> columanClass_[] = { Boolean.class, 
										 Color.class,
										 String.class, 
										 String.class,
										 String.class,
										 String.class, 
										 Boolean.class,
										 Boolean.class,
										 Boolean.class,
										 Boolean.class,
										 Boolean.class,
										 Boolean.class,
										 Boolean.class,
										 };
	
	private String columanName_[] = { " ", 
									  "Color",
									  "Route", 
									  "Name",
									  "Type", 
									  "Agency", 
									  "Su",
									  "Mo",
									  "Tu",
									  "We",
									  "Th",
									  "Fr",
									  "Sa" 
									   };
	

	
	public RouteTableModel(OpenTransitMap gui)
	{
		top_ = gui;
	}
	
	/**
	 * @return the tableData_
	 */
	public List<RouteQueryResult> getTableData() {
		return tableData_;
	}

	/**
	 * @param tableData_ the tableData_ to set
	 */
	public void setTableData(List<RouteQueryResult> tableData_) {
		this.tableData_ = tableData_;
		flags.clear();
		for ( RouteQueryResult result : tableData_ ) {
			DisplayCell cell = new DisplayCell(false);
			this.getServiceTypes(cell, result.getRouteNumber(), result.getAgency().getName());
			flags.add(cell);
		}
	}
	
	public boolean getDisplayFlag(int rowIndex)
	{
		return flags.get(rowIndex).booleanValue();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return columanName_[column];
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columanClass_[columnIndex];
	}

	@Override
	public int getRowCount() {
		if ( this.getTableData() == null || this.getTableData().isEmpty() ) {
			return 0;
		}
		return this.getTableData().size();
	}

	@Override
	public int getColumnCount() {
		return columanName_.length;
	}
	
	private void getServiceTypes(DisplayCell cell, String route, String agencyName)
	{
		try {

			RouteDao dao =
				RouteDao.class.cast(DaoBeanFactory.create().getDaoBean(RouteDao.class));

			List<Route> list = dao.findByRouteNumber(route, agencyName);
			int serviceDays = 0x0;
			int ndx  = 0;
			for ( Route rt : list ) {
				for ( Trip trip : rt.getTripList()) {
					
					ServiceDate service = trip.getService();
					
					for ( ServiceDate.WeekDay value : ServiceDate.WeekDay.values()) {
						if ( service.hasService(value)) {
							serviceDays |= value.getBit();
						}
					}
				}
				if ( ndx == 0 ) {
					cell.setRouteType(rt.getType().toString());
				}
				ndx++;
			}
			
			cell.setService(serviceDays);
		
		} catch (DaoException e) {
			return;
		}
		return;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		RouteQueryResult row = this.getTableData().get(rowIndex);
		DisplayCell cell = flags.get(rowIndex);
		switch(columnIndex)
		{
			case RouteTableModel.DISPLAY:
				return cell.getValue();
			case RouteTableModel.ROUTETYPE:
				return cell.getRouteType();
			case RouteTableModel.COLOR:
				return cell.getColor();
			case RouteTableModel.ROUTE_NAME:
				return row.getRouteNumber();
			case RouteTableModel.LONG_NAME:
				return row.getTripHeadSign();
			case RouteTableModel.AGENCY:
				return row.getAgency().getName();
		}
		
		int ndx = (columnIndex - RouteTableModel.AGENCY)-1;
		ServiceDate.WeekDay day = ServiceDate.WeekDay.values()[ndx];
		return new Boolean( cell.hasService(day));
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if ( columnIndex == RouteTableModel.DISPLAY ||  columnIndex == RouteTableModel.COLOR) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 */
	public void setValueAt(Object value, int row, int col) {
		 
		 top_.setCursor( Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		 if ( col == RouteTableModel.DISPLAY ) {
			 Boolean boolValue = Boolean.class.cast(value);
			 boolean old = flags.get(row).value;
			 flags.get(row).value =boolValue.booleanValue();
			 
			 if ( old != boolValue.booleanValue()) {
				 if ( getTableData().get(row).getOverlay().isEmpty() ) {
					 this.createOverlay(getTableData().get(row), flags.get(row));
				 }
					 
			     for ( RouteOverlay overlay : getTableData().get(row).getOverlay()) {
			    	overlay.setDisplay(boolValue.booleanValue()) ;
			    	overlay.fireOverlayChangeEvent();
			     }
			 }
		 } else if ( col == RouteTableModel.COLOR ) {
			 Color clr = Color.class.cast(value);
			 for ( RouteOverlay overlay : getTableData().get(row).getOverlay()) {
				 overlay.setColor(clr);
			     overlay.fireOverlayChangeEvent();
			 }
			 flags.get(row).setColor(clr);
		 }
	       	
	     fireTableCellUpdated(row, col);
	     top_.setCursor( Cursor.getDefaultCursor());
	}
	
	/**
	 * 
	 * @param data
	 */
	private void createOverlay(RouteQueryResult data, DisplayCell cell)
	{
		try {
			RouteDao dao =
				RouteDao.class.cast(DaoBeanFactory.create().getDaoBean(RouteDao.class));

			List<Route> list = dao.findByRouteNumber(data.getRouteNumber(), 
													 data.getAgency().getName());
			for ( Route rt : list ) {
				
				Color value = null;
				if ( rt.getColor() != null && rt.getColor().length() > 0 ) {
					ColorToString clr = ColorToString.valueOf(rt.getColor().toUpperCase());
					if ( clr != null ) {
						value = clr.getColor();
					}
				} else {
					value = cell.getColor();
				}
				
				if ( ! rt.getTripList().isEmpty() ) {
					Trip inbound = null;
					Trip outBound = null;
					
					for ( Trip trip : rt.getTripList() ) {
						if ( trip.getShape() != null ) {
							if ( inbound == null && trip.getDirectionId() == Trip.DirectionType.IN_BOUND ) {
								RouteOverlay overlay = new RouteOverlay( trip.getShape().getShape(),
																		 data.getAgency(),
																		 rt.getShortName());
								if ( value != null ) { overlay.setColor(value); }
								data.getOverlay().add( overlay);
								top_.getMap().addPainter(overlay);
								inbound = trip;
							}
							
							if ( outBound == null && trip.getDirectionId() == Trip.DirectionType.OUT_BOUND ) {
								RouteOverlay overlay = new RouteOverlay( trip.getShape().getShape(),
										 data.getAgency(),
										 rt.getShortName());
								if ( value != null ) { overlay.setColor(value); }
								data.getOverlay().add( overlay);
								top_.getMap().addPainter(overlay);
								outBound = trip;
							}
						}
					}
								
				}
			}
			top_.getMap().done();
		
		} catch (DaoException e) {

		}
		return;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	 
	public class DisplayCell { 
		public boolean value = true;
		private Color   color = Color.BLACK;
		private String  routeType = "";
		private int serviceFlag = 0x0;
		
		public DisplayCell(boolean value) {
			this.value = value;
			
		}
		
		public Boolean getValue() {
			return new Boolean(value);
		}
		
		public boolean booleanValue()
		{
			return value;
		}

		/**
		 * @return the color
		 */
		public Color getColor() {
			return color;
		}

		/**
		 * @param color the color to set
		 */
		public void setColor(Color color) {
			this.color = color;
		}

		/**
		 * @return the service
		 */
		public boolean hasService( ServiceDate.WeekDay day) {
			return ((this.serviceFlag & day.getBit()) == day.getBit());
		}

		/**
		 * @param service the service to set
		 */
		public void setService(int service) {
			this.serviceFlag = service;
		}

		/**
		 * @return the routeType
		 */
		public String getRouteType() {
			return routeType;
		}

		/**
		 * @param routeType the routeType to set
		 */
		public void setRouteType(String routeType) {
			this.routeType = routeType;
		}
		
	}


}
