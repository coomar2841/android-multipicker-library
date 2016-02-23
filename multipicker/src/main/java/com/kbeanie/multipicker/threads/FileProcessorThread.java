package com.kbeanie.multipicker.threads;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static com.kbeanie.multipicker.utils.StreamHelper.close;
import static com.kbeanie.multipicker.utils.StreamHelper.flush;
import static com.kbeanie.multipicker.utils.StreamHelper.verifyStream;

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
        try {
            processFiles();
            postProcessFiles();
        } catch (PickerException e) {
            e.printStackTrace();
        }
    }

    protected void processFiles() {
        for (ChosenFile file : files) {
            try {
                Log.d(TAG, "processFile: Before: " + file.toString());
                processFile(file);
                postProcess(file);
                Log.d(TAG, "processFile: Final Path: " + file.toString());
            } catch (PickerException e) {
                e.printStackTrace();
            }
        }
    }

    protected void postProcessFiles() throws PickerException {
        for (ChosenFile file : files) {
            postProcess(file);
        }
    }

    private void postProcess(ChosenFile file) throws PickerException {
        file.setCreatedAt(Calendar.getInstance().getTime());
        file.setSize(new File(file.getOriginalPath()).length());
        copyFileToFolder(file);
    }

    private void copyFileToFolder(ChosenFile file) throws PickerException {
        if (!file.getOriginalPath().contains(File.separator + context.getPackageName() + File.separator)) {
            String outputPath = generateFileName(file);
            BufferedOutputStream outStream = null;
            BufferedInputStream bStream = null;
            try {
                File inputFile;
                inputFile = new File(URLDecoder.decode(file.getOriginalPath(), Charset.defaultCharset().name()));
                File copyTo = new File(outputPath);
                bStream = new BufferedInputStream(new FileInputStream(inputFile));
                outStream = new BufferedOutputStream(new FileOutputStream(copyTo));
                byte[] buf = new byte[2048];
                int len;
                while ((len = bStream.read(buf)) > 0) {
                    outStream.write(buf, 0, len);
                }

                file.setOriginalPath(copyTo.getAbsolutePath());
            } catch (IOException e) {
                throw new PickerException(e);
            } finally {
                flush(outStream);
                close(bStream);
                close(outStream);
            }
        }
    }

    private void processFile(ChosenFile file) throws PickerException {
        String uri = file.getQueryUri();
        if (uri.startsWith("file://")) {
            file = sanitizeUri(file);
            file.setMimeType(guessMimeTypeFromUrl(file.getOriginalPath(), file.getType()));
        } else if (uri.startsWith("http")) {
            file = downloadAndSaveFile(file);
        } else if (uri.startsWith("content:")) {
            file = getAbsolutePathIfAvailable(file);
        }
        uri = file.getOriginalPath();
        // Still content:: Try ContentProvider stream import
        if (uri.startsWith("content:")) {
            file = getFromContentProvider(file);
        }
        uri = file.getOriginalPath();
        // Still content:: Try ContentProvider stream import alternate
        if (uri.startsWith("content:")) {
            file = getFromContentProviderAlternate(file);
        }
    }

    // If starts with file: (For some content providers, remove the file prefix)
    private ChosenFile sanitizeUri(ChosenFile file) {
        if (file.getQueryUri().startsWith("file://")) {
            file.setOriginalPath(file.getQueryUri().substring(7));
        }
        return file;
    }

    protected ChosenFile getFromContentProviderAlternate(ChosenFile file) throws PickerException {
        BufferedOutputStream outStream = null;
        BufferedInputStream bStream = null;

        try {
            InputStream inputStream = context.getContentResolver()
                    .openInputStream(Uri.parse(file.getOriginalPath()));

            bStream = new BufferedInputStream(inputStream);
            String mimeType = URLConnection.guessContentTypeFromStream(inputStream);

            verifyStream(file.getOriginalPath(), bStream);

            String localFilePath = generateFileName(file);

            outStream = new BufferedOutputStream(new FileOutputStream(localFilePath));
            byte[] buf = new byte[2048];
            int len;
            while ((len = bStream.read(buf)) > 0) {
                outStream.write(buf, 0, len);
            }
            flush(outStream);
            file.setOriginalPath(localFilePath);
            if (file.getMimeType() != null && file.getMimeType().contains("/*")) {
                if (mimeType != null && !mimeType.isEmpty()) {
                    file.setMimeType(mimeType);
                } else {
                    file.setMimeType(guessMimeTypeFromUrl(file.getOriginalPath(), file.getType()));
                }
            }
        } catch (IOException e) {
            throw new PickerException(e);
        } finally {
            close(bStream);
            close(outStream);
        }

        return file;
    }

    protected ChosenFile getFromContentProvider(ChosenFile file) throws PickerException {

        BufferedInputStream inputStream = null;
        BufferedOutputStream outStream = null;
        try {
            String localFilePath = generateFileName(file);
            ParcelFileDescriptor parcelFileDescriptor = context
                    .getContentResolver().openFileDescriptor(Uri.parse(file.getOriginalPath()),
                            "r");
            verifyStream(file.getOriginalPath(), parcelFileDescriptor);

            FileDescriptor fileDescriptor = parcelFileDescriptor
                    .getFileDescriptor();

            inputStream = new BufferedInputStream(new FileInputStream(fileDescriptor));
            String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
            BufferedInputStream reader = new BufferedInputStream(inputStream);

            outStream = new BufferedOutputStream(
                    new FileOutputStream(localFilePath));
            byte[] buf = new byte[2048];
            int len;
            while ((len = reader.read(buf)) > 0) {
                outStream.write(buf, 0, len);
            }
            flush(outStream);
            file.setOriginalPath(localFilePath);
            if (file.getMimeType() != null && file.getMimeType().contains("/*")) {
                if (mimeType != null && !mimeType.isEmpty()) {
                    file.setMimeType(mimeType);
                } else {
                    file.setMimeType(guessMimeTypeFromUrl(file.getOriginalPath(), file.getType()));
                }
            }
        } catch (IOException e) {
            throw new PickerException(e);
        } finally {
            flush(outStream);
            close(outStream);
            close(inputStream);
        }
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
            try {
                Cursor cursor = context.getContentResolver().query(Uri.parse(file.getOriginalPath()), projection,
                        null, null, null);
                cursor.moveToFirst();
                try {
                    String path = cursor.getString(cursor
                            .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                    if (path != null) {
                        file.setOriginalPath(path);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE));
                if (mimeType != null) {
                    file.setMimeType(mimeType);
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
        String localFilePath;
        try {
            URL u = new URL(file.getQueryUri());
            HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedInputStream bStream = new BufferedInputStream(stream);

            String mimeType = guessMimeTypeFromUrl(file.getQueryUri(), file.getType());
            if (mimeType == null) {
                mimeType = URLConnection.guessContentTypeFromStream(stream);
            }

            if (mimeType == null) {
                mimeType = file.getType() + "/*";
            }

            file.setMimeType(mimeType);

            localFilePath = generateFileName(file);

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
            case CacheLocation.EXTERNAL_CACHE_DIR:
                directory = FileUtils.getExternalCacheDir(context);
                break;
        }

        return directory;
    }

    // Guess File extension from the file name
    private String guessExtensionFromUrl(String url) {
        return MimeTypeMap.getFileExtensionFromUrl(url);
    }

    // Guess Mime Type from the file extension
    private String guessMimeTypeFromUrl(String url, String type) {
        String mimeType;
        String extension = guessExtensionFromUrl(url);
        if (extension != null && !extension.isEmpty()) {
            mimeType = type + "/" + extension;
        } else {
            mimeType = type + "/*";
        }
        return mimeType;
    }

    private String generateFileName(ChosenFile file) {
        String fileName = UUID.randomUUID().toString();
        // If File name already contains an extension, we don't need to guess the extension
        String extension = file.getFileExtensionFromMimeType();
        if (extension != null && !extension.isEmpty()) {
            fileName += extension;
        }
        String filePath = getTargetDirectory(file.getDirectoryType()) + File.separator
                + fileName;
        return filePath;
    }

    protected Activity getActivityFromContext() {
        return (Activity)context;
    }
}
