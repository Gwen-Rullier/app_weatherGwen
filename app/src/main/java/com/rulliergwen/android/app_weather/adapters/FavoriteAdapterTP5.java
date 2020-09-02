package com.rulliergwen.android.app_weather.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rulliergwen.android.app_weather.R;
import com.rulliergwen.android.app_weather.models.City1;

import java.util.ArrayList;

public class FavoriteAdapterTP5 extends RecyclerView.Adapter<FavoriteAdapterTP5.ViewHolder>{

    private ArrayList<City1> mArrayListCities;
    private Context mContext;

    public FavoriteAdapterTP5(Context context, ArrayList<City1> cities) {
        mContext = context;
        mArrayListCities = cities;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewCity;
        public ImageView mImageViewWeather;
        public TextView mTextViewTemperature;
        public TextView mTextViewDescription;

        public int position;

        public ViewHolder(View view) {
            super(view);

            view.setOnLongClickListener(mOnLongClickListener);
            view.setTag(this);

            mTextViewCity = (TextView) view.findViewById(R.id.textView_item_city);
            mImageViewWeather = (ImageView) view.findViewById(R.id.imageView_item_weather);
            mTextViewTemperature = (TextView) view.findViewById(R.id.textView_item_temperature);
            mTextViewDescription = (TextView) view.findViewById(R.id.textView_item_details);
        }
    }

    @Override
    public FavoriteAdapterTP5.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_city, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        City1 city = mArrayListCities.get(position);

        holder.mTextViewCity.setText(city.mName);
        holder.mImageViewWeather.setImageResource(city.mWeatherResIconGrey);
        holder.mTextViewTemperature.setText(city.mTemperature);
        holder.mTextViewDescription.setText(city.mDescription);

        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mArrayListCities.size();
    }


    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            ViewHolder holder = (ViewHolder) v.getTag();
            final int position = holder.position;

            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Supprimer " + holder.mTextViewCity.getText().toString() + " ?");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mArrayListCities.remove(position);
                    notifyDataSetChanged();
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mArrayListCities.size());

                }
            });

            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            builder.create().show();
            return false;
        }
    };
}
