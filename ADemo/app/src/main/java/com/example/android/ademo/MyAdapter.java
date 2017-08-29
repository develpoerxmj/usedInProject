package com.example.android.ademo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sz132 on 2017/8/28.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int TYPE_TIME = 1;
    public static final int TYPE_ITEM = 2;

    private List<Bean> mList;

    public MyAdapter(List<Bean> list) {
        mList = list;
    }

    public void setList(List<Bean> list) {
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_TIME:
                return new TimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_time, null));
            case TYPE_ITEM:
                return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TimeViewHolder){
            ((TimeViewHolder) holder).mTime.setText(((Bean)getItemData(position)).getTime());
        }else if (holder instanceof ItemViewHolder){
            if (((Bean.BeanItem)getItemData(position)).getState().equals("1")){
                ((ItemViewHolder) holder).one.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).two.setVisibility(View.GONE);
            }else {
                ((ItemViewHolder) holder).one.setVisibility(View.GONE);
                ((ItemViewHolder) holder).two.setVisibility(View.VISIBLE);
            }
        }
    }

    public Object getItemData(int position){
        int count = 0;
        for (Bean bean:mList) {
            if (position - count == 0){
                return bean;
            }else if (position - count > 0 && position - count <= bean.getItemList().size()){
                return bean.getItemList().get(position - count - 1);
            }
            count += bean.getItemList().size() + 1;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (Bean bean : mList) {
            count += 1 + bean.getItemList().size();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int current = 0;
        for (Bean bean : mList) {
            if (position - current == 0){
                return TYPE_TIME;
            }
            if (position - current > 0 && position - current <= bean.getItemList().size()){
                return TYPE_ITEM;
            }
            current += (1 + bean.getItemList().size());
        }
        return 0;
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder{
        TextView mTime;
        public TimeViewHolder(View itemView) {
            super(itemView);
            mTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        Button one;
        Button two;
        public ItemViewHolder(View itemView) {
            super(itemView);
            one = (Button) itemView.findViewById(R.id.one);
            two = (Button) itemView.findViewById(R.id.two);
        }
    }
}
