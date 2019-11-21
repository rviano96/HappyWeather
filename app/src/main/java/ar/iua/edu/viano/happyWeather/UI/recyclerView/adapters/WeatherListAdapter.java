package ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;

import ar.iua.edu.viano.happyWeather.Persistence.Data.Weather;
import ar.iua.edu.viano.happyWeather.R;
//import ar.iua.edu.viano.happyWeather.Model.Weather;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.WeatherHolder;

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherHolder> {
    private List<Weather> weatherList;


    public WeatherListAdapter(@NonNull List
            <Weather> weather) {
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
        //String data = formato.format(weather.getDate());
        String data = "5";
        weatherHolder.gettime().setText(data);
        weatherHolder.getactualTemp().setText(Double.toString(weather.getActualTemp()) + "°");
        //sunny
        if (weather.getWeather() == 0) {
            weatherHolder.geticonView().setBackgroundResource(R.drawable.ic_iconfinder_weather_01_1530392);
        } else {//cloudy
            if (weather.getWeather() == 1) {
                weatherHolder.geticonView().setBackgroundResource(R.drawable.ic_iconfinder_cloudy);
            } else {//snowy
                if (weather.getWeather() == 2) {
                    weatherHolder.geticonView().setBackgroundResource(R.drawable.ic_iconfinder_snowflake_1651934);
                } else {//rainny
                    if (weather.getWeather() == 3) {
                        weatherHolder.geticonView().setBackgroundResource(R.drawable.ic_iconfinder_rainny);
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}
