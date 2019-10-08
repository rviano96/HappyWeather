package ar.iua.edu.viano.happyWeather.UI.recyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ar.iua.edu.viano.happyWeather.R;

public class WeatherDetailsHolder  extends RecyclerView.ViewHolder{
    private TextView detail1;
    private TextView detail2;

    public WeatherDetailsHolder(@NonNull View itemView) {
        super(itemView);
        detail1 = (TextView) itemView.findViewById(R.id.detail1);
        detail2 = (TextView) itemView.findViewById(R.id.detail2);
    }

    public TextView getdetail1() {
        return detail1;
    }

    public TextView getdetail2() {
        return detail2;
    }

}

