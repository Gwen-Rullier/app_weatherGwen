package com.rulliergwen.android.app_weather.models;

public class City1 {

    public String mName;
    public int mWeatherResIconGrey;
    public String mTemperature;
    public String mDescription;

    public City1(String name, String desc, String temp, int resWeatherIcon) {
        mName = name;
        mDescription = desc;
        mTemperature = temp;
        mWeatherResIconGrey = resWeatherIcon;
    }
}
