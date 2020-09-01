package com.rulliergwen.android.app_weather.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rulliergwen.android.app_weather.R;
import com.rulliergwen.android.app_weather.activities.FavoriteActivity;
import com.rulliergwen.android.app_weather.models.City;
import com.rulliergwen.android.app_weather.utiles.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewCityName;
    private TextView mtextViewNoConnect;
    private EditText mEditText ;
    private LinearLayout mLinearLayoutCurrentCity;
    private Button mButton;
    private FloatingActionButton mFloatingButtonFavorite ;
    private ProgressBar mProgressBarMain;
    private TextView mTextViewCity;
    private TextView mTextViewDetails;
    private TextView mTextViewCurrentTemperature;
    private ImageView mImageViewWeatherIcon;

    private Context mContext;

    private OkHttpClient mOkHttpClient;
    private Handler mHandler;

    private City mCurrentCity;

    private static final double LAT = 40.716709;
    private static final double LNG = -74.005698;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;


        mTextViewCityName = (TextView) findViewById(R.id.text_view_city_name);
        mTextViewCityName.setText(R.string.city_name);
        mLinearLayoutCurrentCity = (LinearLayout) findViewById(R.id.linear_layout_current_city);
        mtextViewNoConnect = (TextView) findViewById(R.id.text_view_noConnect);
        mProgressBarMain = (ProgressBar) findViewById(R.id.progress_bar_main);
        mTextViewCity = (TextView) findViewById(R.id.text_view_city_name);
        mTextViewDetails = (TextView) findViewById(R.id.text_view_city_desc);
        mTextViewCurrentTemperature = (TextView) findViewById(R.id.text_view_city_temp);
        mImageViewWeatherIcon = (ImageView) findViewById(R.id.image_view_city_weather);

        Toast.makeText(this, mTextViewCityName.getText(), Toast.LENGTH_SHORT).show();
        Log.d("TAG", "-----------> MainActivity: onCreate()");

        // TP2 connexion
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Oui je suis connecté à internet
            mLinearLayoutCurrentCity.setVisibility(View.VISIBLE);
            mtextViewNoConnect.setVisibility(View.INVISIBLE);
            updateWeatherDataCoordinates();
        } else {
            // Non...
            mLinearLayoutCurrentCity.setVisibility(View.INVISIBLE);
            mtextViewNoConnect.setVisibility(View.VISIBLE);
        }
        // TP2 récupération de l'edittext
        //mEditText = (EditText) findViewById(R.id.editText_message);

        mFloatingButtonFavorite = findViewById(R.id.floating_action_button_favorite);
        mFloatingButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FavoriteActivity.class);
                intent.putExtra(Utils.mMessage, mEditText.getText().toString());
                startActivity(intent);
            }
        });


        // TP4 : utilisation de OkHttpClient
        if (networkInfo != null && networkInfo.isConnected()) {
            // Oui je suis connecté à internet
            updateWeatherDataCoordinates();
        }


    }

    public void updateWeatherDataCoordinates() {

        String[] params = {String.valueOf(LAT), String.valueOf(LNG)};
        String sURL = String.format(Utils.mAPI, params);

        Request request = new Request.Builder().url(sURL).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String stringJson = response.body().string();

                if (response.isSuccessful() && Utils.isSuccessful(stringJson)) {

                    mHandler.post(new Runnable() {
                        public void run() {
                            renderCurrentWeather(stringJson);
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        public void run() {
                            updateViewError(R.string.place_not_found);
                        }
                    });
                }
            }
        });
    }

    private void renderCurrentWeather(String jsonString) {

        try {
            mCurrentCity = new City(jsonString);

            mTextViewCity.setText(mCurrentCity.mName.toUpperCase(Locale.US));
            mTextViewDetails.setText(mCurrentCity.mDescription);
            mTextViewCurrentTemperature.setText(mCurrentCity.mTemperature);
            mImageViewWeatherIcon.setImageResource(mCurrentCity.mWeatherResIconWhite);

            mLinearLayoutCurrentCity.setVisibility(View.VISIBLE);
            mFloatingButtonFavorite.setVisibility(View.VISIBLE);
            mProgressBarMain.setVisibility(View.GONE);

        } catch (JSONException e) {
            updateViewError(R.string.api_error);
        }
    }


    public void updateViewError(int resString) {

        mLinearLayoutCurrentCity.setVisibility(View.INVISIBLE);
        mProgressBarMain.setVisibility(View.INVISIBLE);
        mFloatingButtonFavorite.setVisibility(View.INVISIBLE);
        mtextViewNoConnect.setVisibility(View.VISIBLE);
        mtextViewNoConnect.setText(resString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "-----------> MainActivity: onDestroy()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG", "-----------> MainActivity: onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TAG", "-----------> MainActivity: onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "-----------> MainActivity: onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG", "-----------> MainActivity: onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "-----------> MainActivity: onPause()");
    }
}
