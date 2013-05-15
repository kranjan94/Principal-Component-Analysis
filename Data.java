/**
 * Holds the information of a data set. Each row contains a single data point.
 * @author	Kushal Ranjan
 * @version	051313
 */
class Data {
	double[][] matrix; //matrix[i] is the ith row; matrix[i][j] is the ith row, jth column
	
	/**
	 * Constructs a new data matrix.
	 * @param vals	data for new Data object; dimensions as columns, data points as rows.
	 */
	Data(double[][] vals) {
		matrix = new double[vals.length][vals[0].length];
		for(int i = 0; i < vals.length; i++) {
			for(int j = 0; j < vals[0].length; j++) {
				matrix[i][j] = vals[i][j];
			}
		}
	}
	
	/**
	 * Test code. Constructs an arbitrary data table of 5 data points with 3 variables, normalizes
	 * it, and computes the covariance matrix and its eigenvalues and orthonormal eigenvectors.
	 * Then determines the two principal components.
	 */
	public static void main(String[] args) {
		double[][] data = {{4, 4.2, 3.9, 4.3, 4.1}, {2, 2.1, 2, 2.1, 2.2}, 
				{0.6, 0.59, 0.58, 0.62, 0.63}};
		Data dat = new Data(data);
		dat.normalize();
		double[][] cov = dat.covarianceMatrix();
		System.out.println("Covariance matrix:");
		Matrix.print(cov);
		EigenSet eigen = dat.getCovarianceEigenSet();
		double[][] vals = {eigen.values};
		System.out.println("Eigenvalues:");
		Matrix.print(vals);
		System.out.println("Corresponding eigenvectors:");
		Matrix.print(eigen.vectors);
		System.out.println("Two principal components:");
		Matrix.print(dat.buildPrincipalComponents(2, eigen));
		System.out.println("Principal component transformation:");
		Matrix.print(Data.principalComponentAnalysis(data, 2));
	}
	
	/**
	 * Performs principal component analysis with a specified number of principal components.
	 * @param input			input data; each double[] in input is an array of values of a single
	 * 						variable for each data point
	 * @param numComponents	number of components desired
	 * @return				the transformed data set
	 */
	static double[][] principalComponentAnalysis(double[][] input, int numComponents) {
		Data data = new Data(input);
		data.normalize();
		EigenSet eigen = data.getCovarianceEigenSet();
		double[][] featureVector = data.buildPrincipalComponents(numComponents, eigen);
		double[][] PC = Matrix.transpose(featureVector);
		double[][] inputTranspose = Matrix.transpose(input);
		return Matrix.transpose(Matrix.multiply(PC, inputTranspose));
	}
	
	/**
	 * Returns a list containing the principal components of this data set with the number of
	 * loadings specified.
	 * @param numComponents	the number of principal components desired
	 * @param eigen			EigenSet containing the eigenvalues and eigenvectors
	 * @return				the numComponents most significant eigenvectors
	 */
	double[][] buildPrincipalComponents(int numComponents, EigenSet eigen) {
		double[] vals = eigen.values;
		if(numComponents > vals.length) {
			throw new RuntimeException("Cannot produce more principal components than those provided.");
		}
		boolean[] chosen = new boolean[vals.length];
		double[][] vecs = eigen.vectors;
		double[][] PC = new double[numComponents][];
		for(int i = 0; i < PC.length; i++) {
			int max = 0;
			while(chosen[max]) {
				max++;
			}
			for(int j = 0; j < vals.length; j++) {
				if(Math.abs(vals[j]) > Math.abs(vals[max]) && !chosen[j]) {
					max = j;
				}
			}
			chosen[max] = true;
			PC[i] = vecs[max];
		}
		return PC;
	}
	
	/**
	 * Uses the QR algorithm to determine the eigenvalues and eigenvectors of the covariance 
	 * matrix for this data set. Iteration continues until no eigenvalue changes by more than 
	 * 1/10000.
	 * @return	an EigenSet containing the eigenvalues and eigenvectors of the covariance matrix
	 */
	EigenSet getCovarianceEigenSet() {
		double[][] data = covarianceMatrix();
		double[][] Q = new double[data.length][data.length];
		for(int i = 0; i < Q.length; i++) {
			Q[i][i] = 1; //Q starts as an identity matrix
		}
		boolean done = false;
		while(!done) {
			double[][][] fact = Matrix.QRFactorize(data);
			double[][] newMat = Matrix.multiply(fact[1], fact[0]); //[A_k+1] := [R_k][Q_k]
			Q = Matrix.multiply(fact[0], Q);
			//Stop the loop if no eigenvalue changes by more than 1/100000
			for(int i = 0; i < data.length; i++) {
				if(Math.abs(newMat[i][i] - data[i][i]) > 0.00001) {
					data = newMat;
					break;
				} else if(i == data.length - 1) { //End of data table
					done = true;
				}
			}
		}
		EigenSet ret = new EigenSet();
		ret.values = Matrix.extractDiagonalEntries(data); //Eigenvalues lie on diagonal
		ret.vectors = Q; //Columns of Q converge to the eigenvectors
		return ret;
	}
	
	/**
	 * Constructs the covariance matrix for this data set.
	 * @return	the covariance matrix of this data set
	 */
	double[][] covarianceMatrix() {
		double[][] out = new double[matrix.length][matrix.length];
		for(int i = 0; i < out.length; i++) {
			for(int j = 0; j < out.length; j++) {
				double[] dataA = matrix[i];
				double[] dataB = matrix[j];
				out[i][j] = covariance(dataA, dataB);
			}
		}
		return out;
	}
	
	/**
	 * Returns the covariance of two data vectors.
	 * @param a	double[] of data
	 * @param b	double[] of data
	 * @return	the covariance of a and b, cov(a,b)
	 */
	static double covariance(double[] a, double[] b) {
		if(a.length != b.length) {
			throw new MatrixException("Cannot take covariance of different dimension vectors.");
		}
		double divisor = a.length - 1;
		double sum = 0;
		double aMean = mean(a);
		double bMean = mean(b);
		for(int i = 0; i < a.length; i++) {
			sum += (a[i] - aMean) * (b[i] - bMean);
		}
		return sum/divisor;
	}
	
	
	/**
	 * Normalizes the data matrix so that each column is centered at 0.
	 */
	void normalize() {
		for(int i = 0; i < matrix.length; i++) {
			double mean = mean(matrix[i]);
			for(int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] -= mean;
			}
		}
	}
	
	/**
	 * Calculates the mean of an array of doubles.
	 * @param entries	input array of doubles
	 */
	static double mean(double[] entries) {
		double out = 0;
		for(double d: entries) {
			out += d/entries.length;
		}
		return out;
	}
}

/**
 * Data holder class that contains a set of eigenvalues and their corresponding eigenvectors.
 * @author	Kushal Ranjan
 * @version 051413
 */
class EigenSet {
	double[] values;
	double[][] vectors;
}
