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

    private Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        initView();
        initEvent();
    }

    /**
     * 绑定activity_take界面，为activity_take的include界面设置TakeActivity和Str数据
     * 初始化intent
     */
    @Override
    public void init() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_take);
        binding.include.setTakeActivity(this);
        binding.include.setStr("联系人");

        intent = new Intent();
    }

    /**
     * 展示地图信息，定位到本地
     * 隐藏底部选框，添加返回的点击事件
     */
    @Override
    public void initView() {
        baiduMap = binding.mapView.getMap();
        map = new BDMap(this);
        map.location(0,baiduMap);
        map.start();

        //初始化activity_take的map数据
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

    /**
     * 监听其他界面发过来的数据
     */
    @Override
    public void initEvent() {
        LiveDataBus.get().with("TakeActivity").observerSticky(this, new TakeActivityObserver(), true);

    }

    /**
     * 点击事件，实现界面跳转。直接在界面调用
     * @param type edit界面为1，address界面为2
     */
    public void startActivity(int type){
        if(type == 1){
            intent.setClass(this,EditActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
        }else if(type == 2){
            intent.setClass(this,AddressActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
        }
    }

    public void showBottomDialog(){

    }

    public void commit(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.mapView.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
        map.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    /**
     * 具体实现监听接口
     */
    private class TakeActivityObserver implements Observer<Address> {
        @Override
        public void onChanged(Address address) {

            if(address.getType() == 1){
                //属于用户自己信息
                binding.include.setTakeAddress(address);
            }else{
                //属于对方信息
                binding.include.setCollectAddress(address);
            }
        }
    }

}
