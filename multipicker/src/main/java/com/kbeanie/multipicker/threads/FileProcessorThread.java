package com.kbeanie.multipicker.threads;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

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
            file = sanitizeUri(file);
        } else if (uri.startsWith("http")) {
            file = downloadAndSaveFile(file);
        } else if (uri.startsWith("content:")) {
            file = getAbsolutePathIfAvailable(file);
        } else if (uri.startsWith("content:")) {
            file = getFromContentProvider(file);
        }
        Log.d(TAG, "processFile: Query Path: " + file.toString());
        Log.d(TAG, "processFile: Final Path: " + file.toString());
    }

    // If starts with file: (For some content providers, remove the file prefix)
    private ChosenFile sanitizeUri(ChosenFile file) {
        if (file.getQueryUri().startsWith("file://")) {
            file.setOriginalPath(file.getQueryUri().substring(7));
        }
        return file;
    }

    private ChosenFile getFromContentProvider(ChosenFile file) {
        return file;
    }

    // Try to get a local copy if available
    private ChosenFile getAbsolutePathIfAvailable(ChosenFile file) {
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.MIME_TYPE};

        // Workaround for various implementations for Google Photos/Picasa
        if (file.getQueryUri().startsWith(
                "content://com.android.gallery3d.provider")) {
            file.setOriginalPath(Uri.parse(file.getQueryUri().replace(
                    "com.android.gallery3d", "com.google.android.gallery3d")).toString());
        } else {
            file.setOriginalPath(file.getQueryUri());
        }

        // Try to see if there's a cached local copy that is available
        if (file.getOriginalPath().startsWith("content://")) {
            Cursor cursor = context.getContentResolver().query(Uri.parse(file.getOriginalPath()), projection,
                    null, null, null);
            cursor.moveToFirst();
            String path = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            if (path != null) {
                file.setOriginalPath(path);
            }
            String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE));
            if (mimeType != null) {
                file.setMimeType(mimeType);
                Log.d(TAG, "getAbsolutePathIfAvailable: Mime Type:" + mimeType);
            }
            cursor.close();
        }
        Log.d(TAG, "getAbsolutePathIfAvailable(Local cached version): " + file.getOriginalPath());

        // Check if DownloadsDocument in which case, we can get the local copy by using the content provider
        if (file.getOriginalPath().startsWith("content:") && isDownloadsDocument(Uri.parse(file.getOriginalPath()))) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String[] data = getPathAndMimeType(file);
                if (data[0] != null) {
                    file.setOriginalPath(data[0]);
                }
                if (data[1] != null) {
                    file.setMimeType(data[1]);
                }
            }
        }
        Log.d(TAG, "getAbsolutePathIfAvailable(Dowload Document): " + file.getOriginalPath());

        // Still don't have a local copy??
        if (file.getOriginalPath().startsWith("content:")) {
            Log.d(TAG, "getAbsolutePathIfAvailable: (No Local Copy Available)");
        }

        Log.d(TAG, "getAbsolutePathIfAvailable(Final): " + file.getOriginalPath());

        return file;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String[] getPathAndMimeType(ChosenFile file) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        Uri uri = Uri.parse(file.getOriginalPath());
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataAndMimeType(contentUri, null, null, file.getType());
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

                return getDataAndMimeType(contentUri, selection, selectionArgs, file.getType());
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataAndMimeType(uri, null, null, file.getType());
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            String path = uri.getPath();
            String mimeType = guessMimeTypeFromUrl(path, file.getType());
            String[] data = new String[2];
            data[0] = path;
            data[1] = mimeType;
            return data;
        }

        return null;
    }

    private String[] getDataAndMimeType(Uri uri, String selection,
                                        String[] selectionArgs, String type) {
        String[] data = new String[2];
        Cursor cursor = null;
        String[] projection = {MediaStore.MediaColumns.DATA};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                data[0] = path;
                data[1] = guessMimeTypeFromUrl(path, type);
                return data;
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

    private ChosenFile downloadAndSaveFile(ChosenFile file) {
        String localFilePath = "";
        try {
            URL u = new URL(file.getQueryUri());
            HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedInputStream bStream = new BufferedInputStream(stream);

            String mimeType = guessExtensionFromUrl(file.getQueryUri());
            if (mimeType == null) {
                mimeType = URLConnection.guessContentTypeFromStream(stream);
            }

            if (mimeType == null) {
                mimeType = "image/jpg";
            }

            file.setMimeType(mimeType);

            localFilePath = getTargetDirectory(file.getDirectoryType()) + File.separator
                    + UUID.randomUUID().toString() + "."
                    + file.getFileExtensionFromMimeType();

            File localFile = new File(localFilePath);

            FileOutputStream fileOutputStream = new FileOutputStream(localFile);

            byte[] buffer = new byte[2048];
            int len;
            while ((len = bStream.read(buffer)) > 0)
                fileOutputStream.write(buffer, 0, len);
            fileOutputStream.flush();
            fileOutputStream.close();
            bStream.close();
            file.setOriginalPath(localFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
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
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        return extension;
    }

    private String guessMimeTypeFromUrl(String url, String type) {
        String mimeType = null;
        String extension = guessExtensionFromUrl(url);
        if (extension != null) {
            mimeType = type + "/" + extension;
        } else {
            mimeType = type + "/*";
        }
        return mimeType;
    }
}
