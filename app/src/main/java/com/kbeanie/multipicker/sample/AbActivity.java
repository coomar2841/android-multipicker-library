package com.kbeanie.multipicker.sample;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.RequestConfiguration;
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


    protected void setupAds() {
        adView = (AdView) findViewById(R.id.adView);
        AdRequest.Builder builder = new AdRequest.Builder();
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
