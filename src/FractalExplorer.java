import java.awt.geom.Rectangle2D;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Swing GUI for displaying fractals on a complex plane */
public class FractalExplorer {
	
	/** display width and height in pixels */
	private int size;
	
	/** reference to BufferedImage component for fractal display */
	private JImageDisplay display;
	
	/** calculation engine for generating fractals */
	private FractalGenerator fractal = new Mandelbrot();
	
	/** represent current zoom range */
	private Rectangle2D.Double range = new Rectangle2D.Double();
	
	
	/** constructor initializes private variables */
	public FractalExplorer(int size) {
		this.size = size;
		display = new JImageDisplay(size, size);
		fractal.getInitialRange(range);
	}
	
	
	/** helper to set up Swing user interface */
	private void createAndShowGUI() {
		JFrame frame = new JFrame("Fractal Explorer");
		frame.setLayout(new BorderLayout());
		
		display.addMouseListener(new MouseHandler());
		frame.add(display, BorderLayout.CENTER);
		
		JButton button = new JButton("Reset Display");
		button.addActionListener(new ActionHandler());
		frame.add(button, BorderLayout.SOUTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	
	/** handles button click events by zooming out */
	private class ActionHandler implements ActionListener {
		
		/** reset display to initial zoom and draw */
		@Override
		public void actionPerformed(ActionEvent e) {
			fractal.getInitialRange(range);
			drawFractal();
		}
	}
	
	
	/** handles mouse click events by zooming in */
	private class MouseHandler extends MouseAdapter {
		
		/** zoom in around mouse click and draw */
		@Override
		public void mouseClicked(MouseEvent e) {
			// get mouse coordinates
			double x = FractalGenerator.getCoord(range.x, range.x + range.width, 
												 size, e.getX());
			double y = FractalGenerator.getCoord(range.y, range.y + range.height, 
												 size, e.getY());
			
			// zoom in
			fractal.recenterAndZoomRange(range, x, y, 0.5);
			drawFractal();
		}
	}
	
	
	/** helper to render each pixel and display */
	private void drawFractal() {
		// loop over each pixel
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				
				// compute coordinates
				double x = FractalGenerator.getCoord(range.x, range.x + range.width, size, i);
				double y = FractalGenerator.getCoord(range.y, range.y + range.height, size, j);
				
				// compute number of iterations for divergence
				int n = fractal.numIterations(x, y);
				
				// draw
				if (n == -1) { // black
					display.drawPixel(i, j, 0); // 0 = black
//					display.drawPixel(i, j, Color.BLACK.getRGB());
				}
				else { // smooth color based on number of iterations 
					float hue = 0.7f + (float) n / 200f;
					display.drawPixel(i, j, Color.HSBtoRGB(hue, 1f, 1f));
				}
			}
		}
		
		// update display
		display.repaint();
	}
	
	
    /**
     * Entry-point for the application.  No command-line arguments are
     * recognized at this time.
     **/
	public static void main(String[] args) {
		FractalExplorer explorer = new FractalExplorer(800);
    	explorer.createAndShowGUI();			
    	explorer.drawFractal();
	} 
}
