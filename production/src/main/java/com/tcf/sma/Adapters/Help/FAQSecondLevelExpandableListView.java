package com.tcf.sma.Adapters.Help;

import android.content.Context;
import android.widget.ExpandableListView;

public class FAQSecondLevelExpandableListView extends ExpandableListView
{

    public FAQSecondLevelExpandableListView(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //999999 is a size in pixels. ExpandableListView requires a maximum height in order to do measurement calculations.
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
