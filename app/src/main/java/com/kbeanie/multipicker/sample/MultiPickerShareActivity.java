package com.kbeanie.multipicker.sample;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.kbeanie.multipicker.api.AudioPicker;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.kbeanie.multipicker.sample.adapters.MediaResultsAdapter;
import com.kbeanie.multipicker.utils.IntentUtils;

import java.util.List;

/**
 * Created by kbibek on 3/1/16.
 */
public class MultiPickerShareActivity extends AbActivity implements ImagePickerCallback, VideoPickerCallback, FilePickerCallback, AudioPickerCallback {

    private final static String TAG = MultiPickerShareActivity.class.getSimpleName();
    private String action;
    private String type;

    private ListView lvResults;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multipicker_share);

        lvResults = (ListView) findViewById(R.id.lvResults);

        if (getIntent() != null && getIntent().getType() != null && getIntent().getAction() != null) {
            type = getIntent().getType();
            action = getIntent().getAction();
            Log.i(TAG, "onCreate: Action: " + action + " Type: " + type);
        }

        startProcessing();
    }

    private void startProcessing() {
        if (action.equals(Intent.ACTION_SEND) || action.equals(Intent.ACTION_SEND_MULTIPLE)) {
            handleMultipleShares();
        }
    }

    private void handleMultipleShares() {
        if (type.startsWith("image")) {
            ImagePicker picker = new ImagePicker(this);
            picker.setImagePickerCallback(this);
            picker.submit(IntentUtils.getPickerIntentForSharing(getIntent()));
        } else if (type.startsWith("video")) {
            VideoPicker picker = new VideoPicker(this);
            picker.setVideoPickerCallback(this);
            picker.submit(IntentUtils.getPickerIntentForSharing(getIntent()));
        } else if (type.startsWith("application") || type.startsWith("file") || type.startsWith("*")) {
            FilePicker picker = new FilePicker(this);
            picker.setFilePickerCallback(this);
            picker.submit(IntentUtils.getPickerIntentForSharing(getIntent()));
        } else if (type.startsWith("audio")) {
            AudioPicker picker = new AudioPicker(this);
            picker.setAudioPickerCallback(this);
            picker.submit(IntentUtils.getPickerIntentForSharing(getIntent()));
        }
    }

    @Override
    public void onImagesChosen(List<ChosenImage> images) {
        MediaResultsAdapter adapter = new MediaResultsAdapter(images, this);
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVideosChosen(List<ChosenVideo> videos) {
        MediaResultsAdapter adapter = new MediaResultsAdapter(videos, this);
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onAudiosChosen(List<ChosenAudio> audios) {
        MediaResultsAdapter adapter = new MediaResultsAdapter(audios, this);
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onFilesChosen(List<ChosenFile> files) {
        MediaResultsAdapter adapter = new MediaResultsAdapter(files, this);
        lvResults.setAdapter(adapter);
    }
}
