package com.example.singleselectordemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sz132 on 2017/6/22.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Bean> list;
    private RecyclerView recyclerView;
    private Click click;
    private int currentPosition = -1;

    public void setClick(Click click) {
        this.click = click;
    }

    public MyAdapter(List<Bean> list, RecyclerView recyclerView) {
        this.list = list;
        this.recyclerView = recyclerView;

        for (int i = 0; i < list.size(); i++){
            if (list.get(i).isSelected()){
                currentPosition = i;
            }
        }
    }

    public void setList(List<Bean> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.textView.setText(list.get(position).getName());
        holder.checkBox.setChecked(list.get(position).isSelected());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition >= list.size()){
                    currentPosition = 0;
                }
                MyViewHolder viewHolder = (MyViewHolder) recyclerView.findViewHolderForLayoutPosition(currentPosition);
                if (viewHolder != null){
                    viewHolder.checkBox.setChecked(false);
                }else {
                    notifyDataSetChanged();
                }
                list.get(currentPosition).setSelected(false);
                currentPosition = position;
                list.get(currentPosition).setSelected(true);
                holder.checkBox.setChecked(true);
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.click(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        Button button;
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb);
            button = (Button) itemView.findViewById(R.id.button);
            textView = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    public interface Click{
        void click(int position);
    }
}
