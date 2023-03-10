package com.tcf.sma.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.Scholarship_Category;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.ScholarshipCategoryModel;
import com.tcf.sma.Models.SchoolClassesModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.SectionModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;
import com.tcf.sma.Survey.model.SurveyAppModel;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StudentReadmissionActivity extends DrawerActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    View view;
    ImageView header;
    Button btn_save_student_info;
    EditText et_name, et_GrNo, et_dateofadmission, et_dob, et_formb, et_fathername, et_fathercnic, et_father_occupation, et_mothername, et_mother_cnic,
            et_mother_occupation, et_guardian_name, et_guardian_cnic, et_guardian_occupation,
            et_previous_school, et_class_in_previous_school, et_address, et_contact, et_tution_fees;
    Spinner ClassSpinner, SectionSpinner, GenderSpinner, ClassSectionSpinner;
    int index = -1, maxLength = 2;
    ArrayAdapter<ClassModel> ClassAdapter;
    ArrayAdapter<SectionModel> SectionAdapter;
    ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    ArrayAdapter<String> GenderAdapter;
    ClassModel classModel;
    SectionModel sectionModel;
    ClassSectionModel classSectionModel;
    TextView tv_scholarship_cat;
    LinearLayout ll_scholarshipCat;
    List<SchoolModel> schoolModelList;
    LinearLayout ll_fee_assessment;
    private String GenderText = "";
    private StudentModel sm;
    private long id;
    private boolean rangeTrue = false;
    private boolean setGrNO = true;
    private int tempSchoolClassID = 0;
    private int schoolId;
    private int scholarshipCategoryId = 0;
    List<String> allowedModules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_student_edit_profile);
        setToolbar(getString(R.string.student_readmisison), this, false);
        init(view);
        disableviews();
        working();
        validation();
    }


    private void init(View view) {
        Intent intent = getIntent();
        if (intent != null)
            index = intent.getIntExtra("StudentEditIndex", -1);

        schoolId = (getIntent().hasExtra("SchoolId")) ? getIntent().getIntExtra("SchoolId", 0) : AppModel.getInstance().getSelectedSchool(this);

        btn_save_student_info = (Button) view.findViewById(R.id.btn_save_student_info);
        btn_save_student_info.setText("ReAdmit Student");
        et_name = (EditText) view.findViewById(R.id.et_name);
        et_GrNo = (EditText) view.findViewById(R.id.et_GrNo);
        et_dateofadmission = (EditText) view.findViewById(R.id.et_dateofadmission);
        ClassSpinner = (Spinner) view.findViewById(R.id.spn_class_name);
        SectionSpinner = (Spinner) view.findViewById(R.id.spn_section);
        ClassSectionSpinner = (Spinner) view.findViewById(R.id.spn_class_section_name);
        GenderSpinner = (Spinner) view.findViewById(R.id.spn_gender);
        et_dob = (EditText) view.findViewById(R.id.et_dob);
        et_formb = (EditText) view.findViewById(R.id.et_formb);
        et_fathername = (EditText) view.findViewById(R.id.et_fathername);
        et_fathercnic = (EditText) view.findViewById(R.id.et_fathercnic);
        et_father_occupation = (EditText) view.findViewById(R.id.et_father_occupation);
        et_mothername = (EditText) view.findViewById(R.id.et_mothername);
        et_mother_cnic = (EditText) view.findViewById(R.id.et_mother_cnic);
        et_mother_occupation = (EditText) view.findViewById(R.id.et_mother_occupation);
        et_guardian_name = (EditText) view.findViewById(R.id.et_guardian_name);
        et_guardian_cnic = (EditText) view.findViewById(R.id.et_guardian_cnic);
        et_guardian_occupation = (EditText) view.findViewById(R.id.et_guardian_occupation);
        et_previous_school = (EditText) view.findViewById(R.id.et_previous_school);
        et_class_in_previous_school = (EditText) view.findViewById(R.id.et_class_in_previous_school);
        et_address = (EditText) view.findViewById(R.id.et_address);
        et_contact = (EditText) view.findViewById(R.id.et_contact);
        header = (ImageView) view.findViewById(R.id.header);
        et_tution_fees = (EditText) view.findViewById(R.id.et_tution_fees);
        ll_scholarshipCat = (LinearLayout) view.findViewById(R.id.ll_scholarship_cat);
        tv_scholarship_cat = (TextView) view.findViewById(R.id.tv_scholarship_cat);

        initSpinners();

        et_tution_fees.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ll_scholarshipCat.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_tution_fees.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ScholarshipCategoryModel model = Scholarship_Category.getInstance(StudentReadmissionActivity.this).getScholarshipCategory(
                            schoolId,
                            (et_tution_fees.getText().toString().isEmpty() ? -1 : Integer.parseInt(et_tution_fees.getText().toString().trim())));

                    if (model.getScholarship_category_description() != null && !model.getScholarship_category_description().isEmpty()) {
                        tv_scholarship_cat.setText(model.getScholarship_category_description());
                        ll_scholarshipCat.setVisibility(View.VISIBLE);

                        scholarshipCategoryId = model.getScholarship_category_id();
                    } else {
                        scholarshipCategoryId = 0;
                        Toast.makeText(StudentReadmissionActivity.this, "Scholarship Category not found", Toast.LENGTH_LONG).show();
                        ll_scholarshipCat.setVisibility(View.GONE);
                    }


                    AppModel.getInstance().hideSoftKeyboard(StudentReadmissionActivity.this);
                    return true;
                }
                return false;
            }
        });
        // ClickListner For Comonents
        et_dateofadmission.setInputType(InputType.TYPE_NULL);
        et_dateofadmission.setOnClickListener(this);
        btn_save_student_info.setOnClickListener(this);

        ll_fee_assessment = (LinearLayout) view.findViewById(R.id.ll_fee_assessment);
    }

    private void initSpinners() {
        classModel = new ClassModel();
        classModel.setClassesList(DatabaseHelper.getInstance(this).getAllClasses());
        classModel.getClassesList().add(0, new ClassModel(0, getString(R.string.select_class)));
        ClassAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classModel.getClassesList());
        ClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSpinner.setAdapter(ClassAdapter);
        ClassSpinner.setOnItemSelectedListener(this);


        sectionModel = new SectionModel();
        sectionModel.setSectionsList(DatabaseHelper.getInstance(this).getAllSections());
        sectionModel.getSectionsList().add(0, new SectionModel(0, getString(R.string.select_section)));
        SectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sectionModel.getSectionsList());
        SectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SectionSpinner.setAdapter(SectionAdapter);
        SectionSpinner.setOnItemSelectedListener(this);

        GenderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.gender_array));
        GenderSpinner.setAdapter(GenderAdapter);
        GenderSpinner.setOnItemSelectedListener(this);

        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(String.valueOf(schoolId)));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
        ClassSectionSpinner.setOnItemSelectedListener(this);

        if(isFlagShipSchool()){
            setEditTextMaxLength(et_GrNo,5);
        }
    }

    private void validation() {
    /*  SectionSpinner.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if(cs.equals("") ){ // for backspace
                            return cs;
                        }
                        if(cs.toString().matches("[a-z A-Z ]+")){
                            return cs;
                        }
                        return "";
                    }
                }
        });*/
        et_fathername.setFilters(new InputFilter[]{new InputFilter() {

            @Override
            public CharSequence filter(CharSequence cs, int start, int end, Spanned dest, int dstart, int dend) {
                if (cs.equals("")) { // for backspace
                    return cs;
                }
                if (cs.toString().matches("[a-z A-Z ]+")) {
                    return cs;

                }
                return "";
            }
        }, new InputFilter.LengthFilter(50)});


        et_father_occupation.setFilters(new InputFilter[]{new InputFilter() {

            @Override
            public CharSequence filter(CharSequence cs, int start, int end, Spanned dest, int dstart, int dend) {
                if (cs.equals("")) { // for backspace
                    return cs;
                }
                if (cs.toString().matches("[a-z A-Z ]+")) {
                    return cs;

                }
                return "";
            }
        }, new InputFilter.LengthFilter(50)});


        et_mothername.setFilters(new InputFilter[]{new InputFilter() {

            @Override
            public CharSequence filter(CharSequence cs, int start, int end, Spanned dest, int dstart, int dend) {
                if (cs.equals("")) { // for backspace
                    return cs;
                }
                if (cs.toString().matches("[a-z A-Z ]+")) {
                    return cs;

                }
                return "";
            }
        }, new InputFilter.LengthFilter(50)});
        et_mother_occupation.setFilters(new InputFilter[]{new InputFilter() {

            @Override
            public CharSequence filter(CharSequence cs, int start, int end, Spanned dest, int dstart, int dend) {
                if (cs.equals("")) { // for backspace
                    return cs;
                }
                if (cs.toString().matches("[a-z A-Z ]+")) {
                    return cs;

                }
                return "";
            }
        }, new InputFilter.LengthFilter(50)});


        et_guardian_name.setFilters(new InputFilter[]{new InputFilter() {

            @Override
            public CharSequence filter(CharSequence cs, int start, int end, Spanned dest, int dstart, int dend) {
                if (cs.equals("")) { // for backspace
                    return cs;
                }
                if (cs.toString().matches("[a-z A-Z ]+")) {
                    return cs;

                }
                return "";
            }
        }, new InputFilter.LengthFilter(50)});

        et_guardian_occupation.setFilters(new InputFilter[]{new InputFilter() {

            @Override
            public CharSequence filter(CharSequence cs, int start, int end, Spanned dest, int dstart, int dend) {
                if (cs.equals("")) { // for backspace
                    return cs;
                }
                if (cs.toString().matches("[a-z A-Z ]+")) {
                    return cs;

                }
                return "";
            }
        }, new InputFilter.LengthFilter(50)});

        et_previous_school.setFilters(new InputFilter[]{new InputFilter() {

            @Override
            public CharSequence filter(CharSequence cs, int start, int end, Spanned dest, int dstart, int dend) {
                if (cs.equals("")) { // for backspace
                    return cs;
                }
                if (cs.toString().matches("[a-z A-Z ]+")) {
                    return cs;

                }
                return "";
            }
        }, new InputFilter.LengthFilter(200)});


    }

    private void disableviews() {
        //views which are  not Editable
        et_name.setEnabled(false);
        et_GrNo.setEnabled(true);    //This work is done in working() Method which is commented for readmission
        et_formb.setEnabled(false);
        et_dob.setEnabled(false);
        et_fathercnic.setEnabled(false);
        et_mother_cnic.setEnabled(false);
        et_guardian_cnic.setEnabled(true);
        et_father_occupation.setEnabled(true);
        et_mother_occupation.setEnabled(true);
        et_guardian_occupation.setEnabled(true);
        et_fathername.setEnabled(false);
        et_mothername.setEnabled(false);
        GenderSpinner.setEnabled(false);
        ClassSpinner.setEnabled(true);

        ClassSectionSpinner.setEnabled(true);
    }

    private void working() {
//        String admissionDate = SurveyAppModel.getInstance().convertDatetoFormat(sm.getEnrollmentDate(), "yyyy-MM-dd", "dd-MM-yyyy");
//        String DateOfBirth = SurveyAppModel.getInstance().convertDatetoFormat(sm.getDob(), "yyyy-MM-dd", "dd-MM-yyyy");

        schoolModelList = DatabaseHelper.getInstance(this).getAllUserSchoolsById(schoolId);
        List<String> allowedModules = Arrays.asList(schoolModelList.get(0).getAllowedModule_App().split(","));

        if ((schoolModelList.get(0).getAllowedModule_App() != null && allowedModules.contains(AppConstants.FinanceModuleValue))
                || DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(schoolId)) {
            ll_fee_assessment.setVisibility(View.VISIBLE);
        } else {
            ll_fee_assessment.setVisibility(View.GONE);
        }

        sm = DatabaseHelper.getInstance(this).getStudentwithGR(Integer.parseInt(StudentModel.getInstance().getStudentsList().get(index).getGrNo()), schoolId);
        String admissionDate = "";
        String DateOfBirth = "";
//        if (sm.getEnrollmentDate() != null)
////            addmissionDate = SurveyAppModel.getInstance().convertDatetoFormat(sm.getEnrollmentDate(), "yyyy-MM-dd", "dd-MM-yyyy");
//        admissionDate = SurveyAppModel.getInstance().getCurrentDateTime("dd-MM-yyyy");
        admissionDate = AppModel.getInstance().getCurrentDateTime("dd-MMM-yy");
        if (sm.getDob() != null)
            DateOfBirth = AppModel.getInstance().convertDatetoFormat(sm.getDob(), "yyyy-MM-dd", "dd-MM-yyyy");
        et_name.setText(sm.getName());
        et_GrNo.setText(sm.getGrNo());
//        et_GrNo.setText("");

        et_dateofadmission.setText(admissionDate);

        if (sm.getGender() != null) {
            if (sm.getGender().toLowerCase().equals("m")) {
                GenderSpinner.setSelection(1);
            } else {
                GenderSpinner.setSelection(2);
            }
        }
        ClassModel cm = DatabaseHelper.getInstance(this).getClassByName(sm.getCurrentClass());
//        for (int b = 0; b < classModel.getClassesList().size(); b++) {
//            try {
//                if (classModel.getClassesList().get(b).getName().toLowerCase().trim().equals(sm.getCurrentClass().toLowerCase().trim())) {
//                    ClassSpinner.setSelection(b);
//                }
//                for (int i = 0; i < sectionModel.getSectionsList().size(); i++) {
//                    if (sectionModel.getSectionsList().get(i).getName().toLowerCase().trim()
//                            .equals(sm.getCurrentSection().toLowerCase().trim())) {
//                        SectionSpinner.setSelection(i);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        for (int b = 0; b < classSectionModel.getClassAndSectionsList().size(); b++) {
            try {
                String concatClassSection = sm.getCurrentClass().toLowerCase().trim().concat(" " + sm.getCurrentSection().toLowerCase().trim());

                if (classSectionModel.getClassAndSectionsList().get(b).getClass_section_name().toLowerCase().trim().equals(concatClassSection)) {
                    ClassSectionSpinner.setSelection(b);
                    tempSchoolClassID = ((ClassSectionModel) ClassSectionSpinner.getSelectedItem()).getSchoolClassId();
                }
//                for (int i = 0; i < sectionModel.getSectionsList().size(); i++) {
//                    if (sectionModel.getSectionsList().get(i).getName().toLowerCase().trim()
//                            .equals(sm.getCurrentSection().toLowerCase().trim())) {
//                        SectionSpinner.setSelection(i);
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ((schoolModelList.get(0).getAllowedModule_App() != null && allowedModules.contains(AppConstants.FinanceModuleValue))
                    || DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(schoolId)) {
                if (sm.getActualFees() > 0) {

                    et_tution_fees.setText((int) sm.getActualFees() + "");
                    scholarshipCategoryId = sm.getScholarshipCategoryId();

                    if (scholarshipCategoryId > 0) {
                        String desc = Scholarship_Category.getInstance(this).getScholarshipCategoryDescription(scholarshipCategoryId);

                        if (desc != null && !desc.isEmpty()) {
                            ll_scholarshipCat.setVisibility(View.VISIBLE);
                            tv_scholarship_cat.setText(desc);
                        }
                    }
                }
            }

            et_dob.setText(DateOfBirth);
            //et_dob.setText(SurveyAppModel.getInstance().convertDatetoFormat(DateOfBirth,"dd-MM-yyyy","dd-MMM-yy"));
            et_formb.setText(sm.getFormB());
            et_fathername.setText(sm.getFathersName());
            et_fathercnic.setText(sm.getFatherNic());
            et_father_occupation.setText(sm.getFatherOccupation());
            et_mothername.setText(sm.getMotherName());
            et_mother_cnic.setText(sm.getMotherNic());
            et_mother_occupation.setText(sm.getMotherOccupation());
            et_guardian_name.setText(sm.getGuardianName());
            et_guardian_cnic.setText(sm.getGuardianNic());
            et_guardian_occupation.setText(sm.getGuardianOccupation());
            et_previous_school.setText(sm.getPreviousSchoolName());
            et_class_in_previous_school.setText(sm.getPreviousSchoolClass());
            et_address.setText(sm.getAddress1());
            et_contact.setText(sm.getContactNumbers());
//            et_tution_fees.setText(sm.getActualFees()+"");
            try {
                header.setImageBitmap(AppModel.getInstance()
                        .setImage("P", sm.getPictureName(),
                                this, Integer.parseInt(sm.getGrNo()), false));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {

            }
        }

        //        disable or enable GrNo View by checking range
//        int grNo = Integer.valueOf(et_GrNo.getText().toString().trim());
//        if (grNo >= 7000 && grNo <= 8000) {
//            et_GrNo.setEnabled(true);
//            rangeTrue = true;
//        } else {
//            et_GrNo.setEnabled(false);
//            rangeTrue = false;
//        }

    }

    private void reAdmitStudent() {
        int classId, section;
        sm.getSchoolClassId();
//        int schoolId = SurveyAppModel.getInstance().getSelectedSchool(this);

//        classId = DatabaseHelper.getInstance(this).getClassByName(ClassSpinner.getSelectedItem().toString().trim()).getClassId();
//        section = DatabaseHelper.getInstance(this).getSectionByName(SectionSpinner.getSelectedItem().toString().trim()).getSectionId();

        classId = ((ClassSectionModel) ClassSectionSpinner.getSelectedItem()).getClassId();
        section = ((ClassSectionModel) ClassSectionSpinner.getSelectedItem()).getSectionId();

        SchoolClassesModel scm = DatabaseHelper.getInstance(this).getSchoolClassByIds(schoolId, classId, section);
//        String admissionDate = SurveyAppModel.getInstance().convertDatetoFormat(et_dateofadmission.getText().toString().trim(), "dd-MM-yyyy", "yyyy-MM-dd");
        String admissionDate = AppModel.getInstance().convertDatetoFormat(et_dateofadmission.getText().toString().trim(), "dd-MMM-yy", "dd-MM-yyyy");
        sm.setEnrollmentDate(admissionDate);
        sm.setGender(GenderText);
//        sm.setCurrentClass(ClassSpinner.getSelectedItem().toString().trim());
//        sm.setCurrentSection(SectionSpinner.getSelectedItem().toString().trim());
        sm.setFathersName(et_fathername.getText().toString().trim());
        sm.setFatherNic(et_fathercnic.getText().toString().trim());
        sm.setFatherOccupation(et_father_occupation.getText().toString().trim());
        sm.setMotherName(et_mothername.getText().toString().trim());
        sm.setMotherNic(et_mother_cnic.getText().toString().trim());
        sm.setMotherOccupation(et_mother_occupation.getText().toString().trim());
        sm.setGuardianName(et_guardian_name.getText().toString().trim());
        sm.setGuardianNic(et_guardian_cnic.getText().toString().trim());
        sm.setGuardianOccupation(et_guardian_occupation.getText().toString().trim());
        sm.setPreviousSchoolName(et_previous_school.getText().toString().trim());
        sm.setPreviousSchoolClass(et_class_in_previous_school.getText().toString().trim());
        sm.setAddress1(et_address.getText().toString().trim());
//        sm.setActualFees(et_tution_fees.getText().toString().isEmpty() ? 0:Double.valueOf(et_tution_fees.getText().toString()));
        sm.setContactNumbers(et_contact.getText().toString().trim());

//        if ((schoolModelList.get(0).getAllowedModule_App() != null && all.contains(AppConstants.FinanceModuleValue))) {
//            sm.setActualFees(Double.parseDouble(et_tution_fees.getText().toString()));
//            if (scholarshipCategoryId > 0)
//                sm.setScholarshipCategoryId(scholarshipCategoryId);
//        }
        if (schoolModelList.get(0).getAllowedModule_App() != null) {
            allowedModules = Arrays.asList(schoolModelList.get(0).getAllowedModule_App().split(","));
            if (allowedModules.contains(AppConstants.FinanceModuleValue)
                    || DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(schoolId)) {
                sm.setActualFees(Double.parseDouble(et_tution_fees.getText().toString()));
                if (scholarshipCategoryId > 0)
                    sm.setScholarshipCategoryId(scholarshipCategoryId);

            }
        }

//        if (rangeTrue) {
        if (et_GrNo.getText().toString().isEmpty()) {
            et_GrNo.setError("Required");
        } else {
            id = DatabaseHelper.getInstance(StudentReadmissionActivity.this).FindGRNOSTUDENTPROFILE(et_GrNo.getText().toString(), String.valueOf(schoolId));
            if (id == -1) {
                sm.setGrNo(et_GrNo.getText().toString().trim());
                setGrNO = true;
            } else if (et_GrNo.getText().toString().trim().equals(sm.getGrNo())) {
                setGrNO = true;
            } else {
                et_GrNo.setError("GR No. " + et_GrNo.getText().toString() + " already exist!");
                setGrNO = false;
            }
        }
//        }

        sm.setPreviousStudentID(
                StudentModel.getInstance().getStudentsList().get(index).getId());
        sm.setActive(true);
        sm.setPictureName(StudentModel.getInstance().getStudentsList().get(index).getPictureName());
        if (scm != null && setGrNO) {
            sm.setSchoolClassId(scm.getId());

            long i = DatabaseHelper.getInstance(this).reAdmitStudentUpdate(sm, sm.getId());

            if (i > 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseHelper.getInstance(this).STUDENT_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                contentValues.put(DatabaseHelper.getInstance(this).STUDENT_MODIFIED_BY, DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
                contentValues.put(DatabaseHelper.getInstance(this).STUDENT_UPLOADED_ON, (String) null);
//
//
                DatabaseHelper.getInstance(this).updateStudentByColumns(contentValues, sm.getId());

                //Important when any change in table call this method
                AppModel.getInstance().changeMenuPendingSyncCount(StudentReadmissionActivity.this, false);


                Intent leave_intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                leave_intent.putExtra("StudentGrNo", Integer.parseInt(sm.getGrNo()));//Old One
                //leave_intent.putExtra("StudentGrNo", sm.getGrNo());//new One
                leave_intent.putExtra("studentid", Integer.valueOf(sm.getId()));

                leave_intent.putExtra("StudentProfileIndex", 0);

                leave_intent.putExtra("classId", classId);
                leave_intent.putExtra("schoolId", schoolId);
                leave_intent.putExtra("sectionId", section);
                leave_intent.putExtra("isFinanceOpen", allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue));
                leave_intent.putExtra("admissiondate", sm.getEnrollmentDate());
                leave_intent.putExtra("class_sec", ClassSectionSpinner.getSelectedItem().toString().replace("Class-", ""));
                if ((allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue))
                        || DatabaseHelper.getInstance(StudentReadmissionActivity.this).isRegionIsInFlagShipSchool(schoolId)) {
                    leave_intent.putExtra("category", "(" + tv_scholarship_cat.getText().toString() + ")");
                }
                leave_intent.putExtra("grno", sm.getGrNo());
                leave_intent.putExtra("monthlyfees", sm.getMonthlyfee());
                //intent.putExtra("receivables", "SectionId");
                leave_intent.putExtra("status", "Active");


                MessageBox("Student ReAdmitted Successfully!", true, leave_intent);


            } else {
                MessageBox("Something went wrong please try again later!");
            }
        } else
            MessageBox("Could not update student to the following class and section!");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_dateofadmission:
                AppModel.getInstance().datePickerForAdmission(et_dateofadmission, this);
                break;
            case R.id.et_dob:
                AppModel.getInstance().DatePicker(et_dob, this);
                break;
            case R.id.btn_save_student_info:
                if ((ClassSectionSpinner.getSelectedItem().toString().equals(getString(R.string.select_class_section)))) {
                    MessageBox("Please select class and section for Save info!");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Student Readmission");
                    builder.setMessage("Are you sure you want to Readmit this student?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (validate()) {
                                reAdmitStudent();
                            }
                        }


                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }

//                if ((SectionSpinner.getSelectedItem().toString().equals(getString(R.string.select_section)))) {
//                    MessageBox("Please select section for Save info!");
//                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("Student ReAdmission");
//                    builder.setMessage("Are you sure you want to ReAdmit this student?");
//                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (validate()) {
//                                reAdmitStudent();
//                            }
//                        }
//
//
//                    });
//                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.show();
//                }
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_gender:
                GenderText = GenderAdapter.getItem(i);
                if (GenderText != null && GenderText.toLowerCase().equals("male")) {
                    GenderText = "m";
                } else {
                    GenderText = "f";
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public boolean validate() {
        int allOK = 0;

        int classId = ((ClassSectionModel) ClassSectionSpinner.getSelectedItem()).getClassId();
        int section = ((ClassSectionModel) ClassSectionSpinner.getSelectedItem()).getSectionId();

        SchoolClassesModel scm = DatabaseHelper.getInstance(this).getSchoolClassByIds(schoolId, classId, section);
        int StudentCount = DatabaseHelper.getInstance(current_activity).getStudentCount(scm.getId());
        int capacity = DatabaseHelper.getInstance(current_activity).getMaxCapacityFromSchoolClass(scm.getSchoolId(), scm.getId());

        if (et_name.getText().toString().equals("")) {
            et_name.setError("Required");
            allOK++;
        } else {
            et_name.setError(null);
        }

        if (ClassSectionSpinner.getSelectedItem().equals(getString(R.string.select_class_section))) {
            Toast.makeText(current_activity, "Please select class and section", Toast.LENGTH_SHORT).show();
            allOK++;
        } else if (tempSchoolClassID != scm.getId()) {
            if (StudentCount >= capacity) {
                Toast.makeText(current_activity, "Cannot ReAdmit student in this class because it reaches Max Capacity:" + capacity + " and No. of Students are:" + StudentCount,
                        Toast.LENGTH_LONG).show();
                allOK++;
            }
        }

//        if (ClassSpinner.getSelectedItem().toString().toLowerCase().equals("select class")) {
//            Toast.makeText(current_activity, "Please select class", Toast.LENGTH_SHORT).show();
//            allOK++;
//        }

        if (GenderSpinner.getSelectedItem().toString().toLowerCase().equals("select gender")) {
            Toast.makeText(current_activity, "Please select gender", Toast.LENGTH_SHORT).show();
            allOK++;
        }

        if (schoolModelList.get(0).getAllowedModule_App() != null ) {
            List<String> allowedModules = Arrays.asList(schoolModelList.get(0).getAllowedModule_App().split(","));
            if (allowedModules.contains(AppConstants.FinanceModuleValue)
                    || DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(schoolId)) {
                if (et_tution_fees.getText().toString().isEmpty()) {
                    Toast.makeText(current_activity, "Please enter tution fees in fee assessment", Toast.LENGTH_SHORT).show();
                    allOK++;
                } else {
                    int monthlyFee = Integer.parseInt(et_tution_fees.getText().toString().trim());
                    if (monthlyFee < 10) {
                        et_tution_fees.setError("Tution fees should be greater than or equals to 10");
                        allOK++;
                    } else if (monthlyFee > 1500) {
                        et_tution_fees.setError("Tution fees should be less than or equals to 1500");
                        allOK++;
                    }
                }
            }
        }

//        if (!et_tution_fees.getText().toString().trim().isEmpty()){
//            int monthlyFee = Integer.parseInt(et_tution_fees.getText().toString().trim());
//            if (monthlyFee < 10){
//                et_tution_fees.setError("Tution fees should be greater than or equals to 10");
//                allOK++;
//            }else if (monthlyFee > 1500){
//                et_tution_fees.setError("Tution fees should be less than or equals to 1500");
//                allOK++;
//            }
//        }

        return allOK == 0;
    }

    /**********************************Muhammad Salman Saleem**************/
    private boolean isFlagShipSchool(){
        return DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(schoolId);
    }

    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }

    private int getAge(Calendar dob) {
        Calendar now = Calendar.getInstance();

        int years = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);//2023-20=2003
        int months = now.get(Calendar.MONTH) - dob.get(Calendar.MONTH);//03-03=0
        int days = now.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);//01-02=1

        // Adjust age based on difference in months and days
        if (months < 0 || (months == 0 && days < 0)) {
            years--;
        }

        return years;
    }

    public boolean isValidAgeForClass(Context context, String dobString, String className) {//"dd/MM/yy", "dd-MMM-yy"
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            Calendar dob = Calendar.getInstance();

            try {
                dob.setTime(format.parse(dobString));
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }

            int age = getAge(dob);

            switch (className) {
                case "Nursery":
                    if (age >= 4 && age <= 6) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 4 and 6 years for Nursery class", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "KG":
                    if (age >= 4 && age <= 7) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 4 and 7 years for KG class", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-1":
                    if (age >= 5 && age <= 8) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 5 and 8 years for Class-1", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-2":
                    if (age >= 6 && age <= 9) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 6 and 9 years for Class-2", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-3":
                    if (age >= 7 && age <= 10) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 7 and 10 years for Class-3", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-4":
                    if (age >= 8 && age <= 11) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 8 and 11 years for Class-4", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-5":
                    if (age >= 9 && age <= 12) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 9 and 12 years for Class-5", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-6":
                    if (age >= 10 && age <= 13) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 10 and 13 years for Class-6", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-7":
                    if (age >= 11 && age <= 13) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 11 and 13 years for Class-7", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-8":
                    if (age >= 12 && age <= 14) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 12 and 14 years for Class-8", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-9":
                    if (age >= 13 && age <= 15) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 13 and 15 years for Class-9", Toast.LENGTH_LONG).show();
                        return false;
                    }
                case "Class-10":
                    if (age >= 14 && age <= 16) {
                        return true;
                    } else {
                        Toast.makeText(context, "Age should be between 14 and 16 years for Class-10", Toast.LENGTH_LONG).show();
                        return false;
                    }
                default:
                    Toast.makeText(context, "Invalid class name", Toast.LENGTH_LONG).show();
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /********************************Muhammad Salman Saleem**************/

}
