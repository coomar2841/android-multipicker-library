package com.kbeanie.multipicker.threads;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.util.Log;

import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by kbibek on 2/20/16.
 */
public class ImageProcessorThread extends FileProcessorThread {
    private final static String TAG = ImageProcessorThread.class.getSimpleName();

    private boolean shouldGenerateThumbnails;
    private boolean shouldGenerateMetadata;

    public ImageProcessorThread(Context context, List<ChosenImage> paths, int cacheLocation) {
        super(context, paths, cacheLocation);
    }

    public void setShouldGenerateThumbnails(boolean shouldGenerateThumbnails) {
        this.shouldGenerateThumbnails = shouldGenerateThumbnails;
    }

    @Override
    public void run() {
        super.run();
        postProcessImages();
        onDone();
    }

    private void onDone() {
        for (ChosenFile file : files) {
            Log.d(TAG, "onDone: Images: " + file);
        }
    }

    private void postProcessImages() {
        Log.d(TAG, "postProcessImages: ");
        for (ChosenFile file : files) {
            ChosenImage image = (ChosenImage) file;
            postProcessImage(image);
        }
    }

    private ChosenImage postProcessImage(ChosenImage image) {
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
        image.setWidth(Integer.parseInt(getWidth(image.getOriginalPath())));
        image.setHeight(Integer.parseInt(getHeight(image.getOriginalPath())));
        return image;
    }

    private ChosenImage generateThumbnails(ChosenImage image) {
        return image;
    }

    public void setShouldGenerateMetadata(boolean shouldGenerateMetadata) {
        this.shouldGenerateMetadata = shouldGenerateMetadata;
    }

    protected String getWidth(String path) {
        String width = "";
        try {
            ExifInterface exif = new ExifInterface(path);
            width = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            if (width.equals("0")) {
                width = Integer.toString(getBitmap(path).get().getWidth());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return width;
    }

    protected String getHeight(String path) {
        String height = "";
        try {
            ExifInterface exif = new ExifInterface(path);
            height = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            if (height.equals("0")) {
                height = Integer.toString(getBitmap(path).get().getHeight());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return height;
    }

    protected SoftReference<Bitmap> getBitmap(String path) {
        SoftReference<Bitmap> bitmap = null;
        try {
            bitmap = new SoftReference<>(BitmapFactory.decodeStream(new FileInputStream(
                    new File(path))));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
