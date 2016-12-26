import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/** computes fractals based on the burning ship series */
public class BurningShip extends FractalGeneratorAdapter {

	/** constructs a Burning Ship fractal generator
	 *  based on the iteration z_(n+1) = (|x_n| + i*|y_n|)^2 + c
	 *  where z_n = x_n + i * y_n
	 *  initial zoom range (-2, -2.5) to (2, 1.5) */
	public BurningShip() {
		super("BurningShip", 
				new Rectangle2D.Double(-2.0, -2.5, 4.0, 4.0), 
				(z, c) -> 
					new Point2D.Double(z.x * z.x - z.y * z.y + c.x, 
									   2.0 * Math.abs(z.x * z.y) + c.y)
		);
	}

}
