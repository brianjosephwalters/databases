package ui.swing.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Person;

@SuppressWarnings("serial")
public class NavigationPanel extends JPanel {
	NavigationController controller;
	
	List<Person> list;
	int currentIndex;
	
	PropertyChangeSupport pcS;
	
	JButton btnDelete;
	JButton btnPrevious;
	JButton btnNext;
	JButton btnNew;
	JTextField tfIndex;
	JTextField tfMax;

	/**
	 * Create the panel.
	 */
	public NavigationPanel(List<Person> list) {
		pcS = new PropertyChangeSupport(this);
		this.currentIndex = 0;
		this.list = list;
		
		this.controller = new NavigationController();
		initializeGUIComponents();
		updateList(list);
	}
	
	private void initializeGUIComponents() {
		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		btnDelete.setActionCommand("delete");
		btnDelete.addActionListener(controller);
		
		btnPrevious = new JButton("Previous");
		//btnPrevious.setEnabled(false);
		btnPrevious.setActionCommand("previous");
		btnPrevious.addActionListener(controller);
		
		tfIndex = new JTextField();
		tfIndex.setHorizontalAlignment(JTextField.CENTER);
		tfIndex.setActionCommand("index");
		tfIndex.addActionListener(controller);
		
		tfMax = new JTextField();
		tfMax.setHorizontalAlignment(JTextField.CENTER);
		tfMax.setEditable(false);
		
		btnNext = new JButton("Next");
		//btnNext.setEnabled(false);
		btnNext.setActionCommand("next");
		btnNext.addActionListener(controller);
		
		btnNew = new JButton("New");
		btnNew.setEnabled(false);
		btnNew.setActionCommand("new");
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
		add(Box.createHorizontalGlue());
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcS.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcS.addPropertyChangeListener(listener);
	}
	
	public int getCurrentIndex() {
		return this.currentIndex;
	}
	
	public void setCurrentIndex(int newIndex) {
		int oldIndex = this.currentIndex;
		this.currentIndex = newIndex;
		pcS.firePropertyChange("currentIndex", oldIndex, newIndex);
	}
	
	private void updateList(List<Person> list) {
		this.list = list;
		setCurrentIndex(0);
		tfIndex.setText("" + getCurrentIndex() + 1);
		tfMax.setText("" + list.size());
	}
	
	private class NavigationController implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == btnDelete) {
				deleteButton();
			} else if (e.getSource() == btnPrevious) {
				prevButton();
			} else if (e.getSource() == btnNext) {
				nextButton();
			} else if (e.getSource() == btnNew) {
				newButton();
			} else if (e.getSource() == tfIndex) {
				indexUpdate();
			}
		}
		
		private void deleteButton() {
			
		}
		
		private void prevButton() {
			if (getCurrentIndex() - 1 < 0) {
				setCurrentIndex(list.size() - 1);
			}  else {
				setCurrentIndex(getCurrentIndex() - 1);
			}
			tfIndex.setText("" + (getCurrentIndex() + 1) );
		}
		
		private void nextButton() {
			if (getCurrentIndex() + 1 >= list.size()) {
				setCurrentIndex(0);
			} else {
				setCurrentIndex(getCurrentIndex() + 1);
			}
			tfIndex.setText("" + (getCurrentIndex() + 1) );
		}
		
		private void newButton () {
			
		}
		
		private void indexUpdate() {
			int index = Integer.parseInt(tfIndex.getText()) - 1;
			if (index >= 0 && index < list.size()) {
				setCurrentIndex(index);
			}
			tfIndex.setText("" + (getCurrentIndex() + 1));
		}
	}
	
	public class ListListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("personList")) {
				updateList( (List<Person>)evt.getNewValue() );
			}
		}
	}
}
