package com.ceng.muhendis.muneckexercises.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import com.ceng.muhendis.muneckexercises.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProgressBack extends AsyncTask<String[],String[],String> {
    ProgressDialog PD;
    AlertDialog dialog;
    public Activity activity;
    private final String TAG="ProgressBack";
    static AlertDialog.Builder builder;

    @Override
    protected String doInBackground(String[]... urls) {
        String rootDir = Environment.getExternalStorageDirectory()
                + File.separator + ".mü_neck_exercises/";

        File rootFile = new File(rootDir);
        rootFile.mkdir();

        String dir = Environment.getExternalStorageDirectory()
                + File.separator + ".mü_neck_exercises/videos/";
        rootFile = new File(dir);
        rootFile.mkdir();

        for (int i=0;i<urls[0].length;i++) {
            Log.d(TAG,"FILE TO DOWNLOAD\nLink: "+urls[0][i]+"\nName: "+urls[1][i]);
            if(!downloadFile(urls[0][i],urls[1][i])){
                return "error";
            }
            else{


            }
        }
        return "ok";
    }

    @Override
    protected void onPreExecute() {


        //PD= ProgressDialog.show(MainActivity.this,"Veriler İndiriliyor", "Lütfen Bekleyiniz. Program verileri indiriliyor. Videoların indirilmesi zaman alabilir.", true);
        //PD.setCancelable(false);
        builder = new AlertDialog.Builder(activity);
        // Get the layout inflater
        LayoutInflater inflater = activity.getLayoutInflater();
        String title="Veriler İndiriliyor";
        View custom_dialog = inflater.inflate(R.layout.custom_dialog_downloading, null);
        if(FunctionsHelper.IsLanguageEnglish(activity.getApplicationContext())){
            title="Downloading Media Files";
            custom_dialog = inflater.inflate(R.layout.custom_dialog_downloading_eng, null);
        }
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(custom_dialog)
                .setCancelable(false)
                .setTitle(title);
        dialog = builder.create();
        dialog.show();

    }

    protected void onPostExecute(String result) {
        dialog.dismiss();
        //PD.dismiss();
        if(result=="ok")
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            // Get the layout inflater
            LayoutInflater inflater = activity.getLayoutInflater();
            View custom_dialog = inflater.inflate(R.layout.custom_dialog_downloading_success, null);

            String title="Veriler İndirildi";
            String buttonText = "TAMAM";
            if(FunctionsHelper.IsLanguageEnglish(activity.getApplicationContext())){
                title="Download Succesful";
                buttonText = "OK";

                custom_dialog = inflater.inflate(R.layout.custom_dialog_downloading_success_eng, null);
            }
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(custom_dialog)
                    .setCancelable(true)
                    .setTitle(title)
                    .setPositiveButton(buttonText,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity

                        }
                    });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            // Get the layout inflater
            LayoutInflater inflater = activity.getLayoutInflater();
            View custom_dialog = inflater.inflate(R.layout.custom_dialog_downloading_fail, null);
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            String title="Veriler İndirilemedi";
            String buttonText = "TAMAM";
            if(FunctionsHelper.IsLanguageEnglish(activity.getApplicationContext())){
                title="Download Error";
                buttonText = "OK";

                custom_dialog = inflater.inflate(R.layout.custom_dialog_downloading_fail_eng, null);
            }
            builder.setView(custom_dialog)
                    .setCancelable(true)
                    .setTitle(title)
                    .setPositiveButton(buttonText,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity

                        }
                    });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }


    }

    private boolean downloadFile(String fileURL, String fileName) {
        try {
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + ".mü_neck_exercises/videos/";

            File rootFile = new File(rootDir);
            rootFile.mkdirs();
            URL url = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            //c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    fileName));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();

            return true;
        } catch (Exception e) {
            Log.d("Error....", e.toString());
            return false;
        }

    }




}



