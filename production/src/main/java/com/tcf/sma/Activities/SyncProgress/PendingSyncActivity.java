package com.tcf.sma.Activities.SyncProgress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Adapters.SyncProgress.PendingSyncAdapter;
import com.tcf.sma.Helpers.SyncProgress.SyncProgressHelperClass;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.SyncProgress.PendingSyncModel;
import com.tcf.sma.R;

import java.util.List;

public class PendingSyncActivity extends DrawerActivity implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private RecyclerView rv_pendingRecords;
    public TextView tv_total_pending_sync,tv_noRecordFound;
    private SwipeRefreshLayout pullToRefresh;
    private LinearLayout ll_Result;

    private PendingSyncAdapter pendingSyncAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pending_sync);
        view = setActivityLayout(this, R.layout.activity_pending_sync);
        setToolbar("Pending Records", this, false);

        initialize();
        setAdapter();
    }

    private void initialize() {
        tv_total_pending_sync = view.findViewById(R.id.tv_total_pending_sync);
        tv_noRecordFound = view.findViewById(R.id.tv_noRecordFound);

        ll_Result = view.findViewById(R.id.ll_Result);

        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(this);

        rv_pendingRecords = view.findViewById(R.id.rv_pendingRecords);
        rv_pendingRecords.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    private void setAdapter() {
        List<PendingSyncModel> pendingSyncModels = SyncProgressHelperClass.getInstance(this)
                .getPendingSyncList(AppModel.getInstance().getAllUserSchoolsCommaSeparated(this));
        if (pendingSyncModels != null && pendingSyncModels.size() > 0){

            pendingSyncAdapter = new PendingSyncAdapter(pendingSyncModels,this);
            rv_pendingRecords.setAdapter(pendingSyncAdapter);
            ll_Result.setVisibility(View.VISIBLE);
            tv_noRecordFound.setVisibility(View.GONE);
        }else {
            ll_Result.setVisibility(View.GONE);
            tv_noRecordFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        setAdapter();
        pullToRefresh.setRefreshing(false);
    }
}
