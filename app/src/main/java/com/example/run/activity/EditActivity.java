package com.example.run.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.baidu.mapapi.model.LatLng;
import com.example.run.BaseActivity;
import com.example.run.R;
import com.example.run.databinding.ActivityEditBinding;
import com.example.run.java.LiveDataBus;
import com.example.run.room.Address;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.HashMap;

public class EditActivity extends BaseActivity {

    private ActivityEditBinding binding;

    private Intent intent;

    private String sex = "女士";
    private LatLng latLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        initView();
        initData();
        initEvent();
    }

    @Override
    public void init() {
        LiveDataBus.get().with("EditActivity").observerSticky(this, new EditActivityObserver(),true);
    }

    @Override
    public void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        binding.setEditActivity(this);

        intent = new Intent();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        binding.boxManEdit.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if(isChecked){
                    sex = "先生";
                    binding.boxMissEdit.setChecked(false);
                }
            }
        });

        binding.boxMissEdit.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if(isChecked){
                    sex = "女士";
                    binding.boxManEdit.setChecked(false);
                }
            }
        });
    }

    public void startActivity(){
        intent.setClass(this,SelectActivity.class);
        startActivity(intent);
    }

    public void complete(){
        if (TextUtils.isEmpty(binding.etSelectEdit.getText())){
            XToast.warning(this,"选择地址").show();
            return;
        }else if (TextUtils.isEmpty(binding.etAddressEdit.getText())){
            XToast.warning(this,"地址是空").show();
            return;
        }else if (TextUtils.isEmpty(binding.etNameEdit.getText())){
            XToast.warning(this,"联系人是空").show();
            return;
        }else if (TextUtils.isEmpty(binding.etPhoneEdit.getText())){
            XToast.warning(this,"手机号是空").show();
            return;
        }else if (!binding.boxManEdit.isChecked() && !binding.boxMissEdit.isChecked()){
            XToast.warning(this,"选择性别").show();
            return;
        }

        Address address = new Address();

        address.setType(1);
        address.setAddress(binding.etAddressEdit.getText().toString());
        address.setName(binding.etNameEdit.getText().toString());
        address.setPhone(binding.etPhoneEdit.getText().toString());
        address.setSex(sex);
        address.setLatitude(latLng.latitude);
        address.setLongitude(latLng.longitude);

        LiveDataBus.get().with("TakeActivity").setStickyData(address);
        finish();

    }

    private class EditActivityObserver implements Observer<HashMap>{

        @Override
        public void onChanged(HashMap hashMap) {
            String address = (String) hashMap.get("address");
            latLng = (LatLng) hashMap.get("latlng");
            binding.etSelectEdit.setText(address);
            Log.e("EditActivity","纬度： " + latLng.latitude + "\n经度： " + latLng.longitude);
        }
    }
}
