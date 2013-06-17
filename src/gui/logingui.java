package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class logingui extends JFrame {

	private JPanel contentPane;
	private JLabel lblNewLabel;
	private JLabel lblEnterUsername;
	private JTextField username;
	private JLabel lblEnterIp;
	private JTextField ip;
	private JLabel lblEnterPort;	
	private JTextField port;
	private JLabel results; 
	private JButton btnNewButton;
	
	private String usernameval;
	private String ipval;
	private String portval;
	
	/**
	 * Create the frame.
	 */
	public logingui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 250, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("icons\\icon2.jpg"));
		
		lblEnterUsername = new JLabel("Enter Username");
		lblEnterUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		username = new JTextField("");
		username.setColumns(10);
		
		lblEnterIp = new JLabel("Enter IP");
		lblEnterIp.setFont(new Font("Tahoma", Font.BOLD, 14));	
		
		ip = new JTextField("");
		ip.setColumns(10);
		
		lblEnterPort = new JLabel("Enter Port");
		lblEnterPort.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		port = new JTextField("");
		port.setColumns(10);
		
		results = new JLabel("");
		
		btnNewButton = new JButton("Log On");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Get the username and clear the field.
				usernameval = username.getText();
				username.setText("");
				
				//Get the IP and clear the field.
				ipval = ip.getText();
				ip.setText("");
				
				//Get the port and clear the field.
				portval = port.getText();
				port.setText("");			
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(lblNewLabel)
							.addGap(57))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(lblEnterUsername)
							.addGap(51))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(lblEnterIp)
							.addGap(78))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(ip, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
								.addComponent(username, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
								.addComponent(port, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE))
							.addGap(33))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(lblEnterPort)
							.addGap(71))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(btnNewButton)
							.addGap(73))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(results, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(33)
					.addComponent(lblNewLabel)
					.addGap(42)
					.addComponent(lblEnterUsername)
					.addGap(11)
					.addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblEnterIp, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblEnterPort, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(results)
					.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addGap(22))
		);
		contentPane.setLayout(gl_contentPane);
	}

	/**
	 * Launch the application.
	 */

	public String[] logval(){
		return new String[]{usernameval, ipval, portval};
	}	
	
	public JButton getButton(){
		return this.btnNewButton;
	}
	
	
	
	public void setResult(String result){
		results.setText(result);
		}

	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					logingui frame = new logingui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}	
}
