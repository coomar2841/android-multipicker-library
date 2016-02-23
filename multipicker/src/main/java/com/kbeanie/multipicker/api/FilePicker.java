package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by kbibek on 2/18/16.
 */
public class FilePicker extends PickerManager {
    public FilePicker(Activity activity, int pickerType) {
        super(activity, pickerType);
    }

    public FilePicker(Fragment fragment, int pickerType) {
        super(fragment, pickerType);
    }

    public FilePicker(android.app.Fragment appFragment, int pickerType) {
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
