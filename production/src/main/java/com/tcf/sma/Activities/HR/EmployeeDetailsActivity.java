package com.tcf.sma.Activities.HR;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.TooltipCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.CameraViewActivity;
import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Activities.NewAdmissionActivity;
import com.tcf.sma.Activities.StudentProfileActivity;
import com.tcf.sma.Adapters.HR.PositionHistoryAdapter;
import com.tcf.sma.Adapters.HR.QualificationHistoryAdapter;
import com.tcf.sma.Adapters.HR.ResignationHistoryAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Interfaces.OnChangePendingSyncCount;
import com.tcf.sma.Managers.ImagePreviewDialog;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.EnabledModules;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeLeaveModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeePositionModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeQualificationDetailModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.Models.RetrofitModels.HR.UserImageModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


public class EmployeeDetailsActivity extends DrawerActivity implements View.OnClickListener,OnChangePendingSyncCount {
    boolean ImageCaptured = false;
    private FrameLayout user_profile;
    private CircleImageView iv_capture;
    private View view;
    private LinearLayout posHSW, qualHSW, rsgHSW;
    private RecyclerView recyclerView, recyclerViewPosition, recyclerViewQualification, recyclerViewResign;
    private Button btn_leaves, btn_resign, btn_terminate, btn_close;
    private ImageView btn_update, iv_showLeave, iv_showResignation, iv_showPosition, iv_showQualification, iv_addLeaves, iv_showEP;
    private TextView profileProgressCount, tv_userId, tv_status, tv_employeeCode, tv_designation, tv_joiningDate, tv_email, tv_fatherName,
            tv_motherName, tv_cnic, tv_MobileNumber, tv_emp_name, tv_noLeaves, tv_noQual, tv_noPos, tv_noRsg;
    //    private TextView tv_firstName, tv_lastName;
    private EditText et_MobileNumber, email, mobileNumber;
    private LinearLayout epCV;
    private ProgressBar profileProgressBar;

//    private LeavesAdapter leaveAdapter;
    private LinearLayout llRsg, resignTitle, ll_leaves;
    private PositionHistoryAdapter positionHistoryAdapter;
    private QualificationHistoryAdapter qualificationHistoryAdapter;
    private ResignationHistoryAdapter resignationHistoryAdapter;
    private List<EmployeeSeparationModel> employeeSeparationModelList;
    private EmployeeSeparationModel esm;
    private boolean isShowLeaveClicked = false, isShowQualificationClicked = false, isShowPositionClicked = false, isShowEPClicked = true, isShowResignationClicked = false;
    private EmployeeModel employeeModel, em;
    private int empDetailId = 0, schoolId;
    private SchoolModel schoolModel;
    private boolean isFABOpen = false;
    private EnabledModules enabledModules;
    private String globalMessage = " Because your app version is lower than our latest version";
    private String strEmail, strMN;

    private OnChangePendingSyncCount onChangePendingSyncCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.new_employee_profile);
        setToolbar("Employee Profile", this, false);
        init(view);
        working();
    }

    private void working() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("empDetailId")) {
            empDetailId = intent.getIntExtra("empDetailId", 0);
            employeeModel = EmployeeHelperClass.getInstance(view.getContext()).getEmployee(empDetailId);
        }

        if (intent != null && intent.hasExtra("schoolId")) {
            schoolId = intent.getIntExtra("schoolId", 0);
            schoolModel = DatabaseHelper.getInstance(view.getContext()).getSchoolById(schoolId);
        }

        if (schoolId <= 0) {
            schoolModel = DatabaseHelper.getInstance(view.getContext())
                    .getSchoolById(AppModel.getInstance().getSpinnerSelectedSchool(view.getContext()));
        }

        populateFields();
        try {
            enabledModules = AppModel.getInstance().getEnabledModules(new WeakReference<>(EmployeeDetailsActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        employeeSeparationModelList = EmployeeHelperClass.getInstance(EmployeeDetailsActivity.this).
                getResignationHistory(employeeModel.getId());

        if (employeeSeparationModelList != null && employeeSeparationModelList.size() > 0) {
            llRsg.setVisibility(View.VISIBLE);
            resignTitle.setVisibility(View.VISIBLE);
        }



        enableDisableViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (employeeModel != null) {
            try {
                File userImagePath = new File(EmployeeHelperClass.getInstance(this).getUserImagePath(employeeModel.getId()));

                byte[] data = AppModel.getInstance().bitmapToByte(Compressor.getDefault(view.getContext()).compressToBitmap(userImagePath));
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                iv_capture.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void populateFields() {
        if (employeeModel != null) {
            String doj = AppModel.getInstance().convertDatetoFormat(employeeModel
                    .getDate_Of_Joining(), "yyyy-MM-dd", "dd-MMM-yy");
            tv_employeeCode.setText(employeeModel.getEmployee_Code());
            String Cadre = employeeModel.getCADRE();
            if (Cadre == null)
                Cadre = "-";
            tv_designation.setText(employeeModel.getDesignation() + " (" + Cadre + ")");
            if (Cadre.equals("Non-Faculty"))
                tv_designation.setTextColor(getResources().getColor(R.color.orange));
            // Admin Assistant 102
            if (DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getId() == employeeModel.getId()
                    || DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_27_P
                    || DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_101_ST
                    || DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_109_CM
                    || DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_102_AA) {
                btn_update.setVisibility(View.VISIBLE);
            }
            String lwd = "";
            if (employeeModel.getLastWorkingDay() == null)
                lwd = "Current";
            else
                lwd = AppModel.getInstance().convertDatetoFormat(employeeModel.getLastWorkingDay(), "yyyy-MM-dd", "dd-MMM-yy");

            tv_joiningDate.setText(doj + " > " + lwd);
            tv_email.setText(employeeModel.getEmail());


            if (!EmployeeHelperClass.getInstance(view.getContext()).employeeIsInactive(empDetailId)) {
                tv_status.setText("Active");
            } else {
                esm = EmployeeHelperClass.getInstance(view.getContext()).getResignedEmployee(empDetailId);
                esm.setEmp_Resign_Type(EmployeeHelperClass.getInstance(this).getResignTypeByReason(esm.getEmp_SubReasonID()));
                tv_status.setTextColor(getResources().getColor(R.color.red));

                tv_emp_name.setTextColor(getResources().getColor(R.color.gray));
                tv_designation.setTextColor(getResources().getColor(R.color.gray));
                tv_status.setTextColor(getResources().getColor(R.color.gray));
                profileProgressCount.setTextColor(getResources().getColor(R.color.gray));
                profileProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.employee_listing_gray_pp));
                if (esm.getEmp_Resign_Type() == 2) {
                    tv_status.setText("Terminated");
                    btn_resign.setEnabled(false);
                    btn_resign.setText("Already Terminated");
                } else if (esm.getEmp_Resign_Type() == 1) {
                    tv_status.setText("Resigned");
                    btn_resign.setText("Update/Cancel Resignation");
                } else if (esm.getEmp_Resign_Type() == 3) {
                    tv_status.setText("Deceased");
                    btn_resign.setEnabled(false);
                    btn_resign.setText("Marked Deceased");
                } else {
                    tv_status.setText("Type Not Found");
                    btn_resign.setEnabled(false);
                    btn_resign.setText("Type Not Found");
                }
            }

            if (!employeeModel.getJob_Status().equals("Active")) {
                tv_emp_name.setTextColor(getResources().getColor(R.color.gray));
                tv_designation.setTextColor(getResources().getColor(R.color.gray));
                tv_status.setTextColor(getResources().getColor(R.color.gray));
                profileProgressCount.setTextColor(getResources().getColor(R.color.gray));
                profileProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.employee_listing_gray_pp));
                tv_status.setText("Inactive");
            }


            int progress = 0;
//                if(employeeModelList.get(new_position).getMother_Name() != null && !employeeModelList.get(new_position).getMother_Name().trim().equals(""))
//                    progress += 20;
//                if(employeeModelList.get(new_position).getFather_Name() != null && !employeeModelList.get(new_position).getFather_Name().trim().equals(""))
//                    progress += 20;
            if (employeeModel.getMobile_No() != null && !employeeModel.getMobile_No().trim().equals(""))
                progress += 80;
//                if(employeeModel.getNIC_No() != null && !employeeModel.getNIC_No().trim().equals(""))
//                    progress += 20;
            if (employeeModel.getEmail() != null && !employeeModel.getEmail().trim().equals(""))
                progress += 20;

            profileProgressCount.setText(progress + "%");
            profileProgressBar.setProgress(progress);
            if (progress < 50)
                profileProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.employee_profile_progress_red));
            else if (progress < 80)
                profileProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.employee_listing_profile_progress_orange));


            String fullName = employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name();
            tv_emp_name.setText(fullName);
            if (employeeModel.getGender().equals("M"))
                tv_emp_name.setTextColor(getResources().getColor(R.color.blue));
            else if (employeeModel.getGender().equals("F"))
                tv_emp_name.setTextColor(getResources().getColor(R.color.pink));
//            tv_firstName.setText(employeeModel.getFirst_Name());
//            tv_lastName.setText(employeeModel.getLast_Name());
            tv_fatherName.setText(employeeModel.getFather_Name());
            tv_motherName.setText(employeeModel.getMother_Name());
            tv_cnic.setText(employeeModel.getNIC_No());
            tv_MobileNumber.setText(employeeModel.getMobile_No());
            tv_userId.setText(employeeModel.getId() + "");

        }
    }

    private void init(View view) {
        onChangePendingSyncCount = this;
        user_profile = view.findViewById(R.id.user_profile);
        user_profile.setOnClickListener(this);
        iv_capture = view.findViewById(R.id.iv_capture);
//        iv_capture.setOnClickListener(this);
        profileProgressBar = view.findViewById(R.id.profileProgressBar);
        profileProgressCount = view.findViewById(R.id.profileProgressCount);
        tv_userId = view.findViewById(R.id.tv_userId);
        tv_status = view.findViewById(R.id.tv_status);
//        et_MobileNumber = view.findViewById(R.id.et_MobileNumber);
        tv_employeeCode = findViewById(R.id.tv_employeeCode);
        tv_designation = findViewById(R.id.tv_designation);
        tv_joiningDate = findViewById(R.id.tv_joiningDate);
        tv_email = findViewById(R.id.tv_email);
        tv_emp_name = findViewById(R.id.tv_emp_name);
//        tv_firstName = findViewById(R.id.tv_firstName);
//        tv_lastName = findViewById(R.id.tv_lastName);
        tv_fatherName = findViewById(R.id.tv_fatherName);
        tv_motherName = findViewById(R.id.tv_motherName);
        ll_leaves = findViewById(R.id.ll_leaves);
        resignTitle = findViewById(R.id.resignTitle);
        resignTitle.setVisibility(View.GONE);
        tv_cnic = findViewById(R.id.tv_cnic);
        tv_MobileNumber = findViewById(R.id.tv_MobileNumber);
        em = new EmployeeModel();

        tv_noLeaves = findViewById(R.id.tv_noLeaves);
        tv_noQual = findViewById(R.id.tv_noQual);
        tv_noPos = findViewById(R.id.tv_noPosition);
        tv_noRsg = findViewById(R.id.tv_noResignation);

        recyclerView = view.findViewById(R.id.rv_leaves);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        posHSW = view.findViewById(R.id.PosScrollView);
        recyclerViewPosition = view.findViewById(R.id.rv_positionHistory);
        recyclerViewPosition.setNestedScrollingEnabled(false);
        recyclerViewPosition.setHasFixedSize(true);
        recyclerViewPosition.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        qualHSW = view.findViewById(R.id.QualScrollView);
        recyclerViewQualification = view.findViewById(R.id.rv_qualificationHistory);
        recyclerViewQualification.setNestedScrollingEnabled(false);
        recyclerViewQualification.setHasFixedSize(true);
        recyclerViewQualification.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        rsgHSW = view.findViewById(R.id.RsgScrollView);
        recyclerViewResign = view.findViewById(R.id.rv_resignHistory);
        recyclerViewResign.setNestedScrollingEnabled(false);
        recyclerViewResign.setHasFixedSize(true);
        recyclerViewResign.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        btn_update = view.findViewById(R.id.btn_update);
        //btn_leaves = view.findViewById(R.id.btn_leaves);
        btn_resign = view.findViewById(R.id.btn_resign);
        llRsg = view.findViewById(R.id.linearResignation);
        llRsg.setVisibility(View.GONE);
        btn_close = view.findViewById(R.id.btn_close);
        btn_terminate = view.findViewById(R.id.btn_terminate);
        iv_showResignation = view.findViewById(R.id.showResignation);
        iv_showLeave = view.findViewById(R.id.showLeave);
        iv_showEP = view.findViewById(R.id.showEP);
        iv_showPosition = view.findViewById(R.id.showPosition);
        iv_showQualification = view.findViewById(R.id.showQualification);
        iv_addLeaves = view.findViewById(R.id.iv_addLeaves);
        epCV = view.findViewById(R.id.cv1);
        TooltipCompat.setTooltipText(iv_addLeaves, "Add Leaves");

        btn_update.setOnClickListener(this);
//        btn_leaves.setOnClickListener(this);
        btn_resign.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        btn_terminate.setOnClickListener(this);
        iv_showLeave.setOnClickListener(this);
        iv_showEP.setOnClickListener(this);
        iv_showResignation.setOnClickListener(this);
        iv_showPosition.setOnClickListener(this);
        iv_showQualification.setOnClickListener(this);
        iv_addLeaves.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                iv_capture.setImageURI(resultUri);
                ImageCaptured = true;

                Bitmap thumbBitmap;
                String path = resultUri.getPath();
                thumbBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path),100, 100);

                byte[] user_image = AppModel.getInstance().bitmapToByte(thumbBitmap);
                String imagePath = AppModel.getInstance().saveImageToStorage2(user_image, this, "User_Image_", 1, employeeModel.getId());

                updateUserImage(imagePath);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                ShowToast(error.getMessage());
            }
        }
    }

    public void updateUserImage(String userImagePath) {
        UserImageModel userImageModel = new UserImageModel();
        userImageModel.setUser_id(employeeModel.getId());
        userImageModel.setUser_image_path(userImagePath);
        userImageModel.setUploaded_on(null);
        long id = EmployeeHelperClass.getInstance(this).insertOrUpdateUserImage(userImageModel, this);

        if (id > 0) {
            //Important when any change in table call this method
            onChangePendingSyncCount.onChangeRecords();
            ShowToast("Image Updated Successfully");
        }
        else
            ShowToast("Something went wrong");
    }

    private void previewImage(Drawable previewImageFile) {
        ImagePreviewDialog imagePreviewDialog = new ImagePreviewDialog(this, previewImageFile);
        imagePreviewDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_capture:
//                previewImage(user_profile.getBackground());
//                break;

            case R.id.user_profile:

                if (DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getId() == employeeModel.getId() ||
                        DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_27_P ||
                        DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_101_ST) {

                    try {
                        openCameraGalleryDialog("User Profile Image");
                        /*PickImageDialog.build(new PickSetup().setTitle("Profile Image")
                                .setPickTypes(EPickType.CAMERA, EPickType.GALLERY)).show(this).setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {

                                try {
                                    if (r.getError() == null) {
                                        CropImage.activity(r.getUri())
                                                .setGuidelines(CropImageView.Guidelines.ON)
                                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                                .setFixAspectRatio(true)
                                                .start(EmployeeDetailsActivity.this);
                                    } else {
                                        Toast.makeText(EmployeeDetailsActivity.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                    AppModel.getInstance().appendErrorLog(EmployeeDetailsActivity.this,"Error in Imageview:" + e.getMessage());
                                }
                            }
                        });*/
                    } catch (Exception e) {
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(this,"Error in Imageview:" + e.getMessage());
                    }

                } else {
                    previewImage(iv_capture.getDrawable());
                }
                break;

            case R.id.btn_update:


                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.employee_profile_update_dialog, null);
                //cancelReason = dialogView.findViewById(R.id.edt_comment);
                mobileNumber = (EditText) dialogView.findViewById(R.id.et_MobileNumber);
                email = dialogView.findViewById(R.id.et_Email);

                final AlertDialog dialog = new AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setTitle("Edit Profile")
                        //.setMessage("Are you sure you want to cancel the resignation of " +employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name())
                        .setPositiveButton("Update", null) //Set to null. We override the onclick
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                        try {
                            if (employeeModel.getMobile_No() != null) {
                                if (!employeeModel.getMobile_No().isEmpty()) {
                                    mobileNumber.setText(employeeModel.getMobile_No());
                                }
                            }

                            if (employeeModel.getEmail() != null) {
                                if (!employeeModel.getEmail().isEmpty()) {
                                    email.setText(employeeModel.getEmail());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        button.setOnClickListener(new View.OnClickListener() {


                            @Override
                            public void onClick(View view) {

                                em.setId(employeeModel.getId());

                                if (validate()) {
                                    long id = EmployeeHelperClass.getInstance(EmployeeDetailsActivity.this).updateEmployeeDetail(em);
                                    if (id > 0) {
                                        //Important when any change in table call this method
                                        AppModel.getInstance().changeMenuPendingSyncCount(EmployeeDetailsActivity.this, true);

                                        Intent leave_intent = new Intent(getApplicationContext(), EmployeeDetailsActivity.class);
                                        leave_intent.putExtra("empDetailId", empDetailId);
                                        MessageBox("Employee updated successfully", true, leave_intent);

                                    } else {
                                        MessageBox("Something went wrong");
                                    }

                                    dialog.dismiss();

                                }
                            }
                        });
                    }
                });
                dialog.show();
                break;
            case R.id.btn_resign:
                if (enabledModules.isModuleHRResignationEnabled()) {
                    Intent intent = new Intent(EmployeeDetailsActivity.this, EmployeeSeparation.class);
                    intent.putExtra("empDetailId", empDetailId);
                    intent.putExtra("schoolId", schoolId);
                    startActivity(intent);
                    finish();
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(EmployeeDetailsActivity.this),
                            "Info!", "Resignation Module is disabled" + globalMessage);
                }
                break;
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_terminate:
                if (enabledModules.isModuleHRTerminationEnabled()) {
                    Intent terminate_intent = new Intent(EmployeeDetailsActivity.this, ApprovalList_Terminate.class);
                    terminate_intent.putExtra("empDetailId", empDetailId);
                    terminate_intent.putExtra("schoolId", schoolId);
                    startActivity(terminate_intent);
                    finish();
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(EmployeeDetailsActivity.this),
                            "Info!", "Termination Module is disabled" + globalMessage);
                }
                break;

            case R.id.showEP:
                if (!isShowEPClicked) {
                    if (isShowLeaveClicked)
                        hideLeaveView();
                    if (isShowPositionClicked)
                        hidePositionView();
                    if (isShowQualificationClicked)
                        hideQualificationView();
                    if (isShowResignationClicked)
                        hideResignationView();

                    isShowEPClicked = true;
                    epCV.setVisibility(View.VISIBLE);
                    epCV.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_fall_down));
                    iv_showEP.animate().rotation(iv_showEP.getRotation() - 180).start();
                } else
                    hideEPView();
                break;

            case R.id.showLeave:
                if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                    List<EmployeeLeaveModel> leaveModelList = EmployeeHelperClass.getInstance(EmployeeDetailsActivity.this).getLeavesOfEmployee(employeeModel, schoolId);
                    if (!isShowLeaveClicked) {
                        if (isShowEPClicked)
                            hideEPView();
                        if (isShowPositionClicked)
                            hidePositionView();
                        if (isShowQualificationClicked)
                            hideQualificationView();
                        if (isShowResignationClicked)
                            hideResignationView();

                        isShowLeaveClicked = true;
                        iv_showLeave.animate().rotation(iv_showLeave.getRotation() - 180).start();
                        if (leaveModelList != null && leaveModelList.size() > 0) {
                            /*leaveAdapter = new LeavesAdapter(EmployeeDetailsActivity.this, leaveModelList);
                            recyclerView.setAdapter(leaveAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_fall_down));*/
                        } else
                            tv_noLeaves.setVisibility(View.VISIBLE);
                    } else
                        hideLeaveView();
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(EmployeeDetailsActivity.this),
                            "Info!", "Leave Module is disabled" + globalMessage);
                }
                break;
            case R.id.showPosition:
                List<EmployeePositionModel> employeePositionModelList = EmployeeHelperClass
                        .getInstance(EmployeeDetailsActivity.this).getPositionHistory(employeeModel);
                if (!isShowPositionClicked) {

                    if (isShowEPClicked)
                        hideEPView();
                    if (isShowLeaveClicked)
                        hideLeaveView();
                    if (isShowQualificationClicked)
                        hideQualificationView();
                    if (isShowResignationClicked)
                        hideResignationView();

                    isShowPositionClicked = true;
                    iv_showPosition.animate().rotation(iv_showPosition.getRotation() - 180).start();

                    if (employeePositionModelList != null && employeePositionModelList.size() > 0) {
                        positionHistoryAdapter = new PositionHistoryAdapter(EmployeeDetailsActivity.this, employeePositionModelList);
                        recyclerViewPosition.setAdapter(positionHistoryAdapter);
                        recyclerViewPosition.setVisibility(View.VISIBLE);
                        posHSW.setVisibility(View.VISIBLE);
                        recyclerViewPosition.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_fall_down));
                    } else
                        tv_noPos.setVisibility(View.VISIBLE);
                } else
                    hidePositionView();
                break;
            case R.id.showQualification:
                List<EmployeeQualificationDetailModel> qualificationDetailModelList = EmployeeHelperClass.getInstance(EmployeeDetailsActivity.this).
                        getQualificationHistory(employeeModel.getId());

                if (!isShowQualificationClicked) {
                    if (isShowEPClicked)
                        hideEPView();
                    if (isShowLeaveClicked)
                        hideLeaveView();
                    if (isShowPositionClicked)
                        hidePositionView();
                    if (isShowResignationClicked)
                        hideResignationView();

                    isShowQualificationClicked = true;
                    iv_showQualification.animate().rotation(iv_showQualification.getRotation() - 180).start();

                    if (qualificationDetailModelList != null && qualificationDetailModelList.size() > 0) {
                        qualificationHistoryAdapter = new QualificationHistoryAdapter(EmployeeDetailsActivity.this, qualificationDetailModelList);
                        recyclerViewQualification.setAdapter(qualificationHistoryAdapter);
                        recyclerViewQualification.setVisibility(View.VISIBLE);
                        qualHSW.setVisibility(View.VISIBLE);
                        recyclerViewQualification.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_fall_down));
                    } else
                        tv_noQual.setVisibility(View.VISIBLE);
                } else
                    hideQualificationView();
                break;


            case R.id.showResignation:
                if (!isShowResignationClicked) {

                    if (isShowEPClicked)
                        hideEPView();
                    if (isShowLeaveClicked)
                        hideLeaveView();
                    if (isShowPositionClicked)
                        hidePositionView();
                    if (isShowQualificationClicked)
                        hideQualificationView();

                    isShowResignationClicked = true;
                    iv_showResignation.animate().rotation(iv_showResignation.getRotation() - 180).start();

                    resignationHistoryAdapter = new ResignationHistoryAdapter(EmployeeDetailsActivity.this, employeeSeparationModelList, employeeModel);
                    recyclerViewResign.setAdapter(resignationHistoryAdapter);
                    recyclerViewResign.setVisibility(View.VISIBLE);
                    rsgHSW.setVisibility(View.VISIBLE);
                    recyclerViewResign.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_fall_down));

                } else
                    hideResignationView();
                break;

            /*case R.id.iv_addLeaves:
                if (enabledModules.isModuleHRAttendanceAndLeavesEnabled()) {
                    Intent leave_intent = new Intent(getApplicationContext(), EmployeeLeaveActivity.class);
                    leave_intent.putExtra("empDetailId", empDetailId);
                    startActivity(leave_intent);
                    finish();
                } else {
                    AppModel.getInstance().showMessage(new WeakReference<>(EmployeeDetailsActivity.this),
                            "Info!", "Leave Module is disabled" + globalMessage);
                }
                break;*/
        }
    }

    private void hideEPView() {
        isShowEPClicked = false;
        epCV.setVisibility(View.GONE);
        iv_showEP.animate().rotation(iv_showEP.getRotation() - 180).start();
    }

    private void hideLeaveView() {
        recyclerView.setVisibility(View.GONE);
        tv_noLeaves.setVisibility(View.GONE);
        iv_showLeave.animate().rotation(iv_showLeave.getRotation() - 180).start();
        isShowLeaveClicked = false;
    }

    private void hideResignationView() {
        iv_showResignation.animate().rotation(iv_showResignation.getRotation() - 180).start();
        recyclerViewResign.setVisibility(View.GONE);
        rsgHSW.setVisibility(View.GONE);
        tv_noRsg.setVisibility(View.GONE);
        isShowResignationClicked = false;
    }

    private void hideQualificationView() {
        iv_showQualification.animate().rotation(iv_showQualification.getRotation() - 180).start();
        recyclerViewQualification.setVisibility(View.GONE);
        qualHSW.setVisibility(View.GONE);
        tv_noQual.setVisibility(View.GONE);
        isShowQualificationClicked = false;
    }

    private void hidePositionView() {
        iv_showPosition.animate().rotation(iv_showPosition.getRotation() - 180).start();
        recyclerViewPosition.setVisibility(View.GONE);
        posHSW.setVisibility(View.GONE);
        tv_noPos.setVisibility(View.GONE);
        isShowPositionClicked = false;
    }

    private boolean validate() {
        int allOk = 0;

        if (!mobileNumber.getText().toString().trim().isEmpty()) {
            String mobNum = mobileNumber.getText().toString();
            if (mobNum.length() == 11 && mobNum.substring(0, 2).equals("03")) {
                em.setMobile_No(mobNum);
            } else {
                mobileNumber.setError("Please Enter Valid Mobile Number");
                allOk++;
            }
        } else {
            em.setMobile_No("");
        }

        if (!email.getText().toString().trim().isEmpty()) {
            String strEmail = email.getText().toString();
            if (strEmail.length() >= 5 && strEmail.contains(".") && strEmail.contains("@")) {
                em.setEmail(strEmail);
            } else {
                email.setError("Please Enter Valid Email Address");
                allOk++;
            }
        } else {
            em.setEmail("");
        }

        return allOk == 0;
    }

    private void enableDisableViews() {
        if (schoolModel != null) {
            List<String> allowedModules = null;
            if (schoolModel.getAllowedModule_App() != null) {
                allowedModules = Arrays.asList(schoolModel.getAllowedModule_App().split(","));
                allowedModules = Arrays.asList(schoolModel.getAllowedModule_App().split(","));
            }
            if (allowedModules != null && allowedModules.contains(AppConstants.HRAttendanceAndLeavesModuleValue)) {
                showOrHideLeaveButton();
                iv_showLeave.setEnabled(true);
                iv_showLeave.setClickable(true);
                ll_leaves.setVisibility(View.VISIBLE);
            } else {
                showOrHideLeaveButton();
                iv_showLeave.setEnabled(false);
                iv_showLeave.setClickable(false);
                ll_leaves.setVisibility(View.GONE);
            }


            if(allowedModules != null && allowedModules.contains(AppConstants.HRResignationModuleValue) &&
                    DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_101_ST &&
                    (employeeModel.getDesignation().equalsIgnoreCase("SP") || employeeModel.getDesignation().equalsIgnoreCase("PP"))){ //Senior teacher cant make resignation for principal
                btn_resign.setVisibility(View.GONE);
            }
            else if (allowedModules != null && allowedModules.contains(AppConstants.HRResignationModuleValue) &&
                    DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getId() != employeeModel.getId() &&
                    (DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_7_AEM ||
                                    DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_27_P ||
                                    DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_101_ST ||
                                    DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_112_ ||
                                    DatabaseHelper.getInstance(EmployeeDetailsActivity.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_75_)) {
                btn_resign.setVisibility(View.VISIBLE);
            } else {
                btn_resign.setVisibility(View.GONE);
            }


            if (allowedModules != null && allowedModules.contains(AppConstants.HRTerminationModuleValue)) {
//                btn_terminate.setVisibility(View.VISIBLE);
            } else {
//                btn_terminate.setVisibility(View.GONE);
            }
        }
    }

    private void showOrHideLeaveButton() {
        int roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        if (roleID == AppConstants.roleId_103_V) {
            iv_addLeaves.setVisibility(View.INVISIBLE);
        } else {
            iv_addLeaves.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onChangeRecords() {
        super.calPendingRecords(AppModel.getInstance().getSpinnerSelectedSchool(EmployeeDetailsActivity.this));
        super.autoSyncStartWhenUploadIsPendingInQueue();
    }


    void openCameraGalleryDialog(String title){
        try {
            PickImageDialog dialog = PickImageDialog.build(new PickSetup().setTitle(title)
                    .setPickTypes(EPickType.CAMERA, EPickType.GALLERY));

            dialog.setOnClick(new IPickClick() {
                @Override
                public void onGalleryClick() {
                    dialog.onGalleryClick();
                }

                @Override
                public void onCameraClick() {
                    if (dialog.isVisible()) dialog.dismiss();
                    startActivity(new Intent(EmployeeDetailsActivity.this, CameraViewActivity.class).putExtra("empId", employeeModel.getId()));
                }
            }).setOnPickResult(new IPickResult() {
                @Override
                public void onPickResult(PickResult r) {
                    if (r.getError() == null) {
                        CropImage.activity(r.getUri())
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setFixAspectRatio(true)
                                .start(EmployeeDetailsActivity.this);
                    } else {
                        Toast.makeText(EmployeeDetailsActivity.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).show(this);


        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(this,"Error in Imageview:" + e.getMessage());
        }
    }
}

