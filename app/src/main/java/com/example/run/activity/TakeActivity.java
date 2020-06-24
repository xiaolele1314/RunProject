package com.example.run.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

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

public class TakeActivity extends BaseActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;

    private Boolean isFirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take);
        initView();
        initEvent();
    }

    @Override
    public void init() {

    }

    @Override
    public void initView() {
        mMapView = findViewById(R.id.mapView);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        //获取地图和定位
        mBaiduMap =  mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getApplicationContext());

        //设置定位选项
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setCoorType("bd0911");
        locationOption.setScanSpan(0);
        locationOption.setIsNeedAddress(true);
        locationOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        //locationOption.setLocationNotify(true);
        locationOption.setOpenGps(true);

        //注册定位并打开
        mLocationClient.setLocOption(locationOption);
        mLocationClient.registerLocationListener(new MyLocationListener());
        mLocationClient.start();


    }

    /**
     * 定位监听
     */
    private class MyLocationListener extends BDAbstractLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(bdLocation == null){
                return;
            }
            Log.e("TakeActivity","当前地址：" + bdLocation.getAddrStr());

            //定位成功
            MyLocationData myLocationData = new MyLocationData.Builder()
                    .accuracy(0)
                    .direction(100f)
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);

            //第一次定位将焦点移动到本地
            if(isFirst){
                LatLng latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                MapStatusUpdate mapStatusUpdate1 = MapStatusUpdateFactory.zoomTo(18f);
                mBaiduMap.setMapStatus(mapStatusUpdate);
                mBaiduMap.animateMapStatus(mapStatusUpdate1);
                isFirst = false;
            }
            mLocationClient.stop();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }


}
