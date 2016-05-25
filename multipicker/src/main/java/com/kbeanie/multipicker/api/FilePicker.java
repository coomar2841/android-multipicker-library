package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.core.PickerManager;
import com.kbeanie.multipicker.core.threads.FileProcessorThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Choose a file from your device. Gallery, Downloads, Dropbox etc.
 */
public final class FilePicker extends PickerManager {
    private final static String TAG = FilePicker.class.getSimpleName();
    private FilePickerCallback callback;

    private String mimeType = "*/*";

    /**
     * Constructor for choosing a file from an {@link Activity}
     * @param activity
     */
    public FilePicker(Activity activity) {
        super(activity, Picker.PICK_FILE);
    }

    /**
     * Constructor for choosing a file from a {@link Fragment}
     * @param fragment
     */
    public FilePicker(Fragment fragment) {
        super(fragment, Picker.PICK_FILE);
    }

    /**
     * Constructor for choosing a file from a {@link android.app.Fragment}
     * @param appFragment
     */
    public FilePicker(android.app.Fragment appFragment) {
        super(appFragment, Picker.PICK_FILE);
    }

    /**
     * Allow multiple files to be chosen. Default is false. This will only work for applications that support multiple file selection. Else, you will get only one result.
     */
    public void allowMultiple() {
        this.allowMultiple = true;
    }

    /**
     * Listener which gets callbacks when your file is processed and ready to be used.
     * @param callback
     */
    public void setFilePickerCallback(FilePickerCallback callback) {
        this.callback = callback;
    }

    /**
     * Default: All types of files. Set this value to a specific mimetype to pick.
     *
     * ex: application/pdf, application/xls
     * @param mimeType
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Triggers file selection
     */
    public void pickFile() {
        try {
            pick();
        } catch (PickerException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
    }

    @Override
    protected String pick() throws PickerException {
        if (callback == null) {
            throw new PickerException("FilePickerCallback is null!!! Please set one");
        }
        String action = Intent.ACTION_GET_CONTENT;
        Intent intent = new Intent(action);
        intent.setType(mimeType);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        pickInternal(intent, pickerType);
        return null;
    }

    /**
     * Call this method from
     * {@link Activity#onActivityResult(int, int, Intent)}
     * OR
     * {@link Fragment#onActivityResult(int, int, Intent)}
     * OR
     * {@link android.app.Fragment#onActivityResult(int, int, Intent)}
     * @param data
     */
    @Override
    public void submit(Intent data) {
        handleFileData(data);
    }

    private void handleFileData(Intent intent) {
        List<String> uris = new ArrayList<>();
        if (intent != null) {
            if (intent.getDataString() != null) {
                String uri = intent.getDataString();
                Log.d(TAG, "handleFileData: " + uri);
                uris.add(uri);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (intent.getClipData() != null) {
                    ClipData clipData = intent.getClipData();
                    Log.d(TAG, "handleFileData: Multiple files with ClipData");
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Log.d(TAG, "Item [" + i + "]: " + item.getUri().toString());
                        uris.add(item.getUri().toString());
                    }
                }
            }
            if (intent.hasExtra("uris")) {
                ArrayList<Uri> paths = intent.getParcelableArrayListExtra("uris");
                for (int i = 0; i < paths.size(); i++) {
                    uris.add(paths.get(i).toString());
                }
            }

            processFiles(uris);
        }
    }

    private void processFiles(List<String> uris) {
        FileProcessorThread thread = new FileProcessorThread(getContext(), getFileObjects(uris), cacheLocation);
        thread.setFilePickerCallback(callback);
        thread.setRequestId(requestId);
        thread.start();
    }

    private void onError(final String errorMessage) {
        try {
            if (callback != null) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(errorMessage);
                    }
                });
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private List<ChosenFile> getFileObjects(List<String> uris) {
        List<ChosenFile> files = new ArrayList<>();
        for (String uri : uris) {
            ChosenFile file = new ChosenFile();
            file.setQueryUri(uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                file.setDirectoryType(Environment.DIRECTORY_DOCUMENTS);
            } else {
                file.setDirectoryType(Environment.DIRECTORY_DOWNLOADS);
            }
            file.setType("file");
            files.add(file);
        }
        return files;
    }
}
