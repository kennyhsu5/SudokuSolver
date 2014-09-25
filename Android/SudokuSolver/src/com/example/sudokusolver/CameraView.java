package com.example.sudokusolver;

import java.util.List;

import org.opencv.android.JavaCameraView;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.AttributeSet;

public class CameraView extends JavaCameraView {
	public CameraView(Context c, AttributeSet a) {
		super(c, a);
	}
	
	public Camera getCamera() {
		Parameters param = mCamera.getParameters();
		
		//Setup resolution of the captured image
		List<Size> sizes = param.getSupportedPictureSizes();
		Size minSize = sizes.get(sizes.size() - 1);
		if (minSize.height <= 240 && minSize.width <= 320) {
			param.setPictureSize(320, 240);
		} else {
			param.setPictureSize(minSize.width, minSize.height);
		}
		mCamera.setParameters(param);
		return mCamera;
	}
}