package com.example.run.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.run.R;
import com.example.run.model.SelectInfo;

import java.util.List;

public class SelectRecyclerAdapter extends RecyclerView.Adapter<SelectRecyclerAdapter.ViewHolder> {

    private List<SelectInfo> list;
    private OnItemClickListener clickListener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.clickListener = listener;
    }

    public void setList(List<SelectInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_select, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAddress.setText(list.get(position).getAddress());
        holder.tvCity.setText(list.get(position).getCity());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onClick(list.get(position));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvAddress;
        private TextView tvCity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tv_address_search_select);
            tvCity = itemView.findViewById(R.id.tv_city_search_select);
        }
    }

    public interface OnItemClickListener{
        void onClick(SelectInfo info);
    }
}
