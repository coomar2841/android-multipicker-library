package com.kbeanie.multipicker.sample.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.kbeanie.multipicker.sample.R;
import com.kbeanie.multipicker.sample.adapters.MediaResultsAdapter;
import com.kbeanie.multipicker.sample.utils.PickerUtils;

import java.util.List;

/**
 * Created by kbibek on 2/25/16.
 */
public class VideoPickerFragment extends Fragment implements VideoPickerCallback {
    private ListView lvResults;

    private Button btPickVideoSingle;
    private Button btPickVideoMultiple;
    private Button btTakeVideo;

    private int pickerType;
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
        picker.setCacheLocation(PickerUtils.getSavedCacheLocation(getActivity()));
        return picker;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == Picker.PICK_VIDEO_CAMERA || requestCode == Picker.PICK_VIDEO_DEVICE) && resultCode == Activity.RESULT_OK) {
            if (picker == null) {
                picker = getVideoPicker(pickerType);
                picker.reinitialize(pickerPath);
            }
            picker.submit(requestCode, resultCode, data);
        }
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

    @Override
    public void onVideosChosen(List<ChosenVideo> files) {
        MediaResultsAdapter adapter = new MediaResultsAdapter(files, getActivity());
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {

    }
}
