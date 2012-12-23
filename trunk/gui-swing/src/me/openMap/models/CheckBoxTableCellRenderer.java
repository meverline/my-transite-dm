package me.openMap.models;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CheckBoxTableCellRenderer implements TableCellRenderer {

	/*
	 * (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean isFocused, int row,
			int col) {
		boolean marked = (Boolean) value;
		JCheckBox rendererComponent = new JCheckBox();
		if (marked) {
			rendererComponent.setSelected(true);
		}
		return rendererComponent;
	}
}
