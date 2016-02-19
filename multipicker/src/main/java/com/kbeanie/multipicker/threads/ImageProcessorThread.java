package com.kbeanie.multipicker.threads;

import android.content.Context;

import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.util.List;

/**
 * Created by kbibek on 2/20/16.
 */
public class ImageProcessorThread extends FileProcessorThread {
    public ImageProcessorThread(Context context, List<ChosenImage> paths, int cacheLocation) {
        super(context, paths, cacheLocation);
    }
}
