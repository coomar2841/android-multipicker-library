package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.threads.FileProcessorThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kbibek on 2/18/16.
 */
public class FilePicker extends PickerManager {
    private final static String TAG = FilePicker.class.getSimpleName();
    private FilePickerCallback callback;

    private String mimeType = "*/*";

    public FilePicker(Activity activity, int pickerType) {
        super(activity, pickerType);
    }

    public FilePicker(Fragment fragment, int pickerType) {
        super(fragment, pickerType);
    }

    public FilePicker(android.app.Fragment appFragment, int pickerType) {
        super(appFragment, pickerType);
    }

    public void setFilePickerCallback(FilePickerCallback callback) {
        this.callback = callback;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String pick() {
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

    @Override
    public void submit(int requestCode, int resultCode, Intent data) {
        if (requestCode != pickerType) {
            onError("onActivityResult requestCode is different from the type the chooser was initialized with.");
        } else {
            handleFileData(data);
        }
    }

    private void handleFileData(Intent intent) {
        List<String> uris = new ArrayList<>();
        if (intent != null) {
            if (intent.getDataString() != null) {
                String uri = intent.getDataString();
                Log.d(TAG, "handleFileData: " + uri);
                uris.add(uri);
            } else if (intent.getClipData() != null) {
                ClipData clipData = intent.getClipData();
                Log.d(TAG, "handleFileData: Multiple files with ClipData");
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Log.d(TAG, "Item [" + i + "]: " + item.getUri().toString());
                    uris.add(item.getUri().toString());
                }
            }

            processFiles(uris);
        }
    }

    private void processFiles(List<String> uris) {
        FileProcessorThread thread = new FileProcessorThread(getContext(), getFileObjects(uris), cacheLocation);
        thread.setFilePickerCallback(callback);
        thread.start();
    }

    private void onError(final String errorMessage) {
        if (callback != null) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.onError(errorMessage);
                }
            });
        }
    }

    private List<ChosenFile> getFileObjects(List<String> uris) {
        List<ChosenFile> files = new ArrayList<>();
        for (String uri : uris) {
            ChosenFile file = new ChosenFile();
            file.setQueryUri(uri);
            file.setDirectoryType(Environment.DIRECTORY_DOCUMENTS);
            file.setType("file");
            files.add(file);
        }
        return files;
    }
}
