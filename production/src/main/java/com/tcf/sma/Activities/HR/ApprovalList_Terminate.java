package com.tcf.sma.Activities.HR;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Managers.ImagePreviewDialog;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignReasonModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.R;
import com.tcf.sma.utils.ImageCompression;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ApprovalList_Terminate extends DrawerActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, IPickResult {
    private static final int PICK_IMAGE = 1;
    int terminateReasonID, empDetailId;
    String Name = "", Reason = "", RecommendFormPath = null;
    View view;
    int schoolId;
    private Button approve, close;
    private AppCompatImageView recommendForm;
    private Spinner spinner;
    private List<EmployeeResignReasonModel> reasonsList;
    private ArrayAdapter<EmployeeResignReasonModel> ReasonAdapter;
    private TextView name, EmpCode, date, LWDate;
    private Uri mUri;
    private File image = null;
    private EmployeeModel employeeModel;
    private String nfCurrentDate, sdfCurrentDate, _pickedDate;
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_approval_list_terminate);
        setToolbar("Terminate Employee", this, false);
        Intent intent = getIntent();


        if (intent != null && intent.hasExtra("empDetailId")) {
            empDetailId = intent.getIntExtra("empDetailId", 0);
            schoolId = intent.getIntExtra("schoolId", 0);
            employeeModel = EmployeeHelperClass.getInstance(view.getContext()).getEmployee(empDetailId);
        }

        init(view);

        SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yy");
        Calendar calender = Calendar.getInstance();

        currentDate = calender.getTime();
        sdfCurrentDate = sd.format(currentDate);
        nfCurrentDate = newFormat.format(currentDate);
        _pickedDate = sdfCurrentDate;
        date.setText(nfCurrentDate);
        LWDate.setText(nfCurrentDate);

        if (employeeModel != null) {
            name.setText(employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name());
            EmpCode.setText(employeeModel.getEmployee_Code());
        }

        populateTerminateSpinner();

    }

    private void init(View view) {
        recommendForm = view.findViewById(R.id.iv_recomend_form);
        approve = (Button) view.findViewById(R.id.btn_terminateApprove);
        close = (Button) view.findViewById(R.id.btnClose);
        name = (TextView) view.findViewById(R.id.tv_terminate_name);
        date = view.findViewById(R.id.tv_terminate_date);
        LWDate = view.findViewById(R.id.tv_terminate_Lastday);
        EmpCode = view.findViewById(R.id.tv_terminate_empCode);
        spinner = view.findViewById(R.id.spin_terminate_reasons);
        spinner.setOnItemSelectedListener(this);

        recommendForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (RecommendFormPath != null) {
                    previewImage(recommendForm.getDrawable());
                } else {
                    try {
                        PickImageDialog.build(new PickSetup().setTitle("Resignation Form")
                                .setPickTypes(EPickType.CAMERA)).show(ApprovalList_Terminate.this).setOnPickResult(ApprovalList_Terminate.this::onPickResult);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

        });
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RecommendFormPath != null && !spinner.getSelectedItem().toString().equals(getString(R.string.select_reason))) {
                    Terminate();
                } else {
                    String toast = "Please fill the following fields\n";
                    if (spinner.getSelectedItem().toString().equals(getString(R.string.select_reason)))
                        toast += "Resign Reason\n";
                    if (RecommendFormPath == null)
                        toast += "Resign Form";
                    Toast.makeText(ApprovalList_Terminate.this, toast, Toast.LENGTH_SHORT).show();
//
//                    Toast.makeText(ApprovalList_Terminate.this, "Please fill all the required fields above.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void Terminate() {

        EmployeeSeparationModel erm = new EmployeeSeparationModel();
        erm.setEmp_Resign_Date(AppModel.getInstance().convertDatetoFormat(date.getText().toString(), "dd-MMM-yy", "yyyy-MM-dd"));
        erm.setEmp_Resign_Letter_IMG("");
        erm.setEmp_Resign_Form_IMG(RecommendFormPath);
        erm.setEmp_SubReasonID(terminateReasonID);
        erm.setEmployee_Personal_Detail_ID(employeeModel.getId());
        erm.setEmp_Resign_Cancel_Reason("");
        erm.setCREATED_ON_SERVER("");
        erm.setDeviceId(AppModel.getInstance().getDeviceId(ApprovalList_Terminate.this));
        erm.setUploadedOn((String) null);
        erm.setCREATED_BY(DatabaseHelper.getInstance(ApprovalList_Terminate.this).getCurrentLoggedInUser().getId());
        erm.setCREATED_ON_APP(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        erm.setEmp_Resign_Type(2);
        erm.setEmp_Status(1);
        erm.setLastWorkingDay(AppModel.getInstance().convertDatetoFormat(LWDate.getText().toString(), "dd-MMM-yy", "yyyy-MM-dd"));

        if (employeeModel != null && erm != null) {
//            EmployeeResignationDialogManager erdm = new EmployeeResignationDialogManager(ApprovalList_Terminate.this, employeeModel, erm, "");
//            erdm.show();
        }
//
//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
//                .setTitle("Confirm")
////                .setIcon(R.drawable.alertmark)
//                .setMessage("Are you sure you want to approve "+ name.getText().toString()+ " termination?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//       alertDialogBuilder.create();
//        alertDialogBuilder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                // Log.d(TAG, String.valueOf(bitmap));

                //  ImageView imageView = findViewById(R.id.img_ResLetter);
                Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap1, 550, 500, true);
                recommendForm.setImageBitmap(bMapScaled);
                byte[] Recommend_Form = AppModel.getInstance().bitmapToByte(bMapScaled);
                RecommendFormPath = AppModel.getInstance().saveImageToStorage2(Recommend_Form, view.getContext(), employeeModel.getEmployee_Code(), 1, employeeModel.getEmployee_ID());

//                img_ResLetter.setAdjustViewBounds(true);
//                img_ResLetter.setScaleType(ImageView.ScaleType.FIT_XY);
            } catch (IOException e) {
                e.printStackTrace();
            }

//           recommendForm.setImageURI(selectedImage);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        BitmapDrawable drawable = (BitmapDrawable) recommendForm.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        outState.putParcelable("image", bitmap);

        super.onSaveInstanceState(outState);
    }

    private void pickFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Recommend Form"), PICK_IMAGE);


    }

    private void populateTerminateSpinner() {
        reasonsList = EmployeeHelperClass.getInstance(this).getResignReasons(2);
        reasonsList.add(0, new EmployeeResignReasonModel(0, getString(R.string.select_reason)));


        ReasonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reasonsList);
        ReasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ReasonAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spin_terminate_reasons) {
            Reason = ((EmployeeResignReasonModel) parent.getItemAtPosition(position)).getResignReason();
            terminateReasonID = ((EmployeeResignReasonModel) parent.getItemAtPosition(position)).getID();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {

            ImageCompression imageCompression = new ImageCompression(ApprovalList_Terminate.this);

            String path = pickResult.getPath();

            imageCompression.execute(path);

            image = new File(path);

            //Bitmap rotatedImage = setOrientation(pickResult.getBitmap(), path);

            Uri targetUri = AppModel.getInstance().getImageUri(getApplicationContext(), pickResult.getBitmap(), image);
//            Uri targetUri = Uri.fromFile(image);
            mUri = targetUri;

//            capturedImage = rotatedImage;
//            captureImagePath = path;


            recommendForm.setImageBitmap(pickResult.getBitmap());


            byte[] Recommendation_Form = AppModel.getInstance().bitmapToByte(pickResult.getBitmap());
            RecommendFormPath = AppModel.getInstance().saveImageToStorage2(Recommendation_Form, view.getContext(), employeeModel.getDesignation(), 1, employeeModel.getId());


//            ivCaptureImage.setImageBitmap(pickResult.getBitmap());
//            ivCaptureImage.setRotation(90);

        } else {
            Toast.makeText(ApprovalList_Terminate.this, pickResult.getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void previewImage(Drawable previewImageFile) {
        ImagePreviewDialog imagePreviewDialog = new ImagePreviewDialog(this, previewImageFile);
        imagePreviewDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ApprovalList_Terminate.this, EmployeeDetailsActivity.class);
        intent.putExtra("empDetailId", empDetailId);
        intent.putExtra("schoolId", schoolId);
        startActivity(intent);
        finish();
    }
}
