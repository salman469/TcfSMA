package com.tcf.sma.Managers;

/**
 * Created by Zubair Soomro on 10/19/2017.
 */

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class StorageUtil {

    private static final Pattern DIR_SEPARATOR = Pattern.compile("/");

    /**
     * Returns all available SD-Cards in the system (include emulated)
     * <p/>
     * Warning: Hack! Based on Android source code of version 4.3 (API 18)
     * Because there is no standard way to get it.
     * Edited by hendrawd
     *
     * @return paths to all available SD-Cards in the system (include emulated)
     */
    private static String[] getStorageDirectories(Context context) {
        // Final set of paths
        final Set<String> rv = new HashSet<>();
        // Primary physical SD-CARD (not emulated)
        final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        // All Secondary SD-CARDs (all exclude primary) separated by ":"
        final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        // Primary emulated SD-CARD
        final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            //fix of empty raw emulated storage on marshmallow
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                File[] files = context.getExternalFilesDirs(null);
                for (File file : files) {
                    if (file == null) continue;
                    String applicationSpecificAbsolutePath = file.getAbsolutePath();
                    String emulatedRootPath = applicationSpecificAbsolutePath.substring(0, applicationSpecificAbsolutePath.indexOf("Android/data"));
                    rv.add(emulatedRootPath);
                }
            } else {
                // Device has physical external storage; use plain paths.
                if (TextUtils.isEmpty(rawExternalStorage)) {
                    // EXTERNAL_STORAGE undefined; falling back to default.
                    rv.addAll(Arrays.asList(getPhysicalPaths()));
                } else {
                    rv.add(rawExternalStorage);
                }
            }
        } else {
            // Device has emulated storage; external storage paths should have
            // userId burned into them.
            final String rawUserId;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                rawUserId = "";
            } else {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                final String[] folders = DIR_SEPARATOR.split(path);
                final String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch (NumberFormatException ignored) {
                }
                rawUserId = isDigit ? lastFolder : "";
            }
            // /storage/emulated/0[1,2,...]
            if (TextUtils.isEmpty(rawUserId)) {
                rv.add(rawEmulatedStorageTarget);
            } else {
                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }
        // Add all secondary storages
        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
            // All Secondary SD-CARDs splited into array
            final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
            Collections.addAll(rv, rawSecondaryStorages);
        }
        return rv.toArray(new String[rv.size()]);
    }

    /**
     * @return physicalPaths based on phone model
     */
    private static String[] getPhysicalPaths() {
        return new String[]{
                "/storage/0403-0201/",              //Q TAB
                "/storage/sdcard0",
                "/storage/sdcard1",                 //Motorola Xoom
                "/storage/extsdcard",               //Samsung SGS3
                "/storage/sdcard0/external_sdcard", //User request
                "/mnt/extsdcard",
                "/mnt/sdcard/external_sd",          //Samsung galaxy family
                "/mnt/external_sd",
                "/mnt/media_rw/sdcard1",            //4.4.2 on CyanogenMod S3
                "/removable/microsd",               //Asus transformer prime
                "/mnt/emmc",
                "/storage/external_SD",             //LG
                "/storage/ext_sd",                  //HTC One Max
                "/storage/removable/sdcard1",       //Sony Xperia Z1
                "/data/sdext",
                "/data/sdext2",
                "/data/sdext3",
                "/data/sdext4",
                "/sdcard1",                         //Sony Xperia Z
                "/sdcard2",                         //HTC One M8s
                "/storage/microsd",                  //ASUS ZenFone 2
                "/storage/9016-4EF8/",//GALAXy S5
                "/storage/4494-0EE2/" //Dany tab
        };
    }

    public synchronized static File getSdCardPath(Context context) {

        File[] storages = ContextCompat.getExternalFilesDirs(context, null);
        File result = storages[0];

        try {
            for (File storage : storages) {
//            for (String physicalPath : getPhysicalPaths()) {
//                if (!storage.getAbsolutePath().toLowerCase().contains(physicalPath.toLowerCase()) && storage.exists()) {
                if (!storage.getAbsolutePath().toLowerCase().contains(result.getAbsolutePath().toLowerCase()) && storage.exists()) {
                    result = storage;
                    break;
                }
//            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String removeLastChar(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '/') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static void appendLog(Context context, String text) {

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        String CurrentDate = String.valueOf(mDay) + "-" + String.valueOf(mMonth + 1) + "-" + String.valueOf(mYear);

        String fileName = "StoragePaths.txt";
        File data = Environment.getExternalStorageDirectory();
        File folderPath = new File(data + "/TCF/Logs");
        File filePath = new File(folderPath.getPath() + "/" + fileName);

        // create app folder
        if (!folderPath.exists()) {
            folderPath.mkdirs();
        }


        if (!filePath.exists()) {
            try {
                filePath.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(filePath, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

