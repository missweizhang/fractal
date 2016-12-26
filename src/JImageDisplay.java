import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;


/** BufferedImage component for drawing colored graphics */
public class JImageDisplay extends JComponent {

	/** image content that can be written to */
	private BufferedImage image;
	
	/** constructs a new JImageDisplay with 8-bit RGB pixels */
	JImageDisplay(int width, int height) {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		setPreferredSize(new Dimension(width, height));
	}
	
	public BufferedImage getImage() {
		return image;
	}

	/** sets all pixels in the image to black */
	public void clearImage() {
		int black = 0; // RGB value
		
		for (int x=0; x<image.getWidth(); x++) {
			for (int y=0; y<image.getHeight(); y++) {
				image.setRGB(x, y, black); 
			}
		}
	}

	/** set a single pixel to a specific color */
	public void drawPixel(int x, int y, int rgbColor) {
		image.setRGB(x, y, rgbColor);
	}
	
    /**
     * Implementation of the paint method to draw the image.
     **/
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	}
}
