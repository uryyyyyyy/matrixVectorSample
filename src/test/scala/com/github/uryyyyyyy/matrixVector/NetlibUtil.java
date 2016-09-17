package com.github.uryyyyyyy.matrixVector;

import com.github.fommil.netlib.BLAS;

class NetlibUtil {

  static double[] calcWIthJavaPrimitive(double[][] matrix_A, double[] vector_B){

    BLAS blas = BLAS.getInstance();
    int m = matrix_A.length;
    int n = vector_B.length;
    double[] matrix = new double[m * n];
    double[] result = new double[m];

    for(int i = 0; i < m; i++){
      double[] vector = matrix_A[i];
      int temp = i;
      for(int j = 0; j < n; j++){
        matrix[temp] = vector[j];
        temp = temp + m;
      }
    }

    blas.dgemv("N", m, n, 1.0, matrix, m, vector_B, 1, 0.0, result, 1);
    return result;
  }
}
