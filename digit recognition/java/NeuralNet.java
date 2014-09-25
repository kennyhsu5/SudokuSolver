import Jama.*;

/*Neural Network for use on Android*/
public class NeuralNet implements java.io.Serializable {
    private static final long serialVersionUID = 0L;
    //Weights for Neural Net
    Matrix theta1;
    Matrix theta2;
    Matrix hiddenBias;
    Matrix outputBias;

    //Constructor
    public NeuralNet(Matrix t1, Matrix t2, Matrix h, Matrix o) {
	theta1 = t1;
	theta2 = t2;
	hiddenBias = h;
	outputBias = o;
    }

    //Vectorized sigmoid function for output activation of each neuron
    public Matrix sigmoid(Matrix z) {
	int rSize = z.getRowDimension();
	int cSize = z.getColumnDimension();
	for (int r = 0; r < rSize; r++) {
	    for (int c = 0; c < cSize; c++) {
		double a = 1.0 / (1.0 + -1.0 * z.get(r, c));
		z.set(r, c, a);
	    }
	}
	return z;
    }
    public double[][] predict(Matrix X) {
	return sigmoid(sigmoid(X.times(theta1.transpose()).plus(hiddenBias)).times(theta2.transpose()).plus(outputBias)).getArray();
    }
}