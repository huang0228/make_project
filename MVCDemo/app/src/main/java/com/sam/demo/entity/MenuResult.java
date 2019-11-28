package com.sam.demo.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 爱代码，不爱BUG
 * 作    者：赵腾飞
 * 版    本：1.0.1
 * 创建日期：2018/3/15
 * 描    述：菜单权限回调
 * 修订历史：
 * ================================================
 */

public class MenuResult extends BaseResult {
    private List<Menu> data=new ArrayList<>();

    public List<Menu> getData() {
        return data;
    }

    public void setData(List<Menu> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MenuResult{" +
                "data=" + data +
                '}';
    }
}
