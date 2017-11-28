/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.gallery3d.anim;

import com.android.gallery3d.glrenderer.GLCanvas;

public abstract class CanvasAnimation extends Animation {

    protected float mWidth;
    protected float mHeight;
    protected float mParentWidth;
    protected float mParentHeight;

    /**
     * This value must be set to true by {@link #initialize(int, int, int, int)}. It
     * indicates the animation was successfully initialized and can be played.
     */
    boolean mInitialized = false;

    /**
     * Whether or not the animation has been initialized.
     *
     * @return Has this animation been initialized.
     * @see #initialize(int, int, int, int)
     */
    public boolean isInitialized() {
        return mInitialized;
    }

    @Override
    public void reset() {
        super.reset();
        mInitialized = false;
    }

    /**
     * Initialize this animation with the dimensions of the object being
     * animated as well as the objects parents. (This is to support animation
     * sizes being specified relative to these dimensions.)
     * <p>
     * <p>Objects that interpret Animations should call this method when
     * the sizes of the object being animated and its parent are known, and
     * before calling {@link #calculate}.
     *
     * @param width        Width of the object being animated
     * @param height       Height of the object being animated
     * @param parentWidth  Width of the animated object's parent
     * @param parentHeight Height of the animated object's parent
     */
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        reset();
        mInitialized = true;

        mWidth = width;
        mHeight = height;
        mParentWidth = parentWidth;
        mParentHeight = parentHeight;
    }



    public abstract int getCanvasSaveFlags();
    public abstract void apply(GLCanvas canvas);
}
