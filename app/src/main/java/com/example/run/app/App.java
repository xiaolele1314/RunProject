package com.example.run.app;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.xuexiang.xui.XUI;

import cn.bmob.v3.Bmob;

public class App extends Application {

    private final static String APP_KEY = "8f2d1735da01881474ce330ad4f06a61";
    @Override
    public void onCreate() {
        super.onCreate();

        //bmob
        Bmob.initialize(this,APP_KEY);

        ///XUI
        XUI.init(this); //初始化UI框架
        //XUI.debug(true);  //开启UI框架调试日志

        //百度SDK
        SDKInitializer.initialize(this);
        //设置坐标类型
        SDKInitializer.setCoordType(CoordType.BD09LL);

    }
}
