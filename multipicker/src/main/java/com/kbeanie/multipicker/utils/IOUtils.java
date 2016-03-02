package com.kbeanie.multipicker.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by kbibek on 3/2/16.
 */
public class IOUtils {
    public static String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public static void convertStreamToFile(InputStream is) throws IOException {
        FileWriter writer = new FileWriter(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "blogaway" + File.separator + "output.xml");
        writer.write(convertStreamToString(is));
        writer.flush();
        writer.close();
    }
}
