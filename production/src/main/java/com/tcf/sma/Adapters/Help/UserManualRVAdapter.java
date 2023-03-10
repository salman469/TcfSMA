package com.tcf.sma.Adapters.Help;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcf.sma.Helpers.DbTables.Help.HelpHelperClass;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.Help.UserManualModel;
import com.tcf.sma.R;
import com.tcf.sma.Services.UserManualDownloadService;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class UserManualRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<UserManualModel> userManualModelList;

    public UserManualRVAdapter(Context context, List<UserManualModel> userManualModelList) {
        this.context = context;
        this.userManualModelList = userManualModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserManualViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_user_manual_data, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserManualViewHolder userManualViewHolder = (UserManualViewHolder) holder;

        if (userManualModelList.get(position).getManual_Name() != null)
            userManualViewHolder.tv_manual_name.setText(userManualModelList.get(position).getManual_Name());
        if (userManualModelList.get(position).getVersion() != null)
            userManualViewHolder.tv_version.setText(userManualModelList.get(position).getVersion());
    }

    @Override
    public int getItemCount() {
        return userManualModelList.size();
    }

    public class UserManualViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_manual_name, tv_version;
        private CardView card_view;

        public UserManualViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_manual_name = itemView.findViewById(R.id.tv_manual_name);
            tv_version = itemView.findViewById(R.id.tv_version);
            card_view = itemView.findViewById(R.id.card_view);

            card_view.setOnClickListener(v -> {
                String fileName = userManualModelList.get(getAdapterPosition()).getFilename();
                String dirPath = StorageUtil.getSdCardPath(context).getAbsolutePath() + "/TCF/TCF Documents/";
                File manualFile = new File(dirPath,fileName);
                if (!manualFile.exists()) {
                    if (AppModel.getInstance().isConnectedToInternet(context)) {
                        downloadManual(getAdapterPosition());
                    } else {
                        AlertDialog msg = new AlertDialog.Builder(context).create();
                        msg.setMessage("No internet connectivity!\nPlease connect to internet");
                        msg.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        msg.show();
                    }
                }else {
                    HelpHelperClass.getInstance(context).openManual(fileName);
                }
            });
        }

        private void downloadManual(int adapterPosition) {
            String filePath = userManualModelList.get(adapterPosition).getFilepath();
            String fileUrl = AppConstants.URL_DOWNLOAD_USER_MANUAL + filePath;
            String fileName = userManualModelList.get(adapterPosition).getFilename();

            //Start Async service
            UserManualDownloadService manualDownloadService = new UserManualDownloadService(context);
            manualDownloadService.execute(fileUrl, fileName);
        }
    }
}
