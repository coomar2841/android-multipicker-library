package com.kbeanie.multipicker.core.threads;

import android.content.Context;
import android.util.Log;

import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.exceptions.PickerException;

import java.util.List;

/**
 * Created by kbibek on 2/20/16.
 */
public final class ImageProcessorThread extends FileProcessorThread {
    private final static String TAG = ImageProcessorThread.class.getSimpleName();

    private boolean shouldGenerateThumbnails;
    private boolean shouldGenerateMetadata;

    private int maxImageWidth = -1;
    private int maxImageHeight = -1;

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
        postProcessImages();
        onDone();
    }

    private void onDone() {
        try {
            if (callback != null) {
                getActivityFromContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onImagesChosen((List<ChosenImage>) files);
                    }
                });
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void postProcessImages() {
        for (ChosenFile file : files) {
            ChosenImage image = (ChosenImage) file;
            try {
                postProcessImage(image);
                image.setSuccess(true);
            } catch (PickerException e) {
                e.printStackTrace();
                image.setSuccess(false);
            }
        }
    }

    private ChosenImage postProcessImage(ChosenImage image) throws PickerException {
        if (maxImageWidth != -1 && maxImageHeight != -1) {
            image = ensureMaxWidthAndHeight(maxImageWidth, maxImageHeight, image);
        }
        Log.d(TAG, "postProcessImage: " + image.getMimeType());
        if (shouldGenerateMetadata) {
            try {
                image = generateMetadata(image);
            } catch (Exception e) {
                Log.d(TAG, "postProcessImage: Error generating metadata");
                e.printStackTrace();
            }
        }
        if (shouldGenerateThumbnails) {
            image = generateThumbnails(image);
        }
        Log.d(TAG, "postProcessImage: " + image);
        return image;
    }

    private ChosenImage generateMetadata(ChosenImage image) {
        image.setWidth(Integer.parseInt(getWidthOfImage(image.getOriginalPath())));
        image.setHeight(Integer.parseInt(getHeightOfImage(image.getOriginalPath())));
        image.setOrientation(getOrientation(image.getOriginalPath()));
        return image;
    }

    private ChosenImage generateThumbnails(ChosenImage image) throws PickerException {
        String thumbnailBig = downScaleAndSaveImage(image.getOriginalPath(), THUMBNAIL_BIG);
        image.setThumbnailPath(thumbnailBig);
        String thumbnailSmall = downScaleAndSaveImage(image.getOriginalPath(), THUMBNAIL_SMALL);
        image.setThumbnailSmallPath(thumbnailSmall);
        return image;
    }

    public void setShouldGenerateMetadata(boolean shouldGenerateMetadata) {
        this.shouldGenerateMetadata = shouldGenerateMetadata;
    }

    public void setOutputImageDimensions(int maxWidth, int maxHeight) {
        this.maxImageWidth = maxWidth;
        this.maxImageHeight = maxHeight;
    }

}
