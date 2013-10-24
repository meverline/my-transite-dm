/**
 * 
 */
package browser.gui.dialog;

import java.awt.BorderLayout;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import browser.gui.commands.CloseDialogAction;

/**
 * @author markeverline
 *
 */
public class ExceptionDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExceptionDialog(JFrame frame, Exception e, String message)
	{
		super(frame);
		build(e, message);
		setTitle("Error");
		setVisible(true);
		
	}
	
	public ExceptionDialog(JDialog frame, Exception e, String message)
	{
		super(frame);
		build(e, message);
		setTitle("Error");
		setVisible(true);

	}
	
	public ExceptionDialog(JFrame frame, Exception e)
	{
		super(frame);
		build(e, e.getLocalizedMessage());
		setTitle("Error");
		setVisible(true);
		
	}
	
	public ExceptionDialog(JDialog frame, Exception e)
	{
		super(frame);
		build(e, e.getLocalizedMessage());
		setTitle("Error");
		setVisible(true);

	}
	
	/**
	 * 
	 */
	private void build(Exception e, String message)
	{
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());
		
		JTabbedPane tabpane = new JTabbedPane();
		panel.add(tabpane, BorderLayout.CENTER);
		
		tabpane.addTab("Message", new JLabel(message));
		
		JTextArea textArea = new JTextArea();
		
		StringWriter writer = new StringWriter();
		PrintWriter print = new PrintWriter(writer);
		
		e.printStackTrace(print);
		
		textArea.setText(writer.toString());
		
		tabpane.addTab("Stack Trace", new JScrollPane(textArea));
		 		
		JPanel buttonBox = new JPanel();
		
		JButton close = new JButton("Cancel");
		close.addActionListener( new CloseDialogAction(this));
		buttonBox.add(close);
		
		panel.add(buttonBox, BorderLayout.SOUTH);
		
		this.setContentPane(panel);
	}
}
