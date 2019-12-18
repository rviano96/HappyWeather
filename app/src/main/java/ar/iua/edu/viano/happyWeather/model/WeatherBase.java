package ar.iua.edu.viano.happyWeather.Model;

import android.arch.persistence.room.Ignore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherBase {

    private double maximum;
    private double minimum;
    private String DOW; //DayOftheWeek
    private String location;
    @Ignore
    private Date date;


    // private WeatherDetails weatherDetails;
    private int weather;

    public WeatherBase() {

    }

    public int getWeather() {
        return weather;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }


    public WeatherBase(double maximum, double minimum, String location, Date date, int weather) {
        this.maximum = maximum;
        this.minimum = minimum;
        this.location = location;
        this.date = date;
        this.DOW = new SimpleDateFormat("EEEE").format(date); //obtiene el dia de la semana
        this.weather = weather;

    }


    public WeatherBase(double maximum, double minimum, String location, String DOW, int weather) {

        this.maximum = maximum;
        this.minimum = minimum;
        this.location = location;
        this.DOW = DOW;
        this.weather = weather;
    }

    public double getMaximum(){
        return maximum;
    }
    public double getMinimum(){
        return minimum;
    }


    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }


    public String getDOW() {
        return DOW;
    }

    public void setDOW(String DOW) {
        this.DOW = DOW;

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "maximum=" + maximum +
                ", minimum=" + minimum +
                ", DOW='" + DOW + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                '}';
    }


}
