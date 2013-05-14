/**
 * Class for performing matrix calculations.
 * @author	Kushal Ranjan
 * @version	051413
 */
class MatrixMath {
	
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