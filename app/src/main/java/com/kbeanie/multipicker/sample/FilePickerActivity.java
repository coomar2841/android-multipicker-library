package com.kbeanie.multipicker.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.sample.adapters.ResultsAdapter;

import java.util.List;

/**
 * Created by kbibek on 2/19/16.
 */
public class FilePickerActivity extends AbActivity implements FilePickerCallback {
    private final static String TAG = FilePickerActivity.class.getSimpleName();

    private FilePicker filePicker;
    private ListView lvResults;
    private Button btFileSingle;
    private Button btFileMultiple;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker_activity);

        getSupportActionBar().setTitle("File Picker");
        getSupportActionBar().setSubtitle("Activity example");

        lvResults = (ListView) findViewById(R.id.lvResults);
        btFileSingle = (Button) findViewById(R.id.btFileSingle);
        btFileSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFilesSingle();
            }
        });
        btFileMultiple = (Button) findViewById(R.id.btFileMultiple);
        btFileMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFilesMultiple();
            }
        });
    }

    private void pickFilesSingle() {
        filePicker = getFilePicker();
        filePicker.pick();
    }

    private void pickFilesMultiple() {
        filePicker = getFilePicker();
        Bundle extras = new Bundle();
        extras.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        filePicker.setExtras(extras);
        filePicker.pick();
    }

    private FilePicker getFilePicker() {
        filePicker = new FilePicker(this, Picker.PICK_FILE);
        filePicker.setFilePickerCallback(this);
        return filePicker;
    }

    @Override
    public void onFilesChosen(List<ChosenFile> files) {
        for (ChosenFile file : files) {
            Log.i(TAG, "onFilesChosen: " + file);
        }

        ResultsAdapter adapter = new ResultsAdapter(files, this);
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Picker.PICK_FILE && resultCode == RESULT_OK) {
            filePicker.submit(requestCode, resultCode, data);
        }
    }
}
