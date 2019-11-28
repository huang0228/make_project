package com.sam.demo.entity;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/3/7
 * 描    述：token
 * 修订历史：
 * ================================================
 */

public class Token extends BaseEntity {

    private static final long serialVersionUID = -2940763060723008385L;
    private String Token;
    private int Duration;

    public String getValue() {
        return Token;
    }

    public void setValue(String value) {
        this.Token = value;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        this.Duration = duration;
    }
}
