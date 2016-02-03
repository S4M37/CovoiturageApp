package tn.iac.mobiledevelopment.covoiturageapp.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by kalou on 14/01/2016.
 */
public class remplaceFont {
    public static void replaceDefaultFont(Context context, String nameOfFont, String nameOfFONTinAss) {
        Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), nameOfFONTinAss);
        replaceFont(nameOfFont, customFontTypeface);
    }

    public static void replaceFont(String nameOfFont, Typeface customFontTypeface) {
        try {
            Field myfield = Typeface.class.getDeclaredField(nameOfFont);
            myfield.setAccessible(true);
            myfield.set(null, customFontTypeface);
        } catch (NoSuchFieldException e) {
            //   e.printStackTrace();
        } catch (IllegalAccessException e) {
            //   e.printStackTrace();
        }

    }
}
