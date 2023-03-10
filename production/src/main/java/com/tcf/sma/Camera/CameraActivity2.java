package com.tcf.sma.Camera;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tcf.sma.Models.AppModel;
import com.tcf.sma.R;
import com.tcf.sma.Views.VerticalTextView;

public class CameraActivity2 extends AppCompatActivity implements SensorEventListener {
    public boolean mAutoFocus = false;
    Preview mPreview;
    VerticalTextView captureimg;
    RelativeLayout rv;
    TouchView mView;
    ProgressDialog dialog;
    private int mScreenHeight;
    private int mScreenWidth;
    private Rect rec = new Rect();
    private SensorManager mSensorManager;
    private Sensor mAccel;
    private boolean mInvalidate = false;
    private boolean mInitialized = false;
    private float mLastX = 0;
    private float mLastY = 0;
    private float mLastZ = 0;
    private Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {

        public void onAutoFocus(boolean autoFocusSuccess, Camera arg1) {
            mAutoFocus = true;
            CaptureImage();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mSensorManager = (SensorManager) getSystemService(Context.
                SENSOR_SERVICE);

        mAccel = mSensorManager.getDefaultSensor(Sensor.
                TYPE_ACCELEROMETER);

        setContentView(R.layout.camera_activity_custom);

        mPreview = (Preview) findViewById(R.id.preview);
        captureimg = (VerticalTextView) findViewById(R.id.captureimg);
        mView = (TouchView) findViewById(R.id.left_top_view);
        rv = (RelativeLayout) findViewById(R.id.parentSurfaceView);
        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CameraActivity2.this, "asfa", Toast.LENGTH_SHORT).show();
            }
        });
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScreenHeight = displaymetrics.heightPixels;
        mScreenWidth = displaymetrics.widthPixels;

        rec.set((int) ((double) mScreenWidth * .85),
                (int) ((double) mScreenHeight * .10),
                (int) ((double) mScreenWidth * .85) + captureimg.getMinimumWidth(),
                (int) ((double) mScreenHeight * .70) + captureimg.getMinimumHeight());
        mView.setRec(rec);

        captureimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreview.setCameraFocus(myAutoFocusCallback);
//                CaptureImage();
            }
        });
    }

    public void CaptureImage() {

        dialog = new ProgressDialog(CameraActivity2.this);
        dialog.setMessage("Capturing...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mAutoFocus) {
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

    public Double[] getRatio() {
        Camera.Size s = mPreview.getCameraParameters().getPreviewSize();
        double heightRatio = (double) s.height / (double) mScreenHeight;
        double widthRatio = (double) s.width / (double) mScreenWidth;
        Double[] ratio = {heightRatio, widthRatio};
        return ratio;
    }

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
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.i(TAG, "onPause()");
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_UI);
        //Log.i(TAG, "onResume()");
    }
}
