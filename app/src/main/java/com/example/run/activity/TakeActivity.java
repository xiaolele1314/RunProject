package com.example.run.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.run.Money;
import com.example.run.java.LiveDataBus;
import com.example.run.manager.UserManage;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xuexiang.xui.widget.picker.XSeekBar;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        baiduMap.setMyLocationEnabled(true);
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
            if(TextUtils.isEmpty(binding.include.tvTakeAddress.getText()))
            {
                XToast.warning(this,"先填写取件地址").show();
                return;
            }
            intent.setClass(this,AddressActivity.class);
            intent.putExtra("type",type);
            startActivity(intent);
        }else if(type == 3){
            intent.setClass(this,RemarksActivity.class);
            startActivityForResult(intent,1001);
        }
    }

    public void showBottomDialog(){
        if(TextUtils.isEmpty(binding.include.tvCollectAddress.getText())){
            XToast.warning(this,"填写地址信息").show();
            return;
        }

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.layout_bottom_dialog_take);


        TextView tvComplete = bottomSheetDialog.findViewById(R.id.tv_complete);
        XSeekBar seekBar = bottomSheetDialog.findViewById(R.id.seekBar);

        seekBar.setDefaultValue(5);
        seekBar.setOnSeekBarListener(new XSeekBar.OnSeekBarListener() {
            @Override
            public void onValueChanged(XSeekBar seekBar, int newValue) {
                weight = newValue;
            }
        });

        tvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.include.tvWeight.setText(weight + "KG");
                money = Double.valueOf(Money.money(dist, weight));
                binding.include.tvMoneyTake.setText(Money.money(dist,weight));
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    public void commit(){
        if (TextUtils.isEmpty(binding.include.tvTakeAddress.getText())||TextUtils.isEmpty(binding.include.tvCollectAddress.getText())){
            XToast.warning(this,"完善地址信息").show();
            return;
        }else if (TextUtils.isEmpty(binding.include.tvWeight.getText())){
            XToast.warning(this,"完善物品信息").show();
            return;
        }

        JSONObject json = new JSONObject();

        try {
            //json.put("userId",UserManager.get().getUser().getObjectId());
            json.put("userId", UserManage.getInstance().getUser().getObjectId());
            json.put("takeAddress",binding.include.tvTakeAddress.getText().toString());
            json.put("takeName",binding.include.tvTakeName.getText().toString());
            json.put("takePhone",binding.include.tvTakePhone.getText().toString());
            json.put("takeLatitude",takeLL.latitude);
            json.put("takeLongitude",takeLL.longitude);

            json.put("collectAddress",binding.include.tvCollectAddress.getText().toString());
            json.put("collectName",binding.include.tvCollectName.getText().toString());
            json.put("collectPhone",binding.include.tvCollectPhone.getText().toString());
            json.put("collectLatitude",collectLL.latitude);
            json.put("collectLongitude",collectLL.longitude);

            json.put("money",money);
            json.put("dist",dist);
            json.put("weight",weight);
            json.put("remarks",binding.include.tvInfoTake.getText().toString());
            json.put("runType",1);
            json.put("riderId","null");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LiveDataBus.get().with("PayActivity").setStickyData(json);
        intent.setClass(this,PayActivity.class);
        startActivity(intent);
    }

    private void overLayMap(){
        List<LatLng> latLngList = new ArrayList<>();
        latLngList.add(takeLL);
        latLngList.add(collectLL);

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.gps);
        OverlayOptions takeOptions = new MarkerOptions()
                .position(takeLL)
                .draggable(false)
                .animateType(MarkerOptions.MarkerAnimateType.drop)
                .icon(icon);

        OverlayOptions collectOptions = new MarkerOptions()
                .position(collectLL)
                .draggable(false)
                .animateType(MarkerOptions.MarkerAnimateType.drop)
                .icon(icon);

        OverlayOptions lineOptions = new PolylineOptions()
                .width(10)
                .color(ActivityCompat.getColor(this,R.color.main_blue))
                .points(latLngList);

        baiduMap.addOverlay(takeOptions);
        baiduMap.addOverlay(collectOptions);
        baiduMap.addOverlay(lineOptions);

        MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(16f);
        baiduMap.setMapStatus(update);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode == RESULT_OK){
            binding.include.tvInfoTake.setText(data.getStringExtra("content"));
        }
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
                takeLL = new LatLng(address.getLatitude(),address.getLongitude());
            }else{
                //属于对方信息
                binding.include.setCollectAddress(address);
                collectLL = new LatLng(address.getLatitude(),address.getLongitude());

                double distance = DistanceUtil.getDistance(takeLL, collectLL);
                dist = Double.valueOf(map.dist(distance));
                binding.include.tvDistTake.setText(dist + "米");

                //绘制地图
                overLayMap();

                if(behavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        }
    }

}
