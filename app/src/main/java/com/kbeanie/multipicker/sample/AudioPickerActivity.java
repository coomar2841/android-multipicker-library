package com.kbeanie.multipicker.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.kbeanie.multipicker.api.AudioPicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.sample.adapters.MediaResultsAdapter;
import com.kbeanie.multipicker.sample.utils.PickerUtils;

import java.util.List;

/**
 * Created by kbibek on 2/19/16.
 */
public class AudioPickerActivity extends AbActivity implements AudioPickerCallback {
    private final static String TAG = AudioPickerActivity.class.getSimpleName();

    private AudioPicker audioPicker;
    private ListView lvResults;
    private Button btAudioSingle;
    private Button btAudioMultiple;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_picker_activity);

        getSupportActionBar().setTitle("Audio Picker");
        getSupportActionBar().setSubtitle("Activity example");

        lvResults = (ListView) findViewById(R.id.lvResults);
        btAudioSingle = (Button) findViewById(R.id.btAudioSingle);
        btAudioSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFilesSingle();
            }
        });
        btAudioMultiple = (Button) findViewById(R.id.btAudioMultiple);
        btAudioMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFilesMultiple();
            }
        });
    }

    private void pickFilesSingle() {
        audioPicker = getAudioPicker();
        audioPicker.pick();
    }

    private void pickFilesMultiple() {
        audioPicker = getAudioPicker();
        Bundle extras = new Bundle();
        extras.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        audioPicker.setExtras(extras);
        audioPicker.pick();
    }

    private AudioPicker getAudioPicker() {
        audioPicker = new AudioPicker(this, Picker.PICK_AUDIO);
        audioPicker.setAudioPickerCallback(this);
        audioPicker.setCacheLocation(PickerUtils.getSavedCacheLocation(this));
        return audioPicker;
    }

    @Override
    public void onError(String message) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Picker.PICK_AUDIO && resultCode == RESULT_OK) {
            audioPicker.submit(requestCode, resultCode, data);
        }
    }

    @Override
    public void onAudiosChosen(List<ChosenAudio> audios) {
        for (ChosenAudio audio : audios) {
            Log.i(TAG, "onFilesChosen: " + audio);
        }

        MediaResultsAdapter adapter = new MediaResultsAdapter(audios, this);
        lvResults.setAdapter(adapter);
    }
}
