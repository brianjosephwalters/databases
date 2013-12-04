package ui.swing.panels;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import db.DBConnection;
import models.queries.QueryResultsTableModel;
import models.queries.RequiredQueries;

import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class QueryResultsPanel extends JPanel{
	QueryResultsTableModel tableModel;
	JTable table;
	
	public QueryResultsPanel(String label) {
		setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		JScrollPane pane = new JScrollPane(table);
		add(pane);
	}
	
	public void setResultsSet(ResultSet results) {
		tableModel = new QueryResultsTableModel(results);
		tableModel.fireTableStructureChanged();
		table.setModel(tableModel);
		redrawTable();
	}
	
	public void setLabel(String label) {
		this.setBorder(new TitledBorder(null, label, TitledBorder.LEADING, TitledBorder.TOP, null, null));
	}
	
	private void redrawTable() {
		//int columnCount = table.getColumnCount();
		//int tableWidth = table.getWidth();
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setPreferredSize(this.getSize());
		//for (int i = 0; i < columnCount; i++ ) {
		//	TableColumn header = table.getTableHeader().getColumnModel().getColumn(i);
		//	TableColumn column = table.getColumnModel().getColumn(i);
		//	header.setPreferredWidth(tableWidth/columnCount);
		//	column.setPreferredWidth(tableWidth/columnCount);
		//}
		this.doLayout();
	}
	
	public static void main (String[] args) {
		ResultSet results = null;
		try {
			Connection conn = DBConnection.getConnection();
			RequiredQueries r = new RequiredQueries(conn);
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT * " +
					"FROM person", 
					ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
			//results = stmt.executeQuery();
			results = r.getCompanyEmployees("400");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame();
		QueryResultsPanel panel = new QueryResultsPanel("Person at Comany 100");
		panel.setResultsSet(results);
		frame.getContentPane().add(panel);
		
		frame.setTitle("PersonTable");
		frame.setSize(600, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
