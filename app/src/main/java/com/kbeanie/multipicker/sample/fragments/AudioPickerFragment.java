package com.kbeanie.multipicker.sample.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kbeanie.multipicker.api.AudioPicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.sample.R;
import com.kbeanie.multipicker.sample.adapters.MediaResultsAdapter;
import com.kbeanie.multipicker.sample.utils.PickerUtils;

import java.util.List;

/**
 * Created by kbibek on 2/26/16.
 */
public class AudioPickerFragment extends Fragment implements AudioPickerCallback {
    private final static String TAG = AudioPickerFragment.class.getSimpleName();

    private AudioPicker audioPicker;
    private ListView lvResults;
    private Button btAudioSingle;
    private Button btAudioMultiple;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_picker, null);
        lvResults = (ListView) view.findViewById(R.id.lvResults);
        btAudioSingle = (Button) view.findViewById(R.id.btAudioSingle);
        btAudioSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFilesSingle();
            }
        });
        btAudioMultiple = (Button) view.findViewById(R.id.btAudioMultiple);
        btAudioMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFilesMultiple();
            }
        });
        return view;
    }

    private void pickFilesSingle() {
        audioPicker = getAudioPicker();
        audioPicker.pickAudio();
    }

    private void pickFilesMultiple() {
        audioPicker = getAudioPicker();
        audioPicker.allowMultiple();
        audioPicker.pickAudio();
    }

    private AudioPicker getAudioPicker() {
        audioPicker = new AudioPicker(this);
        audioPicker.setAudioPickerCallback(this);
        audioPicker.setCacheLocation(PickerUtils.getSavedCacheLocation(getContext()));
        return audioPicker;
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Picker.PICK_AUDIO && resultCode == Activity.RESULT_OK) {
            audioPicker.submit(data);
        }
    }

    @Override
    public void onAudiosChosen(List<ChosenAudio> audios) {
        for (ChosenAudio audio : audios) {
            Log.d(TAG, "onFilesChosen: " + audio);
        }

        MediaResultsAdapter adapter = new MediaResultsAdapter(audios, getActivity());
        lvResults.setAdapter(adapter);
    }
}
