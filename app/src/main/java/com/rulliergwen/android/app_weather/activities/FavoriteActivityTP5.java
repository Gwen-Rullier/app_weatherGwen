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
import com.rulliergwen.android.app_weather.adapters.FavoriteAdapterTP5;
import com.rulliergwen.android.app_weather.models.City1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class FavoriteActivityTP5 extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<City1> mCities;
    private Context mContext;

    private City1 mCityRemoved;
    private int mPositionCityRemoved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_tp5);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTP5);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layoutTP5);
        toolBarLayout.setTitle("Vos favoris");

        mContext = this;


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_add_favorite, null);
                final EditText editTextCity = (EditText) v.findViewById(R.id.editText_dialog_city);
                builder.setView(v);
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

        mCities = new ArrayList<>();

        City1 city1 = new City1("Montréal", "Légères pluies", "22°C", R.drawable.weather_rainy_grey);
        City1 city2 = new City1("New York", "Ensoleillé", "22°C", R.drawable.weather_sunny_grey);
        City1 city3 = new City1("Paris", "Nuageux", "24°C", R.drawable.weather_foggy_grey);
        City1 city4 = new City1("Toulouse", "Pluies modérées", "20°C", R.drawable.weather_rainy_grey);

        mCities.add(city1);
        mCities.add(city2);
        mCities.add(city3);
        mCities.add(city4);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_viewTP5);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FavoriteAdapterTP5(mContext, mCities);
        mRecyclerView.setAdapter(mAdapter);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                mPositionCityRemoved = ((FavoriteAdapterTP5.ViewHolder) viewHolder).position;
                mCityRemoved = mCities.remove(mPositionCityRemoved);

                mAdapter.notifyDataSetChanged();

                Snackbar.make(findViewById(R.id.myCoordinatorLayoutTP5), mCityRemoved.mName + " est supprimé", Snackbar.LENGTH_LONG)
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
        City1 city = new City1(cityName, "Ensoleillé", "28°C", R.drawable.weather_sunny_grey);
        mCities.add(city);
        mAdapter.notifyDataSetChanged();
    }

}