package com.kbeanie.multipicker.api.entity;

import android.os.Parcel;

/**
 * Created by kbibek on 2/20/16.
 */
public class ChosenAudio extends ChosenFile {
    private long duration;

    public ChosenAudio(){

    }

    protected ChosenAudio(Parcel in) {
        super(in);
        this.duration = in.readLong();
    }

    public static final Creator<ChosenAudio> CREATOR = new Creator<ChosenAudio>() {
        @Override
        public ChosenAudio createFromParcel(Parcel in) {
            return new ChosenAudio(in);
        }

        @Override
        public ChosenAudio[] newArray(int size) {
            return new ChosenAudio[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(duration);
    }

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
