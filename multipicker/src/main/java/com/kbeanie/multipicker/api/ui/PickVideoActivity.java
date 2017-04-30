package com.kbeanie.multipicker.api.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.CameraVideoPicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.core.ImagePickerImpl;
import com.kbeanie.multipicker.core.VideoPickerImpl;

/**
 * Helper activity to pick videos from your device's storage or using Camera.
 */

public abstract class PickVideoActivity extends AppCompatActivity implements VideoPickerCallback {
    private VideoPicker videoPicker;
    private CameraVideoPicker cameraVideoPicker;
    private int pickerType;
    private String pickerPath;

    /**
     * Used to return a single image from your phone's storage
     */
    protected void pickSingleVideo() {
        videoPicker = prepareVideoPicker();
        videoPicker.pickVideo();
        pickerType = Picker.PICK_VIDEO_DEVICE;
    }

    /**
     * Used to return multiple images from your phone's storage
     */
    protected void pickMultipleVideos() {
        videoPicker = prepareVideoPicker();
        videoPicker.allowMultiple();
        videoPicker.pickVideo();
        pickerType = Picker.PICK_VIDEO_DEVICE;
    }

    /**
     * Used to return single image using your phone's camera
     */
    protected void pickVideoFromCamera() {
        cameraVideoPicker = prepareCameraVideoPicker();
        pickerPath = cameraVideoPicker.pickVideo();
        pickerType = Picker.PICK_VIDEO_CAMERA;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_VIDEO_CAMERA || requestCode == Picker.PICK_VIDEO_DEVICE) {
                VideoPickerImpl videoPickerImpl = null;
                if (pickerType == Picker.PICK_VIDEO_DEVICE) {
                    if (videoPicker == null) {
                        videoPicker = prepareVideoPicker();
                    }
                    videoPickerImpl = videoPicker;
                } else if (pickerType == Picker.PICK_VIDEO_CAMERA) {
                    if (cameraVideoPicker == null) {
                        cameraVideoPicker = prepareCameraVideoPicker();
                        cameraVideoPicker.reinitialize(pickerPath);
                    }
                    videoPickerImpl = cameraVideoPicker;
                }
                videoPickerImpl.submit(data);
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("mpl_picker_type", pickerType);
        outState.putString("mpl_picker_path", pickerPath);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pickerType = savedInstanceState.getInt("mpl_picker_type");
        pickerPath = savedInstanceState.getString("mpl_picker_path");
    }

    private VideoPicker prepareVideoPicker() {
        VideoPicker videoPicker = new VideoPicker(this);
        videoPicker.setVideoPickerCallback(this);
        return videoPicker;
    }

    private CameraVideoPicker prepareCameraVideoPicker() {
        CameraVideoPicker imagePicker = new CameraVideoPicker(this);
        imagePicker.setVideoPickerCallback(this);
        return imagePicker;
    }
}
