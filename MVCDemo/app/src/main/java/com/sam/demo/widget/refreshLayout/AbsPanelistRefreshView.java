package com.sam.demo.widget.refreshLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.sam.demo.R;

import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.extra.IRefreshView;
import me.dkzwm.widget.srl.indicator.IIndicator;
import me.dkzwm.widget.srl.utils.PixelUtl;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/4/2
 * 描    述：封装上拉记载下拉刷新
 * 修订历史：
 * ================================================
 */
public abstract class AbsPanelistRefreshView<T extends IIndicator> extends RelativeLayout
        implements IRefreshView<T> {
    @RefreshViewStyle
    protected int mStyle = STYLE_DEFAULT;
    protected int mDefaultHeightInDP = 64;
    protected int mRotateAniTime = 200;

    public AbsPanelistRefreshView(Context context) {
        this(context, null);
    }

    public AbsPanelistRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsPanelistRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            final TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable
                    .AbsClassicRefreshView, 0, 0);
            @RefreshViewStyle
            int style = arr.getInt(R.styleable.AbsClassicRefreshView_sr_style, mStyle);
            mStyle = style;
            arr.recycle();
        }





    }








    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @Override
    public int getStyle() {
        return mStyle;
    }

    public void setStyle(@RefreshViewStyle int style) {
        mStyle = style;
        requestLayout();
    }

    public void setTimeUpdater(@NonNull LastUpdateTimeUpdater.ITimeUpdater timeUpdater) {
    }

    public void setDefaultHeightInDP(@IntRange(from = 0) int defaultHeightInDP) {
        mDefaultHeightInDP = defaultHeightInDP;
    }

    @Override
    public int getCustomHeight() {
        return PixelUtl.dp2px(getContext(), mDefaultHeightInDP);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }
    /**
     * 重置视图;
     */
    @Override
    public void onReset(SmoothRefreshLayout frame) {
    }
    /**
     * 手指离开屏幕;
     */
    @Override
    public void onFingerUp(SmoothRefreshLayout layout, T indicator) {

    }

    @Override
    public void onPureScrollPositionChanged(SmoothRefreshLayout layout, byte status, T indicator) {
        if (indicator.hasJustLeftStartPosition()) {

        }
    }







}
