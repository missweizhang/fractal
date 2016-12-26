import static org.junit.Assert.*;

import org.junit.Test;

import java.awt.geom.Rectangle2D;


public class MandelbrotTest {
	FractalGenerator fractal = new Mandelbrot();
	
	@Test
	public void testNumIterations() {
		// always converge
		assertEquals(-1, fractal.numIterations(0, 0));
		
		// immediately diverged |c| > 2
		assertEquals(0, fractal.numIterations(1.732051, 1.0));
		
		// diverges after several iterations
		assertTrue( 0 < fractal.numIterations(1.0, 1.0));
	}

	// tolerance for floating point comparisons
	static final double DELTA = 1e-4;

	@Test
	public void testGetInitialRangeDouble() {
		Rectangle2D.Double range = new Rectangle2D.Double(0, 0, 0, 0);
		fractal.getInitialRange(range);
		
		assertEquals(-2.0, range.getX(), DELTA);
		assertEquals(-1.5, range.getY(), DELTA);
		assertEquals(3.0, range.getHeight(), DELTA);
		assertEquals(3.0, range.getWidth(), DELTA);
		
		assertEquals(1.0, range.getMaxX(), DELTA);
		assertEquals(1.5, range.getMaxY(), DELTA);	
	}
}
