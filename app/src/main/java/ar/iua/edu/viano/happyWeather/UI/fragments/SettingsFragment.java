package ar.iua.edu.viano.happyWeather.UI.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.Model.User;


public class SettingsFragment extends Fragment {
    private User user;
    private EditText fullName;
    private EditText password;
    private EditText email;
    private Button  save;
    private ImageButton edit;
    private ImageButton picture;
    private TextView sliderText;
    SeekBar seekBar;
    Switch unitSwitch;
    private Bitmap photo= null;
    private boolean editing = false;
    private PreferencesUtils preferencesUtils;
    private EditButtonListener editButtonListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View retView = inflater.inflate(R.layout.fragment_settings, container, false);
        //user = getArguments().getParcelable("User");//obtengo lo que viene del activity
        fullName = (EditText) retView.findViewById(R.id.txtName);
        email = (EditText) retView.findViewById(R.id.txtEmail);
        password = (EditText)retView.findViewById(R.id.txtPwd);
        save = (Button) retView.findViewById(R.id.btnSave);
        edit = (ImageButton) retView.findViewById(R.id.editButton);
        picture = (ImageButton) retView.findViewById(R.id.picture);
        sliderText = (TextView) retView.findViewById(R.id.sliderText);
        seekBar = (SeekBar) retView.findViewById(R.id.seekBar);
        unitSwitch = (Switch) retView.findViewById(R.id.unitSwitch);
        takePicture(picture);
        editEnabled(edit);
        save(save);
        preferencesUtils = new PreferencesUtils(getActivity().getApplicationContext());
        sliderText.setText("Update weather each : " + preferencesUtils.getUpdateRatio() + " hours");
        seekBar.setProgress(preferencesUtils.getUpdateRatio());
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
                if(unitSwitch.isChecked()){
                    preferencesUtils.setUnits((String) unitSwitch.getTextOn());
                }else{
                    preferencesUtils.setUnits((String) unitSwitch.getTextOff());
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if( i == 0){
                    i=1;
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
    }
    public interface EditButtonListener{
        void saveEdit(User user);
        Bitmap takePicture();
    }

    private void editEnabled(ImageButton editButton){
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si esta editando dejo de editar, de lo contrario habilito la edicion
                if(editing){
                    fullName.setEnabled(false);
                    email.setEnabled(false);
                    password.setEnabled(false);
                    picture.setClickable(true);
                    save.setVisibility(View.INVISIBLE);
                }else{
                    fullName.setEnabled(true);
                    email.setEnabled(true);
                    password.setEnabled(true);
                    save.setVisibility(View.VISIBLE);
                }
                editing = !editing;
            }
        });
    }

    private void save(Button saveButton){
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
        if(photo!=null){
            picture.setImageBitmap(photo);
        }
    }

    private void takePicture(ImageButton takePicture){
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editing)
                    photo = editButtonListener.takePicture();
            }
        });
    }
    private void setTexts(){
        fullName.setText(preferencesUtils.getUserName());
        email.setText(preferencesUtils.getUserEmail());
        password.setText("genericPsw");
    }


}
