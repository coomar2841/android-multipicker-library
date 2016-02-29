package com.kbeanie.multipicker.api.callbacks;

import com.kbeanie.multipicker.api.entity.ChosenVideo;

import java.util.List;

/**
 * Created by kbibek on 2/23/16.
 */
public interface VideoPickerCallback extends PickerCallback {
    void onVideosChosen(List<ChosenVideo> videos);
}
