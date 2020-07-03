package com.example.run.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;

import com.example.run.BaseActivity;
import com.example.run.R;
import com.example.run.java.LiveDataBus;
import com.example.run.java.Pay;
import com.example.run.manager.UserManage;
import com.example.run.model.Result;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.verify.VerifyCodeEditText;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import io.reactivex.Observable;

public class PayActivity extends BaseActivity {

    private TextView tvMoney;
    private ImageView ivClose;
    private MaterialButton btnPay;

    private JSONObject jsonObject;

    private MaterialDialog.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        init();
        initView();
        initEvent();
    }

    @Override
    public void init() {
        builder = new MaterialDialog.Builder(this);
    }

    @Override
    public void initView() {
        tvMoney = findViewById(R.id.tv_money_pay);
        ivClose = findViewById(R.id.iv_close_pay);
        btnPay = findViewById(R.id.btn_pay);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayDialog();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showPayDialog(){
        new MaterialDialog.Builder(this)
                .customView(R.layout.layout_pay_dialog, true)
                .title("4位交易密码")
                .positiveText("确定")
                .negativeText("取消")
                .positiveColor(ActivityCompat.getColor(this, R.color.main_blue))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        showDialog();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View view = dialog.getView();
                        VerifyCodeEditText text = view.findViewById(R.id.verify_pay);
                        String payPwd = text.getInputValue();

                        if (payPwd.equals(UserManage.getInstance().getUser().getPayPwd())) {
                             Pay pay = new Pay();

                             //内部类持有外部类的对象，所以不能定义static方法
                             pay.commit(jsonObject, new Pay.PayCallBack() {
                                 @Override
                                 public void onDone(cn.bmob.v3.http.bean.Result result) {
                                     if (result.getCode() == 200){
                                         XToast.warning(PayActivity.this,"支付成功").show();
                                         finish();
                                     }else if (result.getCode() == 201){
                                         XToast.warning(PayActivity.this,"余额不足").show();
                                     }
                                 }

                                 @Override
                                 public void onError() {
                                     XToast.warning(PayActivity.this, "交易失败").show();
                                 }
                             });
                         } else {
                            XToast.warning(PayActivity.this, "密码错误").show();
                         }

                    }
                }).show();
    }



    private void showDialog(){
        builder.iconRes(R.drawable.ic_help_24dp)
                .title("订单交易")
                .content("是否取消订单")
                .positiveText("是")
                .negativeText("不")
                .show();

        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                finish();
            }
        });
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                showPayDialog();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        LiveDataBus.get().with("PayActivity").observerSticky(this, new PayActivityObserver(), true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showPayDialog();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        showDialog();
    }

    private class PayActivityObserver implements Observer<JSONObject> {

        @Override
        public void onChanged(JSONObject json) {
            jsonObject = json;
            try {
                double money = (double) json.get("money");
                tvMoney.setText(String.valueOf(money));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
