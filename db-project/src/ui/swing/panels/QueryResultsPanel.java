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
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class QueryResultsPanel extends JPanel{
	QueryResultsTableModel tableModel;
	JTable table;
	
	public QueryResultsPanel(String label) {
		setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		table = new JTable();
		add(new JScrollPane(table));
	}
	
	public void setResultsSet(ResultSet results) {
		tableModel = new QueryResultsTableModel(results);
		table.setModel(tableModel);
	}
	
	public void setLabel(String label) {
		this.setBorder(new TitledBorder(null, label, TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
		frame.getContentPane().add(panel);
		
		frame.setTitle("PersonTable");
		frame.setSize(600, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
