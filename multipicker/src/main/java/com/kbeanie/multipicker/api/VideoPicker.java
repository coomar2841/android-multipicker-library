package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by kbibek on 2/18/16.
 */
public class VideoPicker extends PickerManager {
    public VideoPicker(Activity activity, int pickerType) {
        super(activity, pickerType);
    }

    public VideoPicker(Fragment fragment, int pickerType) {
        super(fragment, pickerType);
    }

    public VideoPicker(android.app.Fragment appFragment, int pickerType) {
        super(appFragment, pickerType);
    }

    @Override
    public String pick() {
        return null;
    }

    @Override
    public void submit(int requestCode, int resultCode, Intent data) {

    }
}
