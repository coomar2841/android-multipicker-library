package com.kbeanie.multipicker.api.entity;

/**
 * Created by kbibek on 2/20/16.
 */
public class ChosenVideo extends ChosenFile {
    private int width;
    private int height;
    private int duration;
    private String previewImage;
    private String previewThumbnail;
    private String previewThumbnailSmall;
    private int orientation;

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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    public String getPreviewThumbnail() {
        return previewThumbnail;
    }

    public void setPreviewThumbnail(String previewThumbnail) {
        this.previewThumbnail = previewThumbnail;
    }

    public String getPreviewThumbnailSmall() {
        return previewThumbnailSmall;
    }

    public void setPreviewThumbnailSmall(String previewThumbnailSmall) {
        this.previewThumbnailSmall = previewThumbnailSmall;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getOrientationName() {
        return orientation + " Deg";
    }
}
