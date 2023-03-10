package com.tcf.sma.Activities.FeesCollection.CashInHandAndDeposit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.FeeTypeModel;
import com.tcf.sma.Models.Fees_Collection.CashInHandFeeTypeModel;
import com.tcf.sma.Models.Fees_Collection.CashInHandModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.UserModel;
import com.tcf.sma.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By Mohammad Haseeb
 */
public class CashInHandActivity extends DrawerActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    View screen;
    CashInHandModel cashInHandModel;
    //Filter views
    private Button btnDepositInBank;
    private TextView tvBalanceAfterReConciliation, tvTodayDate,
            tvAdmissionFees, tvExamFees, tvMonthlyFees, tvBooks,
            tvCopies, tvUniform, tvOthers, tvTotal, tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = setActivityLayout(this, R.layout.activity_cash_in_hand);
        setToolbar("Cash In Hand", this, false);
        init();
//        populateSchoolSpinner();
        cashInHandModel = getCashInHandData();
        setUIFields();

    }


    public void init() {
        tvBalanceAfterReConciliation = (TextView) screen.findViewById(R.id.tvBlncAfterReConciliation);
        tvTodayDate = (TextView) screen.findViewById(R.id.tvTodayDate);
        tvAdmissionFees = (TextView) screen.findViewById(R.id.tvAdmissionFees);
        tvExamFees = (TextView) screen.findViewById(R.id.tvExamFees);
        tvMonthlyFees = (TextView) screen.findViewById(R.id.tvMonthlyFees);
        tvBooks = (TextView) screen.findViewById(R.id.tvBooks);
        tvCopies = (TextView) screen.findViewById(R.id.tvCopies);
        tvUniform = (TextView) screen.findViewById(R.id.tvUniform);
        tvOthers = (TextView) screen.findViewById(R.id.tvOthers);
        tvTotal = (TextView) screen.findViewById(R.id.tvTotal);
        tvError = (TextView) screen.findViewById(R.id.tvError);
//        et_grNo = (EditText) screen.findViewById(R.id.et_grNo);
//        etFromDate = (TextView) screen.findViewById(R.id.etFromDate);
//        etToDate = (TextView) screen.findViewById(R.id.etToDate);


        btnDepositInBank = (Button) screen.findViewById(R.id.btnDepositInBank);
        btnDepositInBank.setOnClickListener(this);

    }

    private void setUIFields() {
        if (cashInHandModel != null && (cashInHandModel.getTotal() != null
                && cashInHandModel.getAdmissionFees() != null
                && cashInHandModel.getExamFees() != null
                && cashInHandModel.getMonthlyFees() != null
                && cashInHandModel.getBooks() != null
                && cashInHandModel.getCopies() != null
                && cashInHandModel.getUniforms() != null
                && cashInHandModel.getOthers() != null)) {


            tvTodayDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MMM-yy"));

            //multiplying by -1 because the condition on the query is inverted. In order to avoid changes in query multiplying the
            //value shown in view by -1 to get the right sign with the result
            tvAdmissionFees.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getAdmissionFees())));
            tvExamFees.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getExamFees())));
            tvMonthlyFees.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getMonthlyFees())));
            tvBooks.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getBooks())));
            tvCopies.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getCopies())));
            tvUniform.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getUniforms())));
            tvOthers.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getOthers())));
            tvTotal.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getTotal())));

             if(Integer.parseInt(cashInHandModel.getTotal()) > 0) {
                 tvTotal.setTextColor(getResources().getColor(R.color.colorBlack));
                 btnDepositInBank.setVisibility(View.VISIBLE);
                 tvError.setVisibility(View.GONE);
             }
             else if(Integer.parseInt(cashInHandModel.getTotal()) < 0){
                 tvTotal.setTextColor(getResources().getColor(R.color.red));
                 btnDepositInBank.setVisibility(View.GONE);
                 tvError.setVisibility(View.VISIBLE);
                 tvError.setText("Cash In Hand is not correct please contact Finance HO");
             }else {
                 tvAdmissionFees.setText("0");
                 tvExamFees.setText("0");
                 tvMonthlyFees.setText("0");
                 tvBooks.setText("0");
                 tvCopies.setText("0");
                 tvUniform.setText("0");
                 tvOthers.setText("0");
                 tvTotal.setText("0");


                 tvTodayDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MMM-yy"));
                 btnDepositInBank.setVisibility(View.GONE);
                 tvTotal.setTextColor(getResources().getColor(R.color.colorBlack));
                 if (tvError.getVisibility() == View.VISIBLE)
                     tvError.setVisibility(View.GONE);
             }

        } else {

            tvAdmissionFees.setText("0");
            tvExamFees.setText("0");
            tvMonthlyFees.setText("0");
            tvBooks.setText("0");
            tvCopies.setText("0");
            tvUniform.setText("0");
            tvOthers.setText("0");
            tvTotal.setText("0");


            tvTodayDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MMM-yy"));
            btnDepositInBank.setVisibility(View.GONE);

        }
    }

    private CashInHandModel getCashInHandData() {
        UserModel userModel = DatabaseHelper.getInstance(CashInHandActivity.this).getCurrentLoggedInUser();
//        CashInHandModel cashInHandModel = DatabaseHelper.getInstance(this).getCashInHandData(userModel.getId());
//        CashInHandModel cashInHandModel = FeesCollection.getInstance(this).getCashInHandData(userModel.getId());
        CashInHandModel cashInHandModel = new CashInHandModel();
        ArrayList<SchoolModel> schoolModels = AppModel.getInstance().getSchoolsForLoggedInUser(this);
        String schoolIds = "";
        for (int i = 0; i < schoolModels.size(); i++) {
            if (i == schoolModels.size() - 1) {
                schoolIds += schoolModels.get(i).getId();
            } else {
                schoolIds += schoolModels.get(i).getId() + ",";
            }
        }

//        String cashInHand = FeesCollection.getInstance(this).getCashInHandData(schoolIds).getTotal();

        String cashInHand = String.valueOf(FeesCollection.getInstance(this).getCashInHand(0, DatabaseHelper.getInstance(this).getAcademicSessionId(schoolModels.get(0).getId())));

        List<CashInHandFeeTypeModel> cashInHandFeeTypeModels = FeesCollection.getInstance(this).getCashInHandDataFeeType(schoolIds);
        List<FeeTypeModel> feeTypeModelList = FeesCollection.getInstance(this).getFeeTypes();

        int total = 0;
        for (FeeTypeModel feeTypeModel : feeTypeModelList) {
            for (CashInHandFeeTypeModel cashInHandFeeTypeModel : cashInHandFeeTypeModels) {
                if (feeTypeModel.getId() == cashInHandFeeTypeModel.getFeeType_id()) {
                    if (feeTypeModel.getId() == 1)
                        cashInHandModel.setAdmissionFees(cashInHandFeeTypeModel.getAmount());
                    else if (feeTypeModel.getId() == 2)
                        cashInHandModel.setExamFees(cashInHandFeeTypeModel.getAmount());
                    else if (feeTypeModel.getId() == 3)
                        cashInHandModel.setMonthlyFees(cashInHandFeeTypeModel.getAmount());
                    else if (feeTypeModel.getId() == 4)
                        cashInHandModel.setBooks(cashInHandFeeTypeModel.getAmount());
                    else if (feeTypeModel.getId() == 5)
                        cashInHandModel.setCopies(cashInHandFeeTypeModel.getAmount());
                    else if (feeTypeModel.getId() == 6)
                        cashInHandModel.setUniforms(cashInHandFeeTypeModel.getAmount());
                    else if (feeTypeModel.getId() == 7)
                        cashInHandModel.setOthers(cashInHandFeeTypeModel.getAmount());

                    if (cashInHandFeeTypeModel.getAmount() != null && !cashInHandFeeTypeModel.getAmount().isEmpty())
                        total += Double.parseDouble(cashInHandFeeTypeModel.getAmount());
                    else
                        total += 0;

                    cashInHandModel.setLastDepositDate(cashInHandFeeTypeModel.getLastDepositDate());
                }
            }

            if (cashInHandModel.getAdmissionFees() == null)
                cashInHandModel.setAdmissionFees("0");
            if (cashInHandModel.getExamFees() == null)
                cashInHandModel.setExamFees("0");
            if (cashInHandModel.getMonthlyFees() == null)
                cashInHandModel.setMonthlyFees("0");
            if (cashInHandModel.getBooks() == null)
                cashInHandModel.setBooks("0");
            if (cashInHandModel.getCopies() == null)
                cashInHandModel.setCopies("0");
            if (cashInHandModel.getUniforms() == null)
                cashInHandModel.setUniforms("0");
            if (cashInHandModel.getOthers() == null)
                cashInHandModel.setOthers("0");

        }

//        int reconcileAmount = (Integer.valueOf(cashInHand) - total);
        cashInHandModel.setTotal(cashInHand);
//        cashInHandModel.setReconcileAmount(String.valueOf(reconcileAmount));


//        tvBalanceAfterReConciliation.setText("" + reconcileAmount);
        tvBalanceAfterReConciliation.setText("0");

        return cashInHandModel;
    }

//    private void populateSchoolSpinner() {
//        schoolId = SurveyAppModel.getInstance().getSelectedSchool(this);
//        SurveyAppModel.getInstance().showLoader(this);
//        schoolModels = DatabaseHelper.getInstance(this).getAllUserSchools();
////        ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
////        spnSchools.setAdapter(schoolSelectionAdapter);
////        int selectedSchoolIndex = getSelectedSchoolPosition(schoolModels, schoolId);
////        spnSchools.setSelection(selectedSchoolIndex);
//        SurveyAppModel.getInstance().hideLoader();
//
//    }

//    private int getSelectedSchoolPosition(List<SchoolModel> schoolModels, int appSchoolId) {
//        int index = 0;
//        for (SchoolModel model : schoolModels) {
//            if (model.getId() == appSchoolId)
//                return index;
//            index++;
//        }
//        return index;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDepositInBank:
//                CashInHandModel model = new CashInHandModel();
                String TodayDate = tvTodayDate.getText().toString().trim();
//                cashInHandModel.setLastDepositDate(SurveyAppModel.getInstance().convertDatetoFormat(LastDepositDate, "dd-MMM-yy", "dd-MM-yy"));
                cashInHandModel.setTodayDate(AppModel.getInstance().convertDatetoFormat(TodayDate, "dd-MMM-yy", "dd-MM-yy"));
//                model.setAdmissionFees(tvAdmissionFees.getText().toString().trim());
//                model.setExamFees(tvExamFees.getText().toString().trim());
//                model.setMonthlyFees(tvMonthlyFees.getText().toString().trim());
//                model.setBooks(tvBooks.getText().toString().trim());
//                model.setCopies(tvCopies.getText().toString().trim());
//                model.setUniforms(tvUniform.getText().toString().trim());
                goToCashDeposit(cashInHandModel);
                break;
            case R.id.iv_search:
                cashInHandModel = getCashInHandData();
                setUIFields();
                break;
        }
    }


    private void goToCashDeposit(CashInHandModel model) {
        try {
            if (validate()) {
                correctDepositModel();
                Intent intent = new Intent(CashInHandActivity.this, CashDepositActivity.class);
                intent.putExtra("CashInHand", model);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void correctDepositModel() {
        cashInHandModel.setAdmissionFees((Long.valueOf(cashInHandModel.getAdmissionFees())) + "");
        cashInHandModel.setCopies((Long.valueOf(cashInHandModel.getCopies())) + "");
        cashInHandModel.setBooks((Long.valueOf(cashInHandModel.getBooks())) + "");
        cashInHandModel.setUniforms((Long.valueOf(cashInHandModel.getUniforms())) + "");
        cashInHandModel.setExamFees((Long.valueOf(cashInHandModel.getExamFees())) + "");
        cashInHandModel.setMonthlyFees((Long.valueOf(cashInHandModel.getMonthlyFees())) + "");
        cashInHandModel.setTotal((Long.valueOf(cashInHandModel.getTotal())) + "");
        cashInHandModel.setOthers((Long.valueOf(cashInHandModel.getOthers())) + "");
    }

    private boolean validate() throws ParseException {
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate = dfDate.parse(cashInHandModel.getTodayDate());

        Date lastDate;
        if (cashInHandModel.getLastDepositDate() != null && cashInHandModel.getLastDepositDate() != "")
            lastDate = dfDate.parse(cashInHandModel.getLastDepositDate());
        else
            return true;

        if (currentDate.before(lastDate)) {

            Toast.makeText(this, "Current Date cant be smaller than the last deposit date..", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnSchools:
//                schoolId = ((SchoolModel) parent.getItemAtPosition(position)).getId();
//                cashInHandModel = getCashInHandData();
//                setUIFields();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}