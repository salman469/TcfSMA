package com.tcf.sma.Activities.FeesCollection.ViewReceivables;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.ViewReceivables.ViewReceivablesAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AppInvoice;
import com.tcf.sma.Helpers.DbTables.FeesCollection.AppReceipt;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.ViewReceivables.ViewReceivablesModels;
import com.tcf.sma.Models.ViewReceivables.ViewReceivablesTableModel;
import com.tcf.sma.R;
import com.tcf.sma.SyncClasses.GenericAccountService;
import com.tcf.sma.SyncClasses.SyncUtils;

import java.util.ArrayList;
import java.util.List;

public class ViewReceivables extends DrawerActivity implements ViewReceivablesAdapter.DatasetUpdateListener,
        RadioGroup.OnCheckedChangeListener, OnChartValueSelectedListener, View.OnClickListener {

    RecyclerView rv_receivables;
    TextView tvLastSyncedDate;
    Spinner sp_schools;
    View screen;
    int schoolId = 0;
    List<ViewReceivablesModels> mReceivablesModelList;
    Button btnSyncNow;
    String[] schoolNames;
    ArrayList<SchoolModel> schoolModels = new ArrayList<>();
    private RadioGroup mFilterGroup1;
    private Activity mContext;
    private LineChart chart;
    private String mFilter = null;
    private ViewReceivablesAdapter viewReceivablesAdapter = null;
    private BroadcastReceiver syncFinishReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateAlertsNotification();
        }
    };
    private int roleID = 0;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(syncFinishReciever, new IntentFilter(SyncUtils.syncFinishedIntentFilter));
        updateAlertsNotification();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(syncFinishReciever);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen = setActivityLayout(this, R.layout.activity_view_receivables);

        setToolbar(getString(R.string.viewReceivables), this, false);

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                progressDialog = new ProgressDialog(ViewReceivables.this);
//                progressDialog.setTitle("Please Wait");
//                progressDialog.setMessage("Loading");
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//            }
//        });
        init();
        setFeesReceiptTable();
    }

    private void init() {

        chart = (LineChart) screen.findViewById(R.id.lineChart);
        chart.setOnChartValueSelectedListener(this);

        tvLastSyncedDate = (TextView) screen.findViewById(R.id.tvLastSyncedDate);
        sp_schools = (Spinner) screen.findViewById(R.id.sp_schools);
        btnSyncNow = (Button) screen.findViewById(R.id.btnSyncNow);
        btnSyncNow.setOnClickListener(this);
        mFilterGroup1 = (RadioGroup) screen.findViewById(R.id.RGroup);

        mReceivablesModelList = new ArrayList<>();

        mFilterGroup1.setOnCheckedChangeListener(this);

        mContext = this;

        rv_receivables = (RecyclerView) screen.findViewById(R.id.rv_receivables);
        rv_receivables.setLayoutManager(new LinearLayoutManager(this));
        rv_receivables.setAdapter(new ViewReceivablesAdapter(this, mReceivablesModelList, this));

        populateSchoolSpinner();

        try {
            roleID = DatabaseHelper.getInstance(this).getCurrentLoggedInUser().getRoleId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roleID == AppConstants.roleId_103_V || roleID == AppConstants.roleId_7_AEM) {
            sp_schools.setEnabled(false);
        }
    }

    private void createChart() {
        chart.animateX(3000);
        chart.animateY(3000);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);

        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.WHITE);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.SQUARE);
//        l.setTypeface(tfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = chart.getXAxis();
//        xAxis.setTypeface(tfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);


        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
      /*  leftAxis.setAxisMaximum(30000f);
        leftAxis.setAxisMinimum(0f);*/
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = chart.getAxisRight();
//        rightAxis.setTypeface(tfLight);
        rightAxis.setTextColor(Color.RED);
        /*leftAxis.setAxisMaximum(30000);
        leftAxis.setAxisMinimum(0f);*/
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(true);
//        rightAxis.setDrawLabels(tr);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);

        setChartData();
    }

    private void setChartData() {
        if (mFilter == null) {
            mFilter = AppReceipt.TOTAL_SUM;

        }
        List<ViewReceivablesTableModel> duesList = getGraphList(0);
        List<ViewReceivablesTableModel> collectedList = getGraphList(1);

        final List<String> list = AppModel.getInstance().getMonths();// maintaing x-axis of graph
        ArrayList<Entry> values1 = new ArrayList<>(); // Dues
        ArrayList<Entry> values2 = new ArrayList<>(); // Collected


        for (ViewReceivablesTableModel dues : duesList) {
            values1.add(new Entry(list.indexOf(dues.getMonth()), (float) getYEnteries(mFilter, dues)));
        }

        for (ViewReceivablesTableModel collected : collectedList) {
            values2.add(new Entry(list.indexOf(collected.getMonth()), (float) getYEnteries(mFilter, collected)));
        }


        LineDataSet set1, set2;

        set1 = new LineDataSet(values1, "Invoices");

        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        // set the filled area
        set1.setDrawFilled(false); //mod
        set1.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            set1.setFillColor(Color.BLUE);
        } else {
            set1.setFillColor(Color.BLUE);
        }

        // create a dataset and give it a type
        set2 = new LineDataSet(values2, "Collected");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);
        set2.setLineWidth(1f);
        set2.setValueTextSize(9f);
        set2.setCircleRadius(3f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        set2.setDrawFilled(false); //mod
        set2.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            set2.setFillColor(Color.GREEN);
        } else {
            set2.setFillColor(Color.GREEN);
        }

        chart.getXAxis().setLabelRotationAngle(45);
        chart.getXAxis().setLabelCount(list.size());
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int) value;
                return list.get(index);
            }
        });
        chart.getXAxis().setGranularity(1f);
        // create a data object with the data sets
        LineData data = new LineData(set1, set2);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);

        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate(); // refresh after setting data


    }

    private double getViewReceivableValue(List<ViewReceivablesTableModel> models, String filter) {

        double value = 0;

        for (ViewReceivablesTableModel model : models) {
            switch (filter) {
                case AppInvoice.TOTAL_SUM:
                    value += model.getTotal();
                    break;
                case AppInvoice.FEES_ADMISSION:
                    value += model.getFees_admission();
                    break;
                case AppInvoice.FEES_BOOKS:
                    value += model.getFees_books();
                    break;
                case AppInvoice.FEES_COPIES:
                    value += model.getFees_copies();
                    break;

                case AppInvoice.FEES_EXAM:
                    value += model.getFees_exam();
                    break;

                case AppInvoice.FEES_UNIFORMS:
                    value += model.getFees_uniform();
                    break;

                case AppInvoice.FEES_TUTION:
                    value += model.getFees_tution();
                    break;

            }

        }

        return value;

    }

    private void setFeesReceiptTable() {

        if (mFilter == null) { // first time is null before any selection...
            mFilter = AppReceipt.TOTAL_SUM;
        }
        mReceivablesModelList.clear();

        mReceivablesModelList.add(
                new ViewReceivablesModels(
                        getViewReceivableValue(
                                AppInvoice.getInstance(this).getViewRecvMonth(schoolId, 0)
                                , mFilter)
                        , 0.00,
                        getViewReceivableValue(
                                AppInvoice.getInstance(this).getViewRecvMonth(schoolId, 2)
                                , mFilter) * -1));

        mReceivablesModelList.add(
                new ViewReceivablesModels(
                        getViewReceivableValue(
                                AppInvoice.getInstance(this).getViewRecvMonth(schoolId, 1)
                                , mFilter)
                        , 0.00,
                        getViewReceivableValue(
                                AppInvoice.getInstance(this).getViewRecvMonth(schoolId, 3)
                                , mFilter) * -1));


        //for this academic year

        SchoolModel sm;
        sm = AppInvoice.getInstance(ViewReceivables.this).getSchoolForCurrentAcademicYear(schoolId);
        mReceivablesModelList.add(
                new ViewReceivablesModels(
                        getViewReceivableValue(
                                AppInvoice.getInstance(this).getViewRecvAcad(schoolId, 0, sm.getStart_date(), sm.getEnd_date())
                                , mFilter)
                        , 0.00,
                        getViewReceivableValue(
                                AppInvoice.getInstance(this).getViewRecvAcad(schoolId, 1, sm.getStart_date(), sm.getEnd_date())
                                , mFilter) * -1));


        //for prev academic year
        sm = AppInvoice.getInstance(ViewReceivables.this).getSchoolForPrevAcademicYear(schoolId);
        mReceivablesModelList.add(
                new ViewReceivablesModels(
                        getViewReceivableValue(
                                AppInvoice.getInstance(this).getViewRecvAcad(schoolId, 0, sm.getStart_date(), sm.getEnd_date())
                                , mFilter)
                        , 0.00,
                        getViewReceivableValue(
                                AppInvoice.getInstance(this).getViewRecvAcad(schoolId, 1, sm.getStart_date(), sm.getEnd_date())
                                , mFilter) * -1));
        setAdapter();
    }

    private void setAdapter() {
        if (rv_receivables != null) {
            viewReceivablesAdapter = new ViewReceivablesAdapter(this, mReceivablesModelList, ViewReceivables.this);
            rv_receivables.setAdapter(viewReceivablesAdapter);
            rv_receivables.setNestedScrollingEnabled(true);
        }
    }

    private void populateSchoolSpinner() {

        try {
            sp_schools = (Spinner) screen.findViewById(R.id.sp_schools);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    schoolModels = AppModel.getInstance().getSchoolsForLoggedInUserForFinance(ViewReceivables.this);
//                    schoolModels = DatabaseHelper.getInstance(ViewReceivables.this).getAllUserSchoolsForFinance();
                    try {
                        schoolId = schoolModels.get(0).getId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    schoolNames = new String[schoolModels.size()];
//                    for (int i = 0; i < schoolModels.size(); i++) {
//                        schoolNames[i] = schoolModels.get(i).getName();

                    ViewReceivables.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sp_schools.setAdapter(new ArrayAdapter<>(ViewReceivables.this, android.R.layout.simple_spinner_dropdown_item,
                                    schoolModels));
                            int indexOfSelectedSchool = AppModel.getInstance().populateSelectedSchoolInDropDown(schoolModels,
                                    ViewReceivables.this);
                            if (indexOfSelectedSchool > -1)
                                sp_schools.setSelection(indexOfSelectedSchool);

                            sp_schools.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                    schoolId = ((SchoolModel) adapterView.getItemAtPosition(pos)).getId();
//                                        schoolId = schoolModels.get(pos).getId();
                                    AppModel.getInstance().setSpinnerSelectedSchool(ViewReceivables.this,
                                            schoolId);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            setFeesReceiptTable();
                            try {
                                createChart();
                            } catch (Exception e) {
                                Log.d("Exception", e.toString());
                                e.printStackTrace();
                            }
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        progressDialog.cancel();
//                                    }
//                                }, 1000);
                        }

                    });
                }

            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception Thread", e.toString());
        }
    }

    @Override
    public void onDataSetChanged(List<ViewReceivablesModels> models) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {// find which radio button is selected

        if (checkedId == R.id.rbTotal) {
            mFilter = AppReceipt.TOTAL_SUM;
        } else if (checkedId == R.id.rbAdmFees) {
            mFilter = AppReceipt.FEES_ADMISSION;
        } else if (checkedId == R.id.rbMFees) {
            mFilter = AppReceipt.FEES_TUTION;
        } else if (checkedId == R.id.rbBooks) {
            mFilter = AppReceipt.FEES_BOOKS;
        } else if (checkedId == R.id.rbCopies) {
            mFilter = AppReceipt.FEES_COPIES;
        } else if (checkedId == R.id.rbUnifomrs) {
            mFilter = AppReceipt.FEES_UNIFORMS;
        }

        setFeesReceiptTable();
        setChartData();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSyncNow:
                if (!ContentResolver.isSyncActive(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY) && !ContentResolver.isSyncPending(GenericAccountService.GetAccount(), AppConstants.CONTENT_AUTHORITY)) {
                    Bundle b = new Bundle();
                    b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                    b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                    b.putBoolean("forceSync", true);
                    SyncUtils.TriggerRefresh(mContext, b, SyncProgressHelperClass.SYNC_TYPE_MANUAL_SYNC_ID);
                    Toast.makeText(mContext, "Syncing data please wait...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Sync is already running please wait for it to complete", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void updateAlertsNotification() {
        SharedPreferences prefs = getSharedPreferences("syncPrefs", Context.MODE_PRIVATE);
        tvLastSyncedDate.setText("Last Synced on: " + AppModel.getInstance().
                convertDatetoFormat(prefs.getString("syncSuccessTime", "Nill"), " dd-MM-yyyy hh:mm a", " dd-MMM-yy hh:mm a"));
    }


    private List<ViewReceivablesTableModel> getGraphList(int filter) {

        String[] dates = AppModel.getInstance().getGraphFromToDates();
        List<String> list = AppModel.getInstance().getMonths();
        List<ViewReceivablesTableModel> vrtm = AppInvoice.getInstance(this).getGraphReceivables(schoolId, dates[0], dates[1], filter);

        List<ViewReceivablesTableModel> vrtmFinal = new ArrayList<>();

        for (String ls : list) {
            ViewReceivablesTableModel model = new ViewReceivablesTableModel();
            model.setMonth(ls);
            vrtmFinal.add(model);
        }


        for (ViewReceivablesTableModel model : vrtm) {
            if (list.contains(model.getMonth())) {
                int i = list.indexOf(model.getMonth());
                vrtmFinal.get(i).setTotal((model.getTotal() < 0) ? model.getTotal() * -1 : model.getTotal());
                vrtmFinal.get(i).setFees_admission((model.getFees_admission() < 0) ? model.getFees_admission() * -1 : model.getFees_admission());
                vrtmFinal.get(i).setFees_books((model.getFees_books() < 0) ? model.getFees_books() * -1 : model.getFees_books());
                vrtmFinal.get(i).setFees_copies((model.getFees_copies() < 0) ? model.getFees_copies() * -1 : model.getFees_copies());
                vrtmFinal.get(i).setFees_exam((model.getFees_exam() < 0) ? model.getFees_exam() * -1 : model.getFees_exam());
                vrtmFinal.get(i).setFees_others((model.getFees_others() < 0) ? model.getFees_others() * -1 : model.getFees_others());
                vrtmFinal.get(i).setFees_tution((model.getFees_tution() < 0) ? model.getFees_tution() * -1 : model.getFees_tution());
                vrtmFinal.get(i).setFees_uniform((model.getFees_uniform() < 0) ? model.getFees_uniform() * -1 : model.getFees_uniform());
            }
        }

        return vrtmFinal;
    }

    private double getYEnteries(String filter, ViewReceivablesTableModel model) {
        double value = 0;

        switch (filter) {
            case AppInvoice.TOTAL_SUM:
                value += model.getTotal();
                return value;

            case AppInvoice.FEES_ADMISSION:
                value += model.getFees_admission();
                return value;

            case AppInvoice.FEES_BOOKS:
                value += model.getFees_books();
                return value;

            case AppInvoice.FEES_COPIES:
                value += model.getFees_copies();
                return value;

            case AppInvoice.FEES_EXAM:
                value += model.getFees_exam();
                return value;

            case AppInvoice.FEES_UNIFORMS:
                value += model.getFees_uniform();
                return value;

            case AppInvoice.FEES_TUTION:
                value += model.getFees_tution();
                return value;
        }

        return 0;
    }
}
