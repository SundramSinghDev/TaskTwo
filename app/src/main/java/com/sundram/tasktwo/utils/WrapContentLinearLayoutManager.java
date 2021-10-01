package com.sundram.tasktwo.utils;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WrapContentLinearLayoutManager extends LinearLayoutManager {

    public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    //... constructor
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            Log.e("TAG", "BUG IN LINEAR LAYOUT MANAGER OF RECYCLERVIEW");
        }
    }
//    Skipped 172 frames!  The application may be doing too much work on its main thread.
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }



}

