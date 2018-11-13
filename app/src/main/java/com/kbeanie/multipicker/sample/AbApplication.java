package com.kbeanie.multipicker.sample;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.core.PickerManager;
import com.kbeanie.multipicker.sample.utils.PickerUtils;

import io.fabric.sdk.android.Fabric;

/**
 * Created by kbibek on 2/18/16.
 */
public class AbApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        MobileAds.initialize(this);
        PickerManager.debugglable = true;
    }
}
