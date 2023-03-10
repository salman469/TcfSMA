package com.tcf.sma.Activities;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.squareup.picasso.Picasso;
import com.tcf.sma.DataSync.DataSync;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.RetrofitModels.StudentTransferUploadModel;
import com.tcf.sma.Models.SchoolClassesModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentTransferModel;
import com.tcf.sma.Models.SyncProgress.AppSyncStatusModel;
import com.tcf.sma.R;
import com.tcf.sma.Retrofit.ApiClient;
import com.tcf.sma.Retrofit.ApiInterface;
import com.tcf.sma.SyncClasses.SyncUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class StudentTransferActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public Spinner SchoolSpinner, ClassSectionSpinner;
    public ClassSectionModel classSectionModel;
    private View view;
    private TextView tv_stdId, tv_dob;
    private EditText et_GrNo, et_Name, et_monthly_fees, et_fatherName, et_fathercnic;
    private ImageView iv_stdImage;
    private Button btn_submit_new_student;
    private LinearLayout ll_monthly_fees;
    private StudentTransferModel sm;
    private int SchoolSpinnerValue = 0, SchoolClass_ID = 0;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private ArrayAdapter<ClassSectionModel> ClassSectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_transfer);
        view = setActivityLayout(this, R.layout.activity_student_transfer);
        setToolbar(getString(R.string.student_transfer_text), this, false);

        init(view);
        working();
    }

    private void init(View view) {
        tv_stdId = view.findViewById(R.id.tv_stdId);
        et_GrNo = view.findViewById(R.id.et_GrNo);
        iv_stdImage = view.findViewById(R.id.iv_stdImage);
        et_Name = view.findViewById(R.id.et_Name);
        et_monthly_fees = view.findViewById(R.id.et_monthly_fees);
        ll_monthly_fees = view.findViewById(R.id.ll_monthly_fees);
        tv_dob = view.findViewById(R.id.tv_dob);
        tv_dob.setOnClickListener(this);

        et_fatherName = view.findViewById(R.id.et_fatherName);
        et_fathercnic = view.findViewById(R.id.et_fathercnic);

        btn_submit_new_student = view.findViewById(R.id.btn_submit_new_student);
        btn_submit_new_student.setOnClickListener(this);

        SchoolSpinner = view.findViewById(R.id.spn_school);
        ClassSectionSpinner = view.findViewById(R.id.spn_class_section_name);

        initSpinners();

    }

    private void initSpinners() {

        List<SchoolModel> smList = AppModel.getInstance().getSchoolsForLoggedInUser(this);

        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, smList);
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchoolSpinner.setAdapter(SchoolAdapter);

        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(smList, this);
        if (indexOfSelectedSchool > -1)
            SchoolSpinner.setSelection(indexOfSelectedSchool);

        SchoolSpinner.setOnItemSelectedListener(this);
        SchoolSpinnerValue = ((SchoolModel) SchoolSpinner.getSelectedItem()).getId();

        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
        ClassSectionSpinner.setOnItemSelectedListener(this);

        /*********************Muhammad Salman Saleem****************************/
        if(isFlagShipSchool()){
            setEditTextMaxLength(et_GrNo,5);
        }
        /*********************Muhammad Salman Saleem***************************/

    }

    private void working() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("studentModel"))
            sm = (StudentTransferModel) intent.getSerializableExtra("studentModel");

        if (sm != null) {

            if (DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)) {
                String highestGrNo = DatabaseHelper.getInstance(StudentTransferActivity.this).getHighestGrNO(SchoolSpinnerValue + "");
                et_GrNo.setText(highestGrNo);
            }

            tv_stdId.setText(sm.getStd_Id() + "");
            et_Name.setText(sm.getStd_Name());

            if (SchoolSpinnerValue > 0) {
                String allowedFinanceModule = ((SchoolModel) SchoolSpinner.getSelectedItem()).getAllowedModule_App();
                if ((allowedFinanceModule != null && Arrays.asList(allowedFinanceModule.split(",")).contains(AppConstants.FinanceModuleValue))
                        || DatabaseHelper.getInstance(StudentTransferActivity.this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)) {
                    ll_monthly_fees.setVisibility(View.VISIBLE);
                } else {
                    ll_monthly_fees.setVisibility(View.GONE);
                }
            }
//            et_monthly_fees.setText(Math.round(sm.getActualFees())+"");
            String DateOfBirth = "";
            if (sm.getDob() != null) {
                DateOfBirth = AppModel.getInstance().convertDatetoFormat(sm.getDob(), "dd/MM/yy", "dd-MMM-yyyy");
            }

            tv_dob.setText(DateOfBirth);
            et_fatherName.setText(sm.getFathersName());
            et_fathercnic.setText(sm.getFatherNic());

            try {
                if (sm.getPictureName() != null && !sm.getPictureName().isEmpty()) {
                    String url = AppModel.getInstance().readFromSharedPreferences(view.getContext(), AppConstants.imagebaseurlkey) + sm.getPictureName();
                    Picasso.with(view.getContext()).load(url).noFade().into(iv_stdImage);
//                std_img.setImageBitmap(AppModel.getInstance().setImage("P", sm.getPictureName(), getActivity(), Integer.parseInt(sm.getGrNo())));
                } else {
                    iv_stdImage.setImageDrawable(getResources().getDrawable(R.mipmap.profile_pic));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//            try {
//                iv_stdImage.setImageBitmap(AppModel.getInstance().setImage("P", sm.getPictureName(), this, Integer.parseInt(sm.getGrNo())));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//
//            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        switch (adapterView.getId()) {
            case R.id.spn_school:
                SchoolModel model = (SchoolModel) adapterView.getItemAtPosition(position);
                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();

                if (DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)) {
                    String highestGrNo = DatabaseHelper.getInstance(StudentTransferActivity.this).getHighestGrNO(SchoolSpinnerValue + "");
                    et_GrNo.setText(highestGrNo);
                }

                classSectionModel = new ClassSectionModel();
                classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + ""));
                classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));

                ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
                ClassSectionSpinner.setAdapter(ClassSectionAdapter);

                if (SchoolSpinnerValue > 0) {
                    String allowedFinanceModule = ((SchoolModel) adapterView.getItemAtPosition(position)).getAllowedModule_App();
                    if ((allowedFinanceModule != null && Arrays.asList(allowedFinanceModule.split(",")).contains(AppConstants.FinanceModuleValue))
                            || DatabaseHelper.getInstance(StudentTransferActivity.this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)) {
                        ll_monthly_fees.setVisibility(View.VISIBLE);
                    } else {
                        ll_monthly_fees.setVisibility(View.GONE);
                    }
                }

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, SchoolSpinnerValue);
                if (errorModel != null) {
                    MessageBox(errorModel.getMessage());
                }
                break;
            case R.id.spn_class_section_name:
                SchoolClass_ID = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getSchoolClassId();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit_new_student) {
            if (SchoolSpinnerValue == 0) {
                Toast.makeText(StudentTransferActivity.this, "Please select school", Toast.LENGTH_SHORT).show();
            } else if (SchoolClass_ID == 0) {
                Toast.makeText(StudentTransferActivity.this, "Please select class and section", Toast.LENGTH_SHORT).show();
            } else {
                if (validate()) {
                    String schoolName = ((SchoolModel) SchoolSpinner.getSelectedItem()).getName();
                    String classSectionName = ((ClassSectionModel) ClassSectionSpinner.getSelectedItem()).getClass_section_name();
                    String message = "School: " + SchoolSpinnerValue + "-" + schoolName
                            + "\nClass-Section: " + classSectionName
                            + "\nStudent Name: " + et_Name.getText().toString()
                            + "\nStudent GR No: " + et_GrNo.getText().toString();

                    String allowedFinanceModule = ((SchoolModel) SchoolSpinner.getSelectedItem()).getAllowedModule_App();
                    if ((allowedFinanceModule != null && Arrays.asList(allowedFinanceModule.split(",")).contains(AppConstants.FinanceModuleValue))
                            || DatabaseHelper.getInstance(StudentTransferActivity.this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)) {
                        message += "\nStudent Monthly Fee: " + et_monthly_fees.getText().toString();
                    }


                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    TextView title = new TextView(this);
//              You Can Customise your Title here
                    title.setText("Alert");
                    title.setBackgroundColor(getResources().getColor(R.color.app_green));
                    title.setPadding(10, 10, 10, 10);
                    title.setGravity(Gravity.CENTER);
                    title.setTextSize(20);
                    title.setTextColor(Color.WHITE);

                    builder.setCustomTitle(title);
//                builder.setTitle("Alert");

                    builder.setMessage(message + "\n\nAre you sure you want to Transfer " + et_Name.getText().toString()
                            + " from " + sm.getSchool_name() + " to " + schoolName + "  and Class " + classSectionName + ".");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();

                        if (!AppModel.getInstance().isConnectedToInternet(StudentTransferActivity.this)) {
                            MessageBox("No internet connectivity!\n Please connect to internet");
                        } else {
                            enableTransferSubmitButton(false);
                            new Thread(this::transferStudent).start();
                        }
                    });
                    builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                    builder.show();
                }
            }
        } else if (v.getId() == R.id.tv_dob) {
            DatePicker(tv_dob, this);
        }
    }

    private boolean validate() {
        int allOK = 0;
        int classId = ((ClassSectionModel) ClassSectionSpinner.getSelectedItem()).getClassId();
        int section = ((ClassSectionModel) ClassSectionSpinner.getSelectedItem()).getSectionId();

        //**************************Written by Muhammad Salman Saleem (TCF HO)-(PS-Validation)**//
        String ClassName = "";

        String classSectionName = ((ClassSectionModel) ClassSectionSpinner.getSelectedItem()).getClass_section_name();
        try {
            String split[] = classSectionName.split(" ");
            ClassName = split[0];
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }//Written by Muhammad Salman Saleem (TCF HO)**************************//


        SchoolClassesModel scm = DatabaseHelper.getInstance(this).getSchoolClassByIds(SchoolSpinnerValue, classId, section);
        int StudentCount = DatabaseHelper.getInstance(current_activity).getStudentCount(scm.getId());
        int capacity = DatabaseHelper.getInstance(current_activity).getMaxCapacityFromSchoolClass(scm.getSchoolId(), scm.getId());

        if (et_Name.getText().toString().equals("")) {
            et_Name.setError("Required");
            allOK++;
        } else {
            et_Name.setError(null);
        }

        if (et_GrNo.getText().toString().equals("")) {
            et_GrNo.setError("Required");
            allOK++;
        } else {
            //PS-Validation by Muhammad Salman Saleem ----------------
            long id = DatabaseHelper.getInstance(StudentTransferActivity.this).FindGRNOSTUDENTPROFILE(et_GrNo.getText().toString(), String.valueOf(SchoolSpinnerValue));

            if(id == 1){
                Toast.makeText(StudentTransferActivity.this,"GR No. " + et_GrNo.getText().toString() + " already exist!",Toast.LENGTH_LONG).show();
            }else{
                et_GrNo.setError(null);
            }
            //PS-Validation by Muhammad Salman Saleem ----------------
        }

        if (tv_dob.getText().toString().equals("")) {
            ShowToast("Please select date of birth!");
            allOK++;
        }else if (!isFlagShipSchool()) {//Written by Muhammad Salman Saleem (TCF HO)
            if(!isValidAgeForClass(this, tv_dob.getText().toString(),ClassName)) {
                allOK++;
            }
        }
        //PS-Validation by Muhammad Salman Saleem ----------------


        String date = tv_dob.getText().toString().trim();
        date = AppModel.getInstance().convertDatetoFormat(date, "dd-MMM-yy", "yyyy-MM-dd");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = dateFormat.parse(date);
            Date date2 = dateFormat.parse(AppModel.getInstance().getDate());

            if (DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)
                    && DatabaseHelper.getInstance(this).isNineClass(SchoolSpinnerValue, classId, section)
                    && calcNumberOfYears(date1, date2) < 13) {
                Toast.makeText(current_activity, "Student age must be equal to or greater than 13 years.", Toast.LENGTH_LONG).show();
                allOK++;
            } else if (!(calcNumberOfYears(date1, date2) >= 3 && calcNumberOfYears(date1, date2) <= 25)) {
                Toast.makeText(current_activity, "Student age must be between 3 to 25 years.", Toast.LENGTH_LONG).show();
//                    et_dob.setError("Student age must be between 3 to 25 years.");
                allOK++;
            }

//            if (!(calcNumberOfYears(date1, date2) >= 3 && calcNumberOfYears(date1, date2) <= 25)) {
//                ShowToast("Student age must be between 3 to 25 years.");
//                allOK++;
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (et_fatherName.getText().toString().equals("")) {
            et_fatherName.setError("Required");
            allOK++;
        } else {
            et_fatherName.setError(null);
        }

        if (et_fathercnic.getText().toString().equals("")) {
            et_fathercnic.setError("Required");
            allOK++;
        } else {
            et_fathercnic.setError(null);
        }


        if (SchoolClass_ID == scm.getId()) {
            if (StudentCount >= capacity) {
                ShowToast("Cannot Edit student in this class because it reaches Max Capacity:" + capacity + " and No. of Students are:" + StudentCount);
                allOK++;
            }
        }

        String allowedFinanceModule = ((SchoolModel) SchoolSpinner.getSelectedItem()).getAllowedModule_App();
        if ((allowedFinanceModule != null && Arrays.asList(allowedFinanceModule.split(",")).contains(AppConstants.FinanceModuleValue))
                || DatabaseHelper.getInstance(StudentTransferActivity.this).isRegionIsInFlagShipSchool(SchoolSpinnerValue)) {
            if (et_monthly_fees.getText().toString().isEmpty()) {
                et_monthly_fees.setError("Please enter monthly fees");
                allOK++;
            } else {
                int monthlyFee = Integer.parseInt(et_monthly_fees.getText().toString().trim());
                if (monthlyFee < 10) {
                    et_monthly_fees.setError("Tuition fees should be greater than or equals to 10");
                    allOK++;
                } else if (monthlyFee > 1500) {
                    et_monthly_fees.setError("Tuition fees should be less than or equals to 1500");
                    allOK++;
                }
            }
        }

        return allOK == 0;
    }

    private int calcNumberOfYears(Date fromdate, Date toDate) {
        Calendar a = getCalendar(fromdate);
        Calendar b = getCalendar(toDate);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    //**********************************Written by Muhammad Salman Saleem (PS Validation)***************************//
    private int getAgeOfStudent(Calendar dob){
        Calendar now = Calendar.getInstance();
        int years = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);//2023-2012=11   //02-03-2012
        int months = ((now.get(Calendar.MONTH)) - (dob.get(Calendar.MONTH)));// 2 - 2 =0
        int days = now.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);//03-02=1

        System.out.println("Current Year: "+now.get(Calendar.YEAR));
        System.out.println("Current Month: "+now.get(Calendar.MONTH));
        System.out.println("Current Day: "+now.get(Calendar.DAY_OF_MONTH));

        System.out.println("db: "+ YEAR);
        System.out.println("years: "+years);
        System.out.println("months: "+months);
        System.out.println("days: "+days);

        // Adjust age based on difference in months and days
        if(months==0 && days<0){
            years--;
        } else if( months>0 || (months==0 && days>0) ){
            years++;
        } else if (months < 0 && days < 0) {
            years--;
        }

//prints the age
        System.out.println("Your are in age: "+years);
        return years;
    }

//    public boolean isValidAgeForClass(Context context, String dobString, String className) {//"dd/MM/yy", "dd-MMM-yy"
//        try {
//            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
//            Calendar dob = Calendar.getInstance();
//
//            try {
//                dob.setTime(format.parse(dobString));
//            } catch (ParseException e) {
//                e.printStackTrace();
//                return false;
//            }
//
//            //int age = getAge(dob);
//            int age = getAgeOfStudent(dob);
//
//            switch (className) {
//                case "Nursery":
//                    if (age >= 4 && age <= 6) {
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 4 and 6 years for Nursery class", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                case "KG":
//                    if (age >= 4 && age <= 7) {
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 4 and 7 years for KG class", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                case "Class-1":
//                    if (age >= 5 && age <= 8) {
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 5 and 8 years for Class-1", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                case "Class-2":
//                    if (age >= 6 && age <= 9) {
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 6 and 9 years for Class-2", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                case "Class-3":
//                    if (age >= 7 && age <= 10) {
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 7 and 10 years for Class-3", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                case "Class-4":
//                    if (age >= 8 && age <= 11) { //7
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 8 and 11 years for Class-4", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                case "Class-5":
//                    if (age >= 9 && age <= 12) {//10,11
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 9 and 12 years for Class-5", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                case "Class-6":
//                    if (age >= 10 && age <= 13) {
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 10 and 13 years for Class-6", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                case "Class-7":
//                    if (age >= 11 && age <= 13) {
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 11 and 13 years for Class-7", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                case "Class-8":
//                    if (age >= 12 && age <= 14) {
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 12 and 14 years for Class-8", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                case "Class-9":
//                    if (age >= 13 && age <= 15) {
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 13 and 15 years for Class-9", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                case "Class-10":
//                    if (age >= 14 && age <= 16) {
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 14 and 16 years for Class-10", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                default:
//                    Toast.makeText(context, "Invalid class name", Toast.LENGTH_LONG).show();
//                    return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public boolean isValidAgeForClass(Context context, String dobString, String className) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

            Calendar dob = Calendar.getInstance();

            try {
                dob.setTime(format.parse(dobString));
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
//            Date date1 = format.parse(dobString);
//            Date date2 = format.parse(AppModel.getInstance().getDate());

            //int age = getAge(dob);//Extra
            //int age = getAge_(dob);
            //int age = getAgeOfStudent(dob);
            long age = getNoOfDays(dob);

            switch (className) {
                case "Nursery":
//                    if (age >= 4 && age <= 6) {
                    if(age>=1461 && age< 2192){
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 4 and 6 years for Nursery class", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "KG":
//                    if (age >= 4 && age <= 7) {//475580-627
                    if(age >= 1461 && age < 2557){
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 4 and 7 years for KG class", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-1":
//                    if (age >= 5 && age <= 8) {
                    if (age >= 1826 && age < 2923) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 5 and 8 years for Class-1", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-2":
//                    if (age >= 6 && age <= 9) {
                    if (age >= 2191 && age < 3288) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 6 and 9 years for Class-2", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-3":
//                    if (age >= 7 && age <= 10) {
                    if (age >= 2556 && age < 3653) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 7 and 10 years for Class-3", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-4":
                    //if (calcNumberOfYears(date1, date2) >= 8 && calcNumberOfYears(date1, date2) <= 11){
//                    if (age >= 8 && age <= 11) {
//                        return true;
//                    } else {
//                        Toast.makeText(context, "Age should be between 8 and 11 years for Class-4", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
                    if (age >= 2922 && age < 4018) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 8 and 11 years for Class-4", Toast.LENGTH_LONG).show();
                        return false;
                    }

                case "Class-5":
//                    if (age >= 9 && age <= 12) {
                    if (age >= 3287 && age < 4384) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 9 and 12 years for Class-5", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-6":
//                    if (age >= 10 && age <= 13) {
                    if (age >= 3652 && age < 4749) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 10 and 13 years for Class-6", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-7":
//                    if (age >= 11 && age <= 13) {
                    if (age >= 4017 && age < 4749) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 11 and 13 years for Class-7", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-8":
//                    if (age >= 12 && age <= 14) {
                    if (age >= 4383 && age < 5114) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 12 and 14 years for Class-8", Toast.LENGTH_LONG).show();
                        return false;
                    }

                case "Class-9":
                    //if (age >= 13 && age <= 15) {
                    if (age >= 4748 && age < 5479) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 13 and 15 years for Class-9", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-10":
                    //if (age >= 14 && age <= 16) {
                    if (age >= 5113 && age < 5845) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 14 and 16 years for Class-10", Toast.LENGTH_LONG).show();
                        return false;
                    }
                default:
                    Toast.makeText(context, "Invalid class name", Toast.LENGTH_LONG).show();
                    return false;
                //catch (Exception e) {
//            Toast.makeText(context, "Invalid date of birth format", Toast.LENGTH_LONG).show();
                //return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private long getNoOfDays(Calendar dob) {
        Calendar now = Calendar.getInstance();
        //24-May-2017, change this to your desired Start Date

        LocalDate dateBefore = LocalDate.of(dob.get(Calendar.YEAR), dob.get(MONTH)+1, dob.get(Calendar.DAY_OF_MONTH));
        //29-July-2017, change this to your desired End Date
        long noOfDays;
        LocalDate dateAfter = LocalDate.of(now.get(YEAR), now.get(Calendar.MONTH)+1, now.get(Calendar.DAY_OF_MONTH));
        long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
        System.out.println(noOfDaysBetween);
//        if (noOfDaysBetween >= 2922 && noOfDaysBetween <= 4018) {
//            //year = dateAfter.getYear() - dateBefore.getYear();
//            noOfDays = noOfDaysBetween;
//        } else {
//            //year = dateAfter.getYear() - dateBefore.getYear();
//            noOfDays = noOfDaysBetween;
//        }

        return noOfDaysBetween;
    }
    //**********************************Written by Muhammad Salman Saleem (PS Validation)***************************//

    private Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    private void transferStudent() {
        SyncUtils.getNetworkConnectionInfo(this, SyncProgressHelperClass.SYNC_TYPE_SAVE_SYNC_ID, null, false);

        AppSyncStatusModel appSyncStatusModel = new AppSyncStatusModel();
        appSyncStatusModel.setStartedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
        appSyncStatusModel.setCreatedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss"));
        appSyncStatusModel.setModule_id(Integer.parseInt(AppConstants.StudentTransferModuleValue));
        appSyncStatusModel.setSubModule("Student Transfer");

        StudentTransferUploadModel stum = new StudentTransferUploadModel();
        stum.setStudent_Id(sm.getStd_Id());
        stum.setName(et_Name.getText().toString());

        String dateOfBirth = AppModel.getInstance().convertDatetoFormat(tv_dob.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        stum.setDob(dateOfBirth);

        stum.setFathersName(et_fatherName.getText().toString());
        stum.setFatherNic(et_fathercnic.getText().toString());
        stum.setGrNo(et_GrNo.getText().toString());
        stum.setSchoolId(SchoolSpinnerValue);
        stum.setSchoolClassId(SchoolClass_ID);
        stum.setActualFees(et_monthly_fees.getText().toString().isEmpty() ? 0 : Integer.parseInt(et_monthly_fees.getText().toString()));

        String admissionDate = AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd");
        stum.setEnrollmentDate(admissionDate);
        stum.setWithdrawal(false);
//        stum.setWithdrawalReasonId();
//        stum.setReview_status("A");
        stum.setModifiedBy(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
        stum.setModifiedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));
        stum.setDeviceId(AppModel.getInstance().getDeviceId(this));


        try {
            ApiInterface apiInterface = ApiClient.getClient(StudentTransferActivity.this).create(ApiInterface.class);
            String token = AppModel.getInstance().getToken(StudentTransferActivity.this);
            token = "Bearer " + token;

            Call<ResponseBody> call = apiInterface.uploadStudentTransfer(stum, token);
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                appSyncStatusModel.setEndedOn(AppModel.getInstance().getCurrentDateTime("dd/MM/yyyy hh:mm:ss aa"));
                appSyncStatusModel.setDuration((int) AppModel.getInstance().getDurationBetween(appSyncStatusModel.getStartedOn(),
                        appSyncStatusModel.getEndedOn()));
                appSyncStatusModel.setUploaded(AppModel.getInstance().convertBytesIntoKB(response.raw().body().contentLength(), false));
                SyncProgressHelperClass.getInstance(StudentTransferActivity.this).insertAppSyncRecords(appSyncStatusModel);

                new DataSync(this).uploadAppSyncStatusMaster();

                runOnUiThread(() -> {
                    Intent intent = new Intent(StudentTransferActivity.this, StudentSearchForTransferActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    MessageBox("Student transfer Successfully", true, intent);
                });
                AppModel.getInstance().appendLog(StudentTransferActivity.this, "Student transfer Successfully " +
                        "Student id: " + stum.getStudent_Id() + " Gr No: " + stum.getGrNo());
                AppModel.getInstance().startSyncService(this, SyncProgressHelperClass.SYNC_TYPE_SAVE_SYNC_ID);
            } else {
//                JSONObject res = !response.errorBody().string().equals("null") &&
//                        !response.errorBody().string().equals("") ?
//                        new JSONObject(response.errorBody().string()) : new JSONObject();

                String msg = Objects.requireNonNull(response.errorBody()).string();

                if (msg != null && !msg.isEmpty()) {
                    msg = msg.replace("ERROR:", "");

                    String finalMsg = msg;
                    runOnUiThread(() -> {
                        MessageBox("Error transferring student\n\n" + finalMsg);
                        AppModel.getInstance().appendErrorLog(StudentTransferActivity.this, "Error transferring student due to: " + finalMsg);
                        enableTransferSubmitButton(true);
                    });
                } else {
                    runOnUiThread(() -> {
                        MessageBox("Error transferring student!");
                        AppModel.getInstance().appendErrorLog(StudentTransferActivity.this, "Error transferring student!");
                        enableTransferSubmitButton(true);
                    });
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DatePicker(final TextView date, Activity context) {
        try {
            if (!date.getText().toString().trim().isEmpty()) {
                final Calendar calendar = AppModel.getInstance().getCalendar(date.getText().toString());

                if (calendar != null) {
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                            date.setText(AppModel.getInstance().formatDate(year, month, dayOfmonth, true));
                        }
                    }, mYear, mMonth, mDay);

                    datePickerDialog.show();
                }
            } else {
                final Calendar calendar = AppModel.getInstance().getCalendar(AppModel.getInstance().convertDatetoFormat(AppModel.getInstance().getDate(), "yyyy-MM-dd", "dd-MMM-yy"));

                if (calendar != null) {
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfmonth) {
                            date.setText(AppModel.getInstance().formatDate(year, month, dayOfmonth, true));
                        }
                    }, mYear, mMonth, mDay);

                    datePickerDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void enableTransferSubmitButton(boolean enabled) {
        btn_submit_new_student.setEnabled(enabled);
        btn_submit_new_student.setClickable(enabled);
    }

    //Written by Muhammad Salman Saleem
    private boolean isFlagShipSchool(){
        return DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(SchoolSpinnerValue);
    }

    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }//Written by Muhammad Salman Saleem

}
