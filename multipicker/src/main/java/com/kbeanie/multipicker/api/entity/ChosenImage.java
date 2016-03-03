package com.kbeanie.multipicker.api.entity;

import android.media.ExifInterface;
import android.os.Parcel;

/**
 * Contains details about the image that was chosen
 */
public class ChosenImage extends ChosenFile {
    private int orientation;
    private String thumbnailPath;
    private String thumbnailSmallPath;
    private int width;
    private int height;

    public ChosenImage(){

    }

    protected ChosenImage(Parcel in) {
        super(in);
        this.orientation = in.readInt();
        this.thumbnailPath = in.readString();
        this.thumbnailSmallPath = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
    }


    public static final Creator<ChosenImage> CREATOR = new Creator<ChosenImage>() {
        @Override
        public ChosenImage createFromParcel(Parcel in) {
            return new ChosenImage(in);
        }

        @Override
        public ChosenImage[] newArray(int size) {
            return new ChosenImage[size];
        }
    };

    /**
     * Get orientation of the actual image
     *
     * @return
     */
    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     * Get the path to the thumbnail(big) of the image
     *
     * @return
     */
    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    /**
     * Get the path to the thumbnail(small) of the image
     *
     * @return
     */
    public String getThumbnailSmallPath() {
        return thumbnailSmallPath;
    }

    public void setThumbnailSmallPath(String thumbnailSmallPath) {
        this.thumbnailSmallPath = thumbnailSmallPath;
    }

    /**
     * Get the image width
     *
     * @return
     */
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Get the image height;
     *
     * @return
     */
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private final static String STRING_FORMAT = "Height: %s, Width: %s, Orientation: %s";

    @Override
    public String toString() {
        return super.toString() + " " + String.format(STRING_FORMAT, height, width, getOrientationName());
    }

    /**
     * Get Orientation user friendly label
     *
     * @return
     */
    public String getOrientationName() {
        String orientationName = "NORMAL";
        switch (orientation) {
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                orientationName = "FLIP_HORIZONTAL";
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                orientationName = "FLIP_VERTICAL";
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                orientationName = "ROTATE_90";
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                orientationName = "ROTATE_180";
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                orientationName = "ROTATE_270";
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                orientationName = "TRANSPOSE";
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                orientationName = "TRANSVERSE";
                break;
            case ExifInterface.ORIENTATION_UNDEFINED:
                orientationName = "UNDEFINED";
                break;
        }
        return orientationName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(orientation);
        dest.writeString(thumbnailPath);
        dest.writeString(thumbnailSmallPath);
        dest.writeInt(width);
        dest.writeInt(height);
    }
}
