package com.sam.demo.widget.refreshLayout;

import android.support.annotation.NonNull;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/4/2
 * 描    述：更新时间管理
 * 修订历史：
 * ================================================
 */
public class LastUpdateTimeUpdater implements Runnable {
    private AbsClassicRefreshView mRefreshView;
    private ITimeUpdater mUpdater;
    private boolean mRunning = false;

    LastUpdateTimeUpdater(AbsClassicRefreshView refreshView) {
        mRefreshView = refreshView;
        mUpdater = refreshView;
    }

    void setTimeUpdater(@NonNull ITimeUpdater updater) {
        mUpdater = updater;
    }

    public void start() {
        mRunning = true;
        if (mRefreshView != null)
            mRefreshView.post(this);
    }

    public void stop() {
        mRunning = false;
        if (mRefreshView != null)
            mRefreshView.removeCallbacks(this);
    }

    @Override
    public void run() {
        if (mUpdater != null && mRefreshView != null) {
            if (mUpdater.canUpdate()) {
                mUpdater.updateTime(mRefreshView);
            }
            mRefreshView.removeCallbacks(this);
            if (mRunning) {
                mRefreshView.postDelayed(this, 1000);
            }
        }
    }

    public interface ITimeUpdater {
        boolean canUpdate();

        void updateTime(AbsClassicRefreshView view);
    }
}
