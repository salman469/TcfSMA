package com.tcf.sma.Activities.Help;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.Help.FAQThreeLevelListAdapter;
import com.tcf.sma.Helpers.DbTables.Help.HelpHelperClass;
import com.tcf.sma.Models.RetrofitModels.Help.FAQsModel;
import com.tcf.sma.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FrequentlyAskQuestionsActivity extends DrawerActivity {

    private View view;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<FAQsModel> faQsModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_frequently_ask_questions);
        view = setActivityLayout(this, R.layout.activity_frequently_ask_questions);
        setToolbar(getResources().getString(R.string.faqs), this, false);

        initialize();
        working();
    }

    private void initialize() {
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
    }

    private void working() {
        faQsModelList = HelpHelperClass.getInstance(view.getContext()).getFaqsList();

        if (faQsModelList != null && faQsModelList.size() > 0) {

            LinkedHashMap<String, List<FAQsModel>> thirdLevel = new LinkedHashMap<>();

            List<List<String>> secondLevel = new ArrayList<>();

            List<LinkedHashMap<String, List<FAQsModel>>> data = new ArrayList<>();

            List<String> modules = getModules(faQsModelList);
            List<String> subModules = getSubModules(faQsModelList);

            for (String m : modules) {
                List<String> sm = new ArrayList<>();
                for (FAQsModel model : faQsModelList) {
                    if (m.equals(model.getHead()) && sm.isEmpty()) {
                        sm.add(model.getSubHead());
                    } else {
                        if (m.equals(model.getHead()) && !sm.contains(model.getSubHead())) {
                            sm.add(model.getSubHead());
                        }
                    }
                }
                secondLevel.add(sm);
            }

            for (String sm : subModules) {
                List<FAQsModel> f = new ArrayList<>();
                for (FAQsModel model : faQsModelList) {
                    if (sm.equals(model.getSubHead()) && f.isEmpty()) {
                        f.add(model);
                    } else {
                        if (sm.equals(model.getSubHead()) && !f.contains(model)) {
                            f.add(model);
                        }
                    }
                }
                thirdLevel.put(sm, f);
            }

            for (String m : modules) {
                LinkedHashMap<String, List<FAQsModel>> thirdLevelSubModuleList = new LinkedHashMap<>();
                for (LinkedHashMap.Entry<String, List<FAQsModel>> entry : thirdLevel.entrySet()) {
                    for (FAQsModel f : entry.getValue()) {
                        if (m.equals(f.getHead())) {
                            thirdLevelSubModuleList.put(entry.getKey(), entry.getValue());
                        }
                    }

                }
                data.add(thirdLevelSubModuleList);
            }

            // expandable listview
            expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

            // parent adapter
            FAQThreeLevelListAdapter threeLevelListAdapterAdapter = new FAQThreeLevelListAdapter(this, modules, secondLevel, data);


            // set adapter
            expandableListView.setAdapter(threeLevelListAdapterAdapter);


            // OPTIONAL : Show one list at a time
            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    if (groupPosition != previousGroup)
                        expandableListView.collapseGroup(previousGroup);
                    previousGroup = groupPosition;
                }
            });
        }
    }

    private List<String> getModules(List<FAQsModel> faQsModelList) {
        List<String> modules = new ArrayList<>();
        for (FAQsModel model : faQsModelList) {
            if (modules.isEmpty()) {
                modules.add(model.getHead());
            } else {
                if (!modules.contains(model.getHead())) {
                    modules.add(model.getHead());
                }
            }
        }
        return modules;
    }

    private List<String> getSubModules(List<FAQsModel> faQsModelList) {
        List<String> subModules = new ArrayList<>();
        for (FAQsModel model : faQsModelList) {
            if (subModules.isEmpty()) {
                subModules.add(model.getSubHead());
            } else {
                if (!subModules.contains(model.getSubHead())) {
                    subModules.add(model.getSubHead());
                }
            }
        }
        return subModules;
    }
}