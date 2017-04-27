package com.kbeanie.multipicker.api.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.core.ImagePickerImpl;

/**
 * Created by kbibek on 27/04/17.
 */

public abstract class PickImageActivity extends AppCompatActivity implements ImagePickerCallback {

    private ImagePicker imagePicker;
    private CameraImagePicker cameraImagePicker;
    private int pickerType;
    private String pickerPath;

    protected void pickSingleImage() {
        imagePicker = prepareImagePicker();
        imagePicker.pickImage();
        pickerType = Picker.PICK_IMAGE_DEVICE;
    }

    protected void pickMultipleImages() {
        imagePicker = prepareImagePicker();
        imagePicker.allowMultiple();
        imagePicker.pickImage();
        pickerType = Picker.PICK_IMAGE_DEVICE;
    }

    protected void pickImageFromCamera() {
        cameraImagePicker = prepareCameraImagePicker();
        pickerPath = cameraImagePicker.pickImage();
        pickerType = Picker.PICK_IMAGE_CAMERA;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_CAMERA || requestCode == Picker.PICK_IMAGE_DEVICE) {
                ImagePickerImpl imagePickerImpl = null;
                if (pickerType == Picker.PICK_IMAGE_DEVICE) {
                    imagePickerImpl = imagePicker;
                } else if (pickerType == Picker.PICK_IMAGE_CAMERA) {
                    imagePickerImpl = cameraImagePicker;
                }
                imagePickerImpl.submit(data);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("mpl_picker_type", pickerType);
        outState.putString("mpl_picker_path", pickerPath);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private ImagePicker prepareImagePicker() {
        ImagePicker imagePicker = new ImagePicker(this);
        imagePicker.setImagePickerCallback(this);
        return imagePicker;
    }

    private CameraImagePicker prepareCameraImagePicker() {
        CameraImagePicker imagePicker = new CameraImagePicker(this);
        imagePicker.setImagePickerCallback(this);
        return imagePicker;
    }
}
