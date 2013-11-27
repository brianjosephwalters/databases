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
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import models.Certificate;
import models.Person;
import models.queries.CertificateQueries;

@SuppressWarnings("serial")
public class PersonCertificatePanel extends JPanel {
	private Connection connection;
	private Person person;
	private CertificateQueries certificateQueries;
	private List<Certificate> list;
	
	private ButtonController buttonController;
	
	private JTextArea taCertificates;
	private JScrollPane scrollPane;
	
	private JButton editButton;
	
	public PersonCertificatePanel(Connection connection) {
		this.connection = connection;
		this.certificateQueries = new CertificateQueries(connection);
		this.list = new ArrayList<Certificate>();
		
		this.buttonController = new ButtonController();
		
		initializeGUIComponents();
	}
	
	public void initializeGUIComponents() {
		
		//Setup Data Panel
		editButton = new JButton("Edit");
		editButton.addActionListener(buttonController);
		editButton.setEnabled(false);
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		buttonPanel.add(editButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
			
		// Setup the Main Panel.
		setBorder(BorderFactory.createTitledBorder( 
			BorderFactory.createEtchedBorder(), "Certificates"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Setup TextField
		scrollPane = new JScrollPane();
		taCertificates = new JTextArea();
		taCertificates.setLineWrap(true);
		taCertificates.setWrapStyleWord(true);
		taCertificates.setEditable(false);
		scrollPane.setViewportView(taCertificates);
		
		add(scrollPane);
		add(buttonPanel);
	}
	
	public void displayPerson(Person person) {
		this.person = person;
		setCertificateTextArea();
	}
	
	private void setCertificateTextArea()  {
		String personCode = person.getPersonCode();
		try {
			this.list = certificateQueries.getCertificatesForPerson(personCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		for (Certificate certificate: list) {
			sb.append(certificate.getCertificateTitle() + ", ");
		}
		taCertificates.setText(sb.toString());
	}
	
	private class ButtonController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
				editButton();
		}
		
		private void editButton() {
//			JDialog dialog = new JDialog();
//			dialog.getContentPane().add(new EditPersonSkillsPanel(connection, person));
//			dialog.setBounds(100, 100, 550, 400);
//			dialog.setTitle("Change Skills");
//			dialog.setVisible(true);
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.addWindowListener(new EditWindowListener());
//			dialog.setModal(true);
		}
	}
	
	public class PersonListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			displayPerson((Person)evt.getNewValue());
		}
	}
	
	public class EditWindowListener extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			setCertificateTextArea();
		}
	}
	
}
