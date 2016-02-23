package com.kbeanie.multipicker.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kbeanie.multipicker.api.ImagePickerManager;
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

    private ImagePickerManager imagePickerManager;

    public void pickImageSingle() {
        imagePickerManager = getImagePickerManager(Picker.PICK_IMAGE_DEVICE);
        String tempPath = imagePickerManager.pick();
    }

    public void pickImageMultiple() {
        imagePickerManager = getImagePickerManager(Picker.PICK_IMAGE_DEVICE);
        Bundle extras = new Bundle();
        extras.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imagePickerManager.setExtras(extras);
        String tempPath = imagePickerManager.pick();
    }

    public void takePicture() {
        imagePickerManager = getImagePickerManager(Picker.PICK_IMAGE_CAMERA);
        String tempPath = imagePickerManager.pick();
    }

    private ImagePickerManager getImagePickerManager(int type) {
        ImagePickerManager manager = new ImagePickerManager(this, type);
        manager.shouldGenerateMetadata(true);
        manager.shouldGenerateThumbnails(true);
        manager.setImagePickerCallback(this);
        return manager;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == Picker.PICK_IMAGE_CAMERA || requestCode == Picker.PICK_IMAGE_DEVICE) && resultCode == RESULT_OK) {
            if (imagePickerManager != null) {
                imagePickerManager.submit(requestCode, resultCode, data);
            }
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
}
