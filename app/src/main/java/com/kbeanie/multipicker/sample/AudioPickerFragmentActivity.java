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
import com.kbeanie.multipicker.sample.adapters.ResultsAdapter;
import com.kbeanie.multipicker.sample.utils.PickerUtils;

import java.util.List;

/**
 * Created by kbibek on 2/19/16.
 */
public class AudioPickerFragmentActivity extends AbActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_picker_fragment_activity);

        getSupportActionBar().setTitle("Audio Picker");
        getSupportActionBar().setSubtitle("Activity example");
    }
}
