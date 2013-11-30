package ui.swing.panels;

import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import db.DBConnection;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import models.Person;
import models.Skill;
import models.queries.PersonQueries;
import models.queries.SkillQueries;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.awt.Component;

import javax.swing.Box;

@SuppressWarnings("serial")
public class EditPersonSkillsPanel extends JPanel {
	Connection connection;
	Person person;
	
	SkillQueries skillQueries;
	
	JList<Skill> currentList;
	DefaultListModel<Skill> currentListModel;
	JList<Skill> availableList;
	DefaultListModel<Skill> availableListModel;
	
	ButtonController buttonController;
	
	JButton btnClearCurrent;
	JButton btnAdd;
	JButton btnRemove;
	JButton btnClearAvailable;

	JButton btnNewSkill;
	
	/**
	 * Create the panel.
	 */
	public EditPersonSkillsPanel(Connection connection, Person person) {
		this.connection = connection;
		this.person = person;
		skillQueries = new SkillQueries(connection);
		
		buttonController = new ButtonController();

		initializeGUIComponents();
		updateModels();
	}
	
	private void initializeGUIComponents() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblCurrentSkills = new JLabel("Current Skills");
		lblCurrentSkills.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(lblCurrentSkills, "2, 2, center, default");
		
		JLabel lblAvailableSkills = new JLabel("Available Skills");
		lblAvailableSkills.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(lblAvailableSkills, "6, 2, center, default");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "2, 4, fill, fill");
		
		currentList = new JList<Skill>();
		currentListModel = new DefaultListModel<Skill>();
		currentList.setModel(currentListModel);
		scrollPane.setViewportView(currentList);
		
		JPanel panel = new JPanel();
		add(panel, "4, 4, center, center");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		btnClearCurrent = new JButton("Clear Current");
		btnClearCurrent.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnClearCurrent.addActionListener(buttonController);
		panel.add(btnClearCurrent);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		panel.add(verticalStrut_1);
		
		btnAdd = new JButton("Add");
		btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnAdd.addActionListener(buttonController);
		panel.add(btnAdd);
		
		btnRemove = new JButton("Remove");
		btnRemove.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnRemove.addActionListener(buttonController);
		panel.add(btnRemove);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		panel.add(verticalStrut);
		
		btnClearAvailable = new JButton("Clear Available");
		btnClearAvailable.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnClearAvailable.addActionListener(buttonController);
		panel.add(btnClearAvailable);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1, "6, 4, fill, fill");
		
		availableList = new JList<Skill>();
		availableListModel = new DefaultListModel<Skill>();
		availableList.setModel(availableListModel);
		scrollPane_1.setViewportView(availableList);
		
		
		btnNewSkill = new JButton("New Skill");
		btnNewSkill.addActionListener(buttonController);
		add(btnNewSkill, "6, 6");
	}
	
	private void updateModels() {
		updateCurrentSkillsListModel();
		updateAvailableSkillsListModel();
	}
	
	private void updateCurrentSkillsListModel() {
		List<Skill> list = null;
		try {
			list = skillQueries.getSkillsOfPerson(person.getPersonCode());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		currentListModel.clear();
		for (Skill skill : list) {
			currentListModel.addElement(skill);
		}
	}
	
	private void updateAvailableSkillsListModel() {
		List<Skill> list = null;
		try {
			list = skillQueries.getSkillsNotPossessedByPerson(person.getPersonCode());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		availableListModel.clear();
		for (Skill skill : list) {
			availableListModel.addElement(skill);
		} 
	}
	
	private class ButtonController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnClearAvailable) {
				clearSelectedAvailableSkills();
			} else if (e.getSource() == btnClearCurrent) {
				clearSelectedCurrentSkills();
			} else if (e.getSource() == btnAdd) {
				addSkills();
			} else if (e.getSource() == btnRemove) {
				removeSkills();
			} else if (e.getSource() == btnNewSkill) {
				
			}
		}
		
		public void clearSelectedAvailableSkills() {
			availableList.clearSelection();
		}
		
		public void clearSelectedCurrentSkills() {
			currentList.clearSelection();
		}
		
		public void addSkills() {
			List<Skill> addList = availableList.getSelectedValuesList();
			try {
				skillQueries.addSkillsToPerson(person, addList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			updateModels();
		}
		
		public void removeSkills() {
			List<Skill> removeList = currentList.getSelectedValuesList();
			try {
				skillQueries.removeSkillsFromPerson(person, removeList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			updateModels();
		}
	}

	public static void main(String[] args) {
		Connection connection = null;
		PersonQueries queries = null;
		List<Person> list = null;
		try {
			connection = DBConnection.getConnection();
			queries = new PersonQueries(connection);
			list = queries.getAllPeople();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		EditPersonSkillsPanel panel = 
				new EditPersonSkillsPanel(connection, list.get(0));
		
		JFrame frame = new JFrame();
		frame.add(panel);
		frame.setBounds(100, 100, 550, 400);
		frame.setTitle("Test Change Skills of Person Program");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
