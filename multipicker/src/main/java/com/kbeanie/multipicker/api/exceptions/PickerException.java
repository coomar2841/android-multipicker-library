package com.kbeanie.multipicker.api.exceptions;

import android.content.ActivityNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by kbibek on 2/21/16.
 */
public class PickerException extends Exception {

    public PickerException(String msg) {
        super(msg);
    }

    public PickerException(ActivityNotFoundException e) {
        super(e);
    }

    public PickerException(FileNotFoundException e) {
        super(e);
    }

    public PickerException(IOException e) {
        super(e);
    }
}
