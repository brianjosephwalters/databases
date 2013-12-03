package ui.swing.panels;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import db.DBConnection;

import models.queries.QueryResultsTableModel;

@SuppressWarnings("serial")
public class QueryResultsPanel extends JPanel{
	QueryResultsTableModel tableModel;
	JTable table;
	JLabel lTitle;
	
	public QueryResultsPanel(String label) {		
		setLayout(new BorderLayout());
		this.lTitle = new JLabel(label);
		add(lTitle, BorderLayout.NORTH);
		
		table = new JTable();
		add(new JScrollPane(table), BorderLayout.CENTER);
	}
	
	public void setResultsSet(ResultSet results) {
		tableModel = new QueryResultsTableModel(results);
		table.setModel(tableModel);
	}
	
	public void setLabel(String label) {
		this.lTitle.setText(label);
	}
	
	public static void main (String[] args) {
		ResultSet results = null;
		try {
			Connection conn = DBConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT * " +
					"FROM person", 
					ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
			results = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame();
		QueryResultsPanel panel = new QueryResultsPanel("Person at Comany 100");
		panel.setResultsSet(results);
		frame.add(panel);
		
		frame.setTitle("PersonTable");
		frame.setSize(600, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
