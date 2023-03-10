package com.tcf.sma.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
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
import com.tcf.sma.Managers.StorageUtil;
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
import com.tcf.sma.utils.ImageCompression;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class StudentEditProfileActivity extends DrawerActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener
        , IPickResult {
    View view;
    ImageView header;
    Button btn_save_student_info;
    EditText et_name, et_GrNo, et_dateofadmission, et_dob, et_formb, et_fathername, et_fathercnic, et_father_occupation, et_mothername, et_mother_cnic,
            et_mother_occupation, et_guardian_name, et_guardian_cnic, et_guardian_occupation,
            et_previous_school, et_class_in_previous_school, et_address, et_contact, et_tution_fees;
    TextView tv_scholarship_cat;
    int schoolId = 0, studentClassId;

    Spinner ClassSpinner, SectionSpinner, GenderSpinner, ClassSectionSpinner;
    LinearLayout ll_scholarshipCat;
    int index = -1, maxLength = 2;
    ArrayAdapter<ClassModel> ClassAdapter;
    ArrayAdapter<SectionModel> SectionAdapter;
    ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    ArrayAdapter<String> GenderAdapter;
    ClassModel classModel;
    SectionModel sectionModel;
    ClassSectionModel classSectionModel;
    int schoolClassIdForStudent = 0;
    LinearLayout ll_fee_assessment;
    SchoolModel schoolModel;
    List<SchoolModel> schoolModelList;
    private String GenderText = "";
    private StudentModel sm;
    private long id;
    private int scholarshipCategoryId = 0;
    private double actualFees = 0;
    private boolean rangeTrue = false;
    private boolean setGrNO = true;
    private int tempSchoolClassID = 0;
    private File image = null;
    private Uri mUri;
    private String student_profile_Path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_student_edit_profile);
        setToolbar(getString(R.string.student_edit_profile_text), this, false);
        init(view);
        disableviews();
        working();
    }


    private void init(View view) {
        Intent intent = getIntent();
        if (intent != null)
            index = intent.getIntExtra("StudentEditIndex", -1);


        schoolId = (getIntent().hasExtra("SchoolId")) ? getIntent().getIntExtra("SchoolId", 0) : AppModel.getInstance().getSelectedSchool(this);

        btn_save_student_info = (Button) view.findViewById(R.id.btn_save_student_info);
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
        et_contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isMobileNumberValid(s.toString());
            }
        });
        header = (ImageView) view.findViewById(R.id.header);
        header.setOnClickListener(this);
        ll_scholarshipCat = (LinearLayout) view.findViewById(R.id.ll_scholarship_cat);
        tv_scholarship_cat = (TextView) view.findViewById(R.id.tv_scholarship_cat);
        et_tution_fees = (EditText) view.findViewById(R.id.et_tution_fees);

        initSpinners();

        ll_fee_assessment = (LinearLayout) view.findViewById(R.id.ll_fee_assessment);

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
                    ScholarshipCategoryModel model = Scholarship_Category.getInstance(StudentEditProfileActivity.this).getScholarshipCategory(
                            schoolId,
                            (et_tution_fees.getText().toString().isEmpty() ? -1 : Integer.parseInt(et_tution_fees.getText().toString().trim())));

                    if (model.getScholarship_category_description() != null && !model.getScholarship_category_description().isEmpty()) {
                        tv_scholarship_cat.setText(model.getScholarship_category_description());
                        ll_scholarshipCat.setVisibility(View.VISIBLE);

                        scholarshipCategoryId = model.getScholarship_category_id();
                    } else {
                        scholarshipCategoryId = 0;
                        Toast.makeText(StudentEditProfileActivity.this, "Scholarship Category not found", Toast.LENGTH_LONG).show();
                        ll_scholarshipCat.setVisibility(View.GONE);
                    }


                    AppModel.getInstance().hideSoftKeyboard(StudentEditProfileActivity.this);
                    return true;
                }
                return false;
            }
        });

        // ClickListner For Comonents
        et_dateofadmission.setInputType(InputType.TYPE_NULL);
        et_dateofadmission.setOnClickListener(this);
        et_dob.setInputType(InputType.TYPE_NULL);
        et_dob.setOnClickListener(this);
        btn_save_student_info.setOnClickListener(this);
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
    }


    private void disableviews() {
        //views which are  not Editable
        et_name.setEnabled(true);
        et_GrNo.setEnabled(true);
    }

    private void working() {
        schoolModelList = DatabaseHelper.getInstance(this).getAllUserSchoolsById(schoolId);
        List<String> allowedModules = null;
        if (schoolModelList.get(0).getAllowedModule_App() != null){
            allowedModules = Arrays.asList(schoolModelList.get(0).getAllowedModule_App().split(","));
        }

        if ((allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue))) {
            ll_fee_assessment.setVisibility(View.VISIBLE);
        } else {
            ll_fee_assessment.setVisibility(View.GONE);
        }

        sm = DatabaseHelper.getInstance(this).getStudentwithGR(Integer.parseInt(StudentModel.getInstance().getStudentsList().get(index).getGrNo()), schoolId);
        schoolClassIdForStudent = sm.getSchoolClassId();


        String addmissionDate = "";
        String DateOfBirth = "";
        if (sm.getEnrollmentDate() != null)
            addmissionDate = AppModel.getInstance().convertDatetoFormat(sm.getEnrollmentDate(), "yyyy-MM-dd", "dd-MM-yyyy");
        if (sm.getDob() != null)
            DateOfBirth = AppModel.getInstance().convertDatetoFormat(sm.getDob(), "yyyy-MM-dd", "dd-MM-yyyy");
        et_name.setText(sm.getName());
        et_GrNo.setText(sm.getGrNo());

        et_dateofadmission.setText(AppModel.getInstance().convertDatetoFormat(addmissionDate, "dd-MM-yyyy", "dd-MMM-yy"));

        if (sm.getGender() != null) {
            if (sm.getGender().toLowerCase().equals("m")) {
                GenderSpinner.setSelection(1);
            } else {
                GenderSpinner.setSelection(2);
            }
        }

        for (int b = 0; b < classSectionModel.getClassAndSectionsList().size(); b++) {
            try {
                String concatClassSection = sm.getCurrentClass().toLowerCase().trim().concat(" " + sm.getCurrentSection().toLowerCase().trim());

                if (classSectionModel.getClassAndSectionsList().get(b).getClass_section_name().toLowerCase().trim().equals(concatClassSection)) {
                    ClassSectionSpinner.setSelection(b);
                    tempSchoolClassID = ((ClassSectionModel) ClassSectionSpinner.getSelectedItem()).getSchoolClassId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if ((allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue))) {

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

        et_dob.setText(AppModel.getInstance().convertDatetoFormat(DateOfBirth, "dd-MM-yyyy", "dd-MMM-yy"));
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
        try {
            File f;
            String fdir = StorageUtil.getSdCardPath(this).getAbsolutePath() + "/TCF/TCF Images";
            if (sm.getPictureName().contains("BodyPart"))
                f = new File(fdir + "/" + sm.getPictureName());
            else
                f = new File(sm.getPictureName());

            Bitmap bitmap = Compressor.getDefault(this).compressToBitmap(f);
            header.setImageBitmap(bitmap);

//            header.setImageBitmap(SurveyAppModel.getInstance().setImage("P", sm.getPictureName(), this, Integer.parseInt(sm.getGrNo())));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //        disable or enable GrNo View by checking range
//        int grNo = Integer.valueOf(et_GrNo.getText().toString().trim());
//        if (grNo >= 7000 &&
//                grNo <= 8000) {
//            et_GrNo.setEnabled(true);
//            rangeTrue = true;
//        } else {
//            et_GrNo.setEnabled(false);
//            rangeTrue = false;
//        }
    }

    private void updateStudent() {
        int classId, section;
        sm.getSchoolClassId();

        classId = ((ClassSectionModel) ClassSectionSpinner.getSelectedItem()).getClassId();
        section = ((ClassSectionModel) ClassSectionSpinner.getSelectedItem()).getSectionId();

        SchoolClassesModel scm = DatabaseHelper.getInstance(this).getSchoolClassByIds(schoolId, classId, section);
        String admissionDate = AppModel.getInstance().convertDatetoFormat(et_dateofadmission.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        sm.setEnrollmentDate(admissionDate);
        sm.setGender(GenderText.toUpperCase());
        sm.setName(et_name.getText().toString().trim());
        sm.setFathersName(et_fathername.getText().toString().trim());
        sm.setFatherNic(et_fathercnic.getText().toString().trim());
        sm.setFormB(et_formb.getText().toString());
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
        String dateOfBirth = AppModel.getInstance().convertDatetoFormat(et_dob.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        sm.setDob(dateOfBirth);
        if (student_profile_Path != null) {
            sm.setPictureName(student_profile_Path);
            sm.setPictureUploadedOn((String) null);
        } else {
            sm.setPictureName("");
        }
//        sm.setDob(et_dob.getText().toString());
        sm.setContactNumbers(et_contact.getText().toString().trim());

        List<String> allowedModules = null;
        if (schoolModelList.get(0).getAllowedModule_App() != null){
            allowedModules = Arrays.asList(schoolModelList.get(0).getAllowedModule_App().split(","));
        }
        if ((allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue))) {
            sm.setActualFees(Double.parseDouble(et_tution_fees.getText().toString()));
            if (scholarshipCategoryId > 0)
                sm.setScholarshipCategoryId(scholarshipCategoryId);
        }


//        if (rangeTrue) {
        if (et_GrNo.getText().toString().isEmpty()) {
            et_GrNo.setError("Required");
            setGrNO = false;
            et_GrNo.setText(sm.getGrNo());
        } else {
            id = DatabaseHelper.getInstance(StudentEditProfileActivity.this).FindGRNOSTUDENTPROFILE(et_GrNo.getText().toString(), String.valueOf(schoolId));
            if (id == -1) {
                sm.setGrNo(et_GrNo.getText().toString().trim());
                setGrNO = true;
            } else if (et_GrNo.getText().toString().trim().equals(sm.getGrNo())) {
                setGrNO = true;
            } else {
                et_GrNo.setError("GR No. " + et_GrNo.getText().toString() + " already exist!");
                setGrNO = false;
                et_GrNo.setText(sm.getGrNo());
            }
        }
//        }

        if (scm != null && setGrNO) {
            sm.setSchoolClassId(scm.getId());

            if (isFieldsChanged()) {
                long i = DatabaseHelper.getInstance(this).editStudent(
                        sm,
                        sm.getId());

                if (i > 0) {
                    Intent grIntent = new Intent();
                    grIntent.putExtra("grno", sm.getGrNo());
                    setResult(RESULT_OK, grIntent);
                    MessageBox("Student Edit Successfully!", true);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.getInstance(this).STUDENT_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
                    contentValues.put(DatabaseHelper.getInstance(this).STUDENT_MODIFIED_BY, DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
                    contentValues.put(DatabaseHelper.getInstance(this).STUDENT_UPLOADED_ON, (String) null);

                    //Important when any change in table call this method
                    AppModel.getInstance().changeMenuPendingSyncCount(StudentEditProfileActivity.this, false);

                    DatabaseHelper.getInstance(this).updateStudentByColumns(contentValues, sm.getId());

                } else {
                    MessageBox("Something went wrong please try again later!");
                }
            } else if (student_profile_Path != null) {

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.getInstance(this).STUDENT_PICTURE_NAME, sm.getPictureName());
                values.put(DatabaseHelper.getInstance(this).STUDENT_PICTURE_UPLOADED_ON, sm.getPictureUploadedOn());

                long id = DatabaseHelper.getInstance(this).updateTableColumns(
                        DatabaseHelper.getInstance(this).TABLE_STUDENT, values, sm.getId());
                if (id > 0) {
                    Intent grIntent = new Intent();
                    grIntent.putExtra("grno", sm.getGrNo());
                    setResult(RESULT_OK, grIntent);
                    MessageBox("Student Profile Edit Successfully!", true);

                    //Important when any change in table call this method
                    AppModel.getInstance().changeMenuPendingSyncCount(StudentEditProfileActivity.this, false);
                } else {
                    MessageBox("Something went wrong please try again later!");
                }
            }
        } else
            MessageBox("Could not update student to the following class and section!");

    }

    private boolean isFieldsChanged() {
        boolean changes = false;

        StudentModel searchedSm = ifNullReturnEmptyString(DatabaseHelper.getInstance(this)
                .getStudentwithGR(Integer.parseInt(StudentModel.getInstance().getStudentsList().get(index).getGrNo()), schoolId));
        String admissionDate = AppModel.getInstance().convertDatetoFormat(et_dateofadmission.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        admissionDate = admissionDate == null ? "" : admissionDate;

        String dateOfBirth = AppModel.getInstance().convertDatetoFormat(et_dob.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        dateOfBirth = dateOfBirth == null ? "" : dateOfBirth;

        List<String> allowedModules = null;
        if (schoolModelList.get(0).getAllowedModule_App() != null){
            allowedModules = Arrays.asList(schoolModelList.get(0).getAllowedModule_App().split(","));
        }

        if (!searchedSm.getGrNo().equals(et_GrNo.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getEnrollmentDate().equals(admissionDate))
            changes = true;

        else if (!searchedSm.getGender().equals(GenderText.toUpperCase()))
            changes = true;

        else if (!searchedSm.getName().equals(et_name.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getFathersName().equals(et_fathername.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getFatherNic().equals(et_fathercnic.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getFormB().equals(et_formb.getText().toString()))
            changes = true;

        else if (!searchedSm.getFatherOccupation().equals(et_father_occupation.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getMotherName().equals(et_mothername.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getMotherNic().equals(et_mother_cnic.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getMotherOccupation().equals(et_mother_occupation.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getGuardianName().equals(et_guardian_name.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getGuardianNic().equals(et_guardian_cnic.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getGuardianOccupation().equals(et_guardian_occupation.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getPreviousSchoolName().equals(et_previous_school.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getPreviousSchoolClass().equals(et_class_in_previous_school.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getAddress1().equals(et_address.getText().toString().trim()))
            changes = true;


        else if (!searchedSm.getDob().equals(dateOfBirth))
            changes = true;

        else if (!searchedSm.getContactNumbers().equals(et_contact.getText().toString().trim()))
            changes = true;

        else if ((allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue))) {
            if (searchedSm.getActualFees() != Double.parseDouble(et_tution_fees.getText().toString()))
                changes = true;
//            if (scholarshipCategoryId > 0 && searchedSm.getScholarshipCategoryId() == scholarshipCategoryId)
//                i++;
        }
        return changes;
    }

    public StudentModel ifNullReturnEmptyString(StudentModel sm) {
        if (sm.getGrNo() == null) {
            sm.setGrNo("");
        }
        if (sm.getEnrollmentDate() == null) {
            sm.setEnrollmentDate("");
        }
        if (sm.getGender() == null) {
            sm.setGender("");
        }
        if (sm.getName() == null) {
            sm.setName("");
        }
        if (sm.getFathersName() == null) {
            sm.setFathersName("");
        }
        if (sm.getFatherNic() == null) {
            sm.setFatherNic("");
        }
        if (sm.getFormB() == null) {
            sm.setFormB("");
        }
        if (sm.getFatherOccupation() == null) {
            sm.setFatherOccupation("");
        }
        if (sm.getMotherName() == null) {
            sm.setMotherName("");
        }
        if (sm.getMotherNic() == null) {
            sm.setMotherNic("");
        }
        if (sm.getMotherOccupation() == null) {
            sm.setMotherOccupation("");
        }
        if (sm.getGuardianName() == null) {
            sm.setGuardianName("");
        }
        if (sm.getGuardianNic() == null) {
            sm.setGuardianNic("");
        }
        if (sm.getGuardianOccupation() == null) {
            sm.setGuardianOccupation("");
        }
        if (sm.getPreviousSchoolName() == null) {
            sm.setPreviousSchoolName("");
        }
        if (sm.getPreviousSchoolClass() == null) {
            sm.setPreviousSchoolClass("");
        }
        if (sm.getAddress1() == null) {
            sm.setAddress1("");
        }
        if (sm.getDob() == null) {
            sm.setDob("");
        }
        if (sm.getContactNumbers() == null) {
            sm.setContactNumbers("");
        }

        return sm;
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
                    MessageBox("Please select class section for Save info!");
                } else {
                    //if (validate()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Edit Student");
                        builder.setMessage("Are you sure you want to edit this student?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateStudent();
                            }


                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    /*} else {
                        Toast.makeText(current_activity, "Please fill/check in the required fields", Toast.LENGTH_SHORT).show();
                    }*/
                }
                break;
            case R.id.header:
                try {
                    PickImageDialog.build(new PickSetup().setTitle("Student Image")
                            .setPickTypes(EPickType.CAMERA)).show(StudentEditProfileActivity.this).setOnPickResult(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        if (et_dateofadmission.getText().toString().equals("")) {
            et_dateofadmission.setError("Required");
            allOK++;
        } else {
            et_dateofadmission.setError(null);
        }

        if (et_dob.getText().toString().equals("")) {
            et_dob.setError("Required");
            allOK++;
        } else {
            et_dob.setError(null);
        }

        String date = et_dob.getText().toString().trim();
        date = AppModel.getInstance().convertDatetoFormat(date, "dd-MMM-yy", "yyyy-MM-dd");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = dateFormat.parse(date);
            Date date2 = dateFormat.parse(AppModel.getInstance().getDate());

            if (!(calcNumberOfYears(date1, date2) >= 3 && calcNumberOfYears(date1, date2) <= 25)) {
                et_dob.setError("Student age must be between 3 to 25 years.");
                allOK++;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (et_fathername.getText().toString().equals("")) {
            et_fathername.setError("Required");
            allOK++;
        } else {
            et_fathername.setError(null);
        }


        if (ClassSectionSpinner.getSelectedItem().equals(getString(R.string.select_class_section))) {
            Toast.makeText(current_activity, "Please select class and section", Toast.LENGTH_SHORT).show();
            allOK++;
        } else if (tempSchoolClassID != scm.getId()) {
            if (StudentCount >= capacity) {
                Toast.makeText(current_activity, "Cannot Edit student in this class because it reaches Max Capacity:" + capacity + " and No. of Students are:" + StudentCount, Toast.LENGTH_LONG).show();
                allOK++;
            }
        }


        if (GenderSpinner.getSelectedItem().toString().toLowerCase().equals("select gender")) {
            Toast.makeText(current_activity, "Please select gender", Toast.LENGTH_SHORT).show();
            allOK++;
        }

        List<String> allowedModules = null;
        if (schoolModelList.get(0).getAllowedModule_App() != null){
            allowedModules = Arrays.asList(schoolModelList.get(0).getAllowedModule_App().split(","));
        }

        if ((allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue))) {
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

        if (!isMobileNumberValid(et_contact.getText().toString())) {
            allOK++;
        }
        else {
            et_contact.setError(null);
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

    private Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {

            ImageCompression imageCompression = new ImageCompression(StudentEditProfileActivity.this);
            String path = pickResult.getPath();

            imageCompression.execute(path);
            image = new File(path);

            Uri targetUri = AppModel.getInstance().getImageUri(getApplicationContext(), pickResult.getBitmap(), image);
            mUri = targetUri;

            header.setImageBitmap(pickResult.getBitmap());
            header.setBackground(null);

            byte[] Student_Profile = AppModel.getInstance().bitmapToByte(pickResult.getBitmap());
            student_profile_Path = AppModel.getInstance().saveImageToStorage2(Student_Profile, view.getContext(), sm.getGrNo(), 1, schoolId);

        } else {
            Toast.makeText(StudentEditProfileActivity.this, pickResult.getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMobileNumberValid(String mobileNum) {
        int error = 0;
        if (!mobileNum.isEmpty()){
            if (mobileNum.length() != 11) {
                et_contact.setError("Mobile Number should be 11 digits");
                error++;
            }
            else if (mobileNum.length() > 1 &&
                    !mobileNum.substring(0,2).equals("03")){
                et_contact.setError("Mobile Number should always start with 03");
                error++;
            }
        }
        return error == 0;
    }
}
