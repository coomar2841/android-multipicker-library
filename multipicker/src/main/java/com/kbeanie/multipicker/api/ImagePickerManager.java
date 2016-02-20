package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.threads.ImageProcessorThread;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kbibek on 2/18/16.
 */
public class ImagePickerManager extends PickerManager {
    private final static String TAG = ImagePickerManager.class.getSimpleName();
    private String path;

    public ImagePickerManager(Activity activity, int pickerType) {
        super(activity, pickerType);
    }

    public ImagePickerManager(Fragment fragment, int pickerType) {
        super(fragment, pickerType);
    }

    public ImagePickerManager(android.app.Fragment appFragment, int pickerType) {
        super(appFragment, pickerType);
    }

    public void reinitialize(String path) {
        this.path = path;
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

    protected String pickLocalImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (extras != null) {
            intent.putExtras(extras);
        }
        // For reading from external storage (Content Providers)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickInternal(intent, Picker.PICK_IMAGE_DEVICE);
        return null;
    }

    protected String takePictureWithCamera() {
        String tempFilePath = buildFilePath("jpg", Environment.DIRECTORY_PICTURES);
        Uri uri = Uri.fromFile(new File(tempFilePath));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.putExtra("data", uri);
        Log.d(TAG, "Temp Path for Camera capture: " + tempFilePath);
        pickInternal(intent, Picker.PICK_IMAGE_CAMERA);
        return tempFilePath;
    }

    protected void pickInternal(Intent intent, int type) {
        if (activity != null) {
            activity.startActivityForResult(intent, type);
        } else if (fragment != null) {
            fragment.startActivityForResult(intent, type);
        } else if (appFragment != null) {
            appFragment.startActivityForResult(intent, type);
        }
    }

    @Override
    public void submit(int requestCode, int resultCode, Intent data) {
        if (requestCode != pickerType) {
            onError("onActivityResult requestCode is different from the type the chooser was initialized with.");
        } else {
            if (pickerType == Picker.PICK_IMAGE_CAMERA) {
                handleCameraData(data);
            } else if (pickerType == Picker.PICK_IMAGE_DEVICE) {
                handleGalleryData(data);
            }
        }
    }

    private void handleCameraData(Intent intent) {
        Log.d(TAG, "handleCameraData: " + path);
    }

    private void handleGalleryData(Intent intent) {
        List<String> uris = new ArrayList<>();
        if (intent != null) {
            if (intent.getDataString() != null) {
                String uri = intent.getDataString();
                Log.d(TAG, "handleGalleryData: " + uri);
                uris.add(uri);
            } else if (intent.getClipData() != null) {
                ClipData clipData = intent.getClipData();
                Log.d(TAG, "handleGalleryData: Multiple images with ClipData");
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Log.d(TAG, "Item [" + i + "]: " + item.getUri().toString());
                    uris.add(item.getUri().toString());
                }
            }

            processImages(uris);
        }
    }

    private void onError(String errorMessage) {
        Log.d(getClass().getName(), "onError: " + errorMessage);
    }

    private void processImages(List<String> uris) {
        ImageProcessorThread thread = new ImageProcessorThread(getContext(), getImageObjects(uris), cacheLocation);
        thread.start();
    }

    private List<ChosenImage> getImageObjects(List<String> uris) {
        List<ChosenImage> images = new ArrayList<>();
        for (String uri : uris) {
            ChosenImage image = new ChosenImage();
            image.setQueryUri(uri);
            image.setDirectoryType(Environment.DIRECTORY_PICTURES);
            image.setType("image");
            images.add(image);
        }
        return images;
    }
}
