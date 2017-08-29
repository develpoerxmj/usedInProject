package com.example.android.ademo;

import java.util.List;

/**
 * Created by sz132 on 2017/8/28.
 */

public class Bean {

    private String time;
    private List<BeanItem> mItemList;

    public Bean(String time, List<BeanItem> itemList) {
        this.time = time;
        mItemList = itemList;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<BeanItem> getItemList() {
        return mItemList;
    }

    public void setItemList(List<BeanItem> itemList) {
        mItemList = itemList;
    }

    public static class BeanItem{
        private String state;

        public BeanItem(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
