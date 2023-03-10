package com.tcf.sma.Activities.Help;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.Help.UserManualRVAdapter;
import com.tcf.sma.Helpers.DbTables.Help.HelpHelperClass;
import com.tcf.sma.Models.RetrofitModels.Help.UserManualModel;
import com.tcf.sma.R;

import java.util.List;

public class UserManualActivity extends DrawerActivity {

    private View view;
    private RecyclerView rv_user_manuals;
    private UserManualRVAdapter userManualRVAdapter;
    private TextView tv_noRecordFound;

    private List<UserManualModel> userManualModelList;
    private boolean isPolicy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_manual);
        view = setActivityLayout(this, R.layout.activity_user_manual);
        isPolicy = getIntent().hasExtra("isPolicy");

        if (isPolicy)
            setToolbar(getResources().getString(R.string.policies), this, false);
        else
            setToolbar(getResources().getString(R.string.user_manual), this, false);

        initialize();
        working();
    }

    private void initialize() {
        tv_noRecordFound = findViewById(R.id.tv_noRecordFound);
        rv_user_manuals = findViewById(R.id.rv_user_manuals);
        rv_user_manuals.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rv_user_manuals.setHasFixedSize(true);
    }

    private void working() {
        if (isPolicy)
            userManualModelList = HelpHelperClass.getInstance(view.getContext()).getPolicies();
        else
            userManualModelList = HelpHelperClass.getInstance(view.getContext()).getUserManual();

        if (userManualModelList != null && userManualModelList.size() > 0) {
            userManualRVAdapter = new UserManualRVAdapter(view.getContext(), userManualModelList);
            rv_user_manuals.setAdapter(userManualRVAdapter);

            tv_noRecordFound.setVisibility(View.GONE);
        } else {
            tv_noRecordFound.setVisibility(View.VISIBLE);
        }
    }
}