package com.kbeanie.multipicker.api;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.core.PickerManager;
import com.kbeanie.multipicker.core.threads.AudioProcessorThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to pick an audio file.
 */
public final class AudioPicker extends PickerManager {
    private final static String TAG = AudioPicker.class.getSimpleName();
    private AudioPickerCallback callback;

    private String mimeType = "audio/*";

    /**
     * Constructor to choose an audio file from an {@link Activity}
     * @param activity
     */
    public AudioPicker(Activity activity) {
        super(activity, Picker.PICK_AUDIO);
    }

    /**
     * Constructor to choose an audio file from a {@link Fragment}
     * @param fragment
     */
    public AudioPicker(Fragment fragment) {
        super(fragment, Picker.PICK_AUDIO);
    }

    /**
     * Constructor to choose an audio file from a {@link android.app.Fragment}
     * @param appFragment
     */
    public AudioPicker(android.app.Fragment appFragment) {
        super(appFragment, Picker.PICK_AUDIO);
    }

    /**
     * Listener which gets callbacks when the audio file is ready to be used
     * @param callback
     */
    public void setAudioPickerCallback(AudioPickerCallback callback) {
        this.callback = callback;
    }

    /**
     * Set this to true if you want to choose Multiple audio files
     */
    public void allowMultiple() {
        this.allowMultiple = true;
    }

    /**
     * Set mimeType parameter if you want to choose a specific type of audio
     * @param mimeType
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Triggers audio selection
     */
    public void pickAudio() {
        try {
            pick();
        } catch (PickerException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError(e.getMessage());
            }
        }
    }

    @Override
    protected String pick() throws PickerException {
        if (callback == null) {
            throw new PickerException("AudioPickerCallback is null!!! Please set one");
        }
        String action = Intent.ACTION_GET_CONTENT;
        Intent intent = new Intent(action);
        intent.setType(mimeType);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        pickInternal(intent, pickerType);
        return null;
    }

    /**
     * Call this method from
     * {@link Activity#onActivityResult(int, int, Intent)}
     * OR
     * {@link Fragment#onActivityResult(int, int, Intent)}
     * OR
     * {@link android.app.Fragment#onActivityResult(int, int, Intent)}
     * @param data
     */
    @Override
    public void submit(Intent data) {
        handleAudioData(data);
    }

    private void handleAudioData(Intent intent) {
        List<String> uris = new ArrayList<>();
        if (intent != null) {
            if (intent.getDataString() != null) {
                String uri = intent.getDataString();
                Log.d(TAG, "handleAudioData: " + uri);
                uris.add(uri);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (intent.getClipData() != null) {
                    ClipData clipData = intent.getClipData();
                    Log.d(TAG, "handleAudioData: Multiple audios with ClipData");
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Log.d(TAG, "Item [" + i + "]: " + item.getUri().toString());
                        uris.add(item.getUri().toString());
                    }
                }
            }
            if (intent.hasExtra("uris")) {
                ArrayList<Uri> paths = intent.getParcelableArrayListExtra("uris");
                for (int i = 0; i < paths.size(); i++) {
                    uris.add(paths.get(i).toString());
                }
            }
            processFiles(uris);
        }
    }

    private void processFiles(List<String> uris) {
        AudioProcessorThread thread = new AudioProcessorThread(getContext(), getFileObjects(uris), cacheLocation);
        thread.setRequestId(requestId);
        thread.setAudioPickerCallback(callback);
        thread.start();
    }

    private void onError(final String errorMessage) {
        try {
            if (callback != null) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(errorMessage);
                    }
                });
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private List<ChosenFile> getFileObjects(List<String> uris) {
        List<ChosenFile> files = new ArrayList<>();
        for (String uri : uris) {
            ChosenAudio audio = new ChosenAudio();
            audio.setQueryUri(uri);
            audio.setDirectoryType(Environment.DIRECTORY_MUSIC);
            audio.setType("audio");
            files.add(audio);
        }
        return files;
    }
}