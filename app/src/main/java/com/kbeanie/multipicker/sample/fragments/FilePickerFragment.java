package com.kbeanie.multipicker.sample.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.sample.R;
import com.kbeanie.multipicker.sample.adapters.MediaResultsAdapter;
import com.kbeanie.multipicker.sample.utils.PickerUtils;

import java.util.List;

/**
 * Created by kbibek on 2/25/16.
 */
public class FilePickerFragment extends Fragment implements FilePickerCallback {
    private final static String TAG = FilePickerFragment.class.getSimpleName();

    private FilePicker filePicker;
    private ListView lvResults;
    private Button btFileSingle;
    private Button btFileMultiple;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_picker, null);
        lvResults = (ListView) view.findViewById(R.id.lvResults);
        btFileSingle = (Button) view.findViewById(R.id.btFileSingle);
        btFileSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFilesSingle();
            }
        });
        btFileMultiple = (Button) view.findViewById(R.id.btFileMultiple);
        btFileMultiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFilesMultiple();
            }
        });
        return view;
    }

    private void pickFilesSingle() {
        filePicker = getFilePicker();
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
        filePicker.setCacheLocation(PickerUtils.getSavedCacheLocation(getActivity()));
        return filePicker;
    }

    @Override
    public void onFilesChosen(List<ChosenFile> files) {
        for (ChosenFile file : files) {
            Log.d(TAG, "onFilesChosen: " + file);
        }

        MediaResultsAdapter adapter = new MediaResultsAdapter(files, getActivity());
        lvResults.setAdapter(adapter);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Picker.PICK_FILE && resultCode == Activity.RESULT_OK) {
            filePicker.submit(data);
        }
    }
}
