package com.example.android.ademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new MyAdapter(initData());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<Bean> initData() {
        List<Bean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            list.add(new Bean("2017-08-"+i*2, getItemData()));
        }
        return list;
    }

    private List<Bean.BeanItem> getItemData(){
        List<Bean.BeanItem> list = new ArrayList<>();
        int count = new Random().nextInt(15)+1;
        for (int i = 0; i < count; i++){
            Bean.BeanItem item;
            if (i % 2 == 0){
                item = new Bean.BeanItem("1");
            }else {
                item = new Bean.BeanItem("0");
            }
            list.add(item);
        }
        return list;
    }
}
