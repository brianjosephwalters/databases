package apps;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ui.swing.panels.person.PersonMainPanel;
import ui.swing.test.ConnectionDialog;

@SuppressWarnings("serial")
public class PersonApp extends JFrame {
	private Connection connection;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenuItem mntmConnect;
	private JMenu mnConnection;
	private JMenuItem mntmReset;
	private JMenuItem mntmDisconnect;
	
	private MenuController menuController;

	/**
	 * Create the frame.
	 */
	public PersonApp() {
		this.connection = null;
		this.menuController = new MenuController();
		
		mntmConnect = new JMenuItem("Connect");
		mntmConnect.addActionListener(menuController);
		mntmReset = new JMenuItem("Reset");
		mntmReset.addActionListener(menuController);
		mntmDisconnect = new JMenuItem("Disconnect");
		mntmDisconnect.addActionListener(menuController);
		mnConnection = new JMenu("UserName");
		mnConnection.add(mntmReset);
		mnConnection.add(mntmDisconnect);
		
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		setDisconnected();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
		setBounds(100, 100, 650, 600);
		setTitle("Person Program");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
	private void setConnection(Connection connection, String userName) {
		if (connection != null) {
			this.connection = connection;
			setConnected(userName);
		}
	}
	
	private void setConnected(String userName) {
		menuBar.remove(mntmConnect);
		menuBar.add(mnConnection);
		mnConnection.setText(userName);
		// Add Panels
		setSize(600, 700);
		contentPane.add(new PersonMainPanel(connection));
	}
	
	private void setDisconnected() {
		menuBar.remove(mnConnection);
		menuBar.add(mntmConnect);
		mnConnection.setText("Disconnected");
	}
	
	private class MenuController implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == mntmConnect) {
				ConnectionDialog dialog = new ConnectionDialog();
				dialog.setVisible(true);
				setConnection(dialog.getConnection(), dialog.getUserName());
				dialog.dispose();
			}
			else if (e.getSource() == mntmDisconnect) {
				setDisconnected();
			}
			else if (e.getSource() == mntmReset) {
				
			}
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PersonApp frame = new PersonApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
