package browser.gui.dialog;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import browser.gui.commands.CloseDialogAction;

public class ImageDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageDialog(JDialog frame, String fileName, String title)
	{
		super(frame);
		build(fileName);
		setTitle(title);
		setSize(600, 600);
		this.setLocationByPlatform(true);
		setVisible(true);
		
	}
		
	/**
	 * 
	 */
	private void build(String imageFile)
	{
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());
		
		BufferedImage image;
		try {
			image = ImageIO.read(new File(imageFile));
			
			ImageViewer viewer = new ImageViewer(image);
			JScrollPane sw = new JScrollPane(viewer);
			sw.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			sw.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			panel.add(sw, BorderLayout.CENTER);

		} catch (IOException e) {
			e.printStackTrace();
		}
			
		JPanel buttonBox = new JPanel();
		
		JButton close = new JButton("Cancel");
		close.addActionListener( new CloseDialogAction(this));
		buttonBox.add(close);
		
		panel.add(buttonBox, BorderLayout.SOUTH);
		
		this.setContentPane(panel);
	}
}
