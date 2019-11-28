/*
 * @author 牛世杰
 */

package com.sam.demo.entity;

import java.util.List;

/**
 * Created by lenovo on 2018/4/17.
 */

public class ListResult2<T> extends BaseResult {
    private Data<T> data;

    public Data<T> getData() {
        return data;
    }

    public void setData(Data<T> data) {
        this.data = data;
    }


    public class Data<T>{
        private int unread_message_count;
        private int Total;
        private List<T> Rows;

        public int getUnread_message_count() {
            return unread_message_count;
        }

        public void setUnread_message_count(int unread_message_count) {
            this.unread_message_count = unread_message_count;
        }

        public int getTotal() {
            return Total;
        }

        public void setTotal(int total) {
            Total = total;
        }

        public List<T> getRows() {
            return Rows;
        }

        public void setRows(List<T> rows) {
            Rows = rows;
        }
    }
}
