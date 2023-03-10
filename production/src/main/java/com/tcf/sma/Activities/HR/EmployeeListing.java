package com.tcf.sma.Activities.HR;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.HR.EmployeeListingAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeDesignationModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.UserImageModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;
import com.tcf.sma.utils.ImageCompression;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeListing extends DrawerActivity implements AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    public TextView tv_total_employees;
    private View view;
    private Spinner spinner_SelectSchool, spinner_SelectDesignation;
    private RecyclerView rv_empDetail;
    private RadioGroup rg_cadre, rg_status;
    private Button btn_Search;
    private LinearLayout llResults;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private List<SchoolModel> schoolModels;
    private ArrayAdapter<EmployeeDesignationModel> DesignationAdapter;
    private List<EmployeeDesignationModel> designationsList;
    private EmployeeListingAdapter employeeEmployeeListingAdapter;
    private List<EmployeeModel> employeeModels = new ArrayList<>();
    private int schoolId = 0;
    private String cadre = "";
    private String status = "";
    private String designation = "";
    boolean fromDaashboard = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.employee_listing);

        view = setActivityLayout(this, R.layout.employee_listing);
        setToolbar("View Employee", this, false);
        init(view);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        rv_empDetail.setVisibility(View.GONE);
//        llResults.setVisibility(View.GONE);
        if (employeeEmployeeListingAdapter != null) {
            employeeModels = EmployeeHelperClass.getInstance(EmployeeListing.this).getEmployees(schoolId, designation, cadre, status);
            setAdapter();
        }
    }

    private void setCollapsing(boolean isCollapsable) {
        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        if (isCollapsable)
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL); // list other flags here by |
        else
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        collapsingToolbar.setLayoutParams(params);
    }

    private void init(View view) {
        // rv_empDetail = (RecyclerView)view.findViewById(R.id.rv_empDetail);
        btn_Search = view.findViewById(R.id.btn_Search);
        btn_Search.setOnClickListener(this);

        llResults = view.findViewById(R.id.llResults);
        tv_total_employees = view.findViewById(R.id.tv_total_employees);

        rv_empDetail = view.findViewById(R.id.rv_empDetail);

        spinner_SelectSchool = view.findViewById(R.id.spinner_SelectSchool);
        spinner_SelectSchool.setOnItemSelectedListener(this);
        spinner_SelectDesignation = view.findViewById(R.id.spinner_SelectDesignation);
        spinner_SelectDesignation.setOnItemSelectedListener(this);

//        rv_empDetail.setLayoutManager(new LinearLayoutManager(this));
        rv_empDetail.setNestedScrollingEnabled(false);
        rv_empDetail.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rv_empDetail.setLayoutManager(linearLayoutManager);


        rg_cadre = view.findViewById(R.id.rg_cadre);
        rg_cadre.setOnCheckedChangeListener(this);
        rg_status = view.findViewById(R.id.rg_status);
        rg_status.setOnCheckedChangeListener(this);

        if(getIntent().hasExtra("designation"))
            fromDaashboard = true;

        populateSpinners();
        setCollapsing(false);


//        populateDesignationSpinner();
    }

    private void populateSpinners() {

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForEmployeeListing();
        if (schoolModels != null && schoolModels.size() > 1)
            schoolModels.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SelectSchool.setAdapter(SchoolAdapter);

        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1) {
            //If select school with id 0 is selected show any school by default
            if (schoolModels != null && schoolModels.size() > 1 &&
                    schoolModels.get(indexOfSelectedSchool).getName().equals(getResources().getString(R.string.select_school))){
                spinner_SelectSchool.setSelection(indexOfSelectedSchool + 1);
            }else {
                spinner_SelectSchool.setSelection(indexOfSelectedSchool);
            }
        }

        if(DatabaseHelper.getInstance(EmployeeListing.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_103_V ||
                DatabaseHelper.getInstance(EmployeeListing.this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_7_AEM) {
            spinner_SelectSchool.setEnabled(false);
        }

    }

    private void populateDesignationSpinner() {
//        if (schoolId <= 0){
//            schoolId = ((SchoolModel) spinner_SelectSchool.getSelectedItem()).getId();
//        }

        designationsList = EmployeeHelperClass.getInstance(this).getDesignations(schoolId);
        designationsList.add(0, new EmployeeDesignationModel(0, getString(R.string.select_designation)));
        if (designationsList.size() > 1) {
            designationsList.add(designationsList.size(), new EmployeeDesignationModel(designationsList.size(), "All"));
        }

        DesignationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, designationsList);
        DesignationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SelectDesignation.setAdapter(DesignationAdapter);

        setDesigByDefaultValue();
    }

    private void setDesigByDefaultValue() {
        String designation = "All";
        if(fromDaashboard){
            designation = getIntent().getStringExtra("designation");
            fromDaashboard = false;
        }
        if (designationsList.size() > 1) {
            spinner_SelectDesignation.setSelection(designationsList.size());
            for (EmployeeDesignationModel model : designationsList) {
                if (model.getDesignation_Name().equalsIgnoreCase(designation)) {
                    spinner_SelectDesignation.setSelection(designationsList.indexOf(model));
                    break;
                }
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.spinner_SelectSchool) {
            schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
            AppModel.getInstance().setSpinnerSelectedSchool(this,
                    schoolId);

            populateDesignationSpinner();
        } else if (adapterView.getId() == R.id.spinner_SelectDesignation) {
            designation = ((EmployeeDesignationModel) adapterView.getItemAtPosition(position)).getDesignation_Name();
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_faculty) {
            cadre = "Faculty";
        } else if (checkedId == R.id.rb_non_faculty) {
            cadre = "Non-Faculty";
        } else if (checkedId == R.id.rb_cadre_all) {
            cadre = "";
        } else if (checkedId == R.id.rb_active) {
            status = "Active";
        } else if (checkedId == R.id.rb_resign) {
            status = "Resigned";
        } else if (checkedId == R.id.rb_status_all) {
            status = "";
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Search) {

            if (spinner_SelectSchool.getSelectedItem().toString().equals(getString(R.string.select_school))) {
                MessageBox("Please select school!");
                clearList();
            } else if (spinner_SelectDesignation.getSelectedItem().toString().equals(getString(R.string.select_designation))) {
                MessageBox("Please select designation!");
                clearList();
            } else {
                employeeModels = EmployeeHelperClass.getInstance(EmployeeListing.this).getEmployees(schoolId, designation, cadre, status);
                setAdapter();
            }
        }
    }

    private void setAdapter() {
        if (employeeModels != null && employeeModels.size() > 0) {
//            rv_empDetail.setVisibility(View.VISIBLE);
            llResults.setVisibility(View.VISIBLE);
            employeeEmployeeListingAdapter = new EmployeeListingAdapter(EmployeeListing.this, schoolId, employeeModels);
            tv_total_employees.setText(String.valueOf(employeeModels.size()));
            rv_empDetail.setAdapter(employeeEmployeeListingAdapter);
            setCollapsing(true);
        } else {
            MessageBox("No records found!");
//            rv_empDetail.setVisibility(View.GONE);
            llResults.setVisibility(View.GONE);
            setCollapsing(false);
        }
    }

    private void clearList() {
        if (employeeEmployeeListingAdapter != null) {
            employeeModels = null;
            llResults.setVisibility(View.GONE);
        }
        setCollapsing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //iv_capture.setImageURI(resultUri);
                //ImageCaptured = true;

                ImageCompression imageCompression = new ImageCompression(this);

                String path = resultUri.getPath();

                imageCompression.execute(path);

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] user_image = AppModel.getInstance().bitmapToByte(bitmap);
                String imagePath = AppModel.getInstance().saveImageToStorage2(user_image, this, "User_Image_" + AppModel.getInstance().getCurrentDateTime("dd_MMM_yyyy_hh_mm_ss"), 1, employeeModels.get(EmployeeListingAdapter.pos).getId());

                UserImageModel userImageModel = new UserImageModel();
                userImageModel.setUser_id(employeeModels.get(EmployeeListingAdapter.pos).getId());
                userImageModel.setUser_image_path(imagePath);
                userImageModel.setUploaded_on(null);
                long id = EmployeeHelperClass.getInstance(this).insertOrUpdateUserImage(userImageModel, this);
                if(id > 0) {
                    ShowToast("Image Updated Successfully");
                    employeeEmployeeListingAdapter.notifyItemChanged(EmployeeListingAdapter.pos);
                }
                else {
                    ShowToast("Something went wrong");
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                ShowToast(error.getMessage());
            }
        }
    }
}

