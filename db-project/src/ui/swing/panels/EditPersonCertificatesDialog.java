package ui.swing.panels;

import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import models.Certificate;
import models.Person;
import models.queries.CertificateQueries;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.awt.Component;

import javax.swing.Box;

@SuppressWarnings("serial")
public class EditPersonCertificatesDialog extends JDialog {
	Connection connection;
	Person person;
	
	CertificateQueries certificateQueries;
	
	JList<Certificate> currentList;
	DefaultListModel<Certificate> currentListModel;
	JList<Certificate> availableList;
	DefaultListModel<Certificate> availableListModel;
	
	ButtonController buttonController;
	
	JButton btnClearCurrent;
	JButton btnAdd;
	JButton btnRemove;
	JButton btnClearAvailable;

	JButton btnNewSkill;
	
	/**
	 * Create the panel.
	 */
	public EditPersonCertificatesDialog(Connection connection, Person person) {
		this.connection = connection;
		this.person = person;
		certificateQueries = new CertificateQueries(connection);
		
		buttonController = new ButtonController();
		
		this.setBounds(100, 100, 550, 400);
		this.setTitle("Change Skills");
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);

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
		
		JLabel lblCurrentSkills = new JLabel("Current Certificates");
		lblCurrentSkills.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(lblCurrentSkills, "2, 2, center, default");
		
		JLabel lblAvailableSkills = new JLabel("Available Certificates");
		lblAvailableSkills.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(lblAvailableSkills, "6, 2, center, default");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "2, 4, fill, fill");
		
		currentList = new JList<Certificate>();
		currentListModel = new DefaultListModel<Certificate>();
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
		
		availableList = new JList<Certificate>();
		availableListModel = new DefaultListModel<Certificate>();
		availableList.setModel(availableListModel);
		scrollPane_1.setViewportView(availableList);
		
		
		btnNewSkill = new JButton("New Skill");
		btnNewSkill.setEnabled(false);
		btnNewSkill.addActionListener(buttonController);
		add(btnNewSkill, "6, 6");
	}
	
	private void updateModels() {
		updateCurrentSkillsListModel();
		updateAvailableSkillsListModel();
	}
	
	private void updateCurrentSkillsListModel() {
		List<Certificate> list = null;
		try {
			list = certificateQueries.getCertificatesForPerson(person.getPersonCode());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		currentListModel.clear();
		for (Certificate certificate : list) {
			currentListModel.addElement(certificate);
		}
	}
	
	private void updateAvailableSkillsListModel() {
		List<Certificate> list = null;
		try {
			list = certificateQueries.getCertificatesNotPossessedByPerson(person.getPersonCode());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		availableListModel.clear();
		for (Certificate certificate : list) {
			availableListModel.addElement(certificate);
		} 
	}
	
	private class ButtonController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnClearAvailable) {
				clearSelectedAvailableCertificates();
			} else if (e.getSource() == btnClearCurrent) {
				clearSelectedCurrentCertificates();
			} else if (e.getSource() == btnAdd) {
				addCertificates();
			} else if (e.getSource() == btnRemove) {
				removeCertificates();
			} else if (e.getSource() == btnNewSkill) {
				
			}
		}
		
		public void clearSelectedAvailableCertificates() {
			availableList.clearSelection();
		}
		
		public void clearSelectedCurrentCertificates() {
			currentList.clearSelection();
		}
		
		public void addCertificates() {
			List<Certificate> addList = availableList.getSelectedValuesList();
			try {
				certificateQueries.addCertificatesToPerson(person, addList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			updateModels();
		}
		
		public void removeCertificates() {
			List<Certificate> removeList = currentList.getSelectedValuesList();
			try {
				certificateQueries.removeCertificatesFromPerson(person, removeList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			updateModels();
		}
	}
}
