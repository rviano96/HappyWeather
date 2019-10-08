package ar.iua.edu.viano.happyWeather.UI.recyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ar.iua.edu.viano.happyWeather.R;

public class WeatherForecastHolder extends RecyclerView.ViewHolder {

    private ImageView iconView;
    private TextView DOW;
    private TextView maxTemp;
    private TextView minTemp;

    public WeatherForecastHolder(@NonNull View itemView) {
        super(itemView);
        iconView = (ImageView) itemView.findViewById(R.id.iconView);
        DOW = (TextView) itemView.findViewById(R.id.DOW);
        maxTemp = (TextView) itemView.findViewById(R.id.maxTemp);
        minTemp = (TextView) itemView.findViewById(R.id.minTemp);
    }

    public TextView getDOW() {
        return DOW;
    }

    public TextView getmaxTemp() {
        return maxTemp;
    }

    public TextView getminTemp() {
        return minTemp;
    }

    public ImageView geticonView() {
        return iconView;
    }
}
