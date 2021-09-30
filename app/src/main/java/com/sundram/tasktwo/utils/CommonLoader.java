package com.sundram.tasktwo.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.sundram.tasktwo.R;

import javax.inject.Inject;

public class CommonLoader {

    Dialog dialog;
    Context activity;

    @Inject
    public CommonLoader() {
    }

    public void createDialog(Context activity) {
        try {
            this.activity = activity;
            dialog = new Dialog(activity);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.loader_view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog() {
        dialog.show();
    }

    public void hideDialog() {
        dialog.dismiss();
    }
}
