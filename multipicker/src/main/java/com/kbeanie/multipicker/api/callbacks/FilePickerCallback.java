package com.kbeanie.multipicker.api.callbacks;

import com.kbeanie.multipicker.api.entity.ChosenFile;

import java.util.List;

/**
 * Created by kbibek on 2/23/16.
 */
public interface FilePickerCallback extends PickerCallback {
    void onFilesChosen(List<ChosenFile> files);
}
