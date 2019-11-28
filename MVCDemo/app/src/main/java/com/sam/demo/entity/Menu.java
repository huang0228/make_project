package com.sam.demo.entity;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/3/15
 * 描    述：菜单权限
 * 修订历史：
 * ================================================
 */

public class Menu extends BaseEntity{
    private static final long serialVersionUID = 1485064786202441141L;
    private String MENU_ID;
    private String MENU_NAME;

    private int MENU_QUERYPERSONAL;

    public int getMENU_QUERYPERSONAL() {
        return MENU_QUERYPERSONAL;
    }

    public void setMENU_QUERYPERSONAL(int MENU_QUERYPERSONAL) {
        this.MENU_QUERYPERSONAL = MENU_QUERYPERSONAL;
    }

    public String getMENU_ID() {
        return MENU_ID;
    }

    public void setMENU_ID(String MENU_ID) {
        this.MENU_ID = MENU_ID;
    }

    public String getMENU_NAME() {
        return MENU_NAME;
    }

    public void setMENU_NAME(String MENU_NAME) {
        this.MENU_NAME = MENU_NAME;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "MENU_ID='" + MENU_ID + '\'' +
                ", MENU_NAME='" + MENU_NAME + '\'' +
                ", MENU_QUERYPERSONAL=" + MENU_QUERYPERSONAL +
                '}';
    }
}
