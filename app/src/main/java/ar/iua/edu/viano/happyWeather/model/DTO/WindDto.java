package ar.iua.edu.viano.happyWeather.Model.DTO;

import com.google.gson.annotations.SerializedName;

public class WindDto {
    @SerializedName("speed")
    private String speed;

    public WindDto(String speed) {
        this.speed = speed;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "WindDto{" +
                "speed='" + speed + '\'' +
                '}';
    }
}
