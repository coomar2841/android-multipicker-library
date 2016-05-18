package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.core.ImagePickerImpl;

/**
 * Use this Picker to trigger device Camera and take a snap
 */
public final class CameraImagePicker extends ImagePickerImpl {
    public CameraImagePicker(Activity activity) {
        super(activity, Picker.PICK_IMAGE_CAMERA);
    }

    public CameraImagePicker(Fragment fragment) {
        super(fragment, Picker.PICK_IMAGE_CAMERA);
    }

    public CameraImagePicker(android.app.Fragment appFragment) {
        super(appFragment, Picker.PICK_IMAGE_CAMERA);
    }

    public CameraImagePicker(Activity activity, String path) {
        super(activity, Picker.PICK_IMAGE_CAMERA);
        reinitialize(path);
    }

    public CameraImagePicker(Fragment fragment, String path) {
        super(fragment, Picker.PICK_IMAGE_CAMERA);
        reinitialize(path);
    }

    public CameraImagePicker(android.app.Fragment appFragment, String path) {
        super(appFragment, Picker.PICK_IMAGE_CAMERA);
        reinitialize(path);
    }

    public String pickImage() {
        String path = null;
        try {
            path = pick();
        } catch (PickerException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
        return path;
    }
}
