package com.example.run;

public class Money {

    public static String money(double dist, int weight){
        double money = dist * 0.03 + weight * 0.5;

        String s = String.valueOf(money);
        int of = s.indexOf(".");
        if(of != -1){
            s = s.substring(0, of + 2);
            return s;
        }

        return s;
    }
}
