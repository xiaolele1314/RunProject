package com.example.run.viewmodel;

import android.os.UserManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.run.bomb.BBManager;
import com.example.run.manager.UserManage;
import com.example.run.model.Take;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class OrderViewModel extends ViewModel {

    private MutableLiveData<List<Take>> takeOrderLive = new MutableLiveData<>();

    public MutableLiveData<List<Take>> getTakeOrderLive() {
        findTakeOrder();
        return takeOrderLive;
    }

    public void findTakeOrder() {
        //BBManager.get().findTakeOrder(UserManager.get().getUser().getObjectId());
        BBManager.getInstance().findTakeOrder(UserManage.getInstance().getUser().getObjectId(), new FindListener<Take>() {
            @Override
            public void done(List<Take> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        takeOrderLive.postValue(object);
                    }

                } else {
                    Log.e("order", "错误  " + e.getErrorCode());
                }
            }
        });
    }
}
