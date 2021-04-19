package com.kbeanie.multipicker.sample.utils;

import android.content.Context;

import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.sample.prefs.AppPreferences;

/**
 * Created by kbibek on 2/26/16.
 */
public class PickerUtils {
    public static int getSavedCacheLocation(Context context) {
        AppPreferences preferences = new AppPreferences(context);
        switch (preferences.getCacheLocation()) {
            case 0:
                return CacheLocation.EXTERNAL_STORAGE_APP_DIR;
            case 2:
                return CacheLocation.EXTERNAL_CACHE_DIR;
            case 3:
                return CacheLocation.INTERNAL_APP_DIR;
        }
        return CacheLocation.EXTERNAL_STORAGE_APP_DIR;
    }
}
