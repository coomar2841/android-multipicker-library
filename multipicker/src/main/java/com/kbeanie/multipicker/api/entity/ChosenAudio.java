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

    public String getUserReadableDuration() {
        String hms = String.format("%02dh %02dm %02ds", TimeUnit.MILLISECONDS.toHours(duration),
                TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        return hms;
    }
}
