package com.android.gallery3d.anim;

import com.android.gallery3d.glrenderer.GLCanvas;

import static android.view.animation.Animation.ABSOLUTE;


/**
 * Created by huwei on 17-11-28.
 */

public class TranslateAnimation extends CanvasAnimation {
    private static final String TAG = "TranslateAnimation";
    private int mFromXType = ABSOLUTE;
    private int mToXType = ABSOLUTE;

    private int mFromYType = ABSOLUTE;
    private int mToYType = ABSOLUTE;

    /**
     * @hide
     */
    protected float mFromXValue = 0.0f;
    /**
     * @hide
     */
    protected float mToXValue = 0.0f;

    /**
     * @hide
     */
    protected float mFromYValue = 0.0f;
    /**
     * @hide
     */
    protected float mToYValue = 0.0f;

    /**
     * @hide
     */
    protected float mFromXDelta;
    /**
     * @hide
     */
    protected float mToXDelta;
    /**
     * @hide
     */
    protected float mFromYDelta;
    /**
     * @hide
     */
    protected float mToYDelta;

    protected float mCurrentX;
    protected float mCurrentY;
    protected float mDx;
    protected float mDy;

    /**
     * Constructor to use when building a TranslateAnimation from code
     *
     * @param fromXDelta Change in X coordinate to apply at the start of the
     *                   animation
     * @param toXDelta   Change in X coordinate to apply at the end of the
     *                   animation
     * @param fromYDelta Change in Y coordinate to apply at the start of the
     *                   animation
     * @param toYDelta   Change in Y coordinate to apply at the end of the
     *                   animation
     */
    public TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        mFromXValue = fromXDelta;
        mToXValue = toXDelta;
        mFromYValue = fromYDelta;
        mToYValue = toYDelta;

        mFromXType = ABSOLUTE;
        mToXType = ABSOLUTE;
        mFromYType = ABSOLUTE;
        mToYType = ABSOLUTE;
    }

    /**
     * Constructor to use when building a TranslateAnimation from code
     *
     * @param fromXType  Specifies how fromXValue should be interpreted. One of
     *                   Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *                   Animation.RELATIVE_TO_PARENT.
     * @param fromXValue Change in X coordinate to apply at the start of the
     *                   animation. This value can either be an absolute number if fromXType
     *                   is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param toXType    Specifies how toXValue should be interpreted. One of
     *                   Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *                   Animation.RELATIVE_TO_PARENT.
     * @param toXValue   Change in X coordinate to apply at the end of the
     *                   animation. This value can either be an absolute number if toXType
     *                   is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param fromYType  Specifies how fromYValue should be interpreted. One of
     *                   Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *                   Animation.RELATIVE_TO_PARENT.
     * @param fromYValue Change in Y coordinate to apply at the start of the
     *                   animation. This value can either be an absolute number if fromYType
     *                   is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     * @param toYType    Specifies how toYValue should be interpreted. One of
     *                   Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF, or
     *                   Animation.RELATIVE_TO_PARENT.
     * @param toYValue   Change in Y coordinate to apply at the end of the
     *                   animation. This value can either be an absolute number if toYType
     *                   is ABSOLUTE, or a percentage (where 1.0 is 100%) otherwise.
     */
    public TranslateAnimation(int fromXType, float fromXValue, int toXType, float toXValue,
                              int fromYType, float fromYValue, int toYType, float toYValue) {

        mFromXValue = fromXValue;
        mToXValue = toXValue;
        mFromYValue = fromYValue;
        mToYValue = toYValue;

        mFromXType = fromXType;
        mToXType = toXType;
        mFromYType = fromYType;
        mToYType = toYType;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mFromXDelta = resolveSize(mFromXType, mFromXValue, width, parentWidth);
        mToXDelta = resolveSize(mToXType, mToXValue, width, parentWidth);
        mFromYDelta = resolveSize(mFromYType, mFromYValue, height, parentHeight);
        mToYDelta = resolveSize(mToYType, mToYValue, height, parentHeight);

        mCurrentX = mFromXDelta;
        mCurrentY = mFromYDelta;
    }

    @Override
    public int getCanvasSaveFlags() {
        return GLCanvas.SAVE_FLAG_MATRIX;
    }

    @Override
    public void apply(GLCanvas canvas) {
        //由于在执行apply之前会canvas.save(),所以这里永远是相对第一次位置的偏移。
        canvas.translate(mDx, mDy);
    }

    @Override
    protected void onCalculate(float interpolatedTime) {
        mDx =  (mToXDelta - mFromXDelta) * interpolatedTime;
        mDy = (mToYDelta - mFromYDelta) * interpolatedTime;
    }
}
