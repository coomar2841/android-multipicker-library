package com.kbeanie.multipicker.sample.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.sample.R;
import com.kbeanie.multipicker.sample.adapters.ResultsAdapter;
import com.kbeanie.multipicker.sample.utils.PickerUtils;

import java.util.List;

/**
 * Created by kbibek on 2/25/16.
 */
public class ImagePickerSupportFragment extends android.support.v4.app.Fragment implements ImagePickerCallback {

    private ListView lvResults;

    private Button btPickImageSingle;
    private Button btPickImageMultiple;
    private Button btTakePicture;

    private int pickerType;
    private String pickerPath;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_picker, null);

        lvResults = (ListView) view.findViewById(R.id.lvResults);
        btPickImageSingle = (Button) view.findViewById(R.id.btGallerySingleImage);
        btPickImageSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageSingle();
            }
        });
        btPickImageMultiple = (Button) view.findViewById(R.id.btGalleryMultipleImages);
        btPickImageMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageMultiple();
            }
        });
        btTakePicture = (Button) view.findViewById(R.id.btCameraImage);
        btTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        return view;
    }

    private ImagePicker imagePicker;

    public void pickImageSingle() {
        pickerType = Picker.PICK_IMAGE_DEVICE;
        imagePicker = getImagePickerManager(Picker.PICK_IMAGE_DEVICE);
        pickerPath = imagePicker.pick();

    }

    public void pickImageMultiple() {
        pickerType = Picker.PICK_IMAGE_DEVICE;
        imagePicker = getImagePickerManager(Picker.PICK_IMAGE_DEVICE);
        Bundle extras = new Bundle();
        extras.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imagePicker.setExtras(extras);
        pickerPath = imagePicker.pick();
    }

    public void takePicture() {
        pickerType = Picker.PICK_IMAGE_CAMERA;
        imagePicker = getImagePickerManager(Picker.PICK_IMAGE_CAMERA);
        pickerPath = imagePicker.pick();
    }

    private ImagePicker getImagePickerManager(int type) {
        ImagePicker manager = new ImagePicker(this, type);
        manager.shouldGenerateMetadata(true);
        manager.shouldGenerateThumbnails(true);
        manager.setImagePickerCallback(this);
        manager.setCacheLocation(PickerUtils.getSavedCacheLocation(getActivity()));
        return manager;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == Picker.PICK_IMAGE_CAMERA || requestCode == Picker.PICK_IMAGE_DEVICE) && resultCode == Activity.RESULT_OK) {
            if (imagePicker == null) {
                imagePicker = getImagePickerManager(pickerType);
                imagePicker.reinitialize(pickerPath);
            }
            imagePicker.submit(requestCode, resultCode, data);
        }
    }

    @Override
    public void onImagesChosen(List<ChosenImage> images) {
        ResultsAdapter adapter = new ResultsAdapter(images, getActivity());
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // You have to save these two values in case your activity is killed.
        // In such a scenario, you will need to re-initialize the ImagePicker
        outState.putInt("picker_type", pickerType);
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_type")) {
                pickerType = savedInstanceState.getInt("picker_type");
            }
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
    }
}
