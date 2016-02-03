package tn.iac.mobiledevelopment.covoiturageapp.connectivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tn.iac.mobiledevelopment.covoiturageapp.R;
import tn.iac.mobiledevelopment.covoiturageapp.connectivity.model.User;
import tn.iac.mobiledevelopment.covoiturageapp.home.MainActivity;
import tn.iac.mobiledevelopment.covoiturageapp.utils.AuthUtils;
import tn.iac.mobiledevelopment.covoiturageapp.utils.remplaceFont;

public class InscriptionActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG_USER_ID = "TAG_USER_ID";
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    private static final String[] readPermissions = {"email"};
    protected static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    protected TextInputLayout firstName = null;
    protected TextInputLayout lastName = null;
    protected TextInputLayout email = null;
    protected TextInputLayout emailConfirmed = null;
    protected TextInputLayout password = null;
    protected TextInputLayout passwordConfirmed = null;
    protected ImageView inscrptionButton = null;

    protected CallbackManager callbackManager = null;
    protected AccessToken accessToken = null;
    protected LoginButton loginFacebook = null;
    protected LoginManager loginManager = null;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        remplaceFont.replaceDefaultFont(this, "DEFAULT", "Exo-Medium.otf");
        remplaceFont.replaceDefaultFont(this, "SANS", "Roboto-Light.ttf");
        remplaceFont.replaceDefaultFont(this, "SERIF", "Roboto-Light.ttf");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_inscription);

        firstName = (TextInputLayout) findViewById(R.id.layoutFirstName);
        lastName = (TextInputLayout) findViewById(R.id.layoutLastName);
        email = (TextInputLayout) findViewById(R.id.layoutEmail);
        emailConfirmed = (TextInputLayout) findViewById(R.id.layoutEmailConfirmed);
        password = (TextInputLayout) findViewById(R.id.layoutPassword);
        passwordConfirmed = (TextInputLayout) findViewById(R.id.layoutPasswordConfirmed);
        inscrptionButton = (ImageView) findViewById(R.id.inscrptionButton);

        if (Build.VERSION.SDK_INT >= 21) {
            Slide slide = new Slide();
            slide.setDuration(400);
            getWindow().setEnterTransition(slide);
            getWindow().setReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_a));
        }

        inscrptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameValue = firstName.getEditText().getText().toString();
                String lastNameValue = lastName.getEditText().getText().toString();
                String emailValue = email.getEditText().getText().toString();
                String emailConfirmedValue = emailConfirmed.getEditText().getText().toString();
                String passwordValue = password.getEditText().getText().toString();
                String passwordConfirmedValue = passwordConfirmed.getEditText().getText().toString();

                firstName.setErrorEnabled(false);
                firstName.requestFocus();
                lastName.setErrorEnabled(false);
                lastName.requestFocus();
                email.setErrorEnabled(false);
                email.requestFocus();
                emailConfirmed.setErrorEnabled(false);
                emailConfirmed.requestFocus();
                password.setErrorEnabled(false);
                password.requestFocus();
                passwordConfirmed.setErrorEnabled(false);
                passwordConfirmed.requestFocus();

                if (!validateInput(firstNameValue)) {
                    firstName.setError("nom non valide !");
                    firstName.requestFocus();

                } else if (!validateInput(lastNameValue)) {
                    lastName.setError("prénom non valide !");
                    lastName.requestFocus();
                } else if (!validateEmail(emailValue)) {
                    email.setError("Email non valide !");
                    email.requestFocus();
                } else if (!validateEmail(emailConfirmedValue)) {
                    emailConfirmed.setError("Email non valide !");
                    emailConfirmed.requestFocus();
                } else if (!(emailValue.equals(emailConfirmedValue))) {
                    email.setError("Les deux e-mails ne sont pas identiques");
                    emailConfirmed.setError("Les deux e-mails ne sont pas identiques");
                    emailConfirmed.requestFocus();
                } else if (!validatePassword(passwordValue)) {
                    password.setError("Mot de passe non valide !");
                    password.requestFocus();
                } else if (!validateInput(passwordConfirmedValue)) {
                    passwordConfirmed.setError("Mot de passe non valide !");
                    passwordConfirmed.requestFocus();
                } else if (!(passwordValue.equals(passwordConfirmedValue))) {
                    password.setError("Les deux mots de passe ne sont pas identiques");
                    passwordConfirmed.setError("Les deux mots de passe ne sont pas identiques");
                    passwordConfirmed.requestFocus();
                } else {
                    hideKeyboard();
                    doInscri();
                }
            }
        });

        //facebook login

        callbackManager = CallbackManager.Factory.create();
        loginFacebook = (LoginButton) findViewById(R.id.login_button);
        loginManager = LoginManager.getInstance();
        loginFacebook.setReadPermissions(readPermissions);

        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = AccessToken.getCurrentAccessToken();
                //Log.d("user facebook",loginResult.toString());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.v("LoginActivity", response.toString());
                                try {
                                    User user = new User(object.get("id").toString(), object.get("email").toString(), object.get("name").toString());
                                    Intent intent = new Intent(InscriptionActivity.this, MainActivity.class);
                                    intent.putExtra("user", user);
                                    AuthUtils.saveUser(InscriptionActivity.this, user);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
                //User user=new (accessToken.getUserId(),loginResult);
                ///Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                //startActivity(intent);
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
        //google+ login

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButtonGoogle = (SignInButton) findViewById(R.id.sign_in_button);
        signInButtonGoogle.setSize(SignInButton.SIZE_STANDARD);
        signInButtonGoogle.setScopes(gso.getScopeArray());

        signInButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() > 6;
    }

    public boolean validateInput(String input) {
        return input.length() > 3;
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void doInscri() {
        Intent intent = new Intent(InscriptionActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            User user = new User(acct.getId(), acct.getEmail(), acct.getDisplayName());
            Intent intent = new Intent(InscriptionActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            AuthUtils.saveUser(InscriptionActivity.this, user);
            startActivity(intent);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "failed to connect", Toast.LENGTH_SHORT).show();
    }

}
