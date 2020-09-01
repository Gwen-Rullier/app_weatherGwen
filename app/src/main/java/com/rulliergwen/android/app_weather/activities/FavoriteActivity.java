package com.rulliergwen.android.app_weather.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rulliergwen.android.app_weather.R;
import com.rulliergwen.android.app_weather.adapters.FavoriteAdapter;
import com.rulliergwen.android.app_weather.models.City;
import com.rulliergwen.android.app_weather.utiles.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FavoriteActivity extends AppCompatActivity {

    private ArrayList<City> mCities;
    private City mCityRemoved;
    private int mPositionCityRemoved;

    private RecyclerView mRecyclerView ;
    private Context mContext;
    private RecyclerView.Adapter mAdapter;
    private Handler mHandler ;
    private OkHttpClient mOkHttpClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        mContext = this;
        mHandler = new Handler();
        mOkHttpClient = new OkHttpClient();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_favorite, null);
                final EditText editTextCity = (EditText) v.findViewById(R.id.editText_dialog_city);
                builder.setView(v);
                /**
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                 **/

                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (editTextCity.getText().toString().length() > 0) {
                            updateWeatherDataCityName(editTextCity.getText().toString());
                        }
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                builder.create().show();
            }
        });
        Log.d("TAG", "-----------> FavoriteActivity: onCreate()");

        /** envoi dans le textView TP2
        TextView mTextViewMessage = (TextView) findViewById(R.id.textView_message);

        String message = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            message = extras.getString(Utils.mMessage);

        mTextViewMessage.setText("Message : " + message);
        **/

        // TP3 création de l'arrayList mCities
        mCities = new ArrayList<>();

        /**
        City city1 = new City("Montréal", "Légères pluies", "22°C", R.drawable.weather_rainy_grey);
        City city2 = new City("New York", "Ensoleillé", "22°C", R.drawable.weather_sunny_grey);
        City city3 = new City("Paris", "Nuageux", "24°C", R.drawable.weather_foggy_grey);
        City city4 = new City("Toulouse", "Pluies modérées", "20°C", R.drawable.weather_rainy_grey);

        mCities.add(city1);
        mCities.add(city2);
        mCities.add(city3);
        mCities.add(city4);
         **/

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new FavoriteAdapter(mContext, mCities);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                mPositionCityRemoved = ((FavoriteAdapter.ViewHolder) viewHolder).position;
                mCityRemoved = mCities.remove(mPositionCityRemoved);

                mAdapter.notifyDataSetChanged();

                Snackbar.make(findViewById(R.id.myCoordinatorLayout), mCityRemoved.mName + " est supprimé", Snackbar.LENGTH_LONG)
                        .setAction("Annuler", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mCities.add(mPositionCityRemoved, mCityRemoved);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();

            }
        });

        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void updateWeatherDataCityName(final String cityName) {

        String[] params = {cityName};
        String s = String.format(Utils.mAPI, params);
        Request request = new Request.Builder().url(s).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String stringJson = response.body().string();

                if (response.isSuccessful() && UtilAPI.isSuccessful(stringJson)) {
                    mHandler.post(new Runnable() {
                        public void run() {
                            renderFavoriteCityWeather(stringJson);
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(mContext, getString(R.string.place_not_found), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void renderFavoriteCityWeather(String stringJson) {

        try {
            City city = new City(stringJson);
            mCities.add(city);
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Toast.makeText(mContext, getString(R.string.place_not_found), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "-----------> FavoriteActivity: onDestroy()");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG", "-----------> FavoriteActivity: onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TAG", "-----------> FavoriteActivity: onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "-----------> FavoriteActivity: onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG", "-----------> FavoriteActivity: onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "-----------> FavoriteActivity: onPause()");
    }



}