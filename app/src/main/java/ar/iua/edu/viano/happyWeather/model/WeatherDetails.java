package ar.iua.edu.viano.happyWeather.Model;

public class WeatherDetails {
    private String detail1;
    private String detail2;
    /*private String humidity;
    private String riskOfRain;
    private String pressure;
    private String rain;
    private String apparentTemperature; // Sensacion Termica
    private String wind;
    private String visibility;*/

    public WeatherDetails(String detail1, String detail2) {
        /*this.humidity = String.format(context.getString(R.string.humidity),humidity);
        this.riskOfRain = String.format(context.getString(R.string.riskOfRain),riskOfRain);;
        this.pressure = String.format(context.getString(R.string.pressure),pressure);;
        this.rain = String.format(context.getString(R.string.rain),rain);;
        this.apparentTemperature = String.format(context.getString(R.string.apparentTemperature),apparentTemperature);;
        this.wind = String.format(context.getString(R.string.wind),wind);;
        this.visibility = String.format(context.getString(R.string.visibility),visibility);;*/
        this.detail1 = detail1;
        this.detail2 = detail2;
    }

    public String getDetail1() {
        return detail1;
    }

    public void setDetail1(String detail1) {
        this.detail1 = detail1;
    }

    public String getDetail2() {
        return detail2;
    }

    public void setDetail2(String detail2) {
        this.detail2 = detail2;
    }
}
