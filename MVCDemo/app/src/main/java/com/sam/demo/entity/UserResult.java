package com.sam.demo.entity;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/3/1
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class UserResult extends BaseResult {

    private static final long serialVersionUID = 2624495228695943886L;
    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
