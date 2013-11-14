package ui.swing.test;

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
	
	public QueryResultsPanel(ResultSet results, String label) {		
		setLayout(new BorderLayout());
		add( new JLabel(label), BorderLayout.NORTH);
		
		tableModel = new QueryResultsTableModel(results);
		table = new JTable(tableModel);
		add(new JScrollPane(table), BorderLayout.CENTER);
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
		frame.add(new QueryResultsPanel(results, "Persons at Company 100"));

		frame.setTitle("PersonTable");
		frame.setSize(600, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
