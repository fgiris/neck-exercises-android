package com.ceng.muhendis.muneckexercises;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ceng.muhendis.muneckexercises.helpers.FirebaseDBHelper;
import com.ceng.muhendis.muneckexercises.helpers.FunctionsHelper;
import com.ceng.muhendis.muneckexercises.model.UserFirebaseDb;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import muhendis.diabetex.db.entity.UserEntity;
import muhendis.diabetex.model.User;

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = "RegisterActivity";
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    EditText name,age,weight,height,email,password;
    String mName,mAge,mWeight,mHeight,mEmail,mPassword,mGender;
    Spinner gender;
    Button registerBtn;
    FirebaseDBHelper mFirebaseDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            setContentView(R.layout.activity_register_eng);

        }
        else{
            setContentView(R.layout.activity_register);
        }

        Toolbar toolbar = findViewById(R.id.toolbarRegister);
        name = findViewById(R.id.registerNameEditText);
        age = findViewById(R.id.registerAgeEditText);
        height = findViewById(R.id.registerHeightEditText);
        weight = findViewById(R.id.registerWeightEditText);
        email = findViewById(R.id.registerEmailEditText);
        password = findViewById(R.id.registerPasswordEditText);
        gender = findViewById(R.id.registerGenderSpinner);
        registerBtn = findViewById(R.id.register_btn_finish);

        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            name.setHint("Name");
            age.setHint("Age");
            height.setHint("Height (cm)");
            weight.setHint("Weight (kg)");
            email.setHint("Email");
            password.setHint("Password");
            registerBtn.setText("Register");

        }

        mFirebaseDBHelper = new FirebaseDBHelper(getApplicationContext());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean finishRegister(View btn)
    {
        mName = name.getText().toString();
        mAge = age.getText().toString();
        mHeight = height.getText().toString();
        mWeight = weight.getText().toString();
        mEmail = email.getText().toString();
        mPassword = password.getText().toString();
        mGender = gender.getSelectedItem().toString();

        boolean isOk = checkRequiredFields();
        if(!isOk)
        {
            Context context = getApplicationContext();
            CharSequence text = "Lütfen bütün alanları doldurunuz!";
            if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                text = "Please fill out all of the fields!";
            }
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else
        {
            //new AsyncRegister().execute(mName,mGender,mAge,mHeight,mWeight,mEmail,mPassword);
            if(!mFirebaseDBHelper.isOnline())
            {
                showAlertNoInternet();
            }
            else{
                final String today = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());

                final UserFirebaseDb user = new UserFirebaseDb(mEmail,mPassword,mName,mGender,mHeight,mWeight,mAge,false,false,mFirebaseDBHelper.getFirebaseToken(),today,false);
                mFirebaseDBHelper.insertUser(user);
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
        }

        return true;
    }

    public boolean checkRequiredFields()
    {
        if(mName.equals("")||mAge.equals("")||mHeight.equals("")||mWeight.equals("")||mEmail.equals("")||mPassword.equals("")||gender.getSelectedItemPosition()==0)
            return false;
        else
            return true;
    }


    public void showAlertNoInternet()
    {
        String title="İNTERNET BAĞLANTISI YOK";
        String message="Lütfen internet bağlantınızı kontrol edip tekrar deneyiniz.";
        String btnText = "TAMAM";

        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            title = "NO INTERNET CONNECTION";
            message = "Please check your internet connection and try again.";
            btnText = "OK";
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.no_internet);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnText,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity

                    }
                })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void showAlertAlreadyExists()
    {
        String title="HESABINIZ BULUNMAKTA";
        String message="Bu emaile kayıtlı zaten bir hesap bulunmaktadır. Lütfen giriş yapmayı deneyiniz.";
        String btnText = "TAMAM";

        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            title = "Registration Error";
            message = "This email is already registered to the system";
            btnText = "OK";
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.ic_error_black_24dp);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnText,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity

                    }
                })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void showAlertConnectionProblem()
    {
        String title="BAĞLANTI PROBLEMİ";
        String message="Lütfen internet bağlantınızı kontrol edip tekrar deneyiniz.";
        String btnText = "TAMAM";

        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            title = "CONNECTION PROBLEM";
            message = "Please check your internet connection and try again.";
            btnText = "OK";
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.no_internet);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnText,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity

                    }
                })
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }





    private class AsyncRegister extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(RegisterActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tKayıt Yapılıyor...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL(Config.SERVER_REGISTER_ADRESS);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ");
                conn.setRequestProperty("Cookie", "__test=0a17afdc7b54e80d08a8468c43512b05; expires=2037-12-31T23:55:55.000Z; path=/");



                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("name", params[0])
                        .appendQueryParameter("gender", params[1])
                        .appendQueryParameter("age", params[2])
                        .appendQueryParameter("height", params[3])
                        .appendQueryParameter("weight", params[4])
                        .appendQueryParameter("email", params[5])
                        .appendQueryParameter("password", params[6]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.d("ERROR",e1.getMessage());
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();
                Log.d("RESPOSE CODE", "CODE: "+response_code);

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    //Toast.makeText(MainActivity.this,result.toString(), Toast.LENGTH_LONG);
                    Log.d("RESULT TEXT",""+result.toString());
                    return(result.toString());

                }else{

                    Log.d("RESPONSE CODE", "ERROR");
                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("RESPONSE", "ERROR: "+e.getLocalizedMessage());
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            result = result.trim();
            Log.d("SUCCESSACTIVITY","result text: "+result);
            if(result.equalsIgnoreCase("true"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */

                Log.d("SUCCESSACTIVITY","BEFORE START");
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                intent.putExtra("SNACK",true);
                startActivity(intent);
                RegisterActivity.this.finish();


            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

                showAlertAlreadyExists();

            } else if (result.equalsIgnoreCase("exception")) {

                //Toast.makeText(RegisterActivity.this, "OOPs! Bağlantı hatası", Toast.LENGTH_LONG).show();

                showAlertNoInternet();

            }
            else if (result.equalsIgnoreCase("unsuccessful")) {

                //Toast.makeText(RegisterActivity.this, "OOPs! Bağlantı hatası", Toast.LENGTH_LONG).show();

                showAlertConnectionProblem();

            }
            else
            {
                Toast.makeText(RegisterActivity.this, "OOPs! Bağlantı hatası", Toast.LENGTH_LONG).show();
            }
        }



    }
}
