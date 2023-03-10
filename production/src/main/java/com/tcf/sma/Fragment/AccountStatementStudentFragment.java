package com.tcf.sma.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
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
import com.tcf.sma.Adapters.StudentAutoCompleteAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementModelNewStructure;
import com.tcf.sma.Models.Fees_Collection.AccountStatementSchoolModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentAutoCompleteModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountStatementStudentFragment extends Fragment implements View.OnClickListener {

    private View layoutView;
    private LinearLayout ll_depositAndReceiptNo, ll_title, llStudentInfo;
    private AutoCompleteTextView et_std_name_grNo;
    private TextView etFromDate, etToDate, tv_name, tv_className, tv_sectionName, tv_fathersName, tvNoDataFound, tv_title;
    private RecyclerView rvAccountStatement;
    private EditText et_depSlipNo, et_receiptNo;
    private ImageView iv_search_depositNo_and_receiptNo;

    private String grNo, toDate, fromDate;
    private int schoolId = 0;
    private StudentAutoCompleteAdapter studentAdapter;
    private List<AccountStatementSchoolModel> modelNewStructureList;
    private List<AccountStatementModel> asmList;
    private AccountStatementSchoolAdapter adapter;
    private SchoolModel schoolModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layoutView = inflater.inflate(R.layout.fragment_student_account_statement, container, false);
        init(layoutView);
        working(layoutView);
        return layoutView;
    }

    private void working(View view) {
        schoolId = ((AccountStatementNewActivity) Objects.requireNonNull(getActivity())).schoolId;
        schoolModel = DatabaseHelper.getInstance(view.getContext()).getSchoolById(schoolId);

        if (schoolId > 0) {
            populateStudentAutocompleteAdapter();

            if (schoolModel != null) {
                tv_title.setText("Statement for School \n" + schoolModel.getName());
                ll_title.setVisibility(View.VISIBLE);
            } else {
                ll_title.setVisibility(View.GONE);
            }
        }

        if (getArguments() != null) {
            grNo = getArguments().getString("StudentGrNo");
            et_std_name_grNo.setText(grNo);
            setAdapter(getAccountStatement(grNo,
                    AppModel.getInstance().convertDatetoFormat(etFromDate.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd"),
                    AppModel.getInstance().convertDatetoFormat(etToDate.getText().toString().trim(), "dd-MMM-yy", "yyyy-MM-dd")));
        }

    }

    private void populateStudentAutocompleteAdapter() {
        final List<StudentAutoCompleteModel> studentsList = DatabaseHelper.getInstance(layoutView.getContext()).getAllStudentsForAutocompleteList(schoolId);
        studentAdapter = new StudentAutoCompleteAdapter(layoutView.getContext(), R.layout.student_autocomplete_view,
                studentsList);
        et_std_name_grNo.setAdapter(studentAdapter);
    }

    private void clearStudentFields() {
        tv_name.setText("");
        tv_sectionName.setText("");
        tv_fathersName.setText("");
        tv_className.setText("");
    }

    private void populateStudentFields(AccountStatementSchoolModel sm) {
        tv_name.setText(sm.getStudentName());
        tv_fathersName.setText(sm.getFatherName());
        tv_className.setText(sm.getClassName());
        tv_sectionName.setText(sm.getSection());
    }

    private void init(View view) {
        et_std_name_grNo = view.findViewById(R.id.et_std_name_grNo);
        et_std_name_grNo.setOnItemClickListener((adapterView, view1, position, id) -> {
            String std_grno = ((StudentAutoCompleteModel) adapterView.getItemAtPosition(position)).getGrNo();
            String std_name = ((StudentAutoCompleteModel) adapterView.getItemAtPosition(position)).getStudent_name();
            et_std_name_grNo.setText(std_name);
            grNo = std_grno;
            toDate = etToDate.getText().toString().trim();
            fromDate = etFromDate.getText().toString().trim();

            if (!fromDate.isEmpty())
                fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
            if (!toDate.isEmpty())
                toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");

            setAdapter(getAccountStatement(grNo, fromDate, toDate));


            AppModel.getInstance().hideSoftKeyboard(getActivity());
        });


        etFromDate = (TextView) view.findViewById(R.id.etFromDate);
        etFromDate.setOnClickListener(this);
        etToDate = (TextView) view.findViewById(R.id.etToDate);
        etToDate.setOnClickListener(this);

        etFromDate.setText(AppModel.getInstance().getLastWeekFromCurrentDate("dd-MMM-yy"));
        etToDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MMM-yy"));

        ll_title = (LinearLayout) view.findViewById(R.id.ll_title);
        llStudentInfo = (LinearLayout) view.findViewById(R.id.llStudentInfo);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_className = (TextView) view.findViewById(R.id.tv_className);
        tv_sectionName = (TextView) view.findViewById(R.id.tv_sectionName);
        tv_fathersName = (TextView) view.findViewById(R.id.tv_fathersName);
        tvNoDataFound = (TextView) view.findViewById(R.id.tvNoDataFound);
        tv_title = (TextView) view.findViewById(R.id.tv_title);

        rvAccountStatement = (RecyclerView) view.findViewById(R.id.rvAccountStatement);

        rvAccountStatement.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvAccountStatement.setNestedScrollingEnabled(false);

        et_depSlipNo = (EditText) view.findViewById(R.id.et_depSlipNo);
        et_receiptNo = (EditText) view.findViewById(R.id.et_receiptNo);
        ll_depositAndReceiptNo = (LinearLayout) view.findViewById(R.id.ll_depositAndReceiptNo);

        iv_search_depositNo_and_receiptNo = (ImageView) view.findViewById(R.id.iv_search_depositNo_and_receiptNo);
        iv_search_depositNo_and_receiptNo.setOnClickListener(this);
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
            case R.id.iv_search_depositNo_and_receiptNo:
//                et_std_name_grNo.setText("");
//                grNo = et_std_name_grNo.getText().toString();
                if (grNo != null) {
                    toDate = etToDate.getText().toString().trim();
                    fromDate = etFromDate.getText().toString().trim();

                    if (!fromDate.isEmpty())
                        fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
                    if (!toDate.isEmpty())
                        toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");
//                    SurveyAppModel.getInstance().showLoader(layoutView.getContext());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            setAdapter(getAccountStatement(grNo, fromDate, toDate));

//                            AccountStatementActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    SurveyAppModel.getInstance().hideLoader();
//                                }
//                            });
                        }
                    }).start();
                } else {
                    ll_depositAndReceiptNo.setVisibility(View.GONE);
                }
                break;
        }
    }


    private void setAdapter(List<AccountStatementSchoolModel> asmList) {
        if (asmList.size() > 0) {
            adapter = new AccountStatementSchoolAdapter(getActivity(), asmList);
//            adapter = new AccountStatementAdapter(AccountStatementActivity.this, asmList,
//                    grNo, et_receiptNo.getText().toString(),unionModels,
//                    et_depSlipNo.getText().toString());
//            adapter = new AccountStatementAdapter(AccountStatementActivity.this, asmList);

            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rvAccountStatement.setAdapter(adapter);
                    rvAccountStatement.setVisibility(View.VISIBLE);
                    if (grNo == null || grNo.isEmpty()) {
                        ll_depositAndReceiptNo.setVisibility(View.GONE);
                    } else if (grNo != null) {
                        ll_depositAndReceiptNo.setVisibility(View.VISIBLE);
                    }

                }
            });
        } else {
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                rvAccountStatement.setVisibility(View.GONE);
                ll_depositAndReceiptNo.setVisibility(View.GONE);
            });
        }
    }

    private List<AccountStatementSchoolModel> getAccountStatement(String grNo, String fromDate, String toDate) {
//        unionModels = AppInvoice.getInstance(this).getAccountStatement(schoolId, grNo, fromDate, toDate, depSlipNo, receiptNo);
        modelNewStructureList = FeesCollection.getInstance(layoutView.getContext()).getAccountStatementForStudent(schoolId, fromDate, toDate, Integer.parseInt(grNo));
        asmList = new ArrayList<>();
        if (modelNewStructureList != null && modelNewStructureList.size() > 0) {
            if (grNo != null) {
                if (modelNewStructureList.get(0).getStudentName() != null && !modelNewStructureList.get(0).getStudentName().equals("")) {

                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        populateStudentFields(modelNewStructureList.get(0));
                        ll_title.setVisibility(View.GONE);
                        llStudentInfo.setVisibility(View.VISIBLE);
                        rvAccountStatement.setVisibility(View.VISIBLE);
                        ll_depositAndReceiptNo.setVisibility(View.VISIBLE);
                    });
                } else {

                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        llStudentInfo.setVisibility(View.GONE);
                        ll_title.setVisibility(View.GONE);
                        clearStudentFields();
//                            Toast.makeText(AccountStatementActivity.this, "Student Not found", Toast.LENGTH_LONG).show();
                        rvAccountStatement.setVisibility(View.GONE);
                        ll_depositAndReceiptNo.setVisibility(View.GONE);

                    });
                }

            } else {
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    llStudentInfo.setVisibility(View.GONE);
                    ll_title.setVisibility(View.VISIBLE);
                    clearStudentFields();

                });
            }
            int i = 0;
//            for (AccountStatementUnionModel model : modelNewStructureList) {
//                if (model.getCreated_from().equals("1") && model.getTransaction_type().equals("R")) //correction
//                    checkFeesProperties(model, -1);
//                else //normal receipt
//                    checkFeesProperties(model, 1);
//
//
//            }
//            for (AccountStatementModelNewStructure model : modelNewStructureList) {
//                if (model.getTransaction_typeId() == 2 && model.getTransaction_categoryId() == 2) // 2 = correction 2 = Receipt
//                    checkFeesProperties(model, 1);  // -1 should be 1
//                else //normal receipt
//                    checkFeesProperties(model, -1);  // 1 should be -1
//
//
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

        } else {
            if (!et_std_name_grNo.getText().toString().trim().isEmpty()) {
//                boolean digitsOnly = TextUtils.isDigitsOnly(et_std_name_grNo.getText());
//                if (digitsOnly) {
//                    grNo = et_std_name_grNo.getText().toString();
                final StudentModel sm = DatabaseHelper.getInstance(layoutView.getContext()).getStudentwithGR(Integer.parseInt(grNo), schoolId);
                if (sm.getName() != null && sm.getCurrentClass() != null) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        tv_name.setText(sm.getName());
                        tv_fathersName.setText(sm.getFathersName());
                        tv_className.setText(sm.getCurrentClass());
                        tv_sectionName.setText(sm.getCurrentSection());
                        llStudentInfo.setVisibility(View.VISIBLE);
                    });
                } else {
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                        llStudentInfo.setVisibility(View.GONE);
                        clearStudentFields();
                    });
                }
//                } else {
//                    grNo = "";
//                    AccountStatementActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            llStudentInfo.setVisibility(View.GONE);
//                            clearStudentFields();
//                        }
//                    });
//                }

            }
        }
        return modelNewStructureList;
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

    private ArrayList<String> generateJvNoAndDescForReceipt(AccountStatementModelNewStructure model) {
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

    private ArrayList<String> generateJvNoAndDescForInvoice(AccountStatementModelNewStructure model, double amount) {
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
}
