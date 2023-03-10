package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.tcf.sma.Adapters.CustomPageAdapter;
import com.tcf.sma.Models.StudentModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 1/11/2017.
 */

public class AreaManagerStudentSelection extends DrawerActivity {
    public static ViewPager vp_studentSelection;
    ArrayList<StudentModel> list;
    CustomPageAdapter myadpter;
    private View view;
    private int position, count, grno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getIntent().getIntExtra("Postion", 0);
        count = getIntent().getIntExtra("count", 0);
        grno = getIntent().getIntExtra("grno", 0);

        view = setActivityLayout(this, R.layout.area_manager_student_selection);
        setToolbar(getString(R.string.profile_validation), this, false);
        init(view);
        list = StudentModel.getInstance().getStudentsList();
        myadpter = new CustomPageAdapter(list, this, grno);
        vp_studentSelection.setAdapter(myadpter);
        vp_studentSelection.setCurrentItem(position);

    }


    private void init(View view) {
        vp_studentSelection = (ViewPager) view.findViewById(R.id.viewpager);
    }

    public void removePage(int position) {
        list.remove(position);
        myadpter = null;
        if (list.size() == 0) {
            finish();
        } else {
            myadpter = new CustomPageAdapter(list, this, grno);
            vp_studentSelection.setAdapter(myadpter);
            myadpter.notifyDataSetChanged();
        }
    }
}
