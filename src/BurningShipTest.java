import static org.junit.Assert.*;

import java.awt.geom.Rectangle2D;

import org.junit.Test;

public class BurningShipTest {
	FractalGenerator fractal = new BurningShip();
	
	@Test
	public void testNumIterations() {
		// origin always converge
		assertEquals(-1, fractal.numIterations(0, 0));
		
		// |2 + i| > 2, diverges immediately
		assertEquals(1, fractal.numIterations(2.0, 1.0));

		// c = 1 + i diverges after several iteration
		assertEquals(2, fractal.numIterations(1.0, 1.0));
	}

	static final double DELTA = 1e-4;
	
	@Test
	public void testGetInitialRange() {
		Rectangle2D.Double range = new Rectangle2D.Double(0, 0, 0, 0);
		fractal.getInitialRange(range);
		
		assertEquals(-2.0, range.getX(), DELTA);
		assertEquals(-2.5, range.getY(), DELTA);
		assertEquals(4.0, range.getWidth(), DELTA);
		assertEquals(4.0, range.getHeight(), DELTA);

		assertEquals(2.0, range.getMaxX(), DELTA);
		assertEquals(1.5, range.getMaxY(), DELTA);
	}

}
