package com.sam.demo.eventbus;

import com.sam.demo.entity.HomeBlockEntity;

import java.util.List;

/**
 * 描    述：首页模块Block菜单列表传参
 * 修订历史：
 * ================================================
 */

public class HomeBlockEvent {
    private List<HomeBlockEntity> tempMainPermission;

    public HomeBlockEvent(List<HomeBlockEntity> tempMainPermission) {
        this.tempMainPermission = tempMainPermission;
    }

    public List<HomeBlockEntity> getTempMainPermission() {
        return tempMainPermission;
    }

    public void setTempMainPermission(List<HomeBlockEntity> tempMainPermission) {
        this.tempMainPermission = tempMainPermission;
    }
}
