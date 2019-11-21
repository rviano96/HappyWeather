package ar.iua.edu.viano.happyWeather.Persistence.Database.DailyWeather;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ar.iua.edu.viano.happyWeather.Persistence.Data.Weather;

@Dao
public interface DailyWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Weather weather);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWeather(Weather weather);

    @Query("DELETE FROM Weather")
    void deleteAll();

    @Query("SELECT * FROM Weather ORDER BY id ASC")
    List<Weather> getAllDaysOrdered();

    @Query("SELECT * FROM Weather WHERE dow == :dayName")
    Weather getDayByName(String dayName);
}