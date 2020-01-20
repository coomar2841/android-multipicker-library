package com.kbeanie.multipicker.sample;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.sample.adapters.MediaResultsAdapter;
import com.kbeanie.multipicker.sample.utils.PickerUtils;

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
        filePicker.setMimeType("application/pdf");
        filePicker.pickFile();
    }

    private void pickFilesMultiple() {
        filePicker = getFilePicker();
        filePicker.allowMultiple();
        filePicker.pickFile();
    }

    private FilePicker getFilePicker() {
        filePicker = new FilePicker(this);
        filePicker.setFilePickerCallback(this);
        filePicker.setCacheLocation(PickerUtils.getSavedCacheLocation(this));
        return filePicker;
    }

    @Override
    public void onFilesChosen(List<ChosenFile> files) {
        for (ChosenFile file : files) {
            Log.d(TAG, "onFilesChosen: " + file);
        }

        MediaResultsAdapter adapter = new MediaResultsAdapter(files, this);
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Picker.PICK_FILE && resultCode == RESULT_OK) {
            filePicker.submit(data);
        }
    }
}
