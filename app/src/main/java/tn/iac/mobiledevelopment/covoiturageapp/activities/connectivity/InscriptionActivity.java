package tn.iac.mobiledevelopment.covoiturageapp.activities.connectivity;

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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import tn.iac.mobiledevelopment.covoiturageapp.R;
import tn.iac.mobiledevelopment.covoiturageapp.activities.MainActivity;
import tn.iac.mobiledevelopment.covoiturageapp.models.User;
import tn.iac.mobiledevelopment.covoiturageapp.network.GitHubService;
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

    protected ProgressBar progressBar = null;
    protected ImageView imageSuccess = null;
    protected ImageView imageEchec = null;

    protected CallbackManager callbackManager = null;
    protected AccessToken accessToken = null;
    protected LoginButton loginFacebook = null;
    protected LoginManager loginManager = null;
    protected GoogleApiClient mGoogleApiClient;
    protected SignInButton signInButtonGoogle;

    protected GitHubService gitHubService;

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

        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        imageSuccess = (ImageView) findViewById(R.id.yes);
        imageEchec = (ImageView) findViewById(R.id.no);

        progressBar.setVisibility(View.GONE);
        imageSuccess.setVisibility(View.GONE);
        imageEchec.setVisibility(View.GONE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GitHubService.baseUrl)
                .build();

        gitHubService = retrofit.create(GitHubService.class);

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
                    hideButton();
                    mdperreur();
                } else if (!validateInput(lastNameValue)) {
                    lastName.setError("prénom non valide !");
                    lastName.requestFocus();
                    mdperreur();
                } else if (!validateEmail(emailValue)) {
                    email.setError("Email non valide !");
                    email.requestFocus();
                    hideButton();
                    mdperreur();
                } else if (!validateEmail(emailConfirmedValue)) {
                    emailConfirmed.setError("Email non valide !");
                    emailConfirmed.requestFocus();
                    hideButton();
                    mdperreur();
                } else if (!(emailValue.equals(emailConfirmedValue))) {
                    email.setError("Les deux e-mails ne sont pas identiques");
                    emailConfirmed.setError("Les deux e-mails ne sont pas identiques");
                    emailConfirmed.requestFocus();
                    hideButton();
                    mdperreur();
                } else if (!validatePassword(passwordValue)) {
                    password.setError("Mot de passe non valide !");
                    password.requestFocus();
                    hideButton();
                    mdperreur();
                } else if (!validateInput(passwordConfirmedValue)) {
                    passwordConfirmed.setError("Mot de passe non valide !");
                    passwordConfirmed.requestFocus();
                    hideButton();
                    mdperreur();
                } else if (!(passwordValue.equals(passwordConfirmedValue))) {
                    password.setError("Les deux mots de passe ne sont pas identiques");
                    passwordConfirmed.setError("Les deux mots de passe ne sont pas identiques");
                    passwordConfirmed.requestFocus();
                    hideButton();
                    mdperreur();
                } else {
                    hideKeyboard();
                    doInscri(firstNameValue, lastNameValue, emailValue, passwordValue);
                }
            }
        });

        //facebook login

        callbackManager = CallbackManager.Factory.create();
        loginFacebook = (LoginButton) findViewById(R.id.login_button);
        loginManager = LoginManager.getInstance();
        loginFacebook.setReadPermissions(readPermissions);
        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideButton();
            }
        });
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
                                Log.v("LoginActivity", response.toString());
                                try {
                                    User user = new User(object.get("id").toString(), object.get("email").toString(), object.get("name").toString());
                                    AuthUtils.saveUser(InscriptionActivity.this, user);
                                    succ();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getBaseContext(), "error,retry again", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                mdperreur();
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

        signInButtonGoogle = (SignInButton) findViewById(R.id.sign_in_button);
        signInButtonGoogle.setSize(SignInButton.SIZE_STANDARD);
        signInButtonGoogle.setScopes(gso.getScopeArray());

        signInButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                hideButton();
            }
        });
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() >= 6;
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

    private void doInscri(String firstNameValue, String lastNameValue, String emailValue, String passwordValue) {
        hideButton();
        progressBar.setVisibility(View.VISIBLE);
        //github sign in
        Call<ResponseBody> call = gitHubService.storeUser(firstNameValue, lastNameValue, emailValue, passwordValue);
        Log.d("user", firstNameValue + " " + lastNameValue + " " + emailValue + " " + passwordValue);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                JSONObject jsonObject = null;
                User user = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String result = jsonObject.getString("success");
                    if (result.equals("User exist")) {
                        email.setError("User email exist !");
                        emailConfirmed.getEditText().setText("");
                        email.requestFocus();
                        mdperreur();
                    } else {
                        JSONObject userJson = jsonObject.getJSONObject("user");
                        user = new User(userJson.getString("id_User"), userJson.getString("login"), userJson.getString("nom") + " " + userJson.getString("prenom"));
                        AuthUtils.saveUser(InscriptionActivity.this, user);
                        succ();
                    }
                } catch (JSONException | IOException | NullPointerException e) {
                    e.printStackTrace();
                    mdperreur();
                }
                //AuthUtils.saveToken(InscriptionActivity.this, token);
                AuthUtils.saveUser(InscriptionActivity.this, user);
                Log.d("success", "last one");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(InscriptionActivity.this, "Unable to conenect", Toast.LENGTH_SHORT).show();
                mdperreur();
            }
        });
    }

    public void mdperreur() {
        imageEchec.setVisibility(View.VISIBLE);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        showButton();
                        imageEchec.setVisibility(View.GONE);
                    }
                },
                2000);
    }

    public void succ() {
        progressBar.setVisibility(View.GONE);
        imageSuccess.setVisibility(View.VISIBLE);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent intent = new Intent(InscriptionActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                },
                1000);
    }

    void hideButton() {
        inscrptionButton.setVisibility(View.GONE);
        loginFacebook.setVisibility(View.GONE);
        signInButtonGoogle.setVisibility(View.GONE);
    }

    void showButton() {
        inscrptionButton.setVisibility(View.VISIBLE);
        loginFacebook.setVisibility(View.VISIBLE);
        signInButtonGoogle.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            succ();
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
            AuthUtils.saveUser(this, user);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "failed to connect", Toast.LENGTH_SHORT).show();
        mdperreur();
    }
}
