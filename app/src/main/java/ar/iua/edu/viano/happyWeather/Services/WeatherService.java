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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.iua.edu.viano.happyWeather.Constants.Constants;
import ar.iua.edu.viano.happyWeather.Helpers.ConvertUnits;
import ar.iua.edu.viano.happyWeather.Model.DTO.ForecastFromApi;
import ar.iua.edu.viano.happyWeather.Model.DTO.WeatherFromApi;
import ar.iua.edu.viano.happyWeather.Model.WeatherDetails;
import ar.iua.edu.viano.happyWeather.Persistence.Data.Weather;
import ar.iua.edu.viano.happyWeather.Persistence.Data.WeatherForecast;
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

    /*public void WeatherService(Context context){
        this.context = context;
        this.preferencesUtils = new PreferencesUtils(context);
        /*weatherForecastRepository = new WeatherForecastRepository(application);
        dailyWeatherRepository = new DailyWeatherRepository(application);
    }*/

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

    private void saveData(List<WeatherFromApi> data) {
        WeatherFromApi dto = data.get(0);
        String name = dto.getName();
        WeatherDetails weatherDetails;
        Long day = dto.getDt();
        List<Weather> weather = new ArrayList<>();
        List<WeatherForecast> weatherForecast = new ArrayList<>();
        //simular 1 dia 24hs
        for (int i = 0; i < 8; i++) {
            dto = data.get(i);
            weatherDetails = new WeatherDetails(String.valueOf(dto.getMain().getHumidity()),
                    String.valueOf(dto.getMain().getPressure()), dto.getWind().getSpeed(),
                    String.valueOf(dto.getVisibility()));
            weather.add(new Weather(dto.getMain().getTempMax(), dto.getMain().getTempMin(), dto.getMain().getTemp(),
                    name, new Date(dto.getDt() * 1000), 1, weatherDetails));
        }
        int position = 0;
        // me fijo cuando el campo dateText es diferente,
        //entonces tomo la posicion anterior y a partir de esa, cada nuevo dia se va a encontrar cada 8 posiciones
        for (int i = 2; i <= data.size(); i++) {
            if (!data.get(i - 1).getDateTxt().split(" ")[0].equals(data.get(i).getDateTxt().split(" ")[0])) {
                position = i - 1;
                break;
            }
        }
        Calendar calendar = Calendar.getInstance();
        for (int i = position; i < data.size(); i += 8) {
            //29.0+i,15-i,"Cordoba", setDOW(calendar.get(Calendar.DAY_OF_WEEK)), 1)
            calendar.setTime(new Date(data.get(i).getDt() * 1000));
            weatherForecast.add(new WeatherForecast(data.get(i).getMain().getTempMax(), data.get(i).getMain().getTempMin(),
                    name, String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)), 1));
        }
        dailyWeatherRepository.delete();
        weatherForecastRepository.deleteWeatherForecast();
        for (Weather w : weather) {
            dailyWeatherRepository.insert(w);
        }
        for (WeatherForecast wf : weatherForecast) {
            weatherForecastRepository.insert(wf);
        }
        calendar.setTime(new Date());
        preferencesUtils.setLastUpdate(calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                calendar.get(Calendar.MINUTE));
        preferencesUtils.setActualTemp(convertUnits.formatTemperature(weather.get(0).getActualTemp(),preferencesUtils.getUnits()));
        preferencesUtils.setMaxTemp(convertUnits.formatTemperature(weather.get(0).getMaximum(),preferencesUtils.getUnits()));

    }
}
