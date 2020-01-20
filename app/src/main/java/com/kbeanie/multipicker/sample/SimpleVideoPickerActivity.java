package com.kbeanie.multipicker.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.kbeanie.multipicker.api.ui.PickVideoActivity;
import com.kbeanie.multipicker.sample.adapters.MediaResultsAdapter;

import java.util.List;

/**
 * Created by kbibek on 27/04/17.
 */

public class SimpleVideoPickerActivity extends PickVideoActivity {
    private ListView lvResults;
    private Button btPickVideoSingle;
    private Button btPickVideoMultiple;
    private Button btTakeVideo;

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
                pickSingleVideo();
            }
        });
        btPickVideoMultiple = (Button) findViewById(R.id.btGalleryMultipleVideos);
        btPickVideoMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMultipleVideos();
            }
        });
        btTakeVideo = (Button) findViewById(R.id.btCameraVideo);
        btTakeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVideoFromCamera();
            }
        });
    }

    @Override
    public void onVideosChosen(List<ChosenVideo> videos) {
        MediaResultsAdapter adapter = new MediaResultsAdapter(videos, this);
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
