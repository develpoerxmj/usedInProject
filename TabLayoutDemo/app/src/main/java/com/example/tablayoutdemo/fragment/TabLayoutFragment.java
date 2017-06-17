package com.example.tablayoutdemo.fragment;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tablayoutdemo.R;
import com.example.tablayoutdemo.adapter.TabAdapter;

public class TabLayoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tab_layoyt, container, false);
    }

    private TabLayout layout;
    private ViewPager pager;
    private TabAdapter adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        layout = (TabLayout) view.findViewById(R.id.table);
        pager = (ViewPager) view.findViewById(R.id.page);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.text) != null){
            adapter = new TabAdapter(getFragmentManager()
                    ,new String[]{"全部", "待支付", "待发货", "待签收", "已完成"}
                    ,"btn2"
                    ,new int[]{1,2,3,4,5});
        }else {
            adapter = new TabAdapter(getFragmentManager()
                    ,new String[]{"所有", "待支付"
                    ,"待发货", "已发货"}
                    ,"btn1", new int[]{1,2,3,4});
        }
        pager.setAdapter(adapter);
        layout.setupWithViewPager(pager);
    }
}
