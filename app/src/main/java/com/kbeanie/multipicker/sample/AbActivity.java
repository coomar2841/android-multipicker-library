package com.kbeanie.multipicker.sample;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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

    public final static String GALAXY_TAB = "6B7B033AC9940497E369C02B714E9483";
    public final static String NEXUS_S = "55958F02BF66EEC31424761A58B1733B";
    public final static String TEST_DEVICE_ID_2 = "79B7F70DBE55777CD06F8FE2EBEB92A1";
    public final static String TEST_GALAXY_NEXUS = "E83F73F907EE7CBDDE5F97BD3A901D4A";
    public final static String TEST_OPO = "BB5687CFDF9B599D839B1104E0A27DFC";
    public final static String XIAOMI = "EEAEE535D859D6F87E56B88B7A65D984";
    public final static String TEMP_DEVICE = "6AD51385081B9D970E623E078AADEA4A";
    public final static String ONEPLUS_6 = "24F27F547DF8E70EE386AFE4D978DA68";

    private final static String[] TEST_DEVICES = {GALAXY_TAB, NEXUS_S, TEST_DEVICE_ID_2, TEST_GALAXY_NEXUS, TEST_OPO, TEMP_DEVICE, XIAOMI, ONEPLUS_6};

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
