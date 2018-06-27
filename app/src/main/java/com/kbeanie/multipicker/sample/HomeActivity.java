package com.kbeanie.multipicker.sample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.core.PickerManager;
import com.kbeanie.multipicker.sample.adapters.DemosAdapter;
import com.kbeanie.multipicker.sample.utils.PickerUtils;

/**
 * Created by kbibek on 2/18/16.
 */
public class HomeActivity extends AbActivity implements AdapterView.OnItemClickListener {

    private final static int EXTERNAL_STORAGE_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ListView lvDemoTypes = (ListView) findViewById(R.id.lvDemoTypes);
        lvDemoTypes.setAdapter(new DemosAdapter(this));
        lvDemoTypes.setOnItemClickListener(this);

        requestExternalStoragePermission();

        PickerManager.debugglable = true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (id == DemosAdapter.PICKER_CONTACT) {
            launchRequiredActivity(DemosAdapter.CONTACT_PICKER_ACTIVITY);
        } else if (id == DemosAdapter.PICKER_MEDIA) {
            launchRequiredActivity(DemosAdapter.MEDIA_PICKER_ACTIVITY);
        } else {
            showOptionsDialog((int) id);
        }
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
        switch (which) {
            case DemosAdapter.IMAGE_PICKER_ACTIVITY:
//                intent = new Intent(this, ImagePickerActivity.class);
                intent = new Intent(this, SimpleImagePickerActivity.class);
                break;
            case DemosAdapter.IMAGE_PICKER_FRAGMENT:
                intent = new Intent(this, ImagePickerFragmentActivity.class);
                break;
            case DemosAdapter.IMAGE_PICKER_SUPPORT_FRAGMENT:
                intent = new Intent(this, ImagePickerSupportFragmentActivity.class);
                break;
            case DemosAdapter.VIDEO_PICKER_ACTIVITY:
//                intent = new Intent(this, VideoPickerActivity.class);
                intent = new Intent(this, SimpleVideoPickerActivity.class);
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
            case DemosAdapter.AUDIO_PICKER_ACTIVITY:
                intent = new Intent(this, AudioPickerActivity.class);
                break;
            case DemosAdapter.AUDIO_PICKER_FRAGMENT:
                intent = new Intent(this, AudioPickerFragmentActivity.class);
                break;
            case DemosAdapter.AUDIO_PICKER_SUPPORT_FRAGMENT:
                intent = new Intent(this, AudioPickerSupportFragmentActivity.class);
                break;
            case DemosAdapter.MEDIA_PICKER_ACTIVITY:
                intent = new Intent(this, MediaPickerActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_storage) {
            showStorageSettingsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showStorageSettingsMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cache Location");
        CharSequence[] cacheLocations = new CharSequence[4];
        cacheLocations[0] = "Ext Storage - App Directory";
        cacheLocations[1] = "Ext Storage - Public Directory";
        cacheLocations[2] = "Ext Storage - Cache Directory";
        cacheLocations[3] = "Internal - App Directory";

        builder.setSingleChoiceItems(cacheLocations, preferences.getCacheLocation(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferences.setCacheLocation(which);
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void requestExternalStoragePermission() {
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                    EXTERNAL_STORAGE_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
