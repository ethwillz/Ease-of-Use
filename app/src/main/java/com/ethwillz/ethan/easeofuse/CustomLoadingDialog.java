package com.ethwillz.ethan.easeofuse;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

public class CustomLoadingDialog extends ProgressDialog {
    private AnimationDrawable animation;

    public CustomLoadingDialog(Context context) {
        super(context, R.style.ProgressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);

        ImageView load = (ImageView) findViewById(R.id.loading);
        load.setBackgroundResource(R.drawable.load_animation);
        animation = (AnimationDrawable) load.getBackground();
    }

    @Override
    public void show() {
        super.show();
        animation.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        animation.stop();
    }
}
