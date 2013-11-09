package ui.swing.test;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class TestApplication {

	private JFrame frmWorkforceAndJob;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestApplication window = new TestApplication();
					window.frmWorkforceAndJob.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestApplication() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWorkforceAndJob = new JFrame();
		frmWorkforceAndJob.setTitle("Workforce and Job Market Management System");
		frmWorkforceAndJob.setBounds(100, 100, 640, 480);
		frmWorkforceAndJob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmWorkforceAndJob.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel PeoplePanel = new JPanel();
		tabbedPane.addTab("People", null, PeoplePanel, null);
		PeoplePanel.setLayout(new BorderLayout(0, 0));
		
		JPanel CompanyPanel = new JPanel();
		tabbedPane.addTab("Companies", null, CompanyPanel, null);
		
		JPanel CoursesPanel = new JPanel();
		tabbedPane.addTab("Courses", null, CoursesPanel, null);
	}

}
