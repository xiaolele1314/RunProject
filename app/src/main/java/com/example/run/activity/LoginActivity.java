package com.example.run.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.run.BaseActivity;
import com.example.run.R;
import com.example.run.bomb.BBManager;
import com.example.run.manager.UserManage;
import com.xuexiang.xui.widget.toast.XToast;

import javax.xml.transform.Result;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivClose;
    private ImageView ivSend;
    private EditText etPhone;

    private String phoneNum;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        initView();

    }

    @Override
    public void init() {
        intent = new Intent();
    }

    @Override
    public void initView() {
        ivClose = (ImageView)findViewById(R.id.iv_close_login);
        ivSend = (ImageView)findViewById(R.id.iv_send_login);
        etPhone = (EditText)findViewById(R.id.et_phone_login);

        ivClose.setOnClickListener(this);
        ivSend.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close_login:
                finish();
                break;
            case R.id.iv_send_login:
                sendPhoneNum();
                break;
            default:
                break;
        }
    }

    /**
     * 获取输入电话，发送验证码
     */
    private void sendPhoneNum(){
        phoneNum = etPhone.getText().toString();
        if(phoneNum.length() != 11){
            XToast.warning(this,"手机号错误").show();
            return;
        }

        //监听发送验证码
        BBManager.getInstance().sendCode(phoneNum, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){
                    //发送成功，跳转输入验证码界面，将电话号码作为参数传入
                    intent.setClass(LoginActivity.this,CheckActivity.class);
                    intent.putExtra("phoneNum",phoneNum);
                    startActivityForResult(intent,1);
                }else{
                    XToast.warning(LoginActivity.this,"失败  " + e.getErrorCode()).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){

            //输入验证码成功，跳转home页面
            intent.setClass(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();

            Log.e("LoginIActivity", UserManage.getInstance().getUser().getObjectId());
        }
    }
}
