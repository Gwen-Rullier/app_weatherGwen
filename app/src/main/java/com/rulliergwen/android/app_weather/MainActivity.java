package com.rulliergwen.android.app_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TP2 : récupérer et afficher dans le textView
        mTextViewCityName = (TextView) findViewById(R.id.text_view_city_name);
        mTextViewCityName.setText(R.string.city_name);
        Toast.makeText(this, mTextViewCityName.getText(), Toast.LENGTH_SHORT).show();

        //TP2 : l connexion
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Oui je suis connecté à internet
            setContentView(R.layout.activity_main);
        } else {
            // Non...
            
        }


    }
}
