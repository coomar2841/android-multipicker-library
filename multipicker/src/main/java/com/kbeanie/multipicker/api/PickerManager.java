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
 * Abstract class for all types of Pickers
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

    /**
     * Set extras which will be directly passed to the target applications. You should use this
     * to take advantage of specific applications
     * ex. Some applications support cropping, or editing the image itself before they give you
     * the final image
     *
     * @param extras
     */
    public void setExtras(Bundle extras) {
        this.extras = extras;
    }

    /**
     * Default cache location is {@link CacheLocation#EXTERNAL_STORAGE_APP_DIR}
     *
     * @param cacheLocation {@link CacheLocation}
     */
    public void setCacheLocation(int cacheLocation) {
        this.cacheLocation = cacheLocation;
    }

    /**
     * Triggers pick image
     *
     * @return
     */
    public abstract String pick();

    /**
     * This method should be called after {@link Activity#onActivityResult(int, int, Intent)} is  called.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public abstract void submit(int requestCode, int resultCode, Intent data);

    protected String buildFilePath(String extension, String type) {
        String directoryPath = getDirectory(type);
        return directoryPath + File.separator + UUID.randomUUID().toString() + "." + extension;
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

    protected void pickInternal(Intent intent, int type) {
        if (activity != null) {
            activity.startActivityForResult(intent, type);
        } else if (fragment != null) {
            fragment.startActivityForResult(intent, type);
        } else if (appFragment != null) {
            appFragment.startActivityForResult(intent, type);
        }
    }
}
