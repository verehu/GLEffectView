package com.huwei.gleffectview.sample;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.gallery3d.anim.AlphaAnimation;
import com.android.gallery3d.anim.Animation;
import com.android.gallery3d.anim.TranslateAnimation;
import com.android.gallery3d.ui.GLRootView;
import com.android.gallery3d.ui.GLView;

/**
 * Created by huwei on 17-11-27.
 */

public class LayoutSampleActivity extends AppCompatActivity {
    private static final String TAG = "LayoutSampleActivity";
    private GLRootView mGLRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_layoutsample);
        mGLRootView = findViewById(R.id.gl_root_view);

        GLView panelGLView = new GLView();
        GLView glView = new GLView();
        panelGLView.addComponent(glView);
        panelGLView.setBackgroundColor(Color.RED);
        glView.layout(0, 0, 400, 400);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ice_sea);
        glView.setBackgroundBitmap(bitmapDrawable.getBitmap());

        Log.i(TAG, "width:" + glView.getWidth() + " height:" + glView.getHeight());

        mGLRootView.setContentPane(panelGLView);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        glView.startAnimation(alphaAnimation);

        glView = new GLView();
        glView.setBackgroundColor(Color.BLUE);
        panelGLView.addComponent(glView);
        glView.layout(500, 500, 500 + 200, 500 + 200);

        TranslateAnimation translateAnimation = new TranslateAnimation(500, 800, 500, 800);
        translateAnimation.setDuration(1000);
        translateAnimation.setRepeatCount(Animation.INFINITE);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        glView.startAnimation(translateAnimation);
    }
}
