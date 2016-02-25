package com.kbeanie.multipicker.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by kbibek on 2/20/16.
 */
public class FileUtils {
    public static String getExternalFilesDirectory(String type) {
        File directory = Environment.getExternalStoragePublicDirectory(type);
        return directory.getAbsolutePath();
    }

    public static String getExternalFilesDir(String type, Context context) {
        File directory = context.getExternalFilesDir(type);
        return directory.getAbsolutePath();
    }

    public static String getExternalCacheDir(Context context) {
        File directory = context.getExternalCacheDir();
        return directory.getAbsolutePath();
    }

    public static void copyFile(File source, File destination) throws IOException {
        org.apache.commons.io.FileUtils.copyFile(source, destination);
    }
}
