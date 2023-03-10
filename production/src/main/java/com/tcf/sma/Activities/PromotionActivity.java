package com.tcf.sma.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tcf.sma.Adapters.PromotionAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.PromotionDialogManager;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.PromotionModel;
import com.tcf.sma.Models.SchoolClassesModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.SectionModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

public class PromotionActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, PromotionDialogManager.Ipromotions {
    static TextView TotalPromotion;
    static TextView TotalNotPromotion;
    View view;
    Button bt_submit, Search;
    RecyclerView rv_promotionsList;
    EditText et_student_name;
    EditText et_GrNo;
    ClassSectionModel classSectionModel;
    ArrayList<PromotionModel> promotionList = new ArrayList<>();
    PromotionAdapter promotionAdapter;
    PromotionModel promotionModel;
    TextView spn_pr_class_section;
    AppBarLayout appBarLayout;
    LinearLayout ll_nextClassSection;
    Spinner sp_nextClassSection;
    int SchoolSpinnerValue, ClassSpinnerValue, ClassSpinnerRankValue, SectionSpinnerValue, NextClassSpinnerValue, NextSectionSpinnerValue;
    ArrayAdapter<SchoolModel> SchoolAdapter;
    ArrayAdapter<ClassModel> ClassAdapter;
    ArrayAdapter<SectionModel> SectionAdapter;
    ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    LinearLayout lll, ll_schoolSpinner;
    private Spinner ClassSpinner, SchoolSpinner, SectionSpinner, ClassSectionSpinner, spn_activity;
    private ProgressDialog pd;
    private boolean isSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_promotion);
        setToolbar("Promotion", this, false);
        init(view);
        initSpinners();
//        working();

    }

    private void init(View view) {
        et_student_name = (EditText) view.findViewById(R.id.et_student_name);
        et_GrNo = (EditText) view.findViewById(R.id.et_GrNo);
        et_GrNo.setVisibility(View.GONE);
        et_student_name.setVisibility(View.GONE);
        ll_nextClassSection = (LinearLayout) view.findViewById(R.id.ll_nextClassSection);
        ll_nextClassSection.setVisibility(View.VISIBLE);
        sp_nextClassSection = (Spinner) view.findViewById(R.id.spn_pr_class_section);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        rv_promotionsList = (RecyclerView) view.findViewById(R.id.rv_promotionList);
        TotalPromotion = (TextView) view.findViewById(R.id.tv_total_promoted);
        TotalNotPromotion = (TextView) view.findViewById(R.id.tv_total_non_promoted);
        SectionSpinner = (Spinner) view.findViewById(R.id.spn_section);
        SchoolSpinner = (Spinner) view.findViewById(R.id.spn_school);
        ClassSpinner = (Spinner) view.findViewById(R.id.spn_class_name);
        ClassSectionSpinner = (Spinner) view.findViewById(R.id.spn_class_section_name);
//        spn_activity = (Spinner)view.findViewById(R.id.spn_activity);
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Search = (Button) view.findViewById(R.id.btnsearch);
        lll = (LinearLayout) view.findViewById(R.id.lll);
//        lll.setVisibility(View.VISIBLE);

        bt_submit.setOnClickListener(this);
        Search.setOnClickListener(this);

        ll_schoolSpinner = (LinearLayout) view.findViewById(R.id.ll_schoolSpinner);
        ll_schoolSpinner.setVisibility(View.VISIBLE);

        rv_promotionsList.setNestedScrollingEnabled(false);
        rv_promotionsList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_promotionsList.setLayoutManager(llm);
    }

    private void populateNextClassSpinner(int schoolId, int classSpinnerValue, int classRank, int sectionSpinnerValue) {
        List<ClassSectionModel> sm = new ArrayList<>();
        if (classSpinnerValue <= 0) {
            sm.add(0, new ClassSectionModel(0, 0, "Select Promotion Class and Section"));
            ArrayAdapter<ClassSectionModel> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sm);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_nextClassSection.setAdapter(aa);
            sp_nextClassSection.setOnItemSelectedListener(this);
        } else {
            sm.add(0, new ClassSectionModel(0, 0, "Select Promotion Class and Section"));
            SchoolClassesModel scm = DatabaseHelper.getInstance(this).getNewSchoolClass(schoolId, classRank, sectionSpinnerValue);
            ArrayList<ClassSectionModel> classSectionModel = DatabaseHelper.getInstance(this).getClassSectionBySchoolId(SchoolSpinnerValue + "");
            if (scm != null) {
                for (ClassSectionModel csm : classSectionModel) {
                    if (csm.getRank() == scm.getRank()) {
                        sm.add(csm);
                    }
                }
            }
            ArrayAdapter<ClassSectionModel> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sm);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_nextClassSection.setAdapter(aa);
            sp_nextClassSection.setOnItemSelectedListener(this);

        }
    }

    private void initSpinners() {

        setCollapsing(false);

        //Used for principal
        SchoolModel schoolModel = new SchoolModel();
//        schoolModel = DatabaseHelper.getInstance(this).getSchoolById(SurveyAppModel.getInstance().getSelectedSchool(this));
        schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllUserSchoolsForPromotion());
        schoolModel.getSchoolsList().add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

//        ArrayList<SchoolModel> smList = new ArrayList<>();
//        smList.add(schoolModel);
//        schoolModel.setSchoolsList(smList);
//        SchoolSpinnerValue = schoolModel.getId();

        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchoolSpinner.setAdapter(SchoolAdapter);
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModel.getSchoolsList(), this);
        if (indexOfSelectedSchool > -1) {
            SchoolSpinner.setSelection(indexOfSelectedSchool);
            isSelected = true;
        }
        SchoolSpinner.setOnItemSelectedListener(this);

        ClassModel classModel = new ClassModel();
        classModel.setClassesList(DatabaseHelper.getInstance(this).getAllClasses());
        classModel.getClassesList().add(0, new ClassModel(0, "Select Class"));
        ClassAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classModel.getClassesList());
        ClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSpinner.setAdapter(ClassAdapter);
        ClassSpinner.setOnItemSelectedListener(this);

        SectionModel sectionModel = new SectionModel();
        sectionModel.setSectionsList(DatabaseHelper.getInstance(this).getAllSections());
        sectionModel.getSectionsList().add(0, new SectionModel(0, "Select Section"));
        SectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sectionModel.getSectionsList());
        SectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SectionSpinner.setAdapter(SectionAdapter);
        SectionSpinner.setOnItemSelectedListener(this);

        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getPresentClassSectionBySchoolIdForPromotion(String.valueOf(schoolModel.getId())));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
        ClassSectionSpinner.setOnItemSelectedListener(this);


        populateNextClassSpinner(0, 0, 0, 0);

    }


    public void setTotalPromotedCount() {
        int p = 0, np = 0;
        try {
            for (int i = 0; i < PromotionAdapter.mpma.size(); i++) {
                if (PromotionAdapter.mpma.get(i).getIsPromoted() != null) {
                    switch (PromotionAdapter.mpma.get(i).getIsPromoted()) {
                        case "y":
                            p++;
                            break;
                        case "n":
                            np++;
                            break;
                    }
                }
                TotalPromotion.setText("  " + p);
                TotalNotPromotion.setText("  " + np);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_pr_class_section:
                NextClassSpinnerValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getClassId();
                NextSectionSpinnerValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getSectionId();
                break;
            case R.id.spn_school:
                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(this,
                        SchoolSpinnerValue);
                if (isSelected) {
                    classSectionModel = new ClassSectionModel();
                    classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getPresentClassSectionBySchoolIdForPromotion(SchoolSpinnerValue + ""));
                    classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
                    ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
                    ClassSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ClassSectionSpinner.setAdapter(ClassSectionAdapter);
                    ClassSectionSpinner.setOnItemSelectedListener(this);

                } else {
                    isSelected = true;
                }

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, SchoolSpinnerValue);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }

                break;
            case R.id.spn_section:
                SectionSpinnerValue = ((SectionModel) adapterView.getItemAtPosition(position)).getSectionId();
                break;
            case R.id.spn_class_name:
                ClassSpinnerValue = ((ClassModel) adapterView.getItemAtPosition(position)).getClassId();
                break;
            case R.id.spn_class_section_name:
                ClassSpinnerValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getClassId();
                SectionSpinnerValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getSectionId();
                ClassSpinnerRankValue = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getRank();
                populateNextClassSpinner(SchoolSpinnerValue, ClassSpinnerValue, ClassSpinnerRankValue, SectionSpinnerValue);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setCollapsing(boolean isCollapsable) {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        if (isCollapsable)
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL); // list other flags here by |
        else
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        collapsingToolbar.setLayoutParams(params);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.bt_submit:
//                PromotionDialogManager pdm = new PromotionDialogManager(this, TotalPromotion.getText().toString(), TotalNotPromotion.getText().toString(), PromotionModel.getInstance().getPmList(), ClassSpinnerValue, SectionSpinnerValue);

                if (NextSectionSpinnerValue == 0 && NextClassSpinnerValue == 0)
                    Toast.makeText(PromotionActivity.this, "Please select valid promotion class", Toast.LENGTH_SHORT).show();
                else {
                    PromotionDialogManager pdm = new PromotionDialogManager(this,
                            TotalPromotion.getText().toString(), TotalNotPromotion.getText().toString(),
                            PromotionModel.getInstance().getPmList(), SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, NextClassSpinnerValue, NextSectionSpinnerValue, this);
                    pdm.show();
                }
                break;
            case R.id.btnsearch:
                search();
                break;
        }
    }

    private void search() {

        if (SchoolSpinnerValue == 0) {
            MessageBox("Please select search parameters!");
            hideViews();
        } else if (ClassSpinnerValue <= 0 && SectionSpinnerValue <= 0) {
            MessageBox("Please select search parameters!");
            hideViews();
        } else {
//                    pd = ProgressDialog.show(PromotionActivity.this, "", "Loading", false, false);

            StudentModel.getInstance().setStudentsList(
                    DatabaseHelper.getInstance(this).
                            getAllSearchedStudentsforPromotion(SchoolSpinnerValue, ClassSpinnerValue, SectionSpinnerValue, "", ""));

            if (StudentModel.getInstance().getStudentsList().size() > 0)
                setCollapsing(true);
            else
                setCollapsing(false);

            PromotionModel.getInstance().pmList = new ArrayList<>();
            for (int i = 0; i < StudentModel.getInstance().getStudentsList().size(); i++) {
                PromotionModel.getInstance().pModel = new PromotionModel();
                PromotionModel.getInstance().getPmList().add(PromotionModel.getInstance().getpModel());
            }
            int count = 0;
            if ((count = StudentModel.getInstance().getStudentsList().size()) > 0) {
                setAdapter();
                TotalPromotion.setText("  " + count + "");

            } else {
                MessageBox("No results found");
                hideViews();
            }
        }

//                if (ClassSpinner.getSelectedItem().toString().equals(getString(R.string.select_class)) ||
//                        (SectionSpinner.getSelectedItem().toString().equals(getString(R.string.select_section)))) {
//                    MessageBox("Please select search parameters!");
//                    hideViews();
//                } else {
////                    pd = ProgressDialog.show(PromotionActivity.this, "", "Loading", false, false);
//
//                    StudentModel.getInstance().setStudentsList(
//                            DatabaseHelper.getInstance(this).
//                                    getAllSearchedStudents(SurveyAppModel.getInstance().getSelectedSchool(this), ClassSpinnerValue, SectionSpinnerValue, "", ""));
//
//                    PromotionModel.getInstance().pmList = new ArrayList<>();
//                    for (int i = 0; i < StudentModel.getInstance().getStudentsList().size(); i++) {
//                        PromotionModel.getInstance().pModel = new PromotionModel();
//                        PromotionModel.getInstance().getPmList().add(PromotionModel.getInstance().getpModel());
//                    }
//                    int count = 0;
//                    if ((count = StudentModel.getInstance().getStudentsList().size()) > 0) {
//                        setAdapter();
//                        TotalPromotion.setText("  " + count + "");
//
//                    } else {
//                        MessageBox("No results found");
//                        hideViews();
//                    }
//                }
    }

    private void hideViews() {
        lll.setVisibility(View.GONE);
        bt_submit.setVisibility(View.GONE);
        setCollapsing(false);
    }

    private void setAdapter() {
        promotionAdapter = new PromotionAdapter(PromotionModel.getInstance().getPmList(), this);
        rv_promotionsList.setAdapter(promotionAdapter);
        showViews();
        rv_promotionsList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                rv_promotionsList.removeOnLayoutChangeListener(this);
                Log.e("tag", "updated " + PromotionModel.getInstance().getPmList().size());
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                    pd = null;
                }
            }
        });
    }

    private void showViews() {

        lll.setVisibility(View.VISIBLE);
        bt_submit.setVisibility(View.VISIBLE);
    }

    @Override
    public void studentsPromoted() {
        search();
    }
}
