package com.kbeanie.multipicker.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * Created by kbibek on 2/19/16.
 */
public class VideoPickerFragmentActivity extends AbActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_picker_fragment);

        getSupportActionBar().setTitle("Video Picker");
        getSupportActionBar().setSubtitle("Fragment example");
    }
}
