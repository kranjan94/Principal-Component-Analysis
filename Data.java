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
		matrix = vals;
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
	double mean(double[] entries) {
		double out = 0;
		for(double d: entries) {
			out += d/entries.length;
		}
		return out;
	}
}
