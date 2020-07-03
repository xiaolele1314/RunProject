package com.example.run.java;

import android.content.Context;
import android.util.Log;

import com.example.run.activity.PayActivity;
import com.google.gson.Gson;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.http.bean.Result;
import cn.bmob.v3.listener.CloudCodeListener;

public class Pay {

    private PayCallBack payCallBack;

    public  void commit(JSONObject jsonObject, PayCallBack payCallBack){
        /**AsyncCustomEndpoints ace =  new AsyncCustomEndpoints();

         ace.callEndpoint("demo",json,new CloudCodeListener() {
        @Override public void done(Object object, BmobException e) {
        if (e == null) {
        String result = object.toString();
        Log.e("take",result);
        } else {
        Log.e("take",object.toString());
        }
        }
        });**/

        AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
        ace.callEndpoint("request", jsonObject, new CloudCodeListener() {
            @Override
            public void done(Object object, BmobException e) {
                if (e == null) {
                    Log.e("pay", object.toString());
                    Gson gson = new Gson();
                    Result result = gson.fromJson(object.toString(), Result.class);
                    payCallBack.onDone(result);
                    /**Result result = gson.fromJson(object.toString(), Result.class);
                    if (result.getCode() == 200){
                        XToast.warning(conText,"支付成功").show();

                    }else if (result.getCode() == 201){
                        XToast.warning(conText,"余额不足").show();
                    }**/
                } else {
                    Log.e("pay", "错误代码 ：" + e.getErrorCode() + e.toString());
                    //XToast.warning(conText, "交易失败").show();
                    payCallBack.onError();
                }
            }
        });
    }

    public interface PayCallBack{
        void onDone(Result result);
        void onError();
    }
}
