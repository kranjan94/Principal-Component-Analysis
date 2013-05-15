Principal Component Analysis
============================

Project for exploring PCA through a simple Java implementation. Data is stored as two-dimensional double arrays.  

Data objects store two-dimensional arrays of doubles. Each data object can normalize itself around the mean of each variable, calculate its covariance matrix, and determine its eigenvalues and eigenvectors.  
Determination of eigenvalues and eigenvectors is performed by the QR algorithm on the covariance matrix of the data. The QR decomposition of a matrix A is A = QR, where Q is an orthonormal matrix and R is an upper-triangular matrix. The QR algorithm performs the decomposition A = QR, defines A' as A' = RQ, and repeats the process on A' as desired. The values on the diagonal of A' then converge to the eigenvalues of A. In the case of symmetric matrices, the columns of the product of the Q matrices form a set of orthonormal eigenvectors of A corresponding to those eigenvalues. Covariance matrices are symmetric, so the QR algorithm is ideal for this analysis.  
In this implementation, the QR algorithm iterates until none of the eigenvalues change by more than 1/100000 between iterations.
