package com.ceng.muhendis.muneckexercises;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ceng.muhendis.muneckexercises.helpers.FirebaseDBHelper;
import com.ceng.muhendis.muneckexercises.helpers.FunctionsHelper;
import com.ceng.muhendis.muneckexercises.model.MuscleSurveyFirebaseDb;

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
import java.security.Key;
import java.util.ArrayList;

import muhendis.diabetex.db.viewmodel.UserViewModel;

import static java.sql.Types.NULL;

public class FirstSurveyActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    RadioGroup mRadioGroup;
    LinearLayout mLinearLayout,painLinearLayout,muscle1LinearLayout,muscle2LinearLayout,muscle3LinearLayout,mobileLinearLayout;
    ArrayList<View> radiogroupList1,radiogroupList2,radiogroupList3;
    SeekBar mPainSlider,mMobileSlider;
    private final String TAG ="FirstSurveyActivity";
    private UserViewModel mUserViewModel;
    public int pid,section=0;
    private final String PID_KEY = "ProgramId";
    FirebaseDBHelper mFirebaseDBHelper;
    private boolean isBeforeTreatment=true,isMobileSurveyOk=false;
    private TextView windowTitle,lastSurveyTitleSt,lastSurveyExpSt,painTitleSt,painExpSt,muscle1TitleSt,muscle1ExpSt,muscle2TitleSt,muscle2ExpSt,muscle3TitleSt,muscle3ExpSt;
    Button surveyCompleteBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_survey);
        Toolbar toolbar = findViewById(R.id.toolbarFirstSurvey);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mLinearLayout = findViewById(R.id.surveyRootLinearLayout);
        mPainSlider = findViewById(R.id.painSlider);
        mMobileSlider = findViewById(R.id.mobileSlider);

        mFirebaseDBHelper = new FirebaseDBHelper(getApplicationContext());

        painLinearLayout = findViewById(R.id.painSurveyLinearLayout);
        muscle1LinearLayout = findViewById(R.id.muscleSurvey1LinearLayout);
        muscle2LinearLayout = findViewById(R.id.muscleSurvey2LinearLayout);
        muscle3LinearLayout = findViewById(R.id.muscleSurvey3LinearLayout);
        mobileLinearLayout = findViewById(R.id.mobileSurveyLinearLayout);
        painTitleSt = findViewById(R.id.painST);
        painExpSt = findViewById(R.id.painExpST);
        muscle1TitleSt = findViewById(R.id.muscle1TitleSt);
        muscle1ExpSt = findViewById(R.id.musle1ExpSt);
        muscle2TitleSt = findViewById(R.id.muscle2TitleSt);
        muscle2ExpSt = findViewById(R.id.muscle2ExpSt);
        muscle3TitleSt = findViewById(R.id.muscle3TitleSt);
        muscle3ExpSt = findViewById(R.id.muscle3ExpSt);
        surveyCompleteBtn = findViewById(R.id.btn_survey_complete);
        lastSurveyExpSt = findViewById(R.id.lastSurveyExpSt);
        lastSurveyTitleSt = findViewById(R.id.lastSurveyTitleSt);
        windowTitle = findViewById(R.id.title_first_survey);

        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            windowTitle.setText("Questionnaire");
            lastSurveyTitleSt.setText("Mobile App Satisfaction Assessment");
            lastSurveyExpSt.setText("Evaluate your enhanced mobile app experience for long-time smartphone usage.");
            painTitleSt.setText("VISUAL ANALOG SCALA");
            painExpSt.setText("Mark your pain intensity on the neck-shoulder girdle on the scale below after using the smartphone for a long time.");

            muscle1TitleSt.setText("NORDIC MUSCULOSKELETAL QUESTIONNAIRE");
            muscle1ExpSt.setText("Have you at any time during the last 12 months had trouble (such as ache, pain, discomfort, numbness) in;");
            muscle2TitleSt.setText("NORDIC MUSCULOSKELETAL QUESTIONNAIRE");
            muscle2ExpSt.setText("During the last 12 monts have you been prevented from carrying out normal activities (e.g. job, housework, hobbies) because of this truoble in;");
            muscle3TitleSt.setText("NORDIC MUSCULOSKELETAL QUESTIONNAIRE");
            muscle3ExpSt.setText("During the last 7 days have you had trouble in;");
            surveyCompleteBtn.setText("Continue");




        }



        Intent intent = getIntent();
        pid = intent.getIntExtra(PID_KEY,NULL);
        isBeforeTreatment = !intent.getBooleanExtra(Keys.IF_SIX_WEEKS_PASSED,false);

        if(!isBeforeTreatment && !isMobileSurveyOk)
        {
            painLinearLayout.setVisibility(View.GONE);
            mobileLinearLayout.setVisibility(View.VISIBLE);
            isMobileSurveyOk=true;
        }


    }

    public void completeSurvey(View btn)
    {
        switch (section){
            case 0:
                if(isMobileSurveyOk)
                {
                    mobileLinearLayout.setVisibility(View.GONE);
                    painLinearLayout.setVisibility(View.VISIBLE);
                    isMobileSurveyOk=false;
                }
                else
                {
                    section=1;
                    painLinearLayout.setVisibility(View.GONE);
                    muscle1LinearLayout.setVisibility(View.VISIBLE);
                    if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                        radiogroupList1 = new ArrayList<View>();
                        mLinearLayout.findViewsWithText(radiogroupList1,"survey1",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                        Log.d(TAG,"SURVEY 1 CONTET DESC LENGTH "+radiogroupList1.size());
                        changeRadioButtonsLanguageToEnglish(radiogroupList1);
                        changeTitlesToEnglish();
                    }
                }

                break;
            case 1:
                radiogroupList1 = new ArrayList<View>();
                mLinearLayout.findViewsWithText(radiogroupList1,"survey1",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);

                if(checkIfAllCompleted(radiogroupList1)){
                    section=2;
                    muscle1LinearLayout.setVisibility(View.GONE);
                    muscle2LinearLayout.setVisibility(View.VISIBLE);
                    if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                        radiogroupList2 = new ArrayList<View>();
                        mLinearLayout.findViewsWithText(radiogroupList2,"survey2",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                        changeRadioButtonsLanguageToEnglish(radiogroupList2);
                        changeTitlesToEnglish();
                    }


                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "Lütfen bütün alanları doldurunuz!";
                    if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                        text = "Please fill out all of the fields!";
                    }
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                break;
            case 2:
                radiogroupList2 = new ArrayList<View>();
                mLinearLayout.findViewsWithText(radiogroupList2,"survey2",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                if(checkIfAllCompleted(radiogroupList2)){
                    section=3;
                    muscle2LinearLayout.setVisibility(View.GONE);
                    muscle3LinearLayout.setVisibility(View.VISIBLE);
                    if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                        radiogroupList3 = new ArrayList<View>();
                        mLinearLayout.findViewsWithText(radiogroupList3,"survey3",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                        changeRadioButtonsLanguageToEnglish(radiogroupList3);
                        changeTitlesToEnglish();
                    }
                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "Lütfen bütün alanları doldurunuz!";
                    if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                        text = "Please fill out all of the fields!";
                    }
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                break;
            case 3:
                radiogroupList3 = new ArrayList<View>();
                mLinearLayout.findViewsWithText(radiogroupList3,"survey3",View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
                if(checkIfAllCompleted(radiogroupList3)){
                    SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.saved_user_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.saved_user_isfirstsurveycompleted_key), true);

                    if(!isBeforeTreatment){
                        editor.putBoolean(getString(R.string.saved_user_islastsurveycompleted_key), true);
                        mFirebaseDBHelper.insertMobileSurvey(mMobileSlider.getProgress());

                    }

                    editor.apply();


                    mFirebaseDBHelper.insertMuscleSurvey(1,isBeforeTreatment,radiogroupList1);
                    mFirebaseDBHelper.insertMuscleSurvey(2,isBeforeTreatment,radiogroupList2);
                    mFirebaseDBHelper.insertMuscleSurvey(3,isBeforeTreatment,radiogroupList3);
                    mFirebaseDBHelper.insertPainSurvey(isBeforeTreatment,mPainSlider.getProgress(),this);
                }
                else{
                    Context context = getApplicationContext();
                    CharSequence text = "Lütfen bütün alanları doldurunuz!";
                    if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                        text = "Please fill out all of the fields!";
                    }
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                break;

        }
        /*if(checkIfAllCompleted(radiogroupList1) && checkIfAllCompleted(radiogroupList2) && checkIfAllCompleted(radiogroupList3))
        {
            mFirebaseDBHelper.insertMuscleSurvey(1,true,radiogroupList1);
            mFirebaseDBHelper.insertMuscleSurvey(2,true,radiogroupList2);
            mFirebaseDBHelper.insertMuscleSurvey(3,true,radiogroupList3);
            mFirebaseDBHelper.insertPainSurvey(true,mPainSlider.getProgress(),this);

        }
        else
        {
            Context context = getApplicationContext();
            CharSequence text = "Lütfen bütün alanları doldurunuz!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }*/
    }

    public void changeTitlesToEnglish(){
        findAndChangeTextToEnglish("boyun","Neck");
        findAndChangeTextToEnglish("omuz","Shoulders");
        findAndChangeTextToEnglish("dirsek","Elbows");
        findAndChangeTextToEnglish("elbilek","Wrists/Hands");
        findAndChangeTextToEnglish("sirt","Upper Back");
        findAndChangeTextToEnglish("bell","Lower Back");
        findAndChangeTextToEnglish("kalca","Hips/Thighs");
        findAndChangeTextToEnglish("diz","Knees");
        findAndChangeTextToEnglish("ayak","Ankles/Feet");
    }

    public void findAndChangeTextToEnglish(String desc,String textToBeChanged){
        ArrayList<View> textViewList = new ArrayList<View>();
        mLinearLayout.findViewsWithText(textViewList,desc,View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        for (View view:textViewList
                ) {
            TextView tv = (TextView) view;
            tv.setText(textToBeChanged);
        }

    }

    public boolean checkIfAllCompleted(ArrayList<View> radioGroup)
    {
        for (View view:radioGroup
             ) {
            RadioGroup rg = (RadioGroup) view;
            if(rg.getCheckedRadioButtonId()== -1)
                return false;
        }

        return true;
    }

    public void changeRadioButtonsLanguageToEnglish(ArrayList<View> radioGroup){
        Log.d(TAG,"changeRadioButtonsLanguageToEnglish NOT INDSIDE FOR YET");
        for (View view:radioGroup
                ) {
            Log.d(TAG,"changeRadioButtonsLanguageToEnglish INDSIDE FOR");
            RadioGroup rg = (RadioGroup) view;
            int count = rg.getChildCount();
            ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();
            for (int i=0;i<count;i++) {
                View o = rg.getChildAt(i);
                RadioButton rb = (RadioButton)o;
                Log.d(TAG,"changeRadioButtonsLanguageToEnglish");
                if(i==0){
                    rb.setText("Yes");
                }
                else{
                    rb.setText("No");
                }
            }

        }


    }

    public void showAlertNoInternet()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("İNTERNET BAĞLANTISI YOK");
        alertDialogBuilder.setIcon(R.drawable.no_internet);

        // set dialog message
        alertDialogBuilder
                .setMessage("Lütfen internet bağlantınızı kontrol edip tekrar deneyiniz.")
                .setCancelable(false)
                .setPositiveButton("TAMAM",new DialogInterface.OnClickListener() {
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("BAĞLANTI PROBLEMİ");
        alertDialogBuilder.setIcon(R.drawable.no_internet);

        // set dialog message
        alertDialogBuilder
                .setMessage("Lütfen internet bağlantınızı kontrol edip tekrar deneyiniz.")
                .setCancelable(false)
                .setPositiveButton("TAMAM",new DialogInterface.OnClickListener() {
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

    private class AsyncSurveyKas extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(FirstSurveyActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tAnket Kaydediliyor...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL(Config.SERVER_SURVEY_KAS);

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
                        .appendQueryParameter("pid", params[0])
                        .appendQueryParameter("question", params[1])
                        .appendQueryParameter("boyun", params[2])
                        .appendQueryParameter("omuz", params[3])
                        .appendQueryParameter("dirsek", params[4])
                        .appendQueryParameter("el", params[5])
                        .appendQueryParameter("sirt", params[6])
                        .appendQueryParameter("bel", params[7])
                        .appendQueryParameter("kalca", params[8])
                        .appendQueryParameter("diz", params[9])
                        .appendQueryParameter("ayak", params[10]);
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

                /*Log.d("SUCCESSACTIVITY","BEFORE START");
                Intent intent = new Intent(FirstSurveyActivity.this,LoginActivity.class);
                intent.putExtra("SNACK",true);
                startActivity(intent);
                FirstSurveyActivity.this.finish();*/


            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

                showAlertConnectionProblem();

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
                Toast.makeText(FirstSurveyActivity.this, "OOPs! Bağlantı hatası", Toast.LENGTH_LONG).show();
            }
        }



    }

    private class AsyncSurveyPain extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(FirstSurveyActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tAnket Kaydediliyor...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL(Config.SERVER_SURVEY_PAIN);

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
                        .appendQueryParameter("pid", params[0])
                        .appendQueryParameter("pain", params[1]);
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


            }else if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                //Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

                showAlertConnectionProblem();

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
                Toast.makeText(FirstSurveyActivity.this, "OOPs! Bağlantı hatası", Toast.LENGTH_LONG).show();
            }
        }



    }

}
