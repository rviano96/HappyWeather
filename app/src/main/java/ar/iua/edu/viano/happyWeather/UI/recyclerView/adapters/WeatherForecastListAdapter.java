package ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private Context context;
    public WeatherForecastListAdapter(@NonNull List<WeatherForecast> weatherForecast) {
        this.weatherForecastList = weatherForecast;
    }

    @NonNull
    @Override
    public WeatherForecastHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_forecast_view, viewGroup, false);
        preferencesUtils = new PreferencesUtils(row.getContext());
        context = row.getContext();
        setIcons = new SetIcons(row.getContext());
        return new WeatherForecastHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherForecastHolder weatherForecastHolder, int i) {
        WeatherForecast weather = weatherForecastList.get(i);
        weatherForecastHolder.getDOW().setText(setDOW(Integer.parseInt(weather.getDOW())));
        /*weatherForecastHolder.getmaxTemp().setText(weather.getMaximum() + "°");
        weatherForecastHolder.getminTemp().setText(weather.getMinimum() + "°");*/
        weatherForecastHolder.getmaxTemp().setText(convertUnits.formatTemperature(weather.getMaximum(),preferencesUtils.getUnits()));
        weatherForecastHolder.getminTemp().setText(convertUnits.formatTemperature(weather.getMinimum(),preferencesUtils.getUnits()));
        weatherForecastHolder.geticonView().setBackgroundResource(setIcons.findIcon(String.valueOf(weather.getWeather())));

    }

    private String setDOW(int DOW) {
        Log.d("dow", String.valueOf(DOW));
        switch (DOW) {
            case 1:
                return context.getResources().getString(R.string.monday);
            case 2:
                return context.getResources().getString(R.string.tuesday);
            case 3:
                return context.getResources().getString(R.string.wednesday);
            case 4:
                return context.getResources().getString(R.string.thursday);
            case 5:
                return context.getResources().getString(R.string.friday);
            case 6:
                return context.getResources().getString(R.string.saturday);
            case 7:
                return context.getResources().getString(R.string.sunday);
            default:
                return "Error";
        }
    }
    @Override
    public int getItemCount() {
        return weatherForecastList.size();
    }
}
