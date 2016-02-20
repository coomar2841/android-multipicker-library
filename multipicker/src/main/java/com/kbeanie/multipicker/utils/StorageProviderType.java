package com.kbeanie.multipicker.utils;

/**
 * Created by kbibek on 2/21/16.
 */
public class StorageProviderType {
    private String[] OLD_PROVIDERS = {
            "content://com.google.android.gallery3d",
            "content://com.microsoft.skydrive.content"
    };
    private String[] NEW_PROVIDERS = {
            "content://com.google.android.apps.photos.content",
            "content://com.android.providers.media.documents",
            "content://com.google.android.apps.docs.storage",
            "content://com.android.externalstorage.documents",
            "content://com.android.internalstorage.documents",
            "content://"
    };

    private static int getStorageProviderType(String uri) {
        return 1;
    }
}
