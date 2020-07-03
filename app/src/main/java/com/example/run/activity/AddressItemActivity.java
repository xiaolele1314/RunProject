package com.example.run.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.example.run.BaseActivity;
import com.example.run.R;
import com.example.run.manager.BDMap;

public class AddressItemActivity extends BaseActivity {

    private ImageView ivClose;
    private TextView tvAddress;
    private TextView tvLatitude;
    private TextView tvLongtitude;

    private Intent intent;

    private BaiduMap baiduMap;
    private BDMap bdMap;
    private MapView mapView;

    private String address;
    private double latitude;
    private double longtitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_address_item);

        init();
        initView();
        initData();
        initEvent();

    }

    @Override
    public void init() {
        intent = getIntent();
        address = intent.getStringExtra("addName");
        latitude = intent.getDoubleExtra("latitude",0);
        longtitude = intent.getDoubleExtra("longtitude",0);
    }

    @Override
    public void initView() {
        ivClose = findViewById(R.id.iv_close_add_item);
        tvAddress = findViewById(R.id.tv_address_add_item);
        tvLatitude = findViewById(R.id.tv_latitude_add_item);
        tvLongtitude = findViewById(R.id.tv_longitude_add_item);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvAddress.setText(address);
        tvLatitude.setText(String.valueOf(latitude));
        tvLongtitude.setText(String.valueOf(longtitude));

        mapView = findViewById(R.id.mapView_add_item);
        baiduMap = mapView.getMap();
        bdMap = new BDMap(this);
        bdMap.setNewLoc(latitude,longtitude,baiduMap,this);
        bdMap.start();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        bdMap.destroy();
    }
}
