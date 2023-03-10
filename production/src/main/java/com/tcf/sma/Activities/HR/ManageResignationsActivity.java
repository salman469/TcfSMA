package com.tcf.sma.Activities.HR;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Adapters.HR.EmployeeAutoCompleteAdapter;
import com.tcf.sma.Adapters.HR.ManageResignationsAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Managers.HR.EmployeeApprovalDialogManager;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeAutoCompleteModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationDetailModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeResignationStatusModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ManageResignationsActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    Button pending;
    Button approved;
    Button reject;
    CardView card;
    LinearLayout ll_status;
    private View view;
    private Spinner spinner_SelectSchool;
    private RecyclerView rv_resignations;
    private Button btnSearch, clear;
    private Boolean isViewSeparation = false;
    private ArrayAdapter<SchoolModel> SchoolAdapter;
    private List<SchoolModel> schoolModels;
    private ManageResignationsAdapter manageResignationsAdapter;
    private List<EmployeeSeparationModel> employeeSeparationModelList = new ArrayList<>();

    RadioGroup rg_separationStatus;
    RadioButton rb_pending;


    private AutoCompleteTextView et_std_name_grNo;
    private EmployeeAutoCompleteAdapter employeeAutocompleteAdapter;
    private String empFirstName = "", empLastName = "", empEmpCode = "";
    public LinearLayout ll_total;
    public TextView tv_total;
    boolean fromDashboard = false;



    ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
//            Toast.makeText(current_activity, "movemnet start", Toast.LENGTH_SHORT).show();
//            return true;
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            int position = viewHolder.getPosition();
            pending = viewHolder.itemView.findViewById(R.id.Btn_pending);
            approved = viewHolder.itemView.findViewById(R.id.Btn_approved);
            reject = viewHolder.itemView.findViewById(R.id.Btn_reject);
            card = viewHolder.itemView.findViewById(R.id.card_view);

            if (i == ItemTouchHelper.RIGHT) {

                changeStatus(position,3);

            } else if (i == ItemTouchHelper.LEFT) {

                changeStatus(position,2);

            }
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int position = viewHolder.getPosition();
            if (employeeSeparationModelList.get(position).getEmp_Status() == 2 || employeeSeparationModelList.get(position).getEmp_Status() == 3 || employeeSeparationModelList.get(position).getEmp_Status() == 4) {
                if (viewHolder instanceof ManageResignationsAdapter.ResignationDataVH) return 0;

            }
            return super.getSwipeDirs(recyclerView, viewHolder);
        }


        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(ManageResignationsActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ManageResignationsActivity.this, R.color.light_red))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ManageResignationsActivity.this, R.color.light_green))
                    .addSwipeRightLabel("A")
                    .addSwipeLeftLabel("R")
                    .setSwipeRightLabelColor(ContextCompat.getColor(ManageResignationsActivity.this, R.color.white_2))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(ManageResignationsActivity.this, R.color.white_2))
                    .setSwipeLeftLabelTextSize(2, 30)
                    .setSwipeRightLabelTextSize(2, 30)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX / 2, dY, actionState, isCurrentlyActive);
        }

    });
//    private int resignType = 1
    private int schoolId = 0;
    private int status, approvalStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = setActivityLayout(this, R.layout.activity_manage_resignations);
        init(view);

        if (!isViewSeparation) {
            setToolbar("Separation Approval", this, false);
        } else
            setToolbar("View Separation", this, false);

        working();
    }

    private void working() {

        et_std_name_grNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String emp_Code = ((EmployeeAutoCompleteModel) adapterView.getItemAtPosition(i)).getEmpCode();
                String emp_FirstName = ((EmployeeAutoCompleteModel) adapterView.getItemAtPosition(i)).getEmpFirstName();
                String emp_LastName = ((EmployeeAutoCompleteModel) adapterView.getItemAtPosition(i)).getEmpLastName();


                empEmpCode = emp_Code;
                empFirstName = emp_FirstName;
                empLastName = emp_LastName;
//        employeeCode = Integer.valueOf(emp_Code);

                et_std_name_grNo.setText(emp_FirstName + " " + emp_LastName);
//        SurveyAppModel.getInstance().hideSoftKeyboard(this);
                searchClicked();
            }
        });

        populateEmployeeAutocompleteAdapter();

        et_std_name_grNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() != 0) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    empEmpCode = "";
                    empFirstName = "";
                    empLastName = "";
                }
            }
        });

    }

    private void populateEmployeeAutocompleteAdapter() {
        final List<EmployeeAutoCompleteModel> employeesList = EmployeeHelperClass.getInstance(this).getAllExEmpsForAutocompleteList(schoolId, status, isViewSeparation);
        employeeAutocompleteAdapter = new EmployeeAutoCompleteAdapter(this, R.layout.employee_autocomplete_view,
                employeesList);
        et_std_name_grNo.setAdapter(employeeAutocompleteAdapter);

    }

    private void init(View view) {

        Intent intent = getIntent();
        if (intent.hasExtra("isViewSeparation")) {
            isViewSeparation = intent.getBooleanExtra("isViewSeparation", false);
        }

        if(intent.hasExtra("status"))
            fromDashboard = true;

        rb_pending = findViewById(R.id.rb_pending);

        // rv_empDetail = (RecyclerView)view.findViewById(R.id.rv_empDetail);
        btnSearch = (Button) view.findViewById(R.id.btn_Search);
        btnSearch.setOnClickListener(this);
        tv_total = findViewById(R.id.tv_total);

        ll_total = findViewById(R.id.ll_total);
        et_std_name_grNo = view.findViewById(R.id.et_std_name_grNo);
        clear = (Button) findViewById(R.id.clear);
        clear.setVisibility(View.INVISIBLE);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_std_name_grNo.setText("");
                clear.setVisibility(View.GONE);
                clearList();
            }
        });



        rg_separationStatus = view.findViewById(R.id.rg_status);
        rg_separationStatus.setOnCheckedChangeListener(this);
        if(!isViewSeparation)
            rg_separationStatus.setVisibility(View.GONE);

        spinner_SelectSchool = (Spinner) view.findViewById(R.id.spinner_SelectSchool);
        spinner_SelectSchool.setOnItemSelectedListener(this);


        rv_resignations = view.findViewById(R.id.rv_resigns);
        rv_resignations.setNestedScrollingEnabled(false);
        rv_resignations.setHasFixedSize(true);
        rv_resignations.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));



        populateSpinners();
//        populateStatusSpinner();
    }

    private void populateSpinners() {

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForEmployeeResignationAndTermination();
//        schoolModels.add(0, new SchoolModel(0, getResources().getString(R.string.select_school)));

//        if (schoolModels.size() > 1) {
            schoolModels.add(schoolModels.size(), new SchoolModel(0, "All"));
//        }

        SchoolAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
        SchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SelectSchool.setAdapter(SchoolAdapter);

//        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
//        if (indexOfSelectedSchool > -1) {
//            spinner_SelectSchool.setSelection(indexOfSelectedSchool);
//        }
        spinner_SelectSchool.setSelection(schoolModels.size()-1);

//        if(DatabaseHelper.getInstance(ManageResignationsActivity.this).getCurrentLoggedInUser().getRoleId() == 103){
//            spinner_SelectSchool.setEnabled(false);
//        }

    }
/*
    private void populateStatusSpinner() {
        StatusList = EmployeeHelperClass.getInstance(this).getResignStatus();
        StatusList.add(0, new EmployeeResignationStatusModel(0, getString(R.string.select_status)));

        if (StatusList.size() > 1) {
            StatusList.add(StatusList.size(), new EmployeeResignationStatusModel(StatusList.size(), "All"));
        }

        StatusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, StatusList);
        StatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SelectStatus.setAdapter(StatusAdapter);
        spinner_SelectStatus.setSelection(StatusList.size() - 1);



//        int indexOfSelectedStatus = SurveyAppModel.getInstance().getStatusPosition(this);
//        if (indexOfSelectedStatus > -1) {
//            spinner_SelectStatus.setSelection(indexOfSelectedStatus);
//        }

//        int indexOfSelectedApprovalStatus = getStatusIndex();
//        spinner_SelectStatus.setSelection(indexOfSelectedApprovalStatus);


    }

    private int getStatusIndex() {
        int index = 0;
        for (EmployeeResignationStatusModel model : StatusList) {
            if (model.getId() == status)
                return index;
            index++;
        }
        return index;
    }
*/
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Search) {

            searchClicked();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (adapterView.getId() == R.id.spinner_SelectSchool) {
            schoolId = ((SchoolModel) adapterView.getItemAtPosition(position)).getId();
//            AppModel.getInstance().setSpinnerSelectedSchool(this,
//                    schoolId);
        }
        else if (adapterView.getId() == R.id.spinner_SelectStatus) {
            status = ((EmployeeResignationStatusModel) adapterView.getItemAtPosition(position)).getId();
        }
        if(!isViewSeparation)
            status = 1;
        else if(fromDashboard){
            status = 1;
            fromDashboard = false;

        }
        else
            status = 0;


        populateEmployeeAutocompleteAdapter();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setAdapter() {
        if (employeeSeparationModelList != null && employeeSeparationModelList.size() > 0) {
            manageResignationsAdapter = new ManageResignationsAdapter(ManageResignationsActivity.this, employeeSeparationModelList, isViewSeparation);
            rv_resignations.setAdapter(manageResignationsAdapter);
            if (!isViewSeparation) {
                touchHelper.attachToRecyclerView(rv_resignations);
            }
            rv_resignations.setVisibility(View.VISIBLE);
            rv_resignations.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_fall_down));
            ll_total.setVisibility(View.VISIBLE);
            if(isViewSeparation)
                tv_total.setText("Total Separations: " + employeeSeparationModelList.size());
            else
                tv_total.setText("Pending Separation Approvals: " + employeeSeparationModelList.size());
        } else {
            MessageBox("No records found!");
            rv_resignations.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_slide_up));
            rv_resignations.setVisibility(View.GONE);
            ll_total.setVisibility(View.GONE);
        }
    }

    public void searchClicked() {
        rv_resignations.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_slide_up));
        rv_resignations.setVisibility(View.GONE);
        try {

            if (spinner_SelectSchool.getSelectedItem().toString().equals(getString(R.string.select_school))) {
                MessageBox("Please select school!");
                clearList();
            }
            else {
                if(isViewSeparation)
                    employeeSeparationModelList = EmployeeHelperClass.getInstance(ManageResignationsActivity.this).getViewSeparations(schoolId, status, 0, empFirstName, empLastName);
                else
                    employeeSeparationModelList = EmployeeHelperClass.getInstance(ManageResignationsActivity.this).getPendingApprovals(schoolId, empFirstName, empLastName);
                setAdapter();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearList() {
        if (manageResignationsAdapter != null) {
            employeeSeparationModelList = null;
            rv_resignations.setVisibility(View.GONE);
            ll_total.setVisibility(View.GONE);
        }
    }

    private void changeStatus(int position, int Status) {
        EmployeeModel employeeModel = EmployeeHelperClass.getInstance(ManageResignationsActivity.this).getEmployee(employeeSeparationModelList.get(position).getEmployee_Personal_Detail_ID());

        EmployeeSeparationDetailModel esdm = new EmployeeSeparationDetailModel();
        esdm.setEmp_status(Status);
        esdm.setEmployeeResignationId(employeeSeparationModelList.get(position).getServerId());
        EmployeeApprovalDialogManager eadm = new EmployeeApprovalDialogManager(this, employeeModel, esdm, position);
        eadm.show();
        eadm.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(employeeSeparationModelList.size() > 1)
                    searchClicked();
                else{
                    startActivity(new Intent(ManageResignationsActivity.this, NewDashboardActivity.class));
                    finish();
                }
            }
        });

        /*LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.resignation_cancel_dialog, null);
//        cancelReason = dialogView.findViewById(R.id.edt_comment);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        final TextView name, code, designation;
        final LinearLayout ll_dialog = dialogView.findViewById(R.id.ll_dialog);
        name = dialogView.findViewById(R.id.emp_name);
        code = dialogView.findViewById(R.id.emp_code);
        designation = dialogView.findViewById(R.id.designation);
        final AlertDialog dialog = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setView(dialogView)
                .setTitle("Rejection Reason")
                .setMessage("Are you sure you want to proceed?")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        manageResignationsAdapter.notifyItemChanged(position);
                    }

                })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                EmployeeModel employeeModel = EmployeeHelperClass.getInstance(ManageResignationsActivity.this).getEmployee(employeeSeparationModelList.get(position).getEmployee_Personal_Detail_ID());
                ll_dialog.setBackgroundColor(ContextCompat.getColor(ManageResignationsActivity.this,R.color.light_red_color));
                designation.setText(employeeModel.getDesignation());
                name.setText(employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name());
                code.setText(employeeModel.getEmployee_Code());

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (!editText.getText().toString().trim().isEmpty()) {
                            try {
                                hideSoftKeyboard();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            manageResignationsAdapter.notifyItemChanged(position);
                            EmployeeSeparationDetailModel esdm = new EmployeeSeparationDetailModel();
                            String remarks = editText.getText().toString();
                            esdm.setEmp_status(2);
                            esdm.setSeparation_Remarks(remarks);
                            esdm.setEmployeeResignationId(employeeSeparationModelList.get(position).getServerId());

                            long id =   EmployeeHelperClass.getInstance(ManageResignationsActivity.this)
                                    .changeStatus(esdm);

                            if (id > 0) {

                                employeeSeparationModelList.remove(position);
                                Toast.makeText(getApplicationContext(), "Rejected", Toast.LENGTH_SHORT).show();
                                manageResignationsAdapter.notifyDataSetChanged();
                            } else {
                                MessageBox("Something went wrong");
                            }
                            setAdapter();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(ManageResignationsActivity.this, "Please Enter Reason", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();*/

    }


    private void approveResignation(int position) {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.resignation_cancel_dialog, null);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        final TextView name, code, designation;
        final LinearLayout ll_dialog = dialogView.findViewById(R.id.ll_dialog);
        name = dialogView.findViewById(R.id.emp_name);
        code = dialogView.findViewById(R.id.emp_code);
        designation = dialogView.findViewById(R.id.designation);

        final AlertDialog dialog = new AlertDialog.Builder(this,R.style.MyDialogThemeGreen)
                .setView(dialogView)
                .setTitle("Approve Separation")
                .setMessage("Are you sure you want to proceed?")
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        manageResignationsAdapter.notifyItemChanged(position);
                    }

                })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                EmployeeModel employeeModel = EmployeeHelperClass.getInstance(ManageResignationsActivity.this).getEmployee(employeeSeparationModelList.get(position).getEmployee_Personal_Detail_ID());
                ll_dialog.setBackgroundColor(ContextCompat.getColor(ManageResignationsActivity.this,R.color.light_app_green));
                designation.setText(employeeModel.getDesignation());
                name.setText(employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name());
                code.setText(employeeModel.getEmployee_Code());

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String approvalComments = editText.getText().toString();

                        try {
                            hideSoftKeyboard();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        manageResignationsAdapter.notifyItemChanged(position);

                        EmployeeSeparationDetailModel esdm = new EmployeeSeparationDetailModel();
                        esdm.setEmp_status(3);
                        esdm.setSeparation_Remarks(approvalComments);
                        esdm.setEmployeeResignationId(employeeSeparationModelList.get(position).getServerId());

                        long id = EmployeeHelperClass.getInstance(ManageResignationsActivity.this).changeStatus(esdm);
                        if (id > 0) {
                            employeeSeparationModelList.remove(position);
//                            searchClicked();
//                            card.setBackgroundColor(ContextCompat.getColor(ManageResignationsActivity.this, R.color.light_blue_color));
//                            pending.setVisibility(View.GONE);
//                            reject.setVisibility(View.GONE);
//                            approved.setVisibility(View.VISIBLE);
//                            Toast.makeText(getApplicationContext(), "Approved", Toast.LENGTH_SHORT).show();

                            manageResignationsAdapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                        setAdapter();
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();

    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(
                Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (!fromDashboard) {
            switch(i){
                case R.id.rb_all:
                    status = 0;
                    break;

                case R.id.rb_pending:
                    status = 1;
                    break;

                case R.id.rb_rejected:
                    status = 2;
                    break;

                case R.id.rb_approved:
                    status = 3;
                    break;

                case R.id.rb_cancelled:
                    status = 4;
                    break;
            }
        } else {
            status = 1;
            fromDashboard = false;
            rg_separationStatus.clearCheck();
            rb_pending.setChecked(true);
        }
    }
}
