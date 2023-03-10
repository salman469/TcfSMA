package com.tcf.sma.Survey.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.tcf.sma.R;
import com.tcf.sma.Survey.adapters.ProjectAdapter;
import com.tcf.sma.Survey.helpers.SurveyDBHandler;

import java.util.ArrayList;


public class PendingSurveys extends Activity implements ProjectAdapter.projectRequester {
    public int project_id;
    ArrayList<Project> surveyList;
    SurveyDBHandler dbSurvey;
    ListView lv_stats;
    ProjectAdapter pa;

    @Override
    protected void onResume() {
        if (dbSurvey != null) {
            surveyList = dbSurvey.getPartialSurveys(project_id);
            if (pa != null)
                pa.notifyDataSetChanged();
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pending_surveys);

        if (getIntent().getExtras().containsKey("project_id"))
            project_id = getIntent().getExtras().getInt("project_id");

        dbSurvey = new SurveyDBHandler(getApplicationContext());
        surveyList = dbSurvey.getPartialSurveys(project_id);
        //surveyList = surveyList.
        //System.out.println(surveyList.);


        lv_stats = (ListView) findViewById(R.id.listView_partial_surveys);
        pa = new ProjectAdapter(this, surveyList);
        pa.isPartial = true;
        lv_stats.setAdapter(pa);


    }

    @Override
    public void onSurveryDeleted(int position) {
        //surveyList.remove(position);
        //pa.notifyDataSetChanged();
        onResume();
    }

}
