package ar.iua.edu.viano.happyWeather.UI.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ar.iua.edu.viano.happyWeather.Helpers.ConvertUnits;
import ar.iua.edu.viano.happyWeather.Helpers.SetWeatherStatus;
import ar.iua.edu.viano.happyWeather.Persistence.Data.Weather;
import ar.iua.edu.viano.happyWeather.Persistence.Data.WeatherForecast;
import ar.iua.edu.viano.happyWeather.Persistence.Database.DailyWeather.DailyWeatherRepository;
import ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherForecast.WeatherForecastRepository;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters.WeatherForecastListAdapter;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters.WeatherListAdapter;

public class WeatherFragment extends Fragment {
    // recyclerView del pronostico
    RecyclerView recyclerViewForecast;
    // adapter
    RecyclerView.Adapter viewForecastAdapter;
    // RecyclerView del clima actual
    RecyclerView recyclerViewWeather;
    // adapter
    RecyclerView.Adapter viewWeatherAdapter;

    //Location
    private TextView location;
    // Humidity
    private TextView humidity;
    //Pressure
    private TextView pressure;
    //Visibility
    private TextView visibility;
    //Wind
    private TextView wind;
    //actualWeather
    private TextView actualWeather;
    //temp
    private TextView temp;
    private TextView lastUpdate;
    private ConvertUnits convertUnits = new ConvertUnits();
    // BDD
    private DailyWeatherRepository dailyWeatherRepository;
    private WeatherForecastRepository weatherForecastRepository;
    List<Weather> listWeather = new ArrayList<>();
    List<WeatherForecast> listWeatherForecast = new ArrayList<>();
    SetWeatherStatus setWeatherStatus;

    private PreferencesUtils preferencesUtils;
    private ReloadButtonListener reloadButtonListener;
    View retView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         retView = inflater.inflate(R.layout.fragment_weather, container, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.line_divider));
        dailyWeatherRepository = new DailyWeatherRepository(getActivity().getApplication());
        weatherForecastRepository = new WeatherForecastRepository(getActivity().getApplication());
        reloadButtonListener = (ReloadButtonListener) getActivity();
        setWeatherStatus = new SetWeatherStatus(getContext());
        //-------------------------------Obtiene los campos-------------------------------------------------------------
        location = retView.findViewById(R.id.location);
        actualWeather = retView.findViewById(R.id.actualWeather);
        temp = retView.findViewById(R.id.temp);
        lastUpdate = retView.findViewById(R.id.lastUpdate);
        humidity = retView.findViewById(R.id.humidity);
        pressure = retView.findViewById(R.id.pressure);
        visibility = retView.findViewById(R.id.visibility);
        wind = retView.findViewById(R.id.wind);
        preferencesUtils = new PreferencesUtils(getActivity().getApplicationContext());

        return retView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reloadButtonListener.refreshWeather();
        initAllWeather();
        setActualWeather();
        setDetails();
        //--------------------------Recycler del pronostico --------------------------------------------------------------------
        recyclerViewForecast = retView.findViewById(R.id.recyclerViewWeatherForecast);
        recyclerViewForecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        viewForecastAdapter = new WeatherForecastListAdapter(listWeatherForecast);
        recyclerViewForecast.setAdapter(viewForecastAdapter);
        recyclerViewForecast.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        //--------------------------Recycler del clima 24hs--------------------------------------------------------------------
        recyclerViewWeather = retView.findViewById(R.id.recyclerViewWeather);
        recyclerViewWeather.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        viewWeatherAdapter = new WeatherListAdapter(listWeather);
        recyclerViewWeather.setAdapter(viewWeatherAdapter);


    }

    private void setDetails() {
        if (!isEmpty(listWeather)) {
            Weather w = listWeather.get(0);
            humidity.setText(String.format(getResources().getString(R.string.humidity), w.getHumidity()));
            pressure.setText(String.format(getResources().getString(R.string.pressure), w.getPressure()));
            visibility.setText(String.format(getResources().getString(R.string.visibility), convertUnits.formatVisibility(w.getVisibility(), preferencesUtils.getUnits()) ));
            wind.setText(String.format(getResources().getString(R.string.wind), convertUnits.formatWind(w.getWind(), preferencesUtils.getUnits())));
        }else{
            initAllWeather();
        }

    }

    private void setActualWeather() {

        if (!isEmpty(listWeather)) {
            location.setText(listWeather.get(0).getLocation());
            actualWeather.setText(setWeatherStatus.setStatus(String.valueOf(listWeather.get(0).getWeather())));
            temp.setText(convertUnits.formatTemperature(listWeather.get(0).getActualTemp(),preferencesUtils.getUnits()));
            lastUpdate.setText("Last update: " + preferencesUtils.getLastUpdate());
        }else{
            initAllWeather();
        }

    }

    private void initAllWeather() {
        listWeather = dailyWeatherRepository.getAllHours();
        listWeatherForecast = weatherForecastRepository.getAllDays();
    }




    private boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    private boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public interface ReloadButtonListener {
        void refreshWeather();
    }

    public void refreshData(){
        initAllWeather();
        setActualWeather();
        viewForecastAdapter = new WeatherForecastListAdapter(listWeatherForecast);
        recyclerViewForecast.setAdapter(viewForecastAdapter);
        viewWeatherAdapter = new WeatherListAdapter(listWeather);
        recyclerViewWeather.setAdapter(viewWeatherAdapter);
        // viewWeatherAdapter.notifyDataSetChanged();
        //viewForecastAdapter.notifyDataSetChanged();
        Log.d("refresh", "refresh");
    }
}
