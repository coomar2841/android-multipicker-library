package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.core.VideoPickerImpl;

/**
 * Captures a video using the device's Camera
 */
public final class CameraVideoPicker extends VideoPickerImpl {

    /**
     * Constructor for triggering Video capture from an {@link Activity}
     * @param activity
     */
    public CameraVideoPicker(Activity activity) {
        super(activity, Picker.PICK_VIDEO_CAMERA);
    }

    /**
     * Constructor for triggering Video capture from a {@link Fragment}
     * @param fragment
     */
    public CameraVideoPicker(Fragment fragment) {
        super(fragment, Picker.PICK_VIDEO_CAMERA);
    }

    /**
     * Constructor for triggering Video capture from a {@link android.app.Fragment}
     * @param appFragment
     */
    public CameraVideoPicker(android.app.Fragment appFragment) {
        super(appFragment, Picker.PICK_VIDEO_CAMERA);
    }

    /**
     * Re-initialize {@link CameraVideoPicker} object if your activity is destroyed
     * @param activity
     * @param path
     */
    public CameraVideoPicker(Activity activity, String path) {
        super(activity, Picker.PICK_VIDEO_CAMERA);
        reinitialize(path);
    }

    /**
     * Re-initialize {@link CameraVideoPicker} object if your activity is destroyed
     * @param fragment
     * @param path
     */
    public CameraVideoPicker(Fragment fragment, String path) {
        super(fragment, Picker.PICK_VIDEO_CAMERA);
        reinitialize(path);
    }

    /**
     * Re-initialize {@link CameraVideoPicker} object if your activity is destroyed
     * @param appFragment
     * @param path
     */
    public CameraVideoPicker(android.app.Fragment appFragment, String path) {
        super(appFragment, Picker.PICK_VIDEO_CAMERA);
        reinitialize(path);
    }

    /**
     * Trigger Video Capture using the device's Camera
     * @return
     */
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

    @Override
    public void setCacheLocation(int cacheLocation) {
        if (cacheLocation == CacheLocation.INTERNAL_APP_DIR) {
            throw new RuntimeException("Cannot use CacheLocation.INTERNAL_APP_DIR for taking videos. Please use another cache location.");
        }
        super.setCacheLocation(cacheLocation);
    }
}
