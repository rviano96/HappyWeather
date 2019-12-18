package ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ar.iua.edu.viano.happyWeather.Helpers.ConvertUnits;
import ar.iua.edu.viano.happyWeather.Helpers.SetIcons;
import ar.iua.edu.viano.happyWeather.Persistence.Data.WeatherForecast;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.WeatherForecastHolder;
//import ar.iua.edu.viano.happyWeather.Model.Weather;

public class WeatherForecastListAdapter extends RecyclerView.Adapter<WeatherForecastHolder> {
    private List<WeatherForecast> weatherForecastList;
    private ConvertUnits convertUnits = new ConvertUnits();
    private PreferencesUtils preferencesUtils;
    private SetIcons setIcons;
    public WeatherForecastListAdapter(@NonNull List<WeatherForecast> weatherForecast) {
        this.weatherForecastList = weatherForecast;
    }

    @NonNull
    @Override
    public WeatherForecastHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_forecast_view, viewGroup, false);
        preferencesUtils = new PreferencesUtils(row.getContext());
        setIcons = new SetIcons(row.getContext());
        return new WeatherForecastHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherForecastHolder weatherForecastHolder, int i) {
        WeatherForecast weather = weatherForecastList.get(i);
        weatherForecastHolder.getDOW().setText(weather.getDOW());
        /*weatherForecastHolder.getmaxTemp().setText(weather.getMaximum() + "°");
        weatherForecastHolder.getminTemp().setText(weather.getMinimum() + "°");*/
        weatherForecastHolder.getmaxTemp().setText(convertUnits.formatTemperature(weather.getMaximum(),preferencesUtils.getUnits()));
        weatherForecastHolder.getminTemp().setText(convertUnits.formatTemperature(weather.getMinimum(),preferencesUtils.getUnits()));
        weatherForecastHolder.geticonView().setBackgroundResource(setIcons.findIcon(String.valueOf(weather.getWeather())));
        //sunny
        /*if (weather.getWeather() != -1) {
            if (weather.getWeather() == 0) {

            } else {//cloudy
                if (weather.getWeather() == 1) {
                    weatherForecastHolder.geticonView().setBackgroundResource(R.drawable.ic_iconfinder_thunderstorm);
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
        }*/
    }


    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }
}
