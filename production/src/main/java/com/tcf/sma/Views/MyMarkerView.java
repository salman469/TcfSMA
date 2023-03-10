package com.tcf.sma.Views;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.tcf.sma.Models.AttendancePercentageModel;
import com.tcf.sma.R;

import java.util.ArrayList;

/**
 * Created by Zubair Soomro on 4/6/2017.
 */

public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private ArrayList<AttendancePercentageModel> models;

    public MyMarkerView(Context context, int layoutResource, ArrayList<AttendancePercentageModel> attendanceHeaderList) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
        this.models = attendanceHeaderList;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        try {
            tvContent.setText(models.get((int) e.getX() - 1).getForDate());
        } catch (Exception a) {
            a.printStackTrace();
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
