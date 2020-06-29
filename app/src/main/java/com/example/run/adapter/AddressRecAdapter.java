package com.example.run.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.run.R;
import com.example.run.room.Address;

import org.w3c.dom.Text;

import java.util.List;

public class AddressRecAdapter extends RecyclerView.Adapter<AddressRecAdapter.ViewHolder> {

    private List<Address> list;
    private OnItemClickListener onClickListener;

    public void setList(List<Address> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnItemClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public AddressRecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_address,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressRecAdapter.ViewHolder holder, int position) {
        holder.tvAddress.setText(list.get(position).getAddress());
        holder.tvName.setText(list.get(position).getName());
        holder.tvPhone.setText(list.get(position).getPhone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null){
                    onClickListener.onClick(list.get(position));
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAddress;
        private TextView tvName;
        private TextView tvPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tv_address_address);
            tvName = itemView.findViewById(R.id.tv_name_address);
            tvPhone = itemView.findViewById(R.id.tv_phone_address);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    public interface OnItemClickListener{
        void onClick(Address address);
    }
}
