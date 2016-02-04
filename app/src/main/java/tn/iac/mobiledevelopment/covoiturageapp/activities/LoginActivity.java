package tn.iac.mobiledevelopment.covoiturageapp.activities;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;
import tn.iac.mobiledevelopment.covoiturageapp.R;
import tn.iac.mobiledevelopment.covoiturageapp.models.User;
import tn.iac.mobiledevelopment.covoiturageapp.network.GithubService;
import tn.iac.mobiledevelopment.covoiturageapp.utils.AuthUtils;
import tn.iac.mobiledevelopment.covoiturageapp.utils.remplaceFont;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG_USER_ID = "TAG_USER_ID";
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    private static final String[] readPermissions = {"email"};
    protected ImageView loginButton = null;
    protected ProgressBar progressBar = null;
    protected ImageView imageSuccess = null;
    protected ImageView imageEchec = null;
    protected TextInputLayout emailInput = null;
    protected TextInputLayout passwordInput = null;
    protected CallbackManager callbackManager = null;
    protected AccessToken accessToken = null;
    protected LoginButton loginFacebook = null;
    protected LoginManager loginManager = null;
    private GoogleApiClient mGoogleApiClient;
    protected GithubService githubService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        remplaceFont.replaceDefaultFont(this, "DEFAULT", "Exo-Medium.otf");
        remplaceFont.replaceDefaultFont(this, "SANS", "Roboto-Light.ttf");
        remplaceFont.replaceDefaultFont(this, "SERIF", "Roboto-Light.ttf");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);

        emailInput = (TextInputLayout) findViewById(R.id.layoutLoginEmail);
        passwordInput = (TextInputLayout) findViewById(R.id.layoutLoginePassword);
        loginButton = (ImageView) findViewById(R.id.loginButton);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        imageSuccess = (ImageView) findViewById(R.id.yes);
        imageEchec = (ImageView) findViewById(R.id.no);

        githubService = new RestAdapter.Builder()
                .setEndpoint(GithubService.ENDPOINT)
                .build()
                .create(GithubService.class);

        if (Build.VERSION.SDK_INT >= 21) {
            //   getWindow().setEnterTransition(TransitionInflater.from(Login.this).inflateTransition(R.transition.shared_element_a));
            Slide slide = new Slide();
            slide.setDuration(400);
            getWindow().setEnterTransition(slide);
            getWindow().setReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_a));
        }

        progressBar.setVisibility(View.GONE);
        imageSuccess.setVisibility(View.GONE);
        imageEchec.setVisibility(View.GONE);

        //normal login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailtext = emailInput.getEditText().getText().toString();
                String passtext = passwordInput.getEditText().getText().toString();

                emailInput.setErrorEnabled(false);
                emailInput.requestFocus();
                passwordInput.setErrorEnabled(false);
                passwordInput.requestFocus();

                if (!validateEmail(emailtext)) {
                    emailInput.setError("Email non valide !");
                    emailInput.requestFocus();
                    passwordInput.setErrorEnabled(false);

                } else if (!validatePassword(passtext)) {
                    passwordInput.setError("Mot de passe non valide !");
                    passwordInput.requestFocus();
                    emailInput.setErrorEnabled(false);
                    emailInput.getEditText().setHintTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    emailInput.setErrorEnabled(false);
                    passwordInput.setErrorEnabled(false);
                    hideKeyboard();
                    signin(emailtext, passtext, new Callback<String>() {
                        @Override
                        public void success(String s, Response response) {
                            JSONObject jsonObject = null;
                            String token = null;
                            User user = null;
                            try {
                                jsonObject = new JSONObject(s);
                                token = jsonObject.getString("token");
                                JSONObject userJson = jsonObject.getJSONArray("user").getJSONObject(0);
                                user = new User("", userJson.getString("login"), userJson.getString("nom") + " " + userJson.getString("prenom"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            AuthUtils.saveToken(LoginActivity.this, token);
                            AuthUtils.saveUser(LoginActivity.this, user);
                            Log.d("success", "last one");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(LoginActivity.this, "Unable to conenect", Toast.LENGTH_LONG).show();
                        }
                    });
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
                                    //Toast.makeText(LoginActivity.this,user.toString(),Toast.LENGTH_SHORT).show();
                                    AuthUtils.saveUser(LoginActivity.this, user);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("user", user);
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

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() > 5;
    }

    public boolean validateInpute(String input) {
        return input.length() > 3;
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void signin(String login, String password, final Callback<String> cb) {
        /*AnimationSet animationSet = new AnimationSet(getBaseContext(), null);
        animationSet.addAnimation(new AlphaAnimation(1f, 0f));
        animationSet.addAnimation(new TranslateAnimation(0, 0, 0, 700));
        animationSet.setDuration(750);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loginButton.setVisibility(View.GONE);
                loginFacebook.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        loginButton.startAnimation(animationSet);
        loginFacebook.startAnimation(animationSet);

*/
        progressBar.setVisibility(View.VISIBLE);
        Log.d("connect", login + " " + password);
        //github sign in
        githubService.signin(login, password, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

                TypedInput body = response.getBody();
                Log.d("connect", "second");
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(body.in()));
                    StringBuilder out = new StringBuilder();
                    String newLine = System.getProperty("line.separator");
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                        out.append(newLine);
                    }
                    cb.success(out.toString(), response2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
        progressBar.setVisibility(View.GONE);

        //mdperreur();

        //succ();
    }


    public void mdperreur() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        imageEchec.setVisibility(View.VISIBLE);
                        final TextInputLayout email = (TextInputLayout) findViewById(R.id.layoutLoginEmail);
                        final TextInputLayout password = (TextInputLayout) findViewById(R.id.layoutLoginePassword);
                        password.setError("mot de passe erroné");
                        //loginButton.setVisibility(View.VISIBLE);
                        //loginFacebook.setVisibility(View.VISIBLE);
                        //imageEchec.setVisibility(View.GONE);
                    }
                },
                2000);
    }

    public void succ() {
        final TextInputLayout email = (TextInputLayout) findViewById(R.id.layoutLoginEmail);
        final TextInputLayout password = (TextInputLayout) findViewById(R.id.layoutLoginePassword);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        imageSuccess.setVisibility(View.VISIBLE);
                        password.setErrorEnabled(false);
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                                    }
                                },
                                500);
                    }
                },
                2000);
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
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            AuthUtils.saveUser(this, user);
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
