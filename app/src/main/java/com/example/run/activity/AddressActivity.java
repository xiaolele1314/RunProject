package com.example.run.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.run.BaseActivity;
import com.example.run.R;
import com.example.run.adapter.AddressRecAdapter;
import com.example.run.java.LiveDataBus;
import com.example.run.room.Address;
import com.example.run.room.AddressManager;

import java.util.List;

public class AddressActivity extends BaseActivity {

    private TextView tvAdd;
    private RecyclerView recyclerView;
    private ImageView ivClose;

    private AddressRecAdapter adapter;
    private AddressManager manager;

    private int TYPE  = 2;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        init();
        initView();
        initData();
        initEvent();
    }


    /**
     * 初始化manager
     */
    @Override
    public void init() {
        manager = new AddressManager(this);
        type = getIntent().getIntExtra("type",0);
    }

    @Override
    public void initView() {
        tvAdd = findViewById(R.id.tv_add_address);
        recyclerView = findViewById(R.id.rec_address);
        ivClose = findViewById(R.id.iv_close_address);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

        //初始化适配器并为recyclerView设置适配器
        adapter = new AddressRecAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        //监听room数据库，有数据添加就向list加入
        manager.getAddressList().observe(this, new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addresses) {
                if(addresses.size() > 0){
                    adapter.setList(addresses);
                }

            }
        });
    }

    @Override
    public void initEvent() {

        //点击添加地址跳转SelectActivity添加
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this,SelectActivity.class);
                intent.putExtra("type",TYPE);
                startActivity(intent);
            }
        });

        //点击item选项向TakeActivity发送添加的address添加的数据
        adapter.setOnClickListener(new AddressRecAdapter.OnItemClickListener() {
            @Override
            public void onClick(Address address) {
                type = 1;
                if(type == 1){
                    Intent intent = new Intent(AddressActivity.this,AddressItemActivity.class);
                    intent.putExtra("addName",address.getAddress());
                    intent.putExtra("latitude",address.getLatitude());
                    intent.putExtra("longtitude",address.getLongitude());
                    startActivity(intent);
                }else if(type == 2){
                    LiveDataBus.get().with("TakeActivity").setStickyData(address);
                    finish();
                }

            }
        });
    }
}
