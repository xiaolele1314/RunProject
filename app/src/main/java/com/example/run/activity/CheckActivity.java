package com.example.run.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.run.BaseActivity;
import com.example.run.R;
import com.example.run.bomb.BBManager;
import com.example.run.java.LoginIn;
import com.google.android.material.button.MaterialButton;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog;
import com.xuexiang.xui.widget.edittext.verify.VerifyCodeEditText;
import com.xuexiang.xui.widget.toast.XToast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class CheckActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivClose;
    private ImageView ivUp;
    private TextView tvPhone;
    private VerifyCodeEditText etCode;
    private MaterialButton btSend;

    private String phoneNum;

    private int count = 60;

    private MiniLoadingDialog miniLoadingDialog;

    private Boolean isSend = true;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(isSend){
                if(msg.what == 1){
                    //一分钟内验证验证码未成功
                    btSend.setEnabled(true);
                    btSend.setText("重新发送");
                    btSend.setBackgroundColor(ActivityCompat.getColor(CheckActivity.this,R.color.main_blue));
                    isSend = false;
                    count = 60;
                }else{
                    //设置60s倒计时
                    count--;
                    btSend.setText(count + "s");
                    handler.sendEmptyMessageDelayed(count,1000);
                }
            }


            //验证成功
            if(msg.what == 1001){
                //验证码验证成功
                miniLoadingDialog.dismiss();

                //Intent intent = new Intent(CheckActivity.this,HomeActivity.class);
                setResult(RESULT_OK);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        init();
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //开始60s倒计时
        handler.sendEmptyMessage(count);
    }

    @Override
    public void init() {

        //获取加载view
        miniLoadingDialog = WidgetUtils.getMiniLoadingDialog(this);
        miniLoadingDialog.setDialogSize(200,200);

    }

    @Override
    public void initView() {
        ivClose = (ImageView)findViewById(R.id.iv_close_check);
        ivUp = (ImageView)findViewById(R.id.iv_up_check);
        tvPhone = (TextView)findViewById(R.id.tv_phone_check);
        etCode = (VerifyCodeEditText)findViewById(R.id.et_code_check);
        btSend = (MaterialButton)findViewById(R.id.btn_send_check);

        ivClose.setOnClickListener(this);
        ivUp.setOnClickListener(this);
        btSend.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //获取穿过来的电话
        Intent intent = getIntent();
        phoneNum = intent.getStringExtra("phoneNum");
        tvPhone.setText(phoneNum);
    }

    @Override
    public void initEvent() {

        //监听验证码的输入
        etCode.setOnInputListener(new VerifyCodeEditText.OnInputListener() {

            //输入完成
            @Override
            public void onComplete(String input) {
                checkPhoneCode(input);
            }

            //修改输入
            @Override
            public void onChange(String input) {

            }

            //清除输入
            @Override
            public void onClear() {

            }
        });
    }

    /**
     * 根据输入的验证码检查验证码是否正确
     * @param code c验证码
     */
    private void checkPhoneCode(String code){
        miniLoadingDialog.show();

        //检查验证码是否正确
        LoginIn loginIn = new LoginIn();
        loginIn.checkPhoneCode(phoneNum, code, new LoginIn.LoginInCallback() {
            @Override
            public void done(String id) {
                //正确
                putUserId(id);
            }

            @Override
            public void error(String msg, int errorCode) {
                miniLoadingDialog.dismiss();

                XToast.warning(CheckActivity.this,msg + errorCode);
                Log.e("checkactivity","错误信息：" + msg + "错误码：" + errorCode);
            }
        });
    }


    /**
     *验证码正确，返回bmob的一条数据的id，并保存在本地
     * @param id  id
     */
    private void putUserId(String id){
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("id",id);
        edit.commit();

        //发送验证成功消息
        handler.sendEmptyMessageDelayed(1001,500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close_check:
                finish();
                break;
            case R.id.iv_up_check:
                finish();
                break;
            case R.id.btn_send_check:
                sendCode();
                break;
        }
    }

    private void sendCode() {
        isSend = true;
        BBManager.getInstance().sendCode(phoneNum, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){
                    XToast.success(CheckActivity.this,"发送成功").show();
                }else{
                    XToast.warning(CheckActivity.this,"发送失败  " + e.getErrorCode()).show();
                }
            }
        });
        btSend.setEnabled(false);
        btSend.setBackgroundColor(ActivityCompat.getColor(CheckActivity.this,R.color.main_bac));
        count=60;
        handler.sendEmptyMessage(count);
    }
}
