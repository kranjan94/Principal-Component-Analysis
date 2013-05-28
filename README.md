Principal Component Analysis
============================

Project for exploring PCA through a simple Java implementation. Data is stored as two-dimensional double arrays.  

Data objects store two-dimensional arrays of doubles. Each data object can normalize itself around the mean of each variable, calculate its covariance matrix, and determine its eigenvalues and eigenvectors.  
Determination of eigenvalues and eigenvectors is performed by the QR algorithm on the covariance matrix of the data. The QR decomposition of a matrix A is A = QR, where Q is an orthonormal matrix and R is an upper-triangular matrix. The QR algorithm performs the decomposition A = QR, defines A' as A' = RQ, and repeats the process on A' as desired. The values on the diagonal of A' then converge to the eigenvalues of A. In the case of symmetric matrices, the columns of the product of the Q matrices form a set of orthonormal eigenvectors of A corresponding to those eigenvalues. Covariance matrices are symmetric, so the QR algorithm is ideal for this analysis.  
In this implementation, the QR algorithm iterates until none of the eigenvalues change by more than 1/100000 between iterations.

####Running
To run, compile and execute:  

    java PCA <filename> <num components>

\<filename\> is the name of the file in which the data is stored. \<num components\> is the number of principal components desired for the analysis.  
The data file should be in the same directory as the PCA.class file, or else the <filename> parameter should navigate to the true location of the file. All numbers in the file should be tab-delimited. The first line should be two ints: the first is the number R of rows in the file and the second is the number C of columns. After that, there should be R rows of C numbers.  
For instance, if 50 data points with 8 dimensions each were stored in a file called data.txt, the file would look like:  

    50  8
    <data point 1...>
    <data point 2...>
    ...
    <data point 50...>
  
and the command to perform PCA with 4 principal components would be:  

    java PCA data.txt 4

Output format will be changing as development continues.
