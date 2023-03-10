package com.tcf.sma.Survey.activities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.tcf.sma.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CameraActivity extends Activity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static boolean turnOnFlash = false;
    public static boolean isFlashPresent = false;
    public static boolean isImageCaptured = false;
    byte[] pictureData;
    String imageName;
    String imagePath;
    private Camera mCamera;
    private CameraPreview mPreview;
    private PictureCallback mPicture = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {


            pictureData = new byte[data.length];
            pictureData = data;

            System.out.println(mCamera.getParameters());

//			mCamera.release();
//			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//					.format(new Date());
            File mediaStorageDir = new File(imagePath);

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory");
                }
            }

            //File pictureFile = new File(mediaStorageDir.getPath()+ File.separator + "IMG_" + timeStamp + ".jpg");

            File pictureFile = new File(imagePath + imageName);

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(pictureData);
                fos.close();

                isImageCaptured = true;

//				mCamera.release();
                finish();

            } catch (FileNotFoundException e) {
                Log.d("Error", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("Error", "Error accessing file: " + e.getMessage());
            }

            // }
            // });

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity_survey);

        if (getIntent().hasExtra("fileName") && getIntent().hasExtra("filePath")) {
            imageName = getIntent().getExtras().getString("fileName");
            imagePath = getIntent().getExtras().getString("filePath");
        }
//		cameraIntent.putExtra("fileName", imageFileName);
//		cameraIntent.putExtra("filePath", imageFilePath);


        FrameLayout preview = findViewById(R.id.camera_preview);

        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            Log.e("Number of Cameras on Device",
                    String.valueOf(Camera.getNumberOfCameras()));

            try {
                mCamera = Camera.open();

                mPreview = new CameraPreview(this, mCamera);
                preview.addView(mPreview);
//				mPreview = new CameraPreview(this, mCamera);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Camera not Available");
        }


        ImageView finishButton = (ImageView) findViewById(R.id.finish_camera);
        finishButton.setOnClickListener(v -> {
//				mCamera.release();
            finish();
        });


        final ImageView flashButton = (ImageView) findViewById(R.id.flash_Button);
        flashButton.setOnClickListener(v -> {

            final Parameters parameters = mCamera.getParameters();

            if (isFlashPresent) {
                if (turnOnFlash) {
                    turnOnFlash = false;
                    parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
                    flashButton.setImageResource(R.drawable.flashon);
                } else {
                    turnOnFlash = true;
                    parameters.setFlashMode(Parameters.FLASH_MODE_ON);
                    flashButton.setImageResource(R.drawable.flashoff);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Flash not Present", Toast.LENGTH_LONG).show();

            }

            parameters.set("jpeg-quality", 100);

            mCamera.setParameters(parameters);
            mCamera.startPreview();
        });

        ImageView captureButton = findViewById(R.id.button_capture);
        captureButton.setOnClickListener(v -> {
            // get an image from the camera
            mCamera.takePicture(null, null, mPicture);
        });
    }

    public void onBackPressed() {
        if (mCamera != null) {
//			mCamera.release();
        }
        finish();
    }
}