package com.sam.demo.widget.refreshLayout;


import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.sam.demo.R;

import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.indicator.IIndicator;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/4/2
 * 描    述：下拉刷新
 * 修订历史：
 * ================================================
 */
public class ClassicHeader<T extends IIndicator> extends AbsClassicRefreshView<T> {
    @StringRes
    private int mPullDownToRefreshRes = R.string.sr_pull_down_to_refresh;
    @StringRes
    private int mPullDownRes = R.string.sr_pull_down;
    @StringRes
    private int mRefreshingRes = R.string.sr_refreshing;
    @StringRes
    private int mRefreshSuccessfulRes = R.string.sr_refresh_complete;
    @StringRes
    private int mRefreshFailRes = R.string.sr_refresh_failed;
    @StringRes
    private int mReleaseToRefreshRes = R.string.sr_release_to_refresh;

    public ClassicHeader(Context context) {
        this(context, null);
    }

    public ClassicHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mArrowImageView.setImageResource(R.drawable.sr_classic_arrow_icon);
    }

    public void setPullDownToRefreshRes(@StringRes int pullDownToRefreshRes) {
        mPullDownToRefreshRes = pullDownToRefreshRes;
    }

    public void setPullDownRes(@StringRes int pullDownRes) {
        mPullDownRes = pullDownRes;
    }

    public void setRefreshingRes(@StringRes int refreshingRes) {
        mRefreshingRes = refreshingRes;
    }

    public void setRefreshSuccessfulRes(@StringRes int refreshSuccessfulRes) {
        mRefreshSuccessfulRes = refreshSuccessfulRes;
    }

    public void setRefreshFailRes(@StringRes int refreshFailRes) {
        mRefreshFailRes = refreshFailRes;
    }

    public void setReleaseToRefreshRes(@StringRes int releaseToRefreshRes) {
        mReleaseToRefreshRes = releaseToRefreshRes;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

    /**
     * 重新配置视图，准备刷新;
     */
    @Override
    public void onRefreshPrepare(SmoothRefreshLayout frame) {
        mShouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();
        if (!TextUtils.isEmpty(mLastUpdateTimeKey))
            mLastUpdateTimeUpdater.start();
        mProgressBar.setVisibility(INVISIBLE);
        mArrowImageView.setVisibility(VISIBLE);
        mTitleTextView.setVisibility(VISIBLE);
        if (frame.isEnabledPullToRefresh()) {
            mTitleTextView.setText(mPullDownToRefreshRes);
        } else {
            mTitleTextView.setText(mPullDownRes);
        }
    }

    /**
     * 开始刷新;
     */
    @Override
    public void onRefreshBegin(SmoothRefreshLayout frame, T indicator) {
        mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(INVISIBLE);
        mProgressBar.setVisibility(VISIBLE);
        mTitleTextView.setVisibility(VISIBLE);
        mTitleTextView.setText(mRefreshingRes);
        tryUpdateLastUpdateTime();
    }

    /**
     * 刷新完成;
     */
    @Override
    public void onRefreshComplete(SmoothRefreshLayout frame, boolean isSuccessful) {
        mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(INVISIBLE);
        mProgressBar.setVisibility(INVISIBLE);
        mTitleTextView.setVisibility(VISIBLE);
        if (frame.isRefreshSuccessful()) {
            //刷新完成
            mTitleTextView.setText(mRefreshSuccessfulRes);
            mLastUpdateTime = System.currentTimeMillis();
            ClassicConfig.updateTime(getContext(), mLastUpdateTimeKey, mLastUpdateTime);
        } else
            mTitleTextView.setText(mRefreshFailRes);
    }

    /**
     * 当头部视图发生位置变化;
     */
    @Override
    public void onRefreshPositionChanged(SmoothRefreshLayout frame, byte status, T indicator) {
        final int offsetToRefresh = indicator.getOffsetToRefresh();
        final int currentPos = indicator.getCurrentPos();
        final int lastPos = indicator.getLastPos();

        if (currentPos < offsetToRefresh && lastPos >= offsetToRefresh) {
            if (indicator.hasTouched() && status == SmoothRefreshLayout.SR_STATUS_PREPARE) {
                //滑动距离美触发刷新
                mTitleTextView.setVisibility(VISIBLE);
                mTitleTextView.setText(mPullDownToRefreshRes);
                mArrowImageView.setVisibility(VISIBLE);
                mArrowImageView.clearAnimation();
                mArrowImageView.startAnimation(mReverseFlipAnimation);
            }
        } else if (currentPos > offsetToRefresh && lastPos <= offsetToRefresh) {

            if (indicator.hasTouched() && status == SmoothRefreshLayout.SR_STATUS_PREPARE) {
                //滑动距离触发刷新
                mTitleTextView.setVisibility(VISIBLE);
                if (!frame.isEnabledPullToRefresh()) {
                    mTitleTextView.setText(mReleaseToRefreshRes);
                }
                mArrowImageView.setVisibility(VISIBLE);
                mArrowImageView.clearAnimation();
                mArrowImageView.startAnimation(mFlipAnimation);
            }
        }
    }
}
