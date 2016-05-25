package com.kbeanie.multipicker.api;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.callbacks.MediaPickerCallback;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.kbeanie.multipicker.api.exceptions.PickerException;
import com.kbeanie.multipicker.core.PickerManager;
import com.kbeanie.multipicker.core.threads.FileProcessorThread;
import com.kbeanie.multipicker.core.threads.ImageProcessorThread;
import com.kbeanie.multipicker.core.threads.VideoProcessorThread;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This is not yet full proof. It has bugs, which doesn't work on all devices. Use this at your own risk.
 */
public class MediaPicker extends PickerManager implements FilePickerCallback, ImagePickerCallback, VideoPickerCallback {
    private final static String TAG = MediaPicker.class.getSimpleName();
    private MediaPickerCallback callback;

    private boolean generateThumbnails = true;
    private boolean generateMetadata = true;
    private boolean generatePreviewImages = true;

    /**
     * Constructor for choosing media from an {@link Activity}
     * @param activity
     */
    public MediaPicker(Activity activity) {
        super(activity, Picker.PICK_MEDIA);
    }

    /**
     * Constructor for choosing media from a {@link Fragment}
     * @param fragment
     */
    public MediaPicker(Fragment fragment) {
        super(fragment, Picker.PICK_MEDIA);
    }

    /**
     * Constructor for choosing media from a {@link android.app.Fragment}
     * @param appFragment
     */
    public MediaPicker(android.app.Fragment appFragment) {
        super(appFragment, Picker.PICK_MEDIA);
    }

    public void allowMultiple() {
        this.allowMultiple = true;
    }


    /**
     * Set this to true if you want thumbnails of the media files to be generated.
     * @param generateThumbnails
     */
    public void shouldGenerateThumbnails(boolean generateThumbnails) {
        this.generateThumbnails = generateThumbnails;
    }

    /**
     * Set this to true if you want the metadata of the chosen media to be processed
     * @param generateMetadata
     */
    public void shouldGenerateMetadata(boolean generateMetadata) {
        this.generateMetadata = generateMetadata;
    }

    /**
     * Set this to true if you want to generate a preview thumnail for video files
     * @param generatePreviewImages
     */
    public void shouldGeneratePreviewImages(boolean generatePreviewImages) {
        this.generatePreviewImages = generatePreviewImages;
    }

    @Override
    protected String pick() throws PickerException {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType("*/*");
            String[] mimeTypes = {"image/*", "video/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        } else {
            intent.setType("image/*, video/*");
        }
        if (extras != null) {
            intent.putExtras(extras);
        }
        // For reading from external storage (Content Providers)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        pickInternal(intent, Picker.PICK_MEDIA);
        return null;
    }

    /**
     * Triggers media selection
     */
    public void pickMedia() {
        try {
            pick();
        } catch (PickerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call this method from
     * {@link Activity#onActivityResult(int, int, Intent)}
     * OR
     * {@link Fragment#onActivityResult(int, int, Intent)}
     * OR
     * {@link android.app.Fragment#onActivityResult(int, int, Intent)}
     * @param intent
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void submit(Intent intent) {
        List<String> uris = new ArrayList<>();
        if (intent != null) {
            if (intent.getDataString() != null && isClipDataApi() && intent.getClipData() == null) {
                String uri = intent.getDataString();
                Log.d(TAG, "submit: Uri: " + uri);
                uris.add(uri);
            } else if (isClipDataApi()) {
                if (intent.getClipData() != null) {
                    ClipData clipData = intent.getClipData();
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
        }

        processMedia(uris);
    }

    private void processMedia(List<String> uris) {
        FileProcessorThread thread = new FileProcessorThread(getContext(), getFileObjects(uris), cacheLocation);
        thread.setFilePickerCallback(this);
        thread.setRequestId(requestId);
        thread.start();
    }

    /**
     * Listener which gets callbacks when your media is processed and ready to be used.
     * @param callback
     */
    public void setMediaPickerCallback(MediaPickerCallback callback) {
        this.callback = callback;
    }

    private List<ChosenFile> getFileObjects(List<String> uris) {
        List<ChosenFile> files = new ArrayList<>();
        for (String uri : uris) {
            ChosenFile file = new ChosenFile();
            file.setQueryUri(uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                file.setDirectoryType(Environment.DIRECTORY_DOCUMENTS);
            } else {
                file.setDirectoryType(Environment.DIRECTORY_DOWNLOADS);
            }
            file.setType("file");
            files.add(file);
        }
        return files;
    }

    private List<ChosenImage> imagesToProcess;
    private List<ChosenVideo> videosToProcess;

    @Override
    public void onFilesChosen(List<ChosenFile> files) {
        imagesToProcess = new ArrayList<>();
        videosToProcess = new ArrayList<>();
        for (ChosenFile file : files) {
            if (file.getMimeType().contains("image")) {
                ChosenImage image = new ChosenImage();
                image.setQueryUri(Uri.fromFile(new File(file.getOriginalPath())).toString());
                image.setType("image");
                image.setDirectoryType(Environment.DIRECTORY_PICTURES);
                image.setDisplayName(file.getDisplayName());
                image.setExtension(file.getExtension());
                imagesToProcess.add(image);
            } else if (file.getMimeType().contains("video")) {
                ChosenVideo video = new ChosenVideo();
                video.setQueryUri(Uri.fromFile(new File(file.getOriginalPath())).toString());
                video.setType("video");
                video.setDirectoryType(Environment.DIRECTORY_MOVIES);
                video.setDisplayName(file.getDisplayName());
                video.setExtension(file.getExtension());
                videosToProcess.add(video);
            }
        }

        if (imagesToProcess != null && imagesToProcess.size() > 0) {
            ImageProcessorThread imgThread = new ImageProcessorThread(getContext(), imagesToProcess, cacheLocation);
            imgThread.setImagePickerCallback(this);
            imgThread.setShouldGenerateMetadata(generateMetadata);
            imgThread.setShouldGenerateThumbnails(generateThumbnails);
            imgThread.setRequestId(requestId);
            imgThread.start();
        } else if (videosToProcess != null && videosToProcess.size() > 0) {
            VideoProcessorThread vidThread = new VideoProcessorThread(getContext(), videosToProcess, cacheLocation);
            vidThread.setRequestId(requestId);
            vidThread.setShouldGenerateMetadata(generateMetadata);
            vidThread.setShouldGeneratePreviewImages(generatePreviewImages);
            vidThread.setVideoPickerCallback(this);
            vidThread.start();
        }
    }

    @Override
    public void onError(String message) {

    }

    private List<ChosenImage> images;
    private List<ChosenVideo> videos;

    @Override
    public void onImagesChosen(List<ChosenImage> images) {
        this.images = images;
        if (videosToProcess != null && videosToProcess.size() > 0) {
            VideoProcessorThread vidThread = new VideoProcessorThread(getContext(), videosToProcess, cacheLocation);
            vidThread.setRequestId(requestId);
            vidThread.setShouldGenerateMetadata(generateMetadata);
            vidThread.setShouldGeneratePreviewImages(generatePreviewImages);
            vidThread.setVideoPickerCallback(this);
            vidThread.start();
        } else {
            if (callback != null) {
                callback.onMediaChosen(images, null);
            }
        }
    }

    @Override
    public void onVideosChosen(List<ChosenVideo> videos) {
        this.videos = videos;
        if (callback != null) {
            callback.onMediaChosen(images, videos);
        }
    }
}
