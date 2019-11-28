package com.sam.demo.eventbus;

/**
 * Created by Administrator on 2018/7/5.
 */

/**
 * 版本监听事件
 * sam.huang
 * date 2018.12.29
 */

public class CheckVersionEvent {
    private boolean isNewVersion;

    public CheckVersionEvent(boolean versino)
    {
        this.isNewVersion=versino;
    }

    public boolean isNewVersion() {
        return isNewVersion;
    }

    public void setNewVersion(boolean newVersion) {
        isNewVersion = newVersion;
    }
}
