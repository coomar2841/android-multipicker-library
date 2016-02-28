package com.kbeanie.multipicker.api.entity;

import java.util.concurrent.TimeUnit;

/**
 * Created by kbibek on 2/20/16.
 */
public class ChosenAudio extends ChosenFile {
    private long duration;

    /**
     * Duration in milliseconds
     *
     * @return
     */
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
