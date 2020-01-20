package com.kbeanie.multipicker.sample;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kbeanie.multipicker.api.MediaPicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.MediaPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.kbeanie.multipicker.sample.adapters.MediaResultsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kbibek on 2/19/16.
 */
public class MediaPickerActivity extends AbActivity implements MediaPickerCallback {
    private ListView lvResults;

    private Button btPickMedia;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_picker_activity);

        getSupportActionBar().setTitle("Media Picker");
        getSupportActionBar().setSubtitle("Activity example");

        lvResults = (ListView) findViewById(R.id.lvResults);
        btPickMedia = (Button) findViewById(R.id.btPickMedia);
        btPickMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMedia();
            }
        });
    }

    private MediaPicker picker;

    public void pickMedia() {
        picker = new MediaPicker(this);
        picker.allowMultiple();
        picker.setMediaPickerCallback(this);
        picker.pickMedia();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AbActivity.RESULT_OK) {
            if (requestCode == Picker.PICK_MEDIA) {
                if (picker == null) {
                    picker = new MediaPicker(this);
                    picker.setMediaPickerCallback(this);
                }
                picker.submit(data);
            }
        }
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMediaChosen(List<ChosenImage> images, List<ChosenVideo> videos) {
        List<ChosenFile> files = new ArrayList<>();
        if (images != null) {
            files.addAll(images);
        }
        if (videos != null) {
            files.addAll(videos);
        }
        MediaResultsAdapter adapter = new MediaResultsAdapter(files, this);
        lvResults.setAdapter(adapter);
    }
}
