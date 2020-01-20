package com.kbeanie.multipicker.sample.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kbeanie.multipicker.api.CameraVideoPicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.kbeanie.multipicker.sample.R;
import com.kbeanie.multipicker.sample.adapters.MediaResultsAdapter;

import java.util.List;

/**
 * Created by kbibek on 2/25/16.
 */
public class VideoPickerSupportFragment extends Fragment implements VideoPickerCallback {
    private ListView lvResults;

    private Button btPickVideoSingle;
    private Button btPickVideoMultiple;
    private Button btTakeVideo;
    private String pickerPath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_picker, null);

        lvResults = (ListView) view.findViewById(R.id.lvResults);
        btPickVideoSingle = (Button) view.findViewById(R.id.btGallerySingleVideo);
        btPickVideoSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVideoSingle();
            }
        });
        btPickVideoMultiple = (Button) view.findViewById(R.id.btGalleryMultipleVideos);
        btPickVideoMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickVideoMultiple();
            }
        });
        btTakeVideo = (Button) view.findViewById(R.id.btCameraVideo);
        btTakeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeVideo();
            }
        });
        return view;
    }

    private VideoPicker videoPicker;

    private void pickVideoSingle() {
        videoPicker = new VideoPicker(this);
        videoPicker.setVideoPickerCallback(this);
        videoPicker.shouldGenerateMetadata(true);
        videoPicker.shouldGeneratePreviewImages(true);
        videoPicker.pickVideo();
    }

    private void pickVideoMultiple() {
        videoPicker = new VideoPicker(this);
        videoPicker.setVideoPickerCallback(this);
        videoPicker.shouldGenerateMetadata(true);
        videoPicker.shouldGeneratePreviewImages(true);
        videoPicker.allowMultiple();
        videoPicker.pickVideo();
    }

    private CameraVideoPicker cameraPicker;

    private void takeVideo() {
        Bundle extras = new Bundle();
        // For capturing Low quality videos; Default is 1: HIGH
        extras.putInt(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        // Set the duration of the video
        extras.putInt(MediaStore.EXTRA_DURATION_LIMIT, 5);

        cameraPicker = new CameraVideoPicker(this);
        cameraPicker.shouldGenerateMetadata(true);
        cameraPicker.shouldGeneratePreviewImages(true);
        cameraPicker.setVideoPickerCallback(this);
        cameraPicker.pickVideo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Picker.PICK_VIDEO_DEVICE) {
                if (videoPicker == null) {
                    videoPicker = new VideoPicker(this);
                    videoPicker.setVideoPickerCallback(this);
                }
                videoPicker.submit(data);
            } else if (requestCode == Picker.PICK_VIDEO_CAMERA) {
                if (cameraPicker == null) {
                    cameraPicker = new CameraVideoPicker(this, pickerPath);
                    cameraPicker.setVideoPickerCallback(this);
                    cameraPicker.setVideoPickerCallback(this);
                }
                cameraPicker.submit(data);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // You have to save these two values in case your activity is killed.
        // In such a scenario, you will need to re-initialize the ImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
    }

    @Override
    public void onVideosChosen(List<ChosenVideo> files) {
        MediaResultsAdapter adapter = new MediaResultsAdapter(files, getActivity());
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
