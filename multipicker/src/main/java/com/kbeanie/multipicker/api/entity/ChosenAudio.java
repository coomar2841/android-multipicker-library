package com.kbeanie.multipicker.api.entity;

import java.util.concurrent.TimeUnit;

/**
 * Created by kbibek on 2/20/16.
 */
public class ChosenAudio extends ChosenFile {
    private int duration;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return super.toString() + " Duration: " + duration + " secs";
    }


}
