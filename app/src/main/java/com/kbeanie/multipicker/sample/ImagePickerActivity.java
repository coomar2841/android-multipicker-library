package com.kbeanie.multipicker.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.sample.adapters.ResultsAdapter;

import java.util.List;

/**
 * Created by kbibek on 2/19/16.
 */
public class ImagePickerActivity extends AbActivity implements ImagePickerCallback {
    private ListView lvResults;

    private Button btPickImageSingle;
    private Button btPickImageMultiple;
    private Button btTakePicture;

    private int pickerType;
    private String pickerPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker_activity);

        getSupportActionBar().setTitle("Image Picker");
        getSupportActionBar().setSubtitle("Activity example");

        lvResults = (ListView) findViewById(R.id.lvResults);
        btPickImageSingle = (Button) findViewById(R.id.btGallerySingleImage);
        btPickImageSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageSingle();
            }
        });
        btPickImageMultiple = (Button) findViewById(R.id.btGalleryMultipleImages);
        btPickImageMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageMultiple();
            }
        });
        btTakePicture = (Button) findViewById(R.id.btCameraImage);
        btTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
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
        return manager;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == Picker.PICK_IMAGE_CAMERA || requestCode == Picker.PICK_IMAGE_DEVICE) && resultCode == RESULT_OK) {
            if (imagePicker == null) {
                imagePicker = getImagePickerManager(pickerType);
                imagePicker.reinitialize(pickerPath);
            }
            imagePicker.submit(requestCode, resultCode, data);
        }
    }

    @Override
    public void onImagesChosen(List<ChosenImage> images) {
        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
        ResultsAdapter adapter = new ResultsAdapter(images, this);
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save these two values in case your activity is killed.
        // In such a scenario, you will need to re-initialize the ImagePicker
        outState.putInt("picker_type", pickerType);
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize ImagePicker
        if (savedInstanceState.containsKey("picker_type")) {
            pickerType = savedInstanceState.getInt("picker_type");
        }
        if (savedInstanceState.containsKey("picker_path")) {
            pickerPath = savedInstanceState.getString("picker_path");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
