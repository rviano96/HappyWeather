package ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.WeatherForecastHolder;
import ar.iua.edu.viano.happyWeather.Model.Weather;

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
        weatherForecastHolder.getDOW().setText(weather.getDOW().toUpperCase());
        weatherForecastHolder.getmaxTemp().setText(weather.getMaximum()+ "°");
        weatherForecastHolder.getminTemp().setText(weather.getMinimum() + "°");
        //sunny

        if (weather.getWeather() == 0) {
            weatherForecastHolder.geticonView().setBackgroundResource(R.drawable.ic_iconfinder_weather_01_1530392);
        } else {//cloudy
            if (weather.getWeather() == 1) {
                weatherForecastHolder.geticonView().setBackgroundResource(R.drawable.ic_iconfinder_cloudy);
            } else {//snowy
                if (weather.getWeather() == 2) {
                    weatherForecastHolder.geticonView().setBackgroundResource(R.drawable.ic_iconfinder_snowflake_1651934);
                } else {//rainny
                    if (weather.getWeather() == 3) {
                        weatherForecastHolder.geticonView().setBackgroundResource(R.drawable.ic_iconfinder_rainny);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }
}
