package com.example.run.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission {
    private String permisson = Manifest.permission.ACCESS_COARSE_LOCATION;

    private String[] permissionArr = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public void checkPermission(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int check = ContextCompat.checkSelfPermission(activity,permisson);
            if(check != PackageManager.PERMISSION_GRANTED){
                requestPermission(activity);
            }
        }

    }

    private void requestPermission(Activity activity){
        ActivityCompat.requestPermissions(activity,permissionArr,1000);
    }
}
