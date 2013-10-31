package browser.gui.dialog;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import prefuse.data.Graph;
import browser.gui.commands.CloseDialogAction;

public class ImageDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageDialog(JDialog frame, Graph graph, String title)
	{
		super(frame);
		build(graph, title);
		setTitle(title);
		setSize(600, 600);
		this.setLocationByPlatform(true);
		setVisible(true);
		
	}
		
	/**
	 * 
	 */
	private void build(Graph graph, String title)
	{
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());
			
		ImageViewer viewer = new ImageViewer(graph, title);
		JScrollPane sw = new JScrollPane(viewer);
		sw.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sw.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(sw, BorderLayout.CENTER);

		JPanel buttonBox = new JPanel();
		
		JButton close = new JButton("Cancel");
		close.addActionListener( new CloseDialogAction(this));
		buttonBox.add(close);
		
		panel.add(buttonBox, BorderLayout.SOUTH);
		
		this.setContentPane(panel);
	}
}
