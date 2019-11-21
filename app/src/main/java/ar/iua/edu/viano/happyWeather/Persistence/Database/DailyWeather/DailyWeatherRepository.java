package ar.iua.edu.viano.happyWeather.Persistence.Database.DailyWeather;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ar.iua.edu.viano.happyWeather.Persistence.Data.Weather;
import ar.iua.edu.viano.happyWeather.Persistence.Database.WeatherRoomDatabase;

public class DailyWeatherRepository {
    private DailyWeatherDao dailyWeatherDao;
    private List<Weather> allHours;

    public DailyWeatherRepository(Application application) {
        WeatherRoomDatabase database = WeatherRoomDatabase.getDatabase(application);
        dailyWeatherDao = database.dailyWeatherDao();
    }

    public List<Weather> getAllHours() {
        try {
            allHours = new getDaysAsyncTask(dailyWeatherDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allHours;
    }

    public void delete(){
        new deleteDailyRepository(dailyWeatherDao).execute();
    }
    public void insert(Weather weather) {
        new insterWeatherDialy(dailyWeatherDao).execute(weather);
    }

    public void updateWeather(Weather weather) {
        new updateWeather(dailyWeatherDao).execute(weather);
    }

    private static class insterWeatherDialy extends AsyncTask<Weather, Void, Void> {

        private DailyWeatherDao asyncTaskDailyWeatherDao;

        insterWeatherDialy(DailyWeatherDao dailyWeatherDao) {
            asyncTaskDailyWeatherDao = dailyWeatherDao;
        }

        @Override
        protected Void doInBackground(Weather... weathers) {
            asyncTaskDailyWeatherDao.insert(weathers[0]);
            return null;
        }
    }

    private static class updateWeather extends AsyncTask<Weather, Void, Void> {

        private DailyWeatherDao asyncTaskDailyWeatherDao;

        updateWeather(DailyWeatherDao dailyWeatherDao) {
            asyncTaskDailyWeatherDao = dailyWeatherDao;
        }

        @Override
        protected Void doInBackground(Weather... weathers) {
            asyncTaskDailyWeatherDao.updateWeather(weathers[0]);
            return null;
        }
    }

    private static class deleteDailyRepository extends AsyncTask<Void, Void, Void> {

        private DailyWeatherDao asyncTaskDailyWeatherDao;

        deleteDailyRepository(DailyWeatherDao dailyWeatherDao) {
            asyncTaskDailyWeatherDao = dailyWeatherDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDailyWeatherDao.deleteAll();
            return null;
        }
    }
    private static class getDaysAsyncTask extends AsyncTask<Void, Void, List<Weather>> {
        private DailyWeatherDao asyncTaskDailyWeatherDao;

        getDaysAsyncTask(DailyWeatherDao dailyWeatherDao) {
            asyncTaskDailyWeatherDao = dailyWeatherDao;
        }

        @Override
        protected List<Weather> doInBackground(Void... voids) {
            return asyncTaskDailyWeatherDao.getAllDaysOrdered();
        }
    }
}
