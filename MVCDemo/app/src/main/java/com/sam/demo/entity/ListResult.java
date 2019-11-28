package com.sam.demo.entity;

import java.util.List;

/**
 * Created by lenovo on 2018/4/17.
 */

public class ListResult<T> extends BaseResult {
    private int total;
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
