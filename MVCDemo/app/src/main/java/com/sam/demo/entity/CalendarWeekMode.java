/*
 * @author 牛世杰
 */

package com.sam.demo.entity;

/**
 * 周解析类
 * date：2019.01.18
 */

public class CalendarWeekMode extends BaseEntity {

    private String DATE;
    private String WEEK;

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getWEEK() {
        return WEEK;
    }

    public void setWEEK(String WEEK) {
        this.WEEK = WEEK;
    }

    @Override
    public String toString() {
        return this.WEEK;
    }
}
