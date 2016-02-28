package com.kbeanie.multipicker.api.entity;

import java.util.concurrent.TimeUnit;

/**
 * Created by kbibek on 2/20/16.
 */
public class ChosenVideo extends ChosenFile {
    private int width;
    private int height;
    private long duration;
    private String previewImage;
    private String previewThumbnail;
    private String previewThumbnailSmall;
    private int orientation;

    /**
     * Get the width of the processed video
     * @return
     */
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Get the height of the processed video
     * @return
     */
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get the duration of the video in milliseconds
     * @return
     */
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Get the preview image file path
     * @return
     */
    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    /**
     * Get the preview image's thumbnail file path
     * @return
     */
    public String getPreviewThumbnail() {
        return previewThumbnail;
    }

    public void setPreviewThumbnail(String previewThumbnail) {
        this.previewThumbnail = previewThumbnail;
    }

    /**
     * Get the preview image's small thumbnail file path
     * @return
     */
    public String getPreviewThumbnailSmall() {
        return previewThumbnailSmall;
    }

    public void setPreviewThumbnailSmall(String previewThumbnailSmall) {
        this.previewThumbnailSmall = previewThumbnailSmall;
    }

    /**
     * Get the orientation of the video
     * @return
     */
    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /**
     * Get pretty format orientation of the video
     * @return
     */
    public String getOrientationName() {
        return orientation + " Deg";
    }
}
