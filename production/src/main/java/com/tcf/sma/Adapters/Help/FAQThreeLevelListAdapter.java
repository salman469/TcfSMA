package com.tcf.sma.Adapters.Help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tcf.sma.Models.RetrofitModels.Help.FAQsModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FAQThreeLevelListAdapter extends BaseExpandableListAdapter {

    List<String> parentHeaders;
    List<List<String>> secondLevel;
    private Context context;
    List<LinkedHashMap<String, List<FAQsModel>>> data;

    public FAQThreeLevelListAdapter(Context context, List<String> parentHeader, List<List<String>> secondLevel, List<LinkedHashMap<String, List<FAQsModel>>> data) {
        this.context = context;

        this.parentHeaders = parentHeader;

        this.secondLevel = secondLevel;

        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return parentHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {


        // no idea why this code is working

        return 1;

    }

    @Override
    public Object getGroup(int groupPosition) {

        return groupPosition;
    }

    @Override
    public Object getChild(int group, int child) {


        return child;


    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.faq_row_first, null);
        TextView text = (TextView) convertView.findViewById(R.id.rowParentText);
        text.setText(this.parentHeaders.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final FAQSecondLevelExpandableListView secondLevelELV = new FAQSecondLevelExpandableListView(context);

        List<String> headers = secondLevel.get(groupPosition);

        List<List<FAQsModel>> childData = new ArrayList<>();
        HashMap<String, List<FAQsModel>> secondLevelData = data.get(groupPosition);

        for (String key : secondLevelData.keySet()) {
            childData.add(secondLevelData.get(key));
        }


        secondLevelELV.setAdapter(new FAQSecondLevelAdapter(context, headers, childData));

        secondLevelELV.setGroupIndicator(null);

        secondLevelELV.setChildIndicator(null);
        secondLevelELV.setChildDivider(context.getResources().getDrawable(R.color.transparent));
//        secondLevelELV.setDivider(context.getResources().getDrawable(R.color.white));
//        secondLevelELV.setDividerHeight(2);


        secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    secondLevelELV.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });


        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
