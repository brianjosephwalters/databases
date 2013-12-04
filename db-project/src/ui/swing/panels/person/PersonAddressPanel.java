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

import models.Address;
import models.Person;
import models.queries.AddressQueries;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class PersonAddressPanel extends JPanel {
	Connection connection;
	Person person;
	AddressQueries addressQueries;
	List<Address> list;
	
	
	private ComboBoxController cbController;
	
	private JComboBox<Address> cbType;
	private DefaultComboBoxModel<Address> cbTypeModel;
	
	private JTextField tfStreet1;
	private JTextField tfStreet2;
	private JTextField tfCity;
	private JTextField tfZipcode;
	
	JButton btnEdit;
	
	public PersonAddressPanel(Connection connection) {
		this.connection = connection;
		this.addressQueries = new AddressQueries(connection);
		this.list = new ArrayList<Address>();
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
				ColumnSpec.decode("max(37dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(15dlu;default)"),
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblType = new JLabel("Type:");
		add(lblType, "4, 4");
		
		cbType = new JComboBox<Address>();
		cbType.addActionListener(cbController);
		cbTypeModel = new DefaultComboBoxModel<Address>();
		add(cbType, "6, 4, 9, 1, fill, default");
		
		JLabel lblStreet = new JLabel("Street 1:");
		add(lblStreet, "6, 6, right, default");
		
		tfStreet1 = new JTextField();
		tfStreet1.setEditable(false);
		add(tfStreet1, "8, 6, 7, 1, fill, default");
		tfStreet1.setColumns(10);
		
		JLabel lblStreet_1 = new JLabel("Street 2:");
		add(lblStreet_1, "6, 8, right, default");
		
		tfStreet2 = new JTextField();
		tfStreet2.setEditable(false);
		add(tfStreet2, "8, 8, 7, 1, fill, default");
		tfStreet2.setColumns(10);
		
		JLabel lblCity = new JLabel("City:");
		add(lblCity, "6, 10, right, default");
		
		tfCity = new JTextField();
		tfCity.setEditable(false);
		add(tfCity, "8, 10, 7, 1, fill, top");
		tfCity.setColumns(10);
		
		JLabel lblZipcode = new JLabel("Zipcode:");
		add(lblZipcode, "6, 12, right, default");
		
		tfZipcode = new JTextField();
		tfZipcode.setEditable(false);
		add(tfZipcode, "8, 12, 3, 1, fill, top");
		tfZipcode.setColumns(10);
		
		btnEdit = new JButton("Edit");
		add(btnEdit, "14, 12");
	}
	
	private void clearAddress() {
		tfStreet1.setText("");
		tfStreet2.setText("");
		tfCity.setText("");
		tfZipcode.setText("");
	}
	
	private void displayPersonAddress(Address address) {
		if (address != null) {
			tfStreet1.setText(address.getStreet1());
			tfStreet2.setText(address.getStreet2());
			tfCity.setText(address.getCity());
			tfZipcode.setText(address.getZipcode());
		} else {
			clearAddress();
		}
	}
	
	private void generateAddressList() {
		String personCode = person.getPersonCode();
		try {
			this.list = addressQueries.getPersonAddresses(personCode);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void displayPersonTypes() {
		generateAddressList();
		cbTypeModel = new DefaultComboBoxModel<Address>();
		cbTypeModel.removeAllElements(); // Redundant?
		for (Address address: list) {
			cbTypeModel.addElement(address);
		}
		
		cbType.setModel(cbTypeModel);
	}
	
	private void displayPerson(Person person) {
		this.person = person;
		clearAddress();
		displayPersonTypes();
		Address address = (Address)cbType.getSelectedItem();
		displayPersonAddress(address);
	}
	
	private class ComboBoxController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cbType) {
				Address address = (Address)cbType.getSelectedItem(); 
				displayPersonAddress(address);
			}
		}
	}
	
	public class PersonListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("currentPerson")) {
				displayPerson((Person)evt.getNewValue());
			} else if (evt.getPropertyName().equals("currentList")) {
				
			}
		}
	}
}
