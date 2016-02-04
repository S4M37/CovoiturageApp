package tn.iac.mobiledevelopment.covoiturageapp.utils;


import android.content.Context;

import com.google.gson.Gson;

import tn.iac.mobiledevelopment.covoiturageapp.models.User;

/**
 * Created by S4M37 on 02/02/2016.
 */
public class AuthUtils {
    //user's shared preferences
    public static void saveUser(Context context, User user) {
        Gson gson = new Gson();
        context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putString("user_info", gson.toJson(user)).commit();
    }

    public static String retireiveUser(Context context) {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("user_info", null);
    }

    //token's shared preferences
    public static void saveToken(Context context, String token) {
        context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putString("user_token", token).commit();
    }

    public static String retireiveToken(Context context) {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("user_token", null);
    }
}
