package com.example.run.bomb;

import java.util.Locale;

import cn.bmob.v3.BmobObject;

public class MyUser extends BmobObject {
    private String name;
    private String phone;
    private String photoUrl;
    private String payPwd;

    private byte[] portrait;
    private Double money;
    private Double upMoney;

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public Double getMoney() {
        return money;
    }

    public Double getUpMoney() {
        return upMoney;
    }

    public byte[] getPortrait() {
        return portrait;
    }

    public void setPortrait(byte[] portrait) {
        this.portrait = portrait;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public void setUpMoney(Double upMoney) {
        this.upMoney = upMoney;
    }

    public static class Buidler{
        private String name;
        private String phone;
        private String photoUrl;
        private String payPwd;

        private byte[] portrait;
        private Double money;
        private Double upMoney;

        public Buidler setName(String name){
            this.name = name;
            return this;
        }

        public Buidler setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Buidler setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public Buidler setPayPwd(String payPwd) {
            this.payPwd = payPwd;
            return this;
        }

        public Buidler setMoney(Double money) {
            this.money = money;
            return this;
        }

        public Buidler setUpMoney(Double upMoney) {
            this.upMoney = upMoney;
            return this;
        }

        public void setPortrait(byte[] portrait) {
            this.portrait = portrait;
        }

        public MyUser build(){
            MyUser myUser = new MyUser();

            myUser.setName(name);
            myUser.setPhone(phone);
            myUser.setPhotoUrl(photoUrl);
            myUser.setMoney(money);
            myUser.setPayPwd(payPwd);
            myUser.setUpMoney(upMoney);
            myUser.setPortrait(portrait);

            return myUser;
        }
    }
}
