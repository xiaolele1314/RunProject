package com.example.run.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.example.run.java.LiveDataBus;
import com.example.run.room.Address;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.run.BaseActivity;
import com.example.run.R;
import com.example.run.databinding.ActivityTakeBinding;
import com.example.run.manager.BDMap;
import com.example.run.manager.BDmanager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class TakeActivity extends BaseActivity {

    private ActivityTakeBinding binding;

    private BDMap map;
    private BaiduMap baiduMap;

    private int weight;
    private double dist;
    private double money;
    private LatLng takeLL;
    private LatLng collectLL;

    private View bottomSheet;
    private BottomSheetBehavior<View> behavior;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        initView();
        initEvent();
    }

    @Override
    public void init() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_take);
        binding.include.setTakeActivity(this);
        binding.include.setStr("联系人");

        Intent intent = new Intent();
    }

    @Override
    public void initView() {
        baiduMap = binding.mapView.getMap();
        map = new BDMap(this);
        map.location(0,baiduMap);
        map.start();

        binding.setMap(map);

        bottomSheet = binding.nesTake;
        behavior = BottomSheetBehavior.from(bottomSheet);

        behavior.setHideable(false);
        behavior.setPeekHeight(550);

        binding.ivCloseTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        LiveDataBus.get().with("TakeActivity").observerSticky(this, new TakeActivityObserver(), true);

    }

    public void startActivity(int type){

    }

    public void showBottomDialog(){

    }

    public void commit(){

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private class TakeActivityObserver implements Observer<Address> {
        @Override
        public void onChanged(Address address) {
            if(address.getType() == 1){
                binding.include.setTakeAddress(address);
            }else{
                binding.include.setCollectAddress(address);
            }
        }
    }

}
