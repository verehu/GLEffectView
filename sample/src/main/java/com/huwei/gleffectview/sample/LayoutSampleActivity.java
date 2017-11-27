package com.huwei.gleffectview.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.gallery3d.ui.GLRootView;
import com.android.gallery3d.ui.GLView;
import com.huwei.gleffectview.util.LogUtil;

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

        GLView glView = new GLView();
        glView.setBackgroundColor(Color.BLUE);
        glView.layout(0, 0, 400, 400);

        Log.i(TAG, "width:" + glView.getWidth() + " height:" + glView.getHeight());

        mGLRootView.setContentPane(glView);
        mGLRootView.requestRender();
    }
}
