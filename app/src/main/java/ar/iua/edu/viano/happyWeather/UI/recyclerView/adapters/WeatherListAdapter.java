package ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;

import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.model.Weather;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.WeatherHolder;

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherHolder> {
    private List<Weather> weatherList;

    public WeatherListAdapter(@NonNull List<Weather> weather) {
        this.weatherList = weather;
    }

    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_view, viewGroup, false);
        return new WeatherHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHolder weatherHolder, int i) {
        SimpleDateFormat formato = new SimpleDateFormat("HH");
        Weather weather = weatherList.get(i);
        String data = formato.format(weather.getDate());
        weatherHolder.gettime().setText(data);
        weatherHolder.getactualTemp().setText(Double.toString(weather.getActualTemp()));
        weatherHolder.geticonView().setBackgroundResource(R.drawable.ic_wb_sunny_24px);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}
