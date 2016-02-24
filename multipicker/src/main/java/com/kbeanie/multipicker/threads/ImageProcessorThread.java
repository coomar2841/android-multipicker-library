package com.kbeanie.multipicker.threads;

import android.content.Context;
import android.media.ExifInterface;
import android.util.Log;

import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.exceptions.PickerException;

import java.util.List;

/**
 * Created by kbibek on 2/20/16.
 */
public class ImageProcessorThread extends FileProcessorThread {
    private final static String TAG = ImageProcessorThread.class.getSimpleName();

    private boolean shouldGenerateThumbnails;
    private boolean shouldGenerateMetadata;

    private ImagePickerCallback callback;

    public ImageProcessorThread(Context context, List<ChosenImage> paths, int cacheLocation) {
        super(context, paths, cacheLocation);
    }

    public void setShouldGenerateThumbnails(boolean shouldGenerateThumbnails) {
        this.shouldGenerateThumbnails = shouldGenerateThumbnails;
    }

    public void setImagePickerCallback(ImagePickerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        super.run();
        try {
            postProcessImages();
            onDone();
        } catch (Exception e) {
            e.printStackTrace();
            onError(e.getMessage());
        }
    }

    private void onError(final String message) {
        if (callback != null) {
            getActivityFromContext().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.onError(message);
                }
            });
        }
    }

    private void onDone() {
        if (callback != null) {
            getActivityFromContext().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.onImagesChosen((List<ChosenImage>) files);
                }
            });
        }
    }

    private void postProcessImages() throws PickerException {
        Log.d(TAG, "postProcessImages: ");
        for (ChosenFile file : files) {
            ChosenImage image = (ChosenImage) file;
            postProcessImage(image);
        }
    }

    private ChosenImage postProcessImage(ChosenImage image) throws PickerException {
        Log.d(TAG, "postProcessImage: " + image.getMimeType());
        if (shouldGenerateMetadata) {
            image = generateMetadata(image);
        }
        if (shouldGenerateThumbnails) {
            image = generateThumbnails(image);
        }
        return image;
    }

    private ChosenImage generateMetadata(ChosenImage image) {
        image.setWidth(Integer.parseInt(getWidthOfImage(image.getOriginalPath())));
        image.setHeight(Integer.parseInt(getHeightOfImage(image.getOriginalPath())));
        image.setOrientation(getOrientation(image.getOriginalPath()));
        return image;
    }

    private ChosenImage generateThumbnails(ChosenImage image) throws PickerException {
        String thumbnailBig = compressAndSaveImage(image.getOriginalPath(), THUMBNAIL_BIG);
        image.setThumbnailPath(thumbnailBig);
        String thumbnailSmall = compressAndSaveImage(image.getOriginalPath(), THUMBNAIL_SMALL);
        image.setThumbnailSmallPath(thumbnailSmall);
        return image;
    }

    public void setShouldGenerateMetadata(boolean shouldGenerateMetadata) {
        this.shouldGenerateMetadata = shouldGenerateMetadata;
    }

}
