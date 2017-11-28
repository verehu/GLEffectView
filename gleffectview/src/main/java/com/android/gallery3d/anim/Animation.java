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

import android.view.animation.Interpolator;
import android.view.animation.Transformation;

import com.android.gallery3d.common.Utils;

// Animation calculates a value according to the current input time.
//
// 1. First we need to use setDuration(int) to set the duration of the
//    animation. The duration is in milliseconds.
// 2. Then we should call start(). The actual start time is the first value
//    passed to calculate(long).
// 3. Each time we want to get an animation value, we call
//    calculate(long currentTimeMillis) to ask the Animation to calculate it.
//    The parameter passed to calculate(long) should be nonnegative.
// 4. Use get() to get that value.
//
// In step 3, onCalculate(float progress) is called so subclasses can calculate
// the value according to progress (progress is a value in [0,1]).
//
// Before onCalculate(float) is called, There is an optional interpolator which
// can change the progress value. The interpolator can be set by
// setInterpolator(Interpolator). If the interpolator is used, the value passed
// to onCalculate may be (for example, the overshoot effect).
//
// The isActive() method returns true after the animation start() is called and
// before calculate is passed a value which reaches the duration of the
// animation.
//
// The start() method can be called again to restart the Animation.
//
abstract public class Animation {
    private static final long ANIMATION_START = -1;
    private static final long NO_ANIMATION = -2;

    /**
     * Repeat the animation indefinitely.
     */
    public static final int INFINITE = -1;

    /**
     * When the animation reaches the end and the repeat count is INFINTE_REPEAT
     * or a positive value, the animation restarts from the beginning.
     */
    public static final int RESTART = 1;

    /**
     * When the animation reaches the end and the repeat count is INFINTE_REPEAT
     * or a positive value, the animation plays backward (and then forward again).
     */
    public static final int REVERSE = 2;

    private long mStartTime = NO_ANIMATION;

    /**
     * The delay in milliseconds after which the animation must start. When the
     * start offset is > 0, the start time of the animation is startTime + startOffset.
     */
    long mStartOffset;

    /**
     * The number of times the animation must repeat. By default, an animation repeats
     * indefinitely.
     */
    int mRepeatCount = 0;

    /**
     * Indicates how many times the animation was repeated.
     */
    int mRepeated = 0;

    /**
     * The behavior of the animation when it repeats. The repeat mode is either
     * {@link #RESTART} or {@link #REVERSE}.
     */
    int mRepeatMode = RESTART;

    /**
     * Set by  when the animation repeats
     * in REVERSE mode.
     */
    boolean mCycleFlip = false;

    private int mDuration;
    private Interpolator mInterpolator;

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public int getRepeatMode() {
        return mRepeatMode;
    }

    public void setRepeatMode(int mRepeatMode) {
        this.mRepeatMode = mRepeatMode;
    }

    public void setRepeatCount(int repeatCount) {
        if (repeatCount < 0) {
            repeatCount = INFINITE;
        }
        this.mRepeatCount = repeatCount;
    }

    public long getStartOffset() {
        return mStartOffset;
    }

    public void setStartOffset(long mStartOffset) {
        this.mStartOffset = mStartOffset;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void start() {
        mStartTime = ANIMATION_START;
    }

    public void setStartTime(long time) {
        mStartTime = time;
    }

    public boolean isActive() {
        return mStartTime != NO_ANIMATION;
    }

    public boolean isCanceled() {
        return false;
    }

    public void forceStop() {
        mStartTime = NO_ANIMATION;
    }

    public boolean calculate(long currentTimeMillis) {
        if (mStartTime == NO_ANIMATION) return false;
        if (mStartTime == ANIMATION_START) mStartTime = currentTimeMillis;
        boolean more = true;

        final int duration = mDuration;
        final long startOffset = mStartOffset;
        float normalizedTime;
        if (duration != 0) {
            normalizedTime = ((float) (currentTimeMillis - (mStartTime + startOffset))) /
                    (float) duration;
        } else {
            // time is a step-change with a zero duration
            normalizedTime = currentTimeMillis < mStartTime ? 0.0f : 1.0f;
        }
        final boolean expired = normalizedTime >= 1.0f || isCanceled();

        if (mCycleFlip) {
            normalizedTime = 1 - normalizedTime;
        }

        Interpolator i = mInterpolator;
        onCalculate(i != null ? i.getInterpolation(normalizedTime) : normalizedTime);
        if (expired) {
            if (mRepeated == mRepeatCount) {
                mStartTime = NO_ANIMATION;

                more = false;
            } else {
                if (mRepeatCount > 0) {
                    mRepeated++;
                }

                if (mRepeatMode == REVERSE) {
                    mCycleFlip = !mCycleFlip;
                }

                mStartTime = ANIMATION_START;
            }
        }
        return more;
    }

    abstract protected void onCalculate(float progress);
}
