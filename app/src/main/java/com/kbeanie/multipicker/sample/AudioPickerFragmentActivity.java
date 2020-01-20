package com.kbeanie.multipicker.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;

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
