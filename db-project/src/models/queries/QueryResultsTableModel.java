package models.queries;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class QueryResultsTableModel extends AbstractTableModel {
	ResultSet results;

	public QueryResultsTableModel() {
	}
	
	public QueryResultsTableModel(ResultSet results) {
		this.results = results;
		fireTableStructureChanged();
	}

	public int getColumnCount() {
		int count = 0;
		try {
		count = results.getMetaData().getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public Class getColumnClass(int column) {
		String typeName = "";
		try {
			typeName = results.getMetaData().getColumnClassName(column+1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			return Class.forName(typeName);
		} catch (ClassNotFoundException e) {
			return Object.class;
		}
	}
	
	public String getColumnName(int column) {
		String name = "";
		try {
			name = results.getMetaData().getColumnName(column + 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public int getRowCount() {
		int rows = 0;
		try {
			results.last();
			rows = results.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		fireTableStructureChanged();
		return rows;
	}
	
	public Object getValueAt(int row, int column) {
		Object value = null;
		try {
			results.absolute(row + 1);
			value = results.getObject(column + 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}
}
