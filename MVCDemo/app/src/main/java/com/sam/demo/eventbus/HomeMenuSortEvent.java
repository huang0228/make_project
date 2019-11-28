package com.sam.demo.eventbus;

import com.sam.demo.entity.MainPermission;

import java.util.List;

/**
 * 描    述：首页菜单列表排序传参
 * 修订历史：
 * ================================================
 */

public class HomeMenuSortEvent {
    private List<MainPermission> tempMainPermission;
    private String strMove;//辨识位，确定个性化设置页面是否改变内容

    public String getStrMove() {
        return strMove;
    }

    public void setStrMove(String strMove) {
        this.strMove = strMove;
    }

    public HomeMenuSortEvent(List<MainPermission> tempMainPermission) {
        this.tempMainPermission = tempMainPermission;
    }

    public List<MainPermission> getTempMainPermission() {
        return tempMainPermission;
    }

    public void setTempMainPermission(List<MainPermission> tempMainPermission) {
        this.tempMainPermission = tempMainPermission;
    }
}
