package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by kbibek on 2/27/16.
 */
public class ImagePicker extends ImagePickerImpl {
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
        super.pick();
    }
}
