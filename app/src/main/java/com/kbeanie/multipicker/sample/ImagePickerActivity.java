package com.kbeanie.multipicker.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

/**
 * Created by kbibek on 2/19/16.
 */
public class ImagePickerActivity extends AbActivity {
    ListView lvResults;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker_activity);

        getSupportActionBar().setTitle("Image Picker");
        getSupportActionBar().setSubtitle("Activity example");

        lvResults = (ListView) findViewById(R.id.lvResults);
        lvResults.setEmptyView(getEmptyView());
    }
}
