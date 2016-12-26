import java.awt.geom.Rectangle2D;

/** computes fractals based on the Tricorn series 
 *  z_(n+1) = z_n_conj ^2 + c 
 *  where conj denotes the complex conjugate */
public class Tricorn extends FractalGenerator {

	/** number of iterations for series to be considered diverging */ 
	public static final int MAX_ITERATIONS = 2000;

	/** set the initial zoom range to (-2, -2) to (2, 2) */
	@Override
	public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2.0;
        range.y = -2.0;
        range.width = 4.0;
        range.height = 4.0;
	}

	
	/** compute Tricorn series <em>z_(n+1)</em> = z_n_conj ^ 2 + c,
	 *  where c = x + iy is a complex number representing 
	 *  a point on the 2D image
	 *  
	 *  @param x real part of c
	 *  @param y imaginary part of c
	 *  @return number of iterations it takes until |<em>z</em>|&gt;2, or -1 if exceeds 2000 iterations
	 * */
	@Override
	public int numIterations(double x, double y) {
		// z_0 = 0, z_1 = c = x + iy
		double real = x, imaginary = y; 
		
		for(int i=0; i < MAX_ITERATIONS; i++) {
			// calculate mod squared |z|^2 > 4
			if ((real * real + imaginary * imaginary) > 4.0) {
				// series diverged
				return i;
			}

			// calculate next iteration z_(n+1)
			// z_conj ^2 + c = (re - i*im)^2 + x + i*y
			//         = re^2 - im^2 - 2*i*re*im + x + i*y
			double nextReal = real * real - imaginary * imaginary + x;
			double nextImaginary = -2.0 * real * imaginary + y;

			real = nextReal;
			imaginary = nextImaginary;
		}	
		
		// series did not diverge
		return -1;
	}
}
