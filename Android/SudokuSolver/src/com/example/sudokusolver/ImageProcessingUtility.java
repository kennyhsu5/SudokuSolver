package com.example.sudokusolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import Jama.Matrix;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

//Functions used to load the neural network and process image
public final class ImageProcessingUtility {
	//Matrix for pixel values
	static final double[] white = {255.0, 255.0, 255.0, 255.0};
	static final double[] black = {0.0, 0.0, 0.0, 255.0};
	
	//Save mats possibly containing a digit to an image
	public static void save(ArrayList<Mat> m) {
		//Open folder to save images
		File storage = new File(Environment.getExternalStoragePublicDirectory(
		           Environment.DIRECTORY_PICTURES), "SudokuSolverUnLabeled");
		
		//Check to make sure folder exist
		if (!storage.exists()){
			if (!storage.mkdirs()){
				Log.d("sudokusolver", "failed to create directory");
				return;
			}
		}
		
		int size = m.size();
		for (int i = 0; i < size; i++) {
			Mat curr = m.get(i);
			//Generate title given current time and index
	        String time = new SimpleDateFormat("MMddHHmm").format(new Date());
			File outFile = new File(storage.getPath() + File.separator + time + Integer.toString(i) + ".jpg");
			//Write to file
			if (!Highgui.imwrite(outFile.toString(), curr)) {
				Log.d("saveFile", "Saving " + outFile.toString() + ": failed");
			} else {
				Log.d("saveFile", "Saving " + outFile.toString() + ": success");
			}
		}
	}
	
	//Loads the neural network from file
	public static NeuralNet loadNN(Context context, String fName) {
		BufferedReader reader = null;
		NeuralNet tempNet = null;
		try {
			int id = context.getResources().getIdentifier(fName, "raw", context.getPackageName());
			//Opens file
			InputStream file = context.getResources().openRawResource(id);
		    reader = new BufferedReader(new InputStreamReader(file));
		    
		    //Reads the first line to get architecture of neural network
		    String line = reader.readLine();
		    String[] temp = line.split(" ");
		    int inputLayer = Integer.parseInt(temp[0]);
		    int hiddenLayer = Integer.parseInt(temp[1]);
		    int outputLayer = Integer.parseInt(temp[2]);

		    //Initialize the weights matrix
		    Matrix theta1 = new Matrix(hiddenLayer, inputLayer + 1);
		    Matrix theta2 = new Matrix(outputLayer, hiddenLayer + 1);

		    Matrix[] m = {theta1, theta2};

		    //Read through each line in file, sets the corresponding value in weight matrix
		    int count = 0;
		    int mNum = 0;
		    while ((line = reader.readLine()) != null) {
				if (line.equals("")) {
				    mNum++;
				    count = 0;
				} else {
				    Matrix mat = m[mNum];
				    temp = line.split(" ");
				    int len = temp.length;
				    for (int c = 0; c < len; c++) {
				    	//Damn you parse double.... slowing everything down...... 
						mat.set(count, c, Double.parseDouble(temp[c]));
				    }
				    count++;
				}
		    }
		    reader.close();

		    tempNet = new NeuralNet(theta1, theta2);

		} catch(IOException e) {
		    e.printStackTrace();
		}
		return tempNet;
	}

	//Queries the database and loads weights of neural network
	public static NeuralNet load2(Context context) {
		//Instantiate database and neural net object
		SQLiteHelper db = new SQLiteHelper(context);
		NeuralNet tempNet = null;
		
		//Determine shape of neural network
		Cursor shapes = db.getShapes();
		shapes.moveToFirst();
		String[] temp = shapes.getString(
				shapes.getColumnIndex(SQLiteHelper.DIMENSIONS)).split(" ");
	    int inputLayer = Integer.parseInt(temp[0]);
	    int hiddenLayer = Integer.parseInt(temp[1]);
	    int outputLayer = Integer.parseInt(temp[2]);

	    //Create matrix of appropriate size for the two sets of weights
	    Matrix theta1 = new Matrix(hiddenLayer, inputLayer + 1);
	    Matrix theta2 = new Matrix(outputLayer, hiddenLayer + 1);
	    
	    //Set up weights between input and hidden layer
	    Cursor t1 = db.getTheta1();
	    t1.moveToFirst();
	    int index = t1.getColumnIndex(SQLiteHelper.WEIGHTS_COLUMN);

	    for (int r = 0; r < theta1.getRowDimension(); r++) {
	    	for (int c = 0; c < theta1.getColumnDimension(); c++) {
	    		theta1.set(r, c, t1.getDouble(index));
	    		t1.moveToNext();
	    	}
	    }

	    //Set up weights between hidden and output layer
	    Cursor t2 = db.getTheta2();
	    t2.moveToFirst();
	    index = t2.getColumnIndex(SQLiteHelper.WEIGHTS_COLUMN);
	    
	    for (int r = 0; r < theta2.getRowDimension(); r++) {
	    	for (int c = 0; c < theta2.getColumnDimension(); c++) {
	    		theta2.set(r, c, t2.getDouble(index));
	    		t2.moveToNext();
	    	}
	    }
	    
	    //Create Neural Net using the weights
	    tempNet = new NeuralNet(theta1, theta2);
		return tempNet;
	}
	
	//Process the image to extract sudoku digits from image
	//Returns a matrix of pixels of possible digits.
	public static PossibleDigits process(Bitmap input) {
		//Height and width of input image
		int height;
		int width;
		
		//Convert bitmap to opencv mat for processing
		//Original version is used to extract board
		Mat original = new Mat();
		Utils.bitmapToMat(input, original);
		
		//Get height and width of mat
		Size s = original.size();
		height = (int) s.height;
		width = (int) s.width;
		
		//Make a copy to extract digit from
		//Filters out noise from lcd screens
		Mat copy = copyFilter(original);
		
		//Convert original image from RGB to gray
		Imgproc.cvtColor(original, original, Imgproc.COLOR_RGB2GRAY);
		
		//Adaptive threshold to create binary image
		//Uses adaptive threshold to reduce effect of lighting
		Imgproc.adaptiveThreshold(original, original, 255,
				Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 7, 4);

		//Find outline of sudokuboard in picture by finding biggest blob
		//Create mask for use in floodfill
		Mat mask = new Mat(new Size(width + 2, height + 2), original.type());
		Imgproc.copyMakeBorder(original, mask, 1, 1, 1, 1, Imgproc.BORDER_REPLICATE);
		//Find starting point of sudoku board outline
		Point maxP = findBlob(original, mask);	
		Imgproc.copyMakeBorder(original, mask, 1, 1, 1, 1, Imgproc.BORDER_REPLICATE);
		//Fill the sudoku board edge/outline with 254 pixel value
		Imgproc.floodFill(original, mask, maxP, new Scalar(254));
		//Loop through image again to apply threshold,
		//white out anything that is not sudoku board grid

		double[] color = {255};
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				//If pixel == 254, it is sudoku grid
				if (original.get(y, x)[0] == 254) {
					color[0] = 0;
				} else {
					color[0] = 255;
				}
				//White out the background
				original.put(y, x, color);
			}
		}

		//erode image to make sudoku grid wide.
		//Allows the corner to be detected more easily
		Imgproc.erode(original, original,
				Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));

		//Find four corners of the sudoku board
		int[][] corners = findCorners(original);

		//transform sudokuboard from surface in image(laptop screen, newspaper, etc)
		//to 2d plane. Warp using copy of image to get image of digits
		Mat board = warp(copy, corners[0], corners[1], corners[2], corners[3]);
		
		//Set up matrix for input to neural network to recognize digit
		PossibleDigits nnInput = findDigits(board);

		return nnInput;
	}
	
	public static SudokuBoard constructBoard(PossibleDigits pd, NeuralNet nn1, NeuralNet nn2) {
		ArrayList<int[]> locations = pd.locations;
		Matrix inputs = pd.inputs;
		SudokuBoard sudoku = new SudokuBoard();
		
		double[][] results1 = nn1.predict(inputs);
		double[][] results2 = nn2.predict(inputs);

		int len = results1.length;
		for (int i = 0; i < len; i++) {
			double[] yVec1 = results1[i];
			double[] yVec2 = results2[i];
			int maxIndex = -1;
			double maxProb = Double.MIN_VALUE; 
			for (int t = 0; t < yVec1.length; t++) {
				double val1 = yVec1[t];
				double val2 = yVec2[t];
				
				double avg = (val1 + val2) / 2.0;
				if (avg > maxProb) {
					maxIndex = t;
					maxProb = avg;
				}
			}
			
			if (maxProb > 0.50 && maxIndex > 0) {
				int[] loc = locations.get(i);
				sudoku.setTile(loc[0], loc[1], maxIndex);
			}
		}
		return sudoku;
	}
	
	private static PossibleDigits findDigits(Mat board) {
		//Initialize arraylists to hold mats and locations
		ArrayList<Mat> squares = new ArrayList<Mat>();
		ArrayList<int[]> locations = new ArrayList<int[]>();
		
		//Get size of input image
		Size tempSize = board.size();
		int height = (int) tempSize.height;
		int width = (int) tempSize.width;
		
		//Divide the length of sudokueboard by 9 to find length of each square
		int step = height / 9;
		double[] newVals = new double[]{0.0, 0.0, 0.0, 255.0};
		//Input size of neural network
		Size newSize = new Size(20, 20);
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				//Offset the start of each sqaure by two, to avoid
				//capturing outline of sudoku
				int rowStart = r * step + 2;
				int rowEnd = rowStart + step;
				int columnStart = c * step + 2;
				int columnEnd = columnStart + step;
				
				//Make sure coordinates are within the image
				if (rowEnd >= height) {
					rowEnd = height;
				}
				if (columnEnd >= width) {
					columnEnd = width;
				}
				
				//Extract the region corresponding to the square
				Mat p = board.submat(rowStart, rowEnd, columnStart, columnEnd);
				Mat resizedImage = new Mat();
				//resize image to be 20 X 20
				Imgproc.resize(p, resizedImage, newSize);
				
				//Get size of new resized image
				Size temp = resizedImage.size();
				int rowLimit = (int) temp.height;
				int columnLimit = (int) temp.width;
				
				//Go through each pixel in the square to determine if it is blank
				int background = 0;
				int total = 0;
 				for (int row = 0; row < rowLimit; row++) {
 					for (int col = 0; col < columnLimit; col++) {
 						double[] color =  resizedImage.get(row, col);
 						//Invert the image color
 						double newVal = (color[0] - 255) * -1;
 						newVals[0] = newVal;
 						newVals[1] = newVal;
 						newVals[2] = newVal;
 						
 						if (row > 1 && row < rowLimit - 2
 							&& col > 1 && col < columnLimit - 2) {
 							//Tally up the number of background pixel
 							if (newVal == 0) {
 	 							background++;
 							}
 	 						total++;
 						}
 						resizedImage.put(row, col, newVals);
 					}
 				}
 				
 				//If less than 80 percent of image is background,
 				//it might have a digit, so add to list
 				if (((double) background / (double) total) < 0.80) {
 					squares.add(resizedImage);
 					locations.add(new int[]{c, (r - 8) * -1});
 				}
			}
		}
		
		//used to gather data.
		//save(squares);
				
		//Create matrix for input to neural network
		Matrix inputs = new Matrix(squares.size(), 401);
		//Convert each mat into a row in the matrix
		int len = squares.size();
		for (int i = 0; i < len; i++) {
			//Set first column to 1 for bias input
			inputs.set(i, 0, 1);
			Mat m = squares.get(i);
			int index = 1;
			for (int r = 0; r < 20; r++) {
				for (int c = 0; c < 20; c++) {
					inputs.set(i, index, m.get(r, c)[0]);
					index++;
				}
			}
		}
		
		return new PossibleDigits(locations, inputs);
	}
	
	//Transform region from image within the four points into a square in 2d-plane
	private static Mat warp(Mat input, int[] bl, int[] br, int[] tl, int[] tr) {
		//Warp perspective to flatten sudoku board
		Mat src = new Mat();
		Mat dst = new Mat();

		//Find height and width of region
		int height = Math.max(bl[1] - tl[1], br[1] - tr[1]);
		int width = Math.max(br[0] - bl[0], tr[0] - tl[0]);
		//find dimension of square output
		height = Math.max(height, width);
		width = height;
		
		//Set up list containing points of input region
		//and list containing corresponding points of mapped output image
		ArrayList<Point> srcPoints = new ArrayList<Point>(4);
		ArrayList<Point> dstPoints = new ArrayList<Point>(4);
		
		srcPoints.add(new Point(tl[0], tl[1]));
		srcPoints.add(new Point(tr[0], tr[1]));
		srcPoints.add(new Point(br[0], br[1]));
		srcPoints.add(new Point(bl[0], bl[1]));
		
		dstPoints.add(new Point(0, 0));
		dstPoints.add(new Point(width - 1, 0));
		dstPoints.add(new Point(width - 1, height - 1));
		dstPoints.add(new Point(0, height - 1));
		
		src = Converters.vector_Point_to_Mat(srcPoints, CvType.CV_32F);
		dst = Converters.vector_Point_to_Mat(dstPoints, CvType.CV_32F);

		//Set up transformation matrix for use by opencv
		Mat transformation = Imgproc.getPerspectiveTransform(src, dst);
		
		//Use opencv's warpPerspective to transform image
		Mat result = new Mat(new Size(width, height), input.type());		
		Imgproc.warpPerspective(input, result, transformation, new Size(width, height));
		return result;
	}
	
	//Find x,y coordinates of four corners of the sudoku board
	private static int[][] findCorners(Mat original) {
		//Find list of corner candidates
		ArrayList<int[]> topLeft = new ArrayList<int[]>();
		ArrayList<int[]> bottomLeft = new ArrayList<int[]>();
		ArrayList<int[]> topRight = new ArrayList<int[]>();
		ArrayList<int[]> bottomRight = new ArrayList<int[]>();
		
		//Get size of input image
		Size tempSize = original.size();
		int height = (int) tempSize.height;
		int width = (int) tempSize.width;
		
		//Ignore outer-most pixel, assumes sudoku will
		//not be cutoff/at edge of image
		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				if (original.get(y, x)[0] == 0) {
					//Find values of all surrounding pixels
					double above = original.get(y - 1, x)[0];
					double leftAbove = original.get(y - 1, x - 1)[0];
					double rightAbove = original.get(y - 1, x + 1)[0];
					double left = original.get(y, x - 1)[0];
					double right = original.get(y, x + 1)[0];
					double bottom = original.get(y + 1, x)[0];
					double leftBottom = original.get(y  + 1, x - 1)[0];
					double rightBottom = original.get(y + 1, x + 1)[0];
					
					//If surrounding pixels satisfy requirements/match pattern,
					//add to candidates list
					if (left == 0 && bottom == 0 && above == 255
						&& right == 255 && rightAbove == 255) {
						topRight.add(new int[]{x, y});
					}
					if (right == 0 && bottom == 0 && left == 255
						&& leftAbove == 255 && above == 255) {
						topLeft.add(new int[]{x, y});
					}
					if (above == 0 && left == 0 && rightBottom == 255
						&& bottom == 255 && right == 255) {
						bottomRight.add(new int[]{x, y});
					}
					if (right == 0 && above == 0
						&& leftBottom == 255 && left == 255
						&& bottom == 255) {
						bottomLeft.add(new int[]{x, y});
					}
				}
			}
		}
				
		//Find actual corners from list of candidates
		if (bottomLeft.size() != 0 && bottomRight.size() != 0
			&& topLeft.size() != 0 && topRight.size() != 0) {
			
			//Compute distance from point to corner of image
			int[] tl = findBestCorner(topLeft, 0, 0);
			int[] bl = findBestCorner(bottomLeft, 0, height);
			int[] tr = findBestCorner(topRight, width, 0);
			int[] br = findBestCorner(bottomRight, width, height);
			
			return new int[][]{bl, br, tl, tr};
		}
		return null;
	}
	
	//Find the four corners from list of candidate points
	private static int[] findBestCorner(ArrayList<int[]> input, int x1, int y1) {
		double min = Double.MAX_VALUE;
		int[] minPoint = new int[]{0, 0};
		int len = input.size();
		//For each point in list, compute distance from point to
		//point (x1, y1), the corresponding corner of image
		for (int i = 0; i < len; i++) {
			int x2 = input.get(i)[0];
			int y2 = input.get(i)[1];
			double distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
			//point with least distance to corner of image
			//is most likely the corner we want
			if (distance < min) {
				minPoint[0] = x2;
				minPoint[1] = y2;
				min = distance;
			}
		}
		return minPoint;
	}
	
	//Goes through the entire image to find biggest blob
	//Assumes that sudoku is biggest object in image
	private static Point findBlob(Mat input, Mat mask) {
		Size s = input.size();
		int mWidth = (int) s.width;
		int mHeight = (int) s.height;
				
		//Find blob with maximum size/area
		int max = Integer.MIN_VALUE;
		Point maxP = new Point();
		
		//Loops through each pixel in image
		Scalar color = new Scalar(0);
		for (int y = 0; y < mHeight; y++) {
			for (int x = 0; x < mWidth; x++) {
				//If pixel is black, use it to floodfill
				//if pixel is not black, it is probably background
				if (input.get(y, x)[0] == 0) {
					//Uses each pixel as start point for floodfill
					Point p = new Point(x, y);
					int val = Imgproc.floodFill(input, mask, p, color);
					
					//update maximum area and starting point
					if (val > max) {
						max = val;
						maxP = p;
					}
				}
			}
		}
		return maxP;
	}
	
	//Makes a copy of the original and use global threshold
	//to filters out noise/lines caused by lcd screens
	private static Mat copyFilter(Mat original) {
		//Make copy of original
		Mat copy = original.clone();
		
		//Get size of mat
		Size tempSize = copy.size();
		int h = (int) tempSize.height;
		int w = (int) tempSize.width;
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				double[] colors = copy.get(y, x);
				//if pixels higher than a threshold, set to white. else default to black 
				if (colors[0] > 100.0 && colors[1] > 100.0 && colors[2] > 100.0) {
					copy.put(y, x, white);
				} else {
					copy.put(y, x, black);
				}
			}
		}

		
		return copy;
	}
}