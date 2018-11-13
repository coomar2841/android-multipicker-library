package com.kbeanie.multipicker.sample;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;

/**
 * Created by kbibek on 3/3/16.
 */
public class ImagePreviewActivity extends AbActivity {
    private ImageView ivImageGlide;
    private String uri;
    private String mimeType;

    private ChosenImage image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        uri = getIntent().getExtras().getString("uri");
        mimeType = getIntent().getExtras().getString("mimetype");
        image = getIntent().getExtras().getParcelable("chosen");

        ivImageGlide = (ImageView) findViewById(R.id.ivImageGlide);

        ivImageGlide.postDelayed(new Runnable() {
            @Override
            public void run() {
                displayImage();
            }
        }, 500);
    }

    private void displayImage() {
        int width = ivImageGlide.getWidth();
        int height = ivImageGlide.getHeight();
        Log.d(getClass().getSimpleName(), "displayImage: " + width + " x " + height);


        Glide.with(this)
                .load(Uri.fromFile(new File(uri)))
//                .placeholder(R.drawable.ic_image)
                .into(ivImageGlide);
    }
}
