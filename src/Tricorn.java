import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/** computes fractals based on the Tricorn series */
public class Tricorn extends FractalGeneratorAdapter {

	/** constructs a Tricorn fractal generator
	 *  based on the iteration z_(n+1) = z_n_conj ^ 2 + c
	 *  where conj denotes the complex conjugate
	 *  initial zoom range (-2, -2) to (2, 2) */
	public Tricorn() {
		super("Tricorn",
				new Rectangle2D.Double(-2.0, -2.0, 4.0, 4.0),
				(z, c) ->
					new Point2D.Double(z.x * z.x - z.y * z.y + c.x,
									   -2.0 * z.x * z.y + c.y)
		);
	}

}
