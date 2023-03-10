package com.tcf.sma.Adapters.Expense;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.Expense.AddExpenseFragment;
import com.tcf.sma.Activities.Expense.TransferFragment;
import com.tcf.sma.Managers.ImagePreviewDialog;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.Expense.SlipModel;
import com.tcf.sma.R;
import com.tcf.sma.Services.BasicImageDownloader;
import com.tcf.sma.utils.ImageCompression;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

public class ExpenseAttachmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> expenseAttachments;
    private boolean isUpdate;
    public static List<SlipModel> paths = new ArrayList<>();
    public static List<String> slip_number = new ArrayList<>();
    boolean pictureAllowed = true, addexpensefragment = false;
    private List<String> Images;
    private ArrayAdapter<String> ImagesAdapter;
    private Spinner sp_attachments;
    private LinearLayout ll_slip_sp;
    private ImageView iv_capture;
    private SlipModel slipModel;
    private int schoolId = 0;
    private boolean transfer_tab = false;


    public ExpenseAttachmentsAdapter(boolean isUpdate, Context context, List<String> expenseAttachments) {
        this.context = context;
        this.expenseAttachments = expenseAttachments;
        this.isUpdate = isUpdate;
        transfer_tab = true;
    }

    public ExpenseAttachmentsAdapter(int schoolId, boolean isUpdate, Context context, List<String> expenseAttachments, boolean addexpensefragment) {
        this.schoolId = schoolId;
        this.context = context;
        this.expenseAttachments = expenseAttachments;
        this.isUpdate = isUpdate;
        this.addexpensefragment = addexpensefragment;
        transfer_tab = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (!isUpdate) {
            switch (viewType) {
                case 0:
                    return new addAttachmentItem(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.custom_attachment_view, parent, false));

                case 1:
                    return new ReceiptAttachmentsDataVH(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.custom_receipt_images_view_data, parent, false));
            }
        } else {
            return new ReceiptAttachmentsDataVH(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_receipt_attachment_view_data, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        File imagePath;
        if (!isUpdate) {
            if (position > 0) {
                try {
                    ReceiptAttachmentsDataVH ReceiptAttachmentsDataVH = (ReceiptAttachmentsDataVH) viewHolder;
                    int new_position = position - 1;
                    imagePath = new File(paths.get(new_position).getSlip_path());
                    if (imagePath.exists()) {
                        byte[] dataF = AppModel.getInstance().bitmapToByte(Compressor.getDefault(context).compressToBitmap(imagePath));
                        Bitmap bitmapF = BitmapFactory.decodeByteArray(dataF, 0, dataF.length);
                        ReceiptAttachmentsDataVH.iv_imageView.setImageBitmap(bitmapF);
                        ReceiptAttachmentsDataVH.iv_imageView.setPadding(7, 7, 7, 7);
                    } else {
                        ReceiptAttachmentsDataVH.iv_imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_file_download));
                    }

                  /*  ReceiptAttachmentsDataVH.iv_imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(imagePath.exists()){
                                ReceiptAttachmentsDataVH.iv_imageView.setPadding(7,7,7,7);
                                previewImage(ReceiptAttachmentsDataVH.iv_imageView.getDrawable());
                            }
                            else {
                                ReceiptAttachmentsDataVH.iv_imageView.setVisibility(View.GONE);
                                ReceiptAttachmentsDataVH.progressBar.setVisibility(View.VISIBLE);
                                String url = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.imagebaseurlkey) + paths.get(new_position).getSlip_path().substring(paths.get(new_position).indexOf("HumanResources"));

                                BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                                    @Override
                                    public void onError(BasicImageDownloader.ImageError error) {
                                        Log.d("imageDownloadError", error.getMessage());
                                        String errorMessage;
                                        if (error.getErrorCode() == -1)
                                            errorMessage = "Download Failed: No Internet Connection";
                                        else
                                            errorMessage = "Download Failed: " + error.getMessage();
                                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                        ReceiptAttachmentsDataVH.iv_imageView.setVisibility(View.VISIBLE);
                                        ReceiptAttachmentsDataVH.progressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onProgressChange(int percent) {

                                    }

                                    @Override
                                    public void onComplete(Bitmap result) {
                                        BasicImageDownloader.writeToDisk(imagePath, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                            @Override
                                            public void onBitmapSaved() {
                                                ReceiptAttachmentsDataVH.iv_imageView.setVisibility(View.VISIBLE);
                                                ReceiptAttachmentsDataVH.progressBar.setVisibility(View.GONE);
                                                byte[] dataF = AppModel.getInstance().bitmapToByte(Compressor.getDefault(context).compressToBitmap(imagePath));
                                                Bitmap bitmapF = BitmapFactory.decodeByteArray(dataF, 0, dataF.length);
                                                ReceiptAttachmentsDataVH.iv_imageView.setImageBitmap(bitmapF);
                                                ReceiptAttachmentsDataVH.iv_imageView.setPadding(7,7,7,7);
                                            }

                                            @Override
                                            public void onBitmapSaveError(BasicImageDownloader.ImageError error) {

                                                Toast.makeText(context, "Image not saved: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                ReceiptAttachmentsDataVH.iv_imageView.setVisibility(View.VISIBLE);
                                                ReceiptAttachmentsDataVH.progressBar.setVisibility(View.GONE);

                                            }
                                        }, Bitmap.CompressFormat.JPEG, true);
                                    }
                                });
                                downloader.download(url, false);
                            }

                        }
                    });*/

                    ReceiptAttachmentsDataVH.iv_imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (imagePath.exists()) {
                                ReceiptAttachmentsDataVH.iv_imageView.setPadding(7, 7, 7, 7);
                                previewImage(ReceiptAttachmentsDataVH.iv_imageView.getDrawable());
                            }
                        }
                    });

                    ReceiptAttachmentsDataVH.deleteImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            paths.remove(new_position);
                            expenseAttachments.remove(new_position);

                            notifyDataSetChanged();
                        }
                    });

                    if (paths.get(new_position).getSlip_category().equalsIgnoreCase("C")) {
                        ReceiptAttachmentsDataVH.slipcategory_images.setText("Cheque");
                    }
                    if (paths.get(new_position).getSlip_category().equalsIgnoreCase("R")) {
                        ReceiptAttachmentsDataVH.slipcategory_images.setText("Deposit slip");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


            if (position == 0) {

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //if (separationAttachments.size() < 1) {
                        if (!transfer_tab) {
                            if (AddExpenseFragment.rb_bank.isChecked()) {
                                OpenDialogueforCheckorReceiptNumberAndImages(position);
                            }
                            if (AddExpenseFragment.rb_cashinhand.isChecked()) {
                                slipModel = new SlipModel();
                                slipModel.setSlip_category("R");
                                CaptureImage(position);
                            }
                            if (AddExpenseFragment.rb_salary.isChecked()) {
                                slipModel = new SlipModel();
                                slipModel.setSlip_category("C");
                                CaptureImage(position);
                            }
                        } else if (transfer_tab) {
                            if (TransferFragment.sp_category.getSelectedItemId() == 0) {
                                slipModel = new SlipModel();
                                slipModel.setSlip_category("C");
                                CaptureImage(position);
                            } else if (TransferFragment.sp_category.getSelectedItemId() == 1) {
                                slipModel = new SlipModel();
                                slipModel.setSlip_category("R");
                                CaptureImage(position);
                            }
                        }
                      /*  } else {
                            Toast.makeText(context, "Only 1 picture allowed", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });

            }

        } else {
            ReceiptAttachmentsDataVH ReceiptAttachmentsDataVH = (ReceiptAttachmentsDataVH) viewHolder;
            try {
                ReceiptAttachmentsDataVH.deleteImage.setVisibility(View.GONE);
                imagePath = new File(paths.get(position).getSlip_path());
                if(imagePath.exists()){
                    byte[] dataF = AppModel.getInstance().bitmapToByte(Compressor.getDefault(context).compressToBitmap(imagePath));
                    Bitmap bitmapF = BitmapFactory.decodeByteArray(dataF, 0, dataF.length);
                    ReceiptAttachmentsDataVH.iv_imageView.setImageBitmap(bitmapF);
                    ReceiptAttachmentsDataVH.iv_imageView.setPadding(7,7,7,7);
                    if(paths.get(position).getSlip_category().equalsIgnoreCase("C")){
                        ReceiptAttachmentsDataVH.slipcategory_attachment.setText(context.getResources().getText(R.string.cheque));
                    }
                    else {
                        ReceiptAttachmentsDataVH.slipcategory_attachment.setText(context.getResources().getText(R.string.receipt));
                    }

                } else {
                    ReceiptAttachmentsDataVH.iv_imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_file_download));
                }


                ReceiptAttachmentsDataVH.iv_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(imagePath.exists()){
                            previewImage(ReceiptAttachmentsDataVH.iv_imageView.getDrawable());
                            ReceiptAttachmentsDataVH.iv_imageView.setPadding(7,7,7,7);
                        }

                        else{
                            ReceiptAttachmentsDataVH.iv_imageView.setVisibility(View.GONE);
                            ReceiptAttachmentsDataVH.progressBar.setVisibility(View.VISIBLE);
                            String url = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.imagebaseurlkey) + paths.get(position).getSlip_path().substring(paths.get(position).getSlip_path().indexOf("Expense"));

                            BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                                @Override
                                public void onError(BasicImageDownloader.ImageError error) {
                                    Log.d("imageDownloadError", error.getMessage());
                                    String errorMessage;
                                    if(error.getErrorCode() == -1)
                                        errorMessage = "Download Failed: No Internet Connection";
                                    else
                                        errorMessage = "Download Failed: " + error.getMessage();
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                    ReceiptAttachmentsDataVH.iv_imageView.setVisibility(View.VISIBLE);
                                    ReceiptAttachmentsDataVH.progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onProgressChange(int percent) {

                                }

                                @Override
                                public void onComplete(Bitmap result) {
                                    BasicImageDownloader.writeToDisk(imagePath, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                        @Override
                                        public void onBitmapSaved() {
                                            ReceiptAttachmentsDataVH.iv_imageView.setVisibility(View.VISIBLE);
                                            ReceiptAttachmentsDataVH.progressBar.setVisibility(View.GONE);
                                            byte[] dataF = AppModel.getInstance().bitmapToByte(Compressor.getDefault(context).compressToBitmap(imagePath));
                                            Bitmap bitmapF = BitmapFactory.decodeByteArray(dataF, 0, dataF.length);
                                            ReceiptAttachmentsDataVH.iv_imageView.setImageBitmap(bitmapF);
                                            ReceiptAttachmentsDataVH.iv_imageView.setPadding(7,7,7,7);
                                            if(paths.get(position).getSlip_category().equalsIgnoreCase("C")){
                                                ReceiptAttachmentsDataVH.slipcategory_attachment.setText(context.getResources().getText(R.string.cheque));
                                            }
                                            else {
                                                ReceiptAttachmentsDataVH.slipcategory_attachment.setText(context.getResources().getText(R.string.receipt));
                                            }
                                        }

                                        @Override
                                        public void onBitmapSaveError(BasicImageDownloader.ImageError error) {

                                            Toast.makeText(context, "Image not saved: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                            ReceiptAttachmentsDataVH.iv_imageView.setVisibility(View.VISIBLE);
                                            ReceiptAttachmentsDataVH.progressBar.setVisibility(View.GONE);

                                        }
                                    }, Bitmap.CompressFormat.JPEG, true);
                                }
                            });
                            downloader.download(url, false);
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                ReceiptAttachmentsDataVH.iv_imageView.setVisibility(View.GONE);
            }
        }
    }

    private void CaptureImage(int position) {
        if (pictureAllowed) {
            try {
                PickImageDialog.build(new PickSetup().setTitle("Images")
                        .setPickTypes(EPickType.CAMERA).setSystemDialog(true)).show((FragmentActivity) context).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult pickResult) {
                        if (pickResult.getError() == null) {

                            ImageCompression imageCompression = new ImageCompression(context);

                            String path = pickResult.getPath();

                            imageCompression.execute(path);

                             byte[] expenseAttachmentImage = AppModel.getInstance().bitmapToByte(pickResult.getBitmap());
                             String imagePath = AppModel.getInstance().saveImageToStorage2(expenseAttachmentImage, context, "Expense Attachment_" + AppModel.getInstance().getCurrentDateTime("dd_MMM_yyyy_hh_mm_ss"), 1, paths.size());

                            slipModel.setSlip_path(imagePath);
                            paths.add(slipModel);
                            expenseAttachments.add(imagePath);

                            notifyDataSetChanged();

                        } else {
                            Toast.makeText(context, pickResult.getError().getMessage(), Toast.LENGTH_SHORT).show();
                            //slip_number.remove(position);
                            paths.remove(position);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                slip_number.remove(position);
                paths.remove(position);
            }
        } /*else {
            Toast.makeText(context, "Only one picture allowed", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public int getItemCount() {
        if (!isUpdate)
            return expenseAttachments.size() + 1;
        else
            return expenseAttachments.size();

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }


    private class addAttachmentItem extends RecyclerView.ViewHolder {

        addAttachmentItem(View inflate) {
            super(inflate);
        }
    }

    private class ReceiptAttachmentsDataVH extends RecyclerView.ViewHolder {

        private AppCompatImageView iv_imageView;
        private ImageView deleteImage;
        ProgressBar progressBar;
        private TextView slipcategory_images,slipcategory_attachment;

        ReceiptAttachmentsDataVH(View inflate) {
            super(inflate);
            iv_imageView = itemView.findViewById(R.id.attachment);
            deleteImage = itemView.findViewById(R.id.iv_delete);
            progressBar = itemView.findViewById(R.id.progress);
            slipcategory_images = itemView.findViewById(R.id.slipcategory_images);
            slipcategory_attachment = itemView.findViewById(R.id.slipcategory_attachment);
        }


    }

    private void previewImage(Drawable previewImageFile) {
        ImagePreviewDialog imagePreviewDialog = new ImagePreviewDialog(context, previewImageFile);
        imagePreviewDialog.show();
    }

    private void OpenDialogueforCheckorReceiptNumberAndImages(int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.check_receipt_image_number_dialog, null);
        sp_attachments = dialogView.findViewById(R.id.sp_attachments);
        ll_slip_sp = dialogView.findViewById(R.id.ll_slip_sp);
        iv_capture = dialogView.findViewById(R.id.iv_capture);
        slipModel = new SlipModel();

        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("")
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                populateSpinner(sp_attachments);

                iv_capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (AddExpenseFragment.rb_bank.isChecked()) {
                            if (sp_attachments.getSelectedItemPosition() == 1) {
                                slipModel.setSlip_category("C");
                                dialog.dismiss();
                                CaptureImage(position);
                            } else if (sp_attachments.getSelectedItemPosition() == 2) {
                                slipModel.setSlip_category("R");
                                dialog.dismiss();
                                CaptureImage(position);
                            } else {
                                Toast.makeText(context, "please select cheque/receipt", Toast.LENGTH_SHORT).show();
                            }
                        }


                        /*if(AddExpenseFragment.rb_cashinhand.isChecked()) {
                                if (!et_depositslipno.getText().toString().equals("") && !et_depositslipno.getText().toString().isEmpty()) {
                                    if(ExpenseHelperClass.getInstance(context).checkSlipno(schoolId,Long.parseLong(et_depositslipno.getText().toString().trim()),0)==-1) {
                                        slipModel.setSlip_no(Long.parseLong(et_depositslipno.getText().toString().trim()));
                                        slipModel.setSlip_category(2);
                                        dialog.dismiss();
                                        CaptureImage(position);
                                    }

                                }
                        }*/
                    }
                });
            }
        });
        dialog.show();
    }

    private void populateSpinner(Spinner sp_attachments) {

        Images = new ArrayList<>();
        Images.add(0, "Select Attachment Category");
        Images.add(1, "Cheque");
        Images.add(2, "Receipt");

        ImagesAdapter = new ArrayAdapter<>(context, R.layout.new_spinner_layout_black, Images);
        ImagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_attachments.setAdapter(ImagesAdapter);
        sp_attachments.setSelection(0);
    }
}
