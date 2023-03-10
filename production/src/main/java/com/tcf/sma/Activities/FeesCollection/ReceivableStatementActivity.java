package com.tcf.sma.Activities.FeesCollection;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.FeesCollection.AccountStatementAdapter;
import com.tcf.sma.Adapters.FeesCollection.ReceivableReportAdapter;
import com.tcf.sma.Adapters.StudentAutoCompleteAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementModel;
import com.tcf.sma.Models.Fees_Collection.AccountStatementModelNewStructure;
import com.tcf.sma.Models.Fees_Collection.AccountStatementUnionModel;
import com.tcf.sma.Models.Fees_Collection.ReceiptListModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.StudentAutoCompleteModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created By Meghani
 */
public class ReceivableStatementActivity extends DrawerActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    View screen;
    String grNo, toDate, fromDate;
    List<AccountStatementModel> asmList;
    List<AccountStatementUnionModel> unionModels;
    List<AccountStatementModelNewStructure> modelNewStructureList;
    List<SchoolModel> schoolModels;
    private RecyclerView rvReceivableStatement;
    private Spinner spnSchools;
    private EditText et_grNo;
    private TextView etFromDate, etToDate, tvNoDataFound;
    private int schoolId = 0;
    private ReceivableReportAdapter adapter;
    private boolean isRunForFirstTime = false;
    private int roleID = 0;
    private AutoCompleteTextView et_std_name_grNo;
    private StudentAutoCompleteAdapter studentAdapter;
    private LinearLayout llResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = setActivityLayout(this, R.layout.activity_receivable_statement);
        setToolbar("Receivable Statement", this, false);
        init();
        populateSchoolSpinner();
        populateStudentAutocompleteAdapter();

        et_std_name_grNo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
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
                AppModel.getInstance().showLoader(ReceivableStatementActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        setAdapter(getReceivableReport(grNo, fromDate, toDate));
                        ReceivableStatementActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AppModel.getInstance().hideLoader();
                            }
                        });
                    }
                }).start();

                AppModel.getInstance().hideSoftKeyboard(ReceivableStatementActivity.this);
            }
        });
        et_std_name_grNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    grNo = "";

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setAdapter(getReceivableReport(grNo, fromDate, toDate));
                        }
                    });
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    }).start();*/
                }
            }
        });
        etFromDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fromDate = etFromDate.getText().toString().trim();
                if (!fromDate.isEmpty())
                    fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
                AppModel.getInstance().showLoader(ReceivableStatementActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        setAdapter(getReceivableReport(grNo, fromDate, toDate));
                        ReceivableStatementActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AppModel.getInstance().hideLoader();
                            }
                        });
                    }
                }).start();

                AppModel.getInstance().hideSoftKeyboard(ReceivableStatementActivity.this);
            }
        });

        etToDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                toDate = etToDate.getText().toString().trim();
                if (!toDate.isEmpty())
                    toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");
                AppModel.getInstance().showLoader(ReceivableStatementActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        setAdapter(getReceivableReport(grNo, fromDate, toDate));
                        ReceivableStatementActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AppModel.getInstance().hideLoader();
                            }
                        });
                    }
                }).start();

                AppModel.getInstance().hideSoftKeyboard(ReceivableStatementActivity.this);
            }
        });
//        grNo = et_std_name_grNo.getText().toString().trim();


//        grNo = et_grNo.getText().toString().trim();
        toDate = etToDate.getText().toString().trim();
        fromDate = etFromDate.getText().toString().trim();


        if (!fromDate.isEmpty())
            fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
        if (!toDate.isEmpty())
            toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");

        Intent intent = getIntent();
        if (intent.hasExtra("StudentGrNo")/*intent.hasExtra("classId") && intent.hasExtra("sectionId") &&*/) {
            try {
//                classId = intent.getIntExtra("classId", 0);
//                sectionID = intent.getIntExtra("sectionId", 0);

                grNo = intent.getStringExtra("StudentGrNo");
//                et_grNo.setText(grNo);
                et_std_name_grNo.setText(grNo);
//                iv_search.performClick();
                AppModel.getInstance().hideLoader();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AppModel.getInstance().showLoader(this);


            new Thread(new Runnable() {
                @Override
                public void run() {


                    setAdapter(getReceivableReport(grNo, fromDate, toDate));
                    ReceivableStatementActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AppModel.getInstance().hideLoader();
                        }
                    });
                }
            }).start();
        }

    }

    private void populateStudentAutocompleteAdapter() {
        final List<StudentAutoCompleteModel> studentsList = DatabaseHelper.getInstance(this).getAllStudentsForAutocompleteList(schoolId);
        studentAdapter = new StudentAutoCompleteAdapter(this, R.layout.student_autocomplete_view,
                studentsList);
        et_std_name_grNo.setAdapter(studentAdapter);
    }

    private List<ReceiptListModel> getReceivableReport(String grNo, String fromDate, String toDate) {
        String openingReceivables = "0";
        List<ReceiptListModel> receiptsList = FeesCollection.getInstance(this).getPreviousReceiptsForReceivableReport(schoolId, fromDate, toDate, grNo);
        List<ReceiptListModel> invoicesList = FeesCollection.getInstance(this).getPreviousInvoicesForReceivableReport(schoolId, fromDate, toDate, grNo);
        ReceiptListModel openingBalance = FeesCollection.getInstance(this).getOpeningForReceivableReport(schoolId, fromDate, toDate, grNo);

        List<ReceiptListModel> previousReceiptsList = FeesCollection.getInstance(this).getPreviousReceiptsForReceivableReport(schoolId, "2015-10-18 00:00:00 AM", AppModel.getInstance().getYesterdayFromCurrentDate(fromDate, "yyyy-MM-dd"), grNo);
        List<ReceiptListModel> previousInvoicesList = FeesCollection.getInstance(this).getPreviousInvoicesForReceivableReport(schoolId, "2015-10-18 00:00:00 AM", AppModel.getInstance().getYesterdayFromCurrentDate(fromDate, "yyyy-MM-dd"), grNo);
        double previousReceiptsBalance = previousReceiptsList.stream().mapToDouble(ReceiptListModel::getTotalAmount).sum();
        double previousInvoicesBalance = previousInvoicesList.stream().mapToDouble(ReceiptListModel::getTotalAmount).sum();


        receiptsList.addAll(invoicesList);

        if (receiptsList.size() > 0) {
            Collections.sort(receiptsList, Comparator.comparing(ReceiptListModel::getCreateOn));

            ReceiptListModel openingBalanceModel = new ReceiptListModel();
            openingBalanceModel.setCreateOn(receiptsList.get(0).getCreateOn());
            openingBalanceModel.setReceivables(previousInvoicesBalance - previousReceiptsBalance);
            openingBalanceModel.setTotalAmount(openingBalance.getTotalAmount());
            openingBalanceModel.setTypeId(0);

            receiptsList.add(0, openingBalanceModel);

            for(int i = 1; i<receiptsList.size(); i++) {
                if(receiptsList.get(i).getCategoryId() == 1){
                    double receivable = receiptsList.get(i-1).getReceivables() + receiptsList.get(i).getTotalAmount();
                    receiptsList.get(i).setReceivables(receivable);
                } else if (receiptsList.get(i).getCategoryId() == 2) {
                    double receivable = receiptsList.get(i-1).getReceivables() - receiptsList.get(i).getTotalAmount();
                    receiptsList.get(i).setReceivables(receivable);
                }
            }
        }
        return receiptsList;


    }

    public void init() {
        et_grNo = (EditText) screen.findViewById(R.id.et_grNo);
        etFromDate = (TextView) screen.findViewById(R.id.etFromDate);
        etToDate = screen.findViewById(R.id.etToDate);
        etFromDate.setText(AppModel.getInstance().getLastWeekFromCurrentDate("dd-MMM-yy"));
        etToDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MMM-yy"));
//        tvNoDataFound = (TextView) screen.findViewById(R.id.tvNoDataFound);
        et_std_name_grNo = screen.findViewById(R.id.et_std_name_grNo);
        llResults = screen.findViewById(R.id.llResults);

        etToDate.setOnClickListener(this);
        etFromDate.setOnClickListener(this);
        spnSchools = (Spinner) screen.findViewById(R.id.spnSchools);
        spnSchools.setOnItemSelectedListener(this);

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            spnSchools.setEnabled(false);
        }

        rvReceivableStatement = (RecyclerView) screen.findViewById(R.id.rvReceivableStatement);

        rvReceivableStatement.setLayoutManager(new LinearLayoutManager(this));
        rvReceivableStatement.setNestedScrollingEnabled(false);

        et_grNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    grNo = et_grNo.getText().toString().trim();
                    toDate = etToDate.getText().toString().trim();
                    fromDate = etFromDate.getText().toString().trim();

                    if (!fromDate.isEmpty())
                        fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
                    if (!toDate.isEmpty())
                        toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");

                    AppModel.getInstance().showLoader(ReceivableStatementActivity.this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            setAdapter(getReceivableReport(grNo, fromDate, toDate));

                            ReceivableStatementActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AppModel.getInstance().hideLoader();
                                }
                            });
                        }
                    }).start();
                    AppModel.getInstance().hideSoftKeyboard(ReceivableStatementActivity.this);
                    return true;
                }
                return false;
            }
        });

        setCollapsing(false);

    }

    private void populateSchoolSpinner() {
        schoolId = AppModel.getInstance().getSelectedSchool(this);
        schoolModels = AppModel.getInstance().getSchoolsForLoggedInUserForFinance(this);

        ArrayAdapter<SchoolModel> schoolSelectionAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, schoolModels);
        spnSchools.setAdapter(schoolSelectionAdapter);
        int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels, this);
        if (indexOfSelectedSchool > -1) {
            spnSchools.setSelection(indexOfSelectedSchool);
            schoolId = AppModel.getInstance().getSpinnerSelectedSchool(this);
        }

    }

    private void setAdapter(final List<ReceiptListModel> asmList) {
        if (asmList != null && asmList.size() > 0) {
//            tvNoDataFound.setVisibility(View.GONE);
            adapter = new ReceivableReportAdapter(asmList);
            ReceivableStatementActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setCollapsing(true);
                    rvReceivableStatement.setAdapter(adapter);
                    llResults.setVisibility(View.VISIBLE);
                }
            });
        } else {
            ReceivableStatementActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setCollapsing(false);
                    llResults.setVisibility(View.GONE);
                    MessageBox("No data found");
//                    tvNoDataFound.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnSchools:
                if (isRunForFirstTime) {
                    schoolId = ((SchoolModel) parent.getItemAtPosition(position)).getId();
                    AppModel.getInstance().setSpinnerSelectedSchool(this,
                            schoolId);
                    populateStudentAutocompleteAdapter();
//                    et_grNo.setText("");
                    et_std_name_grNo.setText("");
                    grNo = "";
                    toDate = etToDate.getText().toString().trim();
                    fromDate = etFromDate.getText().toString().trim();
                    if (!fromDate.isEmpty())
                        fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
                    if (!toDate.isEmpty())
                        toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");
                    AppModel.getInstance().showLoader(ReceivableStatementActivity.this);


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            setAdapter(getReceivableReport(grNo, fromDate, toDate));
                            ReceivableStatementActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AppModel.getInstance().hideLoader();
                                }
                            });
                        }
                    }).start();
                }

                isRunForFirstTime = true;

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                toDate = etToDate.getText().toString().trim();
                fromDate = etFromDate.getText().toString().trim();
                if (!fromDate.isEmpty())
                    fromDate = AppModel.getInstance().convertDatetoFormat(fromDate, "dd-MMM-yy", "yyyy-MM-dd");
                if (!toDate.isEmpty())
                    toDate = AppModel.getInstance().convertDatetoFormat(toDate, "dd-MMM-yy", "yyyy-MM-dd");
                AppModel.getInstance().showLoader(ReceivableStatementActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        setAdapter(getReceivableReport(grNo, fromDate, toDate));
                        ReceivableStatementActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AppModel.getInstance().hideLoader();
                            }
                        });
                    }
                }).start();
                break;
            case R.id.etFromDate:
                AppModel.getInstance().DatePicker(etFromDate, ReceivableStatementActivity.this);
                break;
            case R.id.etToDate:
                AppModel.getInstance().DatePicker(etToDate, ReceivableStatementActivity.this);
                break;
        }
    }

    private void setCollapsing(boolean isCollapsable) {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        if (isCollapsable)
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL); // list other flags here by |
        else
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP); // list other flags here by |
        collapsingToolbar.setLayoutParams(params);
    }

}
