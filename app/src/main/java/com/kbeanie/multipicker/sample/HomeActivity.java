package com.kbeanie.multipicker.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kbeanie.multipicker.sample.adapters.DemosAdapter;

/**
 * Created by kbibek on 2/18/16.
 */
public class HomeActivity extends AbActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ListView lvDemoTypes = (ListView) findViewById(R.id.lvDemoTypes);
        lvDemoTypes.setAdapter(new DemosAdapter(this));
        lvDemoTypes.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = null;
        switch ((int) id) {
            case DemosAdapter.IMAGE_PICKER_ACTIVITY:
                intent = new Intent(this, ImagePickerActivity.class);
                break;
            case DemosAdapter.IMAGE_PICKER_FRAGMENT:
                intent = new Intent(this, ImagePickerFragmentActivity.class);
                break;
            case DemosAdapter.IMAGE_PICKER_SUPPORT_FRAGMENT:
                intent = new Intent(this, ImagePickerSupportFragmentActivity.class);
                break;
            case DemosAdapter.VIDEO_PICKER_ACTIVITY:
                intent = new Intent(this, VideoPickerActivity.class);
                break;
            case DemosAdapter.VIDEO_PICKER_FRAGMENT:
                intent = new Intent(this, VideoPickerFragmentActivity.class);
                break;
            case DemosAdapter.VIDEO_PICKER_SUPPORT_FRAGMENT:
                intent = new Intent(this, VideoPickerSupportFragmentActivity.class);
                break;
            case DemosAdapter.FILE_PICKER_ACTIVITY:
                intent = new Intent(this, FilePickerActivity.class);
                break;
            case DemosAdapter.FILE_PICKER_FRAGMENT:
                intent = new Intent(this, FilePickerFragmentActivity.class);
                break;
            case DemosAdapter.FILE_PICKER_SUPPORT_FRAGMENT:
                intent = new Intent(this, FilePickerSupportFragmentActivity.class);
                break;
            case DemosAdapter.CONTACT_PICKER_ACTIVITY:
                intent = new Intent(this, ContactPickerActivity.class);
                break;
            case DemosAdapter.CONTACT_PICKER_FRAGMENT:
                intent = new Intent(this, ContactPickerFragmentActivity.class);
                break;
            case DemosAdapter.CONTACT_PICKER_SUPPORT_FRAGMENT:
                intent = new Intent(this, ContactPickerSupportFragmentActivity.class);
                break;
        }

        if (intent != null) {
            showDemo(intent);
        }
    }

    private void showDemo(Intent intent) {
        startActivity(intent);
    }
}
