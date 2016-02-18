package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by kbibek on 2/18/16.
 */
public abstract class PickerManager {
    protected Activity activity;
    protected Fragment fragment;
    protected android.app.Fragment appFragment;

    protected String folderName;

    protected int pickerType;

    protected int cacheLocation;

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

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setCacheLocation(int cacheLocation) {
        this.cacheLocation = cacheLocation;
    }

    public abstract String pick();

    protected String buildFilePath(String extension) {
        return null;
    }
}
