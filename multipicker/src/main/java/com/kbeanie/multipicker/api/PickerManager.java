package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.kbeanie.multipicker.utils.FileUtils;

import java.io.File;
import java.util.UUID;

/**
 * Created by kbibek on 2/18/16.
 */
public abstract class PickerManager {
    protected Activity activity;
    protected Fragment fragment;
    protected android.app.Fragment appFragment;

    protected int pickerType;

    protected int cacheLocation = CacheLocation.EXTERNAL_STORAGE_APP_DIR;

    protected Bundle extras;

    public PickerManager(Activity activity, int pickerType) {
        this.activity = activity;
        this.pickerType = pickerType;
    }

    public PickerManager(Fragment fragment, int pickerType) {
        this.fragment = fragment;
        this.pickerType = pickerType;
    }

    public PickerManager(android.app.Fragment appFragment, int pickerType) {
        this.appFragment = appFragment;
        this.pickerType = pickerType;
    }

    public void setExtras(Bundle extras) {
        this.extras = extras;
    }

    public void setCacheLocation(int cacheLocation) {
        this.cacheLocation = cacheLocation;
    }

    public abstract String pick();

    public abstract void submit(int requestCode, int resultCode, Intent data);

    protected String buildFilePath(String extension, String type) {
        String directoryPath = getDirectory(type);
        String filePath = directoryPath + File.separator + UUID.randomUUID().toString() + "." + extension;
        return filePath;
    }

    private String getDirectory(String type) {
        String directory = null;
        switch (cacheLocation) {
            case CacheLocation.EXTERNAL_STORAGE_PUBLIC_DIR:
                directory = FileUtils.getExternalFilesDirectory(type);
                break;
            case CacheLocation.EXTERNAL_STORAGE_APP_DIR:
                directory = FileUtils.getExternalFilesDir(type, getContext());
                break;
        }
        return directory;
    }

    protected Context getContext() {
        if (activity != null) {
            return activity;
        } else if (fragment != null) {
            return fragment.getActivity();
        } else if (appFragment != null) {
            return appFragment.getActivity();
        }
        return null;
    }
}
