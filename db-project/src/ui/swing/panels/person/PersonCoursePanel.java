package ui.swing.panels.person;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;

import models.Person;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import ui.swing.panels.QueryResultsPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

@SuppressWarnings("serial")
public class PersonCoursePanel extends JPanel {
	Connection connection;
	Person person;
	
	QueryResultsPanel queryResultsPanel;

	public PersonCoursePanel(Connection connection) {
		this.connection = connection;
		this.person = null;
		
		this.queryResultsPanel = new QueryResultsPanel("Nothing Yet");
		initializeGUIComponents();
	}
	
	private void initializeGUIComponents() {
		setLayout(new BorderLayout(0, 0));
		add(queryResultsPanel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		add(panel, BorderLayout.SOUTH);
		
		JButton btnEdit = new JButton("Edit");
		panel.add(btnEdit);
	}
	
	private void generateCourseList() {
		ResultSet results = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" SELECT course_title, completed_date, score " +
				" FROM person NATURAL JOIN attended NATURAL JOIN " +
				"      section NATURAL JOIN course " +
				" WHERE person_code = ?",
				ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE
				);
			stmt.setString(1, person.getPersonCode());
			results = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		queryResultsPanel.setLabel("Courses");
		queryResultsPanel.setResultsSet(results);

	}
	
	private void displayPerson(Person person) {
		this.person = person;
		generateCourseList();
		
	}
	
	public class PersonListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("currentPerson")) {
				displayPerson((Person)evt.getNewValue());
			}
		}
	}
}
