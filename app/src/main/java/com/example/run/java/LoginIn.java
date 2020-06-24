package com.example.run.java;

import com.example.run.bomb.BBManager;
import com.example.run.bomb.MyUser;
import com.example.run.manager.UserManage;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class LoginIn {

    private LoginInCallback mLoginInCallback;
    private String phone;

    /**
     * 检查验证码
     * @param phone 电话
     * @param code 验证码
     * @param callback 结果回调
     */
    public void checkPhoneCode(String phone, final String code, LoginInCallback callback){
        this.mLoginInCallback = callback;
        this.phone = phone;
        BBManager.getInstance().checkPhoneCode(phone, code, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    findUserByPhone();
                }else{
                    mLoginInCallback.error(e.getMessage(),e.getErrorCode());
                }
            }
        });
    }

    /**
     * 通过电话查找用户
     */
    private void findUserByPhone(){
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.addWhereEqualTo("phone",phone);
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if(e == null){
                    if(list.size() > 0){
                        //有此用户保存到内存
                        MyUser myUser = list.get(0);
                        UserManage.getInstance().saveUser(myUser);
                        mLoginInCallback.done(UserManage.getInstance().getUser().getObjectId());
                    }else{
                        //没有则创建用户
                        createUser();
                    }
                }else{
                    mLoginInCallback.error(e.getMessage(),e.getErrorCode());
                }
            }
        });
    }

    /**
     * 在bmob创建用户
     */
    private void createUser(){
        final MyUser user = new MyUser.Buidler()
                .setName("小乐乐")
                .setPhone(phone)
                .setPhotoUrl(null)
                .setPayPwd("0000")
                .setMoney(0.0)
                .setUpMoney(0.0)
                .build();

        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    UserManage.getInstance().saveUser(user);
                    mLoginInCallback.done(UserManage.getInstance().getUser().getObjectId());
                }else{
                    mLoginInCallback.error(e.getMessage(),e.getErrorCode());
                }
            }
        });
    }

    /**
     * 回调接口
     */
    public interface LoginInCallback{
        void done(String id);
        void error(String msg,int errorCode);
    }
}
