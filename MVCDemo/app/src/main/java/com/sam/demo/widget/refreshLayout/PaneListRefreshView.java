package com.sam.demo.widget.refreshLayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.extra.IRefreshView;
import me.dkzwm.widget.srl.indicator.IIndicator;
import me.dkzwm.widget.srl.utils.PixelUtl;

/**
 * Created by Administrator on 2018/4/11.
 */

public abstract class PaneListRefreshView<T extends IIndicator> extends RelativeLayout
        implements IRefreshView<T> {
    protected int mStyle = STYLE_DEFAULT;
    protected int mDefaultHeightInDP = 64;
    public PaneListRefreshView(Context context) {
        this(context, null);
    }

    public PaneListRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaneListRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onFingerUp(SmoothRefreshLayout layout, T indicator) {

    }

    @Override
    public void onReset(SmoothRefreshLayout layout) {

    }

    @Override
    public void onRefreshPrepare(SmoothRefreshLayout layout) {

    }

    @Override
    public void onRefreshBegin(SmoothRefreshLayout layout, T indicator) {

    }

    @Override
    public void onRefreshComplete(SmoothRefreshLayout layout, boolean isSuccessful) {

    }

    @Override
    public void onRefreshPositionChanged(SmoothRefreshLayout layout, byte status, T indicator) {

    }

    @Override
    public void onPureScrollPositionChanged(SmoothRefreshLayout layout, byte status, T indicator) {

    }
    public int getStyle() {
        return mStyle;
    }

    @Override
    public int getCustomHeight() {
        return PixelUtl.dp2px(getContext(), mDefaultHeightInDP);
    }
}
