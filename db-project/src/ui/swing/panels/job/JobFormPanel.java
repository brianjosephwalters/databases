package ui.swing.panels.job;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Job;
import models.queries.JobQueries;

@SuppressWarnings("serial")
public class JobFormPanel extends JPanel {
	private Connection connection;
	private ButtonController controller;
	
	private JobQueries jobQueries;
	private Job job;
	
	JTextField tfJobCode;
	JTextField tfJobProfile;
	JTextField tfCompanyName;
	JTextField tfJobType;
	JTextField tfPayRate;
	JTextField tfOpeningDate;
	JTextField tfClosingDate;
	
	JButton editButton;
	private JPanel panel;
	private Component horizontalStrut;
	private Component horizontalStrut_1;
	private JLabel label;
	
	public JobFormPanel(Connection connection) {
		this.connection = connection;
		this.jobQueries = new JobQueries(connection);
		this.controller = new ButtonController();
		
		initializeGUIComponents();
	}
	
	private void initializeGUIComponents() {
		initializeDisplayPanel();
		initializeButtonPanel();
	}
	
	private void initializeDisplayPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		panel = new JPanel();
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		horizontalStrut = Box.createHorizontalStrut(5);
		panel.add(horizontalStrut);
		tfJobCode = new JTextField(10);
		tfJobCode.setEditable(false);
		tfJobProfile = new JTextField(20);
		tfJobProfile.setEditable(false);
		tfCompanyName = new JTextField(20);
		tfCompanyName.setEditable(false);
		tfJobType = new JTextField(15);
		tfJobType.setEditable(false);
		tfPayRate = new JTextField(10);
		tfPayRate.setEditable(false);
		tfOpeningDate = new JTextField(15);
		tfOpeningDate.setEditable(false);
		tfClosingDate = new JTextField(15);
		tfClosingDate.setEditable(false);
		
		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new GridLayout(8, 2));
		displayPanel.add(new JLabel("Job Code:"));
		displayPanel.add(tfJobCode);
		displayPanel.add(new JLabel("Job Profile:"));
		displayPanel.add(tfJobProfile);
		displayPanel.add(new JLabel("Company Name:"));
		displayPanel.add(tfCompanyName);
		displayPanel.add(new JLabel("Job Type:"));
		displayPanel.add(tfJobType);
		displayPanel.add(new JLabel("Pay Rate:"));
		displayPanel.add(tfPayRate);
		displayPanel.add(new JLabel("Opening Date:"));
		displayPanel.add(tfOpeningDate);
		displayPanel.add(new JLabel("Closing Date:"));
		displayPanel.add(tfClosingDate);
		
		panel.add(displayPanel);
		
		label = new JLabel("");
		displayPanel.add(label);
		horizontalStrut_1 = Box.createHorizontalStrut(5);
		panel.add(horizontalStrut_1);
	}
	
	private void initializeButtonPanel() {
		editButton = new JButton("Edit");
		editButton.addActionListener(controller);
		
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		buttonPanel.add(editButton);
		add(buttonPanel);
	}
	
	private void displayJob(Job job) {
		this.job = job;
		List<Job> list = null;
		try {
			list  = jobQueries.getJob(job.getJobCode());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (list != null) {
			Job jobR = list.get(0);
			Date opening = jobR.getOpeningDate();
			Date closing = jobR.getClosingDate();
			tfJobCode.setText(jobR.getJobCode());
			tfJobProfile.setText(jobR.getJobProfileTitle());
			tfCompanyName.setText(jobR.getCompanyName());
			tfJobType.setText(jobR.getJobType());
			tfPayRate.setText(jobR.getPayRate() + "");
			if (opening != null) {
				tfOpeningDate.setText(opening.toString());
			} else {
				tfOpeningDate.setText("");
			}
			if (closing != null) {
				tfClosingDate.setText(closing.toString());
			} else {
				tfClosingDate.setText("");
			}
			
		}
	}
	
	public class ButtonController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			editButton();
		}
		
		public void editButton() {
			
		}
	}
	
	public class JobListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("currentPerson")) {
				displayJob((Job)evt.getNewValue());
			}
		}
	}
}
