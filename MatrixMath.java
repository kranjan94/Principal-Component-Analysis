/**
 * Class for performing matrix calculations.
 * @author	Kushal Ranjan
 * @version	051413
 */
class MatrixMath {
	
	/**
	 * Performs a QR factorization on the input matrix.
	 * @param input	input matrix
	 * @return		{Q, R}, the QR factorization of input.
	 */
	static double[][][] QRFactorize(double[][] input) {
		double[][][] out = new double[2][][];
		double[][] orthonorm = gramSchmidt(input);
		out[0] = orthonorm;
		double[][] R = new double[orthonorm.length][orthonorm.length];
		for(int i = 0; i < R.length; i++) {
			for(int j = 0; j <= i; j++) {
				R[i][j] = dot(input[i], orthonorm[j]);
			}
		}
		out[1] = R;
		return out;
	}
	
	/**
	 * Converts the input list of vectors into an orthonormal list with the same span.
	 * @param input	list of vectors
	 * @return		orthonormal list with the same span as input
	 */
	static double[][] gramSchmidt(double[][] input) {
		double[][] out = new double[input.length][input[0].length];
		for(int outPos = 0; outPos < out.length; outPos++) {
			double[] v = input[outPos];
			for(int j = outPos - 1; j >= 0; j--) {
				double[] sub = proj(v, out[j]);
				v = subtract(v, sub); //Subtract off non-orthogonal components
			}
			out[outPos] = normalize(v);
		}
		return out;
	}
	
	/**
	 * Returns the Givens rotation matrix with parameters (i, j, th).
	 * @param size	total number of rows/columns in the matrix
	 * @param i		the first axis of the plane of rotation; i > j
	 * @param j		the second axis of the plane of rotation; i > j
	 * @param th	the angle of the rotation
	 * @return		the Givens rotation matrix G(i,j,th)
	 */
	static double[][] GivensRotation(int size, int i, int j, double th) {
		double[][] out = new double[size][size];
		double sine = Math.sin(th);
		double cosine = Math.cos(th);
		for(int x = 0; x < size; x++) {
			if(x != i && x != j) {
				out[x][x] = cosine;
			} else {
				out[x][x] = 1;
			}
		}
		out[i][j] = -sine;//ith column, jth row
		out[j][i] = sine;
		return out;
	}
	
	/**
	 * Returns the transpose of the input matrix.
	 * @param matrix	double[][] matrix of values
	 * @return			the matrix transpose of matrix
	 */
	static double[][] transpose(double[][] matrix) {
		double[][] out = new double[matrix[0].length][matrix.length];
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				out[i][j] = matrix[j][i];
			}
		}
		return out;
	}
	
	/**
	 * Returns the sum of a and b.
	 * @param a	double[][] matrix of values
	 * @param b	double[][] matrix of values
	 * @return	the matrix sum a + b
	 */
	static double[][] add(double[][] a, double[][] b) {
		if(a.length != b.length || a[0].length != b[0].length) {
			throw new MatrixException("Matrices not same size.");
		}
		double[][] out = new double[a.length][a[0].length];
		for(int i = 0; i < out.length; i++) {
			for(int j = 0; j < out[0].length; j++) {
				out[i][j] = a[i][j] + b[i][j];
			}
		}
		return out;
	}
	
	/**
	 * Returns the difference of a and b.
	 * @param a	double[][] matrix of values
	 * @param b	double[][] matrix of values
	 * @return	the matrix difference a - b
	 */
	static double[][] subtract(double[][] a, double[][] b) {
		if(a.length != b.length || a[0].length != b[0].length) {
			throw new MatrixException("Matrices not same size.");
		}
		double[][] out = new double[a.length][a[0].length];
		for(int i = 0; i < out.length; i++) {
			for(int j = 0; j < out[0].length; j++) {
				out[i][j] = a[i][j] - b[i][j];
			}
		}
		return out;
	}
	
	/**
	 * Returns the difference of a and b.
	 * @param a	double[] vector of values
	 * @param b	double[] vector of values
	 * @return	the vector difference a - b
	 */
	static double[] subtract(double[] a, double[] b) {
		if(a.length != b.length) {
			throw new MatrixException("Vectors are not same length.");
		}
		double[] out = new double[a.length];
		for(int i = 0; i < out.length; i++) {
			out[i] = a[i] - b[i];
		}
		return out;
	}
	
	/**
	 * Returns the matrix product of a and b; if the horizontal length of a is not equal to the
	 * vertical length of b, throws an exception.
	 * @param a	double[][] matrix of values
	 * @param b	double[][] matrix of values
	 * @return	the matrix product ab
	 */
	static double[][] multiply(double[][] a, double[][] b) {
		if(a.length != b[0].length) {
			throw new MatrixException("Matrices not compatible for multiplication.");
		}
		double[][] out = new double[b.length][a[0].length];
		for(int i = 0; i < out.length; i++) {
			for(int j = 0; j < out.length; j++) {
				double[] row = getRow(a, j);
				double[] column = getColumn(b, i);
				out[i][j] = dot(row, column);
			}
		}
		return out;
	}
	
	/**
	 * Takes the dot product of two vectors, {a[0]b[0], ..., a[n]b[n]}.
	 * @param a	double[] of values
	 * @param b	double[] of values
	 * @return	the dot product of a with b
	 */
	static double dot(double[] a, double[] b) {
		if(a.length != b.length) {
			throw new MatrixException("Vector lengths not equal: " + a.length + "=/=" + b.length);
		}
		double sum = 0;
		for(int i = 0; i < a.length; i++) {
			sum += a[i] * b[i];
		}
		return sum;
	}
	
	/**
	 * Returns the ith column of the input matrix.
	 */
	static double[] getColumn(double[][] matrix, int i) {
		return matrix[i];
	}
	
	/**
	 * Returns the ith row of the input matrix.
	 */
	static double[] getRow(double[][] matrix, int i) {
		double[] vals = new double[matrix.length];
		for(int j = 0; j < vals.length; j++) {
			vals[j] = matrix[i][j];
		}
		return vals;
	}
	
	/**
	 * Returns the projection of vec onto the subspace spanned by proj
	 * @param vec	vector to be projected
	 * @param proj	spanning vector of the target subspace
	 * @return		proj_proj(vec)
	 */
	static double[] proj(double[] vec, double[] proj) {
		double constant = dot(proj, vec)/dot(proj, proj);
		double[] projection = new double[vec.length];
		for(int i = 0; i < proj.length; i++) {
			projection[i] = proj[i]*constant;
		}
		return projection;
	}
	
	/**
	 * Returns a normalized version of the input vector, i.e. vec scaled such that ||vec|| = 1.
	 * @return	vec/||vec||
	 */
	static double[] normalize(double[] vec) {
		double[] newVec = new double[vec.length];
		double norm = norm(vec);
		for(int i = 0; i < vec.length; i++) {
			newVec[i] = vec[i]/norm;
		}
		return newVec;
	}
	
	/**
	 * Computes the norm of the input vector
	 * @return ||vec||
	 */
	static double norm(double[] vec) {
		return Math.sqrt(dot(vec,vec));
	}
	
	/**
	 * Prints the input matrix with each value rounded to two decimal places.
	 */
	static void print(double[][] matrix) {
		for(int j = 0; j < matrix[0].length; j++) {
			for(int i = 0; i < matrix.length; i++) {
				double formattedValue = Double.parseDouble(String.format("%.3g%n", matrix[i][j]));
				System.out.print(formattedValue + "\t");
			}
			System.out.print("\n");
		}
	}
}

/**
 * Exception class thrown when invalid matrix calculations are attempted
 */
class MatrixException extends RuntimeException {
	MatrixException(String string) {
		super(string);
	}
}