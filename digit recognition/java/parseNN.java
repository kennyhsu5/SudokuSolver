import Jama.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

/*Read weights from file and create a serialized neural network
  for use on Android. */
public class parseNN {
    public static void main(String[] args) {
	BufferedReader reader = null;
	try {
	    File f = new File("100.net");
	    reader = new BufferedReader(new FileReader(f));
	    
	    String line = reader.readLine();
	    String[] temp = line.split(" ");
	    int inputLayer = Integer.parseInt(temp[0]);
	    int hiddenLayer = Integer.parseInt(temp[1]);
	    int outputLayer = Integer.parseInt(temp[2]);

	    Matrix theta1 = new Matrix(hiddenLayer, inputLayer);
	    Matrix theta2 = new Matrix(outputLayer, hiddenLayer);
	    Matrix hiddenBias = new Matrix(1, hiddenLayer);
	    Matrix outputBias = new Matrix(1, outputLayer);

	    Matrix[] m = {theta1, theta2, hiddenBias, outputBias};

	    int count = 0;
	    int mNum = 0;
	    while ((line = reader.readLine()) != null) {
		if (line.equals("")) {
		    mNum++;
		    count = 0;
		} else {
		    Matrix mat = m[mNum];
		    temp = line.split(" ");
		    for (int c = 0; c < temp.length; c++) {
			mat.set(count, c, Double.parseDouble(temp[c]));
		    }
		    count++;
		}
	    }
	    reader.close();

	    NeuralNet nn = new NeuralNet(theta1, theta2, hiddenBias, outputBias);
	    FileOutputStream fileOut = new FileOutputStream("NeuralNet100.ser");
	    ObjectOutputStream out = new ObjectOutputStream(fileOut);
	    out.writeObject(nn);
	    out.close();
	    fileOut.close();

	} catch(IOException e) {
	    e.printStackTrace();
	}
    }
}