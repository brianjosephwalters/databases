package ui.swing.panels.person;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.swing.panels.DeletePersonDialog;
import ui.swing.panels.EditPersonDialog;
import ui.swing.panels.NewPersonDialog;

import models.Person;
import models.queries.PersonQueries;

import java.awt.Component;

@SuppressWarnings("serial")
public class PersonNavigationPanel2 extends JPanel {
	Connection connection;
	NavigationController controller;
	
	PersonQueries personQueries;
	
	List<Person> personList;
	Person currentPerson;
	int currentIndex;
	
	PropertyChangeSupport pcS;

	JButton btnDelete;
	JButton btnPrevious;
	JButton btnNext;
	JButton btnNew;
	JTextField tfIndex;
	JTextField tfMax;
	private JButton btnEdit;
	private Component horizontalStrut;

	/**
	 * Create the panel.
	 */
	public PersonNavigationPanel2(Connection connection) {
		this.connection = connection;
		this.personQueries = new PersonQueries(connection);
		
		pcS = new PropertyChangeSupport(this);
		
		this.personList = new ArrayList<Person>();
		this.currentPerson = null;
		this.currentIndex = 0;
		
		
		this.controller = new NavigationController();
		
		initializeGUIComponents();
		resetPersonList();
	}
	
	private void initializeGUIComponents() {
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(controller);
		
		btnPrevious = new JButton("Previous");
		btnPrevious.addActionListener(controller);
		
		tfIndex = new JTextField();
		tfIndex.setHorizontalAlignment(JTextField.CENTER);
		tfIndex.addActionListener(controller);
		
		tfMax = new JTextField();
		tfMax.setHorizontalAlignment(JTextField.CENTER);
		tfMax.setEditable(false);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(controller);
		
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(controller);
		
		btnNew = new JButton("New");
		btnNew.addActionListener(controller);
		
		setLayout(new BoxLayout( this, BoxLayout.X_AXIS));
		add(Box.createHorizontalGlue());
		add(btnDelete);
		add(Box.createHorizontalStrut(10));
		add(btnPrevious);
		add(Box.createHorizontalStrut(10));
		add(tfIndex);
		add(Box.createHorizontalStrut(10));
		add(new JLabel("of"));
		add(tfMax);
		add(Box.createHorizontalStrut(10));
		add(btnNext);
		add(Box.createHorizontalStrut(10));
		add(btnNew);
		
		horizontalStrut = Box.createHorizontalStrut(20);
		add(horizontalStrut);
		
		
		add(btnEdit);
		add(Box.createHorizontalGlue());
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcS.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcS.addPropertyChangeListener(listener);
	}
	
	public List<Person> getPersonList() {
		return this.personList;
	}
	
	public void setPersonList(List<Person> newList) {
		List<Person> oldList = this.personList;
		this.personList = newList;
		pcS.firePropertyChange("currentList", oldList, newList);
	}
	
	public Person getCurrentPerson() {
		return this.currentPerson;
	}
	
	public void setCurrentPerson(Person newPerson) {
		Person oldPerson = this.currentPerson;
		this.currentPerson = newPerson;
		pcS.firePropertyChange("currentPerson", oldPerson, newPerson);
	}
	
	private List<Person> generatePersonList() {
		List<Person> list = new ArrayList<Person>();
		try {
			list = personQueries.getAllPeople();
		} catch (SQLException e) {
			System.out.println("Unable to get people!");
		}
		return list;
	}
	
	private void updatePersonList(int index) {
		setPersonList(generatePersonList());
		setCurrentIndex(index);
		setCurrentPerson(personList.get(currentIndex));
	}
	
	public void resetPersonList() {
		setPersonList(generatePersonList());
		setCurrentIndex(0);
		setCurrentPerson(personList.get(currentIndex));
	}
	
	private void setCurrentIndex(int index) {
		this.currentIndex = index;
		tfIndex.setText("" + (index + 1));
		tfMax.setText("" + personList.size());
	}
	
	private int getCurrentIndex() {
		return this.currentIndex;
	}
	
	private class NavigationController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnDelete) {
				deleteButton();
			} else if (e.getSource() == btnPrevious) {
				prevButton();
			} else if (e.getSource() == btnNext) {
				nextButton();
			} else if (e.getSource() == btnNew) {
				newButton();
			} else if (e.getSource() == btnEdit) {
				editButton();
			}
			else if (e.getSource() == tfIndex) {
				indexUpdate();
			} 
		}
		
		private void deleteButton() {
			Person person = getCurrentPerson();
			JDialog dialog = new DeletePersonDialog(connection, person);
			dialog.addWindowListener(new DeleteWindowListener());
		}
		
		private void prevButton() {
			if (getCurrentIndex() - 1 < 0) {
				setCurrentIndex(personList.size() - 1);
			} else {
				setCurrentIndex(getCurrentIndex() - 1);
			}
			setCurrentPerson(personList.get(getCurrentIndex()));
		}
		
		private void nextButton() {
			if (getCurrentIndex() + 1 == personList.size()) {
				setCurrentIndex(0);
			} else {
				setCurrentIndex(getCurrentIndex() + 1);
			}
			setCurrentPerson(personList.get(getCurrentIndex()));
		}
		
		private void newButton () {
			JDialog dialog = new NewPersonDialog(connection);
			dialog.addWindowListener(new NewDialogListener());
		}
		
		private void editButton() {
			Person person = getCurrentPerson();
			JDialog dialog = new EditPersonDialog(connection, person);
			dialog.addWindowListener(new EditWindowListener());
		}
		
		private void indexUpdate() {
			int index = Integer.parseInt(tfIndex.getText()) - 1;
			if (index >= 0 && index < personList.size()) {
				setCurrentIndex(index);
			}
			setCurrentPerson(personList.get(getCurrentIndex()));
		}
	}
	
	public class EditWindowListener extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			updatePersonList(currentIndex);
		}
	}
	
	public class NewDialogListener extends WindowAdapter {
		public void windowClosed(WindowEvent e) {
			updatePersonList(currentIndex);
		}
	}
	
	public class DeleteWindowListener extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			updatePersonList(currentIndex);
		}
	}

}
