package ar.iua.edu.viano.happyWeather.Persistence.Database.DailyWeather;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ar.iua.edu.viano.happyWeather.Persistence.Data.DailyWeather;

@Dao
public interface DailyWeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DailyWeather weather);

    @Query("DELETE FROM DailyWeather")
    void deleteAll();

    @Query("SELECT * FROM DailyWeather ORDER BY id ASC")
    List<DailyWeather> getAllDaysOrdered();

    @Query("SELECT * FROM DailyWeather WHERE dow == :dayName")
    DailyWeather getDayByName(String dayName);
}