import java.io.*;
/**
 * Performs principal component analysis on a set of data and returns the resulting data set. The
 * QR algorithm is used to find the eigenvalues and orthonormal eigenvectors of the covariance
 * matrix of the data set. The eigenvectors corresponding to the largest eigenvalues are the
 * principal components. The data file should be in the same directory as the PCA.class file.
 * All numbers should be tab-delimited. The first line of the data should be two numbers: the 
 * number of rows R followed by the number of columns C. After that, there should be R lines of 
 * C tab-delimited values. The columns would most likely represent the dimensions of measure; the
 * rows would each represent a single data point.
 * @author	Kushal Ranjan
 * @version	051513
 */
public class PCA {
	
	/**
	 * Performs PCA on a set of data and prints the resulting transformed data set.
	 * @param args	args[0] is the name of the file containing the data. args[1] is an integer
	 * 				giving the number of desired principal components.
	 */
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Invalid number of arguments. Arguments should be <filename> <# components>.");
			System.exit(0);
		}
		double[][] data = null;
		try {
			 data = parseData(args[0]);
		} catch(ArrayIndexOutOfBoundsException e) {
			System.err.println("Malformed data table.");
		} catch(IOException e) {
			System.err.println("Malformed data file.");
		}
		int numComps = Integer.parseInt(args[1]);
		
//		Uses previous method
//		double[][] results = Data.principalComponentAnalysis(data, numComps);
//		System.out.println(numComps + " principal components:");
//		Matrix.print(results);
//		saveResults(results, args[0]);
		
		double[][] scores = Data.PCANIPALS(data, numComps);
//		System.out.println("Scores:");
//		Matrix.print(scores);
		saveResults(scores, args[0]);
		
		System.out.println(Matrix.numMults + " multiplications performed.");
	}
	
	/**
	 * Uses the file given by filename to construct a table for use by the application.
	 * All numbers should be tab-delimited.
	 * The first line of the data should be two numbers: the number of rows R followed by the
	 * number of columns C. After that, there should be R lines of C tab-delimited values.
	 * @param filename	the name of the file containing the data
	 * @return			a double[][] containing the data in filename
	 * @throws IOException	if an error occurs while reading the file
	 */
	private static double[][] parseData(String filename) throws IOException{
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(new File(filename)));
		} catch(FileNotFoundException e) {
			System.err.println("File " + filename + " not found.");
		}
		String firstLine = in.readLine();
		String[] dims = firstLine.split(","); // <# points> <#dimensions>
		double[][] data = new double[Integer.parseInt(dims[1])][Integer.parseInt(dims[0])];
		for(int j = 0; j < data[0].length; j++) {
			String text = in.readLine();
			String[] vals = text.split(",");
			for(int i = 0; i < data.length; i++)  {
				data[i][j] = Double.parseDouble(vals[i]);
			}
		}
		try {
			in.close();
		} catch(IOException e) {
			System.err.println(e);
		}
		return data;
	}
	
	/**
	 * Saves the results of PCA to a file. The filename has "_processed" appended to it before
	 * the extension.
	 * @param results	double[][] of PCA results
	 * @param filename	original filename of data
	 */
	private static void saveResults(double[][] results, String filename) {
		String[] filenameComps = filename.split("\\.");
		String newFilename = filenameComps[0] + "_processed";
		if(filenameComps.length == 2) {
			newFilename += "." + filenameComps[1]; //Add filename extension
		}
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(new File(newFilename)));
		} catch(IOException e) {
			System.err.println("Error trying to write new file.");
		}
		for(int i = 0; i < results[0].length; i++) {
			for(int j = 0; j < results.length; j++) {
				try {
					out.write("" + results[j][i]);
					if(j != results.length - 1) {
						out.write(",");
					} else {
						out.write("\n");
					}
				} catch(IOException e) {
					System.err.println("Error trying to write new file.");
				}
			}
		}
	}
}
