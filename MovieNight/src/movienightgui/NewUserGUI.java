package movienightgui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class NewUserGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameTextField;
	private JPanel usernamePanel;
	private JPanel PassField;
	private JLabel passLabel;
	private JPasswordField passField;
	private JPanel buttonsPanel;
	private JButton createNewUserButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewUserGUI frame = new NewUserGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NewUserGUI() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 250, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(3, 1, 0, 0));
		
		usernamePanel = new JPanel();
		usernamePanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		contentPane.add(usernamePanel);
		
		JLabel usernameLabel = new JLabel("New Username:");
		usernamePanel.add(usernameLabel);
		
		usernameTextField = new JTextField();
		usernamePanel.add(usernameTextField);
		usernameTextField.setColumns(10);
		
		PassField = new JPanel();
		contentPane.add(PassField);
		
		passLabel = new JLabel("New Password:");
		PassField.add(passLabel);
		
		passField = new JPasswordField();
		passField.setColumns(10);
		PassField.add(passField);
		
		buttonsPanel = new JPanel();
		contentPane.add(buttonsPanel);
		
		createNewUserButton = new JButton("Create New User");
		buttonsPanel.add(createNewUserButton);
	}

}
