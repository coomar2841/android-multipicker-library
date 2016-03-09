package com.kbeanie.multipicker.sample;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kbeanie.multipicker.sample.adapters.SearchImageAdapter;
import com.kbeanie.multipicker.search.BingImageSearchActivity;
import com.kbeanie.multipicker.search.api.RemoteImage;

import java.util.List;

/**
 * Created by kbibek on 3/1/16.
 */
public class ImagesSearchActivity extends BingImageSearchActivity implements SearchImageAdapter.ChoiceListener {
    private final static String TAG = ImagesSearchActivity.class.getSimpleName();
    private EditText etSearch;

    private RecyclerView rvImages;

    private boolean allowMultiple;

    private SearchImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_search);
        setupAds();

        try {
            allowMultiple = getIntent().getExtras().getBoolean(Intent.EXTRA_ALLOW_MULTIPLE);
        } catch (Exception e) {
            // Do nothing
        }

        etSearch = (EditText) findViewById(R.id.etSearch);

        rvImages = (RecyclerView) findViewById(R.id.rvImages);
        rvImages.setHasFixedSize(true);

        RecyclerView.LayoutManager lm = new GridLayoutManager(this, 3);
        rvImages.setLayoutManager(lm);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = etSearch.getText().toString();
                    Log.d(TAG, "onEditorAction: Query: " + query);
                    search(query);
                }
                return false;
            }
        });
    }

    @Override
    public void showResults(List<RemoteImage> images) {
        if (images == null) {
            Toast.makeText(this, "Error searching images", Toast.LENGTH_LONG).show();
            return;
        }
        if (adapter == null) {
            adapter = new SearchImageAdapter(this, images);
            adapter.setChoiceListener(this);
            if (allowMultiple) {
                adapter.setAllowMultiple();
            }
            rvImages.setAdapter(adapter);
        } else {
            adapter.setImages(images);
        }
    }

    /**
     * For showing ads
     */
    private final static String GALAXY_TAB = "6B7B033AC9940497E369C02B714E9483";
    public final static String NEXUS_S = "55958F02BF66EEC31424761A58B1733B";
    public final static String TEST_DEVICE_ID_2 = "79B7F70DBE55777CD06F8FE2EBEB92A1";
    public final static String TEST_GALAXY_NEXUS = "E83F73F907EE7CBDDE5F97BD3A901D4A";
    public final static String TEST_OPO = "BF997DF77ED76DCABEC05DC2B9BF44D3";
    public final static String TEMP_DEVICE = "3DA4EE1237EDE839B2003C9DD6675D97";

    private final static String[] TEST_DEVICES = {GALAXY_TAB, NEXUS_S, TEST_DEVICE_ID_2, TEST_GALAXY_NEXUS, TEST_OPO, TEMP_DEVICE};

    protected void setupAds() {
        AdView adView = (AdView) findViewById(R.id.adView);

        AdRequest.Builder builder = new AdRequest.Builder();
        builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

        for (String device : TEST_DEVICES) {
            builder.addTestDevice(device);
        }
        AdRequest request = builder.build();
        adView.loadAd(request);
    }

    private ActionMode actionMode;

    private boolean isActionModeActive;

    @Override
    public void onItemCountChanged(int count) {
        if (count == 0) {
            if (actionMode != null) {
                actionMode.finish();
            }
        } else {
            if (!isActionModeActive) {
                isActionModeActive = true;
                actionMode = startSupportActionMode(new SelectionActionModeOld());
            }
        }
    }

    private void selectionDone() {
        List<String> images = adapter.getSelectedImages();
        ClipData clipData = null;
        for (String image : images) {
            ClipData.Item item = new ClipData.Item(Uri.parse(image));
            if (clipData == null) {
                String[] mimeTypes = {"image/*"};
                clipData = new ClipData("AMP - Image", mimeTypes, item);
            } else {
                clipData.addItem(item);
            }
            Log.d(TAG, "selectionDone: URL: " + image);
        }

        Intent intent = new Intent();
        intent.setClipData(clipData);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class SelectionActionModeOld implements android.support.v7.view.ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_image_search, menu);
            mode.setTitle("Choose an action");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.menu_done) {
                selectionDone();
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {
            adapter.clearSelections();
            isActionModeActive = false;
        }
    }
}
