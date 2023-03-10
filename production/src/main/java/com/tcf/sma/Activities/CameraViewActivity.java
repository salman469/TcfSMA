package com.tcf.sma.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.size.Size;
import com.tcf.sma.Activities.HR.EmployeeDetailsActivity;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Interfaces.OnChangePendingSyncCount;
import com.tcf.sma.Managers.ImagePreviewDialog;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.UserImageModel;
import com.tcf.sma.R;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class CameraViewActivity extends DrawerActivity implements View.OnClickListener, FrameProcessor, OnChangePendingSyncCount {

    private Facing cameraFacing = Facing.BACK;
    private CameraView camera;
    private LinearLayout capturePicture;
    private ImageButton toggleCameraButton,toggleFlash;
    private ImageView iv_face_mask;
    private OnChangePendingSyncCount onChangePendingSyncCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        onChangePendingSyncCount = this;

        init();

        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);

                result.toBitmap(1000, 1000, bitmap -> {
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    previewImage(drawable, result, bitmap);
//                        iv_face_mask.setImageBitmap(bitmap);
                });


//                iv_face_mask.setBackground(null);
            }
        });
    }

    void init(){
        camera = findViewById(R.id.camera);
        camera.setLifecycleOwner(this);
        camera.addFrameProcessor(this);
        capturePicture = findViewById(R.id.capturePicture);
        capturePicture.setOnClickListener(this);
        toggleCameraButton = findViewById(R.id.toggleCamera);
        toggleCameraButton.setOnClickListener(this);
        toggleFlash = findViewById(R.id.toggleFlash);
        toggleFlash.setOnClickListener(this);
        iv_face_mask = findViewById(R.id.iv_face_mask);

        String guidelineText =
                "Use White/Blue background.\n"+
                "\n" +
                        "Picture should not be blur.\n"+
                "\n" +
                        "Picture should be bright & with proper light.\n"+
                "\n" +
                        "Take a close-up of the full face and shoulders.\n"+
                        "\n" +
                        "Picture should be within the green boundaries.\n";


        if(AppModel.getInstance().photoFlag == 1){
            AppModel.getInstance().showMessage(new WeakReference<>(CameraViewActivity.this),
                    "Instructions",
                    guidelineText);

            iv_face_mask.setVisibility(View.VISIBLE);
        }
        else if (AppModel.getInstance().photoFlag == 2 || AppModel.getInstance().photoFlag == 3)
            iv_face_mask.setVisibility(View.GONE);
        else {
            if(!getIntent().hasExtra("showMsg"))
                 guidelineText =
                        "Use White/Blue background.\n"+
                                "\n" +
                                "Picture should not be blur.\n"+
                                "\n" +
                                "Picture should be bright & with proper light.\n"+
                                "\n" +
                                "Take a close-up of the full face and shoulders.\n"+
                                "\n" +
                                "Picture should be within the green boundaries.\n";
            AppModel.getInstance().showMessage(new WeakReference<>(CameraViewActivity.this),
                    "Instructions",
                    guidelineText
                    );
                    /*"Prepare your phone. Make sure your phone is charged, camera is clean, and you have backup storage for your images.\n" +
                            "\n" +
                            "Light: Take the picture outdoor or find a room with a well-lit window. Close flash, as this will mess up the colors.\n" +
                            "\n" +
                            "Backdrop:  Use a white (pastel colors) background for your setup\n" +
                            "\n" +
                            "Focus. Touch the circular focus button, then touch your student so that the camera knows to properly focus on the child. Avoid shaking the camera.\n" +
                            "\n" +
                            "Eye level. Picture should show the student looking straight on, and have a close-up of the full face and shoulder\n" +
                            "\n" +
                            "Things to Avoid. Do not take a picture of a student polaroid picture. Student should be captured live. Student must not be wearing mask or covering face");*/
            iv_face_mask.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.capturePicture:
                capturePicture();
                break;
            case R.id.toggleCamera:
                toggleCamera();
                break;
            case R.id.toggleFlash:
                Log.i("flash",camera.getFlash() + "");
                if(camera.getFlash() == Flash.TORCH){
                    camera.setFlash(Flash.OFF);
                    toggleFlash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_off));
                }else if(camera.getFlash() == Flash.OFF || camera.getFlash() == Flash.AUTO){
                    camera.setFlash(Flash.TORCH);
                    toggleFlash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flash_on));
                }
                break;
        }
    }

    @Override
    public void process(@NonNull Frame frame) {
//        long time = frame.getTime();
//        Size size = frame.getSize();
//        int format = frame.getFormat();
//        int userRotation = frame.getRotationToUser();
//        int viewRotation = frame.getRotationToView();
//        if (frame.getDataClass() == byte[].class) {
//            byte[] data = frame.getData();
//            // Process byte array...
//        } else if (frame.getDataClass() == Image.class) {
//            Image data = frame.getData();
//            // Process android.media.Image...
//        }
    }

    private void capturePicture() {
        if (camera.getMode() == Mode.VIDEO)
            return;
        if (camera.isTakingPicture())
            return;
//        captureTime = System.currentTimeMillis();
//        message("Capturing picture...", false);
        camera.takePicture();
    }

    private void toggleCamera() {
        if (camera.isTakingPicture()) return;
        switch (camera.toggleFacing()) {
            case BACK:
                cameraFacing = Facing.BACK;
//                ->message("Switched to back camera!", false)
                break;
            case FRONT:
                cameraFacing = Facing.FRONT;
//                 ->message("Switched to front camera!", false)
                break;
        }
    }

    private void previewImage(Drawable previewImageFile, PictureResult result, Bitmap bitmap) {
        ImagePreviewDialog imagePreviewDialog = new ImagePreviewDialog(this, previewImageFile, true);
        imagePreviewDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (AppModel.getInstance().photoFlag == 1) {
                        AppModel.getInstance().img1 = result.getData();

                    } else if (AppModel.getInstance().photoFlag == 2) {
                        AppModel.getInstance().img2 = result.getData();

                    } else if (AppModel.getInstance().photoFlag == 3) {
                        AppModel.getInstance().img3 = result.getData();
                    } else if (getIntent().hasExtra("empId")) {
                        int empId = getIntent().getIntExtra("empId", 0);
                        Bitmap thumbBitmap;
                        thumbBitmap = ThumbnailUtils.extractThumbnail(bitmap,100, 100);

                        byte[] user_image = AppModel.getInstance().bitmapToByte(thumbBitmap);
                        String imagePath = AppModel.getInstance().saveImageToStorage2(user_image, CameraViewActivity.this, "User_Image_", 1, empId);

                        UserImageModel userImageModel = new UserImageModel();
                        userImageModel.setUser_id(empId);
                        userImageModel.setUser_image_path(imagePath);
                        userImageModel.setUploaded_on(null);
                        long id = EmployeeHelperClass.getInstance(CameraViewActivity.this).insertOrUpdateUserImage(userImageModel, CameraViewActivity.this);

                        if (id > 0) {
                            //Important when any change in table call this method
                            onChangePendingSyncCount.onChangeRecords();
                            ShowToast("Image Updated Successfully");
                        }
                        else
                            ShowToast("Something went wrong");

                    }
                    else {
                        StudentProfileActivity.profileBitmap = ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
                    }
                    finish();
            }
        });
        imagePreviewDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(Arrays.stream(grantResults).allMatch(e-> e == PackageManager.PERMISSION_GRANTED) && !camera.isOpened()){
         camera.open();
        }else{
            Toast.makeText(this, "Please allow camera and microphone permission from app settings to use camera features", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onChangeRecords() {
        super.calPendingRecords(AppModel.getInstance().getSpinnerSelectedSchool(CameraViewActivity.this));
        super.autoSyncStartWhenUploadIsPendingInQueue();
    }
}