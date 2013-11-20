package ui.swing.test;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ui.swing.panels.LoginPanel;

import java.awt.Component;
import java.sql.Connection;

import javax.swing.Box;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ConnectionDialog extends JDialog {
	LoginPanel loginPanel;
	Connection connection;
	/**
	 * Create the dialog.
	 */
	public ConnectionDialog() {
		this.connection = null;
		
		getContentPane().setLayout(new BorderLayout());
		
		Component verticalStrut = Box.createVerticalStrut(20);
		getContentPane().add(verticalStrut, BorderLayout.NORTH);
		
		loginPanel = new LoginPanel();
		getContentPane().add(loginPanel, BorderLayout.CENTER);
		
		JPanel closePanel = new JPanel();
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setConnection(loginPanel.getConnection());
				dispose();
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetConnection();
				dispose();
			}
		});
		
		closePanel.add(okButton);
		closePanel.add(cancelButton);
		getContentPane().add(closePanel, BorderLayout.SOUTH);

		setBounds(100, 100, 450, 300);
		setVisible(true);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	private void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	private void resetConnection() {
		this.connection = null;
	}
	
	public Connection getConnection() {
		return this.connection;
	}
	
	public String getUserName() {
		return this.loginPanel.getUserName();
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			new ConnectionDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
