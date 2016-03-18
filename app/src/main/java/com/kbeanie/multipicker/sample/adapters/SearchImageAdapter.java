package com.kbeanie.multipicker.sample.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kbeanie.multipicker.sample.R;
import com.kbeanie.multipicker.search.api.RemoteImage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kbibek on 3/1/16.
 */
public class SearchImageAdapter extends RecyclerView.Adapter<SearchImageAdapter.ImageViewHolder> {
    private List<RemoteImage> images;
    private Context context;
    private Set<String> selectedItems;
    private ChoiceListener listener;
    private boolean allowMultiple;

    public SearchImageAdapter(Context context, List<RemoteImage> images) {
        this.images = images;
        this.context = context;
        this.selectedItems = new HashSet<>();
    }

    public void setAllowMultiple() {
        this.allowMultiple = true;
    }

    public void setChoiceListener(ChoiceListener listener) {
        this.listener = listener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_search_image, null);
        ImageViewHolder ivh = new ImageViewHolder(view);
        return ivh;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        final RemoteImage image = images.get(position);
        String url = image.getThumb() != null && !image.getThumb().isEmpty() ? image.getThumb() : image.getUrl();
        Glide.with(context)
                .load(Uri.parse(url))
                .listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.pBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.pBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.ivImage);
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleClickOnItem(image.getUrl());
            }
        });

        if (selectedItems.contains(image.getUrl())) {
            holder.ivOverlay.setVisibility(View.VISIBLE);
        } else {
            holder.ivOverlay.setVisibility(View.INVISIBLE);
        }
    }

    private void handleClickOnItem(String url) {
        if (selectedItems.contains(url)) {
            selectedItems.remove(url);
        } else {
            if (!allowMultiple) {
                selectedItems.clear();
            }
            selectedItems.add(url);
        }

        notifyDataSetChanged();

        if (listener != null) {
            listener.onItemCountChanged(selectedItems.size());
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public List<String> getSelectedImages() {
        List<String> selectedURLs = new ArrayList<>();
        for (String url : selectedItems) {
            selectedURLs.add(url);
        }
        return selectedURLs;
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void clearData() {
        images.clear();
        notifyDataSetChanged();
    }

    public void setImages(List<RemoteImage> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        ImageView ivOverlay;
        ProgressBar pBar;

        public ImageViewHolder(View itemView) {
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            ivOverlay = (ImageView) itemView.findViewById(R.id.ivOverlay);
            pBar = (ProgressBar) itemView.findViewById(R.id.pBar);
        }
    }

    public interface ChoiceListener {
        void onItemCountChanged(int count);
    }
}
