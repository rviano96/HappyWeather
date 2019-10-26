package ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherForecast;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ar.iua.edu.viano.happyWeather.Persistence.Data.WeatherForecast;

@Dao
public interface WeatherForecastDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(WeatherForecast weather);

    @Query("DELETE FROM WeatherForecast")
    void deleteAll();

    @Query("SELECT * FROM WeatherForecast ORDER BY id ASC")
    List<WeatherForecast> getAllDaysOrdered();

    @Query("SELECT * FROM WeatherForecast WHERE dow == :dayName")
    WeatherForecast getDayByName(String dayName);
}