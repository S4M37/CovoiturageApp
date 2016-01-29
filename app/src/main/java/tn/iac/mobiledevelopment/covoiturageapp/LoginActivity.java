package tn.iac.mobiledevelopment.covoiturageapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    protected ImageView loginButton = null;
    protected ProgressBar progressBar = null;
    protected ImageView imageSuccess = null;
    protected ImageView imageEchec = null;
    protected TextInputLayout mailInput = null ;
    protected TextInputLayout passwordinput = null;
    protected CallbackManager callbackManager=null;
    protected AccessToken accessToken=null;
    protected LoginButton loginFacebook= null;
    protected LoginManager loginManager=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);

        mailInput=(TextInputLayout)findViewById(R.id.layoutLoginEmail);
        passwordinput=(TextInputLayout)findViewById(R.id.layoutLoginePassword);
        loginButton = (ImageView) findViewById(R.id.loginButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        imageSuccess = (ImageView) findViewById(R.id.yes);
        imageEchec = (ImageView) findViewById(R.id.no);

        progressBar.setVisibility(View.GONE);
        imageSuccess.setVisibility(View.GONE);
        imageEchec.setVisibility(View.GONE);

        callbackManager= CallbackManager.Factory.create();
        loginFacebook=(LoginButton)findViewById(R.id.login_button);
        loginManager= LoginManager.getInstance();

        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = AccessToken.getCurrentAccessToken();
                Toast.makeText(getBaseContext(), accessToken.getUserId(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getBaseContext(), "error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
