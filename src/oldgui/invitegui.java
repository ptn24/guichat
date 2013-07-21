package oldgui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import main.Client;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class invitegui extends JFrame {

	private JPanel contentPane;
	private Client client;


	/**
	 * Create the frame.
	 */
	public invitegui(final Client client) {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 300, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		this.client = client;
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("icons\\envelope_icon.png"));
		
		JLabel lblWouldYouLike = new JLabel("Would You Like to Join This Conversation");
		lblWouldYouLike.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton accept = new JButton("Accept");
		accept.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				client.enterChat(getTitle());
				client.removeInvite(getTitle());
				setVisible(false);
			}
		});
		
		JButton Decline = new JButton("Decline");
		Decline.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				client.removeInvite(getTitle());
				setVisible(false);
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(78)
							.addComponent(lblNewLabel))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(15)
							.addComponent(lblWouldYouLike)))
					.addContainerGap(5, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(50, Short.MAX_VALUE)
					.addComponent(accept)
					.addGap(54)
					.addComponent(Decline)
					.addGap(40))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(19)
					.addComponent(lblNewLabel)
					.addGap(43)
					.addComponent(lblWouldYouLike)
					.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(accept)
						.addComponent(Decline))
					.addGap(28))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	
}
