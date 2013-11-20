package ui.swing.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import javax.swing.JMenu;

@SuppressWarnings("serial")
public class TestPersonFrame extends JFrame {
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
	public TestPersonFrame() {
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
		
		
		setBounds(100, 100, 450, 300);
		setTitle("Test Person Program");
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
				setConnection(dialog.getConnection(), dialog.getUserName());
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
					TestPersonFrame frame = new TestPersonFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
