package com.example.run.activity;

import android.content.Context;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.run.BaseActivity;
import com.example.run.R;
import com.example.run.adapter.SelectRecyclerAdapter;
import com.example.run.databinding.ActivitySelectBinding;
import com.example.run.java.LiveDataBus;
import com.example.run.manager.BDMap;
import com.example.run.manager.BDmanager;
import com.example.run.model.SelectInfo;

import java.util.HashMap;
import java.util.List;

public class SelectActivity extends BaseActivity {
    private ActivitySelectBinding binding;

    private BaiduMap baiduMap;
    private BDMap bdMap;

    private LatLng latLng;
    private BDmanager bDmanager;

    private SelectRecyclerAdapter adapter;

    private InputMethodManager inputMethodManager;

    private String address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        initData();
        initView();
        initEvent();


    }

    @Override
    public void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select);
        binding.setSelectActivity(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void initView() {
        baiduMap = binding.mapView.getMap();
        baiduMap.setMyLocationEnabled(true);

        adapter = new SelectRecyclerAdapter();
        binding.recSelect.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.recSelect.setAdapter(adapter);
    }

    @Override
    public void initData() {
        baiduMap = binding.mapView.getMap();
        bdMap = new BDMap(this);
        bdMap.location(0,baiduMap);
        bdMap.start();

    }

    @Override
    public void initEvent() {
        bDmanager = new BDmanager();
        binding.etContentSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    bDmanager.startPoi(s.toString());
                    binding.recSelect.setVisibility(View.VISIBLE);
                }else{
                    gone();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bDmanager.setOnResultListener(new BDmanager.OnResultListener() {
            @Override
            public void result(List<SelectInfo> list) {
                binding.recSelect.removeAllViews();
                adapter.setList(list);
            }
        });

        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                if(mapStatus != null){
                    latLng = mapStatus.target;
                    bDmanager.setLatLong(latLng);
                    binding.tvLatitudeSelect.setText(String.valueOf(latLng.latitude));
                    binding.tvLongitudeSelect.setText(String.valueOf(latLng.longitude));
                }
            }
        });
        bDmanager.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if(reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR){
                    Log.e("SelectActivity","没有地址");
                }else{
                    address = reverseGeoCodeResult.getAddress();
                    binding.tvAddressSelect.setText(address);
                }
            }
        });

        adapter.setOnItemClickListener(new SelectRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(SelectInfo info) {
                //点击地图会移动，所以selectLL与latlng值是一样的
                LatLng selectLL = new LatLng(info.getLatitude(),info.getLongtitude());
                baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(selectLL));
                baiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(18f));
                gone();
            }
        });
    }


    public void hideBehavior(){

    }

    public void gone(){
        inputMethodManager.hideSoftInputFromWindow(binding.etContentSelect.getWindowToken(),0);

        binding.recSelect.setVisibility(View.GONE);
        binding.ivCloseSelect.setVisibility(View.GONE);
    }

    public void addAddress(){

    }

    public void complete(){
        HashMap hashMap = new HashMap();
        hashMap.put("address",address);
        hashMap.put("latlng",latLng);
        LiveDataBus.get().with("EditActivity").setStickyData(hashMap);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
        bdMap.destroy();
    }
}
