package com.tcf.sma.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Adapters.HighestDuesListAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Models.HighestDuesStudentsModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.Collections;

public class HighestDuesStudentsListActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout ll_stName, ll_stGrno, ll_stClassSec, ll_stAmount;
    RecyclerView rvHighestDuesFromStudent;
    ArrayList<HighestDuesStudentsModel> hdList;
    HighestDuesListAdapter highestDuesListAdapter;
    ImageView iv_stName, iv_stGrno, iv_stClassSec, iv_stAmount;

    String schoolId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highest_dues_list);
        handleIntent(getIntent());
        init();
        setRvHighestDuesStudents();

    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra("schoolId")) {
                schoolId = intent.getStringExtra("schoolId");
            }
        }
    }

    private void init() {
        ll_stName = findViewById(R.id.ll_stName);
        ll_stGrno = findViewById(R.id.ll_stGrno);
        ll_stClassSec = findViewById(R.id.ll_stClassSec);
        ll_stAmount = findViewById(R.id.ll_stAmount);

        iv_stName = findViewById(R.id.iv_stName);
        iv_stGrno = findViewById(R.id.iv_stGrno);
        iv_stClassSec = findViewById(R.id.iv_stClassSec);
        iv_stAmount = findViewById(R.id.iv_stAmount);

        ll_stName.setOnClickListener(this);
        ll_stGrno.setOnClickListener(this);
        ll_stClassSec.setOnClickListener(this);
        ll_stAmount.setOnClickListener(this);

        rvHighestDuesFromStudent = (RecyclerView) findViewById(R.id.rvHighestDuesFromStudent);
        rvHighestDuesFromStudent.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvHighestDuesFromStudent.setLayoutManager(llm);

    }

    private void setRvHighestDuesStudents() {
        hdList = DatabaseHelper.getInstance(this).getAllhighestDuesStudent(schoolId);
        if (hdList != null && hdList.size() != 0) {
            highestDuesListAdapter = new HighestDuesListAdapter(hdList, this, schoolId);
            rvHighestDuesFromStudent.setAdapter(highestDuesListAdapter);
        }
    }

    private void Sort_In_Asc(int f) {
        if (f == 1) {        //Name
            Collections.sort(hdList, (hdsm1, hdsm2) -> {
                return hdsm1.getStudntsName().compareTo(hdsm2.getStudntsName());
            });

        } else if (f == 2) {     //Grno
            Collections.sort(hdList, (hdsm1, hdsm2) -> {
                if (hdsm1.getStudentGr_NO() != null && hdsm2.getStudentGr_NO() != null) {
                    return Integer.compare(Integer.parseInt(hdsm1.getStudentGr_NO()), Integer.parseInt(hdsm2.getStudentGr_NO()));
                }
                return -1;
            });
        } else if (f == 3) {      //Class sec
            Collections.sort(hdList, (hdsm1, hdsm2) -> {
                return Integer.compare(hdsm1.getClassRank(), hdsm2.getClassRank());
            });

        } else if (f == 4) {      //Amount
            Collections.sort(hdList, (hdsm1, hdsm2) -> {
                if (hdsm1.getAmount() != null && hdsm2.getAmount() != null) {
                    return Integer.compare(Integer.parseInt(hdsm1.getAmount()), Integer.parseInt(hdsm2.getAmount()));
                }
                return -1;
            });
        }

        highestDuesListAdapter.notifyDataSetChanged();
    }

    private void Sort_In_Des(int f) {
        if (f == 1) {        //Name
            Collections.sort(hdList, (hdsm1, hdsm2) -> {
                return hdsm2.getStudntsName().compareTo(hdsm1.getStudntsName());
            });

        } else if (f == 2) {     //Grno
            Collections.sort(hdList, (hdsm1, hdsm2) -> {
                if (hdsm1.getStudentGr_NO() != null && hdsm2.getStudentGr_NO() != null) {
                    return Integer.compare(Integer.parseInt(hdsm2.getStudentGr_NO()), Integer.parseInt(hdsm1.getStudentGr_NO()));
                }
                return -1;
            });
        } else if (f == 3) {      //Class sec
            Collections.sort(hdList, (hdsm1, hdsm2) -> {
                return Integer.compare(hdsm2.getClassRank(), hdsm1.getClassRank());
            });

        } else if (f == 4) {      //Amount
            Collections.sort(hdList, (hdsm1, hdsm2) -> {
                if (hdsm1.getAmount() != null && hdsm2.getAmount() != null) {
                    return Integer.compare(Integer.parseInt(hdsm2.getAmount()), Integer.parseInt(hdsm1.getAmount()));
                }
                return -1;
            });
        }

        highestDuesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_stName) {
            if (iv_stName.getDrawable().getConstantState() == iv_stName.getResources()
                    .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                iv_stName.setImageResource(android.R.drawable.arrow_down_float);
                Sort_In_Des(1);
            } else {
                iv_stName.setImageResource(android.R.drawable.arrow_up_float);
                Sort_In_Asc(1);
            }
        } else if (v.getId() == R.id.ll_stGrno) {
            if (iv_stGrno.getDrawable().getConstantState() == iv_stGrno.getResources()
                    .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                iv_stGrno.setImageResource(android.R.drawable.arrow_down_float);
                Sort_In_Des(2);
            } else {
                iv_stGrno.setImageResource(android.R.drawable.arrow_up_float);
                Sort_In_Asc(2);
            }
        } else if (v.getId() == R.id.ll_stClassSec) {
            if (iv_stClassSec.getDrawable().getConstantState() == iv_stClassSec.getResources()
                    .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                iv_stClassSec.setImageResource(android.R.drawable.arrow_down_float);
                Sort_In_Des(3);
            } else {
                iv_stClassSec.setImageResource(android.R.drawable.arrow_up_float);
                Sort_In_Asc(3);
            }
        } else if (v.getId() == R.id.ll_stAmount) {
            if (iv_stAmount.getDrawable().getConstantState() == iv_stAmount.getResources()
                    .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                iv_stAmount.setImageResource(android.R.drawable.arrow_down_float);
                Sort_In_Des(4);
            } else {
                iv_stAmount.setImageResource(android.R.drawable.arrow_up_float);
                Sort_In_Asc(4);
            }
        }
    }
}
