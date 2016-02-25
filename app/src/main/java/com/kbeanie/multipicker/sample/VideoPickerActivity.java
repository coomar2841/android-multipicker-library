package com.kbeanie.multipicker.sample;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.kbeanie.multipicker.sample.adapters.ResultsAdapter;
import com.kbeanie.multipicker.sample.utils.PickerUtils;

import java.util.List;

/**
 * Created by kbibek on 2/19/16.
 */
public class VideoPickerActivity extends AbActivity implements VideoPickerCallback {

    private ListView lvResults;

    private Button btPickVideoSingle;
    private Button btPickVideoMultiple;
    private Button btTakeVideo;

    private int pickerType;
    private String pickerPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_picker_activity);

        getSupportActionBar().setTitle("Video Picker");
        getSupportActionBar().setSubtitle("Activity example");

        lvResults = (ListView) findViewById(R.id.lvResults);
        btPickVideoSingle = (Button) findViewById(R.id.btGallerySingleVideo);
        btPickVideoSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVideoSingle();
            }
        });
        btPickVideoMultiple = (Button) findViewById(R.id.btGalleryMultipleVideos);
        btPickVideoMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVideoMultiple();
            }
        });
        btTakeVideo = (Button) findViewById(R.id.btCameraVideo);
        btTakeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeVideo();
            }
        });
    }

    private VideoPicker picker;

    private void pickVideoSingle() {
        pickerType = Picker.PICK_VIDEO_DEVICE;
        picker = getVideoPicker(Picker.PICK_VIDEO_DEVICE);
        pickerPath = picker.pick();
    }

    private void pickVideoMultiple() {
        pickerType = Picker.PICK_VIDEO_DEVICE;
        picker = getVideoPicker(Picker.PICK_VIDEO_DEVICE);
        Bundle extras = new Bundle();
        extras.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        picker.setExtras(extras);
        pickerPath = picker.pick();
    }

    private void takeVideo() {
        pickerType = Picker.PICK_VIDEO_CAMERA;
        picker = getVideoPicker(Picker.PICK_VIDEO_CAMERA);
        Bundle extras = new Bundle();
        // For capturing Low quality videos; Default is 1: HIGH
        extras.putInt(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        // Set the duration of the video
        extras.putInt(MediaStore.EXTRA_DURATION_LIMIT, 5);
        pickerPath = picker.pick();
    }

    private VideoPicker getVideoPicker(int pickerType) {
        VideoPicker picker = new VideoPicker(this, pickerType);
        picker.setVideoPickerCallback(this);
        picker.shouldGenerateMetadata(true);
        picker.shouldGeneratePreviewImages(true);
        picker.setCacheLocation(PickerUtils.getSavedCacheLocation(this));
        return picker;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == Picker.PICK_VIDEO_CAMERA || requestCode == Picker.PICK_VIDEO_DEVICE) && resultCode == RESULT_OK) {
            if (picker == null) {
                picker = getVideoPicker(pickerType);
                picker.reinitialize(pickerPath);
            }
            picker.submit(requestCode, resultCode, data);
        }
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

    @Override
    public void onVideosChosen(List<ChosenVideo> files) {
        ResultsAdapter adapter = new ResultsAdapter(files, this);
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {

    }
}
