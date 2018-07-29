package com.ceng.muhendis.muneckexercises.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.ceng.muhendis.muneckexercises.R;

public class FunctionsHelper {
    static public boolean IsLanguageEnglish(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.saved_user_file_key), Context.MODE_PRIVATE);
        boolean isEnglish = sharedPref.getBoolean(context.getString(R.string.saved_user_isLanguageEnglish_key), false);
        return isEnglish;
    }

    static public void showAlertAndLogout(Activity activity,FirebaseDBHelper firebaseDBHelper)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        String title = "Çıkış Yap";
        String message = "Hesabınızdan çıkış yapmak istediğinizden emin misiniz?";
        String positiveBtnText="Çıkış",negativeBtnText="Vazgeç";
        if(FunctionsHelper.IsLanguageEnglish(activity)){
            title = "Log Out";
            message = "Are you sure you want to log out from your account?";
            positiveBtnText = "Log out";
            negativeBtnText = "Cancel";
        }
        alertDialogBuilder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        firebaseDBHelper.logout_user(activity);

                    }
                })
                .setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
