package com.example.ebeat;

public class crimed {
    String desc;
    String imageurl;
    double latitude;
    double longitude;

    public crimed(String desc, String imageurl,double latitude,double longitude) {
        this.desc = desc;
        this.imageurl = imageurl;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public crimed(){}
    public String getDesc() {
        return desc;
    }

    public String getImageurl() {
        return imageurl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
