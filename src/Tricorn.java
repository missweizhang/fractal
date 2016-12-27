import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/** computes fractals based on the Tricorn series */
public class Tricorn extends FractalGeneratorAdapter {

	/** constructs a Tricorn fractal generator
	 *  based on the iteration z_(n+1) = z_n_conj ^ 2 + c
	 *  where conj denotes the complex conjugate
	 *  initial zoom range (-2, -2) to (2, 2) */
	public Tricorn() {
		super("Tricorn", (z, c) ->
				new Point2D.Double(z.x * z.x - z.y * z.y + c.x,
								   -2.0 * z.x * z.y + c.y)
		);
	}


	/** set the initial zoom range to (-2, -2) to (2, 2) */
	@Override
	public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2.0;
        range.y = -2.0;
        range.width = 4.0;
        range.height = 4.0;
	}


}
