package com.kbeanie.multipicker.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by kbibek on 2/20/16.
 */
public class FileUtils {
    public static String getExternalFilesDirectory(String type) {
        File directory = Environment.getExternalStorageDirectory();
        File typeDirectory = new File(directory.getAbsolutePath() + File.separator + type);
        if (!typeDirectory.exists()) {
            typeDirectory.mkdir();
        }
        return typeDirectory.getAbsolutePath();
    }

    public static String getExternalFilesDir(String type, Context context) {
        File directory = context.getExternalFilesDir(type);
        return directory.getAbsolutePath();
    }
}
