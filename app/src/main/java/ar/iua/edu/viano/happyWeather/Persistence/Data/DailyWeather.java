package ar.iua.edu.viano.happyWeather.Persistence.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import ar.iua.edu.viano.happyWeather.Model.Weather;

// Extiende de weather para tener todos los metodos
@Entity(tableName = "DailyWeather")
public class DailyWeather extends Weather {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
