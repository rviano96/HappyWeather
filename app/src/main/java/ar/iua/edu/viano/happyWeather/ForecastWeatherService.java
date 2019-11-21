package ar.iua.edu.viano.happyWeather;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import ar.iua.edu.viano.happyWeather.Constants.Constants;
import ar.iua.edu.viano.happyWeather.Model.DTO.WeatherFromApi;

public class ForecastWeatherService /*extends AsyncTask<String, Void, List<WeatherFromApi>> */{
    /*private static Gson gson;
    @Override
    protected List<WeatherFromApi> doInBackground(String... strings) {
        try {
            WeatherFromApi dto;
            dto = weatherFromApi(strings[0]);
            System.out.println("weather " + dto.getListWeather());
            System.out.println("data " + dto.toString());
            return dto;

            /*List<WeatherFromApi> listDto;
            if (strings[1].equals("forecast")){
                listDto = forecastWeather(strings[0], strings[1]) ;
                System.out.println("data " + listDto.toString());
                return listDto.toString();
            }*/

       /* } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*@Override
    protected void onPostExecute(String gitHubRepoList) {
        super.onPostExecute(gitHubRepoList);
    }*/

    /*private InputStream retrieveStream(String position) throws IOException {
        URL url = null;
        try {
            url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" +position+ "&APPID="+ Constants.UID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return new BufferedInputStream(connection.getInputStream());
    }


    private void showData(String data) {
        System.out.println("data: " + data);
    }

    private WeatherFromApi weatherFromApi(String position) throws IOException {

        InputStream source = retrieveStream(position);

        Reader reader = new InputStreamReader(source);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        return gson.fromJson(reader, WeatherFromApi.class);

    }

    /*private List<WeatherFromApi> forecastWeather(String position, String type) throws IOException {

        InputStream source = retrieveStream(position, type);

        Reader reader = new InputStreamReader(source);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        //Object dto = gson.fromJson(reader, Object.class);
        return gson.fromJson(reader, WeatherFromApi.class);

    }*/
}
