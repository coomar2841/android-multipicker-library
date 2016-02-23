package com.kbeanie.multipicker.threads;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.exceptions.PickerException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.List;

import static com.kbeanie.multipicker.utils.StreamHelper.close;
import static com.kbeanie.multipicker.utils.StreamHelper.flush;

/**
 * Created by kbibek on 2/20/16.
 */
public class ImageProcessorThread extends FileProcessorThread {
    private final static String TAG = ImageProcessorThread.class.getSimpleName();

    private boolean shouldGenerateThumbnails;
    private boolean shouldGenerateMetadata;

    private final static int THUMBNAIL_BIG = 1;

    private final static int THUMBNAIL_SMALL = 2;

    public ImageProcessorThread(Context context, List<ChosenImage> paths, int cacheLocation) {
        super(context, paths, cacheLocation);
    }

    public void setShouldGenerateThumbnails(boolean shouldGenerateThumbnails) {
        this.shouldGenerateThumbnails = shouldGenerateThumbnails;
    }

    @Override
    public void run() {
        super.run();
        try {
            postProcessImages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        onDone();
    }

    private void onDone() {
        for (ChosenFile file : files) {
            Log.d(TAG, "onDone: Images: " + file);
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
        image.setWidth(Integer.parseInt(getWidth(image.getOriginalPath())));
        image.setHeight(Integer.parseInt(getHeight(image.getOriginalPath())));
        image.setOrientation(getOrientation(image));
        return image;
    }

    private int getOrientation(ChosenImage image) {
        int orientation = ExifInterface.ORIENTATION_NORMAL;
        try {
            ExifInterface exif = new ExifInterface(image.getOriginalPath());

            orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orientation;
    }

    private ChosenImage generateThumbnails(ChosenImage image) throws PickerException {
        String thumbnailBig = compressAndSaveImage(image, THUMBNAIL_BIG);
        image.setThumbnailPath(thumbnailBig);
        String thumbnailSmall = compressAndSaveImage(image, THUMBNAIL_SMALL);
        image.setThumbnailSmallPath(thumbnailSmall);
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

    private String compressAndSaveImage(ChosenImage image, int scale) throws PickerException {

        FileOutputStream stream = null;
        BufferedInputStream bstream = null;
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options optionsForGettingDimensions = new BitmapFactory.Options();
            optionsForGettingDimensions.inJustDecodeBounds = true;
            BufferedInputStream boundsOnlyStream = new BufferedInputStream(new FileInputStream(image.getOriginalPath()));
            bitmap = BitmapFactory.decodeStream(boundsOnlyStream, null, optionsForGettingDimensions);
            if (bitmap != null) {
                bitmap.recycle();
            }
            if (boundsOnlyStream != null) {
                boundsOnlyStream.close();
            }
            int w, l;
            w = optionsForGettingDimensions.outWidth;
            l = optionsForGettingDimensions.outHeight;

            ExifInterface exif = new ExifInterface(image.getOriginalPath());

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            int rotate = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = -90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            int what = w > l ? w : l;

            BitmapFactory.Options options = new BitmapFactory.Options();
            if (what > 3000) {
                options.inSampleSize = scale * 6;
            } else if (what > 2000 && what <= 3000) {
                options.inSampleSize = scale * 5;
            } else if (what > 1500 && what <= 2000) {
                options.inSampleSize = scale * 4;
            } else if (what > 1000 && what <= 1500) {
                options.inSampleSize = scale * 3;
            } else if (what > 400 && what <= 1000) {
                options.inSampleSize = scale * 2;
            } else {
                options.inSampleSize = scale;
            }

            options.inJustDecodeBounds = false;
            // TODO: Sometime the decode File Returns null for some images
            // For such cases, thumbnails can't be created.
            // Thumbnails will link to the original file
            BufferedInputStream scaledInputStream = new BufferedInputStream(new FileInputStream(image.getOriginalPath()));
            bitmap = BitmapFactory.decodeStream(scaledInputStream, null, options);
//            verifyBitmap(fileImage, bitmap);
            scaledInputStream.close();
            if (bitmap == null) {
                File original = new File(image.getOriginalPath());
                File file = new File(
                        (original.getParent() + File.separator + original.getName()
                                .replace(".", "_fact_" + scale + ".")));
                stream = new FileOutputStream(file);
                if (rotate != 0) {
                    Matrix matrix = new Matrix();
                    matrix.setRotate(rotate);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), matrix, false);
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                return file.getAbsolutePath();
            }

        } catch (IOException e) {
//            throw new ChooserException(e);
        } catch (Exception e) {
        } finally {
            close(bstream);
            flush(stream);
            close(stream);
        }

        return null;
    }
}
