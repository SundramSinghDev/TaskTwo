package com.sundram.tasktwo.utils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.sundram.tasktwo.R;

public class GlideUtils {

    @BindingAdapter("imgViewSrc")
    public static void showImage(ImageView view,String uri) {
        Glide.with(view.getContext())
                .load(uri)
                .placeholder(view.getContext().getResources().getDrawable(R.drawable.loading))
                .error(view.getContext().getResources().getDrawable(R.drawable.placeholder))
                .into(view);
    }
}
