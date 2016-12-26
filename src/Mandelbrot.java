import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/** computes fractals based on the Mandelbrot series */
public class Mandelbrot extends FractalGeneratorAdapter {

	/** constructs a Mandelbrot fractal generator
	 *  based on the iteration z_(n+1) = z_n ^2 + c
	 *  initial zoom range (-2 - 1.5i) to (1 + 1.5i) */
	public Mandelbrot() {
		super("Mandelbrot",
				new Rectangle2D.Double(-2.0, -1.5, 3.0, 3.0),
				(z, c) ->
					new Point2D.Double(z.x * z.x - z.y * z.y + c.x,
									   2.0 * z.x * z.y + c.y)
		);
	}
	
}
