package com.sam.demo.entity;

import com.sam.demo.base.BaseActivity;

/**
 * 作者：sam.huang
 * <p>
 * 创建日期：2018/8/24
 * <p>
 * 描述：
 */
public class HomeBlockEntity extends BaseActivity {

    private int drawable;
    private String name;
    private String code;
    private boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
