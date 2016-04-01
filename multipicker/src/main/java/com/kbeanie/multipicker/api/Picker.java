package com.kbeanie.multipicker.api;

/**
 * Created by kbibek on 2/18/16.
 */
public interface Picker {
    /**
     * Pick an image from the user's device
     */
    int PICK_IMAGE_DEVICE = 3111;
    /**
     * Take a picture using the user's camera
     */
    int PICK_IMAGE_CAMERA = 4222;
    /**
     * Pick a video from the user's device
     */
    int PICK_VIDEO_DEVICE = 5333;
    /**
     * Take a video using the user's camera
     */
    int PICK_VIDEO_CAMERA = 6444;
    /**
     * Pick a file from the user's device
     */
    int PICK_FILE = 7555;
    /**
     * Pick a contact from the user's device
     */
    int PICK_CONTACT = 8666;
    /**
     * Pick an audio file
     */
    int PICK_AUDIO = 9777;

    /**
     * Pick an image or video
     */
    int PICK_MEDIA = 10888;
}
