package com.example.tablayoutdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tablayoutdemo.bean.Bean;
import com.example.tablayoutdemo.R;
import com.example.tablayoutdemo.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sz132 on 2017/6/16.
 */

public class TabFragment extends Fragment {

    public static TabFragment newInstance(int type, String from){
        TabFragment fragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment, container, false);
    }

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private int type;
    private String from;
    private List<Bean> list = new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerAdapter();
        adapter.setList(list);
        recyclerView.setAdapter(adapter);
        if (getArguments() != null){
            type = getArguments().getInt("type");
            from = getArguments().getString("from");
        }
        if ("btn1".equals(from)){
            switch (type){
                case 1:
                    initData1(1);
                    break;
                case 2:
                    initData1(3);
                    break;
                case 3:
                    initData1(5);
                    break;
                case 4:
                    initData1(7);
                    break;
                default:
                    break;
            }
        }else if ("btn2".equals(from)){
            ((TextView)getActivity().findViewById(R.id.text)).setText("呦呦彻克闹");
            switch (type){
                case 1:
                    initData2(2);
                    break;
                case 2:
                    initData2(4);
                    break;
                case 3:
                    initData2(6);
                    break;
                case 4:
                    initData2(8);
                    break;
                case 5:
                    initData2(10);
                    break;
                default:
                    break;
            }
        }
    }

    private void initData2(int num) {
        for(int i=0; i<num; i++){
            list.add(new Bean());
        }
        adapter.notifyDataSetChanged();
    }

    private void initData1(int num) {
        for(int i=0; i<num; i++){
            list.add(new Bean());
        }
        adapter.notifyDataSetChanged();
    }
}
