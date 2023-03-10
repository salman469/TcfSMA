package com.tcf.sma.Activities.HRTCT;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Adapters.HRTCT.HRTCT_PreviousRegistrationAdapter;
import com.tcf.sma.Adapters.HRTCT.TCTEntryAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HRTCT.TCTHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HRTCT.TCTEmpSubjectTaggingModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class EmployeeTCT_EntryActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, TCTEntryAdapter.DatasetUpdateListener {

    public static final int STATUS_NOT_SUBMITTED = 1;
    public static final int STATUS_SUBMITTED = 2;
    public static final int STATUS_NOT_SUBMITTED_AUTO_SAVED = 3;
    private View view;
    private Spinner sp_school;
    private RecyclerView rv_tctEntry;
    private TextView tv_regionName, tv_areaName, errorMessage, noDataFound;
    private Button btn_submit_tct, btn_save_tct;
    private LinearLayout ll_tctEntry;

    private int schoolId = 0;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private List<SchoolModel> schoolModels;
    private TCTEntryAdapter tctEntryAdapter;
    private HRTCT_PreviousRegistrationAdapter tctViewOnlyAdapter;
    private List<TCTEmpSubjectTaggingModel> empSubjectTaggingModelList = new ArrayList<>();
    private List<TCTEmpSubjectTaggingModel> prevEmpSubjectTaggingModelList = new ArrayList<>();
    private final String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_employee_tct__entry);
        view = setActivityLayout(this, R.layout.activity_employee_tct__entry);
        setToolbar("TCT Entry", this, false);
        init(view);
    }

    private void init(View view) {
        sp_school = view.findViewById(R.id.spinner_SelectSchool);
        sp_school.setOnItemSelectedListener(this);


        tv_regionName = view.findViewById(R.id.tv_regionName);
        tv_areaName = view.findViewById(R.id.tv_areaName);

        noDataFound = view.findViewById(R.id.noDataFound);

        ll_tctEntry = view.findViewById(R.id.ll_tctEntry);
        btn_submit_tct = view.findViewById(R.id.btn_submit_tct);
        btn_submit_tct.setOnClickListener(this);
        btn_save_tct = view.findViewById(R.id.btn_save_tct);
        btn_save_tct.setOnClickListener(this);

        rv_tctEntry = view.findViewById(R.id.rv_tctEntry);
        rv_tctEntry.setNestedScrollingEnabled(false);
        rv_tctEntry.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_tctEntry.setLayoutManager(linearLayoutManager);

        populateSpinners();
        working();


    }

    private void working() {
        AppModel.getInstance().setSchoolInfo(this, schoolId, tv_regionName, tv_areaName);
        setAdapter();
    }

    private void populateSpinners() {

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForTCTEntry();
        schoolModels.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

        SchoolAdapter = new ArrayAdapter<>(this, R.layout.new_spinner_layout, schoolModels);
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_school.setAdapter(SchoolAdapter);

        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1) {
            sp_school.setSelection(indexOfSelectedSchool);
        }

        schoolId = ((SchoolModel) sp_school.getSelectedItem()).getId();
//        setAdapter();
    }

    private void setAdapter() {
        prevEmpSubjectTaggingModelList = TCTHelperClass.getInstance(EmployeeTCT_EntryActivity.this).getEmpTakingTCT(schoolId);
        empSubjectTaggingModelList = TCTHelperClass.getInstance(EmployeeTCT_EntryActivity.this).getEmpTakingTCT(schoolId);

        if (empSubjectTaggingModelList != null && empSubjectTaggingModelList.size() > 0) {
            if (!empSubjectTaggingModelList.get(0).isViewOnly()) {
                btn_submit_tct.setVisibility(View.VISIBLE);
                btn_save_tct.setVisibility(View.VISIBLE);
                tctEntryAdapter = new TCTEntryAdapter(EmployeeTCT_EntryActivity.this, empSubjectTaggingModelList, prevEmpSubjectTaggingModelList, EmployeeTCT_EntryActivity.this, schoolId);
                rv_tctEntry.setAdapter(tctEntryAdapter);
            } else {
                btn_submit_tct.setVisibility(View.GONE);
                btn_save_tct.setVisibility(View.GONE);
                tctViewOnlyAdapter = new HRTCT_PreviousRegistrationAdapter(empSubjectTaggingModelList);
                rv_tctEntry.setAdapter(tctViewOnlyAdapter);
            }
            showData();
        } else {
            hideData();
        }
    }

    private void hideData() {
        ll_tctEntry.setVisibility(View.GONE);
        btn_submit_tct.setVisibility(View.GONE);
        btn_save_tct.setVisibility(View.GONE);
        noDataFound.setVisibility(View.VISIBLE);
    }

    private void showData() {
        ll_tctEntry.setVisibility(View.VISIBLE);
//        btn_submit_tct.setVisibility(View.VISIBLE);
//        btn_save_tct.setVisibility(View.VISIBLE);
        noDataFound.setVisibility(View.GONE);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (adapterView.getId() == R.id.spinner_SelectSchool) {
            schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
            AppModel.getInstance().setSpinnerSelectedSchool(this,
                    schoolId);
            AppModel.getInstance().setSchoolInfo(this, schoolId, tv_regionName, tv_areaName);
            setAdapter();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit_tct) {
            if(validateForMendatory()){

                if (checkChanges()) {
                    if (validate()) {
                        if (validateLeaveType()) {
                            if (validateNewDes()) {
                                if (validateSubs())
                                    submitTctSubjectsTagging(STATUS_SUBMITTED);
                            }
                        }
                    }
                } else {
                    submitTctSubjectsTagging(STATUS_SUBMITTED);
                    //ShowToast("Nothing is changed!");
                }
            }
        } else if (v.getId() == R.id.btn_save_tct) {
            if (checkChanges()) {
                if (validate()) {
                    if (validateLeaveType()) {
                        if (validateNewDes()) {
                            if (validateSubs())
                                submitTctSubjectsTagging(STATUS_NOT_SUBMITTED);
                        }
                    }
                }
            } else {
                submitTctSubjectsTagging(STATUS_NOT_SUBMITTED);
//                ShowToast("Nothing is changed!");
            }
        }
    }

    private boolean validate() {
        int emptyCnic = 0, invalidCnic = 0, subject1 = 0, subject2 = 0;

        for (TCTEmpSubjectTaggingModel model : empSubjectTaggingModelList) {

            if (Strings.isEmptyOrWhitespace(model.getCNIC().trim())) {
                if (model.getCnicEditText() != null) {
                    model.getCnicEditText().setError("Empty CNIC");
                }
                emptyCnic++;
            } else if (model.getCNIC().length() != 13) {
                if (model.getCnicEditText() != null) {
                    model.getCnicEditText().setError("Invalid CNIC");
                }
                invalidCnic++;
            }/* else if((model.getNewDesignationId()==3||model.getNewDesignationId()==7||model.getNewDesignationId()==11||model.getNewDesignationId()==10 )&&model.getReasonID()==3){
                if(model.getSubject1_ID()==0 ){
                  //  model.getSubject1_ID().;
                    subject1++;
                     msg=model.getEMP_Name()+ " Subject 1 is required";
                }else if(model.getSubject2_ID()==0&&model.getNewDesignationId()!=3){
                   // model.getCnicEditText().setError("Subject 2 Required");
                    subject2++;
                     msg=model.getEMP_Name()+ " Subject 2 is required";

                }
            }else if(model.getDesignation_ID()==43||model.getDesignation_ID()==52||model.getDesignation_ID()==57||model.getDesignation_ID()==58){

                if(model.getSubject1_ID()==0 ){
                    subject1++;
                   msg =model.getEMP_Name()+ " Subject 1 is required";

                }

                else if(model.getSubject2_ID()==0&&model.getDesignation_ID()!=57){
                    subject2++;
                    msg=model.getEMP_Name()+ " Subject 2 is required";

                }

            }*/

            /*if (model.getReasonID() == 1 && (model.getComment() == null || model.getComment().isEmpty())){ //Transferred to School ID:
                error++;
                tranferScIdError++;
            }*/
        }

        String errorMsg = "";
        if (emptyCnic > 1)
            errorMsg = emptyCnic + " CNIC fields are empty. Please add all CNIC.";
        else if (emptyCnic == 1)
            errorMsg = emptyCnic + " CNIC field is empty. Please add CNIC.";

        if (invalidCnic > 1) {
            if (Strings.isEmptyOrWhitespace(errorMsg))
                errorMsg = invalidCnic + " CNIC fields are invalid. They should be without dashes and 13 digits.";
            else
                errorMsg += "\n " + invalidCnic + " CNIC fields are also invalid. They should be without dashes and 13 digits.";
        } else if (invalidCnic == 1) {
            if (Strings.isEmptyOrWhitespace(errorMsg))
                errorMsg = invalidCnic + " CNIC field is invalid. It should be without dashes and 13 digits.";
            else
                errorMsg += "\n " + invalidCnic + " CNIC field is also invalid. It should be without dashes and 13 digits.";
        }

        /*if(subject1>0)
            ShowToast(msg);
        else if(subject2>0)
            ShowToast(msg);*/
//        if (!Strings.isEmptyOrWhitespace(errorMsg))
//            AppModel.getInstance().showMessage(new WeakReference<>(this), "CNIC Error", errorMsg);
//            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
        return !(emptyCnic > 0 || invalidCnic > 0
//                ||subject1>0||subject2>0
        );
    }

    private boolean checkChanges() {
        int i = 0;
        for (int j = 0; j < prevEmpSubjectTaggingModelList.size(); j++) {
            TCTEmpSubjectTaggingModel newModel = empSubjectTaggingModelList.get(j);
            TCTEmpSubjectTaggingModel prevModel = prevEmpSubjectTaggingModelList.get(j);
            newModel.setDesignation_ID(prevModel.getDesignation_ID());
            if (newModel.getSubject1_ID() != prevModel.getSubject1_ID() ||
                    newModel.getSubject2_ID() != prevModel.getSubject2_ID() ||
                    newModel.getReasonID() != prevModel.getReasonID() ||
                    newModel.getLeaveTypeID() != prevModel.getLeaveTypeID() ||
                    newModel.getNewDesignationId() != prevModel.getNewDesignationId() ||
                    (prevModel.getCNIC() != null && newModel.getCNIC() != null && !newModel.getCNIC().equals(prevModel.getCNIC()))) {
                i++;
            }
            newModel.setUploadedOn((String) null);
        }
        return i > 0;
    }

    private boolean validateSubs() {
        StringBuilder subject1Message = new StringBuilder("Please Add Subject 1 for following employees first: ");
        StringBuilder subject2Message = new StringBuilder("Please Add Subject 2 for following employees first: ");
        int sub1 = 0, sub2 = 0;
        for (TCTEmpSubjectTaggingModel model : empSubjectTaggingModelList) {
            int subModelSize = model.getTctSubjectsModels().size();
            int reasonId = model.getReasonID();
             if (model.isMandatory() && reasonId != 7) {
                if (reasonId > 0) {
                    if (reasonId == 3 || reasonId == 9) {
                        if (subModelSize > 1 && model.getSubject1_ID() <= 0) {
                            subject1Message.append(model.getEMP_Name()).append(", ");
                            sub1++;
                        }


                        if (subModelSize > 2 && model.getSubject1_ID() != 10 && model.getSubject2_ID() <= 0) {
                            sub2++;
                            subject2Message.append(model.getEMP_Name()).append(", ");
                        }


                    } else if (reasonId == 4) {
                        if (subModelSize > 1 && model.getSubject1_ID() <= 0) {
                            sub1++;
                            subject1Message.append(model.getEMP_Name()).append(", ");
                        }
                    }
                } else {
                    if (model.getSubject1_ID() > 0 && subModelSize > 2 && model.getSubject1_ID() != 10 && model.getSubject2_ID() <= 0) {
                        sub2++;
                        subject2Message.append(model.getEMP_Name()).append(", ");
                    }
//                    if(subModelSize > 1 && model.getSubject1_ID() <= 0)
//                        i++;

//                    if (subModelSize > 2 && model.getSubject1_ID() != 10 && model.getSubject2_ID() <= 0)
//                        i++;

                }

                /*if (model.getTctSubjectsModels().size() == 2 && model.getTctSubjectsModels().get(0).getSubject().equals("Select Sub.")) {
                    if (model.getSubject1_ID() == 0 && (model.getReasonID() != 5 && model.getLeaveTypeID() < 1)
//                            && model.getReasonID() == 0
                    ) {
                        i++;
                        //                    model.setError(true);
                    }
                } else {
                    if (model.getSubject1_ID() == 0 && (model.getReasonID() != 5 && model.getLeaveTypeID() < 1)
//                            && model.getReasonID() == 0
                    ) {
                        i++;
                        //                    model.setError(true);
                    } else if (model.getSubject2_ID() == 0 && !(model.getReasonID() == 5 && model.getLeaveTypeID() > 0)
//                            && (model.getReasonID() == 0)
                    ) {
                        i++;
                        //                    model.setError(true);
                    }
                }*/
            }

        }
        String errorMsg = "";
        if (sub1 > 0 && sub2 > 0) {
            errorMsg = subject1Message.substring(0, subject1Message.length() - 2) + "\n\n" + subject2Message.substring(0, subject2Message.length() - 2);
        } else if (sub1 > 0)
            errorMsg = subject1Message.substring(0, subject1Message.length() - 2);
        else if (sub2 > 0)
            errorMsg = subject2Message.substring(0, subject2Message.length() - 2);

        if(!Strings.isEmptyOrWhitespace(errorMsg))
            AppModel.getInstance().showMessage(new WeakReference<>(EmployeeTCT_EntryActivity.this), "Choose All Subjects!", errorMsg);
        return sub1 == 0 && sub2 == 0;
    }

    private boolean validateForMendatory(){
        StringBuilder subject1Message = new StringBuilder("Please Add Subject 1 for following employees first: ");
        StringBuilder subject2Message = new StringBuilder("Please Add Subject 2 for following employees first: ");
        int sub1 = 0, sub2 = 0;
        for (TCTEmpSubjectTaggingModel model : empSubjectTaggingModelList) {
            int subModelSize = model.getTctSubjectsModels().size();
            int reasonId =model.getReasonID();
            if(model.isMandatory()&&reasonId <= 0){
                if (subModelSize > 1 && model.getSubject1_ID() <= 0) {
                    subject1Message.append(model.getEMP_Name()).append(", ");
                    sub1++;
                }


                if ( subModelSize > 2 && model.getSubject1_ID() != 10 && model.getSubject2_ID() <= 0) {
                    sub2++;
                    subject2Message.append(model.getEMP_Name()).append(", ");
                }
            }
        }
        String errorMsg = "";
        if (sub1 > 0 && sub2 > 0) {
            errorMsg = subject1Message.substring(0, subject1Message.length() - 2) + "\n\n" + subject2Message.substring(0, subject2Message.length() - 2);
        } else if (sub1 > 0)
            errorMsg = subject1Message.substring(0, subject1Message.length() - 2);
        else if (sub2 > 0)
            errorMsg = subject2Message.substring(0, subject2Message.length() - 2);

        if(!Strings.isEmptyOrWhitespace(errorMsg))
            AppModel.getInstance().showMessage(new WeakReference<>(EmployeeTCT_EntryActivity.this), "Choose All Subjects!", errorMsg);
        return sub1 == 0 && sub2 == 0;
    }

    private boolean validateLeaveType() {
        int i = 0;
        for (TCTEmpSubjectTaggingModel model : empSubjectTaggingModelList) {
            if (model.getReasonID() == 5 && model.getLeaveTypeID() < 1) {
                i++;
            }
        }
        if (i > 0) {
            ShowToast("Select Required Leave Type first.");
        }
        return i == 0;
    }

    private boolean validateNewDes() {
        int i = 0;
        for (TCTEmpSubjectTaggingModel model : empSubjectTaggingModelList) {
            if (model.getReasonID() == 3 && model.getNewDesignationId() < 1) {
                i++;
            }
        }
        if (i > 0) {
            ShowToast("Select Required Designations first.");
        }
        return i == 0;
    }

    private void submitTctSubjectsTagging(int regStatusID) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        String msg = "Are you sure you want to save changes?";
        if (regStatusID == STATUS_SUBMITTED)
            msg = "Are you sure you want to submit changes?";
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.create().requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setPositiveButton("Yes", (dialog1, which) -> {
            long id = TCTHelperClass.getInstance(EmployeeTCT_EntryActivity.this).updateEmpSubjectTagging(empSubjectTaggingModelList, regStatusID);
            if (id > 0) {
                ShowToast("Subject Tagged Successfully");

                //Important
                AppModel.getInstance().changeMenuPendingSyncCount(EmployeeTCT_EntryActivity.this, true);

                finish();
                startActivity(new Intent(new Intent(EmployeeTCT_EntryActivity.this, NewDashboardActivity.class)));
//                setAdapter();

            } else {
                ShowToast("Something went wrong!");
            }
        });
        dialog.setNegativeButton("No", (dialog12, which) -> dialog12.dismiss());
        dialog.create();
        dialog.show();
    }

    @Override
    public void onDataSetChanged(List<TCTEmpSubjectTaggingModel> models, int position) {
        if (models.size() > 0) {
            empSubjectTaggingModelList = models;
//            tctEntryAdapter.notifyItemChanged(position);
//            rv_tctEntry.scrollToPosition(position);
        }
    }

//    @Override
//    public void onDataSetChanged(List<TCTEmpSubjectTaggingModel> models) {
//        if (models.size() > 0) {
//            empSubjectTaggingModelList = models;
//        }
//    }
}
