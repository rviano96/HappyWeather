package ar.iua.edu.viano.happyWeather.UI.fragments;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;

import ar.iua.edu.viano.happyWeather.Model.User;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.R;


public class SettingsFragment extends Fragment {
    private User user;
    private EditText fullName;
    private EditText email;
    private Button save;
    private ImageButton edit;
    private ImageButton picture;
    private TextView sliderText;
    private Switch notificationSwitch;
    private TextView hourText;
    SeekBar seekBar;
    private Bitmap photo = null;
    private boolean editing = false;
    private PreferencesUtils preferencesUtils;
    private EditButtonListener editButtonListener;
    //TIME PICKER
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private RadioGroup radioGroup;
    private RadioButton radioButtonMetric;
    private RadioButton radioButtonStandard;
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la hora hora
    final int hour = c.get(Calendar.HOUR_OF_DAY);
    final int minute = c.get(Calendar.MINUTE);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.fragment_settings, container, false);
        fullName =  retView.findViewById(R.id.txtName);
        email = retView.findViewById(R.id.txtEmail);
        save =  retView.findViewById(R.id.btnSave);
        edit =  retView.findViewById(R.id.editButton);
        picture =  retView.findViewById(R.id.picture);
        sliderText =  retView.findViewById(R.id.sliderText);
        seekBar =  retView.findViewById(R.id.seekBar);
        notificationSwitch =  retView.findViewById(R.id.notificationSwitch);
        hourText =  retView.findViewById(R.id.hour);
        radioGroup =  retView.findViewById(R.id.radioGroup);
        radioButtonMetric =  retView.findViewById(R.id.radioMetric);
        radioButtonStandard =  retView.findViewById(R.id.radioStandard);
        takePicture(picture);
        editEnabled(edit);
        save(save);
        preferencesUtils = new PreferencesUtils(getActivity().getApplicationContext());
        if (!preferencesUtils.getUserPictureLocale().equals("")) {
            try {
                picture.setImageBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.parse(preferencesUtils.getUserPictureLocale())));
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error cargando la foto", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            if(!preferencesUtils.getUserPicture().equals("")){
                Picasso.get().load(preferencesUtils.getUserPicture()).into(picture);
            }

        }

        updateLayout();
        setTexts();
        return retView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editButtonListener = (EditButtonListener) getActivity();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //Log.d("radiogroup", String.valueOf(i));
                RadioButton checkedRadioButton = radioGroup.findViewById(i);
                if(checkedRadioButton.getText().equals(getResources().getString(R.string.metric))){
                    preferencesUtils.setUnits(true);
                }
                else{
                    if(checkedRadioButton.getText().equals(getResources().getString(R.string.standard))){
                        preferencesUtils.setUnits(false);
                    }
                }
                Log.d("unit", String.valueOf(preferencesUtils.getUnits()));
            }

        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == 0) {
                    i = 1;
                    seekBar.setProgress(i);
                }
                sliderText.setText("Update weather each : " + i + " hours");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                preferencesUtils.setUpdateRatio(seekBar.getProgress());
                editButtonListener.updateWeather(seekBar.getProgress());
            }
        });
        notificationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificationSwitch.isChecked()) {
                    getHour();
                } else {
                    editButtonListener.deactivateAlarm();

                    preferencesUtils.setNotifications(false);
                    updateLayout();
                }
            }
        });
    }

    public interface EditButtonListener {
        void saveEdit(User user);

        void takePicture(ImageButton image);

        void activateAlarm(int hour, int minute);

        void deactivateAlarm();

        void updateWeather(int hour);
    }

    private void editEnabled(ImageButton editButton) {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si esta editando dejo de editar, de lo contrario habilito la edicion
                if (editing) {
                    fullName.setEnabled(false);
                    email.setEnabled(false);
                    picture.setClickable(true);
                    save.setVisibility(View.INVISIBLE);
                } else {
                    fullName.setEnabled(true);
                    email.setEnabled(true);
                    save.setVisibility(View.VISIBLE);
                }
                editing = !editing;
            }
        });
    }

    private void save(Button saveButton) {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si esta editando dejo de editar, de lo contrario habilito la edicion
                fullName.setEnabled(false);
                email.setEnabled(false);
                save.setVisibility(View.INVISIBLE);
                editing = !editing;
                user = new User(email.getText().toString(),"" ,fullName.getText().toString());
                preferencesUtils.setUserEmail(email.getText().toString());
                preferencesUtils.setUserName(fullName.getText().toString());
                editButtonListener.saveEdit(user);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (photo != null) {
            picture.setImageBitmap(photo);
        }
    }

    private void takePicture(final ImageButton takePicture) {
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editing) {
                    editButtonListener.takePicture(takePicture);
                }
            }
        });
    }

    private void setTexts() {
        fullName.setText(preferencesUtils.getUserName());
        email.setText(preferencesUtils.getUserEmail());

    }


    private void getHour() {
        TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if (hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                preferencesUtils.setNotifications(true);
                preferencesUtils.setAlarmTime(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                updateLayout();
                editButtonListener.activateAlarm(hourOfDay, minute);
            }


            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hour, minute, false);
        recogerHora.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    preferencesUtils.setNotifications(false);
                    updateLayout();
                }
            }
        });
        recogerHora.show();
    }

    private void updateLayout() {
        sliderText.setText("Update weather each : " + preferencesUtils.getUpdateRatio() + " hours");
        seekBar.setProgress(preferencesUtils.getUpdateRatio());
        // unitSwitch.setChecked(preferencesUtils.getUnits());
        if(preferencesUtils.getUnits()){
            radioButtonStandard.setChecked(false);
            radioButtonMetric.setChecked(true);
        }else{
            radioButtonStandard.setChecked(true);
            radioButtonMetric.setChecked(false);
        }
        notificationSwitch.setChecked(preferencesUtils.getNotifications());
        hourText.setText(preferencesUtils.getAlarmTime());
        if (notificationSwitch.isChecked())
            hourText.setVisibility(View.VISIBLE);
    }
}
