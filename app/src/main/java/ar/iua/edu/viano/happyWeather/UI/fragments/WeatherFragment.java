package ar.iua.edu.viano.happyWeather.UI.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ar.iua.edu.viano.happyWeather.CurrentWeatherService;

import ar.iua.edu.viano.happyWeather.GPS;
import ar.iua.edu.viano.happyWeather.Model.DTO.WeatherFromApi;
import ar.iua.edu.viano.happyWeather.Persistence.Data.Weather;
import ar.iua.edu.viano.happyWeather.Persistence.Data.WeatherForecast;
import ar.iua.edu.viano.happyWeather.Persistence.Database.DailyWeather.DailyWeatherRepository;
import ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherForecast.WeatherForecastRepository;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters.WeatherListAdapter;
//import ar.iua.edu.viano.happyWeather.Model.Weather;
import ar.iua.edu.viano.happyWeather.UI.recyclerView.adapters.WeatherForecastListAdapter;
import ar.iua.edu.viano.happyWeather.Model.WeatherDetails;

public class WeatherFragment extends Fragment {
    // recyclerView del pronostico
    RecyclerView recyclerViewForecast;
    // RecyclerView del clima actual
    RecyclerView recyclerViewWeather;
    // Recyclerview de los detalles
    RecyclerView recyclerViewDetails;
    // View para tem max y min
    RecyclerView recyclerViewWeatherToday;
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
    private List<WeatherForecast> weatherForecast = new ArrayList<>();
    private List<Weather> weather = new ArrayList<>();
    // BDD
    private DailyWeatherRepository dailyWeatherRepository;
    private WeatherForecastRepository weatherForecastRepository;
    List<Weather> listWeather = new ArrayList<>();
    List<WeatherForecast> listWeatherForecast = new ArrayList<>();
    List<WeatherForecast> weatherToday = new ArrayList<>();
    //private List<WeatherDetails> weatherDetails = new ArrayList<>();
    private WeatherDetails wd;

    Calendar calendario = new GregorianCalendar();
    private PreferencesUtils preferencesUtils;
    private ReloadButtonListener reloadButtonListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.fragment_weather, container, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.line_divider));
        dailyWeatherRepository = new DailyWeatherRepository(getActivity().getApplication());
        weatherForecastRepository = new WeatherForecastRepository(getActivity().getApplication());
        reloadButtonListener = (ReloadButtonListener) getActivity();
        //-------------------------------Obtiene los campos y llama a la api-------------------------------------------------------------

        location = retView.findViewById(R.id.location);
        actualWeather = retView.findViewById(R.id.actualWeather);
        temp = retView.findViewById(R.id.temp);
        // initWeatherDetails();
        humidity = retView.findViewById(R.id.humidity);
        pressure = retView.findViewById(R.id.pressure);
        visibility = retView.findViewById(R.id.visibility);
        wind = retView.findViewById(R.id.wind);
        preferencesUtils = new PreferencesUtils(getActivity().getApplicationContext());
        initAllWeather();
        if(listWeather.size() == 0 || listWeatherForecast.size() == 0 || weatherToday.size() == 0)
            reloadButtonListener.refreshWeather();
        initAllWeather();
        setActualWeather();
        setDetails();
        //--------------------------Recycler del pronostico --------------------------------------------------------------------
        recyclerViewForecast = retView.findViewById(R.id.recyclerViewWeatherForecast);
        recyclerViewForecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewForecast.setAdapter(new WeatherForecastListAdapter(listWeatherForecast));
        recyclerViewForecast.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        //--------------------------Recycler del clima 24hs--------------------------------------------------------------------
        recyclerViewWeather = retView.findViewById(R.id.recyclerViewWeather);
        recyclerViewWeather.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewWeather.setAdapter(new WeatherListAdapter(listWeather));
        //-------------------------Recycler del clima actual--------------------------------------------------------------------------
        recyclerViewWeatherToday = retView.findViewById(R.id.recyclerViewWeatherToday);
        recyclerViewWeatherToday.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewWeatherToday.setAdapter(new WeatherForecastListAdapter(weatherToday));

        return retView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reloadButtonListener.refreshWeather();
        initAllWeather();
    }

    private void setDetails() {
        Weather w = listWeather.get(0);
        humidity.setText(String.format(getResources().getString(R.string.humidity), w.getHumidity()));
        pressure.setText(String.format(getResources().getString(R.string.pressure), w.getPressure()));
        visibility.setText(String.format(getResources().getString(R.string.visibility), w.getVisibility()));
        wind.setText(String.format(getResources().getString(R.string.wind), w.getWind()));
    }

    private void setActualWeather() {
        listWeather = dailyWeatherRepository.getAllHours();
        listWeatherForecast = weatherForecastRepository.getAllDays();
        location.setText(listWeather.get(0).getLocation());
        actualWeather.setText(R.string.snowy);
        temp.setText(listWeather.get(0).getActualTemp() + "°");
        WeatherForecast fw = new WeatherForecast();
        fw.setDate(listWeather.get(0).getDate());
        fw.setDOW(preferencesUtils.getLastUpdate());
        fw.setMaximum(listWeather.get(0).getMaximum());
        fw.setMinimum(listWeather.get(0).getMinimum());
        fw.setWeather(-1);
        weatherToday.add(fw);
    }

    private void initAllWeather() {
        listWeather = dailyWeatherRepository.getAllHours();
        listWeatherForecast = weatherForecastRepository.getAllDays();


    }

    private void initWeather() {
        //Simula tdo un dia
        //initWeatherDetails();
        for (int i = 0; i < 24; i++) {
            wd.setHumidity("" + i);
            weather.add(new Weather(i + 10.0, 10.0 - i, i, "Cordoba", new Date(new Date().getTime() + 3570947 * i), 3, wd));
        }
    }

    private void initWeatherForecast() {
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            // Simula una semana
            calendar.setTime(new Date(new Date().getTime() + 78097230 * i));
            weatherForecast.add(new WeatherForecast(29.0 + i, 15 - i, "Cordoba", setDOW(calendar.get(Calendar.DAY_OF_WEEK)), 1));
        }
    }

    private String setDOW(int DOW) {
        System.out.println("dow " + DOW);
        switch (DOW) {
            case 1:
                return getResources().getString(R.string.monday);
            case 2:
                return getResources().getString(R.string.tuesday);
            case 3:
                return getResources().getString(R.string.wednesday);
            case 4:
                return getResources().getString(R.string.thursday);
            case 5:
                return getResources().getString(R.string.friday);
            case 6:
                return getResources().getString(R.string.saturday);
            case 7:
                return getResources().getString(R.string.sunday);
            default:
                return "Error";
        }
    }

    private void initWeatherDetails() {
        //Humedad + riesgo de lluvia
      /*  wd = new WeatherDetails(
                getActivity().getApplicationContext(), "50", "30", "1024",
                "15", "10", "15", "9");
*/
    }



    public interface ReloadButtonListener {
        void refreshWeather();
    }


}
