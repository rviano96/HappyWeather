package ar.iua.edu.viano.happyWeather.Persistence.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Date;

//import ar.iua.edu.viano.happyWeather.Model.Weather;
import ar.iua.edu.viano.happyWeather.Model.WeatherBase;
import ar.iua.edu.viano.happyWeather.Model.WeatherDetails;

// Extiende de weather para tener todos los metodos
@Entity(tableName = "Weather")
public class Weather extends WeatherBase {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String humidity;
    private String pressure;
    private String wind;
    private String visibility;
    private double actualTemp;
    private int hour;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Ignore
    private Context context;

    public double getActualTemp() {
        return actualTemp;
    }

    public void setActualTemp(double actualTemp) {
        this.actualTemp = actualTemp;
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Weather() {

    }


    public Weather(double maximum, double minimum, double actualTemp, String location, Date date, int weather, WeatherDetails weatherDetails) {
        super(maximum, minimum, location, "" + date.getDay(), weather);
        this.actualTemp = actualTemp;


        this.hour = date.getHours();
        initDetails(weatherDetails.getContext(), weatherDetails.getHumidity(),
                weatherDetails.getPressure(),
                weatherDetails.getWind(), weatherDetails.getVisibility());
    }

    private void initDetails(Context context, String humidity,
                             String pressure,
                             String wind,
                             String visibility) {
        this.context = context;
        this.humidity = humidity;
        this.pressure = pressure;
        this.wind = wind;
        this.visibility = visibility;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
