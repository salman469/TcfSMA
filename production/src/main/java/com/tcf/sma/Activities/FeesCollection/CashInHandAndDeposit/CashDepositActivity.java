package com.tcf.sma.Activities.FeesCollection.CashInHandAndDeposit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.gms.common.util.Strings;
import com.tcf.sma.Activities.DrawerActivity;
import com.tcf.sma.Helpers.DatabaseHelper;
import com.tcf.sma.Helpers.DbTables.FeesCollection.CashDeposit;
import com.tcf.sma.Helpers.DbTables.FeesCollection.FeesCollection;
import com.tcf.sma.Managers.StorageUtil;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.Fees_Collection.CashDepositModel;
import com.tcf.sma.Models.Fees_Collection.CashInHandModel;
import com.tcf.sma.Models.Fees_Collection.FeesHeaderModel;
import com.tcf.sma.Models.SchoolModel;
import com.tcf.sma.R;
import com.tcf.sma.utils.ImageCompression;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Added By Mohammad Haseeb
 */
public class CashDepositActivity extends DrawerActivity implements View.OnClickListener, IPickResult {
    private static final int CAMERA_REQUEST = 1888;
    View screen;
    private Button btnOk, btnCancle;
    private TextView tvTodayDate, tvAdmissionFees, tvExamFees, tvMonthlyFees, tvBooks, tvCopies, tvUniform, tvOthers, tvTotal, tvReconcileAmount;
    private EditText etSlipNo;
    private CashInHandModel cashInHandModel;
    private AppCompatImageView ivCaptureImage;
    private File image = null;
    private Bitmap capturedImage;
    private String captureImagePath = "";
    private Uri mUri;
    private EditText et_remarks;
    private ProgressDialog progressDialog;
    private String cashDepositImagePath;

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        screen = setActivityLayout(this, R.layout.activity_cash_deposit);
        setToolbar("Cash Deposit", this, false);
        init();
        handleIntent(getIntent());
        setUIFields();

        if (savedInstanceState != null) {
            mUri = savedInstanceState.getParcelable("uri");
            ivCaptureImage.setImageURI(mUri);

//            capturedImage = (Bitmap) savedInstanceState.getParcelable("capImage");
//            captureImagePath = (String) savedInstanceState.getString("captureImagePath");
//            try {
//
//                ImageCompression imageCompression = new ImageCompression(CashDepositActivity.this);
//
//                imageCompression.execute(captureImagePath);
//
//                image = new File(captureImagePath);
//
//                ivCaptureImage.setImageBitmap(capturedImage);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } else {
            ivCaptureImage.setImageResource(R.drawable.ic_camera);
//            capturedImage = null;
//            captureImagePath = "";
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mUri != null) {
            outState.putParcelable("uri", mUri);
        }

//        if (capturedImage != null)
//            outState.putParcelable("capImage", capturedImage);
//        if (captureImagePath != null)
//            outState.putString("captureImagePath", captureImagePath);
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.hasExtra("CashInHand"))
            cashInHandModel = (CashInHandModel) intent.getSerializableExtra("CashInHand");
    }

    public void init() {
        etSlipNo = (EditText) screen.findViewById(R.id.etSlipNo);
        tvTodayDate = (TextView) screen.findViewById(R.id.tvTodayDate);
        tvAdmissionFees = (TextView) screen.findViewById(R.id.tvAdmissionFees);
        tvExamFees = (TextView) screen.findViewById(R.id.tvExamFees);
        tvMonthlyFees = (TextView) screen.findViewById(R.id.tvMonthlyFees);
        tvBooks = (TextView) screen.findViewById(R.id.tvBooks);
        tvCopies = (TextView) screen.findViewById(R.id.tvCopies);
        tvUniform = (TextView) screen.findViewById(R.id.tvUniform);
        tvOthers = (TextView) screen.findViewById(R.id.tvOthers);
        tvTotal = (TextView) screen.findViewById(R.id.tvTotal);
        et_remarks = (EditText) screen.findViewById(R.id.et_remarks);
        tvReconcileAmount = screen.findViewById(R.id.tvBlncAfterReConciliation);

        btnOk = (Button) screen.findViewById(R.id.btnOk);
        btnCancle = (Button) screen.findViewById(R.id.btnCancle);
        ivCaptureImage = (AppCompatImageView) screen.findViewById(R.id.ivCaptureImage);
        btnOk.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        ivCaptureImage.setOnClickListener(this);

    }

    private void setUIFields() {

        tvTodayDate.setText(AppModel.getInstance().convertDatetoFormat(cashInHandModel.getTodayDate(), "dd-MM-yy", "dd-MMM-yy"));
//        tvTodayDate.setText(cashInHandModel.getTodayDate());
        if (cashInHandModel.getAdmissionFees() != null) {
            tvAdmissionFees.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getAdmissionFees())));
        } else {
            tvAdmissionFees.setText("0");
        }
        if (cashInHandModel.getExamFees() != null) {
            tvExamFees.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getExamFees())));
        } else {
            tvExamFees.setText("0");
        }
        if (cashInHandModel.getMonthlyFees() != null) {
            tvMonthlyFees.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getMonthlyFees())));
        } else {
            tvMonthlyFees.setText("0");
        }
        if (cashInHandModel.getBooks() != null) {
            tvBooks.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getBooks())));
        } else {
            tvBooks.setText("0");
        }
        if (cashInHandModel.getCopies() != null) {
            tvCopies.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getCopies())));
        } else {
            tvCopies.setText("0");
        }
        if (cashInHandModel.getUniforms() != null) {
            tvUniform.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getUniforms())));
        } else {
            tvUniform.setText("0");
        }
        if (cashInHandModel.getOthers() != null) {
            tvOthers.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getOthers())));
        } else {
            tvOthers.setText("0");
        }
        if (cashInHandModel.getTotal() != null) {
            tvTotal.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getTotal())));
        } else {
            tvTotal.setText("0");
        }
        /*if (cashInHandModel.getReconcileAmount() != null) {
            tvReconcileAmount.setText(AppModel.getInstance().formatNumberInCommas(Long.valueOf(cashInHandModel.getReconcileAmount())));
        } else {
            tvReconcileAmount.setText("0");
        }*/
        tvReconcileAmount.setText("0");

    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            ivCaptureImage.setImageBitmap(photo);
//
//            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//            Uri tempUri = getImageUri(getApplicationContext(), photo);
//
//            // CALL THIS METHOD TO GET THE ACTUAL PATH
//            image = new File(getRealPathFromURI(tempUri));
//
////            Bitmap bitmap = SurveyAppModel.getInstance().rotateImage(Compressor.getDefault(this).compressToBitmap(image), getWindowManager().getDefaultDisplay());
//
//        }
//    }
//
//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
////        ImageCompression imageCompression = new ImageCompression(CashDepositActivity.this);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//
////        imageCompression.execute(path);
//        return Uri.parse(path);
//    }
//
//    public String getRealPathFromURI(Uri uri) {
//        String path = "";
//        if (getContentResolver() != null) {
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                path = cursor.getString(idx);
//                cursor.close();
//            }
//        }
//        return path;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                DepositCashInBank();
                break;
            case R.id.btnCancle:
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to cancel?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                clearFields();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.ivCaptureImage:
//                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, CAMERA_REQUEST);
                try {
                    PickImageDialog.build(new PickSetup().setTitle("")
                            .setPickTypes(EPickType.CAMERA)).show(CashDepositActivity.this).setOnPickResult(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void clearFields() {
        image = null;
        ivCaptureImage.setImageResource(R.drawable.ic_camera);
//        ivCaptureImage.setRotation(0);
        etSlipNo.setText("");
    }

    private void enableDepositButton(boolean enabled) {
        btnOk.setEnabled(enabled);
        btnOk.setClickable(enabled);
    }

    private void showLoader(Context context, String title, String message) {
        try {
            if (progressDialog == null || !progressDialog.isShowing()) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle(title);
                progressDialog.setMessage(message);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideLoader() {
        progressDialog.dismiss();
    }

    private void DepositCashInBank() {

        enableDepositButton(false);
        showLoader(this, "Cash is depositing in bank", "Please wait...");

        if (validate()) {
            if (depositSlipValid()) {
                new Thread(() -> {
                    ArrayList<SchoolModel> schoolModels = AppModel.getInstance().getSchoolsForLoggedInUser(CashDepositActivity.this);
                    ArrayList<CashDepositModel> modelList = new ArrayList<>();
                    for (SchoolModel smModel : schoolModels) {
                        List<String> allowedModules = null;
                        if (smModel.getAllowedModule_App() != null){
                            allowedModules = Arrays.asList(smModel.getAllowedModule_App().split(","));
                        }
                        if (allowedModules != null && allowedModules.contains(AppConstants.FinanceModuleValue)) {
//                            String cashInHand = FeesCollection.getInstance(CashDepositActivity.this).getCashInHandData(String.valueOf(smModel.getId())).getTotal();

                            String cashInHand = String.valueOf(FeesCollection.getInstance(this).getCashInHand(smModel.getId(), DatabaseHelper.getInstance(this).getAcademicSessionId(schoolModels.get(0).getId())));

                            if (cashInHand != null && !cashInHand.isEmpty() && !cashInHand.equals("0")) {
                                CashDepositModel model = new CashDepositModel();
                                model.setCreatedBy(String.valueOf(DatabaseHelper.getInstance(CashDepositActivity.this).getCurrentLoggedInUser().getId()));
                                model.setCreatedOn(AppModel.getInstance().getCurrentDateTime("yyyy-MM-dd hh:mm:ss a"));
                                model.setSchoolId(smModel.getId());
                                model.setDepositSlipNo(etSlipNo.getText().toString().trim());
                                model.setDepositAmount(Double.valueOf(cashInHand));
                                model.setDepositSlipFilePath(cashDepositImagePath);
//                                model.setDepositSlipFilePath(image.getAbsolutePath());
                                model.setDeviceId(AppModel.getInstance().getDeviceId(CashDepositActivity.this));
                                model.setRemarks(et_remarks.getText().toString());

                                modelList.add(model);

                                /*long id = DatabaseHelper.getInstance(CashDepositActivity.this).insertIntoCashDeposit(model);
                                if (id > 0) {
//                    AppReceipt.getInstance(CashDepositActivity.this).updateAppReceiptForCashDeposit((int) id);
                                    boolean isFeesHeaderUpdatedForCashDeposit = FeesCollection.getInstance(CashDepositActivity.this).updateAppReceiptForCashDeposit(smModel.getId(), (int) id);
                                    if (isFeesHeaderUpdatedForCashDeposit) {
                                        FeesCollection.getInstance(CashDepositActivity.this).insertIntoErrorLog(model.getSchoolId(), id);
                                        runOnUiThread(() -> {
                                            Toast.makeText(CashDepositActivity.this, "Cash deposited in bank successfully", Toast.LENGTH_SHORT).show();
                                            hideLoader();
                                        });

                                        //Important when any change in table call this method
                                        AppModel.getInstance().changeMenuPendingSyncCount(CashDepositActivity.this, true);

                                        Intent intent = new Intent(CashDepositActivity.this, CashInHandActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        AppModel.getInstance().appendErrorLog(CashDepositActivity.this, "Cash deposit id =" + id + " not updated in fees header table after submitting cash deposit");
                                        runOnUiThread(() -> {
                                            Toast.makeText(CashDepositActivity.this, "Cash deposit id not updated in fees header table", Toast.LENGTH_LONG).show();
                                            enableDepositButton(true);
                                            hideLoader();
                                        });

                                    }
                                }*/
                            }
                        }
                    }

                    String maxCreatedOnOfCashDeposit = FeesCollection.getInstance(this).getMaxCreatedOnOfCashDeposit();
                    for (CashDepositModel model : modelList) {
                        String ids = "";
                        if (!Strings.isEmptyOrWhitespace(maxCreatedOnOfCashDeposit)) {
                            for (FeesHeaderModel feesHeaderModel : FeesCollection.getInstance(this).getCashInHandDataAfterMaxDeposit(model.getSchoolId(),
                                    DatabaseHelper.getInstance(this).getAcademicSessionId(model.getSchoolId()),
                                    maxCreatedOnOfCashDeposit)) {
                                if (Strings.isEmptyOrWhitespace(ids))
                                    ids += feesHeaderModel.getId() + "";
                                else
                                    ids += "," + feesHeaderModel.getId();
                            }
                        }
                        long id = DatabaseHelper.getInstance(CashDepositActivity.this).insertIntoCashDeposit(model);
                        if (id > 0) {
//                    AppReceipt.getInstance(CashDepositActivity.this).updateAppReceiptForCashDeposit((int) id);
                            boolean isFeesHeaderUpdatedForCashDeposit = false;
                            if (!Strings.isEmptyOrWhitespace(maxCreatedOnOfCashDeposit)) {
                                isFeesHeaderUpdatedForCashDeposit = FeesCollection.getInstance(CashDepositActivity.this).updateAppReceiptForCashDeposit((int) id, ids);

                            } else {
                                isFeesHeaderUpdatedForCashDeposit = FeesCollection.getInstance(CashDepositActivity.this).updateAppReceiptForCashDeposit(model.getSchoolId(), (int) id);
                            }
                            if (isFeesHeaderUpdatedForCashDeposit) {
                                FeesCollection.getInstance(CashDepositActivity.this).insertIntoErrorLog(model.getSchoolId(), id);
                                runOnUiThread(() -> {
                                    Toast.makeText(CashDepositActivity.this, "Cash deposited in bank successfully", Toast.LENGTH_SHORT).show();
                                    hideLoader();
                                });

                                //Important when any change in table call this method
                                AppModel.getInstance().changeMenuPendingSyncCount(CashDepositActivity.this, true);

                                Intent intent = new Intent(CashDepositActivity.this, CashInHandActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                AppModel.getInstance().appendErrorLog(CashDepositActivity.this, "Cash deposit id =" + id + " not updated in fees header table after submitting cash deposit");
                                runOnUiThread(() -> {
                                    Toast.makeText(CashDepositActivity.this, "Cash deposit id not updated in fees header table", Toast.LENGTH_LONG).show();
                                    enableDepositButton(true);
                                    hideLoader();
                                });

                            }
                        }
                    }
                }).start();
            } else {
                Toast.makeText(this, "Deposit slip already inserted", Toast.LENGTH_SHORT).show();
                enableDepositButton(true);
                hideLoader();
            }

        } else {
            enableDepositButton(true);
            hideLoader();
        }
    }

    private boolean depositSlipValid() {
        return !CashDeposit.getInstance(this).ifDepositSlipExist(etSlipNo.getText().toString().trim());
    }

    /**
     * Method to validate user inputs
     *
     * @return
     */
    private boolean validate() {
        if (TextUtils.isEmpty(etSlipNo.getText().toString().trim())) {
            etSlipNo.setError("Slip Sr.No is required");
            etSlipNo.requestFocus();
            return false;
        } else if (etSlipNo.getText().toString().trim().startsWith("0")) {
            etSlipNo.setError("Slip Sr.No should not starts with zero");
            etSlipNo.requestFocus();
            Toast.makeText(this, "Slip Sr.No should not starts with zero", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(tvTodayDate.getText().toString().trim())) {
            tvTodayDate.setError("Today date is required");
            tvTodayDate.requestFocus();
            return false;
        }
        if (image == null) {
            Toast.makeText(CashDepositActivity.this, "Please take image of deposit slip", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cashInHandModel.getTodayDate() != null) {

        }
        return true;
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if (pickResult.getError() == null) {

            ImageCompression imageCompression = new ImageCompression(CashDepositActivity.this);

            String path = pickResult.getPath();

            imageCompression.execute(path);

            image = new File(path);

            Bitmap rotatedImage = setOrientation(pickResult.getBitmap(), path);

            Uri targetUri = AppModel.getInstance().getImageUri(getApplicationContext(), rotatedImage, image);
//            Uri targetUri = Uri.fromFile(image);
            mUri = targetUri;

//            capturedImage = rotatedImage;
//            captureImagePath = path;

            ivCaptureImage.setImageBitmap(rotatedImage);
//            ivCaptureImage.setImageBitmap(pickResult.getBitmap());
//            ivCaptureImage.setRotation(90);

            byte[] cashDepositImage = AppModel.getInstance().bitmapToByte(rotatedImage);
            cashDepositImagePath = saveImageToStorage2(cashDepositImage, this, image.getName());

        } else {
            Toast.makeText(CashDepositActivity.this, pickResult.getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap setOrientation(Bitmap bitmap, String path) {
        ExifInterface ei = null;
        Bitmap rotatedBitmap = null;
        try {
            ei = new ExifInterface(path);

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {

//            case ExifInterface.ORIENTATION_ROTATE_90:
//                rotatedBitmap = rotateImage(bitmap, 90);
//                break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

//            case ExifInterface.ORIENTATION_ROTATE_270:
//                rotatedBitmap = rotateImage(bitmap, 270);
//                break;

                case ExifInterface.ORIENTATION_NORMAL:
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        rotatedBitmap = rotateImage(bitmap, 90);
                        // Portrait Mode
                    } else {
                        // Landscape Mode
                        rotatedBitmap = bitmap;
                    }
                    break;
                default:
                    rotatedBitmap = bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rotatedBitmap;
    }

    private String saveImageToStorage2(byte[] data, Context context, String fileName) {
        String path = null;
        if (data != null) {
            File folder = new File(StorageUtil.getSdCardPath(context) +
                    File.separator + "TCF" + File.separator + "TCF Images");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                try {
                    File file = new File(folder, fileName);
                    FileOutputStream outStream = new FileOutputStream(file);
                    outStream.write(data);
                    outStream.close();
                    path = folder.getAbsolutePath() + File.separator + fileName;

                } catch (Exception e) {
                    Log.d("asdf", "adf");
                }
            } else {
                Log.d("file", "error");
            }
        }
        return path;
    }
}
