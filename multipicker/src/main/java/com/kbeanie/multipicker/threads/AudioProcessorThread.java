package com.kbeanie.multipicker.threads;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenFile;

import java.io.File;
import java.util.List;

/**
 * Created by kbibek on 2/25/16.
 */
public class AudioProcessorThread extends FileProcessorThread {
    private AudioPickerCallback callback;

    public AudioProcessorThread(Context context, List<? extends ChosenFile> files, int cacheLocation) {
        super(context, files, cacheLocation);
    }

    @Override
    public void run() {
        super.run();
        postProcessAudios();
        onDone();
    }

    private void postProcessAudios() {
        for (ChosenFile file : files) {
            ChosenAudio audio = (ChosenAudio) file;
            postProcessAudio(audio);
        }
    }

    private void postProcessAudio(ChosenAudio audio) {
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        try {
            metadataRetriever.setDataSource(audio.getOriginalPath());
            String duration = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (duration != null) {
                audio.setDuration(Integer.parseInt(duration));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            metadataRetriever.release();
        }
    }

    private void onDone() {
        if (callback != null) {
            getActivityFromContext().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.onAudiosChosen((List<ChosenAudio>) files);
                }
            });
        }
    }

    public void setAudioPickerCallback(AudioPickerCallback callback) {
        this.callback = callback;
    }
}