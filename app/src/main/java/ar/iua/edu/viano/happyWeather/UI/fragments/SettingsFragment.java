package ar.iua.edu.viano.happyWeather.UI.fragments;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

import ar.iua.edu.viano.happyWeather.Constants.Constants;
import ar.iua.edu.viano.happyWeather.Model.User;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.R;


public class SettingsFragment extends Fragment {
    private User user;
    private EditText fullName;
    private EditText password;
    private EditText email;
    private Button save;
    private ImageButton edit;
    private ImageButton picture;
    private TextView sliderText;
    private Switch notificationSwitch;
    private TextView hourText;
    SeekBar seekBar;
    Switch unitSwitch;
    private Bitmap photo = null;
    private boolean editing = false;
    private PreferencesUtils preferencesUtils;
    private EditButtonListener editButtonListener;
    private static final int CAMERA_PERMISSION = 11;
    private static final int WRITE_PERMISSION = 12;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int SELECT_FROM_GALLERY_REQUEST_CODE = 2;
    private File fileImage;
    private Bitmap bitmap;
    private String path;

    //TIME PICKER
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hour = c.get(Calendar.HOUR_OF_DAY);
    final int minute = c.get(Calendar.MINUTE);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.fragment_settings, container, false);
        //user = getArguments().getParcelable("User");//obtengo lo que viene del activity
        fullName = (EditText) retView.findViewById(R.id.txtName);
        email = (EditText) retView.findViewById(R.id.txtEmail);
        password = (EditText) retView.findViewById(R.id.txtPwd);
        save = (Button) retView.findViewById(R.id.btnSave);
        edit = (ImageButton) retView.findViewById(R.id.editButton);
        picture = (ImageButton) retView.findViewById(R.id.picture);
        sliderText = (TextView) retView.findViewById(R.id.sliderText);
        seekBar = (SeekBar) retView.findViewById(R.id.seekBar);
        unitSwitch = (Switch) retView.findViewById(R.id.unitSwitch);
        notificationSwitch = (Switch) retView.findViewById(R.id.notificationSwitch);
        hourText = (TextView) retView.findViewById(R.id.hour);
        takePicture(picture);
        editEnabled(edit);
        save(save);
        preferencesUtils = new PreferencesUtils(getActivity().getApplicationContext());
       /* sliderText.setText("Update weather each : " + preferencesUtils.getUpdateRatio() + " hours");
        seekBar.setProgress(preferencesUtils.getUpdateRatio());
        unitSwitch.setChecked(preferencesUtils.getUnits());
        notificationSwitch.setChecked(preferencesUtils.getNotifications());
        hourText.setText(preferencesUtils.getAlarmTime());
        if(notificationSwitch.isChecked())
            hourText.setVisibility(View.VISIBLE);*/
        updateLayout();
        setTexts();
        return retView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editButtonListener = (EditButtonListener) getActivity();
        unitSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unitSwitch.isChecked()) {
                    preferencesUtils.setUnits(true);
                } else {
                    preferencesUtils.setUnits(false);
                }
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
            }
        });
        notificationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificationSwitch.isChecked()) {
                    getHour();
                    /*preferencesUtils.setNotifications(true);
                    hourText.setVisibility(View.VISIBLE);*/

                } else {
                    editButtonListener.deactivateAlarm();
                    //hourText.setVisibility(View.INVISIBLE);
                    preferencesUtils.setNotifications(false);
                    updateLayout();
                }
            }
        });
    }

    public interface EditButtonListener {
        void saveEdit(User user);
        Bitmap takePicture();
        void activateAlarm(int hour, int minute);
        void deactivateAlarm();
    }

    private void editEnabled(ImageButton editButton) {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si esta editando dejo de editar, de lo contrario habilito la edicion
                if (editing) {
                    fullName.setEnabled(false);
                    email.setEnabled(false);
                    password.setEnabled(false);
                    picture.setClickable(true);
                    save.setVisibility(View.INVISIBLE);
                } else {
                    fullName.setEnabled(true);
                    email.setEnabled(true);
                    password.setEnabled(true);
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
                password.setEnabled(false);
                save.setVisibility(View.INVISIBLE);
                editing = !editing;
                user = new User(email.getText().toString(), password.getText().toString(), fullName.getText().toString());
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

    private void takePicture(ImageButton takePicture) {
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editing) {
                    Context context = getActivity();
                    PackageManager packageManager = context.getPackageManager();
                    if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                        Toast.makeText(getActivity(), "El dispositivo no tiene una camara.", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    } else {
                        dialog();

                    }
                }
            }
        });
    }

    private void setTexts() {
        fullName.setText(preferencesUtils.getUserName());
        email.setText(preferencesUtils.getUserEmail());
        password.setText("genericPsw");
    }

    private void checkWritePermissions() {
        int permissionsWriteCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionsWriteCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
        } else {
            checkCameraPermissions();
        }
    }

    private void checkCameraPermissions() {

        int permissionsCameraCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (permissionsCameraCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        } else {
            callCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case CAMERA_PERMISSION:
                    callCamera();
                    break;
                case WRITE_PERMISSION:
                    checkCameraPermissions();
                    break;
            }
        }
    }

    private void callCamera() {
        File mFile = new File(Environment.getExternalStorageDirectory(), Constants.IMAGE_DIRECTORY);
        boolean isCreated = mFile.exists();
        if (isCreated == false) {
            isCreated = mFile.mkdir();
        }
        if (isCreated) {
            Long timeStamp = System.currentTimeMillis() / 1000; //tiempo en el que se toma la foto
            String name = timeStamp.toString() + ".jpg";//nombre de la foto
            path = Environment.getExternalStorageDirectory() + File.separator + Constants.IMAGE_DIRECTORY
                    + File.separator + name;//indica la ruta donde se va a guardar la foto
            fileImage = new File(path);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImage));
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String s, Uri uri) {
                                Log.i("Path", "" + path);
                                System.out.println(path);
                            }
                        });
                bitmap = BitmapFactory.decodeFile(path);
                picture.setImageBitmap(bitmap);
                break;
            case SELECT_FROM_GALLERY_REQUEST_CODE:
                Toast.makeText(getActivity(), "Select from gallery.", Toast.LENGTH_SHORT)
                        .show();
                Uri miPath = data.getData();
                picture.setImageURI(miPath);
                break;
        }

    }

    private void dialog() {
        final CharSequence[] options = {"Tomar foto", "Elegir de la galeria", "Cancelar"};
        AlertDialog.Builder build = new AlertDialog.Builder(getContext());
        build.setTitle("Elige una opcion");
        build.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        //Toast.makeText(getContext(), "Sacando foto", Toast.LENGTH_LONG).show();
                        checkWritePermissions();
                        callCamera();
                        break;
                    case 1:
                        Toast.makeText(getContext(), "Eligiendo  foto", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        dialogInterface.dismiss();
                        break;
                }
            }
        });
        build.show();
    }
    private void getHour(){
        TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
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

    private void updateLayout(){
        sliderText.setText("Update weather each : " + preferencesUtils.getUpdateRatio() + " hours");
        seekBar.setProgress(preferencesUtils.getUpdateRatio());
        unitSwitch.setChecked(preferencesUtils.getUnits());
        notificationSwitch.setChecked(preferencesUtils.getNotifications());
        hourText.setText(preferencesUtils.getAlarmTime());
        if(notificationSwitch.isChecked())
            hourText.setVisibility(View.VISIBLE);
    }
}
