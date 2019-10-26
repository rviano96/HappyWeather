package ar.iua.edu.viano.happyWeather.UI.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.iua.edu.viano.happyWeather.Persistence.Data.DailyWeather;
import ar.iua.edu.viano.happyWeather.Persistence.Data.WeatherForecast;
import ar.iua.edu.viano.happyWeather.Persistence.Database.DailyWeather.DailyWeatherRepository;
import ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherForecast.WeatherForecastRepository;
import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters.WeatherDetailsListAdapter;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters.WeatherListAdapter;
import ar.iua.edu.viano.happyWeather.Model.Weather;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters.WeatherForecastListAdapter;
import ar.iua.edu.viano.happyWeather.Model.WeatherDetails;


public class WeatherFragment extends Fragment {
    // recyclerView del pronostico
    RecyclerView recyclerViewForecast;
     // RecyclerView del clima actual
    RecyclerView recyclerViewWeather;
    // Recyclerview de los detalles
    RecyclerView recyclerViewDetails;
    //Location
    private TextView location;
    // Humidity
    private TextView humidity;
    //RoR
    private TextView riskOfRain;
    //Pressure
    private TextView pressure;
    //Rain
    private TextView rain;
    //Visibility
    private TextView visibility;
    //Wind
    private TextView wind;
    //ApparentTemp
    private TextView appTemp;
    //actualWeather
    private TextView actualWeather;
    //temp
    private TextView temp;
    private List<Weather> weatherForecast = new ArrayList<>();
    private List<Weather> weather = new ArrayList<>();
    // BDD
    private DailyWeatherRepository dailyWeatherRepository;
    private WeatherForecastRepository weatherForecastRepository;

    //private List<WeatherDetails> weatherDetails = new ArrayList<>();
    private WeatherDetails wd ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView= inflater.inflate(R.layout.fragment_weather, container, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.line_divider));

        //--------------------------Recycler del pronostico --------------------------------------------------------------------
        recyclerViewForecast = retView.findViewById(R.id.recyclerViewWeatherForecast);
        recyclerViewForecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        initWeatherForecast();

        recyclerViewForecast.setAdapter(new WeatherForecastListAdapter(weatherForecast));
        recyclerViewForecast.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        //--------------------------Recycler del clima actual--------------------------------------------------------------------

        recyclerViewWeather = retView.findViewById(R.id.recyclerViewWeather);
        recyclerViewWeather.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        initWeather();

        recyclerViewWeather.setAdapter(new WeatherListAdapter(weather));


        location = retView.findViewById(R.id.location);
        actualWeather = retView.findViewById(R.id.actualWeather);
        temp = retView.findViewById(R.id.temp);
        location.setText( weather.get(0).getLocation());
        actualWeather.setText(R.string.snowy);
        temp.setText("31°");
        initWeatherDetails();
        humidity = retView.findViewById(R.id.humidity);
        riskOfRain = retView.findViewById(R.id.riskOfRain);
        pressure = retView.findViewById(R.id.pressure);
        rain = retView.findViewById(R.id.rain);
        visibility = retView.findViewById(R.id.visibility);
        wind = retView.findViewById(R.id.wind);
        appTemp = retView.findViewById(R.id.apparentTemp);
        setDetails();
        return  retView;
    }

    private void setDetails(){
        humidity.setText(wd.getHumidity());
        riskOfRain.setText(wd.getRiskOfRain());
        pressure.setText(wd.getPressure());
        rain.setText(wd.getRain());
        visibility.setText(wd.getVisibility());
        wind.setText(wd.getWind());
        appTemp.setText(wd.getApparentTemperature());
    }


    private void initAllWeather(){
        List<DailyWeather> listDailyWeather = new ArrayList<>();
        List <WeatherForecast> listWeatherForecast= new ArrayList<>();
        listDailyWeather = dailyWeatherRepository.getAllHours();
        listWeatherForecast = weatherForecastRepository.getAllDays();
        if(listDailyWeather.size() == 0){
            initWeatherDetails();
            initWeather();
            for (Weather weather: weather) {
                    dailyWeatherRepository.insert((DailyWeather) weather);
            }
            //dailyWeatherRepository.insert(weather);
        }

        if(listWeatherForecast.size() == 0){
            initWeatherForecast();
            for (Weather weather: weatherForecast) {
                weatherForecastRepository.insert((WeatherForecast) weather);
            }
        }
    }

    private void initWeather(){
        //Simula tdo un dia
        for(int i = 0; i < 24; i++){
            weather.add(new Weather(29.0+i,15-i, i,"Cordoba", new Date(new Date().getTime() + 3570947*i), 3));
        }
    }

    private void initWeatherForecast(){

        for(int i = 0; i < 7; i++){
            // Simula una semana
            weatherForecast.add(new Weather(29.0+i,15-i,"Cordoba", new Date(new Date().getTime() + 78097230*i), 1));
        }
    }
    private void initWeatherDetails(){
              //Humedad + riesgo de lluvia
        wd = new WeatherDetails(
                getActivity().getApplicationContext(), "50","30","1024",
                "15","10","15", "9" );

    }
}
