package com.tcf.sma.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.FeesCollection.AccountStatement.AccountStatementNewActivity;
import com.tcf.sma.Adapters.FeesCollection.AccountStatementSchoolAdapter;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementModelNewStructure;
import com.tcf.sma.Models.Fees_Collection.AccountStatementSchoolModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountStatementSchoolFragment extends Fragment implements View.OnClickListener {

    String toDate, fromDate;
    private View layoutView;
    private TextView etFromDate, etToDate, tv_title, tvNoDataFound;
    private LinearLayout ll_title, ll_depositAndReceiptNo;
    private RecyclerView rvAccountStatement;
    private EditText et_receiptNo, et_depSlipNo;
    private ImageView iv_search_depositNo_and_receiptNo, iv_search;
    private int schoolId = 0;
    private List<AccountStatementSchoolModel> modelNewStructureList;
    private List<AccountStatementModel> asmList;
    private AccountStatementSchoolAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_school_account_statement, container, false);
        init(layoutView);
        working(layoutView);
        return layoutView;
    }

    private void init(View view) {
        iv_search = view.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);
        etToDate = view.findViewById(R.id.etToDate);
        etToDate.setOnClickListener(this);
        etToDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MMM-yy"));
        etFromDate = view.findViewById(R.id.etFromDate);
        etFromDate.setText(AppModel.getInstance().getLastWeekFromCurrentDate("dd-MMM-yy"));
        etFromDate.setOnClickListener(this);
        ll_title = view.findViewById(R.id.ll_title);
        tvNoDataFound = view.findViewById(R.id.tvNoDataFound);
        tv_title = view.findViewById(R.id.tv_title);
        rvAccountStatement = view.findViewById(R.id.rvAccountStatement);
        rvAccountStatement.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvAccountStatement.setNestedScrollingEnabled(false);

        et_depSlipNo = view.findViewById(R.id.et_depSlipNo);
        et_receiptNo = view.findViewById(R.id.et_receiptNo);
        ll_depositAndReceiptNo = view.findViewById(R.id.ll_depositAndReceiptNo);

        iv_search_depositNo_and_receiptNo = view.findViewById(R.id.iv_search_depositNo_and_receiptNo);

    }

    private void working(View view) {
        toDate = etToDate.getText().toString().trim();
        fromDate = etFromDate.getText().toString().trim();

        if (!fromDate.isEmpty())
            fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
        if (!toDate.isEmpty())
            toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");

        schoolId = ((AccountStatementNewActivity) Objects.requireNonNull(getActivity())).schoolId;
        if (schoolId > 0) {
            setAdapter(getAccountStatement(fromDate, toDate));
        }
    }

    private List<AccountStatementSchoolModel> getAccountStatement(String fromDate, String toDate) {
//        unionModels = AppInvoice.getInstance(this).getAccountStatement(schoolId, grNo, fromDate, toDate, depSlipNo, receiptNo);
        modelNewStructureList = FeesCollection.getInstance(layoutView.getContext()).getAccountStatementForSchool(schoolId, fromDate, toDate);
        asmList = new ArrayList<>();
        if (modelNewStructureList != null && modelNewStructureList.size() > 0) {
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> ll_title.setVisibility(View.VISIBLE));

            int i = 0;

//            for (AccountStatementSchoolModel model : modelNewStructureList) {
//                if (model.getTransaction_typeId() == 2 && model.getTransaction_categoryId() == 2) // 2 = correction 2 = Receipt
//                    checkFeesProperties(model, 1);  // -1 should be 1
//                else //normal receipt
//                    checkFeesProperties(model, -1);  // 1 should be -1
//            }
            double TotalAmount = 0;
            double PreviousYearAmount = 0;
            double TotalBalance = 0;
            for (AccountStatementModel model : asmList) {
                String balance;
                if (i == 0) {
                    // + (invoiceamount) - (feesreceipt);
                    TotalAmount += Double.valueOf(model.getAmount());
                    balance = model.getAmount();
                    model.setBalance(balance);
                } else {
                    TotalAmount += Double.valueOf(model.getAmount());
                    balance = String.valueOf(Double.valueOf(asmList.get(i - 1).getBalance()) + Double.valueOf(model.getAmount()));
                    model.setBalance(balance);
                }
                i++;
            }
            TotalBalance = TotalAmount - PreviousYearAmount;
            asmList.add(0, new AccountStatementModel(-1, "", "", "", "", "", "", "", "", ""));
            asmList.add(new AccountStatementModel(-4, "", "", "", AppModel.getInstance().getCurrentDateTime("dd-MM-yy"), "", "This Academic year", String.valueOf(TotalAmount), asmList.get(asmList.size() - 1).getBalance(), 0, ""));
            asmList.add(new AccountStatementModel(-3, "", "", "", AppModel.getInstance().getCurrentDateTime("dd-MM-yy"), "", "Previous Academic years", String.valueOf(PreviousYearAmount), asmList.get(asmList.size() - 1).getBalance(), 0, ""));
            asmList.add(new AccountStatementModel(-2, "", "", "", AppModel.getInstance().getCurrentDateTime("dd-MM-yy"), "", "Total Balance", String.valueOf(TotalBalance), asmList.get(asmList.size() - 1).getBalance(), 0, ""));

        }
        return modelNewStructureList;
    }


    private void setAdapter(final List<AccountStatementSchoolModel> asmList) {
        if (asmList.size() > 0) {
            adapter = new AccountStatementSchoolAdapter(getActivity(), asmList);

            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rvAccountStatement.setAdapter(adapter);
                    rvAccountStatement.setVisibility(View.VISIBLE);
//                    if (grNo == null || grNo.isEmpty()) {
//                        ll_depositAndReceiptNo.setVisibility(View.GONE);
//                    } else if (grNo != null) {
//                        ll_depositAndReceiptNo.setVisibility(View.VISIBLE);
//                    }
                }
            });
        } else {
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                rvAccountStatement.setVisibility(View.GONE);
//                ll_depositAndReceiptNo.setVisibility(View.GONE);
            });
        }
    }

    private void checkFeesProperties(AccountStatementModelNewStructure model, int value) {
        /** If type is invoice **/
        if (model.getTransaction_categoryId() == 1) {
            if (model.getFees_admission() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_admission());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.admission_fees) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));

            }
            if (model.getFees_books() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_books());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.books) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (model.getFees_copies() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_copies());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.copies) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (model.getFees_exam() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_exam());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.exam_fees) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (model.getFees_tution() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_tution());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.monthly_fees) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (model.getFees_uniform() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_uniform());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.uniform) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (model.getFees_others() > 0) {
                ArrayList<String> data = generateJvNoAndDescForInvoice(model, model.getFees_others());
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), "", "", model.getCreated_on(), "", getString(R.string.others) + data.get(1), data.get(2), model.getTransaction_categoryId(), model.getGrNo()));
            }
        }
        /** If type is Receipt **/
        else if (model.getTransaction_categoryId() == 2) {
            if (Math.abs(model.getFees_admission()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.admission_fees) + data.get(1), String.valueOf(model.getFees_admission() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_books()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.books) + data.get(1), String.valueOf(model.getFees_books() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_copies()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.copies) + data.get(1), String.valueOf(model.getFees_copies() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_exam()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.exam_fees) + data.get(1), String.valueOf(model.getFees_exam() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_tution()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.monthly_fees) + data.get(1), String.valueOf(model.getFees_tution() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_uniform()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.uniform) + data.get(1), String.valueOf(model.getFees_uniform() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
            if (Math.abs(model.getFees_others()) > 0) {
                ArrayList<String> data = generateJvNoAndDescForReceipt(model);
                asmList.add(new AccountStatementModel(model.getId(), data.get(0), model.getDeposit_slip_no() > 0 ? String.valueOf(model.getDeposit_slip_no()) : "", "", model.getCreated_on(), model.getDeposit_date(), getString(R.string.others) + data.get(1), String.valueOf(model.getFees_others() * value), model.getTransaction_categoryId(), model.getGrNo()));
            }
        }
    }

    private ArrayList<String> generateJvNoAndDescForReceipt(AccountStatementModelNewStructure
                                                                    model) {
        ArrayList<String> data = new ArrayList<>();
        String jvNo;
        String desc = "";

        if (model.getTransaction_typeId() > 0) {
            switch (model.getTransaction_typeId()) {
                case 2:
                    jvNo = model.getSysId() > 0 ? "C" + String.valueOf(model.getSysId()) : "Pending";
                    desc = "- Correction Receipt  ";
                    data.add(jvNo);
                    data.add(desc);
                    break;
                case 1:
                    jvNo = model.getSysId() > 0 ? "R" + String.valueOf(model.getSysId()) : "Pending";
                    desc = "- Receipt " +
                            (model.getReceiptNo() != null && !model.getReceiptNo().isEmpty() ? model.getReceiptNo() + " " : "");
                    data.add(jvNo);
                    data.add(desc);
                    break;
            }
        } else {
            jvNo = "R" + String.valueOf(model.getSysId());
            desc = "- Receipt " + (model.getReceiptNo() != null && !model.getReceiptNo().isEmpty() ? model.getReceiptNo() + " - " : "");
            data.add(jvNo);
            data.add(desc);
        }

        return data;
    }

    private ArrayList<String> generateJvNoAndDescForInvoice(AccountStatementModelNewStructure
                                                                    model, double amount) {
        ArrayList<String> data = new ArrayList<>();
        String jvNo;
        String desc = "";
        String strAmount;
        if (model.getSysId() > 0) {
            if (model.getTransaction_typeId() > 0) {
                // 1 = Receipt/Normal, 2 = Correction , 3 = Waiver
                switch (model.getTransaction_typeId()) {
                    case 3:
                        jvNo = "W" + String.valueOf(model.getSysId());
//                        desc = "- Waiver Receipt " + model.getSysId();
                        strAmount = String.valueOf((amount * (-1)));
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
//                    case 1:
//                        jvNo = "B" + String.valueOf(model.getSysId());
////                        desc = "- Waiver Receipt " + model.getSysId();
//                        strAmount = String.valueOf((amount * (-1)));
//                        data.add(jvNo);
//                        data.add(desc);
//                        data.add(strAmount);
//                        break;
                    case 2:
                        jvNo = "C" + String.valueOf(model.getSysId());
//                        desc = "- Correction Receipt " + model.getSysId();
                        strAmount = String.valueOf(amount);
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                    default:
                        jvNo = "V" + String.valueOf(model.getSysId());
//                        desc = "- Invoice " + model.getSysId();
                        strAmount = String.valueOf(amount);
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                }
            } else {
                jvNo = "V" + String.valueOf(model.getSysId());
//                desc = "- Invoice " + model.getSysId();
                strAmount = String.valueOf(amount);
                data.add(jvNo);
                data.add(strAmount);
            }
        } else {


            if (model.getTransaction_typeId() > 0) {
                switch (model.getTransaction_typeId()) {
                    case 2:
                        jvNo = "Pending";
                        desc = "- Correction Invoice ";
                        strAmount = String.valueOf((amount) * (-1));
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                    case 3:
                        jvNo = "Pending";
                        desc = "- Waiver Invoice";
                        strAmount = String.valueOf((amount * (-1)));
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                    default:
                        jvNo = "Pending";
//            desc = "- Invoice ";
                        strAmount = String.valueOf(amount);
                        data.add(jvNo);
                        data.add(desc);
                        data.add(strAmount);
                        break;
                }
            }
        }
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etFromDate:
                AppModel.getInstance().DatePicker(etFromDate, getActivity());
                break;
            case R.id.etToDate:
                AppModel.getInstance().DatePicker(etToDate, getActivity());
                break;
            case R.id.iv_search:
                toDate = etToDate.getText().toString().trim();
                fromDate = etFromDate.getText().toString().trim();

                if (!fromDate.isEmpty())
                    fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
                if (!toDate.isEmpty())
                    toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");

                if (schoolId > 0) {
                    setAdapter(getAccountStatement(fromDate, toDate));
                }
                break;
        }
    }
}

