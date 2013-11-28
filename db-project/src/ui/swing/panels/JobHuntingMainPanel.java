package ui.swing.panels;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import db.DBConnection;

import java.awt.Color;
import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class JobHuntingMainPanel extends JPanel {
	Connection connection;
	
	/**
	 * Create the panel.
	 */
	public JobHuntingMainPanel(Connection connection) {
		this.connection = connection;
	
		
		initializeGUIComponents();
	}
	
	private void initializeGUIComponents() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JPanel personPanel = new JPanel();
		personPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Person Summary", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(personPanel);
		
		JPanel jobQualifiedPanel = new JPanel();
		jobQualifiedPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Jobs Qualified", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		add(jobQualifiedPanel);
		
		JPanel jobNearlyQualifiedPanel = new JPanel();
		jobNearlyQualifiedPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Jobs Closely Qualified", TitledBorder.RIGHT, TitledBorder.TOP, null, null));
		add(jobNearlyQualifiedPanel);
	}
	
	public static void main (String[] args) {
		Connection connection = null;
		try {
			connection = DBConnection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JFrame frame = new JFrame();
		frame.add(new JobHuntingMainPanel(connection));
		frame.setBounds(10, 10, 650, 500);
		frame.setTitle("Job Hunting Program");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
