package com.tcf.sma.Adapters.HR;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tcf.sma.Activities.HR.EmployeeDetailsActivity;
import com.tcf.sma.Activities.HR.ResignationApproval;
import com.tcf.sma.Activities.HR.TerminationApproval;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Interfaces.OnItemClickListener;
import com.tcf.sma.Managers.ImagePreviewDialog;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.R;
import com.tcf.sma.Survey.activities.EmployeeSelector;
import com.tcf.sma.Survey.model.SurveyAppModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class EmployeeListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<EmployeeModel> employeeModelList;
    private int resignType, status;
    private boolean isLeaveApproval;
    private int schoolId;
    private OnItemClickListener listener;
    private EditText  email, mobileNumber;
    private EmployeeModel em = new EmployeeModel();
    private int empDetailId = 0;
    boolean ImageCaptured = false;
    public static int pos=0;

    public EmployeeListingAdapter(Context context) {
        this.context = context;
    }

    public EmployeeListingAdapter(Context context, int schoolId, List<EmployeeModel> employeeModelList) {
        this.context = context;
        this.employeeModelList = employeeModelList;
        this.schoolId = schoolId;
    }

    public EmployeeListingAdapter(Context context, int schoolId, List<EmployeeModel> employeeModelList, OnItemClickListener listener) {
        this.context = context;
        this.employeeModelList = employeeModelList;
        this.schoolId = schoolId;
        this.listener = listener;
    }

    public EmployeeListingAdapter(Context context, List<EmployeeModel> employeeModelList, int schoolid, boolean isLeaveApproval, int Activestatus) {
        this.context = context;
        this.employeeModelList = employeeModelList;
        this.isLeaveApproval = isLeaveApproval;
        this.schoolId = schoolid;
        this.status = Activestatus;
    }

    public EmployeeListingAdapter(Context context, List<EmployeeModel> employeeModelList, int resignType, int Activestatus) {
        this.context = context;
        this.employeeModelList = employeeModelList;
        this.resignType = resignType;
        this.status = Activestatus;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*switch (i) {
            case 0:
                return new EmployeeDetailHeaderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_listing_header, viewGroup, false));
            case 1:
                return new EmployeeDetailViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_employee_listing_detail, viewGroup, false));
        }
        return null;*/
        return new EmployeeDetailViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_employee_listing_detail, viewGroup, false));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder Holder, int new_position) {

        EmployeeDetailViewHolder empDetailViewHolder = (EmployeeDetailViewHolder) Holder;
        try {
            String doj = AppModel.getInstance().convertDatetoFormat(employeeModelList
                    .get(new_position).getDate_Of_Joining(), "yyyy-MM-dd", "dd-MMM-yy");
            String lwd = "";
            if (employeeModelList.get(new_position).getLastWorkingDay() == null)
                lwd = "Current";
            else
                lwd = AppModel.getInstance().convertDatetoFormat(employeeModelList
                        .get(new_position).getLastWorkingDay(), "yyyy-MM-dd", "dd-MMM-yy");
            empDetailViewHolder._empCode.setText(employeeModelList.get(new_position).getEmployee_Code());
            empDetailViewHolder._empUserId.setText(employeeModelList.get(new_position).getId() + "");
            empDetailViewHolder._empName.setText(employeeModelList.get(new_position).getFirst_Name() + " " + employeeModelList.get(new_position).getLast_Name());
            if (employeeModelList.get(new_position).getGender().equals("M"))
                empDetailViewHolder._empName.setTextColor(context.getResources().getColor(R.color.blue));
            else if (employeeModelList.get(new_position).getGender().equals("F"))
                empDetailViewHolder._empName.setTextColor(context.getResources().getColor(R.color.pink));

            empDetailViewHolder._empJoinDate.setText(doj + " > " + lwd);

            int progress = 0;
//                if(employeeModelList.get(new_position).getMother_Name() != null && !employeeModelList.get(new_position).getMother_Name().trim().equals(""))
//                    progress += 20;
//                if(employeeModelList.get(new_position).getFather_Name() != null && !employeeModelList.get(new_position).getFather_Name().trim().equals(""))
//                    progress += 20;
            if (employeeModelList.get(new_position).getMobile_No() != null && !employeeModelList.get(new_position).getMobile_No().trim().equals(""))
                progress += 80;
//                if(employeeModelList.get(new_position).getNIC_No() != null && !employeeModelList.get(new_position).getNIC_No().trim().equals(""))
//                    progress += 20;
            if (employeeModelList.get(new_position).getEmail() != null && !employeeModelList.get(new_position).getEmail().trim().equals(""))
                progress += 20;

            empDetailViewHolder.profileProgressCount.setText(progress + "%");
            empDetailViewHolder.profileProgressBar.setProgress(progress);
            if (progress < 20)
                empDetailViewHolder.profileProgressCount.setTextColor(context.getResources().getColor(R.color.app_green));
            else if (progress < 50) {
                empDetailViewHolder.profileProgressCount.setTextColor(context.getResources().getColor(R.color.light_red_color));
                empDetailViewHolder.profileProgressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.employee_profile_progress_red));
            } else if (progress < 80) {
                empDetailViewHolder.profileProgressCount.setTextColor(context.getResources().getColor(R.color.light_orange_color));
                empDetailViewHolder.profileProgressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.employee_listing_profile_progress_orange));
            }

            String Designation = employeeModelList.get(new_position).getDesignation();
            String Cadre = employeeModelList.get(new_position).getCADRE();
            if (Designation == null)
                Designation = "-";
            if (Cadre == null)
                Cadre = "-";

            empDetailViewHolder._empCadre.setText(Designation + " (" + Cadre + ")");
            if (Cadre.equals("Non-Faculty"))
                empDetailViewHolder._empCadre.setTextColor(context.getResources().getColor(R.color.orange));

            empDetailViewHolder._empStatus.setText(employeeModelList.get(new_position).getJob_Status());
            if (!employeeModelList.get(new_position).getJob_Status().equals("Active")) {
                empDetailViewHolder._empName.setTextColor(context.getResources().getColor(R.color.gray));
                empDetailViewHolder._empCadre.setTextColor(context.getResources().getColor(R.color.gray));
                empDetailViewHolder._empStatus.setTextColor(context.getResources().getColor(R.color.gray));
                if (progress < 20)
                    empDetailViewHolder.profileProgressCount.setTextColor(context.getResources().getColor(R.color.gray));
                else
                    empDetailViewHolder.profileProgressCount.setTextColor(context.getResources().getColor(R.color.light_gray));
                empDetailViewHolder.profileProgressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.employee_listing_gray_pp));

                empDetailViewHolder.employee_item.setBackgroundColor(context.getResources().getColor(R.color.light_red_color));
            }

//                if(progress < 10)
//                    empDetailViewHolder.profileProgressCount.setTextColor(context.getResources().getColor(R.color.gray));
//                    empDetailViewHolder._empStatus.setTextColor(context.getResources().getColor(R.color.red));
//                }

            try {
                File userImagePath = new File(EmployeeHelperClass.getInstance(context).getUserImagePath(employeeModelList.get(new_position).getId()));

                byte[] data = AppModel.getInstance().bitmapToByte(Compressor.getDefault(context).compressToBitmap(userImagePath));
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                empDetailViewHolder.iv_capture.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


//        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) {
//            return 0;
//        } else
//            return 1;
//    }

    @Override
    public int getItemCount() {
        if (employeeModelList != null && employeeModelList.size() > 0) {
            return employeeModelList.size();
        } else {
            return 0;
        }
    }

    private String countEmployees() {
        int empCount = 0;
        for (EmployeeModel em : employeeModelList) {
            empCount++;
        }
        return " " + empCount;
    }

    class EmployeeDetailViewHolder extends RecyclerView.ViewHolder {
        TextView _empCode, _empName, _empJoinDate, _empCadre, _empUserId, _empStatus, profileProgressCount;
        LinearLayout employee_item;
        public CircleImageView iv_capture;
        ProgressBar profileProgressBar;


        EmployeeDetailViewHolder(@NonNull final View itemView) {
            super(itemView);

            employee_item = itemView.findViewById(R.id.employee_listing_item);
            iv_capture = itemView.findViewById(R.id.iv_capture);
            _empCode = itemView.findViewById(R.id.txt_EmpCode);
            _empName = itemView.findViewById(R.id.txt_Name);
            _empJoinDate = itemView.findViewById(R.id.txt_joinDate);
            _empCadre = itemView.findViewById(R.id.txt_Cadre);
            _empUserId = itemView.findViewById(R.id.userId);
            _empStatus = itemView.findViewById(R.id.txt_Status);
            profileProgressCount = itemView.findViewById(R.id.profileProgressCount);
            profileProgressBar = itemView.findViewById(R.id.profileProgressBar);


            if (!(context instanceof EmployeeSelector)) {
                employee_item.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onClick(employeeModelList.get(getAdapterPosition()));
                        return;
                    }
                    if (resignType == 1) {
                        if (status == 1) {
                            Intent dashboardIntent = new Intent(context, ResignationApproval.class);
                            dashboardIntent.putExtra("empDetailId", employeeModelList.get(getAdapterPosition()).getId());
                            context.startActivity(dashboardIntent);
                            ((Activity) context).finish();
                        }

                    } else if (resignType == 2) {
                        if (status == 1) {
                            Intent dashboardIntent = new Intent(context, TerminationApproval.class);
                            dashboardIntent.putExtra("empDetailId", employeeModelList.get(getAdapterPosition()).getId());
                            context.startActivity(dashboardIntent);
                            ((Activity) context).finish();
                        }

                    }

                    else if (isLeaveApproval) {
                        if (status == 1) {
                            /*Intent intent = new Intent(context, LeaveApprovalActivity.class);
                            intent.putExtra("empDetailId", employeeModelList.get(getAdapterPosition()).getId());
                            intent.putExtra("empLeaveId", employeeModelList.get(getAdapterPosition()).getEmployeeLeaveModel().getEmployee_Leave_ID());
                            intent.putExtra("schoolId", schoolId);
                            context.startActivity(intent);*/
                        }
                    } else {
                        Intent intent = new Intent(context, EmployeeDetailsActivity.class);
                        intent.putExtra("empDetailId", employeeModelList.get(getAdapterPosition()).getId());
                        intent.putExtra("schoolId", schoolId);
                        context.startActivity(intent);
                    }

                });
            } else {
                employee_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //pop stack
                        try {
                            Intent intent = new Intent(context, SurveyAppModel.getInstance().selectedProject
                                    .setSelectedEmployeeId(employeeModelList.get(getAdapterPosition()).getId())
                                    .popActivityFromStack());
//                            intent.putExtra("empDetailId", employeeModelList.get(getAdapterPosition()).getId());
//                            intent.putExtra("schoolId", schoolId);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            employee_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    openBottomSheet(getAdapterPosition(),iv_capture);
                    return true;
                }
            });
        }

    }

    private void openBottomSheet(int position,ImageView iv_capture) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.bottomsheet_layout);
        TextView tv_call, tv_sms, tv_upload_profile_image, tv_update_profile_info;
        tv_call = bottomSheetDialog.findViewById(R.id.tv_call);
        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                context.startActivity(intent);
            }
        });

        tv_sms = bottomSheetDialog.findViewById(R.id.tv_sms);
        tv_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();

                /*Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
                context.startActivity(intent);*/

                String defaultSmsPackage = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                        ? Telephony.Sms.getDefaultSmsPackage(context)
                        : Settings.Secure.getString(context.getContentResolver(), "sms_default_application");

                Intent smsIntent = context.getPackageManager().getLaunchIntentForPackage(defaultSmsPackage);

                if (smsIntent == null) {
                    smsIntent = new Intent(Intent.ACTION_MAIN);
                    smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                }

                try {
                    context.startActivity(smsIntent);
                } catch (Exception e) {
                    Log.w("ass", "Could not open SMS app", e);

                    // Inform user
                    Toast.makeText(
                            context,
                            "Your SMS app could not be opened. Please open it manually.",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });

        tv_upload_profile_image = bottomSheetDialog.findViewById(R.id.tv_upload_profile_image);
        tv_upload_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                pos = position;
                if (DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getId() == employeeModelList.get(position).getId() ||
                        DatabaseHelper.getInstance(context).getCurrentLoggedInUser().getRoleId() == AppConstants.roleId_27_P) {

                    try {
                        PickImageDialog.build(new PickSetup().setTitle("Profile Image")
                                .setPickTypes(EPickType.CAMERA, EPickType.GALLERY)).show((FragmentActivity) context).setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {

                                try {
                                    if (r.getError() == null) {
                                        CropImage.activity(r.getUri())
                                                .setGuidelines(CropImageView.Guidelines.ON)
                                                .start((Activity) context);
                                    } else {
                                        Toast.makeText(context, r.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                    AppModel.getInstance().appendErrorLog(context,"Error in Imageview:" + e.getMessage());
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        AppModel.getInstance().appendErrorLog(context,"Error in Imageview:" + e.getMessage());
                    }

                } else {
                    previewImage(iv_capture.getDrawable());
                }
            }
        });

        tv_update_profile_info = bottomSheetDialog.findViewById(R.id.tv_update_profile_info);
        tv_update_profile_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.employee_profile_update_dialog, null);
                //cancelReason = dialogView.findViewById(R.id.edt_comment);
                mobileNumber = (EditText) dialogView.findViewById(R.id.et_MobileNumber);
                email = dialogView.findViewById(R.id.et_Email);

                final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(dialogView)
                        .setTitle("Edit Profile")
                        //.setMessage("Are you sure you want to cancel the resignation of " +employeeModel.getFirst_Name() + " " + employeeModel.getLast_Name())
                        .setPositiveButton("Update", null) //Set to null. We override the onclick
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

                        try {
                            if (employeeModelList.get(position).getMobile_No() != null) {
                                if (!employeeModelList.get(position).getMobile_No().isEmpty()) {
                                    mobileNumber.setText(employeeModelList.get(position).getMobile_No());
                                }
                            }

                            if (employeeModelList.get(position).getEmail() != null) {
                                if (!employeeModelList.get(position).getEmail().isEmpty()) {
                                    email.setText(employeeModelList.get(position).getEmail());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        button.setOnClickListener(new View.OnClickListener() {


                            @Override
                            public void onClick(View view) {

                                em.setId(employeeModelList.get(position).getId());

                                if (validate()) {
                                    long id = EmployeeHelperClass.getInstance(context).updateEmployeeDetail(em);
                                    if (id > 0) {
                                        //Important when any change in table call this method
                                        AppModel.getInstance().changeMenuPendingSyncCount(context, true);

                                        /*Intent leave_intent = new Intent(context, EmployeeDetailsActivity.class);
                                        leave_intent.putExtra("empDetailId", employeeModelList.get(position).getId());
                                        MessageBox("Employee updated successfully", true, leave_intent);*/
                                        if (employeeModelList.get(position).getMobile_No() != null) {
                                            if (!employeeModelList.get(position).getMobile_No().isEmpty()) {
                                                mobileNumber.setText(employeeModelList.get(position).getMobile_No());
                                            }
                                        }

                                        if (employeeModelList.get(position).getEmail() != null) {
                                            if (!employeeModelList.get(position).getEmail().isEmpty()) {
                                                email.setText(employeeModelList.get(position).getEmail());

                                            }
                                        }

                                        MessageBox("Employee updated successfully");


                                    } else {
                                        MessageBox("Something went wrong");
                                    }

                                    dialog.dismiss();

                                }
                            }
                        });
                    }
                });
                dialog.show();
            }
        });

        bottomSheetDialog.show();
    }

    private boolean validate() {
        int allOk = 0;

        if (!mobileNumber.getText().toString().trim().isEmpty()) {
            String mobNum = mobileNumber.getText().toString();
            if (mobNum.length() == 11 && mobNum.substring(0, 2).equals("03")) {
                em.setMobile_No(mobNum);
            } else {
                mobileNumber.setError("Please Enter Valid Mobile Number");
                allOk++;
            }
        } else {
            em.setMobile_No("");
        }

        if (!email.getText().toString().trim().isEmpty()) {
            String strEmail = email.getText().toString();
            if (strEmail.length() >= 5 && strEmail.contains(".") && strEmail.contains("@")) {
                em.setEmail(strEmail);
            } else {
                email.setError("Please Enter Valid Email Address");
                allOk++;
            }
        } else {
            em.setEmail("");
        }

        return allOk == 0;
    }

    public void MessageBox(String message) {
        MessageBox(message, false, null);
    }

    public void MessageBox(String message, final boolean finishOnClose, final Intent intentToStart) {
        android.app.AlertDialog msg = new android.app.AlertDialog.Builder(context).create();
//        msg.setTitle("Warning");
        msg.setCancelable(false);
        msg.setMessage(message);
        msg.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (intentToStart != null) context.startActivity(intentToStart);
                if (finishOnClose) ((Activity)context).finish();;
            }
        });
        msg.show();
    }

    public void ShowToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void previewImage(Drawable previewImageFile) {
        ImagePreviewDialog imagePreviewDialog = new ImagePreviewDialog(context, previewImageFile);
        imagePreviewDialog.show();
    }

   /* class EmployeeDetailHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iv_empCode, iv_empName, iv_empJD, iv_empCadre, iv_empLWD, iv_empDes, iv_empStatus;
        private LinearLayout ll_empCode, ll_empName, ll_empJD, ll_empCadre, ll_empLWD, ll_empDes, ll_empStatus;

        EmployeeDetailHeaderHolder(@NonNull View itemView) {
            super(itemView);
            iv_empCode = itemView.findViewById(R.id.iv_empCode);
            iv_empName = itemView.findViewById(R.id.iv_empName);
            iv_empJD = itemView.findViewById(R.id.iv_empJD);
            iv_empCadre = itemView.findViewById(R.id.iv_empCadre);
            iv_empLWD = itemView.findViewById(R.id.iv_empLWD);
            iv_empDes = itemView.findViewById(R.id.iv_empDes);
            iv_empStatus = itemView.findViewById(R.id.iv_empStatus);

            ll_empCode = itemView.findViewById(R.id.ll_empCode);
            ll_empName = itemView.findViewById(R.id.ll_empName);
            ll_empJD = itemView.findViewById(R.id.ll_empJD);
            ll_empCadre = itemView.findViewById(R.id.ll_empCadre);
            ll_empLWD = itemView.findViewById(R.id.ll_empLWD);
            ll_empDes = itemView.findViewById(R.id.ll_empDes);
            ll_empStatus = itemView.findViewById(R.id.ll_empStatus);

            ll_empCode.setOnClickListener(this);
            ll_empName.setOnClickListener(this);
            ll_empJD.setOnClickListener(this);
            ll_empCadre.setOnClickListener(this);
            ll_empLWD.setOnClickListener(this);
            ll_empDes.setOnClickListener(this);
            ll_empStatus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ll_empCode) {
                if (iv_empCode.getDrawable().getConstantState() == iv_empCode.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_empCode.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(1);
                } else {
                    iv_empCode.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(1);
                }
            } else if (v.getId() == R.id.ll_empName) {
                if (iv_empName.getDrawable().getConstantState() == iv_empName.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_empName.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(2);
                } else {
                    iv_empName.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(2);
                }
            } else if (v.getId() == R.id.ll_empJD) {
                if (iv_empJD.getDrawable().getConstantState() == iv_empJD.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_empJD.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(3);
                } else {
                    iv_empJD.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(3);
                }
            } else if (v.getId() == R.id.ll_empCadre) {
                if (iv_empCadre.getDrawable().getConstantState() == iv_empCadre.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_empCadre.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(4);
                } else {
                    iv_empCadre.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(4);
                }
            } else if (v.getId() == R.id.ll_empDes) {
                if (iv_empDes.getDrawable().getConstantState() == iv_empDes.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_empDes.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(5);
                } else {
                    iv_empDes.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(5);
                }
            } else if (v.getId() == R.id.ll_empLWD) {
                if (iv_empLWD.getDrawable().getConstantState() == iv_empLWD.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_empLWD.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(6);
                } else {
                    iv_empLWD.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(6);
                }
            } else if (v.getId() == R.id.ll_empStatus) {
                if (iv_empStatus.getDrawable().getConstantState() == iv_empStatus.getResources()
                        .getDrawable(android.R.drawable.arrow_up_float).getConstantState()) {
                    iv_empStatus.setImageResource(android.R.drawable.arrow_down_float);
                    Sort_In_Des(7);
                } else {
                    iv_empStatus.setImageResource(android.R.drawable.arrow_up_float);
                    Sort_In_Asc(7);
                }
            }
        }

        private void Sort_In_Asc(int f) {
            if (f == 1) {        //Emp Code
                Collections.sort(employeeModelList, (em1, em2) -> {
                    if (em1.getEmployee_Code() != null && em2.getEmployee_Code() != null) {
                        return Integer.compare(Integer.parseInt(em1.getEmployee_Code()), Integer.parseInt(em2.getEmployee_Code()));
                    }
                    return -1;
                });
            } else if (f == 2) {      //Emp Name
                Collections.sort(employeeModelList, (em1, em2) -> {
                    return em1.getFirst_Name().compareTo(em2.getFirst_Name());
                });
            } else if (f == 3) {      //Emp Join Date
                Collections.sort(employeeModelList, (em1, em2) -> sortDateUsing(em1.getDate_Of_Joining(), em2.getDate_Of_Joining(), "asc"));
            } else if (f == 4) {      //Emp Cadre
                Collections.sort(employeeModelList, (em1, em2) -> {
                    return em1.getCADRE().compareTo(em2.getCADRE());
                });
            } else if (f == 5) {      //Emp Designation
                Collections.sort(employeeModelList, (em1, em2) -> {
                    return em1.getDesignation().compareTo(em2.getDesignation());
                });
            } else if (f == 6) {      //Emp Last Working Day
                Collections.sort(employeeModelList, (em1, em2) -> sortDateUsing(em1.getLastWorkingDay(), em2.getLastWorkingDay(), "asc"));
            } else if (f == 7) {      //Emp Status
                Collections.sort(employeeModelList, (em1, em2) -> {
                    return Boolean.compare(em1.getIs_Active(), em2.getIs_Active());
                });
            }

            notifyDataSetChanged();
        }

        private void Sort_In_Des(int f) {
            if (f == 1) {       //Emp Code
                Collections.sort(employeeModelList, (em1, em2) -> {
                    if (em1.getEmployee_Code() != null && em2.getEmployee_Code() != null) {
                        return Integer.compare(Integer.parseInt(em2.getEmployee_Code()), Integer.parseInt(em1.getEmployee_Code()));
                    }
                    return -1;
                });
            } else if (f == 2) {      //Emp Name
                Collections.sort(employeeModelList, (em1, em2) -> {
                    return em2.getFirst_Name().compareTo(em1.getFirst_Name());
                });
            } else if (f == 3) {      //Emp Join Date
                Collections.sort(employeeModelList, (em1, em2) -> sortDateUsing(em2.getDate_Of_Joining(), em1.getDate_Of_Joining(), "des"));
            } else if (f == 4) {      //Emp Cadre
                Collections.sort(employeeModelList, (em1, em2) -> {
                    return em2.getCADRE().compareTo(em1.getCADRE());
                });
            } else if (f == 5) {      //Emp Designation
                Collections.sort(employeeModelList, (em1, em2) -> {
                    return em2.getDesignation().compareTo(em1.getDesignation());
                });
            } else if (f == 6) {      //Emp Last Working Day
                Collections.sort(employeeModelList, (em1, em2) -> sortDateUsing(em2.getLastWorkingDay(), em1.getLastWorkingDay(), "des"));
            } else if (f == 7) {      //Emp Status
                Collections.sort(employeeModelList, (em1, em2) -> {
                    return Boolean.compare(em2.getIs_Active(), em1.getIs_Active());
                });
            }

            notifyDataSetChanged();
        }

        private int sortDateUsing(String Date, String Date1, String order) {
            Date date = null, date1 = null;

            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            try {
                if (Date != null && Date1 != null && !Date.isEmpty() && !Date1.isEmpty()) {
                    date = formatDate.parse(Date);
                    date1 = formatDate.parse(Date1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date != null && date1 != null) {
                return date.compareTo(date1);
            }
            return 0;
        }
    }*/
}
