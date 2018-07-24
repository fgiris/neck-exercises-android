package com.ceng.muhendis.muneckexercises.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.ceng.muhendis.muneckexercises.R;

public class FunctionsHelper {
    static public boolean IsLanguageEnglish(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.saved_user_file_key), Context.MODE_PRIVATE);
        boolean isEnglish = sharedPref.getBoolean(context.getString(R.string.saved_user_isLanguageEnglish_key), false);
        return isEnglish;
    }
}
