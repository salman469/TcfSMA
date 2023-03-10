package com.tcf.sma.Activities.FeesCollection.CashReceived;

import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.tcf.sma.Adapters.FeesCollection.CorrectionAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Helpers.DbTables.FeesCollection.Scholarship_Category;
import com.tcf.sma.Managers.FeesCollection.GRDialogManager;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.ErrorModel;
import com.tcf.sma.Models.FeeTypeModel;
import com.tcf.sma.Models.Fees_Collection.FeesHeaderModel;
import com.tcf.sma.Models.Fees_Collection.PreviousReceivableModel;
import com.tcf.sma.Models.Fees_Collection.ViewReceivablesCorrectionModel;
import com.tcf.sma.Models.Fees_Collection.ViewReceivablesModels;
import com.tcf.sma.Models.ScholarshipCategoryModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CashCorrectionActivity extends DrawerActivity implements CorrectionAdapter.DatasetUpdateListener, GRDialogManager.StudentDetailInterface {

    RecyclerView rv_fees;
    List<ViewReceivablesCorrectionModel> viewReceivablesModelsCorrection = new ArrayList<>();
    List<ViewReceivablesModels> viewReceivablesModels = new ArrayList<>();
    TextView tv_cashInHand, tv_cashInHandSchool, tv_date, tv_OldSale, tv_name, tv_className, tv_sectionName, tv_fathersName, tv_OldReceived, tv_NewSale, tv_NewReceived, tv_studentId, tv_gr;
    Spinner sp_schools;
    EditText et_receiptNo, et_remarks;
    View screen;
    //    RadioButton rb_correction, rb_wavier;
    ImageView iv_search, student_Img;
    //    RadioGroup rg_correction;
    LinearLayout ll_content, linearLayoutStudenID, linearLayoutGR, linearLayoutName, linearLayoutFName, linearLayoutSectionAndClassName, linearLayoutImage;
    int schoolClassId = -1;
    int studentId = -1;
    String deviceId = "";
    String currentDate;
    int schoolId = 0, schoolYearId = 1, academicSession_id;
    String correctionType = "S";
    int prevrev = 0;
    ArrayList<SchoolModel> schoolModels = new ArrayList<>();
    Button bt_ok, bt_cancel;
    int transactionType = 2; //By default Correction
    private List<FeeTypeModel> feeTypeModelList;
    private long receiptNo;
    private TextView tv_monthlyFee, tv_category;
    private LinearLayout linearLayoutMonthlyFee, linearLayoutCategory;
    private boolean isSchoolSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = setActivityLayout(this, R.layout.activity_amount_correction);
        setToolbar("Receipt Correction", this, false);
        init();

    }

    void init() {
        tv_OldSale = screen.findViewById(R.id.tv_oldSale);
        tv_OldReceived = screen.findViewById(R.id.tv_oldReceived);
        tv_NewSale = screen.findViewById(R.id.tv_newSale);
        tv_NewReceived = screen.findViewById(R.id.tv_newReceived);
        et_receiptNo = screen.findViewById(R.id.et_receiptNo);
        et_remarks = screen.findViewById(R.id.et_remarks);
        tv_gr = screen.findViewById(R.id.tv_gr);
        tv_studentId = screen.findViewById(R.id.tv_student_id);
        tv_cashInHand = screen.findViewById(R.id.tv_cashInHand);
        tv_cashInHandSchool = screen.findViewById(R.id.tv_cashInHand_2);
        tv_name = screen.findViewById(R.id.tv_name);
//        rb_correction = (RadioButton) screen.findViewById(R.id.rb_correction);
//        rb_wavier = (RadioButton) screen.findViewById(R.id.rb_wavier);
//        rg_correction = (RadioGroup) screen.findViewById(R.id.rg_correction);
        iv_search = screen.findViewById(R.id.iv_search);
        ll_content = screen.findViewById(R.id.ll_content);
        tv_date = screen.findViewById(R.id.tv_date);
        student_Img = screen.findViewById(R.id.student_Img);
        linearLayoutName = screen.findViewById(R.id.linearLayoutName);
        linearLayoutFName = screen.findViewById(R.id.linearLayoutFName);
        linearLayoutSectionAndClassName = screen.findViewById(R.id.linearLayoutSectionAndClassName);
        tv_className = screen.findViewById(R.id.tv_className);
        tv_sectionName = screen.findViewById(R.id.tv_sectionName);
        linearLayoutImage = screen.findViewById(R.id.linearLayoutImage);
        linearLayoutStudenID = screen.findViewById(R.id.linearLayoutStudentId);
        linearLayoutGR = screen.findViewById(R.id.linearLayoutGR);
        tv_fathersName = screen.findViewById(R.id.tv_fathersName);

        linearLayoutMonthlyFee = screen.findViewById(R.id.linearLayoutMonthlyFee);
        linearLayoutCategory = screen.findViewById(R.id.linearLayoutCategory);

        tv_monthlyFee = screen.findViewById(R.id.tv_monthlyFee);
        tv_category = screen.findViewById(R.id.tv_category);

        deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        feeTypeModelList = FeesCollection.getInstance(this).getFeeTypes();

        et_receiptNo.addTextChangedListener(new TextWatcher() {
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

            }
        });

        et_receiptNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    showDetails();

                    AppModel.getInstance().hideSoftKeyboard(CashCorrectionActivity.this);
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


        rv_fees = screen.findViewById(R.id.rv_fees);
        rv_fees.setLayoutManager(new LinearLayoutManager(this));
//        rv_fees.setAdapter(new CorrectionAdapter(this, viewReceivablesModelsCorrection, CashCorrectionActivity.this, correctionType));

        rv_fees.setAdapter(new CorrectionAdapter(this, viewReceivablesModelsCorrection, viewReceivablesModels, CashCorrectionActivity.this, transactionType));
        rv_fees.setNestedScrollingEnabled(false);


//        rg_correction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                switch (i) {
//                    case R.id.rb_correction:
//                        transactionType = 2; //For Correction
////                        correctionType = "S";
//                        toggleViewTypes(transactionType);
//                        Log.d("Check", "Correciton");
//                        break;
//
//                    case R.id.rb_wavier:
//                        transactionType = 3; //For Wavier
////                        correctionType = "W";
//                        toggleViewTypes(transactionType);
//                        Log.d("Check", "Wavier");
//                        break;
//
//                }
//            }
//        });

        populateSchoolSpinner();


        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MMM-yy");
        Date date = Calendar.getInstance().getTime();
        currentDate = sdf1.format(date);
        tv_date.setText(sdf3.format(date));
//        tv_date.setText(sdf2.format(date));


        bt_ok = screen.findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("submit","Count");
                if (validate())
                    submitReceipt();
            }
        });

        bt_cancel = screen.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(CashCorrectionActivity.this);
                dialog.setMessage("Are you sure you want to cancel?");
                dialog.create().requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(CashCorrectionActivity.this, CashCorrectionActivity.class));
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

        tv_NewReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                AppModel.getInstance().setCashinHand(CashCorrectionActivity.this,
                        schoolId, tv_OldReceived.getText().toString(), tv_cashInHand, tv_cashInHandSchool, s.toString());
            }
        });

        if (viewReceivablesModelsCorrection.size() > 0) {
            transactionType = 2; //For Correction
//      correctionType = "S";
            toggleViewTypes(transactionType);
            Log.d("Check", "Correciton");
        }

        if (getIntent().hasExtra("receipt_no")) {
            et_receiptNo.setText(getIntent().getStringExtra("receipt_no"));
            showDetails();
        }

    }


    private void toggleViewTypes(int tranType) {
        if (tranType == 2) {

            rv_fees.setAdapter(new CorrectionAdapter(CashCorrectionActivity.this, viewReceivablesModelsCorrection, viewReceivablesModels,
                    CashCorrectionActivity.this, transactionType));


        } else if (tranType == 3) {

            rv_fees.setAdapter(new CorrectionAdapter(CashCorrectionActivity.this, viewReceivablesModelsCorrection,
                    viewReceivablesModels, CashCorrectionActivity.this, transactionType));

        }
    }

    private void submitReceipt() {
        try {
            if (transactionType == 2) {

                String message = "";

                if (alreadyCorrectionPresent(schoolId, receiptNo)){
                    message = "Are you sure you want to update changes?";
                } else {
                    message = "Are you sure you want to save changes?";
                }

                if (areAllSalesEmpty() && areAllAmountsEmpty())
                    Toast.makeText(this, "Please enter data", Toast.LENGTH_SHORT).show();
                else if (!areAllAmountsEmpty() && !areAllSalesEmpty()) {
                    setSubmitButtonEnabled(false);
                    showDialog(message, 2, transactionType);
                } else if (!areAllAmountsEmpty() && areAllSalesEmpty()) {
                    setSubmitButtonEnabled(false);
                    showDialog(message, 0, transactionType);
                } else if (areAllAmountsEmpty() && !areAllSalesEmpty()) {
                    setSubmitButtonEnabled(false);
                    showDialog(message, 1, transactionType);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FeesHeaderModel getInvoiceForCorrection() {
        FeesHeaderModel model = new FeesHeaderModel();
        double totalAmount = 0.0;

        //Category 1 for invoice
        model.setCategory_id(1);
        model.setTransactionType_id(2);

        for (int i = 0; i < viewReceivablesModelsCorrection.size(); i++) {
            if (viewReceivablesModelsCorrection.get(i).getNewSales() != null && viewReceivablesModelsCorrection.get(i).getNewSales() != "") {
                totalAmount += Double.valueOf(viewReceivablesModelsCorrection.get(i).getNewSales());
            }
        }
        model.setTotal_amount(totalAmount);

        model.setDeviceId(deviceId);
        model.setCreatedBy(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
        model.setStudentId(studentId);
        model.setFor_date(currentDate);
//        model.setFor_date(viewReceivablesModelsCorrection.get(0).getForDate());
        model.setSchoolYearId(schoolYearId);
        model.setReceiptNumber(receiptNo);
        model.setAcademicSession_id(academicSession_id);
        model.setCreatedOn(currentDate);
        model.setSchoolClassId(schoolClassId);
        model.setRemarks(et_remarks.getText().toString());

//        model.setCreated_from(correctionType);

        return model;
    }


    private FeesHeaderModel getInvoiceForCorrectionOld() {
        FeesHeaderModel model = new FeesHeaderModel();
        double totalAmount = 0.0;

        //Category 1 for invoice
        model.setCategory_id(1);
        model.setTransactionType_id(2);

        for (int i = 0; i < viewReceivablesModelsCorrection.size(); i++) {
            if (viewReceivablesModelsCorrection.get(i).getOldSale() != null && viewReceivablesModelsCorrection.get(i).getOldSale() != "") {
                totalAmount += Double.valueOf(viewReceivablesModelsCorrection.get(i).getOldSale());
            }
        }
        if (totalAmount == 0)
            return null;

        model.setTotal_amount(-1 * totalAmount);
        model.setDeviceId(deviceId);
        model.setCreatedBy(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
        model.setStudentId(studentId);
        model.setFor_date(currentDate);
//        model.setFor_date(viewReceivablesModelsCorrection.get(0).getForDate());
        model.setReceiptNumber(receiptNo);
        model.setSchoolYearId(schoolYearId);
        model.setAcademicSession_id(academicSession_id);
        model.setCreatedOn(currentDate);
        model.setSchoolClassId(schoolClassId);

//        model.setCreated_from(correctionType);

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
        model.setCreatedBy(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
        model.setDeviceId(deviceId);
//        model.setFor_date(viewReceivablesModelsCorrection.get(0).getForDate());
        model.setTransactionType_id(2);
        model.setReceiptNumber(receiptNo);
        model.setRemarks(et_remarks.getText().toString());

        for (int i = 0; i < viewReceivablesModelsCorrection.size(); i++) {
            if (viewReceivablesModelsCorrection.get(i).getNewReceived() != null && !viewReceivablesModelsCorrection.get(i).getNewReceived().equals("")) {
                totalAmount += Double.valueOf(viewReceivablesModelsCorrection.get(i).getNewReceived());

            }
        }
        model.setTotal_amount(totalAmount);
        return model;
    }

    private FeesHeaderModel getReceiptForCorrectionOld() {

        double totalAmount = 0.0;
        FeesHeaderModel model = new FeesHeaderModel();

        //Category 2 for receipt
        model.setCategory_id(2);
        model.setFor_date(currentDate);
//        model.setFor_date(viewReceivablesModelsCorrection.get(0).getForDate());
        model.setSchoolClassId(schoolClassId);
        model.setStudentId(studentId);
        model.setCreatedOn(currentDate);
        model.setSchoolYearId(schoolYearId);
        model.setAcademicSession_id(academicSession_id);
        model.setCreatedBy(DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getId());
        model.setDeviceId(deviceId);
        model.setTransactionType_id(2);
        model.setReceiptNumber(receiptNo);

        for (int i = 0; i < viewReceivablesModelsCorrection.size(); i++) {
            if (viewReceivablesModelsCorrection.get(i).getOldRecieved() != null && !viewReceivablesModelsCorrection.get(i).getOldRecieved().equals("")) {
                totalAmount += Double.valueOf(viewReceivablesModelsCorrection.get(i).getOldRecieved());
            }
        }
        if (totalAmount == 0)
            return null;

        model.setTotal_amount(-1 * totalAmount);
        return model;
    }


    private boolean validate() {

        if (checkIfOldAndNewAreEqual()) {
            Toast.makeText(this, "There isnt any change in the previous and new data!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (schoolId == 0) {
            Toast.makeText(CashCorrectionActivity.this, "Select School first", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (et_receiptNo.getText().toString().isEmpty()) {
            Toast.makeText(CashCorrectionActivity.this, "Enter Receipt Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (ViewReceivablesCorrectionModel model : viewReceivablesModelsCorrection) {
            if (model.isBalanceNegative()) {
                Toast.makeText(CashCorrectionActivity.this, "Advance not allowed for " + model.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }


        if (isTRExceeds5000()) {
            Toast.makeText(CashCorrectionActivity.this, "Total received Amount should not be greater than 5000. ", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (ViewReceivablesModels models : viewReceivablesModels) {
            if (models.isAmountGreaterThan5000()) {
                Toast.makeText(CashCorrectionActivity.this, "Received amount should not be greater than 5000. " +
                        models.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            } else if (models.isSalesGreaterThan5000()) {
                Toast.makeText(CashCorrectionActivity.this, "Sales amount should not be greater than 5000. " +
                        models.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private boolean isTRExceeds5000() { // total received amount exceeds 5000
        String totalAmountReceive = tv_NewReceived.getText().toString();
        if (!totalAmountReceive.isEmpty()) {
            return Integer.valueOf(totalAmountReceive.replace(",", "")) > 5000;
        }

        return false;
    }

    private boolean areAllAmountsEmpty() {
        for (ViewReceivablesCorrectionModel model : viewReceivablesModelsCorrection) {
            if (model.getNewReceived() != null && !model.getNewReceived().isEmpty())
                return false;
        }
        return true;
    }

    private boolean areAllSalesEmpty() {
        for (ViewReceivablesCorrectionModel model : viewReceivablesModelsCorrection) {
            if (model.getNewSales() != null && !model.getNewSales().isEmpty())
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
                startActivity(new Intent(CashCorrectionActivity.this, CashCorrectionActivity.class));
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

    private boolean checkIfOldAndNewAreEqual() {
        int i = 0;
        for (ViewReceivablesCorrectionModel model : viewReceivablesModelsCorrection) {
            if (!model.getOldRecieved().equals(model.getNewReceived())) {
                return false;
            }
        }
        return true;
    }

    private boolean insertOldRecordAsMinus() {
        long oldInvoiceId = -1, oldReceiptId = -1;
        //Receipt
        FeesHeaderModel FHmodel = getReceiptForCorrectionOld();
        if (FHmodel != null && FHmodel.getTotal_amount() != 0) {
            oldReceiptId = FeesCollection.getInstance(this).insertCorrection(getReceiptForCorrectionOld());
            if (oldReceiptId > 0) {
                FeesCollection.getInstance(CashCorrectionActivity.this).insertFeesDetailsCorrectionOld(oldReceiptId, viewReceivablesModelsCorrection, 2);
            }
        }
        //Invoice
        FHmodel = getInvoiceForCorrectionOld();
        if (FHmodel != null && FHmodel.getTotal_amount() != 0) {
            if (oldReceiptId > 0) {
                FHmodel.setReceipt_id(oldReceiptId);
            }
            oldInvoiceId = FeesCollection.getInstance(CashCorrectionActivity.this).insertCorrection(FHmodel);
            if (oldInvoiceId > 0) {
                FeesCollection.getInstance(CashCorrectionActivity.this).insertFeesDetailsCorrectionOld(oldInvoiceId, viewReceivablesModelsCorrection, 1);
            }
        }
        return oldInvoiceId > 0 || oldReceiptId > 0;
    }

    private void showDialog(String message, final int action, final int transactionTypeId) { // action 0 for only receipt, 1 for only sales and 2 for both
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.create().requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setPositiveButton("Ok", (dialogInterface, i) -> {
            setSubmitButtonEnabled(true);
            long duesId = -1, id = -1;
            switch (action) {
                case 0:
//                    id = AppReceipt.getInstance(this).insertCorrection(getCorrectionModel());

                    if (alreadyCorrectionPresent(schoolId, receiptNo)){
                        //update amount received
                        int fhIdForReceipt = FeesCollection.getInstance(this).getFeeHeaderIdForUpdate(receiptNo,schoolId,2);
                        if (fhIdForReceipt > 0){
                            id = FeesCollection.getInstance(this).updateCorrection(fhIdForReceipt, getReceiptForCorrection());
                            if (id > 0) {
                                //update fee details if not present then it will insert new record
                                FeesCollection.getInstance(this).updateFeesDetailsCorrection(fhIdForReceipt, viewReceivablesModelsCorrection, 2);
                            }
                        }
                    }else {
                        insertOldRecordAsMinus();

                        //insert amount received
                        id = FeesCollection.getInstance(CashCorrectionActivity.this).insertCorrection(getReceiptForCorrection());
                        if (id > 0) {
                            FeesCollection.getInstance(CashCorrectionActivity.this).insertFeesDetailsCorrection(id, viewReceivablesModelsCorrection, 2);
                        }
                    }
                    break;
                case 1:
//                    duesId = AppInvoice.getInstance(this).insertFeesReceipt(getCorrectionDuesModel());

                    if (alreadyCorrectionPresent(schoolId, receiptNo)){
                        //update sales record
                        int fhIdForInvoice = FeesCollection.getInstance(this).getFeeHeaderIdForUpdate(receiptNo,schoolId,1);
                        if (fhIdForInvoice > 0) {
                            duesId = FeesCollection.getInstance(this).updateCorrection(fhIdForInvoice, getInvoiceForCorrection());
                            if (duesId > 0) {
                                //update fee details if not present then it will insert new record
                                FeesCollection.getInstance(this).updateFeesDetailsCorrection(fhIdForInvoice, viewReceivablesModelsCorrection, 1);
                            }
                        }
                    }else {
                        insertOldRecordAsMinus();

                        //insert sales record
                        duesId = FeesCollection.getInstance(CashCorrectionActivity.this).insertCorrection(getInvoiceForCorrection());
                        if (duesId > 0) {
                            FeesCollection.getInstance(CashCorrectionActivity.this).insertFeesDetailsCorrection(duesId, viewReceivablesModelsCorrection, 1);
                        }
                    }
                    break;
                case 2:
                    if (alreadyCorrectionPresent(schoolId, receiptNo)){
                        //update amount received
                        int fhIdForReceipt = FeesCollection.getInstance(this).getFeeHeaderIdForUpdate(receiptNo,schoolId,2);
                        if (fhIdForReceipt > 0){
                            id = FeesCollection.getInstance(this).updateCorrection(fhIdForReceipt, getReceiptForCorrection());
                            if (id > 0) {
                                //update fee details if not present then it will insert new record
                                FeesCollection.getInstance(this).updateFeesDetailsCorrection(fhIdForReceipt, viewReceivablesModelsCorrection, 2);
                            }
                        }

                        //update sales record
                        int fhIdForInvoice = FeesCollection.getInstance(this).getFeeHeaderIdForUpdate(receiptNo,schoolId,1);
                        if (fhIdForInvoice > 0) {
                            int fhSysIdForReceipt = FeesCollection.getInstance(this).getFeeHeaderSysIdForUpdate(receiptNo,schoolId,2);
                            FeesHeaderModel feesHeaderModel = getInvoiceForCorrection();
                            feesHeaderModel.setReceipt_id(fhSysIdForReceipt);
                            duesId = FeesCollection.getInstance(this).updateCorrection(fhIdForInvoice, feesHeaderModel);
                            if (duesId > 0) {
                                //update fee details if not present then it will insert new record
                                FeesCollection.getInstance(this).updateFeesDetailsCorrection(fhIdForInvoice, viewReceivablesModelsCorrection, 1);
                            }
                        }

                    }else {
                        insertOldRecordAsMinus();

                        //insert amount received
                        id = FeesCollection.getInstance(CashCorrectionActivity.this).insertCorrection(getReceiptForCorrection());
                        if (id > 0) {
                            FeesCollection.getInstance(CashCorrectionActivity.this).insertFeesDetailsCorrection(id, viewReceivablesModelsCorrection, 2);
                        }

                        //update sales record
                        FeesHeaderModel feesHeaderModel = getInvoiceForCorrection();
                        feesHeaderModel.setReceipt_id(id);
                        duesId = FeesCollection.getInstance(CashCorrectionActivity.this).insertCorrection(feesHeaderModel);
                        if (duesId > 0) {
                            FeesCollection.getInstance(CashCorrectionActivity.this).insertFeesDetailsCorrection(duesId, viewReceivablesModelsCorrection, 1);
                        }
                    }
                    break;
            }
            if (id > -1 && duesId > -1)
                Toast.makeText(CashCorrectionActivity.this, "Receipt Correction Successfully", Toast.LENGTH_SHORT).show();
            else if (duesId == -1 && id > -1)
                Toast.makeText(CashCorrectionActivity.this, "Receipt Correction Successfully", Toast.LENGTH_SHORT).show();
            else if (duesId > -1 && id == -1)
                Toast.makeText(CashCorrectionActivity.this, "Receipt Correction Successfully", Toast.LENGTH_SHORT).show();


            //Important when any change in table call this method
            AppModel.getInstance().changeMenuPendingSyncCount(CashCorrectionActivity.this, true);

            startActivity(new Intent(CashCorrectionActivity.this, CashCorrectionActivity.class));
            finish();
            dialogInterface.dismiss();

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


    private void showDetails() {

        if (!et_receiptNo.getText().toString().isEmpty()) {
            receiptNo = Long.parseLong(et_receiptNo.getText().toString().trim());
            StudentModel sm = DatabaseHelper.getInstance(CashCorrectionActivity.this).getStudentWithReceiptNo(receiptNo, schoolId);

            schoolClassId = sm.getSchoolClassId();
            studentId = sm.getId();
            prevrev = 0;
            if (sm.getName() != null && !sm.getName().equals("")) {

                populateStudentFields(sm);
                ll_content.setVisibility(View.VISIBLE);
                visibilityVisible();
//                populateList(schoolId, grNo);
                int cashDepositId = FeesCollection.getInstance(this).isReceiptDeposited(schoolId, receiptNo);
                if (cashDepositId == 0) {
                    populateReceivableList(Integer.valueOf(sm.getGrNo()), schoolId);
                    populateReceivableListforCorrection(schoolId, receiptNo);

                } else {
                    String message = "Cannot edit Receipt No:" + receiptNo + " because it's already deposited (Deposit slip no.:" + cashDepositId + ") in Bank.";

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(message);
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            et_receiptNo.setText("");
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }

//                if (prevrev == 0) {
//                    Toast.makeText(CashCorrectionActivity.this, "Amount is not received yet for this student.", Toast.LENGTH_SHORT).show();
//                    ll_content.setVisibility(View.GONE);
//                }
            } else {
                clearStudentFields();
                visibilityGone();
                Toast.makeText(CashCorrectionActivity.this, "Receipt not found!", Toast.LENGTH_SHORT).show();
                ll_content.setVisibility(View.GONE);
            }

        } else
            Toast.makeText(CashCorrectionActivity.this, "Please enter Receipt No", Toast.LENGTH_SHORT).show();
    }

    private boolean alreadyCorrectionPresent(int schoolId, long receiptNo) {
        return FeesCollection.getInstance(this).isFeeCorrectionAlreadyExists(schoolId,receiptNo);
    }

    private void populateSchoolSpinner() {

        sp_schools = screen.findViewById(R.id.sp_schools);

        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchoolsForFinance();

        sp_schools.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                schoolModels));
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1)
            sp_schools.setSelection(indexOfSelectedSchool);

        schoolId = ((SchoolModel) sp_schools.getSelectedItem()).getId();

        sp_schools.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                schoolId = ((SchoolModel) adapterView.getItemAtPosition(pos)).getId();
                AppModel.getInstance().setSpinnerSelectedSchool(CashCorrectionActivity.this,
                        schoolId);
                schoolYearId = DatabaseHelper.getInstance(CashCorrectionActivity.this).getSchoolYearId(schoolId);
                academicSession_id = DatabaseHelper.getInstance(CashCorrectionActivity.this).getAcademicSessionId(schoolId);
                AppModel.getInstance().setCashinHand(CashCorrectionActivity.this, schoolId, tv_OldReceived.getText().toString(), tv_cashInHand, tv_cashInHandSchool, null);
                if (isSchoolSelected) {
                    clearStudentFields();
                    ll_content.setVisibility(View.GONE);
                    visibilityGone();
//                else {
//                    et_receiptNo.setBackground(getResources().getDrawable(R.drawable.textview_border_gray));
//                    et_receiptNo.setEnabled(false);
//                    Toast.makeText(CashCorrectionActivity.this, "Cash is already deposited to bank. You cant make a correction", Toast.LENGTH_SHORT).show();
//                    ll_content.setVisibility(View.GONE);
//                    tv_cashInHand.setText("0");
//                }
                } else {
                    isSchoolSelected = true;
                }

                ErrorModel errorModel = AppModel.getInstance().checkSchoolClassDataDownloaded(CashCorrectionActivity.this, schoolId);
                if (errorModel != null){
                    MessageBox(errorModel.getMessage());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


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
    }

    private void populateReceivableListforCorrection(int schoolId, long ReceiptNo) {
        viewReceivablesModelsCorrection = new ArrayList<>();
        int amountRecv;
        int balance = 0;
        int sales;
        int prevRecv = 0;
//        List<String> prevRecvs = FeesCollection.getInstance(this).getPrevReceivable(schoolId + "", grNo + "");
        List<PreviousReceivableModel> prevRecvs = FeesCollection.getInstance(this).getPrevReceivableWithReceiptNO(schoolId + "", ReceiptNo + "");
        for (int i = 0; i < feeTypeModelList.size(); i++) {

            if (i < 7) {
                sales = 0;
                amountRecv = 0;
                balance = 0;
                int feeType = feeTypeModelList.get(i).getId();

                for (int j = 0; j < prevRecvs.size(); j++) {
                    prevRecv = 0;
                    if (prevRecvs.get(j).getFeeType_id() == feeTypeModelList.get(i).getId()) {
                        if (prevRecvs.get(j).getTotalAmount() != null && !prevRecvs.get(j).getTotalAmount().equals("") && !prevRecvs.get(j).getTotalAmount().equals("0")) {
                            prevRecv = Integer.valueOf(prevRecvs.get(j).getTotalAmount());
                        } else {
                            prevRecv = 0;
                        }

                        balance = prevRecv + sales - amountRecv;
                        break;
                    }
                }

                this.prevrev += prevRecv;
                ViewReceivablesCorrectionModel rm = new ViewReceivablesCorrectionModel();
                if (feeType >= 1 && feeType <= 3) {
                    rm.setOldSale("");
                    rm.setOldRecieved(String.valueOf(balance));
                } else {
                    rm.setOldSale(String.valueOf(balance));
                    rm.setOldRecieved(String.valueOf(balance));
                }
                rm.setNewReceived(String.valueOf(balance));
//                rm.setForDate(prevRecvs.get(0).getForDate());
                rm.setNewSales("");
                rm.setNewReceived("");
                rm.setTitle("Fees Collection");

                viewReceivablesModelsCorrection.add(rm);
            }
        }

        for (int i = 0; i < feeTypeModelList.size(); i++) {
            if (i < 7) {
                viewReceivablesModelsCorrection.get(i).setTitle(feeTypeModelList.get(i).getName());
                viewReceivablesModelsCorrection.get(i).setFeeTypeId(feeTypeModelList.get(i).getId());

//                if (i < 3) {
//                    viewReceivablesModelsCorrection.get(i).setSalesDisabled(true);
//                }
            }
        }
//        rv_fees.setAdapter(new CorrectionAdapter(this, viewReceivablesModelsCorrection, this, transactionType));

        if (viewReceivablesModelsCorrection.size() > 0) {

            transactionType = 2; //For Correction
//      correctionType = "S";
            toggleViewTypes(transactionType);
            Log.d("Check", "Correciton");
        }

    }

    private void getTotal() {
        int totalNewRecieved = 0;
        int oldRecieved = 0;
        int oldSales = 0;
        int totalNewSales = 0;


        for (ViewReceivablesCorrectionModel rv : viewReceivablesModelsCorrection) {
            if (!rv.getNewReceived().isEmpty())
                totalNewRecieved += Integer.valueOf(rv.getNewReceived());

            if (!rv.getNewSales().isEmpty())
                totalNewSales += Integer.valueOf(rv.getNewSales());

            if (!rv.getOldRecieved().isEmpty())
                oldRecieved += Integer.valueOf(rv.getOldRecieved());

            if (!rv.getOldSale().isEmpty())
                oldSales += Integer.valueOf(rv.getOldSale());
        }


        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedTotalNewRecievedAmount = formatter.format(totalNewRecieved);
        String formattedTotalNewSaleAmount = formatter.format(totalNewSales);
        String formattedTotalOldRecievedAmount = formatter.format(oldRecieved);
        String formattedTotalOldSaleAmount = formatter.format(oldSales);


        tv_OldSale.setText(formattedTotalOldSaleAmount + "");
        tv_OldReceived.setText(formattedTotalOldRecievedAmount + "");
        tv_NewSale.setText(formattedTotalNewSaleAmount + "");
        tv_NewReceived.setText(formattedTotalNewRecievedAmount + "");

//        if (prerecv == 0) {
//            ll_content.setVisibility(View.GONE);
//        } else
//            ll_content.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDataSetChanged(List<ViewReceivablesCorrectionModel> models) {
        if (models.size() > 0) {
            viewReceivablesModelsCorrection = models;
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

        ScholarshipCategoryModel scholarshipCategory = Scholarship_Category.getInstance(CashCorrectionActivity.this).getScholarshipCategory(
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
        linearLayoutImage.setVisibility(View.VISIBLE);
        linearLayoutStudenID.setVisibility(View.VISIBLE);
        linearLayoutGR.setVisibility(View.VISIBLE);
        linearLayoutMonthlyFee.setVisibility(View.VISIBLE);
        linearLayoutCategory.setVisibility(View.VISIBLE);
    }

    private void setSubmitButtonEnabled(boolean enabled){
        bt_ok.setEnabled(enabled);
        bt_ok.setClickable(enabled);
    }
}
