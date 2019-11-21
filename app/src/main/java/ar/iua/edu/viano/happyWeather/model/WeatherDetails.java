package ar.iua.edu.viano.happyWeather.Model;

import android.arch.persistence.room.Ignore;
import android.content.Context;

import ar.iua.edu.viano.happyWeather.R;

public class WeatherDetails {
    private String humidity;
    private String pressure;
    private String wind;
    private String visibility;
    @Ignore
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public WeatherDetails() {

    }


    public WeatherDetails(String humidity,  String pressure, String wind, String visibility) {
        this.humidity = humidity;
        this.pressure = pressure;
        this.wind = wind;
        this.visibility = visibility;
    }
}
