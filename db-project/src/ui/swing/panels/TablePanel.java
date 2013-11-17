package ui.swing.panels;

import java.awt.BorderLayout;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import models.queries.QueryResultsTableModel;

@SuppressWarnings("serial")
public class TablePanel extends JPanel{
	QueryResultsTableModel tableModel;
	JTable table;
	
	public TablePanel(ResultSet results, String label) {		
		setLayout(new BorderLayout());
		add( new JLabel(label), BorderLayout.NORTH);
		
		tableModel = new QueryResultsTableModel(results);
		table = new JTable(tableModel);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}
}
