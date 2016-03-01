package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.core.VideoPickerImpl;

/**
 * Created by kbibek on 2/28/16.
 */
public final class CameraVideoPicker extends VideoPickerImpl {

    public CameraVideoPicker(Activity activity) {
        super(activity, Picker.PICK_VIDEO_CAMERA);
    }

    public CameraVideoPicker(Fragment fragment) {
        super(fragment, Picker.PICK_VIDEO_CAMERA);
    }

    public CameraVideoPicker(android.app.Fragment appFragment) {
        super(appFragment, Picker.PICK_VIDEO_CAMERA);
    }

    public CameraVideoPicker(Activity activity, String path) {
        super(activity, Picker.PICK_VIDEO_CAMERA);
        reinitialize(path);
    }

    public CameraVideoPicker(Fragment fragment, String path) {
        super(fragment, Picker.PICK_VIDEO_CAMERA);
        reinitialize(path);
    }

    public CameraVideoPicker(android.app.Fragment appFragment, String path) {
        super(appFragment, Picker.PICK_VIDEO_CAMERA);
        reinitialize(path);
    }

    public String pickVideo() {
        String path = null;
        try {
            path = super.pick();
        } catch (PickerException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
        return path;
    }
}
