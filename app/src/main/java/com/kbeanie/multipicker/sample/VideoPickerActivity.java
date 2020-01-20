package com.kbeanie.multipicker.sample;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraVideoPicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.kbeanie.multipicker.sample.adapters.MediaResultsAdapter;

import java.util.List;

/**
 * Created by kbibek on 2/19/16.
 */
public class VideoPickerActivity extends AbActivity implements VideoPickerCallback {

    private ListView lvResults;

    private Button btPickVideoSingle;
    private Button btPickVideoMultiple;
    private Button btTakeVideo;

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

    private VideoPicker videoPicker;

    private void pickVideoSingle() {
        videoPicker = new VideoPicker(this);
        videoPicker.shouldGenerateMetadata(true);
        videoPicker.shouldGeneratePreviewImages(true);
        videoPicker.setVideoPickerCallback(this);
        videoPicker.pickVideo();
    }

    private void pickVideoMultiple() {
        videoPicker = new VideoPicker(this);
        videoPicker.shouldGenerateMetadata(true);
        videoPicker.shouldGeneratePreviewImages(true);
        videoPicker.setVideoPickerCallback(this);
        videoPicker.allowMultiple();
        videoPicker.pickVideo();
    }

    private CameraVideoPicker cameraPicker;

    private void takeVideo() {
        cameraPicker = new CameraVideoPicker(this);
        cameraPicker.shouldGenerateMetadata(true);
        cameraPicker.setCacheLocation(CacheLocation.INTERNAL_APP_DIR);
        cameraPicker.shouldGeneratePreviewImages(true);
        Bundle extras = new Bundle();
        // For capturing Low quality videos; Default is 1: HIGH
        extras.putInt(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        // Set the duration of the video
        extras.putInt(MediaStore.EXTRA_DURATION_LIMIT, 5);
        cameraPicker.setExtras(extras);
        cameraPicker.setVideoPickerCallback(this);
        pickerPath = cameraPicker.pickVideo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_VIDEO_DEVICE) {
                if (videoPicker == null) {
                    videoPicker = new VideoPicker(this);
                    videoPicker.setVideoPickerCallback(this);
                }
                videoPicker.submit(data);
            } else if (requestCode == Picker.PICK_VIDEO_CAMERA) {
                if (cameraPicker == null) {
                    cameraPicker = new CameraVideoPicker(this, pickerPath);
                }
                cameraPicker.submit(data);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraVideoPicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // path value to be able to re-intialize CameraVideoPicker
        if (savedInstanceState.containsKey("picker_path")) {
            pickerPath = savedInstanceState.getString("picker_path");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onVideosChosen(List<ChosenVideo> files) {
        MediaResultsAdapter adapter = new MediaResultsAdapter(files, this);
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
