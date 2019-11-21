package ar.iua.edu.viano.happyWeather;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ar.iua.edu.viano.happyWeather.Constants.Constants;
import ar.iua.edu.viano.happyWeather.Model.DTO.ForecastFromApi;
import ar.iua.edu.viano.happyWeather.Model.DTO.WeatherFromApi;
import ar.iua.edu.viano.happyWeather.Model.WeatherDetails;
import ar.iua.edu.viano.happyWeather.Persistence.Data.Weather;
import ar.iua.edu.viano.happyWeather.Persistence.Data.WeatherForecast;

public class CurrentWeatherService extends AsyncTask<String, Void, List<WeatherFromApi>> {
    private static Gson gson;
    private Weather weather;
    private WeatherDetails weatherDetails;
    private WeatherForecast weatherForecast;


    @Override
    protected List<WeatherFromApi> doInBackground(String... strings) {
        try {
            WeatherFromApi dto;
            List<WeatherFromApi> dtoList = new ArrayList<>();
            //strings 0 = latittud, strings 1 = longitud, strings 2 = units
            String[] coords =  {strings[0],strings[1]};
            dto = weatherFromApi(coords, strings[2]);
            dtoList.add(dto);
            dtoList.addAll(forecastWeather(coords, strings[2]).getWeatherFromApiList());
            return dtoList;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream retrieveStream(String[] position, String type, String units) throws IOException {
        URL url = null;
        try {
            url = new URL("http://api.openweathermap.org/data/2.5/" + type + "?lat=" + position[0] + "&lon=" + position[1] + "&units=" + units + "&APPID=" + Constants.UID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return new BufferedInputStream(connection.getInputStream());
    }


    private void showData(String data) {
        System.out.println("data: " + data);
    }

    private WeatherFromApi weatherFromApi(String[] position, String units) throws IOException {
        InputStream source = retrieveStream(position, "weather", units);
        Reader reader = new InputStreamReader(source);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        return gson.fromJson(reader, WeatherFromApi.class);
    }

    private ForecastFromApi forecastWeather(String[] position, String units) throws IOException {
        InputStream source = retrieveStream(position, "forecast", units);
        Reader reader = new InputStreamReader(source);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        return gson.fromJson(reader, ForecastFromApi.class);

    }
}
