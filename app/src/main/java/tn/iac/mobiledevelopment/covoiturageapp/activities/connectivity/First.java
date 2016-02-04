package tn.iac.mobiledevelopment.covoiturageapp.activities.connectivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.google.gson.Gson;

import tn.iac.mobiledevelopment.covoiturageapp.R;
import tn.iac.mobiledevelopment.covoiturageapp.activities.MainActivity;
import tn.iac.mobiledevelopment.covoiturageapp.models.User;
import tn.iac.mobiledevelopment.covoiturageapp.utils.AuthUtils;
import tn.iac.mobiledevelopment.covoiturageapp.utils.remplaceFont;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class First extends AppCompatActivity {

    ImageView coonc;
    ImageView inscri;
    Animation an;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        remplaceFont.replaceDefaultFont(this, "DEFAULT", "Exo-Medium.otf");
        remplaceFont.replaceDefaultFont(this, "SANS", "Roboto-Light.ttf");
        remplaceFont.replaceDefaultFont(this, "SERIF", "Roboto-Light.ttf");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        inscri = (ImageView) findViewById(R.id.inscri);
        coonc = (ImageView) findViewById(R.id.conn);
/*
        if(connected == true) {
            // Divisiblité de buttons
            inscri.setVisibility(View.GONE);
            coonc.setVisibility(View.GONE);

            long Delay = 2000;
            // Remove the Title Bar
            // requestWindowFeature(Window.FEATURE_NO_TITLE);
            // Create a Timer
            Timer RunSplash = new Timer();
            // Task to do when the timer ends
            TimerTask ShowSplash = new TimerTask() {
                @Override
                public void run() {
                    // Close SplashScreenActivity.class
                    finish();
                    // Start MainActivity.class
                    intent = new Intent(First.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            };
// Start the timer
            RunSplash.schedule(ShowSplash, Delay);
        }

        else {*/


        //  check if user authentificated
        Gson gson = new Gson();
        User user = gson.fromJson(AuthUtils.retireiveUser(this), User.class);
        if (user != null) {
            Intent intent = new Intent(First.this, MainActivity.class);
            intent.putExtra("user", user);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                TransitionInflater inflater = TransitionInflater.from(this);
                Transition transition = inflater.inflateTransition(R.transition.shared_element_a);
                getWindow().setExitTransition(transition);
                Slide slide = new Slide();
                slide.setDuration(1000);
                getWindow().setReenterTransition(slide);
                coonc.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v) {
                        v.setTransitionName("LoginButtom");
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(First.this, v, v.getTransitionName());
                        Intent intent = (new Intent(First.this, LoginActivity.class));
                        startActivity(intent, optionsCompat.toBundle());
                    }
                });
                inscri.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View v2) {
                        v2.setTransitionName("InscButtom");
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(First.this, v2, v2.getTransitionName());
                        Intent intent = (new Intent(First.this, InscriptionActivity.class));
                        startActivity(intent, optionsCompat.toBundle());
                    }
                });
            } else {
                coonc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = (new Intent(First.this, LoginActivity.class));
                        startActivity(intent);
                    }
                });
                inscri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = (new Intent(First.this, InscriptionActivity.class));
                        startActivity(intent);
                    }
                });
            }
        }

        // }
    }
}
