package ui.swing.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PersonTable extends JPanel {
	
	Connection connection;
	
	/**
	 * Create the panel.
	 */
	public PersonTable(Connection connection) {
		this.connection = connection;
	}

	public static void main(String[] args) {
		String URL = "jdbc:oracle:thin@dbsvcs.cs.uno.edu:1521:orcl";
		String username = "bwalters";
		String password = "HtoaYUen";
		
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(URL, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JFrame frame = new JFrame();
		frame.add(new PersonTable(connection));
		
		frame.setTitle("PersonTable");
		frame.setSize(600, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
