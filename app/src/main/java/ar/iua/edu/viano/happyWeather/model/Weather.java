package ar.iua.edu.viano.happyWeather.model;

import android.content.res.Resources;

import java.text.SimpleDateFormat;
import java.util.Date;

import ar.iua.edu.viano.happyWeather.R;

public class Weather {
    private double maximum;
    private double minimum;
    private String DOW;//DayOftheWeek
    private String location;
    private Date date;
    private double actualTemp;
    private WeatherDetails weatherDetails;
    private int weather;


    public int getWeather() {
        return weather;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public Weather() {

    }

    public WeatherDetails getWeatherDetails() {
        return weatherDetails;
    }

    public Weather(double maximum, double minimum, String DOW, String location, Date date, double actualTemp, WeatherDetails weatherDetails) {
        this.maximum = maximum;
        this.minimum = minimum;
        this.DOW = DOW;
        this.location = location;
        this.date = date;
        this.actualTemp = actualTemp;
        this.weatherDetails = weatherDetails;

    }

    public void setWeatherDetails(WeatherDetails weatherDetails) {
        this.weatherDetails = weatherDetails;
    }

    public Weather(double maximum, double minimum, String location, Date date,  int weather) {
        this.maximum = maximum;
        this.minimum = minimum;
        this.location = location;
        this.date = date;
        this.DOW = new SimpleDateFormat("EEEE").format(date); //obtiene el dia de la semana
        this.weather = weather;
    }
    public Weather(double maximum, double minimum, double actualTemp, String location, Date date,  int weather) {
        this.maximum = maximum;
        this.minimum = minimum;
        this.location = location;
        this.date = date;
        this.actualTemp = actualTemp;
        this.DOW = new SimpleDateFormat("EEEE").format(date); //obtiene el dia de la semana
        this.weather = weather;
    }
    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public double getMinimum() {
        return minimum;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public double getActualTemp() {
        return actualTemp;
    }

    public void setActualTemp(double actualTemp) {
        this.actualTemp = actualTemp;
    }

    public String getDOW() {
        return DOW;
    }

    public void setDOW(String DOW) {
        switch (DOW){
            case "Monday":
                this.DOW = Resources.getSystem().getString(R.string.monday);
                break;
            case "Tuesday":
                this.DOW = Resources.getSystem().getString(R.string.tuesday);
                break;
            case "Wednesday":
                this.DOW = Resources.getSystem().getString(R.string.wednesday);
                break;
            case "Friday":
                this.DOW = Resources.getSystem().getString(R.string.friday);
                break;
            case "Thursday":
                this.DOW = Resources.getSystem().getString(R.string.thursday);
                break;
            case "Sunday":
                this.DOW = Resources.getSystem().getString(R.string.sunday);
                break;
            case "Saturday":
                this.DOW = Resources.getSystem().getString(R.string.saturday);
                break;
            default:
                this.DOW = "Err";
        }
        //this.DOW = DOW;
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
                ", actualTemp=" + actualTemp +
                '}';
    }
}
