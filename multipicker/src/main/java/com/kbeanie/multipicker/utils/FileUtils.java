package com.kbeanie.multipicker.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.kbeanie.multipicker.api.exceptions.PickerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import storage.StoragePreferences;

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
        File directory = Environment.getExternalStorageDirectory();
        String appName = getAppName(context);
        String appDirectory = directory.getAbsolutePath() + File.separator + appName;
        File fileAppDirectory = new File(appDirectory);
        if (!fileAppDirectory.exists()) {
            fileAppDirectory.mkdir();
        }
        String appTypeDirectory = fileAppDirectory.getAbsolutePath() + File.separator + appName + " " + type;
        File finalDirectory = new File(appTypeDirectory);
        if (!finalDirectory.exists()) {
            finalDirectory.mkdir();
        }
        if (finalDirectory == null) {
            throw new PickerException("Couldn't initialize External Storage Path");
        }
        return finalDirectory.getAbsolutePath();
    }

    private static String getAppName(Context context) {
        StoragePreferences preferences = new StoragePreferences(context);
        String savedFolderName = preferences.getFolderName();
        if (savedFolderName == null || savedFolderName.isEmpty()) {
            try {
                ApplicationInfo info = context.getApplicationInfo();
                savedFolderName = context.getString(info.labelRes);
            } catch (Exception e) {
                String packageName = context.getPackageName();
                if (packageName.contains(".")) {
                    int index = packageName.lastIndexOf(".");
                    savedFolderName = packageName.substring(index + 1);
                } else {
                    savedFolderName = context.getPackageName();
                }
                preferences.setFolderName(savedFolderName);
            }
        }
        return savedFolderName;
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

    public static String getExternalCacheDir(Context context) throws PickerException {
        File directory = context.getExternalCacheDir();
        if (directory == null) {
            throw new PickerException("Couldn't intialize External Cache Directory");
        }
        return directory.getAbsolutePath();
    }

    public static void copyFile(File source, File destination) throws IOException {
        copyFile(source, destination, true);
    }

    public static void copyFile(File srcFile, File destFile,
                                boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (srcFile.exists() == false) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' exists but is a directory");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        }
        if (destFile.getParentFile() != null && destFile.getParentFile().exists() == false) {
            if (destFile.getParentFile().mkdirs() == false) {
                throw new IOException("Destination '" + destFile + "' directory cannot be created");
            }
        }
        if (destFile.exists() && destFile.canWrite() == false) {
            throw new IOException("Destination '" + destFile + "' exists but is read-only");
        }
        doCopyFile(srcFile, destFile, preserveFileDate);
    }

    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileInputStream input = new FileInputStream(srcFile);
        try {
            FileOutputStream output = new FileOutputStream(destFile);
            try {
                copy(input, output);
            } finally {
                output.flush();
                output.close();
            }
        } finally {
            input.close();
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" +
                    srcFile + "' to '" + destFile + "'");
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[2048];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static String getInternalFileDirectory(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }
}
