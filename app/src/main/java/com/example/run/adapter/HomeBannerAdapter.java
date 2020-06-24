package com.example.run.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.run.R;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import java.util.List;

public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.ViewHodler> {

    private List<Integer> list;

    public HomeBannerAdapter(List<Integer> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_home, parent, false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        holder.ivPic.setImageResource(list.get(position % list.size()));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0: Integer.MAX_VALUE;
    }

    class ViewHodler extends RecyclerView.ViewHolder{
        private RadiusImageView ivPic;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.iv_ban_home);
        }
    }
}
