package ar.iua.edu.viano.happyWeather.UI.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import ar.iua.edu.viano.happyWeather.Model.User;
import ar.iua.edu.viano.happyWeather.Preferences.PreferencesUtils;
import ar.iua.edu.viano.happyWeather.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class LoginFragment extends Fragment {
    private LoginFragmentListener loginFragmentListener;
    private TextView usernameTextView;
    private TextView password;
    private LoginButton loginButton;
    private CircleImageView circleImageView;
    private CallbackManager callbackManager;
    private PreferencesUtils preferencesUtils;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //FacebookSdk.sdkInitialize(getContext());
        View retView = inflater.inflate(R.layout.fragment_login, container, false);
        TextView goToRegisterFragment = (TextView)retView.findViewById(R.id.lnkRegister);
        usernameTextView = (TextView)retView.findViewById(R.id.txtUsername);
        password = (TextView)retView.findViewById(R.id.txtPwd);
        loginButton = retView.findViewById(R.id.login_button);
        Button doLoginButon = (Button) retView.findViewById(R.id.btnLogin);
        preferencesUtils = new PreferencesUtils(getContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                    Log.d("login facebook", "ok");
            }

            @Override
            public void onCancel() {
                Log.d("login facebook", "cancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("login facebook", "error");

            }
        });
        goToRegistFragment(goToRegisterFragment);
        doLogin(doLoginButon);
        return retView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null){
                preferencesUtils.setUserPicture("");
                preferencesUtils.setUserName("");
                preferencesUtils.setUserEmail("");
                preferencesUtils.setUserIsLoggedIn(false);
                preferencesUtils.setUserPicture("");
                Toast.makeText(getContext(), "User Is logged out", Toast.LENGTH_LONG).show();
            }else{
                loadUserProfile(currentAccessToken);
            }
        }
    };
    private void loadUserProfile(AccessToken newAccesToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccesToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String image_url = "https://graph.facebook.com/"+id+"/picture?type=normal";
                    preferencesUtils.setUserPicture(image_url);
                    preferencesUtils.setUserName(first_name + " " + last_name);
                    preferencesUtils.setUserEmail(email);
                    preferencesUtils.setUserIsLoggedIn(true);
                    preferencesUtils.setUserPicture(image_url);
                    RequestOptions requestOptions = new RequestOptions();
                    //requestOptions.dontAnimate();
                    // Glide.with( )
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name, last_name, email, id");
        request.setParameters(parameters);
        request.executeAsync();
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
                 User user = new User();
                 user.setPsw(password.getText().toString());
                 user.setUsername(usernameTextView.getText().toString());
                loginFragmentListener.doLogin(user);

            }
        });
    }
}
