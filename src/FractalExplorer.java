import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.SwingWorker;

/** Swing GUI for displaying fractals on a complex plane */
public class FractalExplorer {
	
	/** display width and height in pixels */
	private int size;
	
	/** reference to BufferedImage component for fractal display */
	private JImageDisplay display;
	
	/** calculation engine for generating fractals */
	private FractalGenerator fractal = new Mandelbrot();
	
	/** UI component for choosing between fractal generators */
	JComboBox<FractalGenerator> fractalChooser = new JComboBox<>();

	/** reset button */
	JButton resetButton = new JButton("Reset Display");

	/** save button */
	JButton saveButton = new JButton("Save");
	
	/** represent current zoom range */
	private Rectangle2D.Double range = new Rectangle2D.Double();
	
	/** number of rows remaining to draw: control concurrency */
	int nRowsRemaining;
	
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
		
		ActionListener handler = new ActionHandler(frame);
		
		resetButton.setActionCommand("reset");
		resetButton.addActionListener(handler);

		saveButton.setActionCommand("save");
		saveButton.addActionListener(handler);

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(resetButton);
		bottomPanel.add(saveButton);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		
		JLabel label = new JLabel("Fractal:");
		fractalChooser.addItem(this.fractal); // default Mandelbrot
		fractalChooser.addItem(new Tricorn());
		fractalChooser.addItem(new BurningShip());	
		fractalChooser.addActionListener(handler);
		
		JPanel topPanel = new JPanel();
		topPanel.add(label);
		topPanel.add(fractalChooser);
		frame.add(topPanel, BorderLayout.NORTH);	
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	
	/** handles action events: from buttons and combo box */
	private class ActionHandler implements ActionListener {
		private final Component parent;

		/** constructor initializes parent component for centering dialog boxes */
		ActionHandler(final Component parent) {
			this.parent = parent;
		}

		/** draw new fractal or reset image to zoom out or save to file */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			// choose new fractal type
			if (e.getSource() == fractalChooser) {
				fractal = (FractalGenerator) fractalChooser.getSelectedItem();
			}
			// reset to default zoom
			else if (e.getActionCommand() == "reset") {
				// nothing extra to do except what's common below
			}
			// save to PNG file
			else if (e.getActionCommand() == "save") {
				JFileChooser chooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
				chooser.setFileFilter(filter);
				chooser.setAcceptAllFileFilterUsed(false);

				int returnVal = chooser.showSaveDialog(parent);
				if (returnVal != JFileChooser.APPROVE_OPTION) {
					return; // user canceled
				}

				File file = chooser.getSelectedFile();
				try {
					ImageIO.write(display.getImage(), "png", file);
				}
				catch (IOException exception) {
					JOptionPane.showMessageDialog(parent, exception.getMessage(), 
							"Cannot Save Image", JOptionPane.ERROR_MESSAGE);
				}
				return; // don't refresh
			}
			
			// refresh image for both new fractal type
			// or for reset button click to zoom out
			fractal.getInitialRange(range);
			drawFractal();
		}
	}
	
	
	/** handles mouse click events by zooming in */
	private class MouseHandler extends MouseAdapter {
		
		/** zoom in around mouse click and draw */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (nRowsRemaining > 0) { // busy, not done drawing!
				return; // ignore mouse click
			}

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
	

	/** enable or disable UI components */
	private void enableUI(boolean val) {
		saveButton.setEnabled(val);
		resetButton.setEnabled(val);
		fractalChooser.setEnabled(val);
	}

	/** SwingWorker to compute color from costly number of iterations in background */
	private class FractalWorker extends SwingWorker<Void, Void> {
		/** the y coordinate */
		private int y;

		/** the row of colors computed */
		private int[] colors;

		/** construct worker for a row number, i.e. y coordinate */
		FractalWorker(int y) {
			this.y = y;
		}

		/** compute number of iterations for a row and store */
		@Override
		public Void doInBackground() {
			double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, y);
			colors = new int[size];

			// compute color for row based on number of fractal iterations until divergence
			for (int x=0; x<size; x++) {
				double xCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, x);
				colors[x] = fractal.numIterations(xCoord, yCoord);
			}
			return null;
		}

		/** paint color from number of iterations and update display */
		@Override
		protected void done() {

			// loop over each column of pixels
			for (int x=0; x<size; x++) {
				if (colors[x] == -1) {// black
					display.drawPixel(x, y, 0); // 0 = black
//					display.drawPixel(x, y, Color.BLACK.getRGB());
				}
				else { // smooth color based on number of iterations 
					float hue = 0.7f + (float) colors[x] / 200f;
					display.drawPixel(x, y, Color.HSBtoRGB(hue, 1f, 1f));
				}
			}

			// update display for row y, width=size, height=1
			// note: unused first parameter set to 0
			display.repaint(0, y, size, 1);

			// enable UI when done drawing all rows
			nRowsRemaining--;
			if (nRowsRemaining == 0) {
				enableUI(true);
			}
		}
	}


	/** helper to render each pixel and display */
	private void drawFractal() {
		enableUI(false); // disable, we're busy!
		nRowsRemaining = size;

		// loop over each row of pixels
		for (int y=0; y<size; y++) {

			// compute color on background thread then paint
			FractalWorker w = new FractalWorker(y);
			w.execute();
			// enable UI only when worker is all done in FractalWorker.done()
		}
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
