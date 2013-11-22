package ui.swing.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import models.Person;

import db.DBConnection;

@SuppressWarnings("serial")
public class PersonMainPanel extends JPanel {
	Connection connection;
	
	NavigationPanel navPanel;
	PersonFormPanel personFormPanel;
	PersonSkillPanel personSkillPanel;
	
	public PersonMainPanel(Connection connection) {
		this.connection = connection;
		
		// Setup Navigation Panel
		navPanel = new NavigationPanel();
		
		// Setup Info Panel
		personFormPanel = new PersonFormPanel(connection);
		personSkillPanel = new PersonSkillPanel(connection);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.add(personFormPanel);
		infoPanel.add(personSkillPanel);
		
		// Setup Main Panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(10,10));
		mainPanel.add(navPanel, BorderLayout.NORTH);
		mainPanel.add(infoPanel, BorderLayout.CENTER);

		setLayout(new FlowLayout(10,10,10));
		add(mainPanel);
	}
	
	public void setPerson(Person person) {
		
	}
	
	
	private class NavigationController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			
		}
	}
	
	public static void main (String[] args) {
		Connection connection = null;
		try {
			connection = DBConnection.getConnection();
		} catch (SQLException e) {}
		JFrame frame = new JFrame();
		frame.add(new PersonMainPanel(connection));
		frame.setBounds(100, 100, 550, 400);
		frame.setTitle("Person Program");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
