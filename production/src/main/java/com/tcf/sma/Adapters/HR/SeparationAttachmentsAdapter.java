package com.tcf.sma.Adapters.HR;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tcf.sma.Activities.HR.EmployeeSeparation;
import com.tcf.sma.Activities.HR.ResignationApproval;
import com.tcf.sma.Helpers.DbTables.HR.EmployeeHelperClass;
import com.tcf.sma.Managers.ImagePreviewDialog;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeModel;
import com.tcf.sma.Models.RetrofitModels.HR.EmployeeSeparationModel;
import com.tcf.sma.R;
import com.tcf.sma.Services.BasicImageDownloader;
import com.tcf.sma.utils.ImageCompression;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

public class SeparationAttachmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<String> separationAttachments;
    private boolean isUpdate;
    public static List<String> paths = new ArrayList<>();
    public int resignType;
    boolean pictureAllowed = true;

    public SeparationAttachmentsAdapter(boolean isUpdate, Context context, List<String> separationAttachments, int resignType) {
        this.context = context;
        this.separationAttachments = separationAttachments;
        this.isUpdate = isUpdate;
        this.resignType = resignType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(!isUpdate){
            switch (viewType) {
                case 0:
                    return new addAttachmentItem(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.custom_attachment_view, parent, false));

                case 1:
                    return new SeparationAttachmentsDataVH(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.custom_attachment_view_data, parent, false));
            }
        } else {
            return new SeparationAttachmentsDataVH(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_attachment_view_data, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        File imagePath;
        if (!isUpdate) {
            if(position > 0){
                try {
                    SeparationAttachmentsDataVH SeparationAttachmentsDataVH = (SeparationAttachmentsDataVH) viewHolder;
                    int new_position = position - 1;
                    imagePath = new File(paths.get(new_position));
                    if(imagePath.exists()){
                        byte[] dataF = AppModel.getInstance().bitmapToByte(Compressor.getDefault(context).compressToBitmap(imagePath));
                        Bitmap bitmapF = BitmapFactory.decodeByteArray(dataF, 0, dataF.length);
                        SeparationAttachmentsDataVH.iv_imageView.setImageBitmap(bitmapF);
                        SeparationAttachmentsDataVH.iv_imageView.setPadding(7,7,7,7);
                    } else {
                        SeparationAttachmentsDataVH.iv_imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_file_download));
                    }

                    SeparationAttachmentsDataVH.iv_imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                if(imagePath.exists()){
                                    SeparationAttachmentsDataVH.iv_imageView.setPadding(7,7,7,7);
                                    previewImage(SeparationAttachmentsDataVH.iv_imageView.getDrawable());
                                }
                                else {
                                    SeparationAttachmentsDataVH.iv_imageView.setVisibility(View.GONE);
                                    SeparationAttachmentsDataVH.progressBar.setVisibility(View.VISIBLE);
                                    String url = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.imagebaseurlkey) + paths.get(new_position).substring(paths.get(new_position).indexOf("HumanResources"));

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
                                            SeparationAttachmentsDataVH.iv_imageView.setVisibility(View.VISIBLE);
                                            SeparationAttachmentsDataVH.progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onProgressChange(int percent) {

                                        }

                                        @Override
                                        public void onComplete(Bitmap result) {
                                            BasicImageDownloader.writeToDisk(imagePath, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                                @Override
                                                public void onBitmapSaved() {
                                                    SeparationAttachmentsDataVH.iv_imageView.setVisibility(View.VISIBLE);
                                                    SeparationAttachmentsDataVH.progressBar.setVisibility(View.GONE);
                                                    byte[] dataF = AppModel.getInstance().bitmapToByte(Compressor.getDefault(context).compressToBitmap(imagePath));
                                                    Bitmap bitmapF = BitmapFactory.decodeByteArray(dataF, 0, dataF.length);
                                                    SeparationAttachmentsDataVH.iv_imageView.setImageBitmap(bitmapF);
                                                    SeparationAttachmentsDataVH.iv_imageView.setPadding(7,7,7,7);
                                                }

                                                @Override
                                                public void onBitmapSaveError(BasicImageDownloader.ImageError error) {

                                                    Toast.makeText(context, "Image not saved: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                    SeparationAttachmentsDataVH.iv_imageView.setVisibility(View.VISIBLE);
                                                    SeparationAttachmentsDataVH.progressBar.setVisibility(View.GONE);

                                                }
                                            }, Bitmap.CompressFormat.JPEG, true);
                                        }
                                    });
                                    downloader.download(url, false);
                                }

                        }
                    });

                    SeparationAttachmentsDataVH.deleteImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            paths.remove(new_position);
                            separationAttachments.remove(new_position);
                            if(resignType == 1)
                                pictureAllowed = true;
                            notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(position == 0){

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (pictureAllowed) {
                            try {
                                PickImageDialog.build(new PickSetup().setTitle("Separation Attachments")
                                        .setPickTypes(EPickType.CAMERA, EPickType.GALLERY).setSystemDialog(true)).show((FragmentActivity) context).setOnPickResult(new IPickResult() {
                                    @Override
                                    public void onPickResult(PickResult pickResult) {
                                        if (pickResult.getError() == null) {

                                            ImageCompression imageCompression = new ImageCompression(context);

                                            String path = pickResult.getPath();

                                            imageCompression.execute(path);

        //                                    SeparationAttachmentsDataVH SeparationAttachmentsDataVH = (SeparationAttachmentsDataVH) viewHolder;
        //                                    SeparationAttachmentsDataVH.iv_imageView.setImageBitmap(pickResult.getBitmap());
                                            byte[] separationAttachmentImage = AppModel.getInstance().bitmapToByte(pickResult.getBitmap());
                                            String imagePath = AppModel.getInstance().saveImageToStorage2(separationAttachmentImage, context, "Separation Attachment_" + AppModel.getInstance().getCurrentDateTime("dd_MMM_yyyy_hh_mm_ss"), 1, paths.size());
                                            paths.add(imagePath);
                                            separationAttachments.add(imagePath);
                                            if(resignType == 1)
                                                pictureAllowed = false;
                                            notifyDataSetChanged();

                                        } else {
                                            Toast.makeText(context, pickResult.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context, "Only one picture allowed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            SeparationAttachmentsDataVH SeparationAttachmentsDataVH = (SeparationAttachmentsDataVH) viewHolder;
            try {
                SeparationAttachmentsDataVH.deleteImage.setVisibility(View.GONE);
                imagePath = new File(paths.get(position));
                if(imagePath.exists()){
                    byte[] dataF = AppModel.getInstance().bitmapToByte(Compressor.getDefault(context).compressToBitmap(imagePath));
                    Bitmap bitmapF = BitmapFactory.decodeByteArray(dataF, 0, dataF.length);
                    SeparationAttachmentsDataVH.iv_imageView.setImageBitmap(bitmapF);
                    SeparationAttachmentsDataVH.iv_imageView.setPadding(7,7,7,7);
                } else {
                    SeparationAttachmentsDataVH.iv_imageView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_file_download));
                }


                SeparationAttachmentsDataVH.iv_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(imagePath.exists()){
                            previewImage(SeparationAttachmentsDataVH.iv_imageView.getDrawable());
                            SeparationAttachmentsDataVH.iv_imageView.setPadding(7,7,7,7);
                        }

                        else{
                            SeparationAttachmentsDataVH.iv_imageView.setVisibility(View.GONE);
                            SeparationAttachmentsDataVH.progressBar.setVisibility(View.VISIBLE);
                            String url = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.imagebaseurlkey) + paths.get(position).substring(paths.get(position).indexOf("HumanResources"));

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
                                    SeparationAttachmentsDataVH.iv_imageView.setVisibility(View.VISIBLE);
                                    SeparationAttachmentsDataVH.progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onProgressChange(int percent) {

                                }

                                @Override
                                public void onComplete(Bitmap result) {
                                    BasicImageDownloader.writeToDisk(imagePath, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                        @Override
                                        public void onBitmapSaved() {
                                            SeparationAttachmentsDataVH.iv_imageView.setVisibility(View.VISIBLE);
                                            SeparationAttachmentsDataVH.progressBar.setVisibility(View.GONE);
                                            byte[] dataF = AppModel.getInstance().bitmapToByte(Compressor.getDefault(context).compressToBitmap(imagePath));
                                            Bitmap bitmapF = BitmapFactory.decodeByteArray(dataF, 0, dataF.length);
                                            SeparationAttachmentsDataVH.iv_imageView.setImageBitmap(bitmapF);
                                            SeparationAttachmentsDataVH.iv_imageView.setPadding(7,7,7,7);
                                        }

                                        @Override
                                        public void onBitmapSaveError(BasicImageDownloader.ImageError error) {

                                            Toast.makeText(context, "Image not saved: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                            SeparationAttachmentsDataVH.iv_imageView.setVisibility(View.VISIBLE);
                                            SeparationAttachmentsDataVH.progressBar.setVisibility(View.GONE);

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
                SeparationAttachmentsDataVH.iv_imageView.setVisibility(View.GONE);
            }


        }


    }


    @Override
    public int getItemCount() {
        return separationAttachments.size() + 1;
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

    private class SeparationAttachmentsDataVH extends RecyclerView.ViewHolder {

        private AppCompatImageView iv_imageView;
        private ImageView deleteImage;
        ProgressBar progressBar;

        SeparationAttachmentsDataVH(View inflate) {
            super(inflate);
            iv_imageView = itemView.findViewById(R.id.attachment);
            deleteImage = itemView.findViewById(R.id.iv_delete);
            progressBar = itemView.findViewById(R.id.progress);
        }


    }

    private void previewImage(Drawable previewImageFile) {
        ImagePreviewDialog imagePreviewDialog = new ImagePreviewDialog(context, previewImageFile);
        imagePreviewDialog.show();
    }
}
