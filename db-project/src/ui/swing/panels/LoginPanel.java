package ui.swing.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import db.DBConnection;

import java.awt.GridLayout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.Box;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {
	private Connection connection;
	
	private JTextField tfServer;
	private JTextField tfName;
	private JPasswordField passwordField;
	
	private JButton btnConnect;
	private JButton btnReset;

	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(3,2));
		
		JLabel lblDatabaseServer = new JLabel("Database Server:");

		tfServer = new JTextField("dbsvcs.cs.uno.edu");
		tfServer.setColumns(20);
		
		formPanel.add(lblDatabaseServer);
		formPanel.add(tfServer);
				
		JLabel lblUserName = new JLabel("User Name:");
		formPanel.add(lblUserName);
		
		tfName = new JTextField("bwalters");
		tfName.setColumns(10);
		formPanel.add(tfName);
		
		JLabel lblPassword = new JLabel("Password:");
		formPanel.add(lblPassword);
		
		passwordField = new JPasswordField("HtoaYUen");
		formPanel.add(passwordField);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(0,0));
		topPanel.add(formPanel, BorderLayout.CENTER);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		topPanel.add(horizontalStrut, BorderLayout.EAST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		topPanel.add(horizontalStrut_1, BorderLayout.WEST);

		add(topPanel, BorderLayout.NORTH);
		
		
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createConnection();
				btnConnect.setEnabled(false);
				btnReset.setEnabled(true);
			}
		});
		
		
		btnReset = new JButton("Reset");
		btnReset.setEnabled(false);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeConnection();
				btnReset.setEnabled(false);
			}
		});
		
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.add(btnConnect);
		panel.add(btnReset);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
	}
	
	private void createConnection() {
		String server = this.tfServer.getText();
		String username = this.tfName.getText();
		String password = new String(this.passwordField.getPassword());
		
		try {
			this.connection = DBConnection.getConnection(server, username, password);
		} catch (SQLException e) {
			
		}
	}
	
	private void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			
		} finally {
			this.connection = null;
		}
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public String getUserName() {
		return this.tfName.getText();
	}
}
