package ar.iua.edu.viano.happyWeather.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Weather {
    private double maximum;
    private double minimum;
    private String DOW;//DayOftheWeek
    private String location;
    private Date date;
    private double actualTemp;
    private WeatherDetails weatherDetails;
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

    public Weather(double maximum, double minimum, String location, Date date) {
        this.maximum = maximum;
        this.minimum = minimum;
        this.location = location;
        this.date = date;
        this.DOW = new SimpleDateFormat("EEEEE").format(date); //obtiene el dia de la semana
    }
    public Weather(double maximum, double minimum, double actualTemp, String location, Date date) {
        this.maximum = maximum;
        this.minimum = minimum;
        this.location = location;
        this.date = date;
        this.actualTemp = actualTemp;
        this.DOW = new SimpleDateFormat("EEEE").format(date); //obtiene el dia de la semana
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
                ", actualTemp=" + actualTemp +
                '}';
    }
}
