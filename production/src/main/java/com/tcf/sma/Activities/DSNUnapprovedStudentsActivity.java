package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.DSNUnapprovedAdpater;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;

public class DSNUnapprovedStudentsActivity extends DrawerActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public View view;
    TextView totalCount, unApprovedCount;
    RecyclerView rv_dsnuList;
    DSNUnapprovedAdpater dsnUnapprovedAdpater;
    ArrayList<StudentModel> dsnlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = setActivityLayout(this, R.layout.activity_dns_unapproved_students);
        setToolbar(getString(R.string.dsnuapproved_students), this, false);
        init(view);
        working();
        setAdapter();


    }

    private void working() {
        {
            rv_dsnuList.setNestedScrollingEnabled(false);
            rv_dsnuList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rv_dsnuList.setLayoutManager(llm);

        }
    }

    public void init(View v) {
        rv_dsnuList = (RecyclerView) v.findViewById(R.id.rv_dsnu_validation);
        totalCount = (TextView) v.findViewById(R.id.tv_totat);
        unApprovedCount = (TextView) v.findViewById(R.id.tv_unapproved);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUnApprovedCount();
        if (dsnUnapprovedAdpater != null) {
        }
    }

    public void setUnApprovedCount() {
        int count = DatabaseHelper.getInstance(this).getAllStudentsCount(AppModel.getInstance().getSelectedSchool(this));
        int unApproved = DatabaseHelper.getInstance(this).getAllUnapprovedStudentsCountDashboard(AppModel.getInstance().getSelectedSchool(this));
        totalCount.setText("" + count);
        unApprovedCount.setText("" + unApproved);

    }

    private void setAdapter() {
        dsnlist = DatabaseHelper.getInstance(this).getDashboardUnapprovedStudents(AppModel.getInstance().getSelectedSchool(this), 0);
        if (dsnlist != null && dsnlist.size() != 0) {
            dsnUnapprovedAdpater = new DSNUnapprovedAdpater(dsnlist, this);
            rv_dsnuList.setAdapter(dsnUnapprovedAdpater);
        }
    }
}
