package ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherForecast;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.iua.edu.viano.happyWeather.Persistence.Data.WeatherForecast;

@Dao
public interface WeatherForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeatherForecast weather);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWeatherForecas(WeatherForecast weather);

    @Query("DELETE FROM WeatherForecast")
    void deleteAll();

    @Query("SELECT * FROM WeatherForecast ORDER BY id ASC")
    List<WeatherForecast> getAllDaysOrdered();

    @Query("SELECT * FROM WeatherForecast WHERE dow == :dayName")
    WeatherForecast getDayByName(String dayName);
}