package com.tcf.sma.Survey.mediaServices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tcf.sma.Survey.httpServices.AppConstants;
import com.tcf.sma.Survey.model.SurveyAppModel;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ImageService {

    /*
     * public static boolean storeImage(Bitmap imageData, String filename) {
     * //get path to external storage (SD card) //String iconsStoragePath =
     * Environment.getExternalStorageDirectory() +
     * "/SixDegreez/Imagesconversation/"; File sdIconStorageDir = new
     * File(ImageService.imageConversationRoot);
     *
     * //create storage directories, if they don't exist
     * if(!sdIconStorageDir.exists() || !sdIconStorageDir.isDirectory()){
     * sdIconStorageDir.mkdirs(); }
     *
     * try { String filePath = filename; String filePath =
     * ImageService.imageConversationRoot + filename;
     *
     * FileOutputStream fileOutputStream = new FileOutputStream(filePath);
     *
     * BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
     *
     * //choose another format if PNG doesn't suit you
     * imageData.compress(CompressFormat.JPEG, 85, bos);
     *
     * bos.flush(); bos.close();
     *
     * } catch (Exception e) { // TODO: handle exception return false; }
     *
     * return true; }
     */

    public static String getNewFileName(String prefix, String extention, Context context) {
        String fileName = "";
        do {
            SimpleDateFormat s = new SimpleDateFormat("yyMMddhhmmss");
            String datetime = s.format(new Date());

            File ImgDir = new File(SurveyAppModel.getInstance().getSurveyPath(context));
            if (!ImgDir.exists() || !ImgDir.isDirectory()) {
                ImgDir.mkdirs();
            }
            fileName = SurveyAppModel.getInstance().getSurveyPath(context) + prefix + datetime + extention;

        }
        while (new File(fileName).exists());
//		SimpleDateFormat s = new SimpleDateFormat("yyMMddhhmmss");
//		String datetime = s.format(new Date());
//
//		File ImgDir = new File(AppConstants.APP_STORAGE_ROOT);
//		if (!ImgDir.exists() || !ImgDir.isDirectory()) {
//			ImgDir.mkdirs();
//		}
//		fileName = prefix + datetime + extention;
//		File f = new File(AppConstants.APP_STORAGE_ROOT, "form.frm");
//		if(f.exists())
        return fileName;
//		else
//			return "";
    }

    public static Bitmap getSampledBitmap(String bitmapFile) {

        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(bitmapFile, options); // WARNING:
            // decodeFile will
            // return null, no
            // image would be
            // loaded.
            // int b_width = options.outWidth;
            int b_height = options.outHeight;
            // int v_width = SixDegreeConstants.screenWidth;

            options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            /*
             * if(b_height <= 350 && b_width <= v_width) { options.inSampleSize
             * = 1; } else if(b_height > 1800 || b_width > (v_width * 4)) {
             * options.inSampleSize = 6; } else if(b_height > 1200 || b_width >
             * (v_width * 3)) { options.inSampleSize = 4; } else if(b_height >
             * 500 || b_width > v_width + Math.round(v_width * 0.25f)) {
             * options.inSampleSize = 2; } else { options.inSampleSize = 1; }
             */

            if (b_height <= 500) {
                options.inSampleSize = 1;
            } else if (b_height > 1800) {
                options.inSampleSize = 6;
            } else if (b_height > 1200) {
                options.inSampleSize = 4;
            } else if (b_height > 500) {
                options.inSampleSize = 2;
            } else {
                options.inSampleSize = 1;
            }

            return BitmapFactory.decodeFile(bitmapFile, options);

        } catch (Exception exp) {
            String s = "";
            s = exp.getMessage();

            String t = "";
            t = s;
            return null;
        }

    }

//    public static void saveCategories(String text) {
//
//        try {
//            File temp_file = new File(AppConstants.APP_STORAGE_ROOT + "categories.cat");
//
//            FileWriter writer = new FileWriter(temp_file);
//            writer.append(text);
//            writer.flush();
//            writer.close();
//            // Toast.makeText(this, "Survey Saved on Device",
//            // Toast.LENGTH_LONG).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

//    public static String loadCategories() {
//
//        try {
//            File temp_file = new File(AppConstants.APP_STORAGE_ROOT + "categories.cat");
//            if (temp_file.exists()) {
//
//                FileReader fr = new FileReader(AppConstants.APP_STORAGE_ROOT + "categories.cat");
//                BufferedReader br = new BufferedReader(fr);
//                String s = "";
//                String temp;
//                do {
//                    temp = br.readLine();
//                    if (temp != null) {
//                        s += temp;
//                    }
//                }
//                while (temp != null);
//
//                /*
//                 * while((s += br.readLine()) != null) { System.out.println(s);
//                 * }
//                 */
//
//                fr.close();
//                return s;
//            } else
//                return "";
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "";
//        }
//
//    }

//    public static void saveFile(String text, String prefix, String ext) {
//
//        try {
//            File temp_file = new File(AppConstants.APP_STORAGE_ROOT + prefix + ext);
//
//            FileWriter writer = new FileWriter(temp_file);
//            writer.append(text);
//            writer.flush();
//            writer.close();
//            // Toast.makeText(this, "Survey Saved on Device",
//            // Toast.LENGTH_LONG).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

//    public static String loadFile(String path) {
//
//        try {
//            File temp_file = new File(path);
//            if (temp_file.exists()) {
//
//                FileReader fr = new FileReader(path);
//                BufferedReader br = new BufferedReader(fr);
//                String s = "";
//                String temp;
//                do {
//                    temp = br.readLine();
//                    if (temp != null) {
//                        s += temp;
//                    }
//                }
//                while (temp != null);
//
//                /*
//                 * while((s += br.readLine()) != null) { System.out.println(s);
//                 * }
//                 */
//
//                fr.close();
//                return s;
//            } else
//                return "";
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "";
//        }
//
//    }

//    public static byte[] compressBitmapFileToJPEG(String bitmapFile) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile);
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] byte_arr = stream.toByteArray();
//        return byte_arr;
//    }

}
