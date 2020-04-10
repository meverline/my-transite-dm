package me.openMap.command;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import me.openMap.OpenTransitMap;


public class SaveAsImage implements ICommand{

	private OpenTransitMap top_ = null;
	private static JFileChooser chooser_ = null;

	public SaveAsImage()
	{	
	}

	public void actionPerformed(ActionEvent e) {

		if ( chooser_ == null ) {
		      chooser_ = new JFileChooser();

		      chooser_.setDialogType( JFileChooser.SAVE_DIALOG);
			  SaveFileFilter filter = new SaveFileFilter();
			  filter.addExtension("jpg");
			  filter.setDescription("JPG Images");
			  chooser_.setFileFilter(filter);
		}

	    int returnVal = chooser_.showSaveDialog(top_);

	    if(returnVal == JFileChooser.APPROVE_OPTION) {

	    	File fp = chooser_.getSelectedFile();

	    	try {

	            int width = top_.getMap().getMapKit().getMainMap().getWidth();
	            int height = top_.getMap().getMapKit().getMainMap().getHeight();
	            BufferedImage image = new BufferedImage(width, height,
	                                                    BufferedImage.TYPE_INT_RGB);
	            Graphics2D g2 = image.createGraphics();
	            top_.getMap().getMapKit().getMainMap().paint(g2);
	            g2.dispose();

	            ImageIO.write(image, "jpg", fp);

	    	} catch ( Exception exp ) {
	    		JOptionPane.showMessageDialog(top_, exp.toString(), "Error Writeing: " + fp.toString(), JOptionPane.ERROR_MESSAGE);
	    	}

	    }
	}

	@Override
	public void initilize(OpenTransitMap app) {
		top_  = app;
	}

	@Override
	public void paramenters(List<String> parameters) {
	}

}
