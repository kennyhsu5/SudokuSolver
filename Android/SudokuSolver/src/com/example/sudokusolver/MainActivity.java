package com.example.sudokusolver;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements CvCameraViewListener2, PictureCallback{
	//View used to show camera preview and control camera
	private CameraView mCameraView;
	private boolean preview;
	//Button used to capture data
	private Button button;
	
	//Neural networks used for digit recognition
	//Combines the two using boosting to get better result
	private NeuralNet nn1;
	private NeuralNet nn2;
	
	//Sudoku display index
	//Change later.... 
	private String[] sudokuIndex
		= new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
					   "ten", "eleven", "twelve", "thirteen", "fourteen", "fifthteen", "sixteen", "seventeen", "eightteen",
					   "nineteen", "twenty", "twentyone", "twentytwo", "twentythree", "twentyfour", "twentyfive", "twentysix", "twentyseven",
					   "twentyeight", "twentynine", "thirty", "thirtyone", "thirtytwo", "thirtythree", "thirtyfour", "thirtyfive", "thirtysix",
					   "thirtyseven", "thirtyeight", "thirtynine", "fourty", "fourtyone", "fourtytwo", "fourtythree", "fourtyfour", "fourtyfive",
					   "fourtysix", "fourtyseven", "fourtyeight", "fourtynine", "fifty", "fiftyone", "fiftytwo", "fiftythree", "fiftyfour",
					   "fiftyfive", "fiftysix", "fiftyseven", "fiftyeight", "fiftynine", "sixty", "sixtyone", "sixtytwo", "sixtythree",
					   "sixtyfour", "sixtyfive", "sixtysix", "sixtyseven", "sixtyeight", "sixtynine", "seventy", "seventyone", "seventytwo",
					   "seventythree", "seventyfour", "seventyfive", "seventysix", "seventyseven", "seventyeight", "seventynine", "eighty", "eightyone"};
	
	//Load opencv and enable cameraview
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    mCameraView.enableView();
                    preview = true;
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//Load the neural network in the background thread
		loadNeuralNetwork();
		
		//Set up camera preview and camera		
		mCameraView = (CameraView) findViewById(R.id.cameraView);
		mCameraView.setVisibility(SurfaceView.VISIBLE);
		mCameraView.setCvCameraViewListener(this);
		
		//Set up button for capture
		button = (Button) findViewById(R.id.captureButton);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Camera cam = mCameraView.getCamera();
				focusCamera(cam);
			}
		});
		
	}
	
	//Loads the Neural Network in a background thread on startup
	private void loadNeuralNetwork() {
		Runnable load = new Runnable() {
			public void run() {
				nn1 = ImageProcessingUtility.loadNN(getApplicationContext(), "nnregdataset");
			}
		};
		Runnable load2 = new Runnable() {
			public void run() {
				nn2 = ImageProcessingUtility.loadNN(getApplicationContext(), "nnexpandeddataset");
			}
		};
		Thread thread1 = new Thread(load);
		Thread thread2 = new Thread(load2);
		
		thread1.start();
		thread2.start();
	}
	
	//Focus camera before capture
	public void focusCamera(Camera cam) {
		cam.autoFocus(new AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera cam) {
				if (success) {
					takePicture(cam);
				} else {
					focusCamera(cam);
				}
			}
		});
	}
	
	//Takes picture of sudoku
	public void takePicture(Camera cam) {
		cam.setPreviewCallback(null);
		cam.takePicture(null, null, this);
	}
	
	@Override
	public void onPictureTaken(byte[] data, Camera cam) {
        if (mCameraView != null) {
            mCameraView.disableView();
        }
		preview = false;
		//setContentView(R.layout.sudoku);
		setContentView(R.layout.displaypic);

		Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
		ImageProcessTask task = new ImageProcessTask(bm);
		task.execute(new String[] {});
	}
	
	//Asynctask used to process image in background thread
	private class ImageProcessTask extends AsyncTask<String, Void, String> {
		//Captured image
		Bitmap image;
		//Sudokuboard from image
		SudokuBoard sb;
		
		//Constructor for task
		public ImageProcessTask(Bitmap bm) {
			super();
			image = bm;
		}
		@Override
        protected String doInBackground(String... params) {
			long startTime = System.currentTimeMillis();
			//Process the image to extract matrix of pixels possibly belonging to a digit
			//and arraylist of their locations in sudoku board
			PossibleDigits pd = ImageProcessingUtility.process(image);
			
			if (pd.locations.size() < 10) {
				return "failure";
			}
			return "saved";
			
			//Make sure the neural network has been loaded.	
			while (nn1 == null || nn2 == null) {
				//nop
				assert true;
			}
			
			//Construct the sudoku board
			sb = ImageProcessingUtility.constructBoard(pd, nn1, nn2);
		    
			if (SudokuSolver.solve(sb, sb.getNumSolved())) {
				return "success";
			}
			return "failure";
		}
		
	    @Override
	    protected void onPreExecute() {
	    }
		
	    @Override
	    protected void onPostExecute(String result) {
		/* Used to gather data
	    	if (result == "saved") {
	    		ImageView img = (ImageView) findViewById(R.id.temp);
	    		img.setImageBitmap(image);
	    	}
		*/
	    	if (result == "success") {
	    		//display board
		    	int[][] board = sb.getGameBoard();
		    	for (int r = 0; r < 9; r++) {
		    		for (int c = 0; c < 9; c++) {
			    		int ans = Utility.getAnswer(board[r][c]);
			    		if (ans != 0) {
			    			int i = (r * 9) + c;
			    			TextView v
			    				= (TextView) findViewById(getResources().getIdentifier(sudokuIndex[i] , "id", getPackageName()));
			    			v.setText(Integer.toString(ans));
			    		}
		    		}
		    	}
				System.out.println("done");
	    	} else {
	    		//TODO: display "try again" message
		    	for (int r = 0; r < 9; r++) {
		    		for (int c = 0; c < 9; c++) {
			    		int i = (r * 9) + c;
			    		TextView v
			    			= (TextView) findViewById(getResources().getIdentifier(sudokuIndex[i] , "id", getPackageName()));
			    		v.setText(Integer.toString(0));
		    		}
		    	}
	    	}

	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	        if (!preview) {
	        	recreate();
	            return true;
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
    public void onPause()
    {
        super.onPause();
        if (mCameraView != null) {
            mCameraView.disableView();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mCameraView != null) {
            mCameraView.disableView();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
    }

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		 return inputFrame.rgba();
	}
}
