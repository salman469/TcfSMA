package com.tcf.sma.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.MarshMallowPermission;
import com.tcf.sma.Managers.ImagePreviewDialog;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.EnrollmentImageModel;
import com.tcf.sma.Models.EnrollmentModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.io.File;

import id.zelory.compressor.Compressor;

public class UpdateExistingStudentActivity extends DrawerActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    View view;
    MarshMallowPermission permission;
    ScrollView scrollView;
    Button btn_capture, btn_nic_capture, btn_admission_capture, btn_remove,
            btn_nic_remove, btn_admission_remove,
            btn_preview, btn_nic_preview, btn_admission_preview;
    ImageView iv_capture;
    ImageView iv_nic_capture;
    TextInputLayout textInputLayout;
    ImageView iv_admission_capture;
    EditText et_GrNo, et_Name, et_studentId;
    Button btn_submit;
    long id = -1, id1;
    String P, B, A;
    int schoolId, createdBy, schoolClassid;
    Spinner spn_school, spn_class_section_name;
    ClassSectionModel classSectionModel;
    ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    ArrayAdapter<SchoolModel> SchoolAdapter;
    SchoolModel schoolModel;
    StudentModel studentModel;
    int studentId = 0;
    private int idUpdate1, idUpdate;

    @Override
    protected void onResume() {
        super.onResume();
        if (AppModel.getInstance().img1 != null) {
            iv_capture.setImageBitmap(AppModel.getInstance().rotateImage(AppModel.getInstance().img1, getWindowManager().getDefaultDisplay()));
        }
        if (AppModel.getInstance().img2 != null) {
            iv_nic_capture.setImageBitmap(AppModel.getInstance().rotateImage(AppModel.getInstance().img2, getWindowManager().getDefaultDisplay()));
        }
        if (AppModel.getInstance().img3 != null) {
            iv_admission_capture.setImageBitmap(AppModel.getInstance().rotateImage(AppModel.getInstance().img3, getWindowManager().getDefaultDisplay()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_new_admission);
        setToolbar("Update Student", this, false);
        init(view);


        schoolId = (getIntent().hasExtra("SchoolId")) ? getIntent().getIntExtra("SchoolId", 0) : AppModel.getInstance().getSelectedSchool(this);

        if (getIntent().hasExtra("SchoolClassId"))
            schoolClassid = getIntent().getIntExtra("SchoolClassId", 0);

        if (getIntent().hasExtra("gr")) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            et_GrNo.setText(getIntent().getStringExtra("gr"));
            et_GrNo.setEnabled(false);
//            idUpdate = DatabaseHelper.getInstance(this).getEnrollmentIdfromGR(Integer.parseInt(getIntent().getStringExtra("gr")), SurveyAppModel.getInstance().getSelectedSchool(this));
            idUpdate = DatabaseHelper.getInstance(this).getEnrollmentIdfromGR(Integer.parseInt(getIntent().getStringExtra("gr")), schoolId);

            if (idUpdate > 0) {
                btn_submit.setText("Update");
            } else {
                btn_submit.setText("Submit");
            }
            try {
                EnrollmentImageModel enrollmentImageModel = DatabaseHelper.getInstance(this).getEnrollmentImage(idUpdate, "P");
                if (enrollmentImageModel != null) {
                    if (enrollmentImageModel.getFilename() != null) {
                        if (!enrollmentImageModel.getFilename().equals("")) {
                            File f = new File(enrollmentImageModel.getFilename());
                            Bitmap bitmap = AppModel.getInstance().rotateImage(Compressor.getDefault(this).compressToBitmap(f), getWindowManager().getDefaultDisplay());
                            iv_capture.setImageBitmap(bitmap);
                        }
                    }
                }

                EnrollmentImageModel enrollmentImageModel1 = DatabaseHelper.getInstance(this).getEnrollmentImage(idUpdate, "B");
                if (enrollmentImageModel1 != null) {
                    if (enrollmentImageModel1.getFilename() != null) {
                        if (!enrollmentImageModel1.getFilename().equals("")) {
                            File f1 = new File(enrollmentImageModel1.getFilename());
                            Bitmap bitmap1 = AppModel.getInstance().rotateImage(Compressor.getDefault(this).compressToBitmap(f1), getWindowManager().getDefaultDisplay());
                            iv_nic_capture.setImageBitmap(bitmap1);
                        }
                    }
                }

                EnrollmentImageModel enrollmentImageModel2 = DatabaseHelper.getInstance(this).getEnrollmentImage(idUpdate, "A");
                if (enrollmentImageModel2 != null) {
                    if (enrollmentImageModel2.getFilename() != null) {
                        if (!enrollmentImageModel2.getFilename().equals("")) {
                            File f2 = new File(enrollmentImageModel2.getFilename());
                            Bitmap bitmap2 = AppModel.getInstance().rotateImage(Compressor.getDefault(this).compressToBitmap(f2), getWindowManager().getDefaultDisplay());
                            iv_admission_capture.setImageBitmap(bitmap2);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        initSpinner();

        studentModel = DatabaseHelper.getInstance(this).getStudentwithGR(Integer.parseInt(et_GrNo.getText().toString()), schoolId);

        textInputLayout.setVisibility(View.VISIBLE);

        studentId = studentModel.getId();
        et_studentId.setText(studentModel.getId() + "");
        et_studentId.setEnabled(false);
        et_Name.setText(studentModel.getName());

    }

    private void initSpinner() {

        schoolModel = new SchoolModel();
        schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllUserSchools());
        ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
        spn_school.setAdapter(schoolSelectionAdapter);
        spn_school.setEnabled(false);
        spn_school.setClickable(false);


        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(schoolId + ""));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        spn_class_section_name.setAdapter(new ArrayAdapter<ClassSectionModel>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList()));
        spn_class_section_name.setEnabled(false);
        spn_class_section_name.setClickable(false);

        setSpinner(schoolId, schoolClassid);

    }

    private void setSpinner(int schoolid, int classSectionID) {
        for (SchoolModel sm : schoolModel.getSchoolsList()) {
            if (sm.getId() == schoolid) {
                spn_school.setSelection(schoolModel.getSchoolsList().indexOf(sm));
                break;
            }
        }

        for (ClassSectionModel csm : classSectionModel.getClassAndSectionsList()) {
            if (csm.getSchoolClassId() == classSectionID) {
                spn_class_section_name.setSelection(classSectionModel.getClassAndSectionsList().indexOf(csm));
                break;
            }
        }
    }


    private void init(View rootView) {

        btn_capture = (Button) rootView.findViewById(R.id.btn_capture);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit_new_student);
        btn_nic_capture = (Button) rootView.findViewById(R.id.btn_nic_capture);
        btn_admission_capture = (Button) rootView.findViewById(R.id.btn_admission_capture);
        btn_remove = (Button) rootView.findViewById(R.id.btn_remove);
        btn_nic_remove = (Button) rootView.findViewById(R.id.btn_nic_remove);
        btn_admission_remove = (Button) rootView.findViewById(R.id.btn_admission_remove);
        btn_preview = (Button) rootView.findViewById(R.id.btn_preview);
        btn_nic_preview = (Button) rootView.findViewById(R.id.btn_nic_preview);
        btn_admission_preview = (Button) rootView.findViewById(R.id.btn_admission_preview);

        et_studentId = (EditText) rootView.findViewById(R.id.et_studentId);
        textInputLayout = (TextInputLayout) rootView.findViewById(R.id.til_sid);

        iv_capture = (ImageView) rootView.findViewById(R.id.iv_capture);
        iv_nic_capture = (ImageView) rootView.findViewById(R.id.iv_nic_capture);
        iv_admission_capture = (ImageView) rootView.findViewById(R.id.iv_admission_capture);
        et_GrNo = (EditText) rootView.findViewById(R.id.et_GrNo);
        btn_capture.setOnClickListener(this);
        btn_nic_capture.setOnClickListener(this);
        btn_admission_capture.setOnClickListener(this);
        btn_remove.setOnClickListener(this);
        btn_nic_remove.setOnClickListener(this);
        btn_admission_remove.setOnClickListener(this);
        btn_preview.setOnClickListener(this);
        btn_nic_preview.setOnClickListener(this);
        btn_admission_preview.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        spn_school = (Spinner) rootView.findViewById(R.id.spn_school);
        spn_class_section_name = (Spinner) rootView.findViewById(R.id.spn_class_section_name);

        et_Name = (EditText) rootView.findViewById(R.id.et_Name);
        et_Name.setEnabled(false);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_capture:

                openCamera();
                AppModel.getInstance().photoFlag = 1;
                break;
            case R.id.btn_nic_capture:
                openCamera();
                AppModel.getInstance().photoFlag = 2;
                break;
            case R.id.btn_admission_capture:
                openCamera();
                AppModel.getInstance().photoFlag = 3;
                break;
            case R.id.iv_capture:
                break;
            case R.id.iv_nic_capture:
                break;
            case R.id.iv_admission_capture:
                break;

            case R.id.btn_preview:
                previewImage(iv_capture.getDrawable());
                break;
            case R.id.btn_nic_preview:
                previewImage(iv_nic_capture.getDrawable());
                break;
            case R.id.btn_admission_preview:
                previewImage(iv_admission_capture.getDrawable());
                break;

            case R.id.btn_remove:
                AppModel.getInstance().img1 = null;
                Picasso.with(this).load(R.mipmap.profile_pic).into(iv_capture);
                break;
            case R.id.btn_nic_remove:
                AppModel.getInstance().img2 = null;
                Picasso.with(this).load(R.mipmap.profile_pic).into(iv_nic_capture);
                break;
            case R.id.btn_admission_remove:
                AppModel.getInstance().img3 = null;
                Picasso.with(this).load(R.mipmap.profile_pic).into(iv_admission_capture);
                break;

            case R.id.btn_submit_new_student:

//                schoolId = SurveyAppModel.getInstance().getSelectedSchool(this);
                createdBy = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId();
                if (btn_submit.getText().toString().equals("Update")) {
                    final EnrollmentModel enrollmentModel = new EnrollmentModel(Integer.parseInt(et_GrNo.getText().toString()), schoolId, createdBy, AppModel.getInstance().getDate(), null, null, null, null, studentId + "");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Are you sure you want to update the Information?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            String path1 = AppModel.getInstance().saveImageToStorage2(AppModel.getInstance().img1, UpdateExistingStudentActivity.this, et_GrNo.getText().toString(), 1, schoolId);
                            String path2 = AppModel.getInstance().saveImageToStorage2(AppModel.getInstance().img2, UpdateExistingStudentActivity.this, et_GrNo.getText().toString(), 2, schoolId);
                            String path3 = AppModel.getInstance().saveImageToStorage2(AppModel.getInstance().img3, UpdateExistingStudentActivity.this, et_GrNo.getText().toString(), 3, schoolId);
                            AppModel.getInstance().img1 = null;
                            AppModel.getInstance().img2 = null;
                            AppModel.getInstance().img3 = null;
                            AppModel.getInstance().photoFlag = -1;

                            if (path1 != null) {
                                DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).updateEnrollmentImage(new EnrollmentImageModel(idUpdate, path1, null, null, "P"));
                            }

                            if (path2 != null) {
                                DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).updateEnrollmentImage(new EnrollmentImageModel(idUpdate, path2, null, null, "B"));
                            }

                            if (path3 != null) {
                                DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).updateEnrollmentImage(new EnrollmentImageModel(idUpdate, path3, null, null, "A"));
                            }

//                            if (DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).getEnrollmentImage(idUpdate, "P") != null) {
//                                P = DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).getEnrollmentImage(idUpdate, "P").getFilename();
//                            }
//                            if (DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).getEnrollmentImage(idUpdate, "B") != null) {
//                                B = DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).getEnrollmentImage(idUpdate, "B").getFilename();
//                            }
//                            if (DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).getEnrollmentImage(idUpdate, "A") != null) {
//                                A = DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).getEnrollmentImage(idUpdate, "A").getFilename();
//                            }
                            enrollmentModel.setENROLLMENT_REVIEW_STATUS(AppConstants.PROFILE_APPROVED_KEY);
                            enrollmentModel.setENROLLMENT_UPLOADED_ON(null);
                            DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).updateEnrollment(enrollmentModel, idUpdate);
//                            if (P != null && B != null && A != null) {
//                                enrollmentModel.setENROLLMENT_REVIEW_STATUS(AppConstants.PROFILE_COMPLETE_KEY);
//                                enrollmentModel.setENROLLMENT_UPLOADED_ON(null);
//                                DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).updateEnrollment(enrollmentModel, idUpdate);
//                            } else {
//                                enrollmentModel.setENROLLMENT_REVIEW_STATUS(AppConstants.PROFILE_INCOMPLETE_KEY);
//                                enrollmentModel.setENROLLMENT_UPLOADED_ON(null);
//                                DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).updateEnrollment(enrollmentModel, idUpdate);
//                            }
                            finish();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            AppModel.getInstance().img1 = null;
                            AppModel.getInstance().img2 = null;
                            AppModel.getInstance().img3 = null;
                            AppModel.getInstance().photoFlag = -1;
                            finish();
                        }
                    });
                    builder.show();
                } else {


                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Are you sure you want to Save the Information?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            EnrollmentModel enrollmentModel = new EnrollmentModel(Integer.parseInt(et_GrNo.getText().toString()), schoolId, createdBy, AppModel.getInstance().getDate(), AppConstants.PROFILE_APPROVED_KEY, null, null, null, studentId + "");
                            id = DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).insertEnrollment(enrollmentModel);

                            String path1 = AppModel.getInstance().saveImageToStorage2(AppModel.getInstance().img1, UpdateExistingStudentActivity.this, et_GrNo.getText().toString(), 1, schoolId);
                            String path2 = AppModel.getInstance().saveImageToStorage2(AppModel.getInstance().img2, UpdateExistingStudentActivity.this, et_GrNo.getText().toString(), 2, schoolId);
                            String path3 = AppModel.getInstance().saveImageToStorage2(AppModel.getInstance().img3, UpdateExistingStudentActivity.this, et_GrNo.getText().toString(), 3, schoolId);
                            AppModel.getInstance().img1 = null;
                            AppModel.getInstance().img2 = null;
                            AppModel.getInstance().img3 = null;
                            AppModel.getInstance().photoFlag = -1;

                            if (path1 != null && !path1.equals("")) {
                                DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).insertEnrollmentImage(new EnrollmentImageModel(id, path1, null, null, "P"));
                            }
                            if (path2 != null && !path2.equals("")) {
                                DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).insertEnrollmentImage(new EnrollmentImageModel(id, path2, null, null, "B"));
                            }
                            if (path3 != null && !path3.equals("")) {
                                DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).insertEnrollmentImage(new EnrollmentImageModel(id, path3, null, null, "A"));
                            }

                            finish();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            AppModel.getInstance().img1 = null;
                            AppModel.getInstance().img2 = null;
                            AppModel.getInstance().img3 = null;
                            AppModel.getInstance().photoFlag = -1;
                            DatabaseHelper.getInstance(UpdateExistingStudentActivity.this).deleteEnrollment(id);
                            finish();
                        }
                    });
                    builder.show();

                    break;
                }
        }
    }

    private void previewImage(Drawable previewImageFile) {
        ImagePreviewDialog imagePreviewDialog = new ImagePreviewDialog(this, previewImageFile);
        imagePreviewDialog.show();
    }

    private void openCamera() {

        permission = new MarshMallowPermission(this);
        if (permission.checkPermissionForCamera()) {
            if (permission.checkPermissionForExternalStorage()) {
                captureImage();
            } else {
                permission.requestPermissionForExternalStorage();
            }
        } else {
            permission.requestPermissionForCamera();
        }
    }

    private void captureImage() {
        this.startActivity(new Intent(this, CameraActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MarshMallowPermission.CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permission.requestPermissionForExternalStorage();
        } else if (requestCode == MarshMallowPermission.EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            captureImage();
        }
    }
}
