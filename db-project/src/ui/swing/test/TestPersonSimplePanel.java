package ui.swing.test;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import models.Person;
import models.queries.PersonQueries;

import db.DBConnection;

@SuppressWarnings("serial")
public class TestPersonSimplePanel extends JPanel {
	private Connection connection;
	private PersonQueries personQueries;
	private List<Person> results;
	private int entryIndex;
	private boolean updatePerson;
	private boolean insertPerson;
	private boolean initializing;
	
	private NavigationController navController;
	private DataController dataController;
	private TextFieldController textController;
	private ComboBoxController cbController;
	private JTextField tfPersonCode;
	private JTextField tfLastName;
	private JTextField tfFirstName;
	private JTextField tfGender;
	private JTextField tfEmail;
	
	private JButton prevButton;
	private JTextField tfIndex;
	private JTextField tfMax;
	private JButton nextButton;
	
	private JButton newButton;
	private JButton resetButton;
	
	private DefaultComboBoxModel<Person> cbPersonModel;
	private JComboBox<Person> cbPerson;

	/**
	 * Create the panel.
	 */
	public TestPersonSimplePanel(Connection connection) {
		this.connection = connection;
		this.personQueries = new PersonQueries(connection);
		this.entryIndex = 0;
		this.updatePerson = false;
		this.initializing = true;

		this.navController = new NavigationController();
		this.dataController = new DataController();
		this.textController = new TextFieldController();
		this.cbController =  new ComboBoxController();

		// Setup Display Panel and its elements.
		tfPersonCode = new JTextField(10);
		tfPersonCode.setEnabled(false);
		tfPersonCode.getDocument().addDocumentListener(textController);
		tfLastName = new JTextField(15);
		tfLastName.getDocument().addDocumentListener(textController);
		tfFirstName = new JTextField(15);
		tfFirstName.getDocument().addDocumentListener(textController);
		tfGender = new JTextField(5);
		tfGender.getDocument().addDocumentListener(textController);
		tfEmail = new JTextField(15);
		tfEmail.getDocument().addDocumentListener(textController);		

		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new GridLayout(5, 2));
		displayPanel.add(new JLabel("Person Code:"));
		displayPanel.add(tfPersonCode);
		displayPanel.add(new JLabel("Last Name:"));
		displayPanel.add(tfLastName);
		displayPanel.add(new JLabel("First Name:"));
		displayPanel.add(tfFirstName);
		displayPanel.add(new JLabel("Gender:"));
		displayPanel.add(tfGender);
		displayPanel.add(new JLabel("Email:"));
		displayPanel.add(tfEmail);

		// Setup Navigation Panel and its elements.
		prevButton = new JButton("Previous");
		prevButton.setEnabled(false);
		prevButton.addActionListener(navController);
		tfIndex = new JTextField(4);
		tfIndex.setHorizontalAlignment(JTextField.CENTER);
		tfIndex.addActionListener(navController);
		tfMax = new JTextField(4);
		tfMax.setHorizontalAlignment(JTextField.CENTER);
		tfMax.setEditable(false);
		nextButton = new JButton("Next");
		nextButton.setEnabled(false);
		nextButton.addActionListener(navController);
		
		JPanel navigatePanel = new JPanel();
		navigatePanel.setLayout(new BoxLayout(navigatePanel, BoxLayout.X_AXIS));

		navigatePanel.add(prevButton);
		navigatePanel.add(Box.createHorizontalStrut(10));
		navigatePanel.add(tfIndex);
		navigatePanel.add(Box.createHorizontalStrut(10));
		navigatePanel.add(new JLabel("of"));
		navigatePanel.add(Box.createHorizontalStrut(10));
		navigatePanel.add(tfMax);
		navigatePanel.add(Box.createHorizontalStrut(10));
		navigatePanel.add(nextButton);
		
		// Setup Data Panel and its elements.
		resetButton = new JButton("Reset Person");
		resetButton.setEnabled(false);
		resetButton.addActionListener(dataController);
		newButton = new JButton("New...");

		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.X_AXIS));
		dataPanel.add(resetButton);
		dataPanel.add(Box.createHorizontalStrut(10));
		dataPanel.add(newButton);
		
		// Setup ComboBox Panel and its Elements.
		cbPersonModel = new DefaultComboBoxModel<Person>();
		cbPerson = new JComboBox<Person>(cbPersonModel);
		cbPerson.addActionListener(cbController);
		
		JPanel selectorPanel = new JPanel();
		selectorPanel.add(cbPerson);

		// Setup Main Panel and its elements.
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		add(displayPanel);
		add(navigatePanel);
		add(dataPanel);
		add(selectorPanel);

		setUp();
	}
	
	public void clearForm() {
		tfPersonCode.setText("");
		tfLastName.setText("");
		tfFirstName.setText("");
		tfGender.setText("");
		tfEmail.setText("");
		
		tfPersonCode.setBorder(UIManager.getBorder("TextField.border"));
		tfLastName.setBorder(UIManager.getBorder("TextField.border"));
		tfFirstName.setBorder(UIManager.getBorder("TextField.border"));
		tfGender.setBorder(UIManager.getBorder("TextField.border"));
		tfEmail.setBorder(UIManager.getBorder("TextField.border"));
	}
	
	public void setForm(int index) {
		if (results.size() != 0 && index < results.size()) {
			Person person = results.get(index);
			setForm(person);
		}
	}
	
	public void setForm(Person person) {
		initializing = true;
		tfPersonCode.setText(person.getPersonCode());
		tfLastName.setText(person.getLastName());
		tfFirstName.setText(person.getFirstName());
		tfGender.setText(person.getGender());
		tfEmail.setText(person.getEmail());
		initializing = false;
		resetButton.setEnabled(false);
	}
	
	public void setNavigation(int currentIndex, int maxIndex) {
		tfIndex.setText("" + (currentIndex + 1));
		tfMax.setText("" + maxIndex);	
	}
	
	public void setNavigationPanel(Person person) {
		int index = this.results.indexOf(person);
		setNavigation(index, this.results.size());
	}
	
	public void setComboPanel(Person person) {
		
	}
	
	private void setProblemField(JComponent component) {
		if (!initializing) {
			updatePerson = true;
			component.setBorder(BorderFactory.createLineBorder(Color.RED));
			resetButton.setEnabled(true);
		}
	}
	
	/**
	 * Responds to pressing of the Previous button.
	 */
	private void prevButton() {
		entryIndex--;
		if (entryIndex < 0) {
			entryIndex = results.size() - 1;
		}
		tfIndex.setText("" + (entryIndex + 1) );
		indexUpdate();
	}
	
	/**
	 * Responds to the pressing of the Next button.
	 */
	private void nextButton() {
		entryIndex++;
		if (entryIndex >= results.size()) {
			entryIndex = 0;
		}
		tfIndex.setText("" + (entryIndex + 1) );
		indexUpdate();
	}
	
	private void indexUpdate() {
		//initializing = true;
		clearForm();
		setForm(entryIndex);
		setNavigation(entryIndex, results.size());
		//initializing = false;
	}
	
	private void resetButton() {
		updatePerson = false;
		resetButton.setEnabled(false);
		indexUpdate();
	}
	
	private void newButton() {
		
	}
	
	private void setUp() {
		try {
			this.results = personQueries.getAllPeople();
		} catch (SQLException e) {}
		
		//initializing = true;
		if (results.size() != 0) {
			setForm(entryIndex);
			setNavigation(entryIndex, results.size());
		}
		//initializing = false;
		
		prevButton.setEnabled(true);
		nextButton.setEnabled(true);
		
		for (Person person : results){
			this.cbPersonModel.addElement(person);
		}
	}
	
	private class ComboBoxController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == cbPerson) {
				Person person = (Person)cbPerson.getSelectedItem(); 
				setForm(person);
			}
		}
	}
	
	private class NavigationController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == prevButton) {
				prevButton();
			} else if (e.getSource() == nextButton) {
				nextButton();
			} else if (e.getSource() == newButton) {
				newButton();
			} else if (e.getSource() == tfIndex) {
				indexUpdate();
			}
		}
	}
	
	private class DataController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == resetButton) {
				resetButton();
			}
			if (e.getSource() == newButton) {
				newButton();
			}
		}
	}
	
	private class TextFieldController implements DocumentListener {
		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub	
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			if (e.getDocument() == tfLastName.getDocument()) {
				setProblemField(tfLastName);
			} else if (e.getDocument() == tfFirstName.getDocument()) {
				setProblemField(tfFirstName);
			} else if (e.getDocument() == tfGender.getDocument()) {
				setProblemField(tfGender);
			} else if (e.getDocument() == tfEmail.getDocument()) {
				setProblemField(tfEmail);
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			if (e.getDocument() == tfLastName.getDocument()) {
				setProblemField(tfLastName);
			} else if (e.getDocument() == tfFirstName.getDocument()) {
				setProblemField(tfFirstName);
			} else if (e.getDocument() == tfGender.getDocument()) {
				setProblemField(tfGender);
			} else if (e.getDocument() == tfEmail.getDocument()) {
				setProblemField(tfEmail);
			}			
		}
	}
	
	public static void main(String[] args) {
		Connection connection = null;
		try {
			connection = DBConnection.getConnection();
		} catch (SQLException e) {
			
		}
		JFrame frame = new JFrame();
		frame.add(new TestPersonSimplePanel(connection));
		frame.setBounds(100, 100, 450, 300);
		frame.setTitle("Test Person Program");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
