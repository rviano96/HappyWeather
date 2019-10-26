package ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.WeatherDetailsHolder;
import ar.iua.edu.viano.happyWeather.Model.WeatherDetails;

public class WeatherDetailsListAdapter extends RecyclerView.Adapter<WeatherDetailsHolder>{
    private List<WeatherDetails> weatherListDetails;

    public WeatherDetailsListAdapter(List<WeatherDetails> weatherListDetails) {
        this.weatherListDetails = weatherListDetails;
    }

    @NonNull
    @Override
    public WeatherDetailsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_details_view, viewGroup, false);
        return new WeatherDetailsHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDetailsHolder weatherDetailsHolder, int i) {
        WeatherDetails weatherDetails = weatherListDetails.get(i);
        /*weatherDetailsHolder.getdetail1().setText(weatherDetails.getDetail1());
        weatherDetailsHolder.getdetail2().setText(weatherDetails.getDetail2());*/

    }

    @Override
    public int getItemCount() {
        return weatherListDetails.size();
    }
}
