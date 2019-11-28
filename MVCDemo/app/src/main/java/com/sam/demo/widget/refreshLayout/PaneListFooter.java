package com.sam.demo.widget.refreshLayout;

import android.content.Context;
import android.util.AttributeSet;

import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.indicator.IIndicator;

/**
 * Created by Administrator on 2018/4/11.
 */

public class PaneListFooter extends AbsPanelistRefreshView {

    public PaneListFooter(Context context) {
        this(context, null);
    }

    public PaneListFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaneListFooter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public int getType() {
        return TYPE_FOOTER;
    }

    @Override
    public void onRefreshPrepare(SmoothRefreshLayout layout) {

    }

    @Override
    public void onRefreshBegin(SmoothRefreshLayout layout, IIndicator indicator) {

    }

    @Override
    public void onRefreshComplete(SmoothRefreshLayout layout, boolean isSuccessful) {

    }

    @Override
    public void onRefreshPositionChanged(SmoothRefreshLayout layout, byte status, IIndicator indicator) {

    }



}
