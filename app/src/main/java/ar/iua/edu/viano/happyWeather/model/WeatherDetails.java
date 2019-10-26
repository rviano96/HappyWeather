package ar.iua.edu.viano.happyWeather.Model;

import android.arch.persistence.room.Ignore;
import android.content.Context;

import ar.iua.edu.viano.happyWeather.R;

public class WeatherDetails {
    /*private String detail1;
    private String detail2;*/
    private String humidity;
    private String riskOfRain;
    private String pressure;
    private String rain;
    private String apparentTemperature; // Sensacion Termica
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

    public String getRiskOfRain() {
        return riskOfRain;
    }

    public void setRiskOfRain(String riskOfRain) {
        this.riskOfRain = riskOfRain;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getApparentTemperature() {
        return apparentTemperature;
    }

    public void setApparentTemperature(String apparentTemperature) {
        this.apparentTemperature = apparentTemperature;
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

    public WeatherDetails(){

    }
    public WeatherDetails(Context context, String humidity,
                          String riskOfRain,
                          String pressure,
                          String rain,
                          String apparentTemperature,
                          String wind,
                          String visibility) {
        this.context = context;
        this.humidity = String.format(context.getString(R.string.humidity),humidity);
        this.riskOfRain = String.format(context.getString(R.string.riskOfRain),riskOfRain);;
        this.pressure = String.format(context.getString(R.string.pressure),pressure);;
        this.rain = String.format(context.getString(R.string.rain),rain);;
        this.apparentTemperature = String.format(context.getString(R.string.apparentTemperature),apparentTemperature);;
        this.wind = String.format(context.getString(R.string.wind),wind);;
        this.visibility = String.format(context.getString(R.string.visibility),visibility);
    }


}
