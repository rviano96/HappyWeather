package ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;

import ar.iua.edu.viano.happyWeather.Helpers.ConvertUnits;
import ar.iua.edu.viano.happyWeather.Helpers.SetIcons;
import ar.iua.edu.viano.happyWeather.Persistence.Data.Weather;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.WeatherHolder;

//import ar.iua.edu.viano.happyWeather.Model.Weather;

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherHolder> {
    private List<Weather> weatherList;
    private PreferencesUtils preferencesUtils;
    private SetIcons setIcons;
    private ConvertUnits convertUnits = new ConvertUnits();
    public WeatherListAdapter(@NonNull List
            <Weather> weather) {
        this.weatherList = weather;
    }

    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_view, viewGroup, false);
        preferencesUtils = new PreferencesUtils(row.getContext());
        setIcons = new SetIcons(row.getContext());
        return new WeatherHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHolder weatherHolder, int i) {
        SimpleDateFormat formato = new SimpleDateFormat("HH");
        Weather weather = weatherList.get(i);
        //String data = formato.format(weather.getDate());
        String data = String.valueOf(weather.getHour());
        weatherHolder.gettime().setText(data);
        weatherHolder.getactualTemp().setText(convertUnits.formatTemperature(weather.getActualTemp(), preferencesUtils.getUnits()));
        weatherHolder.geticonView().setBackgroundResource(setIcons.findIcon(String.valueOf(weather.getWeather())));
        //sunny
        /*if (weather.getWeather() == 0) {
           // weatherHolder.geticonView().setBackgroundResource(setIcons.findIcon(weather.getWeather()));
        } else {//cloudy
            if (weather.getWeather() == 1) {
                weatherHolder.geticonView().setBackgroundResource(R.drawable.ic_iconfinder_thunderstorm);
            } else {//snowy
                if (weather.getWeather() == 2) {
                    weatherHolder.geticonView().setBackgroundResource(R.drawable.ic_iconfinder_snowflake_1651934);
                } else {//rainny
                    if (weather.getWeather() == 3) {
                        weatherHolder.geticonView().setBackgroundResource(R.drawable.ic_iconfinder_rainny);
                    }
                }
            }
        }*/

    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}
