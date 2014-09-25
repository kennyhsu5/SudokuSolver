package com.example.sudokusolver;

import Jama.Matrix;

/*Neural Network for use on Android*/
public class NeuralNet implements java.io.Serializable {
    //Weights for Neural Net
    Matrix theta1;
    Matrix theta2;

    //Constructor
    public NeuralNet(Matrix t1, Matrix t2) {
		theta1 = t1;
		theta2 = t2;
    }

    //Vectorized sigmoid function for output activation of each neuron
    public Matrix sigmoid(Matrix z) {
		int rSize = z.getRowDimension();
		int cSize = z.getColumnDimension();
		for (int r = 0; r < rSize; r++) {
		    for (int c = 0; c < cSize; c++) {
		    	double a = 1.0 / (1.0 + Math.exp(-1.0 * z.get(r, c)));
		    	z.set(r, c, a);
		    }
		}
		return z;
    }
    public Matrix appendOne(Matrix input) {
    	int row = input.getRowDimension();
    	int column = input.getColumnDimension();
    	Matrix result = new Matrix(row, column + 1);
    	result.setMatrix(0, row - 1, 0, 0, new Matrix(row, 1, 1.0));
    	result.setMatrix(0, row - 1, 1, column - 1, input);
    	return result;
    }
    public double[][] predict(Matrix X) {
    	return sigmoid(appendOne(sigmoid(X.times(theta1.transpose()))).times(theta2.transpose())).getArray();
    }
}