package tn.iac.mobiledevelopment.covoiturageapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG_USER_ID = "TAG_USER_ID";
    protected ImageView loginButton = null;
    protected ProgressBar progressBar = null;
    protected ImageView imageSuccess = null;
    protected ImageView imageEchec = null;
    protected ImageView inscButton = null;
    protected TextInputLayout mailInput = null;
    protected TextInputLayout passwordinput = null;
    protected CallbackManager callbackManager = null;
    protected AccessToken accessToken = null;
    protected LoginButton loginFacebook = null;
    protected LoginManager loginManager = null;
    private String user_Id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);

        mailInput = (TextInputLayout) findViewById(R.id.layoutLoginEmail);
        passwordinput = (TextInputLayout) findViewById(R.id.layoutLoginePassword);
        loginButton = (ImageView) findViewById(R.id.loginButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        imageSuccess = (ImageView) findViewById(R.id.yes);
        imageEchec = (ImageView) findViewById(R.id.no);
        inscButton = (ImageView) findViewById(R.id.inscButton);

        inscButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, InscriptionActivity.class);
                startActivity(intent);
            }
        });
        progressBar.setVisibility(View.GONE);
        imageSuccess.setVisibility(View.GONE);
        imageEchec.setVisibility(View.GONE);

        callbackManager = CallbackManager.Factory.create();
        loginFacebook = (LoginButton) findViewById(R.id.login_button);
        loginManager = LoginManager.getInstance();

        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = AccessToken.getCurrentAccessToken();
                user_Id = accessToken.getUserId();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(TAG_USER_ID, user_Id);
                startActivity(intent);
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
