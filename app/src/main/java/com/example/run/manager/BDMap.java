package com.example.run.manager;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.example.run.activity.TakeActivity;

public class BDMap {
    private Context context;
    private LocationClient mLocationClient;
    private BaiduMap baiduMap;

    public BDMap(Context context){
        this.context = context;
    }

    public void location(int time, BaiduMap map){
        this.baiduMap = map;

        //获取地图和定位
        baiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(context);

        //设置定位选项
        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setCoorType("bd0911");
        locationOption.setScanSpan(time);
        locationOption.setIsNeedAddress(true);
        locationOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        //locationOption.setLocationNotify(true);
        locationOption.setOpenGps(true);

        //注册定位并打开
        mLocationClient.setLocOption(locationOption);
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                LatLng latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                MapStatusUpdate mapStatusUpdate1 = MapStatusUpdateFactory.zoomTo(18f);
                baiduMap.setMapStatus(mapStatusUpdate);
                baiduMap.animateMapStatus(mapStatusUpdate1);
                stop();
            }
        });
    }

    public void start(){
        mLocationClient.start();
    }

    public void stop(){
        mLocationClient.stop();
    }

    public void destroy(){
        mLocationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        baiduMap = null;
    }

    public String dist(double dist){
        String str = String.valueOf(dist);
        int of = str.indexOf(".");
        if(of != -1){
            String substring = str.substring(0,of + 2);
            return substring;
        }
        return str;
    }


}
