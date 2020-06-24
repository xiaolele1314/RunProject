package com.example.run.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.run.BaseActivity;
import com.example.run.R;
import com.example.run.bomb.BBManager;
import com.example.run.bomb.MyUser;
import com.example.run.manager.UserManage;
import com.xuexiang.xui.widget.toast.XToast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class StartActivity extends BaseActivity {

    private Intent intent;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1000){
                intent.setClass(StartActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }else{
                intent.setClass(StartActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srat);

        init();

        //获取本地用户id,来判断bmob是否有数据
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");

        if(!TextUtils.isEmpty(id)){
            //存在
            findUser(id);
        }else{
            //不存在
            handler.sendEmptyMessageDelayed(1001,3000);
        }


    }

    /**
     * 根据id从bmob获取用户数据
     * @param id id
     */
    private void findUser(String id){

        BBManager.getInstance().findUser(id, new QueryListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if(e == null){
                    //保存用户数据到内存
                    UserManage.getInstance().saveUser(user);
                    handler.sendEmptyMessageDelayed(1000,3000);
                }else{
                    XToast.warning(StartActivity.this,"失败  " + e.getErrorCode()).show();
                }
            }
        });
    }

    @Override
    public void init() {
        intent = new Intent();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
