package com.kbeanie.multipicker.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
        showOptionsDialog((int) id);
    }

    private void showDemo(Intent intent) {
        startActivity(intent);
    }

    private void showOptionsDialog(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose example type");
        CharSequence[] options = new CharSequence[3];
        options[0] = "Activity";
        options[1] = "Fragment";
        options[2] = "Support Fragment";
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                launchRequiredActivity(getWhich(id, which));
            }
        });

        builder.create().show();
    }

    private void launchRequiredActivity(int which) {
        Intent intent = null;
        switch ((int) which) {
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
            case DemosAdapter.AUDIO_PICKER_ACTIVITY:
                intent = new Intent(this, AudioPickerActivity.class);
                break;
        }

        if (intent != null) {
            showDemo(intent);
        }
    }

    private int[] IMAGE_OPTIONS = {DemosAdapter.IMAGE_PICKER_ACTIVITY, DemosAdapter.IMAGE_PICKER_FRAGMENT, DemosAdapter.IMAGE_PICKER_SUPPORT_FRAGMENT};
    private int[] VIDEO_OPTIONS = {DemosAdapter.VIDEO_PICKER_ACTIVITY, DemosAdapter.VIDEO_PICKER_FRAGMENT, DemosAdapter.VIDEO_PICKER_SUPPORT_FRAGMENT};
    private int[] FILE_OPTIONS = {DemosAdapter.FILE_PICKER_ACTIVITY, DemosAdapter.FILE_PICKER_FRAGMENT, DemosAdapter.FILE_PICKER_SUPPORT_FRAGMENT};
    private int[] AUDIO_OPTIONS = {DemosAdapter.AUDIO_PICKER_ACTIVITY, DemosAdapter.AUDIO_PICKER_FRAGMENT, DemosAdapter.AUDIO_PICKER_SUPPORT_FRAGMENT};

    private int getWhich(int id, int index) {
        switch (id) {
            case 1:
                return IMAGE_OPTIONS[index];
            case 2:
                return VIDEO_OPTIONS[index];
            case 3:
                return FILE_OPTIONS[index];
            case 4:
                return AUDIO_OPTIONS[index];
        }
        return -1;
    }
}
