package com.example.run.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import com.example.run.room.Address;
import com.example.run.room.AddressManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.xuexiang.xui.widget.toast.XToast;

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

    private View bottomSheet;
    private BottomSheetBehavior<View> behavior;

    private int type;

    private String sex;

    private Boolean etChange = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        initData();
        initView();
        initEvent();


    }

    /**
     * 绑定界面，初始化界面数据
     * 获取输入键盘
     * 获取传来的type
     */
    @Override
    public void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select);
        binding.setSelectActivity(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        Intent intent = getIntent();
        type = intent.getIntExtra("type",0);

    }

    /**
     * 初始化baidumao
     * 为recSelect设置适配器
     * 隐藏底部选框
     */
    @Override
    public void initView() {
        baiduMap = binding.mapView.getMap();
        baiduMap.setMyLocationEnabled(true);

        adapter = new SelectRecyclerAdapter();
        binding.recSelect.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.recSelect.setAdapter(adapter);

        binding.include.setSelectActivity(this);
        bottomSheet = binding.nesSelect;
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    /**
     * 展示地图并定位到本地
     */
    @Override
    public void initData() {
        baiduMap = binding.mapView.getMap();
        bdMap = new BDMap(this);
        bdMap.location(0,baiduMap);
        bdMap.start();

    }

    @Override
    public void initEvent() {

        //监听输入框的是否输入
        bDmanager = new BDmanager();
        binding.etContentSelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    //获取和输入有关的地址
                    bDmanager.startPoi(s.toString());

                    //显示展示poi获取的地址的recSelect
                    binding.recSelect.setVisibility(View.VISIBLE);

                    //显示清除按钮
                    binding.ivCloseSelect.setVisibility(View.VISIBLE);
                }else{
                    //不显示
                    inputMethodManager.hideSoftInputFromWindow(binding.etContentSelect.getWindowToken(),0);

                    binding.recSelect.setVisibility(View.GONE);
                    binding.ivCloseSelect.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //监听poi结果
        bDmanager.setOnResultListener(new BDmanager.OnResultListener() {
            @Override
            public void result(List<SelectInfo> list) {

                //recSelect每次显示先清除上次搜索内容
                binding.recSelect.removeAllViews();

                //adpter设置数据，setList内部实现recyclerView的动态刷新
                adapter.setList(list);
            }
        });

        //监听地图的改变事件
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

                    //获取移动地图目的的经纬度
                    latLng = mapStatus.target;

                    //移动地图
                    bDmanager.setLatLong(latLng);

                    //显示经纬度信息
                    binding.tvLatitudeSelect.setText(String.valueOf(latLng.latitude));
                    binding.tvLongitudeSelect.setText(String.valueOf(latLng.longitude));
                }
            }
        });

        //监听地图移动结果
        bDmanager.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if(reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR){
                    Log.e("SelectActivity","没有地址");
                }else{
                    //获取移动后的地址并显示
                    address = reverseGeoCodeResult.getAddress();
                    binding.tvAddressSelect.setText(address);
                }
            }
        });

        //为recSelect设置item点击事件
        adapter.setOnItemClickListener(new SelectRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(SelectInfo info) {
                //点击item会移动，所以selectLL与latlng值是一样的
                LatLng selectLL = new LatLng(info.getLatitude(),info.getLongtitude());
                baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(selectLL));
                baiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(18f));
                binding.etContentSelect.setText("");
                gone();
            }
        });

        //选择先生
        binding.include.boxManBottomSelect.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if(isChecked){
                    sex = "先生";
                    binding.include.boxMissBottomSelect.setChecked(false);
                }
            }
        });

        //选择女士
        binding.include.boxMissBottomSelect.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if(isChecked){
                    sex = "女士";
                    binding.include.boxManBottomSelect.setChecked(false);
                }
            }
        });

    }

    /**
     * 点击tv_up_bottom_select隐藏layout_bottom_select，即点击修改地址返回重新选择地址
     */
    public void hideBehavior(){
        if(behavior.getState() != BottomSheetBehavior.STATE_HIDDEN){
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    /**
     * 隐藏输入键盘、recSelect、清除❌号
     */
    public void gone(){
        inputMethodManager.hideSoftInputFromWindow(binding.etContentSelect.getWindowToken(),0);

        binding.recSelect.setVisibility(View.GONE);
        binding.ivCloseSelect.setVisibility(View.GONE);
        binding.etContentSelect.setText("");

    }

    /**
     * 点击btn_ok_bottom_select保存数据到room本地数据库
     */
    public void addAddress(){
        if (TextUtils.isEmpty(binding.include.etAddressBottomSelect.getText())){
            XToast.warning(this,"地址是空").show();
            return;
        }else if (TextUtils.isEmpty(binding.include.etNameBottomSelect.getText())){
            XToast.warning(this,"姓名是空").show();
            return;
        }else if (TextUtils.isEmpty(binding.include.etPhoneBottomSelect.getText())){
            XToast.warning(this,"手机号是空").show();
            return;
        }else if (!binding.include.boxManBottomSelect.isChecked()&&!binding.include.boxMissBottomSelect.isChecked()){
            XToast.warning(this,"选择性别").show();
            return;
        }

        Address address = new Address();
        address.setType(type);
        address.setAddress(binding.include.etAddressBottomSelect.getText().toString());
        address.setName(binding.include.etNameBottomSelect.getText().toString());
        address.setPhone(binding.include.etPhoneBottomSelect.getText().toString());
        address.setSex(binding.include.boxManBottomSelect.isChecked()?"先生":"女士");
        address.setLatitude(latLng.latitude);
        address.setLongitude(latLng.longitude);

        AddressManager addressManager = new AddressManager(this);
        addressManager.addAddress(address);

        finish();
    }

    /**
     * 选择好地址点击“选择这里”按钮根据type进行操作
     */
    public void complete(){
        if(type == 1){
            HashMap hashMap = new HashMap();
            hashMap.put("address",address);
            hashMap.put("latlng",latLng);

            //向EditActivity发送经纬度信息和address数据
            LiveDataBus.get().with("EditActivity").setStickyData(hashMap);
            finish();
        }else{
            //展开底部选框，默认是女士
            if(behavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                binding.include.tvAddressBottomSelect.setText(address);
                binding.include.boxMissBottomSelect.setChecked(true);
            }
        }


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
