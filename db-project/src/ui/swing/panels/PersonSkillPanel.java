package ui.swing.panels;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Person;
import models.Skill;
import models.queries.SkillQueries;

@SuppressWarnings("serial")
public class PersonSkillPanel extends JPanel {
	private Connection connection;
	private Person person;
	private SkillQueries skillQueries;
	private List<Skill> list;
	
	private ButtonController buttonController;
	
	JTextField tfSkills;
	JButton editButton;

	public PersonSkillPanel(Connection connection) {
		this.connection = connection;
		this.skillQueries = new SkillQueries(connection);
		this.list = new ArrayList<Skill>();
		
		this.buttonController = new ButtonController();
		
		initializeGUIComponents();
	}
	
	public void initializeGUIComponents() {
		// Setup TextField
		tfSkills = new JTextField();
		
		//Setup Data Panel
		editButton = new JButton("Edit");
		editButton.addActionListener(buttonController);
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		buttonPanel.add(editButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
			
		// Setup the Main Panel.
		setBorder(BorderFactory.createTitledBorder( 
			BorderFactory.createEtchedBorder(), "Skills"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(tfSkills);
		add(buttonPanel);
	}

	public void displayPerson(Person person) {
		this.person = person;
		setSkillTextField();
	}

	private void setSkillTextField() { 
		String personCode = person.getPersonCode();
		System.out.println(personCode);
		try {
			this.list = skillQueries.getSkillsOfPerson(personCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		for (Skill skill: list) {
			sb.append(skill.getSkillName() + ", ");
		}
		tfSkills.setText(sb.toString());
		System.out.println(list);
	}
		
	private class ButtonController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
				editButton();
		}
		
		private void editButton() {
			JDialog dialog = new JDialog();
			dialog.add(new EditPersonSkillsPanel(connection, person));
			dialog.setModal(true);
			dialog.dispose();
		}
	}
	
	public class PersonListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			displayPerson((Person)evt.getNewValue());
		}
		
	}
}
