package com.rulliergwen.android.app_weather.utiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.rulliergwen.android.app_weather.R;
import com.rulliergwen.android.app_weather.models.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;




public class Utils {

    public static final String mMessage = "mon Message";
    public static final String mAPI_COORDONNEES = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&lang=fr&appid=07287f6892a438d2b0e7c28355879449";
    public static final String mAPI_CITY_NAME = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&lang=fr&appid=07287f6892a438d2b0e7c28355879449";
    //public static final String PREFS_NAME ="";

    // Sauvegarde la liste des favoris (shared pref TP6)
    public static void saveFavouriteCities(Context context, ArrayList<City> cities) {
        JSONArray jsonArrayCities = new JSONArray();
        for (int i = 0; i < cities.size(); i++) {
            jsonArrayCities.put(cities.get(i).mStringJson);
            Log.d("TAG", "-----------> Utils saveFavourite() "+ cities.get(i).mStringJson);
        }
        SharedPreferences preferences = context.getSharedPreferences("Pref_FileName",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Pref_FavoriteCities", jsonArrayCities.toString());
        editor.apply();
    }
    /// récupération de l'arrayList des villes favorites
    public static ArrayList<City> initFavoriteCities(Context context) {
        ArrayList<City> cities = new ArrayList<>();

        SharedPreferences preferences = context.getSharedPreferences("Pref_FileName",
                Context.MODE_PRIVATE);
        try {
            JSONArray jsonArray = new JSONArray(preferences.getString("Pref_FavoriteCities",
                    ""));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCity = new JSONObject(jsonArray.getString(i));
                City city = new City(jsonObjectCity.toString());
                Log.d("TAG", "-----------> Utils initFavourite() "+city);
                        cities.add(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cities;
    }



    // Met en lettres capitales
    public static String capitalize(String s) {
        if (s == null) return null;
        if (s.length() == 1) {
            return s.toUpperCase();
        }
        if (s.length() > 1) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        return "";
    }

    public static boolean isSuccessful(String stringJson) {
        try {
            JSONObject json = new JSONObject(stringJson);

            int cod = json.getInt("cod");
            if (cod != 200)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // Méthode qui initialise l'icône blanche présente dans la MainActivity
    public static int setWeatherIcon(int actualId, long sunrise, long sunset) {

        int id = actualId / 100;
        int icon = R.drawable.weather_sunny_white;

        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = R.drawable.weather_sunny_white;
            } else {
                icon = R.drawable.weather_clear_night_white;
            }
        } else {
            switch (id) {
                case 2:
                    icon = R.drawable.weather_thunder_white;
                    break;
                case 3:
                    icon = R.drawable.weather_drizzle_white;
                    break;
                case 7:
                    icon = R.drawable.weather_foggy_white;
                    break;
                case 8:
                    icon = R.drawable.weather_cloudy_white;
                    break;
                case 6:
                    icon = R.drawable.weather_snowy_white;
                    break;
                case 5:
                    icon = R.drawable.weather_rainy_white;
                    break;
            }
        }
        return icon;
    }
}
