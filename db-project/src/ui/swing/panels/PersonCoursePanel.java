package ui.swing.panels;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import ui.swing.test.QueryResultsPanel;
import models.Exam;
import models.ExamTaken;
import models.Person;
import models.Course;
import models.queries.QueryResultsTableModel;

@SuppressWarnings("serial")
public class PersonCoursePanel extends JPanel {
	Connection connection;
	Person person;
	
	QueryResultsTableModel tableModel;
	JTable table;

	public PersonCoursePanel(Connection connection) {
		this.connection = connection;
		this.person = null;
	}
	
	private void initializeGUIComponents() {
	}
	
	private void generateCourseList() {
		ResultSet results = null;
		try {
			PreparedStatement stmt = connection.prepareStatement(
				" SELECT course_title, completion_date, score " +
				" FROM person NATURAL JOIN attended NATURAL JOIN " +
				"      section_course NATURAL JOIN course " +
				" WHERE person_code = ?"
				);
			stmt.setString(1, person.getPersonCode());
			results = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		add(new QueryResultsPanel(results, "Courses"));

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
