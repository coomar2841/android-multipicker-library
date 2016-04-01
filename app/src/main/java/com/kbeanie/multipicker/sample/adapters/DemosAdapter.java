package com.kbeanie.multipicker.sample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kbeanie.multipicker.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kbibek on 2/19/16.
 */
public class DemosAdapter extends BaseAdapter {
    private Context context;
    private List<Demo> demoTypes;

    public final static int PICKER_IMAGE = 1;
    public final static int PICKER_VIDEO = 2;
    public final static int PICKER_FILE = 3;
    public final static int PICKER_AUDIO = 4;
    public final static int PICKER_CONTACT = 5;
    public static final int PICKER_MEDIA = 6;

    public final static int IMAGE_PICKER_ACTIVITY = 1;
    public final static int IMAGE_PICKER_FRAGMENT = 2;
    public final static int IMAGE_PICKER_SUPPORT_FRAGMENT = 3;
    public final static int VIDEO_PICKER_ACTIVITY = 4;
    public final static int VIDEO_PICKER_FRAGMENT = 5;
    public final static int VIDEO_PICKER_SUPPORT_FRAGMENT = 6;
    public final static int FILE_PICKER_ACTIVITY = 7;
    public final static int FILE_PICKER_FRAGMENT = 8;
    public final static int FILE_PICKER_SUPPORT_FRAGMENT = 9;
    public final static int CONTACT_PICKER_ACTIVITY = 10;

    public final static int MEDIA_PICKER_ACTIVITY = 11;

    public final static int AUDIO_PICKER_ACTIVITY = 13;
    public final static int AUDIO_PICKER_FRAGMENT = 14;
    public final static int AUDIO_PICKER_SUPPORT_FRAGMENT = 15;

    public DemosAdapter(Context context) {
        this.context = context;
        this.demoTypes = new ArrayList<>();

        setupDemoTypes();
    }

    private void setupDemoTypes() {
        demoTypes.add(new Demo("Image Picker", PICKER_IMAGE));
        demoTypes.add(new Demo("Video Picker", PICKER_VIDEO));
        demoTypes.add(new Demo("File Picker", PICKER_FILE));
        demoTypes.add(new Demo("Audio Picker", PICKER_AUDIO));
        demoTypes.add(new Demo("Contact Picker", PICKER_CONTACT));
        demoTypes.add(new Demo("Pick Media", PICKER_MEDIA));

//        demoTypes.add(new Demo("Image Picker - Activity", IMAGE_PICKER_ACTIVITY));
//        demoTypes.add(new Demo("Image Picker - Fragment", IMAGE_PICKER_FRAGMENT));
//        demoTypes.add(new Demo("Image Picker - Support Fragment", IMAGE_PICKER_SUPPORT_FRAGMENT));
//        demoTypes.add(new Demo("Video Picker - Activity", VIDEO_PICKER_ACTIVITY));
//        demoTypes.add(new Demo("Video Picker - Fragment", VIDEO_PICKER_FRAGMENT));
//        demoTypes.add(new Demo("Video Picker - Support Fragment", VIDEO_PICKER_SUPPORT_FRAGMENT));
//        demoTypes.add(new Demo("File Picker - Activity", FILE_PICKER_ACTIVITY));
//        demoTypes.add(new Demo("File Picker - Fragment", FILE_PICKER_FRAGMENT));
//        demoTypes.add(new Demo("File Picker - Support Fragment", FILE_PICKER_SUPPORT_FRAGMENT));
//        demoTypes.add(new Demo("Contact Picker - Activity", CONTACT_PICKER_ACTIVITY));
//        demoTypes.add(new Demo("Contact Picker - Fragment", CONTACT_PICKER_FRAGMENT));
//        demoTypes.add(new Demo("Contact Picker - Support Fragment", CONTACT_PICKER_SUPPORT_FRAGMENT));
    }

    @Override
    public int getCount() {
        return demoTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return demoTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return demoTypes.get(position).demoTypeId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Demo demo = this.demoTypes.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_demos, null);
        }

        TextView tvIndex = (TextView) convertView.findViewById(R.id.tvIndex);
        tvIndex.setText("" + demo.demoTypeId);
        TextView tvDemoType = (TextView) convertView.findViewById(R.id.tvDemoType);
        tvDemoType.setText(demo.demoType);
        return convertView;
    }

    class Demo {
        String demoType;
        int demoTypeId;

        public Demo(String demoType, int demoTypeId) {
            this.demoType = demoType;
            this.demoTypeId = demoTypeId;
        }
    }
}
