package com.tcf.sma.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tcf.sma.Managers.StudentTransferBottomSheetDialog;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.StudentTransferModel;
import com.tcf.sma.R;
import com.tcf.sma.Retrofit.ApiClient;
import com.tcf.sma.Retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Response;

public class StudentSearchForTransferActivity extends DrawerActivity implements View.OnClickListener {

    private View view;
    private EditText et_stdId, et_GrNo;
    private Button btnsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_search_for_transfer);
        view = setActivityLayout(this, R.layout.activity_student_search_for_transfer);
        setToolbar(getString(R.string.student_transfer_text), this, false);

        init(view);
    }

    private void init(View view) {
        et_stdId = view.findViewById(R.id.et_stdId);
        et_GrNo = view.findViewById(R.id.et_GrNo);
        btnsearch = view.findViewById(R.id.btnsearch);
        btnsearch.setOnClickListener(this);
    }


    public void getSingleStudent(int studentId, String grNo) {
        Runnable runnable = () -> AppModel.getInstance().hideLoader();
        try {
            AppModel.getInstance().appendLog(view.getContext(), "\nGetting Single Student Record For Transfer");
            String token = AppModel.getInstance().getToken(view.getContext());
            token = "Bearer " + token;
            ApiInterface apiInterface = ApiClient.getClient(view.getContext()).create(ApiInterface.class);
            Call<StudentTransferModel> call = apiInterface.getSingleStudentForTransfer(studentId, grNo, token);
            Response<StudentTransferModel> response = call.execute();
            if (response.isSuccessful()) {
                AppModel.getInstance().appendLog(view.getContext(), "Single Student for transfer api Called got Response code :" + response.code() + "\n");
                StudentTransferModel sm = response.body();

                if (sm.isActive() == null)
                    sm.setActive(true);
                else {
                    boolean isActive = sm.isActive();
                    sm.setActive(!isActive);
                }

//                if (sm == null) {
//                    runOnUiThread(() -> MessageBox("No student found"));
//
//                }
                if (sm.isActive() && !sm.isWithdrawal()) {
                    runOnUiThread(() -> MessageBox(sm.getStd_Name() + " is active in school " + sm.getSchoolId() + "-" + sm.getSchool_name() +
                            "\nPlease withdraw/graduate this student first to transfer"));

                } else {
                    StudentTransferBottomSheetDialog studentBottomSheetDialog = new StudentTransferBottomSheetDialog();
                    studentBottomSheetDialog.show(getSupportFragmentManager(), "studentbottomsheet");
                    studentBottomSheetDialog.setStudentDetails(sm);

//                    Intent intent = new Intent(StudentSearchForTransferActivity.this, StudentTransferActivity.class);
//                    intent.putExtra("studentModel", sm);
//                    startActivity(intent);
                }
            } else {
                if (response.body() == null) {
                    runOnUiThread(() -> MessageBox("No student found"));
                } else {
                    AppModel.getInstance().appendErrorLog(view.getContext(), "Getting single student for transfer Called got Response code :" + response.code() + " Message " + response.message() + "\n");
                    runOnUiThread(() -> MessageBox("Getting single student for transfer Called got Response code :" + response.code() + " Message " + response.message()));
                }
            }

            runOnUiThread(runnable);

        } catch (Exception e) {
            e.printStackTrace();
            AppModel.getInstance().appendErrorLog(view.getContext(), "In getSingleStudentMethod for transfer student Exception Occurred: " + e.getMessage());
            runOnUiThread(runnable);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnsearch) {
            searchClicked();
        }
    }

    private void searchClicked() {
        String grNo = et_GrNo.getText().toString();
        String stdID = et_stdId.getText().toString();
        boolean isStudentFound = false;
        if (validate()) {
            try {
                if (!AppModel.getInstance().isConnectedToInternet(this)) {
                    MessageBox("No internet connectivity!\n Please connect to internet");
                } else {
                    AppModel.getInstance().showLoader(this, "Searching", "Please wait...");
                    new Thread(() -> getSingleStudent(Integer.parseInt(stdID), grNo)).start();
                }
//                List<SchoolModel> smList = SurveyAppModel.getInstance().getSchoolsForLoggedInUser(this);
//                for (SchoolModel model : smList) {
//                    ArrayList<StudentModel> studentModels = DatabaseHelper.getInstance(this).getStudentUsing(model.getId(), grNo, stdID,false);
//                    if (studentModels != null && studentModels.size() > 0) {
//
//                        StudentModel studentModel = studentModels.get(0);
//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//                        alertDialog.setTitle("Alert");
//                        alertDialog.setMessage(studentModel.getName() + " is already in school " +
//                                model.getId() + "-" + model.getName() + ". \nDo you want to ReAdmit this student?");
//                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                StudentModel.getInstance().setStudentsList(studentModels);
//                                Intent readmissionIntent = new Intent(StudentSearchForTransferActivity.this, StudentReadmissionActivity.class);
//                                readmissionIntent.putExtra("StudentEditIndex", 0);
//                                readmissionIntent.putExtra("SchoolId", model.getId());
//                                startActivity(readmissionIntent);
//                            }
//                        });
//                        alertDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
//                        alertDialog.create().show();
//
//                        isStudentFound = true;
//                        break;
//                    } else {
//                        isStudentFound = false;
//                    }
//                }
//
//                if (!isStudentFound){
//                    StudentModel.getInstance().setStudentsList(null);
//                    if (!SurveyAppModel.getInstance().isConnectedToInternet(this)) {
//                        MessageBox("No internet connectivity!\n Please connect to internet");
//                    } else {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                getSingleStudent(Integer.parseInt(stdID), grNo);
//                            }
//                        }).start();
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validate() {
        int i = 0;
        if (et_GrNo.getText().toString().isEmpty()) {
            i++;
            Toast.makeText(this, "Please enter Gr No.", Toast.LENGTH_SHORT).show();
            et_GrNo.setError("Required");
        } else if (et_stdId.getText().toString().isEmpty()) {
            i++;
            Toast.makeText(this, "Please enter student id", Toast.LENGTH_SHORT).show();
            et_stdId.setError("Required");
        }


        return i == 0;
    }

}
