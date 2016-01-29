package tn.iac.mobiledevelopment.covoiturageapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    protected ProgressBar progressBar= null ;
    protected ImageView cnxButton = null ;
    protected ImageView inscButton = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cnxButton=(ImageView)findViewById(R.id.cnxButton);
        inscButton=(ImageView)findViewById(R.id.inscButton);
        progressBar=(ProgressBar)findViewById(R.id.mainProgressBar);

        progressBar.setVisibility(View.GONE);

        inscButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = (new Intent(MainActivity.this, InscriptionActivity.class));
                startActivity(intent);
            }
        });
        cnxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = (new Intent(MainActivity.this, LoginActivity.class));
                startActivity(intent);
            }
        });

    }
}
