package com.android.gallery3d.anim;

import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;

import com.android.gallery3d.glrenderer.GLCanvas;

import static android.view.animation.Animation.ABSOLUTE;

/**
 * Created by huwei on 17-11-28.
 */

public class ScaleAnimation extends CanvasAnimation {
    private static final String TAG = "ScaleAnimation";
    private float mFromX;
    private float mToX;
    private float mFromY;
    private float mToY;

    private int mFromXType = TypedValue.TYPE_NULL;
    private int mToXType = TypedValue.TYPE_NULL;
    private int mFromYType = TypedValue.TYPE_NULL;
    private int mToYType = TypedValue.TYPE_NULL;

    private int mFromXData = 0;
    private int mToXData = 0;
    private int mFromYData = 0;
    private int mToYData = 0;

    private int mPivotXType = ABSOLUTE;
    private int mPivotYType = ABSOLUTE;
    private float mPivotXValue = 0.0f;
    private float mPivotYValue = 0.0f;

    private float mPivotX;
    private float mPivotY;

    private float mSx;
    private float mSy;

    /**
     * scalefactor to apply to pivot points, etc. during animation. Subclasses retrieve the
     * value via getScaleFactor().
     */
    private float mScaleFactor = 1f;

    /**
     * Constructor to use when building a ScaleAnimation from code
     *
     * @param fromX Horizontal scaling factor to apply at the start of the
     *              animation
     * @param toX   Horizontal scaling factor to apply at the end of the animation
     * @param fromY Vertical scaling factor to apply at the start of the
     *              animation
     * @param toY   Vertical scaling factor to apply at the end of the animation
     */
    public ScaleAnimation(float fromX, float toX, float fromY, float toY) {
        mFromX = fromX;
        mToX = toX;
        mFromY = fromY;
        mToY = toY;
        mPivotX = 0;
        mPivotY = 0;
    }

    /**
     * Constructor to use when building a ScaleAnimation from code
     *
     * @param fromX  Horizontal scaling factor to apply at the start of the
     *               animation
     * @param toX    Horizontal scaling factor to apply at the end of the animation
     * @param fromY  Vertical scaling factor to apply at the start of the
     *               animation
     * @param toY    Vertical scaling factor to apply at the end of the animation
     * @param pivotX The X coordinate of the point about which the object is
     *               being scaled, specified as an absolute number where 0 is the left
     *               edge. (This point remains fixed while the object changes size.)
     * @param pivotY The Y coordinate of the point about which the object is
     *               being scaled, specified as an absolute number where 0 is the top
     *               edge. (This point remains fixed while the object changes size.)
     */
    public ScaleAnimation(float fromX, float toX, float fromY, float toY,
                          float pivotX, float pivotY) {
        mFromX = fromX;
        mToX = toX;
        mFromY = fromY;
        mToY = toY;

        mPivotXType = ABSOLUTE;
        mPivotYType = ABSOLUTE;
        mPivotXValue = pivotX;
        mPivotYValue = pivotY;
        initializePivotPoint();
    }

    /**
     * Constructor to use when building a ScaleAnimation from code
     *
     * @param fromX       Horizontal scaling factor to apply at the start of the
     *                    animation
     * @param toX         Horizontal scaling factor to apply at the end of the animation
     * @param fromY       Vertical scaling factor to apply at the start of the
     *                    animation
     * @param toY         Vertical scaling factor to apply at the end of the animation
     * @param pivotXType  Specifies how pivotXValue should be interpreted. One of
     *                    Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *                    Animation.RELATIVE_TO_PARENT.
     * @param pivotXValue The X coordinate of the point about which the object
     *                    is being scaled, specified as an absolute number where 0 is the
     *                    left edge. (This point remains fixed while the object changes
     *                    size.) This value can either be an absolute number if pivotXType
     *                    is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param pivotYType  Specifies how pivotYValue should be interpreted. One of
     *                    Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *                    Animation.RELATIVE_TO_PARENT.
     * @param pivotYValue The Y coordinate of the point about which the object
     *                    is being scaled, specified as an absolute number where 0 is the
     *                    top edge. (This point remains fixed while the object changes
     *                    size.) This value can either be an absolute number if pivotYType
     *                    is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     */
    public ScaleAnimation(float fromX, float toX, float fromY, float toY,
                          int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
        mFromX = fromX;
        mToX = toX;
        mFromY = fromY;
        mToY = toY;

        mPivotXValue = pivotXValue;
        mPivotXType = pivotXType;
        mPivotYValue = pivotYValue;
        mPivotYType = pivotYType;
        initializePivotPoint();
    }

    protected float getScaleFactor() {
        return mScaleFactor;
    }

    /**
     * Called at the end of constructor methods to initialize, if possible, values for
     * the pivot point. This is only possible for ABSOLUTE pivot values.
     */
    private void initializePivotPoint() {
        if (mPivotXType == ABSOLUTE) {
            mPivotX = mPivotXValue;
        }
        if (mPivotYType == ABSOLUTE) {
            mPivotY = mPivotYValue;
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

        mFromX = resolveScale(mFromX, mFromXType, mFromXData, width, parentWidth);
        mToX = resolveScale(mToX, mToXType, mToXData, width, parentWidth);
        mFromY = resolveScale(mFromY, mFromYType, mFromYData, height, parentHeight);
        mToY = resolveScale(mToY, mToYType, mToYData, height, parentHeight);

        mPivotX = resolveSize(mPivotXType, mPivotXValue, width, parentWidth);
        mPivotY = resolveSize(mPivotYType, mPivotYValue, height, parentHeight);
    }

    float resolveScale(float scale, int type, int data, int size, int psize) {
        float targetSize;
        if (type == TypedValue.TYPE_FRACTION) {
            targetSize = TypedValue.complexToFraction(data, size, psize);
        } else if (type == TypedValue.TYPE_DIMENSION) {
            //resource里面解析动画时需要，目前用不上。
            //targetSize = TypedValue.complexToDimension(data, mResources.getDisplayMetrics());
            targetSize = 0;
        } else {
            return scale;
        }

        if (size == 0) {
            return 1;
        }

        return targetSize / (float) size;
    }

    @Override
    public int getCanvasSaveFlags() {
        return GLCanvas.SAVE_FLAG_MATRIX;
    }

    @Override
    public void apply(GLCanvas canvas) {
        Log.i(TAG, "sx:" + mSx + "  sy:" + mSy);

        canvas.scale(mSx, mSy, 1);
        canvas.translate( - mPivotX ,  -mPivotY);
    }


    @Override
    protected void onCalculate(float interpolatedTime) {
        mSx = (mToX - mFromX) * interpolatedTime;
        mSy = (mToY - mFromY) * interpolatedTime;
    }
}
