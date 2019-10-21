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
import ar.iua.edu.viano.happyWeather.Model.User;


public class LoginFragment extends Fragment {
    private LoginFragmentListener loginFragmentListener;
    private TextView mailTextView;
    private TextView password;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View retView = inflater.inflate(R.layout.fragment_login, container, false);
        TextView goToRegisterFragment = (TextView)retView.findViewById(R.id.lnkRegister);
        mailTextView = (TextView)retView.findViewById(R.id.txtEmail);
        password = (TextView)retView.findViewById(R.id.txtPwd);
        Button doLoginButon = (Button) retView.findViewById(R.id.btnLogin);
        goToRegistFragment(goToRegisterFragment);
        doLogin(doLoginButon);
        return retView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginFragmentListener = (LoginFragmentListener) getActivity();
    }


    public interface LoginFragmentListener{
        void navigateToRegisterScreen();
        void doLogin(User user);

    }
    private void goToRegistFragment(TextView goToRegist){
        goToRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFragmentListener.navigateToRegisterScreen();

            }
        });
    }

    private void doLogin(Button buttonLogin){
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 User user = new User(mailTextView.getText().toString(), password.getText().toString());
                loginFragmentListener.doLogin(user);

            }
        });
    }
}
