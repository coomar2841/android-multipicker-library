package com.kbeanie.multipicker.sample.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kbibek on 3/19/16.
 */
public class SearchImagesDecorator extends RecyclerView.ItemDecoration {
    private int space;

    public SearchImagesDecorator(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
        outRect.top = space;
        outRect.left = space;
        outRect.right = space;
    }
}
