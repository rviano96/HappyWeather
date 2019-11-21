package ar.iua.edu.viano.happyWeather.Model.DTO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.security.acl.Owner;
import java.util.Date;
import java.util.List;

public class WeatherFromApi {
    @SerializedName("weather")
    private List<WeatherDto> listWeather;
    @SerializedName("visibility")
    private int visibility;
    @SerializedName("main")
    private MainDto main;
    @SerializedName("base")
    private String base;
    @SerializedName("wind")
    private WindDto wind;
    @SerializedName("name")
    private String name;
    @SerializedName("dt")
    private Long dt;
    @SerializedName("dt_txt")
    private String dateTxt;
    public WeatherFromApi(List<WeatherDto> listWeather, int visibility, MainDto main, String base, WindDto wind, String name, Long dt, String dateTxt) {
        this.listWeather = listWeather;
        this.visibility = visibility;
        this.main = main;
        this.base = base;
        this.wind = wind;
        this.name = name;
        this.dt = dt;
        this.dateTxt = dateTxt;
    }

    public String getDateTxt() {
        return dateTxt;
    }

    public void setDateTxt(String dateTxt) {
        this.dateTxt = dateTxt;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WeatherDto> getListWeather() {
        return listWeather;
    }

    public void setListWeather(List<WeatherDto> listWeather) {
        this.listWeather = listWeather;
    }

    @Override
    public String toString() {
        return "WeatherFromApi{" +
                "listWeather=" + listWeather +
                ", visibility=" + visibility +
                ", main=" + main +
                ", base='" + base + '\'' +
                ", wind=" + wind +
                ", name='" + name + '\'' +
                ", dt='" + dt + '\'' +
                ", dateTxt='" + dateTxt + '\'' +
                '}';
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public MainDto getMain() {
        return main;
    }

    public void setMain(MainDto main) {
        this.main = main;
    }


    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public WindDto getWind() {
        return wind;
    }

    public void setWind(WindDto wind) {
        this.wind = wind;
    }
}

