package com.tcf.sma.Activities.FeesCollection.ViewSSR;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.tcf.sma.Adapters.ViewSSR.ViewSSRTableAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.Models.ViewSSR.ViewSSRTableModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.List;

public class ViewSSRActivity extends DrawerActivity
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, OnChartValueSelectedListener {

    public static final String VIEW_SSR_GRAPH_FILTER_TOTAL_SUM = "TOTAL_SUM";
    public static final String VIEW_SSR_GRAPH_FILTER_WITHDRAWALS = "WITH_DRAWAL";
    public static final String VIEW_SSR_GRAPH_FILTER_NEW_ENROLMENT = "NEW_ENRROLMENT";

    private static DatabaseHelper sDbHelper;

    int filter = 0;
    private View screen;
    private TextView tvFromDate, tvToDate, tvDateValue, tv_search;
    private ArrayList<String> labels = new ArrayList<String>();
    private ArrayList<SchoolModel> schoolModels = new ArrayList<>();

    private List<ViewSSRTableModel> mViewSSRTableList;

    private ViewSSRTableModel mViewSSRTableModel;

    private ViewSSRTableAdapter mViewSSRTableAdapter;

    private RecyclerView mViewSSRChartRV;

    private int schoolId = 0;
    private Spinner sp_schools;
    private String[] schoolNames;

    private RadioGroup mRadioGroup;
    private String mFilter;

    private LineChart chart;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        screen = setActivityLayout(this, R.layout.activity_view_ssr);
        setToolbar(getString(R.string.viewSSR), this, false);

        showProgressDialog();
        init();
        populateSchoolSpinner();
        setListeners();
    }

    private void init() {
        chart = (LineChart) screen.findViewById(R.id.lineChart);

        mRadioGroup = (RadioGroup) screen.findViewById(R.id.RGroup);

        sDbHelper = DatabaseHelper.getInstance(this);

        sp_schools = (Spinner) screen.findViewById(R.id.sp_schools);

        mViewSSRChartRV = (RecyclerView) screen.findViewById(R.id.rv_ssr);
        mViewSSRChartRV.setLayoutManager(new LinearLayoutManager(this));

        tvFromDate = (TextView) screen.findViewById(R.id.etFromDate);
        tvToDate = (TextView) screen.findViewById(R.id.etToDate);

        tv_search = (TextView) screen.findViewById(R.id.tv_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedTableData();
                createChart();
            }
        });


        tvFromDate.setText(AppModel.getInstance().getLastMonthFromCurrentDate("dd-MMM-yy"));
        tvToDate.setText(AppModel.getInstance().getCurrentDateTime("dd-MMM-yy"));


        mFilter = ViewSSRActivity.VIEW_SSR_GRAPH_FILTER_TOTAL_SUM;
    }

    private void showProgressDialog() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(ViewSSRActivity.this);
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage("Loading");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
    }

    private void feedTableData() {
        mViewSSRTableList = new ArrayList<>();

        String fromDate = AppModel.getInstance().convertDatetoFormat(tvFromDate.getText().toString(), "dd-MMM-yy", "yyy-MM-dd");
        String toDate = AppModel.getInstance().convertDatetoFormat(tvToDate.getText().toString(), "dd-MMM-yy", "yyy-MM-dd");

        //GetSSRPreviousMonthByGender
        mViewSSRTableModel = sDbHelper.getViewSSRTableData(schoolId, fromDate, toDate, 0);

        mViewSSRTableList.add(mViewSSRTableModel);

        //GetNewAdmissionThisMonth
        mViewSSRTableModel = sDbHelper.getViewSSRTableData(schoolId, fromDate, toDate, 1);

        mViewSSRTableList.add(mViewSSRTableModel);

        //GetSSRWithDrawalThisMonth
        mViewSSRTableModel = sDbHelper.getViewSSRTableData(schoolId, fromDate, toDate, 2);

        mViewSSRTableList.add(mViewSSRTableModel);

        //GetGraduatesThisMonth
        mViewSSRTableModel = sDbHelper.getViewSSRTableData(schoolId, fromDate, toDate, 3);

        mViewSSRTableList.add(mViewSSRTableModel);


        mViewSSRTableAdapter = new ViewSSRTableAdapter(ViewSSRActivity.this, mViewSSRTableList);
        mViewSSRChartRV.setAdapter(mViewSSRTableAdapter);

    }

    private void setListeners() {
        tvToDate.setOnClickListener(this);
        tvFromDate.setOnClickListener(this);

        mRadioGroup.setOnCheckedChangeListener(this);

        chart.setOnChartValueSelectedListener(this);
    }

    private void populateSchoolSpinner() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                schoolModels = DatabaseHelper.getInstance(ViewSSRActivity.this).getAllUserSchools();

                try {
                    schoolId = schoolModels.get(0).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ViewSSRActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sp_schools.setAdapter(new ArrayAdapter<>(ViewSSRActivity.this, android.R.layout.simple_spinner_dropdown_item,
                                schoolModels));

                        sp_schools.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                schoolId = schoolModels.get(pos).getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        ViewSSRActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                feedTableData();
                                createChart();
                            }
                        });

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.cancel();
                            }
                        }, 1000);
                    }

                });
            }

        });
        thread.start();
    }

    private void createChart() {
        chart.animateX(3000);
        chart.animateY(3000);

        // no description text
        chart.getDescription().setEnabled(false);
//        chart.getDescription().setText("");

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
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

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

        final List<String> list = AppModel.getInstance().getMonths();

        List<ViewSSRTableModel> vSSRFinal = getGraphList(list, filter);

        ArrayList<Entry> values1 = new ArrayList<>(); // male
        ArrayList<Entry> values2 = new ArrayList<>(); // female
        ArrayList<Entry> values3 = new ArrayList<>(); // total

        for (int i = 0; i < vSSRFinal.size(); i++) {
            values1.add(new Entry(list.indexOf(vSSRFinal.get(i).getRep_month()), (float) vSSRFinal.get(i).getmMale()));
            values2.add(new Entry(list.indexOf(vSSRFinal.get(i).getRep_month()), (float) vSSRFinal.get(i).getmFemale()));
            values3.add(new Entry(list.indexOf(vSSRFinal.get(i).getRep_month()), (float) vSSRFinal.get(i).getmOverall()));


        }

        LineDataSet set1, set2, set3;

        // create a dataset and give it a type
        set1 = new LineDataSet(values1, "Male");

        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        // set the filled area
        set1.setDrawFilled(false);
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
        set2 = new LineDataSet(values2, "Female");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);
        set2.setLineWidth(2f);
        set2.setCircleRadius(3f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        set2.setDrawFilled(false);
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

        set3 = new LineDataSet(values3, "Total");
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
        set3.setColor(R.color.gray);
        set3.setCircleColor(R.color.gray);
        set3.setLineWidth(2f);
        set3.setCircleRadius(3f);
        set3.setFillAlpha(65);
        set3.setFillColor(R.color.gray);
        set3.setDrawCircleHole(false);
        set3.setHighLightColor(Color.rgb(244, 117, 117));
        set3.setDrawFilled(false); // not filled
        set3.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // set color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            set3.setFillColor(R.color.gray);
        } else {
            set3.setFillColor(R.color.gray);
        }
        //set2.setFillFormatter(new MyFillFormatter(900f));


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
        LineData data = new LineData(set1, set2, set3);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

        // set data
        chart.setData(data);

        // redraw

        chart.notifyDataSetChanged();
        chart.invalidate();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etFromDate:
                AppModel.getInstance().DatePicker(tvFromDate, ViewSSRActivity.this);
                break;

            case R.id.etToDate:
                AppModel.getInstance().DatePicker(tvToDate, ViewSSRActivity.this);
                break;

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {// find which radio button is selected
        if (checkedId == R.id.rbTotal) {
            mFilter = ViewSSRActivity.VIEW_SSR_GRAPH_FILTER_TOTAL_SUM;
            filter = 0;
        } else if (checkedId == R.id.rbNewEnrolment) {
            mFilter = ViewSSRActivity.VIEW_SSR_GRAPH_FILTER_NEW_ENROLMENT;
            filter = 1;
        } else if (checkedId == R.id.rbWithdrawls) {
            mFilter = ViewSSRActivity.VIEW_SSR_GRAPH_FILTER_WITHDRAWALS;
            filter = 2;
        }


//        feedTableData();
        setChartData();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private List<ViewSSRTableModel> getGraphList(List<String> list, int filter) {
        String[] dates = AppModel.getInstance().getGraphFromToDates();
        //getMixUpList
        List<ViewSSRTableModel> vrtm = sDbHelper.getViewSSRGraphData(schoolId, dates[0], dates[1], filter);

        //separate list for males and females
        List<ViewSSRTableModel> males = new ArrayList<>();
        List<ViewSSRTableModel> females = new ArrayList<>();


        //separation
        for (ViewSSRTableModel mod : vrtm) {
            if (mod.getGender().equals("M"))
                males.add(mod);
            else
                females.add(mod);
        }

        //final list
        List<ViewSSRTableModel> vSSRFinal = new ArrayList<>();

        //populate months
        for (String ls : list) {
            ViewSSRTableModel model = new ViewSSRTableModel();
            model.setRep_month(ls);
            vSSRFinal.add(model);
        }

        //populate male
        for (ViewSSRTableModel model : males) {
            int i = list.indexOf(model.getRep_month());
            vSSRFinal.get(i).setmMale(model.getmMale());
        }

        //populate fe male
        for (ViewSSRTableModel model : females) {
            int i = list.indexOf(model.getRep_month());
            vSSRFinal.get(i).setmFemale(model.getmFemale());
        }

        for (int i = 0; i < vSSRFinal.size(); i++) {
            vSSRFinal.get(i).setmOverall(vSSRFinal.get(i).getmMale() + vSSRFinal.get(i).getmFemale());
        }

        return vSSRFinal;
    }
}
