package com.tcf.sma.Camera;

/**
 * Created by Zubair Soomro on 5/8/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.tcf.sma.Models.AppModel;
import com.tcf.sma.R;
import com.tcf.sma.Views.VerticalTextView;

import java.io.File;


public class CameraPreview extends Activity implements SensorEventListener {

    private static VerticalTextView mTakePicture;
    private Preview mPreview;
    private TouchView mView;

    private boolean mAutoFocus = true;

    private boolean mFlashBoolean = false;

    private SensorManager mSensorManager;
    private Sensor mAccel;
    private boolean mInitialized = false;
    private float mLastX = 0;
    private float mLastY = 0;
    private float mLastZ = 0;
    private Rect rec = new Rect();

    private int mScreenHeight;
    private int mScreenWidth;

    private boolean mInvalidate = false;

    private File mLocation = new File(Environment.
            getExternalStorageDirectory(), "test.jpg");
    private ProgressDialog dialog;
    // this is the autofocus call back
    private AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {

        public void onAutoFocus(boolean autoFocusSuccess, Camera arg1) {
            //Wait.oneSec();
            mAutoFocus = true;
        }
    };
    // This method takes the preview image, grabs the rectangular
    // part of the image selected by the bounding box and saves it.
    // A thread is needed to save the picture so not to hold the UI thread.
    private OnClickListener previewListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mAutoFocus) {
                mAutoFocus = false;
                Wait.oneSec();
                CaptureImage();
                mAutoFocus = true;
                //mPreview.setCameraFocus(myAutoFocusCallback);
            }
        }
    };

    public static float getBtnX() {
        return Math.abs(mTakePicture.getX());
    }

    // I am not using this in this example, but its there if you want
    // to turn on and off the flash.

    public static float getBtnY() {
        return Math.abs(mTakePicture.getY());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i(TAG, "onCreate()");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // display our (only) XML layout - Views already ordered
        setContentView(R.layout.camera_activity_custom);

        // the accelerometer is used for autofocus
        mSensorManager = (SensorManager) getSystemService(Context.
                SENSOR_SERVICE);
        mAccel = mSensorManager.getDefaultSensor(Sensor.
                TYPE_ACCELEROMETER);

        // get the window width and height to display buttons
        // according to device screen size
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;
        mScreenWidth = displaymetrics.widthPixels;

        // I need to get the dimensions of this drawable to set margins
        // for the ImageView that is used to take pictures

        mTakePicture = (VerticalTextView) findViewById(R.id.captureimg);
        AppModel.getInstance().btnPositionX = mTakePicture.getX();
        AppModel.getInstance().btnPositionY = mTakePicture.getY();
        // rec is used for onInterceptTouchEvent. I pass this from the
        // highest to lowest layer so that when this area of the screen
        // is pressed, it ignores the TouchView events and passes it to
        // this activity so that the button can be pressed.

        mTakePicture.setOnClickListener(previewListener);
        // get our Views from the XML layout
        mPreview = (Preview) findViewById(R.id.preview);
        mView = (TouchView) findViewById(R.id.left_top_view);
        mView.setRec(rec);

    }

    // with this I get the ratio between screen size and pixels
    // of the image so I can capture only the rectangular area of the
    // image and save it.
    public Double[] getRatio() {
        Size s = mPreview.getCameraParameters().getPreviewSize();
        double heightRatio = (double) s.height / (double) mScreenHeight;
        double widthRatio = (double) s.width / (double) mScreenWidth;
        Double[] ratio = {heightRatio, widthRatio};
        return ratio;
    }

    public void CaptureImage() {

        dialog = new ProgressDialog(CameraPreview.this);
        dialog.setMessage("Capturing...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Double[] ratio = getRatio();
                int left = (int) (ratio[1] * (double) mView.getmLeftTopPosX());

                int top = (int) (ratio[0] * (double) mView.getmLeftTopPosY());

                int right = (int) (ratio[1] * (double) mView.getmRightBottomPosX());

                int bottom = (int) (ratio[0] * (double) mView.getmRightBottomPosY());

                if (AppModel.getInstance().photoFlag == 1) {
                    AppModel.getInstance().img1 = AppModel.getInstance().bitmapToByte(mPreview.getPic(left, top, right, bottom));
                    setSharedPrefs("cam_bounds" + 1);
                } else if (AppModel.getInstance().photoFlag == 2) {
                    AppModel.getInstance().img2 = AppModel.getInstance().bitmapToByte(mPreview.getPic(left, top, right, bottom));
                    setSharedPrefs("cam_bounds" + 2);
                } else if (AppModel.getInstance().photoFlag == 3) {
                    AppModel.getInstance().img3 = AppModel.getInstance().bitmapToByte(mPreview.getPic(left, top, right, bottom));
                    setSharedPrefs("cam_bounds" + 3);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        dialog = null;
                        finish();
                    }
                });
            }
        }).start();
    }

    public void setSharedPrefs(String name) {
        SharedPreferences.Editor editor = getSharedPreferences(name, MODE_PRIVATE).edit();
        editor.putFloat("ltpx", mView.getmLeftTopPosX());
        editor.putFloat("ltpY", mView.getmLeftTopPosY());
        editor.putFloat("rtpx", mView.getmRightTopPosX());
        editor.putFloat("rtpY", mView.getmRightTopPosY());
        editor.putFloat("rbpx", mView.getmRightBottomPosX());
        editor.putFloat("rbpy", mView.getmRightBottomPosY());
        editor.putFloat("lbpx", mView.getmLeftBottomPosX());
        editor.putFloat("lbpy", mView.getmLeftBottomPosY());
        editor.apply();
    }

    // just to close the app and release resources.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    // mainly used for autofocus to happen when the user takes a picture
    // I also use it to redraw the canvas using the invalidate() method
    // when I need to redraw things.
    public void onSensorChanged(SensorEvent event) {

        if (mInvalidate) {
            mView.invalidate();
            mInvalidate = false;
        }
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        }
        float deltaX = Math.abs(mLastX - x);
        float deltaY = Math.abs(mLastY - y);
        float deltaZ = Math.abs(mLastZ - z);

        if (deltaX > .5 && mAutoFocus) { //AUTOFOCUS (while it is not autofocusing)
            mAutoFocus = false;
            mPreview.setCameraFocus(myAutoFocusCallback);
        }
        if (deltaY > .5 && mAutoFocus) { //AUTOFOCUS (while it is not autofocusing)
            mAutoFocus = false;
            mPreview.setCameraFocus(myAutoFocusCallback);
        }
        if (deltaZ > .5 && mAutoFocus) { //AUTOFOCUS (while it is not autofocusing) */
            mAutoFocus = false;
            mPreview.setCameraFocus(myAutoFocusCallback);
        }

        mLastX = x;
        mLastY = y;
        mLastZ = z;

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_UI);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

}
