package ui.swing.panels;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JPanel;
import javax.swing.JTextArea;

import models.Job;
import models.Skill;
import models.queries.SkillQueries;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class JobSkillPanel extends JPanel {
	private Connection connection;
	private Job job;
	private SkillQueries skillQueries;
	private List<Skill> list;
	
	private ButtonController buttonController;
	
	private JTextArea taSkills;
	private JScrollPane scrollPane;
	
	private JButton editButton;

	public JobSkillPanel(Connection connection) {
		this.connection = connection;
		this.skillQueries = new SkillQueries(connection);
		this.list = new ArrayList<Skill>();
		
		this.buttonController = new ButtonController();
		
		initializeGUIComponents();
	}
	
	public void initializeGUIComponents() {
		
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
			BorderFactory.createEtchedBorder(), "Skills Required"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Setup TextField
		scrollPane = new JScrollPane();
		taSkills = new JTextArea();
		taSkills.setLineWrap(true);
		taSkills.setWrapStyleWord(true);
		taSkills.setEditable(false);
		scrollPane.setViewportView(taSkills);
		
		add(scrollPane);
		add(buttonPanel);
	}

	public void displayJob(Job job) {
		this.job = job;
		setSkillTextArea();
	}
	
	private void generateSkillList() {
		String jobCode = job.getJobCode();
		String jobProfileCode = job.getJobProfileCode();
		try {
			this.list = skillQueries.getSkillsForJobAndJobProfile(jobCode, jobProfileCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void setSkillTextArea() { 
		generateSkillList();		
		StringBuilder sb = new StringBuilder();
		for (Skill skill: list) {
			sb.append(skill.getSkillName() + ", ");
		}
		taSkills.setText(sb.toString());
	}
		
	private class ButtonController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			editButton();
		}
		
		private void editButton() {
			
		}
	}
	
	public class JobListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			displayJob((Job)evt.getNewValue());
		}
	}
	
	public class EditWindowListener extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			setSkillTextArea();
		}
	}
}
