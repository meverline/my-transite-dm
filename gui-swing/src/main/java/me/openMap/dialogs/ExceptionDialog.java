package me.openMap.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class ExceptionDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1886097313093400990L;
	
	/**
	 * 
	 * @param f
	 * @param ex
	 */
	public ExceptionDialog(Frame f, Exception ex) 
	{
		super(f, true);
		
		this.setName("Exception");
		setSize(300, 300);
		build(ex);	
		this.setLocationRelativeTo(f);
		this.setTitle("Exception");
		this.setVisible(true);
	}
	
	/**
	 * 
	 * @param f
	 * @param ex
	 */
	public static void show(Frame f, Exception ex)
	{
	    new ExceptionDialog(f, ex);
		return;
	}
	
	/**
	 * 
	 * @param ex
	 */
	private void build(Exception ex)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane();
		panel.add(tabbedPane, BorderLayout.CENTER);
		
		tabbedPane.addTab("Message", new JTextArea(ex.getMessage()));
		
		StringWriter str = new StringWriter();
		PrintWriter pw = new PrintWriter(str);
		
		ex.printStackTrace(pw);
		
		tabbedPane.addTab("Stack Track", new JTextArea(str.toString()));
		
		
		JPanel buttons = new JPanel();
		
		JButton close = new JButton("Close");
		close.addActionListener( new CloseAction(this));
		buttons.add(close);
		
		panel.add(buttons, BorderLayout.SOUTH);
		
		this.setContentPane(panel);	
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////

	private class CloseAction implements ActionListener {
		
		JDialog dialog_ = null;
		
		public CloseAction(JDialog dialog)
		{
			dialog_ = dialog;
		}

		public void actionPerformed(ActionEvent e) {
			dialog_.setVisible(false);
		}
	}

}
