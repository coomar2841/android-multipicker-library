package com.kbeanie.multipicker.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.exceptions.PickerException;

import java.io.File;
import java.io.IOException;

/**
 * Created by kbibek on 2/20/16.
 */
public class FileUtils {
    private final static String TAG = FileUtils.class.getSimpleName();

    public static String getExternalFilesDirectory(String type, Context context) throws PickerException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean permissionGranted = checkForExternalStorageRuntimePermission(context);
            if (!permissionGranted) {
                Log.e(TAG, Manifest.permission.WRITE_EXTERNAL_STORAGE + " permission not available");
                throw new PickerException(Manifest.permission.WRITE_EXTERNAL_STORAGE + " permission not available");
            }
        }
        File directory = Environment.getExternalStoragePublicDirectory(type);
        if(directory==null){
            throw new PickerException("Couldn't initialize External Storage Path");
        }
        return directory.getAbsolutePath();
    }

    public static String getExternalFilesDir(String type, Context context) throws PickerException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean permissionGranted = checkForExternalStorageRuntimePermission(context);
            if (!permissionGranted) {
                Log.e(TAG, Manifest.permission.WRITE_EXTERNAL_STORAGE + " permission not available");
                throw new PickerException(Manifest.permission.WRITE_EXTERNAL_STORAGE + " permission not available");
            }
        }
        File directory = context.getExternalFilesDir(type);
        if (directory == null) {
            throw new PickerException("Couldn't initialize External Files Directory");
        }
        return directory.getAbsolutePath();
    }

    private static boolean checkForExternalStorageRuntimePermission(Context context) {
        boolean granted;
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        granted = permissionCheck == PackageManager.PERMISSION_GRANTED;
        return granted;
    }

    public static String getExternalCacheDir(Context context) throws PickerException{
        File directory = context.getExternalCacheDir();
        if(directory==null){
            throw new PickerException("Couldn't intialize External Cache Directory");
        }
        return directory.getAbsolutePath();
    }

    public static void copyFile(File source, File destination) throws IOException {
        org.apache.commons.io.FileUtils.copyFile(source, destination);
    }
}
