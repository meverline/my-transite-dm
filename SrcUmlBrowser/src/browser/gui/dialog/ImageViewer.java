package browser.gui.dialog;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImageViewer extends JPanel {

    private BufferedImage image;

    public ImageViewer(BufferedImage image)   {
       this.image = image;
       this.setSize(this.image.getWidth(), this.image.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getMaximumSize()
	 */
	@Override
	public Dimension getMaximumSize() {
	    return  new Dimension(this.image.getWidth(), this.image.getHeight());
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getMinimumSize()
	 */
	@Override
	public Dimension getMinimumSize() {
	    return  new Dimension(this.image.getWidth(), this.image.getHeight());
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
	    return  new Dimension(this.image.getWidth(), this.image.getHeight());
	}
    
    

}
