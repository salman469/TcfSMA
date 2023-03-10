package com.tcf.sma.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.EnrollmentStatusAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.EnrollmentModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.ArrayList;

public class EnrollmentStatusActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener {
    View view;
    Button addNewStudentButton;
    private RecyclerView rv_enrollmentStatus;
    private EnrollmentStatusAdapter adapter;
    private ArrayList<EnrollmentModel> modelArrayList = new ArrayList<>();
    private int SchoolSpinnerValue = 0;
    private Spinner SchoolSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_enrollment_status);
        view = setActivityLayout(this, R.layout.activity_enrollment_status);
        setToolbar("Enrollment Status", this, false);
        init(view);
        populateSchoolSpinner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Important when any change in table call this method
//        AppModel.getInstance().changeMenuPendingSyncCount(EnrollmentStatusActivity.this, true);
        working();

        AppModel.getInstance().img1 = null;
        AppModel.getInstance().img2 = null;
        AppModel.getInstance().img3 = null;
    }

    private void init(View view) {

        rv_enrollmentStatus = (RecyclerView) view.findViewById(R.id.rv_enrollmentstatus);
        addNewStudentButton = (Button) findViewById(R.id.bt_add_new_student);
        addNewStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String[] schoolID = SchoolSpinner.getSelectedItem().toString().split("-");
                    Intent intent = new Intent(EnrollmentStatusActivity.this, NewAdmissionActivity.class);
                    intent.putExtra("SchoolID", Integer.parseInt(schoolID[0]));
                    startActivity(intent);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
        SchoolSpinner = (Spinner) view.findViewById(R.id.spn_school);
    }

    private void working() {
        rv_enrollmentStatus.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_enrollmentStatus.setLayoutManager(llm);
        modelArrayList = DatabaseHelper.getInstance(this).getIncompleteEnrollments(SchoolSpinnerValue);
        adapter = new EnrollmentStatusAdapter(modelArrayList, this);
        rv_enrollmentStatus.setAdapter(adapter);
    }

    private void populateSchoolSpinner() {
        SchoolModel schoolModel = new SchoolModel();
        schoolModel.setSchoolsList(DatabaseHelper.getInstance(this).getAllUserSchools());

        ArrayAdapter<SchoolModel> SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModel.getSchoolsList());
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SchoolSpinner.setAdapter(SchoolAdapter);
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModel.getSchoolsList(), this);
        if (indexOfSelectedSchool > -1)
            SchoolSpinner.setSelection(indexOfSelectedSchool);
        SchoolSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spn_school:
                SchoolSpinnerValue = ((SchoolModel) adapterView.getItemAtPosition(i)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(this,
                        SchoolSpinnerValue);
                working();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
