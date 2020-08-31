package com.rulliergwen.android.app_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewCityName;
    private TextView mtextViewNoConnect;
    private LinearLayout mLinearLayoutCurrentCity;
    private Button mButton;
    private Context mContext;
    private FloatingActionButton mFloatingButtonFavorite ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TP2 : récupérer et afficher dans le textView
        mTextViewCityName = (TextView) findViewById(R.id.text_view_city_name);
        mTextViewCityName.setText(R.string.city_name);
        Toast.makeText(this, mTextViewCityName.getText(), Toast.LENGTH_SHORT).show();

        //TP2 : l connexion
        mLinearLayoutCurrentCity = (LinearLayout) findViewById(R.id.linear_layout_current_city);
        mtextViewNoConnect = (TextView) findViewById(R.id.text_view_noConnect);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Oui je suis connecté à internet
            mLinearLayoutCurrentCity.setVisibility(View.VISIBLE);
            mtextViewNoConnect.setVisibility(View.INVISIBLE);
        } else {
            // Non...
            mLinearLayoutCurrentCity.setVisibility(View.INVISIBLE);
            mtextViewNoConnect.setVisibility(View.VISIBLE);
        }
        mFloatingButtonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FavoriteActivity.class);
                startActivity(intent);
            }
        });


    }


}
