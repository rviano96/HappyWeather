package ar.iua.edu.viano.happyWeather.Services;

import android.content.Context;

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

import ar.iua.edu.viano.happyWeather.Constants.Constants;
import ar.iua.edu.viano.happyWeather.Helpers.ConvertUnits;
import ar.iua.edu.viano.happyWeather.Model.DTO.ForecastFromApi;
import ar.iua.edu.viano.happyWeather.Model.DTO.WeatherFromApi;
import ar.iua.edu.viano.happyWeather.Persistence.Database.DailyWeather.DailyWeatherRepository;
import ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherForecast.WeatherForecastRepository;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;

public class WeatherService {
    private Gson gson;
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String params [] = {"?lat=", "&lon="};
    private static final String UID = "&APPID="+ Constants.UID;
    private String finalUrl = "";
    private PreferencesUtils preferencesUtils;
    private Context context;
    private WeatherForecastRepository weatherForecastRepository;
    private DailyWeatherRepository dailyWeatherRepository;
    private ConvertUnits convertUnits = new ConvertUnits();
    public WeatherService(Context context) {
        this.context = context;
        this.preferencesUtils = new PreferencesUtils(context);
    }

    public WeatherFromApi weatherFromApi(String[] position, String units) throws IOException {
        InputStream source = retrieveStream(position, "weather");
        Reader reader = new InputStreamReader(source);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        return gson.fromJson(reader, WeatherFromApi.class);
    }

    public ForecastFromApi forecastWeather(String[] position, String units) throws IOException {
        InputStream source = retrieveStream(position, "forecast");
        Reader reader = new InputStreamReader(source);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        return gson.fromJson(reader, ForecastFromApi.class);

    }

    private InputStream retrieveStream(String[] position, String type) throws IOException {
        URL url = null;
        try {
            buildFinalUrl(type, position);
            //Log.d("final url", this.finalUrl);
            url = new URL(this.finalUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return new BufferedInputStream(connection.getInputStream());
    }

    private void buildFinalUrl(String type, String [] position){
        this.finalUrl = this.BASE_URL + type + this.params[0] + position[0] + this.params[1] + position [1] + this.UID;
    }

}
