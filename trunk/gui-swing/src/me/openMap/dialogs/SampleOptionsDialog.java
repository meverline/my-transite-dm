package me.openMap.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class SampleOptionsDialog extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1886097313093400990L;
	private JPanel layOutPanel = null;
	private JButton applyButton = null;
	
	/**
	 * 
	 * @param f
	 * @param title
	 */
	public SampleOptionsDialog(Frame f, String title) 
	{
		super(f, true);
		
		this.setName(title);
		build();
		setSize(300, 300);
		this.setLocationRelativeTo(f);
		this.setTitle(title);
	}
	
	/**
	 * 
	 * @return
	 */
	public JPanel getLayOutPanel()
	{
		return layOutPanel; 
	}
	
	/**
	 * 
	 * @return
	 */
	public JButton getApplyButton()
	{
		return applyButton;
	}
	
	/**
	 * 
	 * @param f
	 * @param title
	 */
	public static void show(Frame f, String title)
	{
	    new SampleOptionsDialog(f, title);
		return;
	}
	
	/**
	 * 
	 */
	private void build()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		this.layOutPanel = new JPanel();
		panel.add(this.layOutPanel, BorderLayout.CENTER);
		
		JPanel buttons = new JPanel();
		
		applyButton = new JButton("Apply");
		buttons.add(applyButton);
		applyButton.addActionListener( new CloseAction(this));
		
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
