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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.GraduationAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Managers.GraduationDialogManager;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ClassSectionModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.GraduationModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;

public class GraduationActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    View view;
    LinearLayout lll, ll_schoolSpinner;
    Button bt_submit, Search;
    TextView TotalGraduation, TotalNotGraduation;
    SchoolModel schoolModel;
    ClassSectionModel classSectionModel;
    ArrayAdapter<SchoolModel> SchoolAdapter;
    ArrayAdapter<ClassSectionModel> ClassSectionAdapter;
    int SchoolSpinnerValue = 0, ClassID = 0, SectionID = 0;
    EditText et_student_name;
    EditText et_GrNo;
    GraduationAdapter graduationAdapter;
    RecyclerView rv_graduationList;
    private Spinner SchoolSpinner, ClassSectionSpinner;
    private boolean isSelected = false;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = setActivityLayout(this, R.layout.activity_graduation);
        setToolbar("Graduation", this, false);
        init(view);
        initSchoolSpinners();
    }

    private void init(View view) {
        lll = (LinearLayout) view.findViewById(R.id.lll);

        ll_schoolSpinner = (LinearLayout) view.findViewById(R.id.ll_schoolSpinner);
        ll_schoolSpinner.setVisibility(View.VISIBLE);

        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);

        Search = (Button) view.findViewById(R.id.btnsearch);
        Search.setOnClickListener(this);

        TotalGraduation = (TextView) view.findViewById(R.id.tv_total_graduated);
        TotalNotGraduation = (TextView) view.findViewById(R.id.tv_total_non_graduated);

        SchoolSpinner = (Spinner) view.findViewById(R.id.spn_school);
        ClassSectionSpinner = (Spinner) view.findViewById(R.id.spn_class_section_name);

        ll_schoolSpinner = (LinearLayout) view.findViewById(R.id.ll_schoolSpinner);
        ll_schoolSpinner.setVisibility(View.VISIBLE);

        et_GrNo = (EditText) view.findViewById(R.id.et_GrNo);
        et_GrNo.setVisibility(View.GONE);
        et_student_name = (EditText) view.findViewById(R.id.et_student_name);
        et_student_name.setVisibility(View.GONE);

        rv_graduationList = (RecyclerView) view.findViewById(R.id.rv_graduationList);
        rv_graduationList.setNestedScrollingEnabled(false);
        rv_graduationList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_graduationList.setLayoutManager(llm);
    }

    private void initSchoolSpinners() {
        schoolModel = new SchoolModel();
        schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllUserSchoolsForGraduation());
        schoolModel.getSchoolsList().add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));
        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchoolSpinner.setAdapter(SchoolAdapter);
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModel.getSchoolsList(), this);
        if (indexOfSelectedSchool > -1) {
            SchoolSpinner.setSelection(indexOfSelectedSchool);
            SchoolSpinnerValue = AppModel.getInstance().getSpinnerSelectedSchool(this);
            isSelected = true;
        }
        SchoolSpinner.setOnItemSelectedListener(this);

        classSectionModel = new ClassSectionModel();
        classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionBySchoolId(String.valueOf(schoolModel.getId())));
        classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
        ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
        ClassSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ClassSectionSpinner.setAdapter(ClassSectionAdapter);
        ClassSectionSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                GraduationDialogManager gdm = new GraduationDialogManager(this,
                        TotalGraduation.getText().toString(), TotalNotGraduation.getText().toString(),
                        GraduationModel.getInstance().getGmList(), SchoolSpinnerValue, ClassID, SectionID);
                gdm.show();
                break;
            case R.id.btnsearch:

                if (SchoolSpinnerValue == 0) {
                    MessageBox("Please select search parameters!");
                    hideViews();
                } else if (ClassID <= 0 && SectionID <= 0) {
                    MessageBox("Please select search parameters!");
                    hideViews();
                } else {
//                    pd = ProgressDialog.show(PromotionActivity.this, "", "Loading", false, false);

                    StudentModel.getInstance().setStudentsList(
                            DatabaseHelper.getInstance(this).
                                    getAllSearchedStudentsforPromotion(SchoolSpinnerValue, ClassID, SectionID, "", ""));

                    GraduationModel.getInstance().gmList = new ArrayList<>();
                    for (int i = 0; i < StudentModel.getInstance().getStudentsList().size(); i++) {
                        GraduationModel.getInstance().gModel = new GraduationModel();
                        GraduationModel.getInstance().getGmList().add(GraduationModel.getInstance().getgModel());
                    }
                    int count = 0;
                    if ((count = StudentModel.getInstance().getStudentsList().size()) > 0) {
                        setAdapter();
                        TotalGraduation.setText("  " + count + "");

                    } else {
                        MessageBox("No results found");
                        hideViews();
                    }
                }

                break;
        }
    }

    private void setAdapter() {
        graduationAdapter = new GraduationAdapter(GraduationModel.getInstance().getGmList(), this);
        rv_graduationList.setAdapter(graduationAdapter);
        showViews();
        rv_graduationList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                rv_graduationList.removeOnLayoutChangeListener(this);
                Log.e("tag", "updated " + GraduationModel.getInstance().getGmList().size());
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                    pd = null;
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_school:
                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(this,
                        SchoolSpinnerValue);
                if (isSelected) {
                    classSectionModel = new ClassSectionModel();
                    classSectionModel.setClassAndSectionsList(DatabaseHelper.getInstance(this).getClassSectionForGraduation(SchoolSpinnerValue));
                    classSectionModel.getClassAndSectionsList().add(0, new ClassSectionModel(0, 0, getString(R.string.select_class_section)));
                    ClassSectionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classSectionModel.getClassAndSectionsList());
                    ClassSectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ClassSectionSpinner.setAdapter(ClassSectionAdapter);
                } else {
                    isSelected = true;
                }

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(this, SchoolSpinnerValue);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }
                break;
            case R.id.spn_class_section_name:
                ClassID = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getClassId();
                SectionID = ((ClassSectionModel) adapterView.getItemAtPosition(position)).getSectionId();
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void hideViews() {
        lll.setVisibility(View.GONE);
        bt_submit.setVisibility(View.GONE);
    }

    private void showViews() {

        lll.setVisibility(View.VISIBLE);
        bt_submit.setVisibility(View.VISIBLE);
    }

    public void setTotalGraduatedCount() {
        int g = 0, ng = 0;
        try {
            for (int i = 0; i < GraduationAdapter.mgma.size(); i++) {
                if (GraduationAdapter.mgma.get(i).getIsGraduated() != null) {
                    switch (GraduationAdapter.mgma.get(i).getIsGraduated()) {
                        case "y":
                            g++;
                            break;
                        case "n":
                            ng++;
                            break;
                    }
                }
                TotalGraduation.setText("  " + g);
                TotalNotGraduation.setText("  " + ng);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
