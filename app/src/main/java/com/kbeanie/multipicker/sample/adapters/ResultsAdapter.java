package com.kbeanie.multipicker.sample.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.sample.R;

import java.io.File;
import java.util.List;

/**
 * Created by kbibek on 2/24/16.
 */
public class ResultsAdapter extends BaseAdapter {
    private final static String TAG = ResultsAdapter.class.getSimpleName();

    private final static int TYPE_IMAGE = 0;
    private final static int TYPE_VIDEO = 1;
    private final static int TYPE_FILE = 2;
    private final static int TYPE_CONTACT = 3;

    private final static String FORMAT_IMAGE_DIMENSIONS = "%sw x %sh";
    private final static String FORMAT_ORIENTATION = "Ortn: %s";

    private final Context context;
    private List<? extends ChosenFile> files;

    public ResultsAdapter(List<? extends ChosenFile> files, Context context) {
        this.files = files;
        this.context = context;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView: " + files.size());
        ChosenFile file = (ChosenFile) getItem(position);
        int itemViewType = getItemViewType(position);
        if (convertView == null) {
            if (itemViewType == TYPE_IMAGE) {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_images, null);
            }
        }

        switch (itemViewType) {
            case TYPE_IMAGE:
                showImage(file, convertView);
                break;
        }
        return convertView;
    }

    private void showImage(ChosenFile file, View view) {
        SimpleDraweeView ivImage = (SimpleDraweeView) view.findViewById(R.id.ivImage);
        ChosenImage image = (ChosenImage) file;
        ivImage.setImageURI(Uri.fromFile(new File(image.getThumbnailSmallPath())));

        TextView tvDimension = (TextView) view.findViewById(R.id.tvDimension);
        tvDimension.setText(String.format(FORMAT_IMAGE_DIMENSIONS, image.getWidth(), image.getHeight()));

        TextView tvMimeType = (TextView) view.findViewById(R.id.tvMimeType);
        tvMimeType.setText(file.getFileExtensionFromMimeTypeWithoutDot());

        TextView tvSize = (TextView) view.findViewById(R.id.tvSize);
        tvSize.setText(file.getHumanReadableSize(false));

        TextView tvOrientation = (TextView) view.findViewById(R.id.tvOrientation);
        tvOrientation.setText(String.format(FORMAT_ORIENTATION, image.getOrientationName()));
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        String type = ((ChosenFile) getItem(position)).getType();
        switch (type) {
            case "image":
                return TYPE_IMAGE;
        }
        return TYPE_FILE;
    }
}
