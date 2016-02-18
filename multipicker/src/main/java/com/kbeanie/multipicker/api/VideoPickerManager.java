package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by kbibek on 2/18/16.
 */
public class VideoPickerManager extends PickerManager{
    public VideoPickerManager(Activity activity, int pickerType) {
        super(activity, pickerType);
    }

    public VideoPickerManager(Fragment fragment, int pickerType) {
        super(fragment, pickerType);
    }

    public VideoPickerManager(android.app.Fragment appFragment, int pickerType) {
        super(appFragment, pickerType);
    }

    @Override
    public String pick() {
        return null;
    }
}
