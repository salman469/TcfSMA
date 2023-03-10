package com.tcf.sma.Survey.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    public static boolean isPreviewStarted;
    Context app_context;
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        app_context = context;
        //System.out.println(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH));

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            WebviewActivity.isPreviewStarted = true;
        } catch (IOException e) {
            Log.d("Error", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        mCamera.stopPreview();
        mCamera.release();
        // 	mCamera.release();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {

            mCamera.setPreviewDisplay(mHolder);

            final Parameters parameters = mCamera.getParameters();

            if (app_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
                CameraActivity.isFlashPresent = true;

            if (CameraActivity.isFlashPresent && CameraActivity.turnOnFlash)
                parameters.setFlashMode(Parameters.FLASH_MODE_ON);
            parameters.set("jpeg-quality", 100);

            mCamera.setParameters(parameters);
            //mCamera.set
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d("Error", "Error starting camera preview: " + e.getMessage());
        }
    }
}