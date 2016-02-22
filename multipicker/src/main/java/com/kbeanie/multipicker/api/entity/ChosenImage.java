package com.kbeanie.multipicker.api.entity;

/**
 * Created by kbibek on 2/20/16.
 */
public class ChosenImage extends ChosenFile {
    private int orientation;
    private String thumbnailPath;
    private String thumbnailSmallPath;
    private int width;
    private int height;

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getThumbnailSmallPath() {
        return thumbnailSmallPath;
    }

    public void setThumbnailSmallPath(String thumbnailSmallPath) {
        this.thumbnailSmallPath = thumbnailSmallPath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private final static String STRING_FORMAT = "Height: %s, Width: %s";

    @Override
    public String toString() {
        return super.toString() + " " + String.format(STRING_FORMAT, height, width);
    }
}
