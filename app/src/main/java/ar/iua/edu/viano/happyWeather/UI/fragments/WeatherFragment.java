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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters.WeatherDetailsListAdapter;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters.WeatherListAdapter;
import ar.iua.edu.viano.happyWeather.model.Weather;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters.WeatherForecastListAdapter;
import ar.iua.edu.viano.happyWeather.model.WeatherDetails;


public class WeatherFragment extends Fragment {
    // recyclerView del pronostico
    RecyclerView recyclerViewForecast;
     // RecyclerView del clima actual
    RecyclerView recyclerViewWeather;
    // Recyclerview de los detalles
    RecyclerView recyclerViewDetails;
    //Location
    private TextView location;
    //actualWeather
    private TextView actualWeather;
    //temp
    private TextView temp;
    private List<Weather> weatherForecast = new ArrayList<>();
    private List<Weather> weather = new ArrayList<>();
    private List<WeatherDetails> weatherDetails = new ArrayList<>();
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



        //-------------------------------Recycler de los detalles------------------------------------------------------------------
        recyclerViewDetails = retView.findViewById(R.id.recyclerViewDetails);
        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        initWeatherDetails();

        recyclerViewDetails.setAdapter(new WeatherDetailsListAdapter(weatherDetails));
        recyclerViewDetails.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        //----------------------------------Datos Actuales--------------------------------------------------------------
        location = retView.findViewById(R.id.location);
        actualWeather = retView.findViewById(R.id.actualWeather);
        temp = retView.findViewById(R.id.temp);
        location.setText( weather.get(0).getLocation());
        actualWeather.setText(R.string.snowy);
        temp.setText("31°");
        return  retView;
    }


    private void initWeatherForecast(){

        for(int i = 0; i < 7; i++){
            // Simula una semana
            weatherForecast.add(new Weather(29.0+i,15-i,"Cordoba", new Date(new Date().getTime() + 78097230*i), 1));
        }
    }
    private void initWeather(){
        //Simula tdo un dia
        for(int i = 0; i < 24; i++){
            weather.add(new Weather(29.0+i,15-i, i,"Cordoba", new Date(new Date().getTime() + 3570947*i), 3));
        }
    }

    private void initWeatherDetails(){
        //Humedad + riesgo de lluvia
        weatherDetails.add(new WeatherDetails(String.format(getString(R.string.humidity),"50" ), String.format(getString(R.string.riskOfRain),"10" )));
        // Presion + Precipitaciones
        weatherDetails.add(new WeatherDetails(String.format(getString(R.string.pressure),"1024" ), String.format(getString(R.string.rain),"15" )));
        // Sensacion Termica + Viento
        weatherDetails.add(new WeatherDetails(String.format(getString(R.string.apparentTemperature),"10" ), String.format(getString(R.string.wind),"15" )));
        //visibilidad
        weatherDetails.add(new WeatherDetails(String.format(getString(R.string.visibility),"9" ), ""));

    }
}
