package ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.WeatherForecastHolder;
import ar.iua.edu.viano.happyWeather.model.Weather;

public class WeatherForecastListAdapter extends RecyclerView.Adapter<WeatherForecastHolder> {
    private List<Weather> weatherForecastList;

    public WeatherForecastListAdapter(@NonNull List<Weather> weatherForecast) {
        this.weatherForecastList = weatherForecast;
    }

    @NonNull
    @Override
    public WeatherForecastHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_forecast_view, viewGroup, false);
        return new WeatherForecastHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherForecastHolder weatherForecastHolder, int i) {
        Weather weather = weatherForecastList.get(i);
        weatherForecastHolder.getDOW().setText(weather.getDOW());
        weatherForecastHolder.getmaxTemp().setText(Double.toString(weather.getMaximum()));
        weatherForecastHolder.getminTemp().setText(Double.toString(weather.getMinimum()));
        weatherForecastHolder.geticonView().setBackgroundResource(R.drawable.ic_wb_sunny_24px);
    }

    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }
}
