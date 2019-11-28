/*
 * @author 牛世杰
 */

package com.sam.demo.entity;

/**
 * Created by lenovo on 2018/5/3.
 */

public class BasicData extends BaseEntity {
    private String ITEM_KEY;
    private String ITEM_SCRIPT;

    public String getITEM_KEY() {
        return ITEM_KEY;
    }

    public void setITEM_KEY(String ITEM_KEY) {
        this.ITEM_KEY = ITEM_KEY;
    }

    public String getITEM_SCRIPT() {
        return ITEM_SCRIPT;
    }

    public void setITEM_SCRIPT(String ITEM_SCRIPT) {
        this.ITEM_SCRIPT = ITEM_SCRIPT;
    }

    @Override
    public String toString() {
        return this.ITEM_SCRIPT;
    }
}
