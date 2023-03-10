package com.tcf.sma.Activities.FeesCollection.CashReceived;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
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
import com.tcf.sma.Activities.NewAdmissionActivity;
import com.tcf.sma.Adapters.FeesCollection.WaiverAdapter;
import com.tcf.sma.Adapters.StudentAutoCompleteAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AppInvoice;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Helpers.DbTables.FeesCollection.Scholarship_Category;
import com.tcf.sma.Managers.FeesCollection.GRDialogManager;
import com.tcf.sma.Models.AppConstants;
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
import java.util.ListIterator;

public class WavierActivity extends DrawerActivity implements WaiverAdapter.DatasetUpdateListener, GRDialogManager.StudentDetailInterface {

    RecyclerView rv_fees;
    List<ViewReceivablesModels> viewReceivablesModels = new ArrayList<>();
    TextView tv_date, tv_prevRecv, tv_name, tv_studentId, tv_gr, tv_className, tv_sectionName, tv_fathersName, et_amountRecv, et_balance, waiver_message;
    Spinner sp_schools;
    EditText et_receiptNo, remarks;
    View screen;
    ImageView student_Img;
    LinearLayout ll_content, linearLayoutName, linearLayoutStudenID, linearLayoutGR, linearLayoutFName, linearLayoutSectionAndClassName, linearLayoutImage;
    int schoolClassId = -1;
    int studentId = -1;
    String deviceId = "";
    String currentDate;
    int schoolId = 0, schoolYearId = 1, academicSession_id;
    String correctionType = "S";
    int prevrev = 0;
    ArrayList<SchoolModel> schoolModels = new ArrayList<>();
    Button bt_ok, bt_cancel;
    int transactionType = 3; //Wavier
    private AutoCompleteTextView et_std_name_grNo;
    private TextView tv_monthlyFee, tv_category;
    private LinearLayout linearLayoutMonthlyFee, linearLayoutCategory;
    private StudentAutoCompleteAdapter studentAdapter;
    private List<FeeTypeModel> feeTypeModelList;
    private int prerecv = 0;
    private Boolean isAnyBalanceNegative = false;
    private String stdGrNo = "", stdName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = setActivityLayout(this, R.layout.activity_amount_waiver);
        setToolbar("Waiver", this, false);
        init();

        populateStudentAutocompleteAdapter();

        et_std_name_grNo.setOnItemClickListener((adapterView, view, position, id) -> {
            String std_grno = ((StudentAutoCompleteModel) adapterView.getItemAtPosition(position)).getGrNo();
            String std_name = ((StudentAutoCompleteModel) adapterView.getItemAtPosition(position)).getStudent_name();

            et_std_name_grNo.setText(std_name);
            et_std_name_grNo.post(() -> et_std_name_grNo.setSelection(et_std_name_grNo.getText().length()));
            stdGrNo = std_grno;
            stdName = std_name;

            showDetails();
            AppModel.getInstance().hideSoftKeyboard(WavierActivity.this);

        });
    }

    void init() {
        waiver_message = screen.findViewById(R.id.zero_waiver_message);
        remarks = screen.findViewById(R.id.et_remarks);
        et_amountRecv = screen.findViewById(R.id.et_amountRecv);
        tv_prevRecv = screen.findViewById(R.id.tv_prevRecv);
        et_receiptNo = screen.findViewById(R.id.et_receiptNo);
        et_balance = screen.findViewById(R.id.et_amountBalance);
        et_std_name_grNo = screen.findViewById(R.id.et_std_name_grNo);
        tv_gr = screen.findViewById(R.id.tv_gr);
        tv_studentId = screen.findViewById(R.id.tv_student_id);
        tv_name = screen.findViewById(R.id.tv_name);
        ll_content = screen.findViewById(R.id.ll_content);
        tv_date = screen.findViewById(R.id.tv_date);
        student_Img = screen.findViewById(R.id.student_Img);
        linearLayoutName = screen.findViewById(R.id.linearLayoutName);
        linearLayoutFName = screen.findViewById(R.id.linearLayoutFName);
        linearLayoutSectionAndClassName = screen.findViewById(R.id.linearLayoutSectionAndClassName);
        tv_className = screen.findViewById(R.id.tv_className);
        tv_sectionName = screen.findViewById(R.id.tv_sectionName);
        linearLayoutImage = screen.findViewById(R.id.linearLayoutImage);
        tv_fathersName = screen.findViewById(R.id.tv_fathersName);
        linearLayoutStudenID = screen.findViewById(R.id.linearLayoutStudentId);
        linearLayoutGR = screen.findViewById(R.id.linearLayoutGR);

        linearLayoutMonthlyFee = screen.findViewById(R.id.linearLayoutMonthlyFee);
        linearLayoutCategory = screen.findViewById(R.id.linearLayoutCategory);

        tv_monthlyFee = screen.findViewById(R.id.tv_monthlyFee);
        tv_category = screen.findViewById(R.id.tv_category);

        deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        feeTypeModelList = FeesCollection.getInstance(this).getFeeTypes();

        et_std_name_grNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clearStudentFields();
                ll_content.setVisibility(View.GONE);
                visibilityGone();


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    stdGrNo = "";
                    stdName = "";
                }
            }
        });

        et_std_name_grNo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                showDetails();
                AppModel.getInstance().hideSoftKeyboard(WavierActivity.this);
                return true;
            }
            return false;
        });

//        iv_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDetails();
//            }
//        });


        rv_fees = screen.findViewById(R.id.rv_fees);
        rv_fees.setLayoutManager(new LinearLayoutManager(this));
//        rv_fees.setAdapter(new CorrectionAdapter(this, viewReceivablesModelsCorrection, CashCorrectionActivity.this, correctionType));
        rv_fees.setAdapter(new WaiverAdapter(this, viewReceivablesModels, WavierActivity.this, transactionType));
        rv_fees.setNestedScrollingEnabled(false);

        populateSchoolSpinner();


        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MMM-yy");
        Date date = Calendar.getInstance().getTime();
        currentDate = sdf1.format(date);
        tv_date.setText(sdf3.format(date));
//        tv_date.setText(sdf2.format(date));


        bt_ok = screen.findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(view -> {
            if (validate())
                submitReceipt();
        });

        bt_cancel = screen.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(view -> {
            if (prerecv != 0) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(WavierActivity.this);
                dialog.setMessage("Are you sure you want to cancel?");
                dialog.create().requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                    startActivity(new Intent(WavierActivity.this, WavierActivity.class));
                    finish();
                    dialogInterface.dismiss();
                });
                dialog.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                dialog.show();
            } else {
                startActivity(new Intent(WavierActivity.this, WavierActivity.class));
                finish();
            }
        });

        getTotal();


    }

    private void populateStudentAutocompleteAdapter() {
        final List<StudentAutoCompleteModel> studentsList = DatabaseHelper.getInstance(this).getAllStudentsForAutocompleteList(schoolId);
        studentAdapter = new StudentAutoCompleteAdapter(this, R.layout.student_autocomplete_view,
                studentsList);
        et_std_name_grNo.setAdapter(studentAdapter);
    }

    private void deleteModel456() {
        ListIterator<ViewReceivablesModels> iterator = viewReceivablesModels.listIterator();
        while (iterator.hasNext()) {
            ViewReceivablesModels model = iterator.next();
            if (model.getFeeTypeId() == 4 || model.getFeeTypeId() == 5 || model.getFeeTypeId() == 6 || model.getFeeTypeId() == 7) {
                iterator.remove();
            }
        }
    }

    private void toggleViewTypes(int tranType) {
        if (tranType == 3) {
//            viewReceivablesModelsCorrection.get(3).setTodaySales("");
//            viewReceivablesModelsCorrection.get(4).setTodaySales("");
//            viewReceivablesModelsCorrection.get(5).setTodaySales("");
//            viewReceivablesModelsCorrection.get(6).setTodaySales("");
//
//            viewReceivablesModelsCorrection.get(3).setSalesDisabled(true);
//            viewReceivablesModelsCorrection.get(4).setSalesDisabled(true);
//            viewReceivablesModelsCorrection.get(5).setSalesDisabled(true);
//            viewReceivablesModelsCorrection.get(6).setSalesDisabled(true);

            rv_fees.setAdapter(new WaiverAdapter(WavierActivity.this, viewReceivablesModels,
                    WavierActivity.this, transactionType));

        }
    }

    private void submitReceipt() {
        try {

            if (transactionType == 3) {
                if (!areAllAmountsEmpty())
                    if (isAnyBalanceNegative)
                        Toast.makeText(this, "Balance cannot be in negative!", Toast.LENGTH_SHORT).show();
                    else
                        showDialog("Are you sure you want to save changes?", -1, transactionType);
                else if (areAllAmountsEmpty())
                    Toast.makeText(this, "Please enter data", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void toggleViewTypes(String viewType) {
//        if (viewType.equals("S")) {
//
//            viewReceivablesModelsCorrection.get(3).setSalesDisabled(false);
//            viewReceivablesModelsCorrection.get(4).setSalesDisabled(false);
//            viewReceivablesModelsCorrection.get(5).setSalesDisabled(false);
//            viewReceivablesModelsCorrection.get(6).setSalesDisabled(false);
//
//            rv_fees.setAdapter(new CorrectionAdapter(CashCorrectionActivity.this, viewReceivablesModelsCorrection,
//                    CashCorrectionActivity.this, correctionType ));
//
//
//        } else if (viewType.equals("W")) {
//            viewReceivablesModelsCorrection.get(3).setTodaySales("");
//            viewReceivablesModelsCorrection.get(4).setTodaySales("");
//            viewReceivablesModelsCorrection.get(5).setTodaySales("");
//            viewReceivablesModelsCorrection.get(6).setTodaySales("");
//
//            viewReceivablesModelsCorrection.get(3).setSalesDisabled(true);
//            viewReceivablesModelsCorrection.get(4).setSalesDisabled(true);
//            viewReceivablesModelsCorrection.get(5).setSalesDisabled(true);
//            viewReceivablesModelsCorrection.get(6).setSalesDisabled(true);
//
//            rv_fees.setAdapter(new CorrectionAdapter(CashCorrectionActivity.this, viewReceivablesModelsCorrection,
//                    CashCorrectionActivity.this, correctionType));
//
//        }
//    }

//    private void submitReceipt() {
//        try {
//            long duesId = -1, id = -1;
//            if (correctionType.equals("S")) {
//
//                //insert amount received
//                if (!areAllAmountsEmpty())
//                    id = AppReceipt.getInstance(this).insertCorrection(getCorrectionModel());
//                //insert sales record
//                if (!areAllSalesEmpty())
//                    duesId = AppInvoice.getInstance(this).insertFeesReceipt(getCorrectionDuesModel());
//
//                if (areAllSalesEmpty() && areAllAmountsEmpty())
//                    Toast.makeText(this, "Please enter data", Toast.LENGTH_SHORT).show();
//
//                if (id > -1 && duesId > -1)
//                    showDialog("Receipt Correction Successful. Click ok to create a new correction");
//                else if (duesId == -1 && id > -1)
//                    showDialog("Receipt Correction Successful. Click ok to create a new correciton");
//
//                else if (duesId > -1 && id == -1)
//                    showDialog("Record Correction successful" + ". Click ok to create a new correction");
//
//            } else if (correctionType.equals("W")) {
//                id = AppInvoice.getInstance(this).insertWavier(getWavierModel());
//
//                if (id > -1)
//                    showDialog("Record Correction successful" + ". Click ok to create a new correction");
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private FeesDuesModel getCorrectionDuesModel() {
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

        model.setDevice_id(deviceId);
        model.setCreated_by(String.valueOf(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId()));
        model.setStudent_id(studentId);
        model.setSchool_year_id(schoolYearId);
        model.setCreated_on(currentDate);
        model.setSchoolclass_id(schoolClassId);
        model.setCreated_from(correctionType);

        return model;
    }

    private FeesHeaderModel getInvoiceForCorrection() {
        FeesHeaderModel model = new FeesHeaderModel();
        double totalAmount = 0.0;

        //Category 1 for invoice
        model.setCategory_id(1);
        model.setTransactionType_id(2);

        for (int i = 0; i < viewReceivablesModels.size(); i++) {
            if (viewReceivablesModels.get(i).getTodaySales() != null && viewReceivablesModels.get(i).getTodaySales() != "") {
                totalAmount += Double.valueOf(viewReceivablesModels.get(i).getTodaySales());
            }
        }
        model.setTotal_amount(totalAmount);

        model.setDeviceId(deviceId);
        model.setCreatedBy(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
        model.setStudentId(studentId);
        model.setSchoolYearId(schoolYearId);
        model.setAcademicSession_id(academicSession_id);
        model.setCreatedOn(currentDate);
        model.setSchoolClassId(schoolClassId);

//        model.setCreated_from(correctionType);

        return model;
    }

    private FeesDuesModel getWavierModel() {
        FeesDuesModel model = new FeesDuesModel();

        String fees_admission = viewReceivablesModels.get(0).getAmountReceived();
        String fees_exam = viewReceivablesModels.get(1).getAmountReceived();
        String fees_tution = viewReceivablesModels.get(2).getAmountReceived();
        String fees_copies = viewReceivablesModels.get(4).getAmountReceived();
        String fees_books = viewReceivablesModels.get(3).getAmountReceived();
        String fees_uniform = viewReceivablesModels.get(5).getAmountReceived();
        String fees_others = viewReceivablesModels.get(6).getAmountReceived();


        if (fees_admission != null && fees_admission != "")
            model.setFees_admission(Double.valueOf(fees_admission));


        if (fees_exam != null && fees_exam != "")
            model.setFees_exam(Double.valueOf(fees_exam));

        if (fees_tution != null && fees_tution != "")
            model.setFees_tution(Double.valueOf(fees_tution));

        if (fees_copies != null && fees_copies != "")
            model.setFees_copies(Double.valueOf(fees_copies));

        if (fees_books != null && fees_books != "")
            model.setFees_books(Double.valueOf(fees_books));

        if (fees_uniform != null && fees_uniform != "")
            model.setFees_uniform(Double.valueOf(fees_uniform));

        if (fees_others != null && fees_others != "")
            model.setFees_other(Double.valueOf(fees_others));

        model.setDevice_id(deviceId);
        model.setSchool_year_id(schoolYearId);
        model.setCreated_by(String.valueOf(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId()));
        model.setStudent_id(studentId);
        model.setCreated_on(currentDate);
        model.setSchoolclass_id(schoolClassId);
        model.setCreated_from(correctionType);

        return model;
    }

    private FeesHeaderModel getWavier() {
        FeesHeaderModel model = new FeesHeaderModel();
        double totalAmount = 0.0;

        for (int i = 0; i < viewReceivablesModels.size(); i++) {
            if (viewReceivablesModels.get(i).getAmountReceived() != null && !viewReceivablesModels.get(i).getAmountReceived().equals("")) {
                totalAmount += Double.valueOf(viewReceivablesModels.get(i).getAmountReceived());
            }
        }
        //sending total amount as minus
        model.setTotal_amount(-1 * totalAmount);

        //Category 1 for Invoice
        model.setCategory_id(1);
        model.setTransactionType_id(3);
        model.setDeviceId(deviceId);
        model.setSchoolYearId(schoolYearId);
        model.setAcademicSession_id(academicSession_id);
        model.setCreatedBy(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
        model.setStudentId(studentId);
        model.setFor_date(currentDate);
        model.setCreatedOn(currentDate);
        model.setSchoolClassId(schoolClassId);
        model.setRemarks(remarks.getText().toString());

//        model.setCreated_from(correctionType);

        return model;
    }

    private FeesReceiptModel getCorrectionModel() {
        FeesReceiptModel model = new FeesReceiptModel();

        String fees_admission = viewReceivablesModels.get(0).getAmountReceived();
        String fees_exam = viewReceivablesModels.get(1).getAmountReceived();
        String fees_tution = viewReceivablesModels.get(2).getAmountReceived();
        String fees_book = viewReceivablesModels.get(3).getAmountReceived();
        String fees_copies = viewReceivablesModels.get(4).getAmountReceived();
        String fees_uniform = viewReceivablesModels.get(5).getAmountReceived();
        String fees_others = viewReceivablesModels.get(6).getAmountReceived();


        if (fees_admission != null && fees_admission != "")
            model.setAdmissionFees((Integer.valueOf(fees_admission)) + "");
        if (fees_exam != null && fees_exam != "")
            model.setExamFees((Integer.valueOf(fees_exam)) + "");
        if (fees_copies != null && fees_copies != "")
            model.setCopyFees((Integer.valueOf(fees_copies)) + "");
        if (fees_book != null && fees_book != "")
            model.setBookFees((Integer.valueOf(fees_book)) + "");
        if (fees_uniform != null && fees_uniform != "")
            model.setUniformFees((Integer.valueOf(fees_uniform)) + "");
        if (fees_tution != null && fees_tution != "")
            model.setTutionFees((Integer.valueOf(fees_tution)) + "");
        if (fees_others != null && fees_others != "")
            model.setOthersFees((Integer.valueOf(fees_others)) + "");

        model.setStudentId(studentId);
        model.setCreatedOn(currentDate);
        model.setSchoolYearId(schoolYearId);
        model.setSchoolClassId(schoolClassId);
        model.setCreatedBy(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
        model.setDeviceId(deviceId);
        model.setCorrectionType("1");
        model.setReceiptNumber(-2);
        return model;
    }

    private FeesHeaderModel getReceiptForCorrection() {

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
//        model.setCorrectionType("0");
        model.setCreatedBy(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
        model.setDeviceId(deviceId);
        model.setTransactionType_id(2);
        model.setReceiptNumber(-2);

        for (int i = 0; i < viewReceivablesModels.size(); i++) {
            if (viewReceivablesModels.get(i).getAmountReceived() != null && !viewReceivablesModels.get(i).getAmountReceived().equals("")) {
                totalAmount += Double.valueOf(viewReceivablesModels.get(i).getAmountReceived());

            }
        }
        model.setTotal_amount(totalAmount);
        return model;
    }


    private boolean validate() {

        if (stdGrNo.isEmpty()) {
            Toast.makeText(WavierActivity.this, "Enter Gr Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean areAllAmountsEmpty() {
        for (ViewReceivablesModels model : viewReceivablesModels) {
            if (model.getAmountReceived() != null && !model.getAmountReceived().isEmpty() && model.getAmountReceived() != null && !model.getAmountReceived().equals("0"))
                return false;
        }
        return true;
    }

    private boolean areAllSalesEmpty() {
        for (ViewReceivablesModels model : viewReceivablesModels) {
            if (model.getTodaySales() != null && !model.getTodaySales().isEmpty())
                return false;
        }
        return true;
    }

    private void showDialog(String message) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.create().requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(WavierActivity.this, WavierActivity.class));
                finish();
                dialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    private void showDialog(String message, final int action, final int transactionTypeId) { // action 0 for only receipt, 1 for only sales and 2 for both
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.create().requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                long duesId = -1, id = -1;
                if (transactionTypeId == 2) {
                    switch (action) {
                        case 0:
//                    id = AppReceipt.getInstance(this).insertCorrection(getCorrectionModel());
                            //insert amount received
                            id = FeesCollection.getInstance(WavierActivity.this).insertWavier(getReceiptForCorrection());
                            if (id > 0) {
                                FeesCollection.getInstance(WavierActivity.this).insertFeesDetails(id, viewReceivablesModels, 2);
                            }
                            break;
                        case 1:
//                    duesId = AppInvoice.getInstance(this).insertFeesReceipt(getCorrectionDuesModel());
                            duesId = FeesCollection.getInstance(WavierActivity.this).insertWavier(getInvoiceForCorrection());
                            if (duesId > 0) {
                                FeesCollection.getInstance(WavierActivity.this).insertFeesDetails(duesId, viewReceivablesModels, 1);
                            }
                            break;
                        case 2:
                            //insert sales record
//                    duesId = AppInvoice.getInstance(this).insertFeesReceipt(getCorrectionDuesModel());
                            duesId = FeesCollection.getInstance(WavierActivity.this).insertWavier(getInvoiceForCorrection());
                            if (duesId > 0) {
                                FeesCollection.getInstance(WavierActivity.this).insertFeesDetails(duesId, viewReceivablesModels, 1);
                            }
//                    id = AppReceipt.getInstance(this).insertCorrection(getCorrectionModel());
                            //insert amount received
                            id = FeesCollection.getInstance(WavierActivity.this).insertWavier(getReceiptForCorrection());
                            if (id > 0) {
                                FeesCollection.getInstance(WavierActivity.this).insertFeesDetails(id, viewReceivablesModels, 2);
                            }
                            break;
                    }
                } else if (transactionTypeId == 3) {
//                id = AppInvoice.getInstance(this).insertWavier(getWavierModel());
                    id = FeesCollection.getInstance(WavierActivity.this).insertWavier(getWavier());
                    if (id > 0) {
                        for (ViewReceivablesModels models : viewReceivablesModels) {
                            if (models.getAmountReceived() != null && models.getAmountReceived().isEmpty())
                                continue;
                            double amount = (-1 * Double.parseDouble(models.getAmountReceived()));
                            models.setAmountReceived(String.valueOf(amount));
                        }
                        FeesCollection.getInstance(WavierActivity.this).insertFeesDetails(id, viewReceivablesModels, 2);
                    }
                }

                if (id > -1 && duesId > -1)
                    Toast.makeText(WavierActivity.this, "Receipt Correction Successfully", Toast.LENGTH_SHORT).show();
                else if (duesId == -1 && id > -1)
                    Toast.makeText(WavierActivity.this, "Receipt Correction Successfully", Toast.LENGTH_SHORT).show();
                else if (duesId > -1 && id == -1)
                    Toast.makeText(WavierActivity.this, "Receipt Correction Successfully", Toast.LENGTH_SHORT).show();

                //Important when any change in table call this method
                AppModel.getInstance().changeMenuPendingSyncCount(WavierActivity.this, true);

                startActivity(new Intent(WavierActivity.this, WavierActivity.class));
                finish();
                dialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }


    private void showDetails() {

        if (!stdGrNo.isEmpty()) {
            int grNo = Integer.valueOf(stdGrNo.trim());
            StudentModel sm = DatabaseHelper.getInstance(WavierActivity.this).getStudentwithGR(grNo, schoolId);

            schoolClassId = sm.getSchoolClassId();
            studentId = sm.getId();
            prevrev = 0;
            if (sm.getName() != null && !sm.getName().equals("")) {

                populateStudentFields(sm);
                ll_content.setVisibility(View.VISIBLE);
                visibilityVisible();
//                populateList(schoolId, grNo);
                populateReceivableList(schoolId, grNo);
//                if (prevrev == 0) {
//                    Toast.makeText(WavierActivity.this, "Amount is not received yet for this student.", Toast.LENGTH_SHORT).show();
//                    ll_content.setVisibility(View.GONE);
//                }
            } else {
                clearStudentFields();
                visibilityGone();
                Toast.makeText(WavierActivity.this, "Student Not found", Toast.LENGTH_SHORT).show();
                ll_content.setVisibility(View.GONE);
            }

        } else
            Toast.makeText(WavierActivity.this, "Please enter GR No", Toast.LENGTH_SHORT).show();
    }

    private void populateSchoolSpinner() {

        sp_schools = screen.findViewById(R.id.sp_schools);

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForFinance();

        sp_schools.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                schoolModels));
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1)
            sp_schools.setSelection(indexOfSelectedSchool);

        sp_schools.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                schoolId = ((SchoolModel) adapterView.getItemAtPosition(pos)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(WavierActivity.this,
                        schoolId);
//                schoolId = schoolModels.get(pos).getId();
                schoolYearId = DatabaseHelper.getInstance(WavierActivity.this).getSchoolYearId(schoolId);
                academicSession_id = DatabaseHelper.getInstance(WavierActivity.this).getAcademicSessionId(schoolId);
                populateStudentAutocompleteAdapter();

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(WavierActivity.this, schoolId);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }

//                String cashInHand = FeesCollection.getInstance(WavierActivity.this)
//                        .getCashInHandData(schoolId).getTotal();
//
//                if (cashInHand != null && cashInHand != "") {
//                    int actualCash = (Integer.valueOf(cashInHand));
////                    .setText(actualCash + "");
//                    et_grNo.setBackground(getResources().getDrawable(R.drawable.textview_border_black));
//                    et_grNo.setEnabled(true);
//
//                } else {
//                    et_grNo.setBackground(getResources().getDrawable(R.drawable.textview_border_gray));
//                    et_grNo.setEnabled(false);
//                    Toast.makeText(WavierActivity.this, "Cash is already deposited to bank. You cant make a waiver", Toast.LENGTH_SHORT).show();
//                    ll_content.setVisibility(View.GONE);
////                    tv_cashInHand.setText("0");
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void populateList(int schoolId, int grNo) {
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

            this.prevrev += prevRecv;

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
        viewReceivablesModels.get(0).setSalesDisabled(true);
        viewReceivablesModels.get(1).setTitle("Examination Fees");
        viewReceivablesModels.get(1).setSalesDisabled(true);
        viewReceivablesModels.get(2).setTitle("Monthly Fees");
        viewReceivablesModels.get(2).setSalesDisabled(true);

        viewReceivablesModels.get(3).setTitle("Books");
        viewReceivablesModels.get(4).setTitle("Copies");
        viewReceivablesModels.get(5).setTitle("Uniform");
        viewReceivablesModels.get(6).setTitle("Others");

        rv_fees.setAdapter(new WaiverAdapter(this, viewReceivablesModels, this, correctionType));


    }

    private void populateReceivableList(int schoolId, int grNo) {
        viewReceivablesModels = new ArrayList<>();
        int amountRecv;
        int balance = 0;
        int sales;
        int prevRecv = 0;

//        List<String> prevRecvs = FeesCollection.getInstance(this).getPrevReceivable(schoolId + "", grNo + "");
        List<PreviousReceivableModel> prevRecvs = FeesCollection.getInstance(this).getPrevReceivableWithFeeType(schoolId + "", grNo + "");

        for (int i = 0; i < feeTypeModelList.size(); i++) {

            if (i < 7) {
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

                this.prevrev += prevRecv;

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
//        rv_fees.setAdapter(new CorrectionAdapter(this, viewReceivablesModelsCorrection, this, transactionType));

        if (viewReceivablesModels.size() > 0) {
            deleteModel456();
            transactionType = 3; //For Wavier
            toggleViewTypes(transactionType);
            Log.d("Check", "Wavier");
        }
    }


    private void getTotal() {
        int totalamount = 0;
        prerecv = 0;
        int totalbalance = 0;
        isAnyBalanceNegative = false;


        for (ViewReceivablesModels rv : viewReceivablesModels) {
            if (!rv.getAmountReceived().isEmpty())
                totalamount += Integer.valueOf(rv.getAmountReceived());
            if (!rv.getBalance().isEmpty()) {
                totalbalance += Integer.valueOf(rv.getBalance());
                if (Integer.valueOf(rv.getBalance()) < 0) {
                    isAnyBalanceNegative = true;
                }
            }
            if (!rv.getPreviouslyReceived().isEmpty())
                prerecv += Integer.valueOf(rv.getPreviouslyReceived());
        }


        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedTotalAmount = formatter.format(totalamount);
        String formattedTotalBalance = formatter.format(totalbalance);
        String formattedTotalPrerecv = formatter.format(prerecv);


        et_amountRecv.setText(formattedTotalAmount + "");
        et_balance.setText(formattedTotalBalance + "");
        tv_prevRecv.setText(formattedTotalPrerecv + "");

        if (prerecv == 0) {
            waiver_message.setVisibility(View.VISIBLE);
            bt_ok.setVisibility(View.GONE);
            bt_cancel.setText("Back");
        } else {
            waiver_message.setVisibility(View.GONE);
            bt_ok.setVisibility(View.VISIBLE);
            bt_cancel.setText("Cancel");
        }
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
        if (studentModel != null)
            populateStudentFields(studentModel);
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

        ScholarshipCategoryModel scholarshipCategory = Scholarship_Category.getInstance(WavierActivity.this).getScholarshipCategory(
                schoolId,
                (int) sm.getActualFees());

        tv_category.setText(scholarshipCategory.getScholarship_category_description());

        try {
            student_Img.setImageBitmap(AppModel.getInstance()
                    .setImage("P", sm.getPictureName(), this, Integer.parseInt(sm.getGrNo()),false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void visibilityGone() {
        linearLayoutName.setVisibility(View.GONE);
        linearLayoutFName.setVisibility(View.GONE);
        linearLayoutSectionAndClassName.setVisibility(View.GONE);
        linearLayoutImage.setVisibility(View.GONE);
        linearLayoutStudenID.setVisibility(View.GONE);
        linearLayoutGR.setVisibility(View.GONE);
        linearLayoutMonthlyFee.setVisibility(View.GONE);
        linearLayoutCategory.setVisibility(View.GONE);
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
}
