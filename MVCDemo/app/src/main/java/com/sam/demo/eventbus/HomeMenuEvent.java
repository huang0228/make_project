package com.sam.demo.eventbus;

import com.sam.demo.entity.MainPermission;

import java.util.List;

/**
 * 描    述：首页菜单列表传参
 * 修订历史：
 * ================================================
 */

public class HomeMenuEvent {
    private List<MainPermission> tempMainPermission;

    public HomeMenuEvent(List<MainPermission> tempMainPermission) {
        this.tempMainPermission = tempMainPermission;
    }

    public List<MainPermission> getTempMainPermission() {
        return tempMainPermission;
    }

    public void setTempMainPermission(List<MainPermission> tempMainPermission) {
        this.tempMainPermission = tempMainPermission;
    }
}
