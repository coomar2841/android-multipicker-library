package com.kbeanie.multipicker.sample;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kbeanie.multipicker.sample.prefs.AppPreferences;

/**
 * Created by kbibek on 2/18/16.
 */
public class AbActivity extends AppCompatActivity {

    private AdView adView;

    protected AppPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(this instanceof HomeActivity)) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        preferences = new AppPreferences(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setupAds();
    }

    public final static String NEXUS_S = "55958F02BF66EEC31424761A58B1733B";
    public final static String TEST_DEVICE_ID_2 = "79B7F70DBE55777CD06F8FE2EBEB92A1";
    public final static String TEST_GALAXY_NEXUS = "EC4FF024718202E6D9E05CAE6C7815E9";
    public final static String TEST_OPO = "BF997DF77ED76DCABEC05DC2B9BF44D3";
    public final static String TEMP_DEVICE = "3DA4EE1237EDE839B2003C9DD6675D97";

    private final static String[] TEST_DEVICES = {NEXUS_S, TEST_DEVICE_ID_2, TEST_GALAXY_NEXUS, TEST_OPO, TEMP_DEVICE};

    protected void setupAds() {
        adView = (AdView) findViewById(R.id.adView);

        AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

        for (String device : TEST_DEVICES) {
            builder.addTestDevice(device);
        }
        AdRequest request = builder.build();
        adView.loadAd(request);
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
