package com.tcf.sma.Survey.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tcf.sma.R;
import com.tcf.sma.Survey.activities.PendingSurveys;
import com.tcf.sma.Survey.activities.Project;
import com.tcf.sma.Survey.activities.ProjectsActivity;
import com.tcf.sma.Survey.helpers.SurveyDBHandler;
import com.tcf.sma.Survey.activities.WebviewActivity;
import com.tcf.sma.Survey.mediaServices.GenericFileService;

import org.json.JSONObject;

import java.io.File;
import java.util.List;


public class ProjectAdapter extends BaseAdapter {

    public boolean isPartial = false;
    Activity activity;
    List<Project> projects;
    SurveyDBHandler dbSurvey;

    private String Json_string;

    public ProjectAdapter(Activity activity) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
    }

    public ProjectAdapter(Activity activity, List<Project> p) {
        this.activity = activity;
        this.projects = p;
    }

    @Override
    public int getCount() {

        return projects.size();

    }

    @Override
    public String getItem(int position) {

        return projects.get(position).getName();

    }

    @Override
    public long getItemId(int position) {

        return projects.get(position).getKey();

    }

    public void showProgress(View progressBar, View Message) {
        progressBar.setVisibility(View.VISIBLE);
        Message.setVisibility(View.VISIBLE);
    }

    public void HideProgress(View progressBar, View Message) {
        progressBar.setVisibility(View.INVISIBLE);
        Message.setVisibility(View.INVISIBLE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();

        final View row;

        if (isPartial) {
            row = inflater.inflate(R.layout.listitem_partial_survey, parent, false);
//			TextView title = (TextView) row.findViewById(android.R.id.text1);
//			title.setText(projects.get(position).getName());
//			title.setTextSize(15);
            dbSurvey = new SurveyDBHandler(activity);
            final int lastQ = dbSurvey.getLastQuestionSaved(projects.get(position).getKey());
            String lastA = "";
            File surveyFile = new File(projects.get(position).getName());

            if (surveyFile.exists()) {
                String jsonBody = GenericFileService.loadFile(projects.get(position).getName());
                if (!jsonBody.equals("")) {
                    try {
                        JSONObject jobj = new JSONObject(jsonBody);
                        Json_string = jobj.toString();

                        //JSONArray jarray = jobj.getJSONArray("survey_form");
                        //if(jarray.length() >0) {
                        //JSONObject curObj = jarray.getJSONObject(jarray.length());
                        //lastA = curObj.getString("field_value");
                        //}
                        //Json_string = jobj.getJSONArray("survey_form").toString();

						/*if(jobj.has("survey_form") ){
							Json_string = jobj.getString("survey_form");
							//remove last and first " here
							JSONArray ajrray = new JSONArray(Json_string);
							for (int i = 0; i < ajrray.length(); i++) {
								ajrray.get(i);
							}
						}*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

//			File file = new File(projects.get(position).getName());
//			if(file.exists())

//			((TextView)row.findViewById(R.id.text_lastQuest)).setText("Last saved Question No. " + String.valueOf(lastQ+1));
//			((TextView)row.findViewById(R.id.text_lastAns)).setText("Answered:  " + lastA);
            ((TextView) row.findViewById(R.id.text_lastAns)).setVisibility(View.GONE);
            ((TextView) row.findViewById(R.id.text_lastQuest)).setText("Pending Survey ...");
            ((TextView) row.findViewById(R.id.text_lastDated)).setText("On: " + dbSurvey.getLastSavedDate(projects.get(position).getKey()));
            row.findViewById(R.id.btn_delete).setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity);

                    dlgAlert.setMessage("Are you sure you want to delete this survey?");
                    dlgAlert.setTitle("Confirm Delete");
                    dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);
                    dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbSurvey.deleteSurvey(projects.get(position).getKey());
                            ((PendingSurveys) activity).onSurveryDeleted(position);
                            projects.remove(position);
                        }
                    });
                    dlgAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    });
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
            });

            row.setOnClickListener(v -> {
                Project info = dbSurvey.getProject(((PendingSurveys) activity).project_id, true);
                File temp_file = new File(projects.get(position).getName());
                Intent webViewIntent;

                webViewIntent = new Intent(activity, WebviewActivity.class);


                webViewIntent.putExtra("cached_file", temp_file.getName());
                webViewIntent.putExtra("project_id", info.getKey());
                webViewIntent.putExtra("project_name", info.getName());
                webViewIntent.putExtra("local_survey_db_id", Long.valueOf(projects.get(position).getKey()));
                webViewIntent.putExtra("json_string", Json_string);
                activity.startActivity(webViewIntent);
            });
        } else {
            row = inflater.inflate(R.layout.projects_list, parent, false);
            TextView title = (TextView) row.findViewById(R.id.prj_title);
            final TextView current_download = (TextView) row.findViewById(R.id.completed);
            Button btn_cancel = (Button) row.findViewById(R.id.btn_cancel);

//			current_download.setVisibility(View.VISIBLE);
            current_download.setText(projects.get(position).getCurrent_download());
            TextView status = (TextView) row.findViewById(R.id.prj_progress);
            ProgressBar progress = (ProgressBar) row.findViewById(R.id.prj_progressBar);
//			progress.setVisibility(View.VISIBLE);
            title.setText(projects.get(position).getName());
            status.setText(projects.get(position).getProgress() + " %");
            progress.setProgress(Math.round(projects.get(position).getProgress()));
            title.setTextSize(15);
            if (projects.get(position).getProgress() > 0 && projects.get(position).getProgress() < 100) {
                current_download.setVisibility(View.VISIBLE);
                progress.setVisibility(View.VISIBLE);
                status.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);

                btn_cancel.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        if (!ProjectsActivity.isProjectCancelled) {
                            current_download.setText(" Cancelling ...");

                            ProjectsActivity.isProjectCancelled = true;
                        }
                    }
                });
            }

        }
        return row;

    }

    public interface projectRequester {
        public void onSurveryDeleted(int position);
    }

}
