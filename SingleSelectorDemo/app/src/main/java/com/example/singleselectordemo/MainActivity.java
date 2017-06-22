package com.example.singleselectordemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;

    private List<Bean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new MyAdapter(list ,recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setClick(new MyAdapter.Click() {
            @Override
            public void click(int position) {
                list.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, list.size());
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 15; i++){
            Bean bean;
            if (i == 10){
                bean = new Bean("来啊来啊"+i, true);
            }else {
                bean = new Bean("来啊来啊"+i, false);
            }
            list.add(bean);
        }
    }
}
