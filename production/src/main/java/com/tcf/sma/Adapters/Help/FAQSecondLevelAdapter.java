package com.tcf.sma.Adapters.Help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tcf.sma.Models.RetrofitModels.Help.FAQsModel;
import com.tcf.sma.R;

import java.util.List;

public class FAQSecondLevelAdapter extends BaseExpandableListAdapter {

    private Context context;


    List<List<FAQsModel>> data;

    List<String> headers;


    public FAQSecondLevelAdapter(Context context, List<String> headers, List<List<FAQsModel>> data) {
        this.context = context;
        this.data = data;
        this.headers = headers;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return headers.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        return headers.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.faq_row_second, null);
        TextView text = (TextView) convertView.findViewById(R.id.rowSecondText);
        String groupText = getGroup(groupPosition).toString();
        text.setText(groupText);

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        List<FAQsModel> childData;

        childData = data.get(groupPosition);


        return childData.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.faq_row_third, null);

        TextView question = (TextView) convertView.findViewById(R.id.tv_question);
        TextView answer = (TextView) convertView.findViewById(R.id.tv_answer);

        List<FAQsModel> childArray = data.get(groupPosition);

        String q = childArray.get(childPosition).getQuestion();
        question.setText(q);

        String a = childArray.get(childPosition).getAnswer();
        answer.setText(a);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<FAQsModel> children = data.get(groupPosition);


        return children.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
