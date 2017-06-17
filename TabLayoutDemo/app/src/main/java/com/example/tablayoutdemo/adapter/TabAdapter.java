package com.example.tablayoutdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tablayoutdemo.fragment.TabFragment;

/**
 * Created by sz132 on 2017/6/16.
 */

public class TabAdapter extends FragmentPagerAdapter {

    private String[] tabs;
    private String from;
    private int[] num;

    public TabAdapter(FragmentManager fm, String[] tabs, String from, int[] num) {
        super(fm);
        this.tabs = tabs;
        this.from = from;
        this.num = num;
    }

    @Override
    public Fragment getItem(int position) {
        return TabFragment.newInstance(num[position], from);
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
