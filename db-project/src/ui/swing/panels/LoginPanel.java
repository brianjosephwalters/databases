package ui.swing.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;

public class LoginPanel extends JPanel {
	private JTextField tfDatabase;
	private JTextField tfName;
	private JPasswordField pwdPassword;
	private JTextField tfServer;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(3,2));
		JLabel lblDatabaseServer = new JLabel("Database Server:");

		tfServer = new JTextField();
		tfServer.setColumns(20);
		
		formPanel.add(lblDatabaseServer);
		formPanel.add(tfServer);
				
		JLabel lblUserName = new JLabel("User Name:");
		formPanel.add(lblUserName);
		
		textField = new JTextField();
		formPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		formPanel.add(lblPassword);
		
		passwordField = new JPasswordField();
		formPanel.add(passwordField);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(0,0));
		topPanel.add(formPanel, BorderLayout.CENTER);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		topPanel.add(horizontalStrut, BorderLayout.EAST);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		topPanel.add(horizontalStrut_1, BorderLayout.WEST);

		add(topPanel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		
		JButton btnConnect = new JButton("Connect\r\n");
		panel.add(btnConnect);
		
		JButton btnReset = new JButton("Reset");
		panel.add(btnReset);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		
	}

}
