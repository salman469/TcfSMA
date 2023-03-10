package com.tcf.sma.Activities;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.common.util.Strings;
import com.squareup.picasso.Picasso;
import com.tcf.sma.Activities.FeesCollection.AccountStatement.AccountStatementNewActivity;
import com.tcf.sma.Activities.FeesCollection.CashReceived.CashReceiptActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.Scholarship_Category;
import com.tcf.sma.Helpers.DbTables.Global.GlobalHelperClass;
import com.tcf.sma.Interfaces.OnChangePendingSyncCount;
import com.tcf.sma.Managers.AttendaceReportDialogManager;
import com.tcf.sma.Managers.ImagePreviewDialog;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.CalendarsModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.EnabledModules;
import com.tcf.sma.Models.EnrollmentModel;
import com.tcf.sma.Models.RetrofitModels.ElectiveSubjectModel;
import com.tcf.sma.Models.RetrofitModels.NationalityModel;
import com.tcf.sma.Models.RetrofitModels.ReligionModel;
import com.tcf.sma.Models.ScholarshipCategoryModel;
import com.tcf.sma.Models.SchoolClassesModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.Models.WithdrawalReasonModel;
import com.tcf.sma.R;
import com.tcf.sma.Services.BasicImageDownloader;
import com.tcf.sma.Views.TooltipWindow;
import com.tcf.sma.utils.FinanceCheckSum;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;

public class StudentProfileActivity extends DrawerActivity implements View.OnClickListener,
//        IPickResult,
        AdapterView.OnItemSelectedListener,
        OnChartValueSelectedListener, OnChangePendingSyncCount {
    View view;
    public static Bitmap profileBitmap;
    ProgressBar profileProgressBar, attendanceProgressBar, basicinfoProgressBar, fatherProgressBar, motherProgressBar, guardianProgressBar;
    ImageView header, header2, showBI, showHH, showFather, showMother, showGuardian, showAttendance, iv_edit_basicinfo, iv_edit_fatherinfo, iv_edit_motherinfo, iv_edit_guardianinfo, iv_edit_profileImg, iv_edit_profileImg2;
    LinearLayout ll_status_message, ll_scholarshipCat, ll_withdrawal, ll_basicinfo, ll_basicinfo2, ll_household, ll_household2, ll_household3, ll_Father, ll_Father2, ll_mother, ll_mother2, ll_guardian, ll_guardian2;
    Button editStudentInfo, updateStudentInfo, btn_report_student_view, btn_student_readmission, btn_cash_receive, btn_account_statement;
    TextView tv_student_name, tv_section_student_Gender, txt_active, tv_student_GR_No, tv_admissiondate,
            tv_section, tv_dob, tv_formB, tv_father_name, tv_father_cnic,
            tv_father_occupation, tv_mother_name, tv_mother_cnic, tv_mother_occupation,
            tv_guardian_name, tv_guardian_cnic, tv_guardian_occupation, tv_previous_school,
            tv_class_previous_school, tv_address, tv_contact, tv_student_id, tv_withdrawalDate, tv_withdrawalReason,
            profileProgressCount, attendanceProgressCount, basicinfoProgressCount, fatherProgressCount, motherProgressCount, guardianProgressCount;
    int index = -1;
    int classId = 0, sectionID, grNo, schoolClassId;
    String grNo_ = "";
    private int schoolId = 0;
    private StudentModel sm;
    private boolean isFinance, isBasicInfo = false, isHouseHold = true, isFather = false, isMother = true, isGuardian = true, isPiechart = true;
    private TextView tv_status_message, txt_class_sec, txt_monthlyfees, txt_receivable, tv_category, tv_scholarship_cat;
    private String globalMessage = " Because your app version is lower than our latest version";
    private String promotionStatus = "";
    private String class_sec = "", grno = "", admissiondate = "", status = "", category = "", student_id = "", receivables = "";
    private int monthlyfees = 0, countprofileprogress = 0, countbasicinfoprogress = 0, countfatherinfoprogress = 0, countmotherinfoprogress = 0, countguardianinfoprogress = 0, attendanceprogress = 0;
    private EnabledModules enabledModules;
    private String student_profile_Path = null;
    private File image = null;
    private Uri mUri;
    ClassSectionModel classSectionModel;
    List<SchoolModel> schoolModelList;
    private int scholarshipCategoryId = 0;
    private boolean setGrNO = true;
    private long id;
    private long idBform;
    private EditText et_name, et_cnic, et_GrNo, et_dob, et_formb, et_contact, et_tution_fees, et_email,
            et_father_name, et_father_cnic, et_father_occupation, et_mother_name, et_mother_cnic, et_mother_occupation, et_guardian_name, et_guardian_cnic, et_guardian_occupation;
    private Spinner spn_gender, spn_class_section_name;
    ArrayAdapter<String> GenderAdapter;
    ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    private int tempSchoolClassID = 0;
    int section = 0, gettotaldays = 0;
    Boolean isBasicInfoEdit = false;

    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    private List<CalendarsModel> OffdaysList;
    int totaldays;
    private KeyListener et_dob_listener;
    boolean FinanceSyncCompleted;
    boolean bFormNoChange;

    private OnChangePendingSyncCount onChangePendingSyncCount;

    ArrayAdapter<ReligionModel> ReligionAdapter;
    ArrayAdapter<NationalityModel> NationalityAdapter;
    ArrayAdapter<ElectiveSubjectModel> ElectiveSubjectAdapter;
    public Spinner ReligionSpinner, NationalitySpinner, ElectiveSubjectSpinner, OrphanSpinner, DisabledSpinner;
    private ReligionModel religionSelectedModel = null;
    private NationalityModel nationalitySelectedModel = null;
    private ElectiveSubjectModel electiveSubjectSelectedModel = null;
    private LinearLayout ll_bform, ll_email, ll_electiveSubject, ll_contact, ll_orphan, ll_deathCertificate, ll_disabled, ll_medicalCertificate,
            ll_religion, ll_nationality, ll_parent_orphan, ll_parent_disabled;
    private String is_orphan = "No"; //By default not orphan
    private byte[] imgDeathCertificate = null, imgMedicalCertificate = null, imgBForm = null;
    private String is_disabled = "No"; //By default not disabled
    private ImageView iv_death_cert_capture, iv_medical_cert_capture, iv_nic_capture;
    private Button btn_death_cert_capture, btn_death_cert_preview, btn_death_cert_remove, btn_nic_capture, btn_nic_preview, btn_nic_remove,
            btn_medical_cert_capture, btn_medical_cert_preview, btn_medical_cert_remove;
    ArrayAdapter<String> OrphanAdapter, DisabledAdapter;
    TextView tv_promStatus_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_student_profile);
        setToolbar(getString(R.string.student_profile_text), this, false);
        bFormNoChange = false;
        init(view);

        FinanceSyncCompleted = AppModel.getInstance().readBooleanFromSharedPreferences(this,
                AppConstants.FinanceSyncCompleted, false);

        ll_household.setVisibility(View.GONE);
        ll_household2.setVisibility(View.GONE);
        ll_household3.setVisibility(View.GONE);

        /*ll_Father.setVisibility(View.GONE);
        ll_Father2.setVisibility(View.GONE);*/

        ll_mother.setVisibility(View.GONE);
        ll_mother2.setVisibility(View.GONE);

        ll_guardian.setVisibility(View.GONE);
        ll_guardian2.setVisibility(View.GONE);


    }

    private void init(View v) {
        onChangePendingSyncCount = this;
        Intent intent = getIntent();
        schoolId = intent.hasExtra("schoolId") ?
                getIntent().getIntExtra("schoolId", 0) : AppModel.getInstance().getSelectedSchool(this);
        if (intent != null)
            index = intent.getIntExtra("StudentProfileIndex", -1);
        if (intent.hasExtra("classId") && intent.hasExtra("sectionId") && intent.hasExtra("StudentGrNo")) {
            try {
                classId = intent.getIntExtra("classId", 0);
                sectionID = intent.getIntExtra("sectionId", 0);
                grNo = intent.getIntExtra("StudentGrNo", 0);//Old One
                //grNo_ = intent.getStringExtra("StudentGrNo");//SS
                isFinance = intent.getBooleanExtra("isFinanceOpen", false);
                if (isFinance || DatabaseHelper.getInstance(StudentProfileActivity.this).isRegionIsInFlagShipSchool(schoolId))
                    category = intent.getStringExtra("category");
                admissiondate = intent.getStringExtra("admissiondate");
                class_sec = intent.getStringExtra("class_sec");
                student_id = "" + intent.getIntExtra("studentid", 0);
                grno = intent.getStringExtra("grno");
                monthlyfees = intent.getIntExtra("monthlyfees", 0);
                status = intent.getStringExtra("status");
                promotionStatus = intent.getStringExtra("promotionStatus");
//                String grNOStr = intent.getStringExtra("StudentGrNo");
//                if (!grNOStr.isEmpty()) {
//                    grNo = Integer.parseInt(grNOStr);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        StudentModel.getInstance().setStudent(sm);
        //Buttons Comment
        //editStudentInfo = (Button) view.findViewById(R.id.btn_edit_student_view);
        updateStudentInfo = (Button) view.findViewById(R.id.btn_update_student_view);
        btn_report_student_view = (Button) view.findViewById(R.id.btn_report_student_view);
        btn_student_readmission = (Button) view.findViewById(R.id.btn_student_readmission);
        btn_cash_receive = (Button) view.findViewById(R.id.btn_cash_receive);
        btn_account_statement = (Button) view.findViewById(R.id.btn_account_statement);

//        boolean isFeesHidden = SurveyAppModel.getInstance().readFromSharedPreferences(this, AppConstants.HIDE_FEES_COLLECTION).equals("1");

        if (!isFinance) {
            btn_cash_receive.setVisibility(View.GONE);
            btn_account_statement.setVisibility(View.GONE);
        }

//      id role ==27 then user is principal,101 Senior Teacher,102 Admin Assistance,103 Viewer and if role ==8 or 9 than area manager and area co-ordinator respectively.
        int roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();

        //editStudentInfo.setOnClickListener(this);
        //updateStudentInfo.setOnClickListener(this);
        btn_report_student_view.setOnClickListener(this);
        btn_student_readmission.setOnClickListener(this);
        btn_cash_receive.setOnClickListener(this);
        btn_account_statement.setOnClickListener(this);

        ll_withdrawal = (LinearLayout) v.findViewById(R.id.ll_withdrawal);
        tv_withdrawalDate = (TextView) v.findViewById(R.id.tv_withdrawnOn);
        tv_withdrawalReason = (TextView) v.findViewById(R.id.tv_withdrawReason);

        pieChart = (PieChart) findViewById(R.id.pieChart);

        ll_basicinfo = v.findViewById(R.id.ll_basicinfo);
        ll_basicinfo2 = v.findViewById(R.id.ll_basicinfo2);

        ll_household = v.findViewById(R.id.ll_household);
        ll_household2 = v.findViewById(R.id.ll_household2);
        ll_household3 = v.findViewById(R.id.ll_household3);

        ll_Father = v.findViewById(R.id.ll_father);
        ll_Father2 = v.findViewById(R.id.ll_father2);

        ll_mother = v.findViewById(R.id.ll_mother);
        ll_mother2 = v.findViewById(R.id.ll_mother2);

        ll_guardian = v.findViewById(R.id.ll_guardian);
        ll_guardian2 = v.findViewById(R.id.ll_guardian2);

        txt_active = v.findViewById(R.id.txt_active);
        tv_student_GR_No = v.findViewById(R.id.tv_student_GR_No);
        txt_class_sec = v.findViewById(R.id.txt_class_sec);
        txt_monthlyfees = v.findViewById(R.id.txt_monthlyfees);
        txt_receivable = v.findViewById(R.id.txt_receivable);
        tv_category = v.findViewById(R.id.tv_category);
        tv_student_name = (TextView) v.findViewById(R.id.tv_student_name);
        tv_section_student_Gender = (TextView) v.findViewById(R.id.tv_section_student_Gender);

        tv_admissiondate = (TextView) v.findViewById(R.id.date_of_admission);
        tv_section = (TextView) v.findViewById(R.id.tv_section);
        tv_dob = (TextView) v.findViewById(R.id.tv_dob);
        tv_formB = (TextView) v.findViewById(R.id.tv_formB);
        tv_father_name = (TextView) v.findViewById(R.id.tv_father_name);
        tv_father_cnic = (TextView) v.findViewById(R.id.tv_father_cnic);
        tv_father_occupation = (TextView) v.findViewById(R.id.tv_father_occupation);
        tv_mother_name = (TextView) v.findViewById(R.id.tv_mother_name);
        tv_mother_cnic = (TextView) v.findViewById(R.id.tv_mother_cnic);
        tv_mother_occupation = (TextView) v.findViewById(R.id.tv_mother_occupation);
        tv_guardian_name = (TextView) v.findViewById(R.id.tv_guardian_name);
        tv_guardian_cnic = (TextView) v.findViewById(R.id.tv_guardian_cnic);
        tv_guardian_occupation = (TextView) v.findViewById(R.id.tv_guardian_occupation);
        //tv_previous_school = (TextView) v.findViewById(R.id.tv_previous_school);
        //tv_class_previous_school = (TextView) v.findViewById(R.id.tv_class_previous_school);
        tv_address = (TextView) v.findViewById(R.id.txt_address);
        tv_contact = (TextView) v.findViewById(R.id.tv_contact);
        tv_student_id = (TextView) v.findViewById(R.id.tv_student_id);
        ll_status_message = view.findViewById(R.id.ll_status_message);
        tv_status_message = (TextView) v.findViewById(R.id.tv_status_message);
        tv_promStatus_message = (TextView) v.findViewById(R.id.promotionStatus);
        showBI = v.findViewById(R.id.showBI);
        showBI.setOnClickListener(this);
        showHH = v.findViewById(R.id.showHH);
        showHH.setOnClickListener(this);
        showFather = v.findViewById(R.id.showFather);
        showFather.setOnClickListener(this);
        showMother = v.findViewById(R.id.showMother);
        showMother.setOnClickListener(this);
        showGuardian = v.findViewById(R.id.showGuardian);
        showGuardian.setOnClickListener(this);
        showAttendance = v.findViewById(R.id.showAttendance);
        showAttendance.setOnClickListener(this);

        header = v.findViewById(R.id.header);

        profileProgressCount = v.findViewById(R.id.profileProgressCount);
        profileProgressBar = v.findViewById(R.id.profileProgressBar);
        profileProgressBar.setOnClickListener(this);

        attendanceProgressCount = v.findViewById(R.id.attendanceProgressCount);
        attendanceProgressBar = v.findViewById(R.id.attendanceProgressBar);
        gettotaldays = DatabaseHelper.getInstance(view.getContext()).getDatesofHolidaysInLast30days(schoolId + "");
        attendanceProgressBar.setMax(gettotaldays);

        basicinfoProgressCount = v.findViewById(R.id.basicinfoProgressCount);
        basicinfoProgressBar = v.findViewById(R.id.basicinfoProgressBar);

        fatherProgressCount = v.findViewById(R.id.fatherProgressCount);
        fatherProgressBar = v.findViewById(R.id.fatherProgressBar);

        motherProgressCount = v.findViewById(R.id.motherProgressCount);
        motherProgressBar = v.findViewById(R.id.motherProgressBar);

        guardianProgressCount = v.findViewById(R.id.guardianProgressCount);
        guardianProgressBar = v.findViewById(R.id.guardianProgressBar);

        iv_edit_basicinfo = v.findViewById(R.id.iv_edit_basicinfo);
        iv_edit_basicinfo.setOnClickListener(this);
        iv_edit_fatherinfo = v.findViewById(R.id.iv_edit_fatherinfo);
        iv_edit_fatherinfo.setOnClickListener(this);
        iv_edit_motherinfo = v.findViewById(R.id.iv_edit_motherinfo);
        iv_edit_motherinfo.setOnClickListener(this);
        iv_edit_guardianinfo = v.findViewById(R.id.iv_edit_guardianinfo);
        iv_edit_guardianinfo.setOnClickListener(this);
        iv_edit_profileImg = v.findViewById(R.id.iv_edit_profileImg);
        iv_edit_profileImg.setOnClickListener(this);


        if (roleID == AppConstants.roleId_102_AA) {
            //editStudentInfo.setVisibility(View.GONE);
            //updateStudentInfo.setEnabled(false);
            //updateStudentInfo.setBackgroundColor(Color.GRAY);
            hide_edit_markers();
        } else if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            btn_cash_receive.setVisibility(View.GONE);
            //editStudentInfo.setVisibility(View.GONE);
            //updateStudentInfo.setVisibility(View.GONE);
            btn_report_student_view.setVisibility(View.GONE);
            hide_edit_markers();
        }


    }

    private String getStatus(String status) {
        if (Strings.isEmptyOrWhitespace(status))
            return "N/A";
        switch (status) {
            case "T":
                return "To Be Verified";

            case "P":
                return "Available for Approval";

            case "A":
                return "Approved";

            case "F":
                return "Not Approved";

            default:
                return "N/A";
        }
    }


    private void working() {
        try {

            enabledModules = AppModel.getInstance().getEnabledModules(new WeakReference<>(StudentProfileActivity.this));
            schoolId = getIntent().hasExtra("schoolId") ?
                    getIntent().getIntExtra("schoolId", 0) : AppModel.getInstance().getSelectedSchool(this);

            sm = DatabaseHelper.getInstance(this).getStudentwithGR(grNo,
                    schoolId);

            schoolModelList = DatabaseHelper.getInstance(this).getAllUserSchoolsById(schoolId);

            String addmissionDate = "";
            String DateOfBirth = "";
            if (sm.getEnrollmentDate() != null)
                addmissionDate = AppModel.getInstance().convertDatetoFormat(sm.getEnrollmentDate(), "yyyy-MM-dd", "dd-MM-yyyy");
            if (sm.getDob() != null)
                DateOfBirth = AppModel.getInstance().convertDatetoFormat(sm.getDob(), "yyyy-MM-dd", "dd-MM-yyyy");


            tv_student_name.setText(changeStringCase(sm.getName()));

            if (isFlagShipAndNine()) {
                tv_promStatus_message.setVisibility(View.VISIBLE);
                tv_promStatus_message.setText(getStatus(sm.getStudent_promotionStatus()));
            } else {
                tv_promStatus_message.setVisibility(View.GONE);
            }

            if (sm.getGender().equals("M"))
                tv_student_name.setTextColor(getResources().getColor(R.color.blue_color));
            else if (sm.getGender().equals("F"))
                tv_student_name.setTextColor(getResources().getColor(R.color.pink));

            if (sm.getGender() != null) {
                tv_section_student_Gender.setText(sm.getGender().toLowerCase().equals("m") ? "Male" : "Female");
            } else {
                tv_section_student_Gender.setText("");
            }
            //tv_student_GR_No.setText(sm.getGrNo());
//            date_of_admission.setText(addmissionDate);
            //date_of_admission.setText(AppModel.getInstance().convertDatetoFormat(addmissionDate, "dd-MM-yyyy", "dd-MMM-yy"));
            //tv_class.setText(String.valueOf(sm.getCurrentClass()));
            //tv_section.setText(sm.getCurrentSection());
//            tv_dob.setText(DateOfBirth);
            countprofileprogress = 0;
            countbasicinfoprogress = 0;
            countfatherinfoprogress = 0;
            countmotherinfoprogress = 0;
            countguardianinfoprogress = 0;

            if (DateOfBirth != null && !DateOfBirth.equals("")) {
                countprofileprogress += 20;
                countbasicinfoprogress += 25;
            }
            if (sm.getFormB() != null && !sm.getFormB().equals("")) {
                countprofileprogress += 10;
                countbasicinfoprogress += 25;
            }
            if (sm.getGender() != null && !sm.getGender().equals("")) {
                countbasicinfoprogress += 25;
            }
            if (sm.getFathersName() != null && !sm.getFathersName().equals("")) {
                countprofileprogress += 10;
                countfatherinfoprogress += 50;
            }
            if (sm.getFatherNic() != null && !sm.getFatherNic().equals("")) {
                countprofileprogress += 20;
                countfatherinfoprogress += 25;
            }
            if (sm.getContactNumbers() != null && !sm.getContactNumbers().equals("")) {
                countprofileprogress += 10;
                countbasicinfoprogress += 25;
            }
            if (sm.getFatherOccupation() != null && !sm.getFatherOccupation().equals("")) {
                countfatherinfoprogress += 25;
                countprofileprogress += 10;
            }
            if (sm.getMotherName() != null && !sm.getMotherName().equals("")) {
                countmotherinfoprogress += 50;
            }
            if (sm.getMotherNic() != null && !sm.getMotherNic().equals("")) {
                countmotherinfoprogress += 25;
            }
            if (sm.getMotherOccupation() != null && !sm.getMotherOccupation().equals("")) {
                countmotherinfoprogress += 25;
            }
            if (sm.getGuardianName() != null && !sm.getGuardianName().equals("")) {
                countguardianinfoprogress += 50;
            }
            if (sm.getGuardianNic() != null && !sm.getGuardianNic().equals("")) {
                countguardianinfoprogress += 25;
            }
            if (sm.getGuardianOccupation() != null && !sm.getGuardianOccupation().equals("")) {
                countguardianinfoprogress += 25;
            }
            tv_dob.setText(AppModel.getInstance().convertDatetoFormat(DateOfBirth, "dd-MM-yyyy", "dd-MMM-yy"));
            tv_formB.setText(sm.getFormB());
            tv_father_name.setText(sm.getFathersName());
            tv_father_cnic.setText(sm.getFatherNic());
            tv_father_occupation.setText(sm.getFatherOccupation());
            tv_mother_name.setText(sm.getMotherName());
            tv_mother_cnic.setText(sm.getMotherNic());
            tv_mother_occupation.setText(sm.getMotherOccupation());
            tv_guardian_name.setText(sm.getGuardianName());
            tv_guardian_cnic.setText(sm.getGuardianNic());
            tv_guardian_occupation.setText(sm.getGuardianOccupation());
            //tv_previous_school.setText(sm.getPreviousSchoolName());
            //tv_class_previous_school.setText(sm.getPreviousSchoolClass());
            tv_address.setText(sm.getAddress1());
            tv_contact.setText(sm.getContactNumbers());
            tv_student_GR_No.setText(sm.getGrNo());
            tv_student_id.setText("" + String.format("%s - ", student_id));
            txt_class_sec.setText(class_sec);
            if (class_sec.contains("Blue"))
                txt_class_sec.setTextColor(getResources().getColor(R.color.blue_color));
            else if (class_sec.contains("Green"))
                txt_class_sec.setTextColor(getResources().getColor(R.color.app_green));
            txt_monthlyfees.setText("RS. " + (int) sm.getActualFees());
            if (isFinance || DatabaseHelper.getInstance(StudentProfileActivity.this).isRegionIsInFlagShipSchool(schoolId)) {
                txt_monthlyfees.setVisibility(View.VISIBLE);
                tv_category.setVisibility(View.VISIBLE);
                ScholarshipCategoryModel model = Scholarship_Category.getInstance(StudentProfileActivity.this).getScholarshipCategory(
                        schoolId,
                        (String.valueOf((int) sm.getActualFees()).isEmpty() ? -1 : (int) sm.getActualFees()));

                if (model.getScholarship_category_description() != null && !model.getScholarship_category_description().isEmpty()) {
                    tv_category.setText("(" + model.getScholarship_category_description() + ")");
                } else {
                    tv_category.setText("");
                }
            } else {
                txt_monthlyfees.setVisibility(View.GONE);
                tv_category.setVisibility(View.GONE);
            }

            receivables = DatabaseHelper.getInstance(this).getAllhighestDuesOfStudent(String.valueOf(schoolId), String.valueOf(sm.getId()));

            if (receivables != null && !receivables.equals("")) {
                txt_receivable.setText("RS. " + receivables);
            } else {
                txt_receivable.setText("RS. 0");
            }

            txt_active.setText(status);


            tv_admissiondate.setText(admissiondate);
            if (!sm.isWithdrawal()) {
                btn_student_readmission.setVisibility(View.GONE);
            } else {
                ll_withdrawal.setVisibility(View.VISIBLE);
                tv_withdrawalDate.setText(sm.getWithdrawnOn());
                ArrayList<WithdrawalReasonModel> wms = DatabaseHelper.getInstance(this).getWithdrawalReasons(false);

                for (WithdrawalReasonModel wrm : wms) {
                    if (wrm.getId() == sm.getWithdrawalReasonId()) {
                        tv_withdrawalReason.setText(wrm.getReasonName());
                        break;
                    }
                }
            }

            if (DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_103_V)
                btn_student_readmission.setVisibility(View.GONE);


            try {
                if (sm.getPictureName() != null) {
                    File f;
                    String fdir = StorageUtil.getSdCardPath(this).getAbsolutePath() + "/TCF/TCF Images";
                    if (sm.getPictureName().contains("BodyPart"))
                        f = new File(fdir + "/" + sm.getPictureName());
                    else
                        f = new File(fdir + "/" + sm.getPictureName());

                    if (f.exists()) {
                        Bitmap bitmap = Compressor.getDefault(this).compressToBitmap(f);
                        countprofileprogress += 20;
                        header.setImageBitmap(bitmap);
                    }
                }
//                header.setImageBitmap(SurveyAppModel.getInstance().setImage("P", sm.getPictureName(), this, Integer.parseInt(sm.getGrNo())));
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (sm.isActive() || !sm.isWithdrawal()) {

                profileProgressCount.setText(countprofileprogress + "%");
                profileProgressBar.setProgress(countprofileprogress);

                if (countprofileprogress < 50)
                    profileProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_profile_progress_red));
                else if (countprofileprogress < 80)
                    profileProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_listing_profile_progress_orange));
                else
                    profileProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_progress_bar_green));

                basicinfoProgressCount.setText(countbasicinfoprogress + "%");
                basicinfoProgressBar.setProgress(countbasicinfoprogress);

                if (countbasicinfoprogress < 50)
                    basicinfoProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_profile_progress_red));
                else if (countbasicinfoprogress < 80)
                    basicinfoProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_listing_profile_progress_orange));
                else
                    basicinfoProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_progress_bar_green));

                fatherProgressCount.setText(countfatherinfoprogress + "%");
                fatherProgressBar.setProgress(countfatherinfoprogress);
                if (countfatherinfoprogress < 50)
                    fatherProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_profile_progress_red));
                else if (countfatherinfoprogress < 80)
                    fatherProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_listing_profile_progress_orange));
                else
                    fatherProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_progress_bar_green));

                motherProgressCount.setText(countmotherinfoprogress + "%");
                motherProgressBar.setProgress(countmotherinfoprogress);
                if (countmotherinfoprogress < 50)
                    motherProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_profile_progress_red));
                else if (countmotherinfoprogress < 80)
                    motherProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_listing_profile_progress_orange));
                else
                    motherProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_progress_bar_green));

                guardianProgressCount.setText(countguardianinfoprogress + "%");
                guardianProgressBar.setProgress(countguardianinfoprogress);
                if (countguardianinfoprogress < 50)
                    guardianProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_profile_progress_red));
                else if (countguardianinfoprogress < 80)
                    guardianProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_listing_profile_progress_orange));
                else
                    guardianProgressBar.setProgressDrawable(this.getResources().getDrawable(R.drawable.employee_progress_bar_green));
            } else {
                hide_edit_markers();
                tv_student_name.setTextColor(getResources().getColor(R.color.gray));
                profileProgressCount.setTextColor(getResources().getColor(R.color.gray));
                txt_class_sec.setTextColor(getResources().getColor(R.color.gray));
                tv_student_id.setTextColor(getResources().getColor(R.color.gray));
                tv_student_GR_No.setTextColor(getResources().getColor(R.color.gray));
                tv_admissiondate.setTextColor(getResources().getColor(R.color.gray));
                txt_monthlyfees.setTextColor(getResources().getColor(R.color.gray));
                txt_receivable.setTextColor(getResources().getColor(R.color.gray));
                tv_category.setTextColor(getResources().getColor(R.color.gray));
                txt_active.setTextColor(getResources().getColor(R.color.gray));
                //Basic
                basicinfoProgressCount.setTextColor(getResources().getColor(R.color.gray));
                showBI.setColorFilter(getResources().getColor(R.color.gray));
                profileProgressCount.setTextColor(getResources().getColor(R.color.gray));
                tv_formB.setTextColor(getResources().getColor(R.color.gray));
                tv_section_student_Gender.setTextColor(getResources().getColor(R.color.gray));
                tv_dob.setTextColor(getResources().getColor(R.color.gray));
                tv_contact.setTextColor(getResources().getColor(R.color.gray));
                //father
                fatherProgressCount.setTextColor(getResources().getColor(R.color.gray));
                showFather.setColorFilter(getResources().getColor(R.color.gray));
                tv_father_name.setTextColor(getResources().getColor(R.color.gray));
                tv_father_cnic.setTextColor(getResources().getColor(R.color.gray));
                tv_father_occupation.setTextColor(getResources().getColor(R.color.gray));
                //Mother
                motherProgressCount.setTextColor(getResources().getColor(R.color.gray));
                showMother.setColorFilter(getResources().getColor(R.color.gray));
                tv_mother_name.setTextColor(getResources().getColor(R.color.gray));
                tv_mother_cnic.setTextColor(getResources().getColor(R.color.gray));
                tv_mother_occupation.setTextColor(getResources().getColor(R.color.gray));
                //Guardian
                guardianProgressCount.setTextColor(getResources().getColor(R.color.gray));
                showGuardian.setColorFilter(getResources().getColor(R.color.gray));
                tv_guardian_name.setTextColor(getResources().getColor(R.color.gray));
                tv_guardian_cnic.setTextColor(getResources().getColor(R.color.gray));
                tv_guardian_occupation.setTextColor(getResources().getColor(R.color.gray));
            }

            try {
                EnrollmentModel enrollmentModel = DatabaseHelper.getInstance(StudentProfileActivity.this)
                        .getEnrollmentfromGR(Integer.parseInt(sm.getGrNo()), schoolId);

                if (enrollmentModel.getENROLLMENT_REVIEW_STATUS() != null &&
                        enrollmentModel.getENROLLMENT_REVIEW_STATUS().equalsIgnoreCase("R") && Strings.isEmptyOrWhitespace(sm.getFathersName())) {
//                    editStudentInfo.setVisibility(View.GONE);
                    hide_edit_markers();
                    ll_status_message.setVisibility(View.VISIBLE);
                    tv_status_message.setVisibility(View.VISIBLE);
                    tv_status_message.setText("Form rejected from HO. Please resubmit");
                    tv_status_message.setTextColor(getResources().getColor(R.color.red_color));
                } else if (sm.getFathersName() == null) {
//                    editStudentInfo.setVisibility(View.GONE);
                    hide_edit_markers();
                    ll_status_message.setVisibility(View.VISIBLE);
                    tv_status_message.setVisibility(View.VISIBLE);
                    tv_status_message.setText("Data entry pending at HO");
                    tv_status_message.setTextColor(getResources().getColor(R.color.app_green));
                } else if (!sm.isActive() && sm.isWithdrawal() && sm.getWithdrawalReasonId() == 14) {
//                    editStudentInfo.setVisibility(View.GONE);
                    hide_edit_markers();
                    ll_status_message.setVisibility(View.VISIBLE);
                    tv_status_message.setVisibility(View.VISIBLE);
                    tv_status_message.setText("You cannot edit graduated student");
                    tv_status_message.setTextColor(getResources().getColor(R.color.app_green));
                } else if (!sm.isActive() && sm.isWithdrawal()) {
//                    editStudentInfo.setVisibility(View.GONE);
                    hide_edit_markers();
                    ll_status_message.setVisibility(View.VISIBLE);
                    tv_status_message.setVisibility(View.VISIBLE);

                    tv_status_message.setText("You cannot update withdrawn student");
                    tv_status_message.setTextColor(getResources().getColor(R.color.app_green));
                } else if (sm.getStudent_promotionStatus().equalsIgnoreCase("F") && !Strings.isEmptyOrWhitespace(sm.getStudent_promotionComments())) {
                    ll_status_message.setVisibility(View.VISIBLE);
                    tv_status_message.setVisibility(View.VISIBLE);
                    tv_status_message.setText(sm.getStudent_promotionComments());
                    tv_status_message.setTextColor(getResources().getColor(R.color.red_color));
                }
                if (!sm.isWithdrawal() && sm.getWithdrawalReasonId() == 14) {
                    MessageBox("You are not allowed to update Graduated Student");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private boolean isChecksumSuccessfull(){
//        int i = 0;
//        if (FinanceCheckSum.Instance(new WeakReference<>(StudentProfileActivity.this)).isCheckSumApplicable()) {
//            i++;
//            Toast.makeText(StudentProfileActivity.this, "Check sum is pending, please wait for the sync to complete.", Toast.LENGTH_SHORT).show();
//        }else if (!FinanceCheckSum.Instance(new WeakReference<>(StudentProfileActivity.this)).isCheckSumSuccessfull()){
//            i++;
//            Toast.makeText(StudentProfileActivity.this, "Disabled due to checksum failure/", Toast.LENGTH_SHORT).show();
//        }else if (FeesCollection.getInstance(StudentProfileActivity.this).getClassSectionForFeeEntryBySchoolId(SurveyAppModel.getInstance().getAllUserSchoolsForFinance(StudentProfileActivity.this)).size() != 0) {
//            i++;
//            Toast.makeText(StudentProfileActivity.this, "Enter fee entry first", Toast.LENGTH_SHORT).show();
//        }else if (FeesCollection.getInstance(StudentProfileActivity.this).getUnuploadedCountFeeEntry(SurveyAppModel.getInstance().getAllUserSchoolsForFinance(StudentProfileActivity.this)) != 0){
//            i++;
//            Toast.makeText(StudentProfileActivity.this, "Enter fee entry first", Toast.LENGTH_SHORT).show();
//        }
//
//        return i == 0;
//    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileProgressBar:
                TooltipWindow tipWindow = TooltipWindow.getInstance(StudentProfileActivity.this, R.string.tooltip_profile_progress);
                if (!tipWindow.isTooltipShown())
                    tipWindow.showToolTip(v);
                break;
            case R.id.btn_cash_receive:
                if (FinanceSyncCompleted) {
                    if (enabledModules.isModuleFinanceEnabled()) {
                        if (FinanceCheckSum.Instance(new WeakReference<>(this)).isChecksumSuccess(this, true)) {
                            Intent irc = new Intent(this, CashReceiptActivity.class);
                            irc.putExtra("gr", tv_student_GR_No.getText().toString());
                            irc.putExtra("schoolId", schoolId);
                            startActivity(irc);
                            finish();
                        }
                    } else {
                        AppModel.getInstance().showMessage(new WeakReference<>(StudentProfileActivity.this), "Info!", "Finance Module is disabled" + globalMessage);
                    }
                } else {
                    Toast.makeText(StudentProfileActivity.this, "Please wait for the first sync to complete successfully", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_account_statement:
                if (FinanceSyncCompleted) {
                    if (enabledModules.isModuleFinanceEnabled()) {
                        if (FinanceCheckSum.Instance(new WeakReference<>(this)).isChecksumSuccess(this, true)) {
                            Intent ac = new Intent(this, AccountStatementNewActivity.class);
                            ac.putExtra("StudentGrNo", tv_student_GR_No.getText().toString());
                            startActivity(ac);
                            finish();
                        }
                    } else {
                        AppModel.getInstance().showMessage(new WeakReference<>(StudentProfileActivity.this), "Info!", "Finance Module is disabled" + globalMessage);
                    }
                } else {
                    Toast.makeText(StudentProfileActivity.this, "Please wait for the first sync to complete successfully", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iv_edit_profileImg:
                openCameraGalleryDialog("Profile Image");
                isBasicInfoEdit = false;
                break;
            case R.id.iv_edit_profileImg2:
                openCameraGalleryDialog("Profile Image");
                isBasicInfoEdit = true;
             /*   try {
//                    if (sm.isActive()) {
                    Intent intent = new Intent(this, StudentEditProfileActivity.class);
                    intent.putExtra("StudentEditIndex", index);
                    intent.putExtra("SchoolId", schoolId);
                    startActivityForResult(intent, 200);
//                    } else if(!sm.isActive()){
//                        MessageBox("You cannot update inactive Student");
//                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
*/
                /*try {
                    PickImageDialog.build(new PickSetup().setTitle("Profile Image")
                            .setPickTypes(EPickType.CAMERA, EPickType.GALLERY)).show(this).setOnPickResult(new IPickResult() {
                        @Override
                        public void onPickResult(PickResult r) {

                            try {
                                if (r.getError() == null) {
                                    CropImage.activity(r.getUri())
                                            .setGuidelines(CropImageView.Guidelines.ON)
                                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                                            .setFixAspectRatio(true)
                                            .start(StudentProfileActivity.this);
                                } else {
                                    Toast.makeText(StudentProfileActivity.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                                AppModel.getInstance().appendErrorLog(StudentProfileActivity.this,"Error in Imageview:" + e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    AppModel.getInstance().appendErrorLog(this,"Error in Imageview:" + e.getMessage());
                }*/
                break;
            case R.id.btn_student_readmission:
                Intent readmissionIntent = new Intent(this, StudentReadmissionActivity.class);
                readmissionIntent.putExtra("StudentEditIndex", index);
                readmissionIntent.putExtra("SchoolId", schoolId);
                startActivity(readmissionIntent);
                finish();
                break;
            case R.id.btn_update_student_view:
                Intent i = new Intent(this, UpdateExistingStudentActivity.class);
                i.putExtra("gr", tv_student_GR_No.getText().toString());
                i.putExtra("SchoolId", schoolId);
                i.putExtra("SchoolClassId", sm.getSchoolClassId());
                startActivity(i);
                break;
            case R.id.btn_report_student_view:
                new AttendaceReportDialogManager(StudentProfileActivity.this, sm.getId(), sm.getSchoolClassId()).show();
                break;
            case R.id.showBI:
                if (!isBasicInfo) {
                    ll_basicinfo.setVisibility(View.GONE);
                    ll_basicinfo2.setVisibility(View.GONE);
                    showBI.animate().rotation(showBI.getRotation() - 180).start();
                    isBasicInfo = true;
                } else {
                    showBI.animate().rotation(showBI.getRotation() - 180).start();
                    ll_basicinfo.setVisibility(View.VISIBLE);
                    ll_basicinfo2.setVisibility(View.VISIBLE);
                    isBasicInfo = false;
                }
                break;
            case R.id.showHH:
                if (!isHouseHold) {
                    ll_household.setVisibility(View.GONE);
                    ll_household2.setVisibility(View.GONE);
                    ll_household3.setVisibility(View.GONE);
                    showHH.animate().rotation(showHH.getRotation() - 180).start();
                    isHouseHold = true;
                } else {
                    showHH.animate().rotation(showHH.getRotation() - 180).start();
                    ll_household.setVisibility(View.VISIBLE);
                    ll_household2.setVisibility(View.VISIBLE);
                    ll_household3.setVisibility(View.VISIBLE);
                    isHouseHold = false;
                }
                break;

            case R.id.showFather:
                if (!isFather) {
                    ll_Father.setVisibility(View.GONE);
                    ll_Father2.setVisibility(View.GONE);
                    showFather.animate().rotation(showFather.getRotation() - 180).start();
                    isFather = true;
                } else {
                    showFather.animate().rotation(showFather.getRotation() - 180).start();
                    ll_Father.setVisibility(View.VISIBLE);
                    ll_Father2.setVisibility(View.VISIBLE);
                    isFather = false;
                }
                break;
            case R.id.showMother:
                if (!isMother) {
                    ll_mother.setVisibility(View.GONE);
                    ll_mother2.setVisibility(View.GONE);
                    showMother.animate().rotation(showMother.getRotation() - 180).start();
                    isMother = true;
                } else {
                    showMother.animate().rotation(showMother.getRotation() - 180).start();
                    ll_mother.setVisibility(View.VISIBLE);
                    ll_mother2.setVisibility(View.VISIBLE);
                    isMother = false;
                }
                break;
            case R.id.showGuardian:
                if (!isGuardian) {
                    ll_guardian.setVisibility(View.GONE);
                    ll_guardian2.setVisibility(View.GONE);
                    showGuardian.animate().rotation(showGuardian.getRotation() - 180).start();
                    isGuardian = true;
                } else {
                    showGuardian.animate().rotation(showGuardian.getRotation() - 180).start();
                    ll_guardian.setVisibility(View.VISIBLE);
                    ll_guardian2.setVisibility(View.VISIBLE);
                    isGuardian = false;
                }
                break;
            case R.id.showAttendance:
                if (!isPiechart) {
                    pieChart.setVisibility(View.GONE);
                    showAttendance.animate().rotation(showAttendance.getRotation() - 180).start();
                    isPiechart = true;
                } else {
                    showAttendance.animate().rotation(showAttendance.getRotation() - 180).start();
                    pieChart.setVisibility(View.VISIBLE);
                    isPiechart = false;
                }
                break;
            case R.id.iv_edit_fatherinfo:

                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.student_profile_update_father_info_dialog, null);
                //cancelReason = dialogView.findViewById(R.id.edt_comment);
                et_father_name = (EditText) dialogView.findViewById(R.id.et_name);
                et_father_cnic = dialogView.findViewById(R.id.et_cnic);
                et_father_occupation = dialogView.findViewById(R.id.et_occupation);

                android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
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
                        Button button = ((android.app.AlertDialog) dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                        try {
                            if (!isNationalityPakistani()) {
                                setEditTextMaxLength(et_father_cnic, 15);
                            }

                            if (isFlagShipSchool()) {
                                setEditTextMaxLength(et_father_cnic, 15);
                            }
                            sm = DatabaseHelper.getInstance(StudentProfileActivity.this).getStudentwithGR(grNo,
                                    schoolId);
                            if (sm.getFathersName() != null) {
                                if (!sm.getFathersName().isEmpty()) {
                                    et_father_name.setText(sm.getFathersName());
                                }
                            }
                            if (sm.getFatherNic() != null) {
                                if (!sm.getFatherNic().isEmpty()) {
                                    et_father_cnic.setText(sm.getFatherNic());
                                }
                            }
                            if (sm.getFatherOccupation() != null) {
                                if (!sm.getFatherOccupation().isEmpty()) {
                                    et_father_occupation.setText(sm.getFatherOccupation());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (validateFatherInfo()) {
                                    tv_father_name.setText(AppModel.getInstance().changeStringCase(et_father_name.getText().toString()));
                                    tv_father_cnic.setText(et_father_cnic.getText().toString());
                                    tv_father_occupation.setText(et_father_occupation.getText().toString());
                                    hideKeyboard(dialog);
                                    updateStudent(false);
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                dialog.show();
                break;
            case R.id.iv_edit_motherinfo:

                inflater = this.getLayoutInflater();
                dialogView = inflater.inflate(R.layout.student_profile_update_mother_info_dialog, null);
                //cancelReason = dialogView.findViewById(R.id.edt_comment);
                et_mother_name = (EditText) dialogView.findViewById(R.id.et_name);
                et_mother_cnic = dialogView.findViewById(R.id.et_cnic);
                et_mother_occupation = dialogView.findViewById(R.id.et_occupation);

                dialog = new android.app.AlertDialog.Builder(this)
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
                        Button button = ((android.app.AlertDialog) dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                        try {
                            if (!isNationalityPakistani()) {
                                setEditTextMaxLength(et_mother_cnic, 15);
                            }
                            if (isFlagShipSchool()) {
                                setEditTextMaxLength(et_mother_cnic, 15);
                            }
                            sm = DatabaseHelper.getInstance(StudentProfileActivity.this).getStudentwithGR(grNo,
                                    schoolId);
                            if (sm.getMotherName() != null) {
                                if (!sm.getMotherName().isEmpty()) {
                                    et_mother_name.setText(sm.getMotherName());
                                }
                            }
                            if (sm.getMotherNic() != null) {
                                if (!sm.getMotherNic().isEmpty()) {
                                    et_mother_cnic.setText(sm.getMotherNic());
                                }
                            }
                            if (sm.getMotherOccupation() != null) {
                                if (!sm.getMotherOccupation().isEmpty()) {
                                    et_mother_occupation.setText(sm.getMotherOccupation());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (validateMotherInfo()) {
                                    tv_mother_name.setText(et_mother_name.getText().toString());
                                    tv_mother_cnic.setText(et_mother_cnic.getText().toString());
                                    tv_mother_occupation.setText(et_mother_occupation.getText().toString());
                                    hideKeyboard(dialog);
                                    updateStudent(false);
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                dialog.show();
                break;
            case R.id.iv_edit_guardianinfo:

                inflater = this.getLayoutInflater();
                dialogView = inflater.inflate(R.layout.student_profile_update_guardian_info_dialog, null);
                //cancelReason = dialogView.findViewById(R.id.edt_comment);
                et_guardian_name = (EditText) dialogView.findViewById(R.id.et_name);
                et_guardian_cnic = dialogView.findViewById(R.id.et_cnic);
                et_guardian_occupation = dialogView.findViewById(R.id.et_occupation);

                dialog = new android.app.AlertDialog.Builder(this)
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
                        Button button = ((android.app.AlertDialog) dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                        try {
                            if (!isNationalityPakistani()) {
                                setEditTextMaxLength(et_guardian_cnic, 15);
                            }
                            if (isFlagShipSchool()) {
                                setEditTextMaxLength(et_guardian_cnic, 15);
                            }
                            sm = DatabaseHelper.getInstance(StudentProfileActivity.this).getStudentwithGR(grNo,
                                    schoolId);
                            if (sm.getMotherName() != null) {
                                if (!sm.getGuardianName().isEmpty()) {
                                    et_guardian_name.setText(sm.getGuardianName());
                                }
                            }
                            if (sm.getGuardianNic() != null) {
                                if (!sm.getGuardianNic().isEmpty()) {
                                    et_guardian_cnic.setText(sm.getGuardianNic());
                                }
                            }
                            if (sm.getGuardianOccupation() != null) {
                                if (!sm.getGuardianOccupation().isEmpty()) {
                                    et_guardian_occupation.setText(sm.getGuardianOccupation());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (validateGuardianInfo()) {
                                    tv_guardian_name.setText(et_guardian_name.getText().toString());
                                    tv_guardian_cnic.setText(et_guardian_cnic.getText().toString());
                                    tv_guardian_occupation.setText(et_guardian_occupation.getText().toString());
                                    hideKeyboard(dialog);
                                    updateStudent(false);
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                dialog.show();
                break;
            case R.id.iv_edit_basicinfo:

                inflater = this.getLayoutInflater();
                dialogView = inflater.inflate(R.layout.student_profile_update_basic_info_dialog, null);
                //cancelReason = dialogView.findViewById(R.id.edt_comment);
                et_name = (EditText) dialogView.findViewById(R.id.et_name);
                et_father_name = (EditText) dialogView.findViewById(R.id.et_father_name);
                LinearLayout ll_father_name = dialogView.findViewById(R.id.ll_father_name);
                LinearLayout ll_nic = dialogView.findViewById(R.id.ll_nic);
                FrameLayout profileLayout = dialogView.findViewById(R.id.profile_image_layout);

                header2 = dialogView.findViewById(R.id.header2);
                iv_edit_profileImg2 = dialogView.findViewById(R.id.iv_edit_profileImg2);
                iv_edit_profileImg2.setOnClickListener(this);
                setStudentProfileImageInEditBasicInfo();


                spn_gender = dialogView.findViewById(R.id.spn_gender);
                et_GrNo = dialogView.findViewById(R.id.et_GrNo);
                spn_class_section_name = dialogView.findViewById(R.id.spn_class_section_name);
                et_dob = dialogView.findViewById(R.id.et_dob);
                et_dob_listener = et_dob.getKeyListener();
                et_dob.setKeyListener(et_dob_listener);
                ll_bform = dialogView.findViewById(R.id.ll_bform);
                ll_email = dialogView.findViewById(R.id.ll_email);
                ll_electiveSubject = dialogView.findViewById(R.id.ll_electiveSubject);
                ll_contact = dialogView.findViewById(R.id.ll_contact);
                ll_orphan = dialogView.findViewById(R.id.ll_orphan);
                ll_deathCertificate = dialogView.findViewById(R.id.ll_deathCertificate);
                ll_disabled = dialogView.findViewById(R.id.ll_disabled);
                ll_medicalCertificate = dialogView.findViewById(R.id.ll_medicalCertificate);
                ll_religion = dialogView.findViewById(R.id.ll_religion);
                ll_nationality = dialogView.findViewById(R.id.ll_nationality);
                ll_parent_orphan = dialogView.findViewById(R.id.ll_parent_orphan);
                ll_parent_disabled = dialogView.findViewById(R.id.ll_parent_disabled);
                et_formb = dialogView.findViewById(R.id.et_formb);
                et_email = dialogView.findViewById(R.id.et_email);
                et_contact = dialogView.findViewById(R.id.et_contact);
                et_tution_fees = dialogView.findViewById(R.id.et_tution_fees);

                iv_death_cert_capture = dialogView.findViewById(R.id.iv_death_cert_capture);
                btn_death_cert_capture = dialogView.findViewById(R.id.btn_death_cert_capture);
                btn_death_cert_preview = dialogView.findViewById(R.id.btn_death_cert_preview);
                btn_death_cert_remove = dialogView.findViewById(R.id.btn_death_cert_remove);
                btn_death_cert_capture.setOnClickListener(this);
                btn_death_cert_preview.setOnClickListener(this);
                btn_death_cert_remove.setOnClickListener(this);

                iv_medical_cert_capture = dialogView.findViewById(R.id.iv_medical_cert_capture);
                btn_medical_cert_capture = dialogView.findViewById(R.id.btn_medical_cert_capture);
                btn_medical_cert_preview = dialogView.findViewById(R.id.btn_medical_cert_preview);
                btn_medical_cert_remove = dialogView.findViewById(R.id.btn_medical_cert_remove);
                btn_medical_cert_capture.setOnClickListener(this);
                btn_medical_cert_preview.setOnClickListener(this);
                btn_medical_cert_remove.setOnClickListener(this);

                iv_nic_capture = dialogView.findViewById(R.id.iv_nic_capture);
                btn_nic_capture = dialogView.findViewById(R.id.btn_nic_capture);
                btn_nic_preview = dialogView.findViewById(R.id.btn_nic_preview);
                btn_nic_remove = dialogView.findViewById(R.id.btn_nic_remove);
                btn_nic_capture.setOnClickListener(this);
                btn_nic_preview.setOnClickListener(this);
                btn_nic_remove.setOnClickListener(this);
                if (!isFlagShipAndNine()) {
                    ll_father_name.setVisibility(View.GONE);
                    profileLayout.setVisibility(View.GONE);
                    ll_nic.setVisibility(View.GONE);

                } else {
                    ll_father_name.setVisibility(View.VISIBLE);
                    profileLayout.setVisibility(View.VISIBLE);
                    ll_nic.setVisibility(View.VISIBLE);
                    setNICImage();
                }
//                iv_nic_capture.setImageBitmap();


                LinearLayout ll_tuition_fees = dialogView.findViewById(R.id.ll_TuitionFee);
                ll_scholarshipCat = (LinearLayout) dialogView.findViewById(R.id.ll_scholarship_cat);
                tv_scholarship_cat = dialogView.findViewById(R.id.tv_scholarship_cat);

                ReligionSpinner = dialogView.findViewById(R.id.spn_religion);
                NationalitySpinner = dialogView.findViewById(R.id.spn_nationality);
                ElectiveSubjectSpinner = dialogView.findViewById(R.id.spn_elective_subject);
                OrphanSpinner = dialogView.findViewById(R.id.spn_orphan);
                DisabledSpinner = dialogView.findViewById(R.id.spn_disabled);
                initSpinners();


                dialog = new android.app.AlertDialog.Builder(this)
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
                            ScholarshipCategoryModel model = Scholarship_Category.getInstance(StudentProfileActivity.this).getScholarshipCategory(
                                    schoolId,
                                    (et_tution_fees.getText().toString().isEmpty() ? -1 : Integer.parseInt(et_tution_fees.getText().toString().trim())));

                            if (model.getScholarship_category_description() != null && !model.getScholarship_category_description().isEmpty()) {
                                tv_scholarship_cat.setText(model.getScholarship_category_description());
                                ll_scholarshipCat.setVisibility(View.VISIBLE);

                                scholarshipCategoryId = model.getScholarship_category_id();
                            } else {
                                scholarshipCategoryId = 0;
                                Toast.makeText(StudentProfileActivity.this, "Scholarship Category not found", Toast.LENGTH_LONG).show();
                                ll_scholarshipCat.setVisibility(View.GONE);
                            }

                            hideKeyboard(dialog);
                            return true;
                        }
                        return false;
                    }
                });


                et_dob.setInputType(InputType.TYPE_NULL);
                et_dob.setOnClickListener(this);
                et_dob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppModel.getInstance().DatePicker2(et_dob, StudentProfileActivity.this);
                    }
                });


                dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button button = ((android.app.AlertDialog) dialog).getButton(android.app.AlertDialog.BUTTON_POSITIVE);

//                        showOrHideViesForBothFSAndPSAndEightNineClassOnly();
//                        showOrHideViesForBothFSAndEightNineClassOnly();

                        try {
                            sm = DatabaseHelper.getInstance(StudentProfileActivity.this).getStudentwithGR(grNo,
                                    schoolId);
                            if (sm.getName() != null) {
                                if (!sm.getName().isEmpty()) {
                                    et_name.setText(sm.getName());
                                }
                            }
                            if (sm.getFathersName() != null) {
                                if (!sm.getFathersName().isEmpty()) {
                                    et_father_name.setText(sm.getFathersName());
                                }
                            }
                            if (sm.getGender() != null) {
                                if (sm.getGender().toLowerCase().equals("m")) {
                                    spn_gender.setSelection(1);
                                } else {
                                    spn_gender.setSelection(2);
                                }
                            }
                            if (sm.getGrNo() != null) {
                                if (!sm.getGrNo().isEmpty()) {
                                    et_GrNo.setText(sm.getGrNo());
                                }
                            }
                            for (int b = 0; b < classSectionModel.getClassAndSectionsList().size(); b++) {
                                try {
                                    String concatClassSection = sm.getCurrentClass().toLowerCase().trim().concat(" " + sm.getCurrentSection().toLowerCase().trim());

                                    if (classSectionModel.getClassAndSectionsList().get(b).getClass_section_name().toLowerCase().trim().equals(concatClassSection)) {
                                        spn_class_section_name.setSelection(b);
                                        tempSchoolClassID = ((ClassSectionModel) spn_class_section_name.getSelectedItem()).getSchoolClassId();
                                        break;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (sm.getDob() != null) {
                                if (!sm.getDob().isEmpty()) {
                                    et_dob.setText(AppModel.getInstance().convertDatetoFormat(sm.getDob(), "yyyy-MM-dd", "dd-MMM-yyyy"));

                                }
                            }

                            if (sm.getFormB() != null) {
                                if (!sm.getFormB().isEmpty()) {

                                    et_formb.setText(sm.getFormB());
                                    ////////////////
                                    et_formb.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            //et_formb.setError("onTextChanged");
                                            bFormNoChange = true;

                                            if (isFlagShipAndNine()) {//Muhammad Salman Saleem
                                                if (nationalitySelectedModel != null && nationalitySelectedModel.getTitle().equals("Pakistani")) {
                                                    setEditTextMaxLength(et_formb,13);
                                                }
                                            }//Muhammad Salman Saleem

                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {

                                        }
                                    });
                                    //////////////////
                                }
                            }

                            if (sm.getContactNumbers() != null) {
                                if (!sm.getContactNumbers().isEmpty()) {
                                    et_contact.setText(sm.getContactNumbers());
                                }
                            }
                            if (isFinance || DatabaseHelper.getInstance(StudentProfileActivity.this).isRegionIsInFlagShipSchool(schoolId)) {
                                et_tution_fees.setText(String.valueOf((int) sm.getActualFees()));
                            } else {
                                ll_tuition_fees.setVisibility(View.GONE);
                            }

                            if (sm.getEmail() != null) {
                                if (!sm.getEmail().isEmpty()) {
                                    et_email.setText(sm.getEmail());
                                }
                            }
                            if (sm.getElectiveSubjectId() != 0) {
                                ll_electiveSubject.setVisibility(View.VISIBLE);
                                ArrayList<ElectiveSubjectModel> mList = GlobalHelperClass.getInstance(StudentProfileActivity.this).getAllElectiveSubjects();
                                mList.add(0, new ElectiveSubjectModel(0, getString(R.string.please_select)));
                                for (int b = 0; b < mList.size(); b++) {
                                    try {
                                        int electiveSub = sm.getElectiveSubjectId();

                                        if (mList.get(b).getId() == electiveSub) {
                                            ElectiveSubjectSpinner.setSelection(b);
                                            break;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        button.setOnClickListener(new View.OnClickListener() {//475507-658
                            @Override
                            public void onClick(View view) {
                                if (validateBasicInfo()) {
                                    tv_student_name.setText(AppModel.getInstance().changeStringCase(et_name.getText().toString()));

                                    if (spn_gender.getSelectedItemPosition() == 1) {
                                        tv_section_student_Gender.setText("Male");
                                    } else {
                                        tv_section_student_Gender.setText("Female");
                                    }

                                    tv_student_GR_No.setText(et_GrNo.getText().toString());
                                    tv_dob.setText(et_dob.getText().toString());
                                    tv_formB.setText(et_formb.getText().toString());
                                    tv_contact.setText(et_contact.getText().toString());
                                    tv_father_name.setText(et_father_name.getText().toString());
                                    if (isFinance || DatabaseHelper.getInstance(StudentProfileActivity.this).isRegionIsInFlagShipSchool(schoolId)) {
                                        txt_monthlyfees.setText("RS. " + et_tution_fees.getText().toString());
                                    }
                                    txt_class_sec.setText(spn_class_section_name.getSelectedItem().toString().replace("Class-", ""));
                                    classId = ((ClassSectionModel) spn_class_section_name.getSelectedItem()).getClassId();
                                    section = ((ClassSectionModel) spn_class_section_name.getSelectedItem()).getSectionId();

                                    isBasicInfoEdit = true;
                                    hideKeyboard(dialog);
                                    updateStudent(true);

                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                });
                dialog.show();
                break;
            case R.id.btn_death_cert_capture:
                try {
                    PickImageDialog.build(new PickSetup().setTitle("Death Certificate Image")
                            .setPickTypes(EPickType.CAMERA, EPickType.GALLERY)).show(this).setOnPickResult(new IPickResult() {
                        @Override
                        public void onPickResult(PickResult r) {

                            try {
                                if (r.getError() == null) {
                                    imgDeathCertificate = AppModel.getInstance().bitmapToByte(r.getBitmap());
                                    sm.setDeathCert_Image(r.getPath());
                                    iv_death_cert_capture.setImageBitmap(r.getBitmap());
                                } else {
                                    Toast.makeText(StudentProfileActivity.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                AppModel.getInstance().appendErrorLog(StudentProfileActivity.this, "Error in Imageview:" + e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    AppModel.getInstance().appendErrorLog(this, "Error in Imageview:" + e.getMessage());
                }

                break;
            case R.id.btn_death_cert_preview:
                previewImage(iv_death_cert_capture.getDrawable());
                break;
            case R.id.btn_death_cert_remove:
                imgDeathCertificate = null;
                sm.setDeathCert_Image("");
                Picasso.with(this).load(R.mipmap.death_cert).into(iv_death_cert_capture);
                break;
            case R.id.btn_medical_cert_capture:
                try {
                    PickImageDialog.build(new PickSetup().setTitle("Medical Certificate Image")
                            .setPickTypes(EPickType.CAMERA, EPickType.GALLERY)).show(this).setOnPickResult(new IPickResult() {
                        @Override
                        public void onPickResult(PickResult r) {

                            try {
                                if (r.getError() == null) {
                                    imgMedicalCertificate = AppModel.getInstance().bitmapToByte(r.getBitmap());
                                    sm.setMedicalCert_Image(r.getPath());
                                    iv_medical_cert_capture.setImageBitmap(r.getBitmap());
                                } else {
                                    Toast.makeText(StudentProfileActivity.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                AppModel.getInstance().appendErrorLog(StudentProfileActivity.this, "Error in Imageview:" + e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    AppModel.getInstance().appendErrorLog(this, "Error in Imageview:" + e.getMessage());
                }

                break;
            case R.id.btn_medical_cert_preview:
                previewImage(iv_medical_cert_capture.getDrawable());
                break;
            case R.id.btn_medical_cert_remove:
                imgMedicalCertificate = null;
                sm.setMedicalCert_Image("");
                Picasso.with(this).load(R.mipmap.medical_cert).into(iv_medical_cert_capture);
                break;
            case R.id.btn_nic_capture:
                try {
                    PickImageDialog.build(new PickSetup().setTitle("BForm Image")
                            .setPickTypes(EPickType.CAMERA, EPickType.GALLERY)).show(this).setOnPickResult(new IPickResult() {
                        @Override
                        public void onPickResult(PickResult r) {

                            try {
                                if (r.getError() == null) {
                                    imgBForm = AppModel.getInstance().bitmapToByte(r.getBitmap());
                                    sm.setbForm_Image(r.getPath());
                                    iv_nic_capture.setImageBitmap(r.getBitmap());
                                } else {
                                    Toast.makeText(StudentProfileActivity.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                AppModel.getInstance().appendErrorLog(StudentProfileActivity.this, "Error in Imageview:" + e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    AppModel.getInstance().appendErrorLog(this, "Error in Imageview:" + e.getMessage());
                }

                break;
            case R.id.btn_nic_preview:
                previewImage(iv_nic_capture.getDrawable());
                break;
            case R.id.btn_nic_remove:
                imgBForm = null;
                sm.setbForm_Image("");
                Picasso.with(this).load(R.mipmap.bform).into(iv_nic_capture);
                break;
        }
    }

    private void setStudentProfileImageInEditBasicInfo() {
        try {
            if (sm.getPictureName() != null) {
                File f;
                String fdir = StorageUtil.getSdCardPath(this).getAbsolutePath() + "/TCF/TCF Images";
                if (sm.getPictureName().contains("BodyPart"))
                    f = new File(fdir + "/" + sm.getPictureName());
                else
                    f = new File(fdir + "/" + sm.getPictureName());

                if (f.exists()) {
                    Bitmap bitmap = Compressor.getDefault(this).compressToBitmap(f);
                    header2.setImageBitmap(bitmap);
                }
            } else {
                header2.setImageDrawable(header.getDrawable());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setNICImage() {
        try {
            if (sm.getbForm_Image() != null && !sm.getbForm_Image().trim().equals("")) {
                File f;
                String fdir = StorageUtil.getSdCardPath(StudentProfileActivity.this).getAbsolutePath() + "/TCF/TCF Images";
                f = new File(fdir + "/" + sm.getbForm_Image());
                if (f.exists()) {
                    Bitmap bitmap = Compressor.getDefault(StudentProfileActivity.this).compressToBitmap(f);
                    iv_nic_capture.setImageBitmap(bitmap);
                    imgBForm = AppModel.getInstance().bitmapToByte(bitmap);
                } else {
                    iv_nic_capture.setImageDrawable(ContextCompat.getDrawable(StudentProfileActivity.this, R.drawable.ic_file_download));
                    iv_nic_capture.setBackgroundColor(ContextCompat.getColor(this, R.color.app_green));
                    iv_nic_capture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String url = AppModel.getInstance().readFromSharedPreferences(StudentProfileActivity.this, AppConstants.imagebaseurlkey) + sm.getbForm_Image();

                            BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                                @Override
                                public void onError(BasicImageDownloader.ImageError error) {
                                    Log.d("imageDownloadError", error.getMessage());
                                    String errorMessage;
                                    if (error.getErrorCode() == -1)
                                        errorMessage = "Download Failed: No Internet Connection";
                                    else
                                        errorMessage = "Download Failed: " + error.getMessage();
                                    Toast.makeText(StudentProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onProgressChange(int percent) {

                                }

                                @Override
                                public void onComplete(Bitmap result) {
                                    BasicImageDownloader.writeToDisk(f, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                        @Override
                                        public void onBitmapSaved() {
                                            Bitmap bitmap = Compressor.getDefault(StudentProfileActivity.this).compressToBitmap(f);
                                            iv_nic_capture.setImageBitmap(bitmap);
                                            imgBForm = AppModel.getInstance().bitmapToByte(bitmap);
                                        }

                                        @Override
                                        public void onBitmapSaveError(BasicImageDownloader.ImageError error) {

                                            Toast.makeText(StudentProfileActivity.this, "Image not saved: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }, Bitmap.CompressFormat.JPEG, true);
                                }
                            });
                            downloader.download(url, false);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*if (!Strings.isEmptyOrWhitespace(sm.getGrNo())) {
            int idUpdate = DatabaseHelper.getInstance(this).getEnrollmentIdfromGR(Integer.parseInt(sm.getGrNo()), schoolId);
            if (idUpdate > 0) {
                EnrollmentImageModel enrollmentImageModel1 = DatabaseHelper.getInstance(this).getEnrollmentImage(idUpdate, "B");
                if (enrollmentImageModel1.getFilename() != null && !enrollmentImageModel1.getFilename().equals("")) {

                    File f1;
                    String fdir = StorageUtil.getSdCardPath(this).getAbsolutePath() + "/TCF/TCF Images";
                    if (enrollmentImageModel1.getFilename().contains("BodyPart"))
                        f1 = new File(fdir + "/" + enrollmentImageModel1.getFilename());
                    else
                        f1 = new File(fdir + "/" + enrollmentImageModel1.getFilename());

                    Bitmap bitmap1 = AppModel.getInstance().rotateImage(Compressor.getDefault(this).compressToBitmap(f1), getWindowManager().getDefaultDisplay());
                    iv_nic_capture.setImageBitmap(bitmap1);
                }

            }
        }*/

    }

    private void setDisabledFields() {
        if (sm.isDisabled() != null) {
            String[] mList = getResources().getStringArray(R.array.no_yes_array);
            for (int b = 0; b < mList.length; b++) {
                try {
                    String disabled = sm.isDisabled().toLowerCase().trim();

                    if (mList[b].toLowerCase().trim().equals(disabled)) {
                        DisabledSpinner.setSelection(b);
//                        if (disabled.equals("yes")){
//                            setMedicalCertificateImage();
//                        }
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setOrphanFields() {
        if (sm.isOrphan() != null) {
            String[] mList = getResources().getStringArray(R.array.no_yes_array);
            for (int b = 0; b < mList.length; b++) {
                try {
                    String orphan = sm.isOrphan().toLowerCase().trim();

                    if (mList[b].toLowerCase().trim().equals(orphan)) {
                        OrphanSpinner.setSelection(b);
//                        if (orphan.equals("yes")){
//                            setDeathCertificateImage();
//                        }
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setDeathCertificateImage() {
        try {
            if (sm.getDeathCert_Image() != null) {
                File f;
                String fdir = StorageUtil.getSdCardPath(StudentProfileActivity.this).getAbsolutePath() + "/TCF/TCF Images";
                f = new File(fdir + "/" + sm.getDeathCert_Image());
                if (f.exists()) {
                    Bitmap bitmap = Compressor.getDefault(StudentProfileActivity.this).compressToBitmap(f);
                    iv_death_cert_capture.setImageBitmap(bitmap);
                    imgDeathCertificate = AppModel.getInstance().bitmapToByte(bitmap);
                } else {
                    iv_death_cert_capture.setImageDrawable(ContextCompat.getDrawable(StudentProfileActivity.this, R.drawable.ic_file_download));
                    iv_death_cert_capture.setBackgroundColor(ContextCompat.getColor(this, R.color.app_green));
                    iv_death_cert_capture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String url = AppModel.getInstance().readFromSharedPreferences(StudentProfileActivity.this, AppConstants.imagebaseurlkey) + sm.getDeathCert_Image();

                            BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                                @Override
                                public void onError(BasicImageDownloader.ImageError error) {
                                    Log.d("imageDownloadError", error.getMessage());
                                    String errorMessage;
                                    if (error.getErrorCode() == -1)
                                        errorMessage = "Download Failed: No Internet Connection";
                                    else
                                        errorMessage = "Download Failed: " + error.getMessage();
                                    Toast.makeText(StudentProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onProgressChange(int percent) {

                                }

                                @Override
                                public void onComplete(Bitmap result) {
                                    BasicImageDownloader.writeToDisk(f, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                        @Override
                                        public void onBitmapSaved() {
                                            Bitmap bitmap = Compressor.getDefault(StudentProfileActivity.this).compressToBitmap(f);
                                            iv_death_cert_capture.setImageBitmap(bitmap);
                                            imgDeathCertificate = AppModel.getInstance().bitmapToByte(bitmap);
                                        }

                                        @Override
                                        public void onBitmapSaveError(BasicImageDownloader.ImageError error) {

                                            Toast.makeText(StudentProfileActivity.this, "Image not saved: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }, Bitmap.CompressFormat.JPEG, true);
                                }
                            });
                            downloader.download(url, false);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMedicalCertificateImage() {
        try {
            if (sm.getMedicalCert_Image() != null) {
                File f;
                String fdir = StorageUtil.getSdCardPath(StudentProfileActivity.this).getAbsolutePath() + "/TCF/TCF Images";
                f = new File(fdir + "/" + sm.getMedicalCert_Image());
                if (f.exists()) {
                    Bitmap bitmap = Compressor.getDefault(StudentProfileActivity.this).compressToBitmap(f);
                    iv_medical_cert_capture.setImageBitmap(bitmap);
                    imgMedicalCertificate = AppModel.getInstance().bitmapToByte(bitmap);
                } else {
                    iv_medical_cert_capture.setImageDrawable(ContextCompat.getDrawable(StudentProfileActivity.this, R.drawable.ic_file_download));
                    iv_medical_cert_capture.setBackgroundColor(ContextCompat.getColor(this, R.color.app_green));
                    iv_medical_cert_capture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String url = AppModel.getInstance().readFromSharedPreferences(StudentProfileActivity.this, AppConstants.imagebaseurlkey) + sm.getMedicalCert_Image();

                            BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                                @Override
                                public void onError(BasicImageDownloader.ImageError error) {
                                    Log.d("imageDownloadError", error.getMessage());
                                    String errorMessage;
                                    if (error.getErrorCode() == -1)
                                        errorMessage = "Download Failed: No Internet Connection";
                                    else
                                        errorMessage = "Download Failed: " + error.getMessage();
                                    Toast.makeText(StudentProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onProgressChange(int percent) {

                                }

                                @Override
                                public void onComplete(Bitmap result) {
                                    BasicImageDownloader.writeToDisk(f, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                        @Override
                                        public void onBitmapSaved() {
                                            Bitmap bitmap = Compressor.getDefault(StudentProfileActivity.this).compressToBitmap(f);
                                            iv_medical_cert_capture.setImageBitmap(bitmap);
                                            imgMedicalCertificate = AppModel.getInstance().bitmapToByte(bitmap);
                                        }

                                        @Override
                                        public void onBitmapSaveError(BasicImageDownloader.ImageError error) {

                                            Toast.makeText(StudentProfileActivity.this, "Image not saved: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }, Bitmap.CompressFormat.JPEG, true);
                                }
                            });
                            downloader.download(url, false);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOrHideViewsForBothFSAndNineClassOnly() {
        if (isFlagShipAndNine()) {
            ll_email.setVisibility(View.VISIBLE);
            ll_electiveSubject.setVisibility(View.VISIBLE);

            ll_parent_orphan.setVisibility(View.VISIBLE);
            ll_parent_disabled.setVisibility(View.VISIBLE);
            setOrphanFields();
            setDisabledFields();
        } else {
            ll_email.setVisibility(View.GONE);
            ll_electiveSubject.setVisibility(View.GONE);

            ll_parent_orphan.setVisibility(View.GONE);
            ll_parent_disabled.setVisibility(View.GONE);
            is_orphan = "";
            is_disabled = "";
            imgDeathCertificate = null;
            imgMedicalCertificate = null;
        }
    }

    private void showOrHideViewsForBothFSAndPSAndEightNineClassOnly() {
        //FS and PS open for both but only 8 and 9 class
//        if(DatabaseHelper.getInstance(StudentProfileActivity.this).isEightAndNineClass(schoolId, classId, sectionID)){
        ll_religion.setVisibility(View.VISIBLE);
        ll_nationality.setVisibility(View.VISIBLE);
        setReligionField();
        setNationalityField();
//        }
        /*else{
            ll_religion.setVisibility(View.GONE);
            ll_nationality.setVisibility(View.GONE);

            religionSelectedModel = null;
            nationalitySelectedModel = null;
        }*/
    }

    private void setNationalityField() {
        if (sm.getNationality() != null) {
            ArrayList<NationalityModel> mList = GlobalHelperClass.getInstance(StudentProfileActivity.this).getAllNationalities();
            for (int b = 0; b < mList.size(); b++) {
                try {
                    String nationality = sm.getNationality().toLowerCase().trim();

                    if (mList.get(b).getTitle().toLowerCase().trim().equals(nationality)) {
                        NationalitySpinner.setSelection(b);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setReligionField() {
        if (sm.getReligion() != null) {
            ArrayList<ReligionModel> mList = GlobalHelperClass.getInstance(StudentProfileActivity.this).getAllReligions();
            for (int b = 0; b < mList.size(); b++) {
                try {
                    String religion = sm.getReligion().toLowerCase().trim();

                    if (mList.get(b).getTitle().toLowerCase().trim().equals(religion)) {
                        ReligionSpinner.setSelection(b);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int validateCnic(String CNIC1, String CNIC2, String CNIC3, EditText editText) {
        int allOk = 0;
        if (isCnicValid(CNIC2, null) || isCnicValid(CNIC3, null)) {
            if (!isCnicValid(CNIC1, editText)) {
                allOk++;
            } else {
                editText.setError(null);
            }
        } else {
            if (Strings.isEmptyOrWhitespace(CNIC1)) {
                editText.setError("Required");
                allOk++;
            } else if (!isCnicValid(CNIC1, editText)) {
                allOk++;
            } else {
                editText.setError(null);
            }
        }

//        if (isCnicValid(CNIC2, null) || isCnicValid(CNIC3, null)) {
//            if (!isValidCNICNumber(CNIC1,editText)) {
//                allOk++;
//            } else {
//                editText.setError(null);
//            }
//        } else {
//            if (Strings.isEmptyOrWhitespace(CNIC1)) {
//                editText.setError("Required");
//                allOk++;
//            } else if (!isValidCNICNumber(CNIC1,editText)) {
//                allOk++;
//            } else {
//                editText.setError(null);
//            }
//        }
        return allOk;
    }

    private boolean validateFatherInfo() {
        int allOk = 0;
        if (Strings.isEmptyOrWhitespace(et_father_name.getText().toString())) {
            et_father_name.setError("Required");
            allOk++;
        } else {
            et_father_name.setError(null);
        }

        if (DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(schoolId)) {
            if (Strings.isEmptyOrWhitespace(et_father_cnic.getText().toString())) {
                et_father_cnic.setError("Required");
                allOk++;
            } else {
                et_father_cnic.setError(null);
            }
        } else {
            allOk += validateCnic(et_father_cnic.getText().toString(), sm.getMotherNic(), sm.getGuardianNic(), et_father_cnic);
        }

        if (Strings.isEmptyOrWhitespace(et_father_occupation.getText().toString())) {
            et_father_occupation.setError("Required");
            allOk++;
        } else {
            et_father_occupation.setError(null);
        }

        return allOk == 0;
    }

    private boolean validateMotherInfo() {
        int allOk = 0;
        if (Strings.isEmptyOrWhitespace(et_mother_name.getText().toString())) {
            et_mother_name.setError("Required");
            allOk++;
        } else {
            et_mother_name.setError(null);
        }

        if (DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(schoolId)) {
            if (Strings.isEmptyOrWhitespace(et_mother_cnic.getText().toString())) {
                et_mother_cnic.setError("Required");
                allOk++;
            } else {
                et_mother_cnic.setError(null);
            }
        } else {
            allOk += validateCnic(et_mother_cnic.getText().toString(), sm.getFatherNic(), sm.getGuardianNic(), et_mother_cnic);
        }


        if (Strings.isEmptyOrWhitespace(et_mother_occupation.getText().toString())) {
            et_mother_occupation.setError("Required");
            allOk++;
        } else {
            et_mother_occupation.setError(null);
        }
        return allOk == 0;
    }

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

    // Returns age given the date of birth
    public int getYears(LocalDate dob) {
        LocalDate curDate = LocalDate.now();
        return Period.between(dob, curDate).getYears();
    }

    public int getMonth(LocalDate dob) {
        // LocalDate dateOfBirth = LocalDate.parse(dob);
        LocalDate curDate = LocalDate.now();
        return Period.between(dob, curDate).getMonths();
    }

    public int getDays(LocalDate dob) {
        //LocalDate dateOfBirth = LocalDate.parse(dob);
        LocalDate curDate = LocalDate.now();
        return Period.between(dob, curDate).getDays();
    }

    private int getAgeYears(String dob) {
        LocalDate dateOfBirth = LocalDate.parse(dob);
        return getYears(dateOfBirth);
    }

    private int getAge_(LocalDate dob) {//475518-781

        int years = getYears(dob);
        int months = getMonth(dob);
        int days = getDays(dob);

        // Adjust age based on difference in months and days
        if (months < 0 || (months == 0 && days < 0)) {
            years--;
        }

        return years;
    }


    private void getDate(int age) {
        Calendar now = Calendar.getInstance();


        Calendar calendar;
        int years = now.get(Calendar.YEAR) - age;
        int months = now.get(Calendar.MONTH);
        int days = now.get(Calendar.DAY_OF_MONTH);

        String date = String.valueOf(years) + String.valueOf(months) + String.valueOf(days);

        SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date dateObj = curFormater.parse(date);

            // now.setTime(dateObj);
//            now.set(YEAR,years);
//            now.set();

            now.set(Calendar.YEAR, years);

            now.set(Calendar.MONTH, months);

            now.set(Calendar.DAY_OF_MONTH, days);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // return getAge(now);
    }

    ///////////********Muhammad Salman Saleem(PS-Validation)*******/////////////// --y=12,m=-9,days=-21
    private int getAgeOfStudent(Calendar dob) {
        Calendar now = Calendar.getInstance();
        int years = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);//2023-2012=11   //02-03-2012
        int months = ((now.get(Calendar.MONTH)) - (dob.get(Calendar.MONTH)));// 2 - 2 =0
        int days = now.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);//08-02=1

        System.out.println("Current Year: " + now.get(Calendar.YEAR));
        System.out.println("Current Month: " + now.get(Calendar.MONTH));
        System.out.println("Current Day: " + now.get(Calendar.DAY_OF_MONTH));

        System.out.println("db: " + YEAR);
        System.out.println("years: " + years);
        System.out.println("months: " + months);
        System.out.println("days: " + days);

        // Adjust age based on difference in months and days
        if (months == 0 && days < 0) {//0,-1
            years--;
        } else if (months > 0 || (months == 0 && days > 0)) {
            years++;
        }else if (months < 0 && days < 0) {
            years--;
        }

        //prints the age
        System.out.println("Your are in age: " + years);
        return years;
    }


    private long getNoOfDays(Calendar dob) {
        Calendar now = Calendar.getInstance();
        //24-May-2017, change this to your desired Start Date

        LocalDate dateBefore = LocalDate.of(dob.get(Calendar.YEAR), dob.get(Calendar.MONTH)+1, dob.get(Calendar.DAY_OF_MONTH));
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
    ///////////********Muhammad Salman Saleem(PS-Validation)*******///////////////


    private int getAge(Calendar dob) {//475518-781
        Calendar now = Calendar.getInstance();
//
        int years = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);//
        int months = ((now.get(Calendar.MONTH)) - (dob.get(Calendar.MONTH)));// 2 - 2 =0
        int days = now.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);//02-28=-26

        // Adjust age based on difference in months and days
        if (months < 0 || (months == 0 && days < 0)) {
            years--;
        }

//        String currentDate = String.valueOf(dob.get(Calendar.DAY_OF_MONTH)) + "-" +
//                String.valueOf(dob.get(Calendar.MONTH)) + "-" + String.valueOf(dob.get(Calendar.YEAR));//02-02-2012
//        String finalDate = String.valueOf(now.get(Calendar.DAY_OF_MONTH)) + "-" +
//                String.valueOf(now.get(Calendar.MONTH)) + "-" + String.valueOf(now.get(Calendar.YEAR));//03-03-2023
//
//        double age_years =  daysDifference(currentDate,finalDate)/365;
//        String val = change(age_years,3);
//        //String val = new DecimalFormat("#.00").format(age_years);
//
//        String value = Double.toString(age_years);//11.008219178082191
//       // int first = Integer.parseInt(value.split(".")[0]);
//        String[] r = val.split("[.]");
//
//        if(!r[1].contains("1")) {
////        for(String str : r){
//            //if(r[1].equals("00")){
//            age_years = Integer.parseInt(r[0]);
//        //}
//       }
//11.01095890410959 -2
//11.008219178082191 -3
        return years; //11
    }

    private String change(double value, int decimalpoint) {

        // Using the pow() method
        value = value * Math.pow(10, decimalpoint);
        value = Math.floor(value);
        value = value / Math.pow(10, decimalpoint);

        return Double.toString(value);
    }

    private long getStudentAge(Calendar dob) {
        Calendar now = Calendar.getInstance();
//
        int years = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);//
        int months = ((now.get(Calendar.MONTH)) - (dob.get(Calendar.MONTH)));// 2 - 2 =0
        int days = now.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);//02-28=-26

        // Adjust age based on difference in months and days
        if (months < 0 || (months == 0 && days < 0)) {
            years--;
        }


        return years; //11
    }

    private double daysDifference(String currentDate, String finalDate) {
        //String dayDifference = "";
        double differenceDates = 0;
        try {
//            String CurrentDate= "02/02/2012";
//            String FinalDate= "03/03/2023";
            Date date1;
            Date date2;
            SimpleDateFormat dates = new SimpleDateFormat("dd-MM-yyyy");
            date1 = dates.parse(currentDate);
            date2 = dates.parse(finalDate);
            long difference = Math.abs(date1.getTime() - date2.getTime());
            differenceDates = difference / (24 * 60 * 60 * 1000);
//            dayDifference = Long.toString(differenceDates);
//            System.out.println("days difference = " + dayDifference);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return differenceDates;
    }


    private boolean validateGuardianInfo() {
        int allOk = 0;
        if (Strings.isEmptyOrWhitespace(et_guardian_name.getText().toString())) {
            et_guardian_name.setError("Required");
            allOk++;
        } else {
            et_guardian_name.setError(null);
        }

        if (DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(schoolId)) {
            if (Strings.isEmptyOrWhitespace(et_guardian_cnic.getText().toString())) {
                et_guardian_cnic.setError("Required");
                allOk++;
            } else {
                et_guardian_cnic.setError(null);
            }
        } else {
            allOk += validateCnic(et_guardian_cnic.getText().toString(), sm.getFatherNic(), sm.getMotherNic(), et_guardian_cnic);
        }


        if (Strings.isEmptyOrWhitespace(et_guardian_occupation.getText().toString())) {
            et_guardian_occupation.setError("Required");
            allOk++;
        } else {
            et_guardian_occupation.setError(null);
        }
        return allOk == 0;
    }

    private void previewImage(Drawable previewImageFile) {
        ImagePreviewDialog imagePreviewDialog = new ImagePreviewDialog(this, previewImageFile);
        imagePreviewDialog.show();
    }

    private boolean isFlagShipAndNine() {
        return DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(schoolId)
                && DatabaseHelper.getInstance(this).isNineOrTenClass(schoolId, classId, sectionID);

    }

    /**********************************Muhammad Salman Saleem**************/
    private boolean isFlagShipSchool() {
        return DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(schoolId);
    }

    /********************************Muhammad Salman Saleem**************/

    @Override
    protected void onResume() {
        super.onResume();

        //Important when any change in table call this method
//        AppModel.getInstance().changeMenuPendingSyncCount(StudentProfileActivity.this, true);

        working();

        DrawChart();
        //Log.d("last30days","Count:"+DatabaseHelper.getInstance(StudentProfileActivity.this).getAttendanceOfstudent30days(String.valueOf(schoolId),Integer.parseInt(student_id)));

        if (profileBitmap != null) {
            header.setImageBitmap(profileBitmap);
            if (header2 != null)
                header2.setImageBitmap(profileBitmap);

            byte[] Student_Profile = AppModel.getInstance().bitmapToByte(profileBitmap);
            student_profile_Path = AppModel.getInstance().saveImageToStorage2(Student_Profile, view.getContext(), sm.getGrNo(), 1, schoolId);

            if (!isBasicInfoEdit) {
                updateStudent(false);
            }

            profileBitmap = null;
        }


        if (sm.getStudent_promotionStatus() != null && sm.getStudent_promotionStatus().equals("P"))
            hide_edit_markers();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                String path = resultUri.getPath();

                Bitmap thumbBitmap;
                thumbBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 100, 100);

                header.setImageBitmap(thumbBitmap);
                header.setBackground(null);

                if (header2 != null) {
                    header2.setImageBitmap(thumbBitmap);
                    header2.setBackground(null);
                }

                byte[] Student_Profile = AppModel.getInstance().bitmapToByte(thumbBitmap);
                student_profile_Path = AppModel.getInstance().saveImageToStorage2(Student_Profile, view.getContext(), sm.getGrNo(), 1, schoolId);

                if (!isBasicInfoEdit) {
                    updateStudent(false);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                ShowToast(error.getMessage());
            }
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            if (data.hasExtra("grno") && data.getStringExtra("grno") != null)
                grNo = Integer.parseInt(data.getStringExtra("grno"));
        }
    }


    private void updateStudent(boolean fromBasicInfo) {

        if (isFlagShipAndNine()) {
            sm.setStudent_promotionStatus("P");
        }
        if (!isBasicInfoEdit) {
            sm.getSchoolClassId();
            classSectionModel = new ClassSectionModel();
            classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(String.valueOf(schoolId)));
            for (int b = 0; b < classSectionModel.getClassAndSectionsList().size(); b++) {
                try {
                    String concatClassSection = sm.getCurrentClass().toLowerCase().trim().concat(" " + sm.getCurrentSection().toLowerCase().trim());

                    if (classSectionModel.getClassAndSectionsList().get(b).getClass_section_name().toLowerCase().trim().equals(concatClassSection)) {
                        classId = classSectionModel.getClassAndSectionsList().get(b).getClassId();
                        section = classSectionModel.getClassAndSectionsList().get(b).getSectionId();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        SchoolClassesModel scm = DatabaseHelper.getInstance(this).getSchoolClassByIds(schoolId, classId, section);
        String addmissionDate = "";
        try {
            if (!Strings.isEmptyOrWhitespace(sm.getEnrollmentDate())) {
                //Format is checking because every time the format changed in db
                String format = AppModel.getInstance().determineDateFormat(sm.getEnrollmentDate());
                if (format != null) {
                    addmissionDate = AppModel.getInstance().convertDatetoFormat(sm.getEnrollmentDate(), format, "dd-MM-yyyy");
//                    addmissionDate = AppModel.getInstance().convertDatetoFormat(sm.getEnrollmentDate(), "yyyy-MM-dd", "dd-MM-yyyy");
                }
            }

//            String admissionDate = AppModel.getInstance().convertDatetoFormat(addmissionDate, "dd-MM-yyyy", "dd-MMM-yy");
            sm.setEnrollmentDate(addmissionDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tv_section_student_Gender.getText().toString().equalsIgnoreCase("male")) {
            sm.setGender("M");
        } else if (tv_section_student_Gender.getText().toString().equalsIgnoreCase("female")) {
            sm.setGender("F");
        }
        sm.setName(tv_student_name.getText().toString().trim());
        // sm.setDob(tv_dob.getText().toString().trim());
        sm.setFormB(tv_formB.getText().toString().trim());
        sm.setContactNumbers(tv_contact.getText().toString().trim());
        sm.setFathersName(tv_father_name.getText().toString().trim());
        sm.setFatherNic(tv_father_cnic.getText().toString().trim());
        sm.setFatherOccupation(tv_father_occupation.getText().toString().trim());
        sm.setMotherName(tv_mother_name.getText().toString().trim());
        sm.setMotherNic(tv_mother_cnic.getText().toString().trim());
        sm.setMotherOccupation(tv_mother_occupation.getText().toString().trim());
        sm.setGuardianName(tv_guardian_name.getText().toString().trim());
        sm.setGuardianNic(tv_guardian_cnic.getText().toString().trim());
        sm.setGuardianOccupation(tv_guardian_occupation.getText().toString().trim());
        sm.setPreviousSchoolName(sm.getPreviousSchoolName());
        sm.setPreviousSchoolClass(sm.getPreviousSchoolClass());
        sm.setAddress1(tv_address.getText().toString().trim());
        String dateOfBirth = AppModel.getInstance().convertDatetoFormat(tv_dob.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        if (Strings.isEmptyOrWhitespace(dateOfBirth))
            dateOfBirth = AppModel.getInstance().convertDatetoFormat(tv_dob.getText().toString().trim(), "dd-MM-yy", "yyyy-MM-dd");
        sm.setDob(dateOfBirth);
        if (student_profile_Path != null) {
            String fileName = new File(student_profile_Path).getName();
            sm.setPictureName(fileName);
            sm.setPictureUploadedOn((String) null);
        } else {
            sm.setPictureName("");
        }
//        sm.setDob(et_dob.getText().toString());
        sm.setContactNumbers(tv_contact.getText().toString().trim());

        schoolModelList = DatabaseHelper.getInstance(this).getAllUserSchoolsById(schoolId);
        List<String> allowedModules = null;
        if (schoolModelList.get(0).getAllowedModule_App() != null) {
            allowedModules = Arrays.asList(schoolModelList.get(0).getAllowedModule_App().split(","));
        }
        if ((allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue))
                || DatabaseHelper.getInstance(StudentProfileActivity.this).isRegionIsInFlagShipSchool(schoolId)) {
            sm.setActualFees(Double.parseDouble(txt_monthlyfees.getText().toString().replace("RS. ", "")));

            if (scholarshipCategoryId > 0)
                sm.setScholarshipCategoryId(scholarshipCategoryId);
        }

        if (ll_email != null && ll_email.getVisibility() == View.VISIBLE)
            sm.setEmail(et_email.getText().toString());
        else
            sm.setEmail("");

        if (ll_electiveSubject != null && ll_electiveSubject.getVisibility() == View.VISIBLE)
            sm.setElectiveSubjectId(electiveSubjectSelectedModel.getId());


        if (religionSelectedModel != null) {
            sm.setReligion(religionSelectedModel.getTitle());
        }

        if (nationalitySelectedModel != null) {
            sm.setNationality(nationalitySelectedModel.getTitle());
        }


        if (ll_orphan != null && ll_orphan.getVisibility() == View.VISIBLE) {
            sm.setOrphan(is_orphan);
        } else {
            sm.setOrphan("");
        }

        if (ll_disabled != null && ll_disabled.getVisibility() == View.VISIBLE) {
            sm.setDisabled(is_disabled);
        } else {
            sm.setDisabled("");
        }

        if (imgDeathCertificate != null) {
            File f;
            String fdir = StorageUtil.getSdCardPath(StudentProfileActivity.this).getAbsolutePath() + "/TCF/TCF Images";
            f = new File(fdir + "/" + sm.getDeathCert_Image());
            if (!f.exists()) {
                String deathCertPath = AppModel.getInstance().saveImageToStorage2(
                        imgDeathCertificate, StudentProfileActivity.this,
                        "Death_Cert_Image_" + AppModel.getInstance().getCurrentDateTime("dd_MMM_yyyy_hh_mm_ss"), 1,
                        schoolId);
                String fileName = new File(deathCertPath).getName();
                sm.setDeathCert_Image(fileName);
                sm.setDeathCert_Image_UploadedOn((String) null);
            }

        } else {
            if (!Strings.isEmptyOrWhitespace(sm.getDeathCert_Image()))
                sm.setDeathCert_Image_UploadedOn((String) null);
            sm.setDeathCert_Image("");
        }

        if (imgMedicalCertificate != null) {
            File f;
            String fdir = StorageUtil.getSdCardPath(StudentProfileActivity.this).getAbsolutePath() + "/TCF/TCF Images";
            f = new File(fdir + "/" + sm.getMedicalCert_Image());
            if (!f.exists()) {
                String medicalCertPath = AppModel.getInstance().saveImageToStorage2(
                        imgMedicalCertificate, StudentProfileActivity.this,
                        "Medical_Cert_Image_" + AppModel.getInstance().getCurrentDateTime("dd_MMM_yyyy_hh_mm_ss"), 1,
                        schoolId);
                String fileName = new File(medicalCertPath).getName();
                sm.setMedicalCert_Image(fileName);
                sm.setMedicalCert_Image_UploadedOn((String) null);
            }

        } else {
            if (!Strings.isEmptyOrWhitespace(sm.getMedicalCert_Image()))
                sm.setMedicalCert_Image_UploadedOn((String) null);
            sm.setMedicalCert_Image("");
        }

        if (imgBForm != null) {
            File f;
            String fdir = StorageUtil.getSdCardPath(StudentProfileActivity.this).getAbsolutePath() + "/TCF/TCF Images";
            f = new File(fdir + "/" + sm.getbForm_Image());
            if (!f.exists()) {
                String bFormPath = AppModel.getInstance().saveImageToStorage2(
                        imgBForm, StudentProfileActivity.this,
                        "BForm_Image_" + AppModel.getInstance().getCurrentDateTime("dd_MMM_yyyy_hh_mm_ss"), 1,
                        schoolId);
                String fileName = new File(bFormPath).getName();
                sm.setbForm_Image(fileName);
                sm.setbForm_Image_UploadedOn((String) null);
            }

        } else {
            if (!Strings.isEmptyOrWhitespace(sm.getbForm_Image()))
                sm.setbForm_Image_UploadedOn((String) null);
            sm.setbForm_Image("");
        }


//        if (rangeTrue) {
        /*if (tv_student_GR_No.getText().toString().isEmpty()) {
            tv_student_GR_No.setError("Required");
            setGrNO = false;
            tv_student_GR_No.setText(sm.getGrNo());
        } else {
            id = DatabaseHelper.getInstance(StudentProfileActivity.this).FindGRNOSTUDENTPROFILE(tv_student_GR_No.getText().toString(), String.valueOf(schoolId));
            if (id == -1) {
                sm.setGrNo(tv_student_GR_No.getText().toString().trim());
                setGrNO = true;
            } else if (tv_student_GR_No.getText().toString().trim().equals(sm.getGrNo())) {
                setGrNO = true;
            } else {
                tv_student_GR_No.setError("GR No. " + tv_student_GR_No.getText().toString() + " already exist!");
                setGrNO = false;
                tv_student_GR_No.setText(sm.getGrNo());
            }
        }*/
//        }

        id = DatabaseHelper.getInstance(StudentProfileActivity.this).FindGRNOSTUDENTPROFILE(tv_student_GR_No.getText().toString(), String.valueOf(schoolId));
        if (id == -1) {
            sm.setGrNo(tv_student_GR_No.getText().toString().trim());
            setGrNO = true;
            try {
                grNo = Integer.parseInt(tv_student_GR_No.getText().toString().trim());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        } else if (tv_student_GR_No.getText().toString().trim().equals(sm.getGrNo())) {
            setGrNO = true;
            try {
                grNo = Integer.parseInt(tv_student_GR_No.getText().toString().trim());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        } else {
            tv_student_GR_No.setError("GR No. " + tv_student_GR_No.getText().toString() + " already exist!");
            setGrNO = false;
            tv_student_GR_No.setText(sm.getGrNo());
        }

        if (scm != null && setGrNO) {
            sm.setSchoolClassId(scm.getId());
            if (student_profile_Path != null) {

                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.getInstance(this).STUDENT_PICTURE_NAME, sm.getPictureName());
                values.put(DatabaseHelper.getInstance(this).STUDENT_PICTURE_UPLOADED_ON, sm.getPictureUploadedOn());

                long id = DatabaseHelper.getInstance(this).updateTableColumns(
                        DatabaseHelper.getInstance(this).TABLE_STUDENT, values, sm.getId());
                if (!fromBasicInfo) {
                    if (id > 0) {
                        Intent leave_intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
                        leave_intent.putExtra("StudentGrNo", Integer.valueOf(grNo));
                        leave_intent.putExtra("studentid", Integer.valueOf(student_id));
                        leave_intent.putExtra("StudentProfileIndex", 0);

                        leave_intent.putExtra("classId", classId);
                        leave_intent.putExtra("schoolId", schoolId);
                        leave_intent.putExtra("sectionId", sectionID);
                        leave_intent.putExtra("isFinanceOpen", isFinance);
                        leave_intent.putExtra("admissiondate", admissiondate);
                        leave_intent.putExtra("class_sec", txt_class_sec.getText().toString().trim());
                        if (isFinance || DatabaseHelper.getInstance(StudentProfileActivity.this).isRegionIsInFlagShipSchool(schoolId)) {
                            leave_intent.putExtra("category", category);
                        }
                        leave_intent.putExtra("grno", grNo);
                        leave_intent.putExtra("monthlyfees", monthlyfees);
                        //intent.putExtra("receivables", "SectionId");
                        leave_intent.putExtra("status", status);
                        leave_intent.putExtra("promotionStatus", sm.getStudent_promotionStatus());
                        MessageBox("Student Profile Edit Successfully!", true, leave_intent);

                        //Important when any change in table call this method
                        onChangePendingSyncCount.onChangeRecords();
                    } else {
                        MessageBox("Something went wrong please try again later!");
                    }
                } else if (isFieldsChanged()) {
                    updateStudentInfo();
                }
            } else if (isFieldsChanged()) {
                updateStudentInfo();
            }
        } else
            MessageBox("Could not update student to the following class and section!");
    }

    private void updateStudentInfo() {
        long i = DatabaseHelper.getInstance(this).editStudent(
                sm,
                sm.getId());
        if (i > 0) {
            Intent leave_intent = new Intent(getApplicationContext(), StudentProfileActivity.class);
            leave_intent.putExtra("StudentGrNo", Integer.valueOf(grNo));
            leave_intent.putExtra("classId", classId);
            leave_intent.putExtra("schoolId", schoolId);
            leave_intent.putExtra("StudentProfileIndex", 0);
            leave_intent.putExtra("sectionId", sectionID);
            leave_intent.putExtra("isFinanceOpen", isFinance);
            leave_intent.putExtra("admissiondate", admissiondate);
            leave_intent.putExtra("class_sec", txt_class_sec.getText().toString().trim());
            if (isFinance || DatabaseHelper.getInstance(StudentProfileActivity.this).isRegionIsInFlagShipSchool(schoolId)) {
                leave_intent.putExtra("category", category);
            }
            leave_intent.putExtra("studentid", Integer.valueOf(student_id));
            leave_intent.putExtra("grno", grNo);
            leave_intent.putExtra("monthlyfees", monthlyfees);
            //intent.putExtra("receivables", "SectionId");
            leave_intent.putExtra("status", status);
            leave_intent.putExtra("promotionStatus", sm.getStudent_promotionStatus());
            MessageBox("Student Edit Successfully!", true, leave_intent);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.getInstance(this).STUDENT_MODIFIED_ON, AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
            contentValues.put(DatabaseHelper.getInstance(this).STUDENT_MODIFIED_BY, DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
            contentValues.put(DatabaseHelper.getInstance(this).STUDENT_UPLOADED_ON, (String) null);

            //Important when any change in table call this method
            AppModel.getInstance().changeMenuPendingSyncCount(StudentProfileActivity.this, false);

            DatabaseHelper.getInstance(this).updateStudentByColumns(contentValues, sm.getId());

        } else {
            MessageBox("Something went wrong please try again later!");
        }
    }
/*
    @Override
    public void onPickResult(PickResult pickResult) {

        try {
            if (pickResult.getError() == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Edit Student");
                builder.setMessage("Are you sure you want to edit this student Picture?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        ImageCompression imageCompression = new ImageCompression(StudentProfileActivity.this);
//                        String path = pickResult.getPath();
                        Bitmap thumbBitmap;
                        float imageWidth = pickResult.getBitmap().getWidth();
                        float imageHeight = pickResult.getBitmap().getHeight();
                        boolean isImageCompressed = false;

                        if (imageHeight > imageWidth) {
                            float targetWidth = (imageWidth / imageHeight) * 100;
                            thumbBitmap = ThumbnailUtils.extractThumbnail(pickResult.getBitmap(), (int) targetWidth, 100);
                        } else {
                            float targetHeight = (imageHeight / imageWidth) * 100;
                            thumbBitmap = ThumbnailUtils.extractThumbnail(pickResult.getBitmap(), 100, (int) targetHeight);
                        }

//                        imageCompression.execute(path);
//                        image = new File(path);

//                        Uri targetUri = getImageUri(getApplicationContext(), pickResult.getBitmap());
//                        mUri = targetUri;

                        header.setImageBitmap(thumbBitmap);
                        header.setBackground(null);

                        byte[] Student_Profile = AppModel.getInstance().bitmapToByte(thumbBitmap);
                        student_profile_Path = AppModel.getInstance().saveImageToStorage2(Student_Profile, view.getContext(), sm.getGrNo(), 1, schoolId);

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

            } else {
                Toast.makeText(StudentProfileActivity.this, pickResult.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(StudentProfileActivity.this, "Error in Imageview:" + e.getMessage());
        }

    }*/

    private Uri getImageUri(Context inContext, Bitmap inImage, File imageFile) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            OutputStream imageOutStream = null;

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Title");
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.getName());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            try {
                imageOutStream = inContext.getContentResolver().openOutputStream(uri);
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream);

                if (imageOutStream != null)
                    imageOutStream.close();

            } catch (Exception e) {
                AppModel.getInstance().appendErrorLog(getApplicationContext(), "Error in getImageUri : " + e.getMessage());
            }

            return uri;
        } else {
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }

    }

    private boolean isFieldsChanged() {
        boolean changes = false;

        StudentModel searchedSm;
        if (StudentModel.getInstance().getStudentsList() != null && StudentModel.getInstance().getStudentsList().size() > 0) {
            searchedSm = ifNullReturnEmptyString(DatabaseHelper.getInstance(this)
                    .getStudentwithGR(Integer.parseInt(StudentModel.getInstance().getStudentsList().get(index).getGrNo()), schoolId));
        } else {
            searchedSm = ifNullReturnEmptyString(DatabaseHelper.getInstance(this)
                    .getStudentwithGR(Integer.parseInt(sm.getGrNo()), schoolId));
            ;
        }
        String admissionDate = AppModel.getInstance().convertDatetoFormat(tv_admissiondate.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        admissionDate = admissionDate == null ? "" : admissionDate;

        String dateOfBirth = AppModel.getInstance().convertDatetoFormat(tv_dob.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd");
        dateOfBirth = dateOfBirth == null ? "" : dateOfBirth;

        List<String> allowedModules = null;
        if (schoolModelList.get(0).getAllowedModule_App() != null) {
            allowedModules = Arrays.asList(schoolModelList.get(0).getAllowedModule_App().split(","));
        }

       /* if (!searchedSm.getGrNo().equals(tv_student_GR_No.getText().toString().trim()))
            changes = true;

        else */

        if (!searchedSm.getEnrollmentDate().equals(admissionDate))
            changes = true;

        else if (!searchedSm.getGender().equals(tv_section_student_Gender.getText().toString().toUpperCase()))
            changes = true;

        else if (!searchedSm.getName().equals(tv_student_name.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getFathersName().equals(tv_father_name.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getFatherNic().equals(tv_father_cnic.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getFormB().equals(tv_formB.getText().toString()))
            changes = true;

        else if (!searchedSm.getFatherOccupation().equals(tv_father_occupation.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getMotherName().equals(tv_mother_name.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getMotherNic().equals(tv_mother_cnic.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getMotherOccupation().equals(tv_mother_occupation.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getGuardianName().equals(tv_guardian_name.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getGuardianNic().equals(tv_guardian_cnic.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getGuardianOccupation().equals(tv_guardian_occupation.getText().toString().trim()))
            changes = true;

     /*   else if (!searchedSm.getPreviousSchoolName().equals(et_previous_school.getText().toString().trim()))
            changes = true;

        else if (!searchedSm.getPreviousSchoolClass().equals(et_class_in_previous_school.getText().toString().trim()))
            changes = true;
     */
        else if (!searchedSm.getAddress1().equals(tv_address.getText().toString().trim()))
            changes = true;


        else if (!searchedSm.getDob().equals(dateOfBirth))
            changes = true;

        else if (!searchedSm.getContactNumbers().equals(tv_contact.getText().toString().trim()))
            changes = true;

        else if ((allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue))
                || DatabaseHelper.getInstance(StudentProfileActivity.this).isRegionIsInFlagShipSchool(schoolId)) {
            if (searchedSm.getActualFees() != Double.parseDouble(txt_monthlyfees.getText().toString().replace("RS. ", "")))
                changes = true;
//            if (scholarshipCategoryId > 0 && searchedSm.getScholarshipCategoryId() == scholarshipCategoryId)
//                i++;
        } else if (!searchedSm.getEmail().equals(et_email.getText().toString().trim()))
            changes = true;
        else if (!searchedSm.getReligion().equals(religionSelectedModel.getTitle().trim()))
            changes = true;
        else if (!searchedSm.getNationality().equals(nationalitySelectedModel.getTitle().trim()))
            changes = true;
        else if (searchedSm.getElectiveSubjectId() != electiveSubjectSelectedModel.getId())
            changes = true;
        else if (!searchedSm.isOrphan().equals(is_orphan))
            changes = true;
        else if (!searchedSm.isDisabled().equals(is_disabled))
            changes = true;

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int position,
                               long id) {
        switch (adapterView.getId()) {
            case R.id.spn_gender:
                tv_section_student_Gender.setText(GenderAdapter.getItem(position));
                if (tv_section_student_Gender.getText().toString().toLowerCase().equals("male")) {
                    tv_section_student_Gender.getText().toString().equalsIgnoreCase("m");
                } else {
                    tv_section_student_Gender.getText().toString().equalsIgnoreCase("f");
                }
                break;
            case R.id.spn_religion:
                religionSelectedModel = (ReligionModel) adapterView.getItemAtPosition(position);
                break;
            case R.id.spn_nationality:
                nationalitySelectedModel = (NationalityModel) adapterView.getItemAtPosition(position);
                break;
            case R.id.spn_elective_subject:
                electiveSubjectSelectedModel = (ElectiveSubjectModel) adapterView.getItemAtPosition(position);
                break;
            case R.id.spn_orphan:
                is_orphan = OrphanAdapter.getItem(position);
                if (is_orphan.equals("Yes")) {
                    ll_deathCertificate.setVisibility(View.VISIBLE);
                    setDeathCertificateImage();
                } else {
                    ll_deathCertificate.setVisibility(View.GONE);
                    imgDeathCertificate = null;
                }
                break;
            case R.id.spn_disabled:
                is_disabled = DisabledAdapter.getItem(position);
                if (is_disabled.equals("Yes")) {
                    ll_medicalCertificate.setVisibility(View.VISIBLE);
                    setMedicalCertificateImage();
                } else {
                    ll_medicalCertificate.setVisibility(View.GONE);
                    imgMedicalCertificate = null;
                }
                break;
            case R.id.spn_class_section_name:
                classId = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getClassId();
                sectionID = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getSectionId();
                showOrHideViewsForBothFSAndNineClassOnly();
                showOrHideViewsForBothFSAndPSAndEightNineClassOnly();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean validateBasicInfo() {
        int allOk = 0;
        int classId = ((ClassSectionModel) spn_class_section_name.getSelectedItem()).getClassId();
        int section = ((ClassSectionModel) spn_class_section_name.getSelectedItem()).getSectionId();

        if (!spn_class_section_name.getSelectedItem().toString().equals(sm.getCurrentClass() + " " + sm.getCurrentSection())) {
            if (AppModel.getInstance().getDaysBetweenDates(sm.getEnrollmentDate(), AppModel.getInstance().getDate(), "yyyy-MM-dd") <= 10) {
                AppModel.getInstance().showMessage(new WeakReference<>(StudentProfileActivity.this), "Can't Update Class Section", "Class Section cannot be changed as student was enrolled within last 10 days. Please try later.");
                allOk++;
            }

        }


        if (isFlagShipAndNine()) {
            if (TextUtils.isEmpty(et_father_name.getText())) {
                et_father_name.setError("Required");
                allOk++;
            } else {
                et_father_name.setError(null);
            }


            if (Strings.isEmptyOrWhitespace(et_email.getText().toString())) {
                et_email.setError("Please enter Email Address");
                allOk++;
            } else if (!Strings.isEmptyOrWhitespace(et_email.getText().toString()) && !isValidEmail(et_email.getText().toString())) {
                et_email.setError("Email Address not valid");
                allOk++;
            } else {
                et_email.setError(null);
            }

            if (electiveSubjectSelectedModel.getTitle().equals(getString(R.string.please_select))) {
                Toast.makeText(this, "Please select elective subject", Toast.LENGTH_LONG).show();
                allOk++;
            }

            if (is_orphan.equals("Yes") && imgDeathCertificate == null) {
                Toast.makeText(this, "Please Upload Death Certificate", Toast.LENGTH_LONG).show();
                allOk++;
            }

            if (is_disabled.equals("Yes") && imgMedicalCertificate == null) {
                Toast.makeText(this, "Please Upload Medical Certificate", Toast.LENGTH_LONG).show();
                allOk++;
            }

            if (Strings.isEmptyOrWhitespace(sm.getPictureName()) && Strings.isEmptyOrWhitespace(student_profile_Path)) {
                Toast.makeText(this, "Please Upload Student Profile Picture", Toast.LENGTH_LONG).show();
                allOk++;
            }

            if (imgBForm == null) {
                Toast.makeText(this, "Please Upload BForm Certificate", Toast.LENGTH_LONG).show();
                allOk++;
            }
        }

        if (Strings.isEmptyOrWhitespace(et_name.getText().toString())) {
            et_name.setError("Required");
            allOk++;
        } else {
            et_name.setError(null);
        }

        if (spn_gender.getSelectedItem().toString().toLowerCase().equals("select gender")) {
            Toast.makeText(current_activity, "Please select gender", Toast.LENGTH_SHORT).show();
            allOk++;
        }

        if (Strings.isEmptyOrWhitespace(et_GrNo.getText().toString())) {
            et_GrNo.setError("Required");
            allOk++;
        } else if (isGRExist(et_GrNo.getText().toString(), String.valueOf(schoolId))) {
            et_GrNo.setError("Already exist in current school");
            allOk++;
        } else {
            et_GrNo.setError(null);
        }

        if (spn_class_section_name.getSelectedItem().toString().equals(getString(R.string.select_class_section))) {
            Toast.makeText(current_activity, "Please select class and section", Toast.LENGTH_SHORT).show();
            allOk++;
        } else {
//            int classId = ((ClassSectionModel) spn_class_section_name.getSelectedItem()).getClassId();
//            int section = ((ClassSectionModel) spn_class_section_name.getSelectedItem()).getSectionId();
            SchoolClassesModel scm = DatabaseHelper.getInstance(this).getSchoolClassByIds(schoolId, classId, section);
            int StudentCount = DatabaseHelper.getInstance(current_activity).getStudentCount(scm.getId());
            int capacity = DatabaseHelper.getInstance(current_activity).getMaxCapacityFromSchoolClass(scm.getSchoolId(), scm.getId());

            if (tempSchoolClassID != scm.getId()) {
                if (StudentCount >= capacity) {
                    Toast.makeText(current_activity, "Cannot Edit student in this class because it reaches Max Capacity:" + capacity + " and No. of Students are:" + StudentCount, Toast.LENGTH_LONG).show();
                    allOk++;
                }
            }
        }

        if (DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(schoolId)) {
            if (Strings.isEmptyOrWhitespace(et_dob.getText().toString())) {
                Toast.makeText(current_activity, "Date of Birth is Required", Toast.LENGTH_SHORT).show();
                allOk++;
            } else {
                et_dob.setError(null);
            }
        } else {
            if (Strings.isEmptyOrWhitespace(et_dob.getText().toString())) {
                Toast.makeText(current_activity, "Date of Birth is Required", Toast.LENGTH_SHORT).show();
                allOk++;
            } else if (!isValidAgeForClass(this, et_dob.getText().toString(), sm.getCurrentClass())) {
                allOk++;
            }
        }


        if (!Strings.isEmptyOrWhitespace(et_dob.getText().toString())) {
            String date = et_dob.getText().toString().trim();
            date = AppModel.getInstance().convertDatetoFormat(date, "dd-MMM-yy", "yyyy-MM-dd");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = dateFormat.parse(date);
                Date date2 = dateFormat.parse(AppModel.getInstance().getDate());

                if (DatabaseHelper.getInstance(this).isRegionIsInFlagShipSchool(schoolId)
                        && DatabaseHelper.getInstance(this).isNineClass(schoolId, classId, section)
                        && calcNumberOfYears(date1, date2) < 13) {
                    Toast.makeText(current_activity, "Student age must be equal to or greater than 13 years.", Toast.LENGTH_LONG).show();
                    allOk++;
                } else if (!(calcNumberOfYears(date1, date2) >= 3 && calcNumberOfYears(date1, date2) <= 25)) {
                    Toast.makeText(current_activity, "Student age must be between 3 to 25 years.", Toast.LENGTH_LONG).show();
//                    et_dob.setError("Student age must be between 3 to 25 years.");
                    allOk++;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Strings.isEmptyOrWhitespace(et_formb.getText().toString())) {
            et_formb.setError("Required");
            allOk++;
        } else {

            /**************************/
            if (isFlagShipAndNine()) {
                if (nationalitySelectedModel != null && nationalitySelectedModel.getTitle().equals("Pakistani")) {
                    String input = et_formb.getText().toString();
                    String pattern = "^[0-9]{13}";
//                    String pattern = "\\d{5}-\\d{7}-\\d";//old one
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(input);


                    if (Strings.isEmptyOrWhitespace(et_formb.getText().toString())) {
                        et_formb.setError("Required");
                        allOk++;
                    } else if (!(m.find() && m.group().equals(input))) {
//                        et_formb.setError("Invalid format (xxxxx-xxxxxxx-x)");//old one
                        et_formb.setError("Invalid format (xxxxxxxxxxxxx)");
                        allOk++;
                    } else if (DatabaseHelper.getInstance(StudentProfileActivity.this).checkIfBFormExistsInSchool(input, schoolId, sm.getId())) {
                        et_formb.setError("Bform already exists of another student.");
                        allOk++;
                    }
                } else {
                    et_formb.setError(null);
                }
            } else {
                // et_formb.setError(null);//546648796766769
//                et_formb.getText().toString()
                String bForm = et_formb.getText().toString();
                if (bFormNoChange) {
                    try {
                        idBform = DatabaseHelper.getInstance(current_activity).isBformNoExist(bForm, String.valueOf(schoolId));
                    } catch (SQLiteException e) {
                        e.printStackTrace();
                    }
                    if (idBform == 1) {
                        allOk++;
                        et_formb.setError("The Bform already exist");
                    }//3218766636599
                    else {
                        et_formb.setError(null);
                    }
                } else {
                    et_formb.setError(null);
                }
            }
            /*************************/

        }

        if (Strings.isEmptyOrWhitespace(et_contact.getText().toString())) {
            allOk++;
            et_contact.setError("Please enter contact number");
        } else if (!isValidMobile(et_contact.getText().toString())) {
            et_contact.setError("Please Enter Valid Mobile Number");
            allOk++;
        } else {
            et_contact.setError(null);
        }


        /*if (!Strings.isEmptyOrWhitespace(et_contact.getText().toString())) {
            String mobNum = et_contact.getText().toString();
            if (isValidMobile(mobNum)) {
//            if (mobNum.length() == 11 && mobNum.substring(0, 2).equals("03")) {
                et_contact.setText(mobNum);
            } else {
                et_contact.setError("Please Enter Valid Mobile Number");
                allOk++;
            }
        } else {
            et_contact.setText("");
        }*/

        List<String> allowedModules = null;
        if (schoolModelList != null && schoolModelList.get(0).getAllowedModule_App() != null) {
            allowedModules = Arrays.asList(schoolModelList.get(0).getAllowedModule_App().split(","));
        }

        if ((allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue))
                || DatabaseHelper.getInstance(StudentProfileActivity.this).isRegionIsInFlagShipSchool(schoolId)) {
            if (et_tution_fees.getText().toString().isEmpty()) {
                Toast.makeText(current_activity, "Please enter tuition fees in fee assessment", Toast.LENGTH_SHORT).show();
                allOk++;
            } else {
                int monthlyFee = Integer.parseInt(et_tution_fees.getText().toString().trim());
                if (monthlyFee < 10) {
                    et_tution_fees.setError("Tuition fees should be greater than or equals to 10");
                    allOk++;
                } else if (monthlyFee > 1500) {
                    et_tution_fees.setError("Tuition fees should be less than or equals to 1500");
                    allOk++;
                }
            }
        }

        return allOk == 0;
    }

    private boolean isGRExist(String grno, String schoolId) {
        if (sm.getGrNo().equals(grno))
            return false;
        long grNoExist = DatabaseHelper.getInstance(this).FindGRNOSTUDENTPROFILE(grno, schoolId);
        return grNoExist > 0;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }

    private boolean isValidCNICNumber(String cnic, EditText text) {
        String regex = "^[0-9]{13}";
        if (Pattern.compile(regex).matcher(cnic).matches()) {
            return cnic.length() == 13;
        }
        text.setError("Invalid format");
        return false;
    }

    private boolean isCnicValid(String cnic, EditText editText) {
        String error = "";
        try {
            if (editText == null && Strings.isEmptyOrWhitespace(cnic)) {
                error = "Required";
            } else {
                if (!Strings.isEmptyOrWhitespace(cnic)) {
                    if (isNationalityPakistani()) {
                        //String pattern = "\\d{5}-\\d{7}-\\d"; //This is Old one
                        String pattern = "^[0-9]{13}";
                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(cnic);

                        if (!(m.find() && m.group().equals(cnic))) {
                            //error = "Invalid format (xxxxx-xxxxxxx-x)";//This is Old One
                            error = "Invalid format (xxxxxxxxxxxxx)";
                        } else if (DatabaseHelper.getInstance(StudentProfileActivity.this).checkIfBFormExistsInSchool(cnic, schoolId, sm.getId())) {
                            error = "Bform already exists of another student";
                        }
                    }
                }
            }

            if (Strings.isEmptyOrWhitespace(error)) {
                return true;
            } else {
                if (editText != null)
                    editText.setError(error);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isNationalityPakistani() {
        return (nationalitySelectedModel != null && nationalitySelectedModel.getTitle().equals("Pakistani")) || (sm.getNationality().equalsIgnoreCase("Pakistani"));

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

    private float getEntries() {
        float difference = 0;
        totaldays = DatabaseHelper.getInstance(view.getContext()).getDatesofHolidaysInLast30days(schoolId + "");
        Float absent = DatabaseHelper.getInstance(StudentProfileActivity.this).getAttendanceOfstudent30days(Integer.parseInt(student_id));
        Log.d("attendancecount", "totaldays: " + totaldays);
        difference = (float) totaldays - absent;
        if (difference == 0.0) {
            pieEntries.add(new PieEntry(absent, "Absent"));
        } else if (difference == totaldays) {
            pieEntries.add(new PieEntry(difference, "Present"));
        } else {
            pieEntries.add(new PieEntry(difference, "Present"));
            pieEntries.add(new PieEntry(absent, "Absent"));
        }
        return difference;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Idddddd", String.valueOf(h.getX()));
        Intent i;
        switch ((int) h.getX()) {
        }
    }

    @Override
    public void onNothingSelected() {

    }

    private void DrawChart() {
        pieEntries = new ArrayList<>();

        float difference = getEntries();
        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterTextSize(0f);
        pieChart.setEntryLabelTextSize(10f);
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.setUsePercentValues(true);
        //pieChart.setDrawHoleEnabled(false);


        pieDataSet.setColors(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);

        attendanceProgressBar.setProgress((int) (difference));

        attendanceProgressCount.setText((int) Math.round((difference / totaldays) * 100) + "%");

        int[] MY_COLORS;

        if (difference == 0.0) {
            // add a lot of colors
            MY_COLORS = new int[]{
                    Color.rgb(235, 31, 37)};
        } else if (difference == gettotaldays) {
            // add a lot of colors
            MY_COLORS = new int[]{
                    Color.rgb(3, 197, 101)};
        } else {
            // add a lot of colors
            MY_COLORS = new int[]{
                    Color.rgb(3, 197, 101),
                    Color.rgb(235, 31, 37)};
        }


        //blue/orange
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : MY_COLORS) colors.add(c);

        pieDataSet.setColors(colors);

        pieChart.setOnChartValueSelectedListener(this);

    }

    private void hideKeyboard(Dialog dialog) {
        View view = dialog.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void hide_edit_markers() {
        iv_edit_basicinfo.setVisibility(View.INVISIBLE);
        iv_edit_fatherinfo.setVisibility(View.INVISIBLE);
        iv_edit_motherinfo.setVisibility(View.INVISIBLE);
        iv_edit_guardianinfo.setVisibility(View.INVISIBLE);
        iv_edit_profileImg.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onChangeRecords() {
        super.calPendingRecords(AppModel.getInstance().getSpinnerSelectedSchool(StudentProfileActivity.this));
//        super.autoSyncStartWhenUploadIsPendingInQueue();
    }

    private void initSpinners() {
        /*********************Muhammad Salman Saleem****************************/
        if (isFlagShipSchool()) {
            setEditTextMaxLength(et_GrNo, 5);
        }
        /*********************Muhammad Salman Saleem***************************/

        GenderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.gender_array));
        spn_gender.setAdapter(GenderAdapter);
        spn_gender.setOnItemSelectedListener(this);

        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(String.valueOf(schoolId)));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_class_section_name.setAdapter(ClassSectionAdapter);
        spn_class_section_name.setOnItemSelectedListener(this);

        ReligionModel religionModel = new ReligionModel();
        religionModel.setRmList(GlobalHelperClass.getInstance(this).getAllReligions());
        ReligionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, religionModel.getRmList());
        ReligionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ReligionSpinner.setAdapter(ReligionAdapter);
        if (!CollectionUtils.isEmpty(religionModel.getRmList())) {
            ReligionSpinner.setSelection(0);
        } //By default Select Muslim
        ReligionSpinner.setOnItemSelectedListener(this);


        NationalityModel nationalityModel = new NationalityModel();
        nationalityModel.setNmList(GlobalHelperClass.getInstance(this).getAllNationalities());
        NationalityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nationalityModel.getNmList());
        NationalityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        NationalitySpinner.setAdapter(NationalityAdapter);
        if (!CollectionUtils.isEmpty(nationalityModel.getNmList())) {
            NationalitySpinner.setSelection(0); //By default Select Pakistani
        }
        NationalitySpinner.setOnItemSelectedListener(this);

        ElectiveSubjectModel electiveSubjectModel = new ElectiveSubjectModel();
        electiveSubjectModel.setEsmList(GlobalHelperClass.getInstance(this).getAllElectiveSubjects());
        electiveSubjectModel.getEsmList().add(0, new ElectiveSubjectModel(0, getString(R.string.please_select)));
        ElectiveSubjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, electiveSubjectModel.getEsmList());
        ElectiveSubjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ElectiveSubjectSpinner.setAdapter(ElectiveSubjectAdapter);
        NationalitySpinner.setSelection(0); //By default Please Select
        ElectiveSubjectSpinner.setOnItemSelectedListener(this);

        OrphanAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.no_yes_array));
        OrphanSpinner.setAdapter(OrphanAdapter);
        OrphanSpinner.setOnItemSelectedListener(this);

        DisabledAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.no_yes_array));
        DisabledSpinner.setAdapter(DisabledAdapter);
        DisabledSpinner.setOnItemSelectedListener(this);


    }


    private boolean isValidMobile(String phone) {
        //923xxxxxxxxx
        String regex = "^([92]{2})([3]{1})([0-9]{9})$";
        if (Pattern.compile(regex).matcher(phone).matches()) {
            return phone.length() == 12;
        }
        return false;
    }


    void openCameraGalleryDialog(String title) {
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
                    startActivity(new Intent(StudentProfileActivity.this, CameraViewActivity.class).putExtra("showMsg", true));
                }
            }).setOnPickResult(new IPickResult() {
                @Override
                public void onPickResult(PickResult r) {
                    if (r.getError() == null) {
                        CropImage.activity(r.getUri())
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setFixAspectRatio(true)
                                .start(StudentProfileActivity.this);
                    } else {
                        Toast.makeText(StudentProfileActivity.this, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).show(this);


        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(this, "Error in Imageview:" + e.getMessage());
        }
    }

    /**************************
     *
     * Written
     * Muhammad Salman Saleem
     */
}


