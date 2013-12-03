package ui.swing.panels.person;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Person;
import models.PhoneNumber;
import models.queries.PhoneQueries;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class PersonPhonePanel extends JPanel{
	private Connection connection;
	private PhoneQueries phoneQueries;
	private Person person;
	private List<PhoneNumber> list;
	
	private ComboBoxController cbController;
	
	private JComboBox<PhoneNumber> cbType;
	private DefaultComboBoxModel<PhoneNumber> cbTypeModel;
	
	private JTextField tfPhoneNumber;
	private JButton btnEdit;
	
	public PersonPhonePanel (Connection connection) {
		this.connection = connection;
		this.phoneQueries = new PhoneQueries(connection);
		this.list = new ArrayList<PhoneNumber>();
		this.person = null;
		
		this.cbController = new ComboBoxController();
		
		initializeGUIComponents();
	}
	
	private void initializeGUIComponents() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default"),
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblType = new JLabel("Type:");
		add(lblType, "4, 4, right, default");
		
		cbType = new JComboBox<PhoneNumber>();
		cbType.addActionListener(cbController);
		cbTypeModel = new DefaultComboBoxModel<PhoneNumber>();
		add(cbType, "6, 4, 5, 1, fill, default");
		
		JLabel lblPhoneNumber = new JLabel("Phone Number:");
		add(lblPhoneNumber, "6, 6, right, default");
		
		tfPhoneNumber = new JTextField();
		tfPhoneNumber.setEditable(false);
		add(tfPhoneNumber, "8, 6, 3, 1, fill, default");
		tfPhoneNumber.setColumns(10);
		
		btnEdit = new JButton("Edit");
		add(btnEdit, "10, 8");
	}
	
	private void clearPhone() {
		tfPhoneNumber.setText("");
	}
	
	private void displayPersonPhone(PhoneNumber phone) {
		tfPhoneNumber.setText(phone.getPhoneNum());
	}
	
	private void generatePhoneList() {
		String personCode = person.getPersonCode();
		try {
			list = phoneQueries.getPersonPhoneNumbers(personCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void displayPersonTypes() {
		generatePhoneList();
		cbTypeModel = new DefaultComboBoxModel<PhoneNumber>();
		cbTypeModel.removeAllElements();
		for (PhoneNumber phone: list) {
			cbTypeModel.addElement(phone);
		}
		
		cbType.setModel(cbTypeModel);
	}
	
	private void displayPerson(Person person) {
		this.person = person;
		clearPhone();
		displayPersonTypes();
		PhoneNumber phone = (PhoneNumber)cbType.getSelectedItem(); 
		displayPersonPhone(phone);
	}
	
	private class ComboBoxController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cbType) {
				PhoneNumber phone = (PhoneNumber)cbType.getSelectedItem(); 
				displayPersonPhone(phone);
			}
		}
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
