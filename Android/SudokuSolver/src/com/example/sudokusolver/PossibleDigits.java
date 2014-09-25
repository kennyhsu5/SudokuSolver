package com.example.sudokusolver;

import java.util.ArrayList;

import Jama.Matrix;

	
//Wrapper class to hold location and input vector of pixels of possible digits
public class PossibleDigits {
	//Holds x,y coordinates of squares with possible digit
	public ArrayList<int[]> locations;
	
	//hold input vector to neural network
	//Rows = an image entry
	//Columns = parameters/pixels
	public Matrix inputs;
	
	//Constructor
	public PossibleDigits(ArrayList<int[]> loc, Matrix x) {
		locations = loc;
		inputs = x;
	}
}