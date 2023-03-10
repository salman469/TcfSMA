package com.tcf.sma.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.Expense.AddNewRecordActivity;
import com.tcf.sma.Activities.NewDashboardActivity;
import com.tcf.sma.Adapters.Expense.TransactionListingAdapter;
import com.tcf.sma.Helpers.DbTables.Expense.ExpenseHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.Expense.TransactionListingModel;
import com.tcf.sma.Models.RetrofitModels.Expense.TransactionListingSearchModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashboardExpenseFragment extends Fragment  implements View.OnClickListener{

    View view;
    private int schoolId = 0;
    TransactionListingSearchModel listingSearchModel = new TransactionListingSearchModel();
    private TextView tv_available_pettycash, tv_spent_pettycash, tv_available_advance, tv_spent_advance, tv_available_salary,
            tv_spent_salary,txtWithdraw,txtDeposit,tv_cashinbank,tv_cashinhand;
    private CardView cv_pettycash, cv_salary, cv_advance;
    private RecyclerView rv_lasttransactions;
    private ArrayList<TransactionListingModel> transactionsList;
    private TransactionListingAdapter transactionListingAdapter;
    private double valueCashinhand = 0, valueCashinbank = 0;
    private List<Integer> pettycash_amt,salary_amt,advance_amt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard_expense, container, false);

        init(view);
        working();
        setValues();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            working();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(View view) {
        tv_cashinbank = view.findViewById(R.id.tv_cashinbank);
        tv_cashinhand = view.findViewById(R.id.tv_cashinhand);
        tv_available_pettycash = view.findViewById(R.id.txt_available_pettycash);
        tv_spent_pettycash = view.findViewById(R.id.txt_spent_pettycash);
        tv_available_advance = view.findViewById(R.id.txt_available_advance);
        tv_spent_advance = view.findViewById(R.id.txt_spent_advance);
        tv_available_salary = view.findViewById(R.id.txt_available_salary);
        tv_spent_salary = view.findViewById(R.id.txt_spent_salary);
        txtWithdraw = view.findViewById(R.id.txtWithdraw);
        txtDeposit = view.findViewById(R.id.txtDeposit);
        cv_pettycash = view.findViewById(R.id.cv_pettycash);
        cv_salary = view.findViewById(R.id.cv_salary);
        cv_advance = view.findViewById(R.id.cv_advance);
        rv_lasttransactions = view.findViewById(R.id.rv_lasttransactions);
        txtDeposit.setOnClickListener(this);
        txtWithdraw.setOnClickListener(this);
        cv_advance.setOnClickListener(this);
        cv_salary.setOnClickListener(this);
        cv_pettycash.setOnClickListener(this);
    }

    private void working() {
        schoolId = ((NewDashboardActivity) Objects.requireNonNull(getActivity())).schoolId;
        PopulateDataRecyclerview();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtDeposit:
                startActivity(new Intent(view.getContext(), AddNewRecordActivity.class).putExtra(AppConstants.KEY_TRANSACTION_TYPE,AppConstants.VALUE_TRANSACTION_DEPOSIT));
                break;
//            case R.id.bt_cashdepositcorrection:
//                startActivity(new Intent(view.getContext(), DepositCashInBank.class).putExtra("correction", "abc"));
//                break;
            case R.id.txtWithdraw:
                startActivity(new Intent(view.getContext(), AddNewRecordActivity.class).putExtra(AppConstants.KEY_TRANSACTION_TYPE,AppConstants.VALUE_TRANSACTION_WITHDRAW));
                break;
//            case R.id.bt_cashwithdrawalcorrection:
//                startActivity(new Intent(view.getContext(), WithdrawCashFromBank.class).putExtra("correction", "abc"));
//                break;
            case R.id.cv_pettycash:
                //startActivity(new Intent(view.getContext(), AddExpense.class).putExtra("expense_type","pcash"));
                startActivity(new Intent(view.getContext(), AddNewRecordActivity.class).putExtra(AppConstants.KEY_EXPENSE_TYPE,AppConstants.VALUE_EXPENSE_TYPE_PETTYCASH));
                break;
            case R.id.cv_salary:
                //startActivity(new Intent(view.getContext(),AddExpense.class).putExtra("expense_type","salary"));
                startActivity(new Intent(view.getContext(), AddNewRecordActivity.class).putExtra(AppConstants.KEY_EXPENSE_TYPE,AppConstants.VALUE_EXPENSE_TYPE_SALARY));
                break;
            case R.id.cv_advance:
                //startActivity(new Intent(view.getContext(),AddExpense.class).putExtra("expense_type","advance"));
                startActivity(new Intent(view.getContext(), AddNewRecordActivity.class).putExtra(AppConstants.KEY_EXPENSE_TYPE,AppConstants.VALUE_EXPENSE_TYPE_ADVANCE));
                break;
        }
    }

    private void PopulateDataRecyclerview() {
        listingSearchModel.setSchoolId(schoolId);
        transactionsList = ExpenseHelperClass.getInstance(getContext()).getFilteredTransactions(listingSearchModel, true);
        rv_lasttransactions.setNestedScrollingEnabled(false);
        rv_lasttransactions.setHasFixedSize(true);
        rv_lasttransactions.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        transactionListingAdapter = new TransactionListingAdapter(getContext(), transactionsList);
        rv_lasttransactions.setAdapter(transactionListingAdapter);
    }

    private void setValues(){
        //2 petty 3 salary 4 advance head_id
        pettycash_amt = ExpenseHelperClass.getInstance(getActivity()).getSpentandAvailable_amount(schoolId,2);
        salary_amt = ExpenseHelperClass.getInstance(getActivity()).getSpentandAvailable_amount(schoolId,3);
        advance_amt = ExpenseHelperClass.getInstance(getActivity()).getSpentandAvailable_amount(schoolId,4);

        tv_available_pettycash.setText(AppModel.getInstance().formatNumberInCommas(pettycash_amt.get(0)-pettycash_amt.get(1)));
        tv_spent_pettycash.setText(AppModel.getInstance().formatNumberInCommas(pettycash_amt.get(1)));

        tv_available_advance.setText(AppModel.getInstance().formatNumberInCommas(advance_amt.get(0)-pettycash_amt.get(1)));
        tv_spent_advance.setText(AppModel.getInstance().formatNumberInCommas(advance_amt.get(1)));

        tv_available_salary.setText(AppModel.getInstance().formatNumberInCommas(salary_amt.get(0)-pettycash_amt.get(1)));
        tv_spent_salary.setText(AppModel.getInstance().formatNumberInCommas(salary_amt.get(1)));

        //bucket id 2 for cash
        valueCashinhand = ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId,2);
        //bucket id 1 for bank
        valueCashinbank = ExpenseHelperClass.getInstance(getActivity()).getAvailableLimitAmountFromTransaction(schoolId,1);
        tv_cashinbank.setText(AppModel.getInstance().formatNumberInCommas((long) valueCashinbank));
        tv_cashinhand.setText(AppModel.getInstance().formatNumberInCommas((long) valueCashinhand));
     }
}
