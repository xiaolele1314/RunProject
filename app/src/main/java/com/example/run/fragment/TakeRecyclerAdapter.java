package com.example.run.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.run.R;
import com.example.run.databinding.RecyclerTakeOrderBinding;
import com.example.run.model.Take;

import java.util.List;

public class TakeRecyclerAdapter extends RecyclerView.Adapter<TakeRecyclerAdapter.ViewHolder> {

    private List<Take> list;
    private RecyclerTakeOrderBinding binding;

    public void setList(List<Take> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_take_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        binding.setData(list.get(position));
        if (list.get(position).getRunType() == 1){
            binding.tvRunOrder.setText("待接单");
        }else if (list.get(position).getRunType() == 2){
            binding.tvRunOrder.setText("已接单");
        }else if (list.get(position).getRunType() == 3){
            binding.tvRunOrder.setText("配送中");
        }else if (list.get(position).getRunType() == 4){
            binding.tvRunOrder.setText("完成");
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
