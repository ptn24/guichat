package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ErrorPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private final JLabel errorLabel;
	
	public ErrorPanel(){
		this.errorLabel = new JLabel(" ");
		this.errorLabel.setName("errorLabel");
		this.add(this.errorLabel);
	}
	
	/**
	 * Change the text of 'errorLabel' to 'message'.
	 * @param message The new text for the error label.
	 */
	public void setErrorLabel(String message){
		this.errorLabel.setText(message);
	}
	
	/**
	 * Clear the text of 'errorLabel'.
	 */
	public void clearErrorLabel(){
		this.errorLabel.setText(" ");
	}
}
