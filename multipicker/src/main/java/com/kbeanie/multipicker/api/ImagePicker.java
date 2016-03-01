package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.core.ImagePickerImpl;

/**
 * Created by kbibek on 2/27/16.
 */
public final class ImagePicker extends ImagePickerImpl {
    public ImagePicker(Activity activity) {
        super(activity, Picker.PICK_IMAGE_DEVICE);
    }

    public ImagePicker(Fragment fragment) {
        super(fragment, Picker.PICK_IMAGE_DEVICE);
    }

    public ImagePicker(android.app.Fragment appFragment) {
        super(appFragment, Picker.PICK_IMAGE_DEVICE);
    }

    public void allowMultiple() {
        this.allowMultiple = true;
    }

    public void pickImage() {
        try {
            super.pick();
        } catch (PickerException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
    }
}
