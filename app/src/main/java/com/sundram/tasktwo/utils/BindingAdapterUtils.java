package com.sundram.tasktwo.utils;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public class BindingAdapterUtils {

    @BindingAdapter("android:text")
    public static void setText(TextView view, CharSequence text) {
        // Some checks removed for clarity

        view.setText(text);
    }
}
