package com.huwei.gleffectview.sample;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.gallery3d.anim.AlphaAnimation;
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
        glView.layout(0, 0, 400, 400);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ice_sea);
        glView.setBackgroundBitmap(bitmapDrawable.getBitmap());

        Log.i(TAG, "width:" + glView.getWidth() + " height:" + glView.getHeight());

        mGLRootView.setContentPane(panelGLView);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        glView.startAnimation(alphaAnimation);
    }
}
