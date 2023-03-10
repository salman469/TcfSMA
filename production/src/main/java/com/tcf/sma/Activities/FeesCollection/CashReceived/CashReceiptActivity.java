package com.tcf.sma.Activities.FeesCollection.CashReceived;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.FeesCollection.ReceiptAdapter;
import com.tcf.sma.Adapters.StudentAutoCompleteAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AppInvoice;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Helpers.DbTables.FeesCollection.Scholarship_Category;
import com.tcf.sma.Managers.FeesCollection.GRDialogManager;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.FeeTypeModel;
import com.tcf.sma.Models.Fees_Collection.FeesDuesModel;
import com.tcf.sma.Models.Fees_Collection.FeesHeaderModel;
import com.tcf.sma.Models.Fees_Collection.FeesReceiptModel;
import com.tcf.sma.Models.Fees_Collection.PreviousReceivableModel;
import com.tcf.sma.Models.Fees_Collection.ViewReceivablesModels;
import com.tcf.sma.Models.ScholarshipCategoryModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentAutoCompleteModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CashReceiptActivity extends DrawerActivity implements ReceiptAdapter.DatasetUpdateListener, GRDialogManager.StudentDetailInterface {

    RecyclerView rv_fees;
    List<ViewReceivablesModels> viewReceivablesModels = new ArrayList<>();
    List<ViewReceivablesModels> temp = new ArrayList<>();
    TextView tv_date, tv_prevRecv, tv_name, tv_studentId, tv_gr, tv_className, tv_sectionName, tv_fathersName, et_amountRecv, et_balance, et_todaySales;
    Spinner sp_schools;
    EditText et_grNo, et_receiptNo;
    View screen;
    ImageView iv_search, student_Img;
    LinearLayout ll_schooSpinner, ll_content, linearLayoutName, linearLayoutStudenID, linearLayoutGR, linearLayoutFName, linearLayoutSectionAndClassName, linearLayoutImage;
    String currentDate;
    int studentId = -1;
    int schoolClassId = -1;
    int schoolId = 0, schoolYearId = 1, academicSession_id;
    String totalTodaySales = "";
    String totalAmountRecv = "";
    String deviceId;
    ArrayList<SchoolModel> schoolModels = new ArrayList<>();
    Button bt_ok, bt_cancel;
    TextView tv_cashInHand, tv_cashInHandSchool;
    private AutoCompleteTextView et_std_name_grNo;
    private StudentAutoCompleteAdapter studentAdapter;
    private TextView tv_monthlyFee, tv_category;
    private LinearLayout linearLayoutMonthlyFee, linearLayoutCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = setActivityLayout(this, R.layout.activity_cash_received);
        setToolbar("Receive Cash", this, false);
        init();
        getIntentData();
        populateStudentAutocompleteAdapter();
        et_std_name_grNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String std_grno = ((StudentAutoCompleteModel) adapterView.getItemAtPosition(position)).getGrNo();
                String std_name = ((StudentAutoCompleteModel) adapterView.getItemAtPosition(position)).getStudent_name();
                et_grNo.setText(std_grno);
                et_std_name_grNo.setText(std_name);

                showDetails();
                AppModel.getInstance().hideSoftKeyboard(CashReceiptActivity.this);

            }
        });
    }

    private void populateStudentAutocompleteAdapter() {
        final List<StudentAutoCompleteModel> studentsList = DatabaseHelper.getInstance(this).getAllStudentsForAutocompleteList(schoolId);
        studentAdapter = new StudentAutoCompleteAdapter(this, R.layout.student_autocomplete_view,
                studentsList);
        et_std_name_grNo.setAdapter(studentAdapter);
    }

    void init() {
        et_amountRecv = (TextView) screen.findViewById(R.id.et_amountRecv);
        et_todaySales = (TextView) screen.findViewById(R.id.et_todaySales);
        tv_prevRecv = (TextView) screen.findViewById(R.id.tv_prevRecv);
        et_receiptNo = (EditText) screen.findViewById(R.id.et_receiptNo);
        et_balance = (TextView) screen.findViewById(R.id.et_amountBalance);
        et_grNo = (EditText) screen.findViewById(R.id.et_grNo);
        et_std_name_grNo = (AutoCompleteTextView) screen.findViewById(R.id.et_std_name_grNo);
        iv_search = (ImageView) screen.findViewById(R.id.iv_search);
        ll_content = (LinearLayout) screen.findViewById(R.id.ll_content);
        tv_date = (TextView) screen.findViewById(R.id.tv_date);
        tv_gr = screen.findViewById(R.id.tv_gr);
        tv_studentId = screen.findViewById(R.id.tv_student_id);
        tv_name = (TextView) screen.findViewById(R.id.tv_name);
        ll_schooSpinner = (LinearLayout) screen.findViewById(R.id.ll_schoolSpinner);
        tv_cashInHand = (TextView) screen.findViewById(R.id.tv_cashInHand);
        tv_cashInHandSchool = (TextView) screen.findViewById(R.id.tv_cashInHand_2);
        tv_className = (TextView) screen.findViewById(R.id.tv_className);
        tv_sectionName = (TextView) screen.findViewById(R.id.tv_sectionName);
        tv_fathersName = (TextView) screen.findViewById(R.id.tv_fathersName);
        student_Img = (ImageView) screen.findViewById(R.id.student_Img);
        linearLayoutStudenID = (LinearLayout) screen.findViewById(R.id.linearLayoutStudentId);
        linearLayoutGR = (LinearLayout) screen.findViewById(R.id.linearLayoutGR);
        linearLayoutName = (LinearLayout) screen.findViewById(R.id.linearLayoutName);
        linearLayoutFName = (LinearLayout) screen.findViewById(R.id.linearLayoutFName);
        linearLayoutSectionAndClassName = (LinearLayout) screen.findViewById(R.id.linearLayoutSectionAndClassName);
        linearLayoutImage = (LinearLayout) screen.findViewById(R.id.linearLayoutImage);

        linearLayoutMonthlyFee = (LinearLayout) screen.findViewById(R.id.linearLayoutMonthlyFee);
        linearLayoutCategory = (LinearLayout) screen.findViewById(R.id.linearLayoutCategory);

        tv_monthlyFee = (TextView) screen.findViewById(R.id.tv_monthlyFee);
        tv_category = (TextView) screen.findViewById(R.id.tv_category);


        deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);


        et_grNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clearStudentFields();
                visibilityGone();
                if (ll_content.getVisibility() == View.VISIBLE)
                    ll_content.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        et_std_name_grNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clearStudentFields();
                visibilityGone();
                if (ll_content.getVisibility() == View.VISIBLE)
                    ll_content.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_grNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    showDetails();
                    AppModel.getInstance().hideSoftKeyboard(CashReceiptActivity.this);
                    return true;
                }
                return false;
            }
        });

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetails();
            }
        });


        populateSchoolSpinner();

//
//        sp_schools.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,
//                getResources().getStringArray(R.array.schoolsList)));


        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MMM-yy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        Date date = Calendar.getInstance().getTime();
        currentDate = sdf1.format(date);
        tv_date.setText(sdf3.format(date));
//        tv_date.setText(sdf2.format(date));

        rv_fees = (RecyclerView) screen.findViewById(R.id.rv_fees);
        rv_fees.setLayoutManager(new LinearLayoutManager(this));
        rv_fees.setAdapter(new ReceiptAdapter(this, viewReceivablesModels, this));
        rv_fees.setNestedScrollingEnabled(false);

        bt_ok = (Button) screen.findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                if (validate()) {
                    submitReceipt();
                }

            }
        });
        bt_cancel = (Button) screen.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboard();

                final AlertDialog.Builder dialog = new AlertDialog.Builder(CashReceiptActivity.this);
                dialog.setMessage("Are you sure you want to cancel?");
                dialog.create().requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(CashReceiptActivity.this, CashReceiptActivity.class));
                        finish();
                        dialogInterface.dismiss();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        });
        getTotal();

        et_amountRecv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                AppModel.getInstance().setCashinHand(CashReceiptActivity.this,
                        schoolId, null, tv_cashInHand, tv_cashInHandSchool, s.toString());
            }
        });

    }

    private void submitReceipt() {
        try {
            Log.i("submit","Count");
            if (areAllSalesEmpty() && areAllAmountsEmpty()) {
                Toast.makeText(this, "Please enter data", Toast.LENGTH_SHORT).show();
            } else if (!areAllAmountsEmpty() && areAllSalesEmpty()) {
                setSubmitButtonEnabled(false);
                showDialog("Total Received: Rs. " + totalAmountRecv + "\nTotal Cash In Hand now: Rs. " + tv_cashInHand.getText().toString() +
                        "\n\nAre you sure you received Rs." + totalAmountRecv + " from " + tv_name.getText().toString() + "?", 0);
            } else if (areAllAmountsEmpty() && !areAllSalesEmpty()) {
                setSubmitButtonEnabled(false);
                showDialog("Total Sales: Rs. " + totalTodaySales +
                        "\n\nAre you sure you gave " + tv_name.getText().toString() + " goods worth Rs." + totalTodaySales + "?", 1);
            } else if (!areAllAmountsEmpty() && !areAllSalesEmpty()) {
                setSubmitButtonEnabled(false);
                showDialog("Total Sales: Rs. " + totalTodaySales + "\nTotal Received: Rs. " +
                        totalAmountRecv + "\nTotal Cash In Hand now: Rs. " + tv_cashInHand.getText().toString() +
                        "\n\nAre you sure you gave " +
                        tv_name.getText().toString() + " goods worth Rs." +
                        totalTodaySales + " and received Rs." +
                        totalAmountRecv + " from " +
                        tv_name.getText().toString() + "?", 2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog(String message, final int action) { // action 0 for only receipt, 1 for only sales and 2 for both

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.create().requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                long hid1 = 0, hid2 = 0;
                setSubmitButtonEnabled(true);
                switch (action) {
                    case 0:
                        /* For AppReceipt */
//                        AppReceipt.getInstance(CashReceiptActivity.this).insertFeesReceipt(getFeesReceiptModel());
                        hid1 = FeesCollection.getInstance(CashReceiptActivity.this).insertFeesHeader(getFeesReceipt());
                        if (hid1 > 0) {
                            FeesCollection.getInstance(CashReceiptActivity.this).insertFeesDetails(hid1, viewReceivablesModels, 2);
                        }
                        break;

                    case 1:
                        /* For AppInvoice */
//                        AppInvoice.getInstance(CashReceiptActivity.this).insertFeesReceipt(getFeesDueModel());
                        hid2 = FeesCollection.getInstance(CashReceiptActivity.this).insertFeesHeader(getFeesDue());
                        if (hid2 > 0) {
                            FeesCollection.getInstance(CashReceiptActivity.this).insertFeesDetails(hid2, viewReceivablesModels, 1);
                        }
                        break;

                    case 2:
//                        AppReceipt.getInstance(CashReceiptActivity.this).insertFeesReceipt(getFeesReceiptModel());
//                        AppInvoice.getInstance(CashReceiptActivity.this).insertFeesReceipt(getFeesDueModel());

                        /* For AppReceipt */
                        hid1 = FeesCollection.getInstance(CashReceiptActivity.this).insertFeesHeader(getFeesReceipt());
                        if (hid1 > 0) {
                            FeesCollection.getInstance(CashReceiptActivity.this).insertFeesDetails(hid1, viewReceivablesModels, 2);
                        }

                        /* For AppInvoice */
                        FeesHeaderModel feesHeaderModel = getFeesDue();
                        feesHeaderModel.setReceipt_id(hid1);
                        feesHeaderModel.setReceiptNumber(Long.parseLong(feesHeaderModel.getCreated_ref_id()));
                        hid2 = FeesCollection.getInstance(CashReceiptActivity.this).insertFeesHeader(feesHeaderModel);
                        if (hid2 > 0) {
                            FeesCollection.getInstance(CashReceiptActivity.this).insertFeesDetails(hid2, viewReceivablesModels, 1);
                        }

                        break;

                }
                //Important when any change in table call this method
                AppModel.getInstance().changeMenuPendingSyncCount(CashReceiptActivity.this, true);

                startActivity(new Intent(CashReceiptActivity.this, CashReceiptActivity.class));
                finish();
                dialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setSubmitButtonEnabled(true);
                dialogInterface.dismiss();
            }
        });
        dialog.show();

    }


    private boolean areAllAmountsEmpty() {
        for (ViewReceivablesModels model : viewReceivablesModels) {
            if (model.getAmountReceived() != null && !model.getAmountReceived().isEmpty())
                if (!model.getAmountReceived().matches("[0]+"))
                    return false;
        }
        return true;
    }

    private boolean areAllSalesEmpty() {
        for (ViewReceivablesModels model : viewReceivablesModels) {
            if (model.getTodaySales() != null && !model.getTodaySales().isEmpty())
                if (!model.getTodaySales().matches("[0]+"))
                    return false;
        }
        return true;
    }

    private FeesDuesModel getFeesDueModel() {
        FeesDuesModel model = new FeesDuesModel();

        String fees_copies = viewReceivablesModels.get(4).getTodaySales();
        String fees_books = viewReceivablesModels.get(3).getTodaySales();
        String fees_uniform = viewReceivablesModels.get(5).getTodaySales();
        String fees_others = viewReceivablesModels.get(6).getTodaySales();

        if (fees_copies != null && fees_copies != "")
            model.setFees_copies(Double.valueOf(fees_copies));

        if (fees_books != null && fees_books != "")
            model.setFees_books(Double.valueOf(fees_books));

        if (fees_uniform != null && fees_uniform != "")
            model.setFees_uniform(Double.valueOf(fees_uniform));

        if (fees_others != null && fees_others != "")
            model.setFees_other(Double.valueOf(fees_others));

        model.setStudent_id(studentId);
        model.setCreated_on(currentDate);
        model.setSchool_year_id(schoolYearId);
        model.setSchool_id(schoolId);
        model.setCreated_by(String.valueOf(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId()));
        model.setSchoolclass_id(schoolClassId);
        model.setDevice_id(deviceId);
        model.setCreated_ref_id(et_receiptNo.getText().toString());
        model.setCreated_from("R");
        return model;

    }

    private FeesReceiptModel getFeesReceiptModel() {
        FeesReceiptModel model = new FeesReceiptModel();
        model.setAdmissionFees(viewReceivablesModels.get(0).getAmountReceived());
        model.setExamFees(viewReceivablesModels.get(1).getAmountReceived());
        model.setCopyFees(viewReceivablesModels.get(4).getAmountReceived());
        model.setBookFees(viewReceivablesModels.get(3).getAmountReceived());
        model.setUniformFees(viewReceivablesModels.get(5).getAmountReceived());
        model.setTutionFees(viewReceivablesModels.get(2).getAmountReceived());
        model.setOthersFees(viewReceivablesModels.get(6).getAmountReceived());
        model.setStudentId(studentId);
        model.setCreatedOn(currentDate);
        model.setSchoolYearId(schoolYearId);
        model.setSchoolClassId(schoolClassId);
        model.setCorrectionType("0");
        model.setCreatedBy(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
        model.setDeviceId(deviceId);
        try {
            model.setReceiptNumber(Integer.valueOf(et_receiptNo.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

//    private FeesHeaderModel getFeesDue() {
//        FeesHeaderModel model = new FeesHeaderModel();
//
//        String fees_copies = viewReceivablesModelsCorrection.get(4).getTodaySales();
//        String fees_books = viewReceivablesModelsCorrection.get(3).getTodaySales();
//        String fees_uniform = viewReceivablesModelsCorrection.get(5).getTodaySales();
//        String fees_others = viewReceivablesModelsCorrection.get(6).getTodaySales();
//
//        if (fees_copies != null && fees_copies != "")
//            model.setCopyFees(Double.valueOf(fees_copies));
//
//        if (fees_books != null && fees_books != "")
//            model.setBookFees(Double.valueOf(fees_books));
//
//        if (fees_uniform != null && fees_uniform != "")
//            model.setUniformFees(Double.valueOf(fees_uniform));
//
//        if (fees_others != null && fees_others != "")
//            model.setOthersFees(Double.valueOf(fees_others));
//
//        model.setStudentId(studentId);
//        model.setCreatedOn(currentDate);
//        model.setSchoolYearId(schoolYearId);
//        model.setSchool_id(schoolId);
//        model.setCreatedBy(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
//        model.setSchoolClassId(schoolClassId);
//        model.setDeviceId(deviceId);
//        model.setCreated_ref_id(et_receiptNo.getText().toString());
//        model.setCreated_from("R");
//        model.setReciptFlag("Invoice");
//        return model;
//
//    }

    private FeesHeaderModel getFeesDue() {
        int userId = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId();
        double totalAmount = 0.0;
        FeesHeaderModel model = new FeesHeaderModel();

        //Category 1 for Invoice
        model.setCategory_id(1);
        model.setFor_date(currentDate);
        model.setStudentId(studentId);
        model.setCreatedOn(currentDate);
        model.setSchoolYearId(schoolYearId);
        model.setAcademicSession_id(academicSession_id);
        model.setSchool_id(schoolId);
        model.setSchoolClassId(schoolClassId);
        model.setCreatedBy(userId);
        model.setDeviceId(deviceId);
        model.setTransactionType_id(1);
        model.setCreated_ref_id(et_receiptNo.getText().toString());

        for (int i = 0; i < viewReceivablesModels.size(); i++) {
            if (viewReceivablesModels.get(i).getTodaySales() != null && viewReceivablesModels.get(i).getTodaySales() != "") {
                totalAmount += Double.valueOf(viewReceivablesModels.get(i).getTodaySales());
            }
        }
        model.setTotal_amount(totalAmount);
//        model.setFeeType_id(viewReceivablesModelsCorrection.get(i).getFeeTypeId());
//            model.setCreated_from("R");
        return model;
    }

    private FeesHeaderModel getFeesReceipt() {
        int userId = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId();
        double totalAmount = 0.0;
        FeesHeaderModel model = new FeesHeaderModel();

        //Category 2 for receipt
        model.setCategory_id(2);
        model.setFor_date(currentDate);
        model.setSchoolClassId(schoolClassId);
        model.setStudentId(studentId);
        model.setCreatedOn(currentDate);
        model.setSchoolYearId(schoolYearId);
        model.setAcademicSession_id(academicSession_id);
        model.setCorrectionType("0");
        model.setCreatedBy(userId);
        model.setDeviceId(deviceId);
        model.setTransactionType_id(1);

        try {
            model.setReceiptNumber(Long.parseLong(et_receiptNo.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < viewReceivablesModels.size(); i++) {
            if (viewReceivablesModels.get(i).getAmountReceived() != null && !viewReceivablesModels.get(i).getAmountReceived().equals("")) {
                totalAmount += Double.valueOf(viewReceivablesModels.get(i).getAmountReceived());

            }
//            model.setFeeType_id(viewReceivablesModelsCorrection.get(i).getFeeTypeId());

        }
        model.setTotal_amount(totalAmount);

//        model.setAdmissionFees(viewReceivablesModelsCorrection.get(0).getAmountReceived());
//        if (viewReceivablesModelsCorrection.get(1).getAmountReceived() != null && !viewReceivablesModelsCorrection.get(1).getAmountReceived().equals("")) {
//            model.setExamFees(Double.valueOf(viewReceivablesModelsCorrection.get(1).getAmountReceived()));
//        }
//        if (viewReceivablesModelsCorrection.get(4).getAmountReceived() != null && !viewReceivablesModelsCorrection.get(4).getAmountReceived().equals("")) {
//            model.setCopyFees(Double.valueOf(viewReceivablesModelsCorrection.get(4).getAmountReceived()));
//        }
//        if (viewReceivablesModelsCorrection.get(3).getAmountReceived() != null && !viewReceivablesModelsCorrection.get(3).getAmountReceived().equals("")) {
//            model.setBookFees(Double.valueOf(viewReceivablesModelsCorrection.get(3).getAmountReceived()));
//        }
//        if (viewReceivablesModelsCorrection.get(5).getAmountReceived() != null && !viewReceivablesModelsCorrection.get(5).getAmountReceived().equals("")) {
//            model.setUniformFees(Double.valueOf(viewReceivablesModelsCorrection.get(5).getAmountReceived()));
//        }
//        if (viewReceivablesModelsCorrection.get(2).getAmountReceived() != null && !viewReceivablesModelsCorrection.get(2).getAmountReceived().equals("")) {
//            model.setTutionFees(Double.valueOf(viewReceivablesModelsCorrection.get(2).getAmountReceived()));
//        }
//        if (viewReceivablesModelsCorrection.get(6).getAmountReceived() != null && !viewReceivablesModelsCorrection.get(6).getAmountReceived().equals("")) {
//            model.setOthersFees(Double.valueOf(viewReceivablesModelsCorrection.get(6).getAmountReceived()));
//        }

//        model.setDeviceId(deviceId);
//        model.setReciptFlag("Receipt");
        return model;
    }


    private boolean validate() {

        if (schoolId == 0) {
            Toast.makeText(CashReceiptActivity.this, "Select School first", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (et_grNo.getText().toString().isEmpty()) {
            Toast.makeText(CashReceiptActivity.this, "Enter Gr Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        String receiptText = et_receiptNo.getText().toString();
        if (receiptText.isEmpty() || receiptText.equals("0")) {
            Toast.makeText(CashReceiptActivity.this, "Please Enter Receipt No", Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (!et_receiptNo.getText().toString().isEmpty() && (areAllAmountsEmpty() && !areAllSalesEmpty())) {
//            Toast.makeText(CashReceiptActivity.this, "Receipt no Not Required for Sales", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//
//        if (!et_receiptNo.getText().toString().isEmpty() && onlyCopiesEntered()) {
//            Toast.makeText(CashReceiptActivity.this, "Receipt not required for copies", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        if (et_receiptNo.getText().toString().isEmpty() && (!areAllAmountsEmpty() && !areAllSalesEmpty())) {
//            Toast.makeText(CashReceiptActivity.this, "Please Enter Receipt No", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        //commented need the reason
//        if (et_receiptNo.getText().toString().isEmpty() && !onlyCopiesEntered()) {
//            Toast.makeText(CashReceiptActivity.this, "Please Enter Receipt No", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        for (ViewReceivablesModels model : viewReceivablesModels) {
            if (model.isBalanceNegative()) {
                Toast.makeText(CashReceiptActivity.this, "Advance not allowed for " + model.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (isTRExceeds5000()) {
            Toast.makeText(CashReceiptActivity.this, "Total received Amount should not be greater than 5000. ", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (ViewReceivablesModels models : viewReceivablesModels) {
            if (models.isAmountGreaterThan5000()) {
                Toast.makeText(CashReceiptActivity.this, "Received amount should not be greater than 5000. " +
                        models.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            } else if (models.isSalesGreaterThan5000()) {
                Toast.makeText(CashReceiptActivity.this, "Sales amount should not be greater than 5000. " +
                        models.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

//        if (!onlyCopiesEntered() && !areAllAmountsEmpty() && AppReceipt.getInstance(this).doesFeesReceiptExists(getFeesReceiptModel(), schoolId)) {
//            Toast.makeText(CashReceiptActivity.this, "Receipt number already exists. ", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        //created new
//        if (!onlyCopiesEntered() && !areAllAmountsEmpty() && !FeesCollection.getInstance(this).isFeesReceiptForSameStudent(getFeesReceipt(), schoolId)) {
//            Toast.makeText(CashReceiptActivity.this, "Receipt number already exists. ", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (FeesCollection.getInstance(this).doesFeesReceiptExists(getFeesReceipt(), schoolId)) {
            Toast.makeText(this, "Receipt No. already exists!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean onlyCopiesEntered() {
        boolean copyEntered = viewReceivablesModels.get(4).getAmountReceived() != null && !viewReceivablesModels.get(4).getAmountReceived().isEmpty();
        for (int i = 0; i < viewReceivablesModels.size(); i++) {
            ViewReceivablesModels model = viewReceivablesModels.get(i);
            if (model.getAmountReceived() != null && !model.getAmountReceived().isEmpty() && i != 4) {
                if (!model.getAmountReceived().matches("[0]+"))
                    return false;
            }
        }
        return copyEntered;
    }

    private void showDetails() {

        if (schoolId == 0) {
            Toast.makeText(CashReceiptActivity.this, "Please Select School", Toast.LENGTH_SHORT).show();

            clearStudentFields();
            visibilityGone();
            ll_content.setVisibility(View.GONE);

            return;
        }


        if (!et_grNo.getText().toString().isEmpty()) {
            int grNo = Integer.valueOf(et_grNo.getText().toString().trim());
            StudentModel sm = DatabaseHelper.getInstance(CashReceiptActivity.this).getStudentwithGR(grNo, schoolId);

            schoolClassId = sm.getSchoolClassId();
            studentId = sm.getId();

            if (sm.getName() != null && !sm.getName().equals("")) {
                populateStudentFields(sm);
                ll_content.setVisibility(View.VISIBLE);
                visibilityVisible();
//                populateList(grNo, schoolId);
                populateReceivableList(grNo, schoolId);
            } else {
                clearStudentFields();
                visibilityGone();
//                populateList();
                Toast.makeText(CashReceiptActivity.this, "Student Not found", Toast.LENGTH_SHORT).show();
                ll_content.setVisibility(View.GONE);
            }

        } else
            Toast.makeText(CashReceiptActivity.this, "Please enter GR No", Toast.LENGTH_SHORT).show();
    }


    private void getIntentData() {
        String grNo = getIntent().hasExtra("gr") ? getIntent().getStringExtra("gr") : "";
        et_grNo.setText(grNo);
        schoolId = getIntent().hasExtra("schoolId") ? getIntent().getIntExtra("schoolId", 0) : AppModel.getInstance().getSpinnerSelectedSchool(this);


        for (SchoolModel model : schoolModels) {
            if (schoolId == model.getId()) {
                sp_schools.setSelection(schoolModels.indexOf(model));
            }
        }

        if (!grNo.isEmpty() && schoolId != 0)
            iv_search.performClick();
    }

    private void populateSchoolSpinner() {

        sp_schools = (Spinner) screen.findViewById(R.id.sp_schools);

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForFinance();


        if (schoolModels.size() <= 1) {
//            schoolId = SurveyAppModel.getInstance().getSelectedSchool(this);
            sp_schools.setEnabled(false);
            sp_schools.setClickable(false); //            SchoolModel schoolModel=DatabaseHelper.getInstance(this).getSchoolById(schoolId);
//            if (schoolModel.getAllowedModule_App()!= null && schoolModel.getAllowedModule_App().equalsIgnoreCase("f")){
            try {
                schoolId = schoolModels.get(0).getId();
            } catch (Exception e) {
                schoolId = 0;
            }
            setTv_cashInHand();
//            }
//            else {
//                schoolId = 0;
//            }

        }

        if (schoolModels.size() > 1)
            schoolModels.add(0, new SchoolModel(0, getString(R.string.select_school)));


//        String[] schoolNames = new String[schoolModels.size() + 1];
//        schoolNames[0] = getResources().getString(R.string.select_school);
//        for (int i = 1; i < schoolModels.size() + 1; i++) {
//            schoolNames[i] = schoolModels.get(i - 1).getName();
//        }

        sp_schools.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                schoolModels));
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1 && schoolModels.size() > 1)
            sp_schools.setSelection(indexOfSelectedSchool);


        sp_schools.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                schoolId = ((SchoolModel) adapterView.getItemAtPosition(pos)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(CashReceiptActivity.this,
                        schoolId);
//                schoolId = schoolModels.get(pos).getId();

                schoolYearId = DatabaseHelper.getInstance(CashReceiptActivity.this).getSchoolYearId(schoolId);
                academicSession_id = DatabaseHelper.getInstance(CashReceiptActivity.this).getAcademicSessionId(schoolId);
                populateStudentAutocompleteAdapter();
                setTv_cashInHand();

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(CashReceiptActivity.this, schoolId);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void setTv_cashInHand() {
        AppModel.getInstance().setCashinHand(this,
                schoolId, null, tv_cashInHand, tv_cashInHandSchool, null);
//        String schoolIds = "";
//        if (schoolId == 0) {
//            for (int i = 0; i < schoolModels.size(); i++) {
//                if (i == schoolModels.size() - 1) {
//                    schoolIds += schoolModels.get(i).getId();
//                } else {
//                    schoolIds += schoolModels.get(i).getId() + ",";
//                }
//            }
//        } else {
//            schoolIds = String.valueOf(schoolId);
//        }
//        String cashInHand = FeesCollection.getInstance(CashReceiptActivity.this)
//                .getCashInHandData(schoolIds).getTotal();
////        String cashInHand = DatabaseHelper.getInstance(CashReceiptActivity.this)
////                .getCashInHandData(schoolId, "", "", "").getTotal();
//
//        if (cashInHand != null && cashInHand != "") {
//            Integer actualCash = (Integer.valueOf(cashInHand));
//            tv_cashInHand.setText(actualCash + "");
//        } else tv_cashInHand.setText("0");
    }

    private void populateReceivableList(int grNo, int schoolId) {
        viewReceivablesModels = new ArrayList<>();
        int amountRecv;
        int balance = 0;
        int sales;
        int prevRecv = 0;


        List<FeeTypeModel> feeTypeModelList = FeesCollection.getInstance(this).getFeeTypes();
        List<PreviousReceivableModel> prevRecvs = FeesCollection.getInstance(this).getPrevReceivableWithFeeType(schoolId + "", grNo + "");

        for (int i = 0; i < feeTypeModelList.size(); i++) {

            if (i < 7) {
                int count = 0;
                sales = 0;
                amountRecv = 0;

                for (int j = 0; j < prevRecvs.size(); j++) {
                    prevRecv = 0;
                    if (prevRecvs.get(j).getFeeType_id() == feeTypeModelList.get(i).getId()) {
                        if (prevRecvs.get(j).getTotalAmount() != null && prevRecvs.get(j).getTotalAmount() != "")
                            prevRecv = Integer.valueOf(prevRecvs.get(j).getTotalAmount());
                        else
                            prevRecv = 0;

                        balance = prevRecv + sales - amountRecv;
                        break;
                    }
                }

                ViewReceivablesModels rm = new ViewReceivablesModels();
                rm.setBalance(balance + "");
                rm.setPreviouslyReceived(prevRecv + "");
                rm.setAmountReceived("");
                rm.setTodaySales("");
                rm.setTitle("Fees Collection");

                viewReceivablesModels.add(rm);

            }
        }

        for (int i = 0; i < feeTypeModelList.size(); i++) {
            if (i < 7) {
                viewReceivablesModels.get(i).setTitle(feeTypeModelList.get(i).getName());
                viewReceivablesModels.get(i).setFeeTypeId(feeTypeModelList.get(i).getId());

                if (i < 3) {
                    viewReceivablesModels.get(i).setSalesDisabled(true);
                }
            }
        }
        rv_fees.setAdapter(new ReceiptAdapter(this, viewReceivablesModels, this));


    }

    private void populateList(int grNo, int schoolId) {
        viewReceivablesModels = new ArrayList<>();
        int amountRecv;
        int balance;
        int sales;
        int prevRecv;

        List<String> prevRecvs = AppInvoice.getInstance(this).getPrevRecv(schoolId + "", grNo + "");

        for (int i = 0; i < 7; i++) {

            sales = 0;
            amountRecv = 0;

            if (prevRecvs.get(i) != null && prevRecvs.get(i) != "")
                prevRecv = Integer.valueOf(prevRecvs.get(i));
            else
                prevRecv = 0;

            balance = prevRecv + sales - amountRecv;

            ViewReceivablesModels rm = new ViewReceivablesModels();
            rm.setBalance(balance + "");
            rm.setPreviouslyReceived(prevRecv + "");
            rm.setAmountReceived("");
            rm.setTodaySales("");
            rm.setTitle("Fees Collection");

            viewReceivablesModels.add(rm);
        }

        viewReceivablesModels.get(0).setTitle("Admission Fees");
        viewReceivablesModels.get(0).setFeeTypeId(1);
        viewReceivablesModels.get(0).setSalesDisabled(true);
        viewReceivablesModels.get(1).setTitle("Examination Fees");
        viewReceivablesModels.get(1).setFeeTypeId(2);
        viewReceivablesModels.get(1).setSalesDisabled(true);
        viewReceivablesModels.get(2).setTitle("Monthly Fees");
        viewReceivablesModels.get(2).setFeeTypeId(3);
        viewReceivablesModels.get(2).setSalesDisabled(true);

        viewReceivablesModels.get(3).setTitle("Books");
        viewReceivablesModels.get(3).setFeeTypeId(4);
        viewReceivablesModels.get(4).setTitle("Copies");
        viewReceivablesModels.get(4).setFeeTypeId(5);
        viewReceivablesModels.get(5).setTitle("Uniform");
        viewReceivablesModels.get(5).setFeeTypeId(6);
        viewReceivablesModels.get(6).setTitle("Others");
        viewReceivablesModels.get(6).setFeeTypeId(7);

        rv_fees.setAdapter(new ReceiptAdapter(this, viewReceivablesModels, this));


    }

    private boolean isTRExceeds5000() { // total received amount exceeds 5000
        String totalAmountReceive = et_amountRecv.getText().toString();
        if (!totalAmountReceive.isEmpty()) {
            if (Integer.valueOf(totalAmountReceive.replace(",", "")) > 5000)
                return true;
        }

        return false;
    }


    private void getTotal() {
        int totalamount = 0;
        int prerecv = 0;
        int totalSales = 0;
        int totalbalance = 0;

        for (ViewReceivablesModels rv : viewReceivablesModels) {
            if (!rv.getAmountReceived().isEmpty())
                totalamount += Integer.valueOf(rv.getAmountReceived());
            if (!rv.getBalance().isEmpty())
                totalbalance += Integer.valueOf(rv.getBalance());
            if (!rv.getPreviouslyReceived().isEmpty())
                prerecv += Integer.valueOf(rv.getPreviouslyReceived());
            if (!rv.getTodaySales().isEmpty())
                totalSales += Integer.valueOf(rv.getTodaySales());
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedTotalAmount = formatter.format(totalamount);
        String formattedTotalBalance = formatter.format(totalbalance);
        String formattedTotalPrerecv = formatter.format(prerecv);
        String formattedTotalTotalSales = formatter.format(totalSales);

        totalTodaySales = formattedTotalTotalSales;
        totalAmountRecv = formattedTotalAmount;

        et_amountRecv.setText(formattedTotalAmount + "");
        et_balance.setText(formattedTotalBalance + "");
        tv_prevRecv.setText(formattedTotalPrerecv + "");
        et_todaySales.setText(formattedTotalTotalSales + "");

    }

    @Override
    public void onDataSetChanged(List<ViewReceivablesModels> models) {
        if (models.size() > 0) {
            viewReceivablesModels = models;
            getTotal();
        }
    }

    @Override
    public void onGetStudenDetail(StudentModel studentModel) {
        clearStudentFields();
        visibilityGone();
        if (studentModel != null)
            populateStudentFields(studentModel);


    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void clearStudentFields() {
        tv_name.setText("");
        tv_sectionName.setText("");
        tv_fathersName.setText("");
        tv_className.setText("");
        tv_monthlyFee.setText("");
        tv_category.setText("");
        student_Img.setImageResource(R.mipmap.profile_pic);
    }

    private void populateStudentFields(StudentModel sm) {
        tv_name.setText(sm.getName());
        tv_fathersName.setText(sm.getFathersName());
        tv_className.setText(sm.getCurrentClass() + " " + sm.getCurrentSection());
        tv_studentId.setText(sm.getId() + "");
        tv_gr.setText(sm.getGrNo());
        tv_monthlyFee.setText((int) sm.getActualFees() + "");

        ScholarshipCategoryModel scholarshipCategory = Scholarship_Category.getInstance(CashReceiptActivity.this).getScholarshipCategory(
                schoolId,
                (int) sm.getActualFees());

        tv_category.setText(scholarshipCategory.getScholarship_category_description());

        try {
            student_Img.setImageBitmap(AppModel.getInstance().setImage("P", sm.getPictureName(), this, Integer.parseInt(sm.getGrNo()),false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void visibilityGone() {
        if (linearLayoutName.getVisibility() == View.VISIBLE)
            linearLayoutName.setVisibility(View.GONE);
        if (linearLayoutFName.getVisibility() == View.VISIBLE)
            linearLayoutFName.setVisibility(View.GONE);
        if (linearLayoutSectionAndClassName.getVisibility() == View.VISIBLE)
            linearLayoutSectionAndClassName.setVisibility(View.GONE);
        if (linearLayoutImage.getVisibility() == View.VISIBLE)
            linearLayoutImage.setVisibility(View.GONE);
        if (linearLayoutMonthlyFee.getVisibility() == View.VISIBLE)
            linearLayoutMonthlyFee.setVisibility(View.GONE);
        if (linearLayoutCategory.getVisibility() == View.VISIBLE)
            linearLayoutCategory.setVisibility(View.GONE);
        linearLayoutStudenID.setVisibility(View.GONE);
        linearLayoutGR.setVisibility(View.GONE);
    }

    private void visibilityVisible() {
        linearLayoutName.setVisibility(View.VISIBLE);
        linearLayoutFName.setVisibility(View.VISIBLE);
        linearLayoutSectionAndClassName.setVisibility(View.VISIBLE);
        linearLayoutMonthlyFee.setVisibility(View.VISIBLE);
        linearLayoutCategory.setVisibility(View.VISIBLE);
        linearLayoutImage.setVisibility(View.VISIBLE);
        linearLayoutStudenID.setVisibility(View.VISIBLE);
        linearLayoutGR.setVisibility(View.VISIBLE);
    }

    private void setSubmitButtonEnabled(boolean enabled){
        bt_ok.setEnabled(enabled);
        bt_ok.setClickable(enabled);
    }
}
