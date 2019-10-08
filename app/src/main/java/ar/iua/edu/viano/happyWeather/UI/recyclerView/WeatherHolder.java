package ar.iua.edu.viano.happyWeather.UI.recyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ar.iua.edu.viano.happyWeather.R;

public class WeatherHolder extends RecyclerView.ViewHolder {

    private ImageView iconView;
    private TextView time;
    private TextView actualTemp;

    public WeatherHolder(@NonNull View itemView) {
        super(itemView);
        iconView = (ImageView) itemView.findViewById(R.id.iconView);
        time = (TextView) itemView.findViewById(R.id.time);
        actualTemp = (TextView) itemView.findViewById(R.id.actualTemp);
    }



    public TextView gettime() {
        return time;
    }

    public TextView getactualTemp() {
        return actualTemp;
    }

    public ImageView geticonView() {
        return iconView;
    }
}
