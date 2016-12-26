import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.function.BinaryOperator;

/**
 * An adapter class for creating general fractal generators
 * each defining its own series of complex numbers
 */
public abstract class FractalGeneratorAdapter extends FractalGenerator {

	/** number of iterations for series to be considered diverging */
	public static final int MAX_ITERATIONS = 2000;

	/** name of the fractal generator */
	private String name;

	/** default initial zoom range */
	private Rectangle2D.Double initialRange;

	/** define iteration function for generating the next complex number
	 *  in the fractal series z_(n+1) = f(z_n, c)
	 * */
	protected BinaryOperator<Point2D.Double> iterationStep;


	/** constructs a general fractal generator */
	FractalGeneratorAdapter(String name, Rectangle2D.Double range,
			BinaryOperator<Point2D.Double> mapper) {
		this.name = name;
		initialRange = (Rectangle2D.Double) range.clone();
		iterationStep = mapper;
	}


	/** set initial zoom range
	 */
	@Override
	public void getInitialRange(Rectangle2D.Double range) {
		range.x = initialRange.x;
		range.y = initialRange.y;
		range.width = initialRange.width;
		range.height = initialRange.height;
	}


	/** compute fractal series z_(n+1) = f(z_n, c) until divergence
	 * @param x real part of c
	 * @param y imaginary part of c
	 * @return number of iterations until |<em>z</em>|&gt;2, or -1 if converges
	 */
	@Override
	public int numIterations(double x, double y) {
		// represent complex numbers as points in the complex plane
		Point2D.Double c = new Point2D.Double(x, y);
		// z_0 = 0
		Point2D.Double z = new Point2D.Double();

		for(int i=0; i < MAX_ITERATIONS; i++) {
			// calculate mod squared |z|^2 > 4
			if ((z.x * z.x + z.y * z.y) > 4.0) {
				// series diverged
				return i;
			}

			// calculate next iteration z_(n+1) = f(z_n, c)
			Point2D.Double zNext = iterationStep.apply(z, c);
			z.x = zNext.x;
			z.y = zNext.y;
		}

		// series did not diverge
		return -1;
	}


	/** name of the fractal generator */
	@Override
	public String toString() {
		return name;
	}

}
