package tn.iac.mobiledevelopment.covoiturageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import tn.iac.mobiledevelopment.covoiturageapp.R;
import tn.iac.mobiledevelopment.covoiturageapp.models.User;
import tn.iac.mobiledevelopment.covoiturageapp.utils.AuthUtils;
import tn.iac.mobiledevelopment.covoiturageapp.utils.remplaceFont;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    /*private static final float BLUR_RADIUS = 25f;
    public static final String TextButtom1 = "Proposer un trajet";
    public static final String TextButtom2 = "Rechercher un trajet";
    public static final String TextButtom3 = "Aide";*/
    public static final String TextButtom4 = "Decconexion";
    protected Toolbar toolbar;
    /*private FloatingActionButton Mfab;
    private FloatingActionMenu Mfabmenu;

    ImageView imageView1, imageView2;
    RoundImage roundedImage, roundedImage1;*/

    protected User user;
    protected GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        remplaceFont.replaceDefaultFont(this, "DEFAULT", "Exo-Medium.otf");
        remplaceFont.replaceDefaultFont(this, "SERIF", "Roboto-Light.ttf");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);

        //get user
        user = (User) getIntent().getSerializableExtra("user");
        Log.d("user", user.toString());

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

/*
        ImageView image1 = (ImageView) findViewById(R.id.back);
        Bitmap bitmapOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.user);
           image1.setImageBitmap(bitmapOriginal);
        //image1.setImageBitmap(createBitmap_ScriptIntrinsicBlur(bitmapOriginal, 22.0f));


        imageView1 = (ImageView) findViewById(R.id.user);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.user);
        roundedImage = new RoundImage(bm);
        imageView1.setImageDrawable(roundedImage);




        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
        getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout) findViewById(R.id.iddrawerlayout),toolbar);

   //     setUpfab();
*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            AuthUtils.saveUser(this, null);
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            } else if (AuthUtils.retireiveToken(this) != null) {
                AuthUtils.saveToken(this,null);
            }else{
               //google sign out
            }
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "failed to disconnect", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals(TextButtom4)) {
            Intent intent = new Intent(this, Parametre.class);
            startActivity(intent);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}

