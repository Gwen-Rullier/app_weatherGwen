package com.rulliergwen.android.app_weather.models;


import com.rulliergwen.android.app_weather.utiles.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.internal.Util;

/** 1ère classe avant appel API
public class City {

    public String mName;
    public String mDescription;
    public String mTemperature;
    public int mWeatherIcon;

    public City(String mName, String mDescription, String mTemperature, int mWeatherIcon) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mTemperature = mTemperature;
        this.mWeatherIcon = mWeatherIcon;
    }


}
 **/

public class City {

    public int mIdCity;
    public String mName;
    public String mCountry;
    public String mTemperature;
    public String mDescription;
    public int mWeatherResIconWhite ;
    public double mLatitude;
    public double mLongitude;

    public String mStringJson;

    public City(String stringJson) throws JSONException {

        mStringJson = stringJson;

        JSONObject json = new JSONObject(stringJson);

        JSONObject details = json.getJSONArray("weather").getJSONObject(0);
        JSONObject main = json.getJSONObject("main");
        JSONObject coord = json.getJSONObject("coord");

        mIdCity = json.getInt("id");
        mName = json.getString("name");
        mCountry = json.getJSONObject("sys").getString("country");
        mTemperature = String.format("%.0f", main.getDouble("temp")) + " ℃";
        mWeatherResIconWhite = Utils.setWeatherIcon(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000, json.getJSONObject("sys").getLong("sunset") * 1000);
        mDescription = Utils.capitalize(details.getString("description"));
        mLatitude = coord.getDouble("lat");
        mLongitude = coord.getDouble("lon");

    }
}
