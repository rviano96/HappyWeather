package ar.iua.edu.viano.happyWeather.UI.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ar.iua.edu.viano.happyWeather.R;
import ar.iua.edu.viano.happyWeather.model.User;


public class RegisterFragment extends Fragment {
    private RegisterFragmentListener registerFragmentListener;
    private TextView mailTextView;
    private TextView password;
    private TextView name;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.fragment_register, container, false);
        TextView goToLoginFragment = (TextView)retView.findViewById(R.id.lnkLogin);
        mailTextView = (TextView)retView.findViewById(R.id.txtEmail);
        password = (TextView)retView.findViewById(R.id.txtPwd);
        name = (TextView)retView.findViewById(R.id.txtName);
        Button doRegisterButton = (Button) retView.findViewById(R.id.btnLogin);
        goToLoginFragment(goToLoginFragment);
        doRegister(doRegisterButton);
        return retView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerFragmentListener = (RegisterFragmentListener) getActivity();
    }


    public interface RegisterFragmentListener{
        void navigateToLoginScreen();
        void doRegister(User user);

    }
    private void goToLoginFragment(TextView goToRegist){
        goToRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerFragmentListener.navigateToLoginScreen();

            }
        });
    }

    private void doRegister(Button buttonRegister){
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(mailTextView.getText().toString(), password.getText().toString(), name.getText().toString());
                registerFragmentListener.doRegister(user);

            }
        });
    }
}
