package com.sam.demo.entity;
/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/3/12
 * 描    述：首页权限模板
 * 修订历史：
 * ================================================
 */

public class MainPermission extends BaseEntity {
    private static final long serialVersionUID = -6661908780365603900L;
    private String tabName;
    private int layoutId;
    private String tabSpec;
    private Class className;
    private boolean isShow=false;//在首页是否显示菜单

    private String codeAuth;

    public String getCodeAuth() {
        return codeAuth;
    }

    public void setCodeAuth(String codeAuth) {
        this.codeAuth = codeAuth;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public String getTabSpec() {
        return tabSpec;
    }

    public void setTabSpec(String tabSpec) {
        this.tabSpec = tabSpec;
    }

    public Class getClassName() {
        return className;
    }

    public void setClassName(Class className) {
        this.className = className;
    }
}
