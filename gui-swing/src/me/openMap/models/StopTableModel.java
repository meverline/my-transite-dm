package me.openMap.models;

import java.util.HashSet;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import me.openMap.models.query.result.TransitStopQueryResult;
import me.transit.dao.neo4j.GraphDatabaseDAO;
import me.transit.database.RouteStopData;
import me.transit.database.TransitStop;

public class StopTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private List<TransitStopQueryResult> tableData_ = null; 
	private Class<?> columanClass_[] = { Boolean.class, String.class, String.class };
	private String columanName_[] = { " ", "Stop", "Route(s)" };
	
	/**
	 * @return the tableData_
	 */
	public List<TransitStopQueryResult> getTableData() {
		return tableData_;
	}

	/**
	 * @param tableData_ the tableData_ to set
	 */
	public void setTableData(List<TransitStopQueryResult> tableData_) {
		this.tableData_ = tableData_;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return columanName_[column];
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		return columanClass_[columnIndex];
	}

	@Override
	public int getRowCount() {
		if (this.getTableData() == null ||  this.getTableData().isEmpty() ) {
			return 0;
		}
		return this.getTableData().size();
	}

	@Override
	public int getColumnCount() {
		return this.columanName_.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		TransitStop row = this.getTableData().get(rowIndex).getStop();
		GraphDatabaseDAO db = GraphDatabaseDAO.instance();

		List<RouteStopData> list = db.findRoutes(row);
		switch(columnIndex)
		{
			case 0:
				return this.getTableData().get(rowIndex).getOverlay().isDisplay();  
			case 1:
				if ( row.getDesc().length() > row.getName().length() ) {
					return row.getDesc();
				} else {
					return row.getName();
				}
			case 2:
				StringBuilder builder = new StringBuilder();
				int ndx = 0;
				
				if ( ! list.isEmpty() ) {
					HashSet<String> map = new HashSet<String>();
					for (RouteStopData r : list ) {
						if ( ! map.contains(r.getRouteShortName()) ) {
							if ( ndx != 0 ) { builder.append(", "); }
							builder.append(r.getRouteShortName());
							ndx++;
							map.add(r.getRouteShortName());
						}
					}
				}
				return builder.toString();
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if ( columnIndex == 0 ) {
			return true;
		}
		return false;
	}
	
	public void setValueAt(Object value, int row, int col) {
		 
		 if ( col == 0 ) {
			 Boolean boolValue = Boolean.class.cast(value);
			 
			 getTableData().get(row).getOverlay().setDisplay(boolValue.booleanValue());
			 getTableData().get(row).getOverlay().fireOverlayChangeEvent(); 
		 }
	       	
	     fireTableCellUpdated(row, col);
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	 
	public class DisplayFlag { 
		public boolean value = true;
		
		public DisplayFlag(boolean value) {
			this.value = value;
		}
		
		public Boolean getValue() {
			return new Boolean(value);
		}
		
		public boolean booleanValue()
		{
			return value;
		}
	}

}
