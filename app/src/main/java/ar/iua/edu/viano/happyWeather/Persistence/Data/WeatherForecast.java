package ar.iua.edu.viano.happyWeather.Persistence.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Date;

// import ar.iua.edu.viano.happyWeather.Model.Weather;
import ar.iua.edu.viano.happyWeather.Model.WeatherBase;
import ar.iua.edu.viano.happyWeather.Model.WeatherDetails;

// Extiende de weather para tener todos los metodos
@Entity(tableName = "WeatherForecast")
public class WeatherForecast  extends WeatherBase {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    public WeatherForecast( double maximum, double minimum, String location, Date date,  int weather, WeatherDetails weatherDetails) {
        super(maximum, minimum, location, date, weather);
    }

    public WeatherForecast(double maximum, double minimum, String location, String DOW, int weather) {
        super(maximum, minimum, location, DOW, weather);
    }
    public WeatherForecast(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
