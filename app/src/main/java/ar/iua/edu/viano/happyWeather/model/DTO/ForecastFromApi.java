package ar.iua.edu.viano.happyWeather.Model.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastFromApi {
    @SerializedName("list")
    private List<WeatherFromApi> weatherFromApiList;

    public ForecastFromApi(List<WeatherFromApi> weatherFromApiList) {
        this.weatherFromApiList = weatherFromApiList;
    }

    public List<WeatherFromApi> getWeatherFromApiList() {
        return weatherFromApiList;
    }

    public void setWeatherFromApiList(List<WeatherFromApi> weatherFromApiList) {
        this.weatherFromApiList = weatherFromApiList;
    }

    @Override
    public String toString() {
        return "ForecastFromApi{" +
                "weatherFromApiList=" + weatherFromApiList +
                '}';
    }
}
