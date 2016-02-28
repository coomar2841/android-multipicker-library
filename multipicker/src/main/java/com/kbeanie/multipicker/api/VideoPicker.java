package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPickerImpl;

/**
 * Created by kbibek on 2/28/16.
 */
public class VideoPicker extends VideoPickerImpl{
    public VideoPicker(Activity activity) {
        super(activity, Picker.PICK_VIDEO_DEVICE);
    }

    public VideoPicker(Fragment fragment) {
        super(fragment, Picker.PICK_VIDEO_DEVICE);
    }

    public VideoPicker(android.app.Fragment appFragment) {
        super(appFragment, Picker.PICK_VIDEO_DEVICE);
    }

    public void allowMultple(){
        this.allowMultiple = true;
    }

    public void pickVideo() {
        super.pick();
    }
}
