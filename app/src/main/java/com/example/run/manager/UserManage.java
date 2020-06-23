package com.example.run.manager;

import com.example.run.bomb.MyUser;

public class UserManage {
    private static volatile UserManage mUserManage;
    private UserManage(){};
    public static UserManage getInstance(){
        if(mUserManage == null){
            synchronized (UserManage.class){
                if(mUserManage == null){
                    mUserManage = new UserManage();
                }
            }
        }
        return mUserManage;
    }




    private MyUser mMyUser;

    public void saveUser(MyUser user){
        this.mMyUser = user;
    }

    public MyUser getUser(){
        return mMyUser;
    }

    public void removeUser(){
        mMyUser = null;
    }
}
