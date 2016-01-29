package tn.iac.mobiledevelopment.covoiturageapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final float BLUR_RADIUS = 25f;
    public static final String TextButtom1 = "Proposer un trajet";
    public static final String TextButtom2 = "Rechercher un trajet";
    public static final String TextButtom3 = "Aide";
    public static final String TextButtom4 = "Decconexion";
    private FloatingActionButton Mfab;
    private FloatingActionMenu Mfabmenu;
    private Toolbar toolbar;
    ImageView imageView1, imageView2;
    RoundImage roundedImage, roundedImage1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        remplaceFont.replaceDefaultFont(this, "DEFAULT", "Exo-Medium.otf");
        // ***   remplaceFont.replaceDefaultFont(this,"DEFAULT","Corbert-Italic.otf");

        // **  remplaceFont.replaceDefaultFont(this,"DEFAULT","circula-medium.otf");

        // **** remplaceFont.replaceDefaultFont(this,"DEFAULT","Bariol_Regular_Italic.otf");
        remplaceFont.replaceDefaultFont(this, "SERIF", "Roboto-Light.ttf");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView image1 = (ImageView) findViewById(R.id.back);
        Bitmap bitmapOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.user);
           image1.setImageBitmap(bitmapOriginal);
        //image1.setImageBitmap(createBitmap_ScriptIntrinsicBlur(bitmapOriginal, 22.0f));


        imageView1 = (ImageView) findViewById(R.id.user);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.user);
        roundedImage = new RoundImage(bm);
        imageView1.setImageDrawable(roundedImage);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setUpfab();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            startActivity(new Intent(this, Parametre.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals(TextButtom4)) {

            Intent intent = new Intent(this, Parametre.class);
            startActivity(intent);

        }
    }

    private void setUpfab() {

        // Fab
        ImageView imageViewFab = new ImageView(this);
        imageViewFab.setImageResource(R.drawable.ic_person_black_48dp);

        Mfab = new FloatingActionButton.Builder(this)
                .setContentView(imageViewFab)
                .setBackgroundDrawable(R.drawable.selector_button)
                .build();

        ImageView menu1 = new ImageView(this);
        ImageView menu2 = new ImageView(this);
        ImageView menu3 = new ImageView(this);
        ImageView menu4 = new ImageView(this);

        menu1.setImageResource(R.drawable.ic_directions_car_black_24dp);
        menu2.setImageResource(R.drawable.ic_search_black_24dp);
        menu3.setImageResource(R.drawable.ic_help_outline_black_24dp);
        menu4.setImageResource(R.drawable.ic_exit_to_app_black_24dp);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        SubActionButton button1 = itemBuilder.setContentView(menu1).build();
        SubActionButton button2 = itemBuilder.setContentView(menu2).build();
        SubActionButton button3 = itemBuilder.setContentView(menu3).build();
        SubActionButton button4 = itemBuilder.setContentView(menu4).build();

        button1.setTag(TextButtom1);
        button2.setTag(TextButtom2);
        button3.setTag(TextButtom3);
        button4.setTag(TextButtom4);


        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);


        Mfabmenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .addSubActionView(button4)
                .attachTo(Mfab)
                .build();

    }

    private void toggleTranslateFAB(float slideOffset) {
        if (Mfabmenu != null) {
            if (Mfabmenu.isOpen()) {
                Mfabmenu.close(true);
            }
            Mfab.setTranslationX(slideOffset * 200);
        }
    }

    public void onDrawerSlide(float slideOffset) {
        toggleTranslateFAB(slideOffset);
    }
}

