package com.tcf.sma.Activities;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.tcf.sma.Models.AppModel;
import com.tcf.sma.R;

import java.io.IOException;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera camera;
    Camera.PictureCallback jpegCallback;
    Button captureimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (SurveyAppModel.getInstance().photoFlag == 1) {
//            setContentView(R.layout.camera_activity);
//        } else {
        setContentView(R.layout.camera_activity_forms);
//        }
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        captureimg.setEnabled(true);
                    }
                });
            }
        });
        captureimg = (Button) findViewById(R.id.captureimg);
        captureimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, jpegCallback);
//                finish();
            }
        });

        getWindow().setFormat(PixelFormat.UNKNOWN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {

                if (AppModel.getInstance().photoFlag == 1) {
                    AppModel.getInstance().img1 = data;

                } else if (AppModel.getInstance().photoFlag == 2) {
                    AppModel.getInstance().img2 = data;

                } else if (AppModel.getInstance().photoFlag == 3) {
                    AppModel.getInstance().img3 = data;
                }
                finish();
            }
        };

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();   // opening
        camera.setDisplayOrientation(90);   // setting  preview orientation

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            //parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA); //applying effect on camera
//            parameters.setExposureCompensation(parameters.getMaxExposureCompensation());
//            if (parameters.isAutoExposureLockSupported()) {
//                parameters.setAutoExposureLock(false);
//            }
//            if (parameters.isAutoWhiteBalanceLockSupported()) {
//                parameters.setAutoWhiteBalanceLock(false);
//            }
            final int[] previewFpsRange = new int[2];
            parameters.getPreviewFpsRange(previewFpsRange);
            if (previewFpsRange[0] == previewFpsRange[1]) {
                final List<int[]> supportedFpsRanges = parameters.getSupportedPreviewFpsRange();
                for (int[] range : supportedFpsRanges) {
                    if (range[0] != range[1]) {
                        parameters.setPreviewFpsRange(range[0], range[1]);
                        break;
                    }
                }
            }

            camera.setParameters(parameters); // setting  parameters
            camera.setPreviewDisplay(surfaceHolder); // setting preview of camera
            camera.startPreview();  // starting  preview
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();  // stopping  preview
        camera.release();       // releasing
        camera = null;
    }
}
