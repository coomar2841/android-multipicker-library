package com.kbeanie.multipicker.threads;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;

/**
 * Created by kbibek on 2/20/16.
 */
public class FileProcessorThread extends Thread {
    private final static String TAG = FileProcessorThread.class.getSimpleName();
    private int cacheLocation;
    protected Context context;
    protected List<? extends ChosenFile> files;

    public FileProcessorThread(Context context, List<? extends ChosenFile> files, int cacheLocation) {
        this.context = context;
        this.files = files;
        this.cacheLocation = cacheLocation;
    }

    @Override
    public void run() {
        super.run();
        processFiles();
    }

    private void processFiles() {
        for (ChosenFile file : files) {
            Log.d(TAG, "processFiles: " + file.getQueryUri());
            processFile(file);
        }
    }

    private void processFile(ChosenFile file) {
        String uri = file.getQueryUri();
        if (uri.startsWith("file://")) {
            file.setOriginalPath(sanitizeUri(uri));
        } else if (uri.startsWith("http")) {
            file.setOriginalPath(downloadAndSaveFile(uri, file.getType()));
        } else if (uri.startsWith("content:")) {
            file.setOriginalPath(getAbsolutePathIfAvailable(uri));
        }
    }

    // If starts with file: (For some content providers, remove the file prefix)
    private String sanitizeUri(String uri) {
        if (uri.startsWith("file://")) {
            uri = uri.substring(7);
        }
        return uri;
    }

    // Try to get a local copy if available
    private String getAbsolutePathIfAvailable(String uri) {
        String absolutePath = uri;
        String[] proj = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};

        // Workaround for various implementations for Google Photos/Picasa
        if (uri.startsWith(
                "content://com.android.gallery3d.provider")) {
            uri = Uri.parse(uri.replace(
                    "com.android.gallery3d", "com.google.android.gallery3d")).toString();
        }

        // Try to see if there's a cached local copy that is available
        if (absolutePath.startsWith("content://")) {
            Cursor cursor = context.getContentResolver().query(Uri.parse(absolutePath), proj,
                    null, null, null);
            cursor.moveToFirst();
            absolutePath = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            cursor.close();
        }
        Log.d(TAG, "getAbsolutePathIfAvailable(Local cached version): " + absolutePath);
        if (absolutePath == null) {
            absolutePath = uri;
        }

        // Check if DownloadsDocument in which case, we can get the local copy by using the content provider
        if (absolutePath.startsWith("content:") && isDownloadsDocument(Uri.parse(absolutePath))) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                absolutePath = getPath(Uri.parse(absolutePath));
        }
        Log.d(TAG, "getAbsolutePathIfAvailable(Dowload Document): " + absolutePath);

        // Still don't have a local copy??
        if (absolutePath.startsWith("content:")) {
            Log.d(TAG, "getAbsolutePathIfAvailable: (No Local Copy Available)");
        }

        Log.d(TAG, "getAbsolutePathIfAvailable(Final): " + absolutePath);

        return absolutePath;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private String downloadAndSaveFile(String url, String type) {
        String localFilePath = "";
        try {
            URL u = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedInputStream bStream = new BufferedInputStream(stream);

            String extension = guessExtensionFromUrl(url);
            if (extension == null) {
                extension = URLConnection.guessContentTypeFromStream(stream);
            }

            if (extension == null) {
                extension = "jpg";
            }

            localFilePath = getTargetDirectory(type) + File.separator
                    + UUID.randomUUID().toString() + "."
                    + extension;

            File localFile = new File(localFilePath);

            FileOutputStream fileOutputStream = new FileOutputStream(localFile);

            byte[] buffer = new byte[2048];
            int len;
            while ((len = bStream.read(buffer)) > 0)
                fileOutputStream.write(buffer, 0, len);
            fileOutputStream.flush();
            fileOutputStream.close();
            bStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localFilePath;
    }

    protected String getTargetDirectory(String type) {
        String directory = null;
        switch (cacheLocation) {
            case CacheLocation.EXTERNAL_STORAGE_PUBLIC_DIR:
                directory = FileUtils.getExternalFilesDirectory(type);
                break;
            case CacheLocation.EXTERNAL_STORAGE_APP_DIR:
                directory = FileUtils.getExternalFilesDir(type, context);
                break;
        }
        return directory;
    }

    private String guessExtensionFromUrl(String url) {
        String extension = null;
        Uri uri = Uri.parse(url);
        String lastSegment = uri.getLastPathSegment();
        if (lastSegment != null) {
            if (lastSegment.contains(".")) {
                String[] parts = lastSegment.split(".");
                if (parts.length >= 2) {
                    extension = parts[parts.length - 1];
                }
            }
        }
        Log.d(TAG, "guessExtensionFromUrl: " + extension);
        return extension;
    }
}
