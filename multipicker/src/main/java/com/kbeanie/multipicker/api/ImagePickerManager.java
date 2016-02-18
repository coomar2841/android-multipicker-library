package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;

/**
 * Created by kbibek on 2/18/16.
 */
public class ImagePickerManager extends PickerManager {
    public ImagePickerManager(Activity activity, int pickerType) {
        super(activity, pickerType);
    }

    public ImagePickerManager(Fragment fragment, int pickerType) {
        super(fragment, pickerType);
    }

    public ImagePickerManager(android.app.Fragment appFragment, int pickerType) {
        super(appFragment, pickerType);
    }

    @Override
    public String pick() {
        if (pickerType == Picker.PICK_IMAGE_DEVICE) {
            return pickLocalImage();
        } else if (pickerType == Picker.PICK_IMAGE_CAMERA) {
            return takePictureWithCamera();
        }
        return null;
    }

    private String pickLocalImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (extras != null) {
            intent.putExtras(extras);
        }
        pickInternal(intent);
        return null;
    }

    private String takePictureWithCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String tempFilePath = buildFilePath("jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tempFilePath)));
        if (extras != null) {
            intent.putExtras(extras);
        }

        pickInternal(intent);
        return tempFilePath;
    }

    private void pickInternal(Intent intent) {
    }
}
