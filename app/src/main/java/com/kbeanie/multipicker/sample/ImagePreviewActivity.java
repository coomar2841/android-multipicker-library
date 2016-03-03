package com.kbeanie.multipicker.sample;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

/**
 * Created by kbibek on 3/3/16.
 */
public class ImagePreviewActivity extends AbActivity {

    private SimpleDraweeView ivImage;
    private String uri;
    private String mimeType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        uri = getIntent().getExtras().getString("uri");
        mimeType = getIntent().getExtras().getString("mimetype");

        ivImage = (SimpleDraweeView) findViewById(R.id.ivImage);

        ivImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                displayImage();
            }
        }, 1000);
    }

    private void displayImage() {
        int width = ivImage.getWidth();
        int height = ivImage.getHeight();
        Log.d(getClass().getSimpleName(), "displayImage: " + width + " x " + height);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(new File(uri)))
                .setResizeOptions(new ResizeOptions(ivImage.getWidth(), ivImage.getHeight()))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(ivImage.getController())
                .setImageRequest(request)
                .setAutoPlayAnimations(mimeType.toLowerCase().contains("gif"))
                .build();
        ivImage.setController(controller);
    }
}
