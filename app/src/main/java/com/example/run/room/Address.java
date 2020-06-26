package com.example.run.room;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;

@Entity
public class Address {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int type;
    private String address;
    private String name;
    private String phone;
    private String sex;

    private double latitude;
    private double longitude;

    public Address() {
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSex() {
        return sex;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
