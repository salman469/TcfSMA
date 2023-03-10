package com.tcf.sma.Managers.Help;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tcf.sma.Activities.AreaManagerStudentSelection;
import com.tcf.sma.Activities.NewAdmissionActivity;
import com.tcf.sma.Adapters.CustomPageAdapter;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.Help.HelpHelperClass;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.Help.FeedbackModel;
import com.tcf.sma.R;

public class FeedbackDialogManager extends Dialog implements View.OnClickListener {
    private Activity activity;
    private EditText et_feedback_comment;
    private ImageView iv_cancel_feedback;
    private Button btn_submit_feedback;
    private RatingBar ratingBar_feedback;
    private TextView tv_rating_description;

    public FeedbackDialogManager(Context context) {
        super(context);
        this.activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialogue_feedback);
        init();

    }

    private void init() {
        ratingBar_feedback = findViewById(R.id.ratingBar_feedback);
        et_feedback_comment = findViewById(R.id.et_feedback_comment);
        tv_rating_description = findViewById(R.id.tv_rating_description);

        btn_submit_feedback = findViewById(R.id.btn_submit_feedback);
        btn_submit_feedback.setOnClickListener(this);

        iv_cancel_feedback = findViewById(R.id.iv_cancel_feedback);
        iv_cancel_feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel_feedback:
                AppConstants.feedbackDialogCanceledPressed = true;
                AppConstants.isFeedbackDialogShowing = false;
                dismiss();
                break;
            case R.id.btn_submit_feedback:
                if(ratingBar_feedback.getRating() > 0){
                    AppConstants.isFeedbackDialogShowing = false;
                    submitFeedback();
                } else
                    Toast.makeText(activity, "Please give rating", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void submitFeedback() {
        try {
            int rating = (int) ratingBar_feedback.getRating();
            String comment = et_feedback_comment.getText().toString();

            FeedbackModel model = new FeedbackModel();
            model.setStars(rating);
            model.setComments(comment);
            model.setCreatedOn_App(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss"));
            model.setCreatedBy(DatabaseHelper.getInstance(activity).getCurrentLoggedInUser().getId());

            long i = HelpHelperClass.getInstance(activity).insertIntoFeedback(model);
            if (i > 0) {
                Toast.makeText(activity, "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                //Important when any change in table call this method
                AppModel.getInstance().changeMenuPendingSyncCount(activity, false);
//                AppModel.getInstance().startSyncService(activity, SyncProgressHelperClass.SYNC_TYPE_SAVE_SYNC_ID);
                dismiss();
            }
        }catch (Exception e){
            AppModel.getInstance().appendErrorLog(activity,"Error in submitFeedback: " +  e.getMessage());
        }
    }
}
