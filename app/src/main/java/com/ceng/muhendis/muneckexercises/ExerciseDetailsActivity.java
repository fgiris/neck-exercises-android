package com.ceng.muhendis.muneckexercises;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ceng.muhendis.muneckexercises.helpers.FirebaseDBHelper;
import com.ceng.muhendis.muneckexercises.helpers.FunctionsHelper;
import com.ceng.muhendis.muneckexercises.model.ExerciseFirebaseDb;
import com.ceng.muhendis.muneckexercises.model.FinishedExerciseFirebaseDb;
import com.ceng.muhendis.muneckexercises.model.UserFirebaseDb;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import muhendis.diabetex.db.entity.StatisticsEntity;
import muhendis.diabetex.db.viewmodel.StatisticsViewModel;

import static java.sql.Types.NULL;

public class ExerciseDetailsActivity extends AppCompatActivity {
    TextView mExDetailsSet,mExDetailsRep,mExDetailsRest,mExDetailsExp,mExDetailsName,setsST,repsST,expST;
    View mGrayLine;
    ImageView mVideoPlay,mExercisePhoto;
    Chronometer mTimer;
    Button startStopButton;
    boolean isStarted=false;
    private final String TAG = "ExerciseDetailsActivity";
    String exName,exExp,exSet,exRep,exRest,exVideoLink,exPhotoLink;
    int mSectionNumber,mExerciseListId;
    FirebaseDBHelper mFirebaseDBHelper;
    long startTime,elapsedTime=0;
    Activity activity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        showInfo();

        mVideoPlay = findViewById(R.id.exDetailsVideoPlayImage);
        startStopButton = findViewById(R.id.startStopButton);
        mExDetailsName = findViewById(R.id.exDetailsHeader);
        mExDetailsSet = findViewById(R.id.exDetailsSet);
        mExDetailsRep = findViewById(R.id.exDetailsRep);
        mExDetailsRest = findViewById(R.id.exDetailsRest);
        mExDetailsExp = findViewById(R.id.exDetailsExpEditText);
        mGrayLine = findViewById(R.id.exDetailsGrayLine);
        mExercisePhoto = findViewById(R.id.exDetailsImage);
        setsST = findViewById(R.id.exDetailsSetsStaticText);
        repsST = findViewById(R.id.exDetailsRepsStaticText);
        expST = findViewById(R.id.exDetailsExpStaticText);

        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            setsST.setText("Sets");
            repsST.setText("Reps");
            expST.setText("Explanation");
            startStopButton.setText("START");
        }

        mTimer = findViewById(R.id.timer);
        implementPlayImageOnClickListener();

        mFirebaseDBHelper = new FirebaseDBHelper(getApplicationContext());


        Intent intent = getIntent();
        exName = intent.getStringExtra(Keys.EX_NAME);
        exExp = intent.getStringExtra(Keys.EX_EXP);
        exSet = intent.getStringExtra(Keys.EX_SET);
        exRep = intent.getStringExtra(Keys.EX_REP);
        exRest = intent.getStringExtra(Keys.EX_REST);
        exVideoLink = intent.getStringExtra(Keys.EX_VIDEO);
        exPhotoLink = intent.getStringExtra(Keys.EX_PICTURE);
        mSectionNumber = intent.getIntExtra(Keys.SECTION_NUMBER,1);
        mExerciseListId = intent.getIntExtra(Keys.EX_LIST_ID,0);
        Log.d(TAG,"LIST ID FROM INTENT: "+mExerciseListId);

        if(mSectionNumber==2)
        {
            startStopButton.setVisibility(View.GONE);
            mTimer.setVisibility(View.GONE);
            mGrayLine.setVisibility(View.GONE);
        }

        mExDetailsName.setText(exName);
        mExDetailsSet.setText(exSet);
        mExDetailsRep.setText(exRep);
        mExDetailsRest.setText(exRest);
        mExDetailsExp.setText(exExp);

        Toolbar toolbar = findViewById(R.id.exDetailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setDisplayOptions();


        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "marmaraegzersiz/images"+File.separator+(mExerciseListId)+".jpg");
        if(f.exists())
        {
            Bitmap bMap = BitmapFactory.decodeFile(f.getPath());

            mExercisePhoto.setImageBitmap(bMap);
        }
        else
        {
            new DownloadImageTask(mExercisePhoto,mExerciseListId)
                    .execute(exPhotoLink);

        }

        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStopTimer();
            }
        });

    }

    private void implementPlayImageOnClickListener()
    {
        mVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ExerciseVideoActivity.class);
                exVideoLink= Environment.getExternalStorageDirectory()
                        + File.separator + ".mü_neck_exercises/videos/" + mExerciseListId +".mp4";
                intent.putExtra(Keys.EX_VIDEO,exVideoLink);
                startActivity(intent);
            }
        });
    }

    private void setDisplayOptions()
    {
        //Get window metrics to resize transperent image layer when actual image being downloaded
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ImageView mTransperentImage = findViewById(R.id.exDetailsTransperentLayer);
        mTransperentImage.setMinimumHeight((int)(width/1920.0*1080));
        Log.d(TAG,"Minimum height: "+((int)(width/1920.0*1080)));
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

    public void startStopTimer()
    {
        if(!isStarted)
        {
            if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                startStopButton.setText("STOP");
            }
            else{
                startStopButton.setText("BİTİR");
            }

            startStopButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.circular_button_pressed));
            mTimer.setBase(SystemClock.elapsedRealtime());
            mTimer.start();
            isStarted=true;
        }
        else
        {
            mTimer.stop();
            final long seconds = SystemClock.elapsedRealtime() - mTimer.getBase();
            Log.d(TAG,"ELAPSED TIME: "+(int)seconds);
            isStarted=false;
            final String today = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
            FirebaseDatabase.getInstance().getReference().child("users").orderByChild("token").equalTo(FirebaseInstanceId.getInstance().getToken()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot is the "issue" node with all children with id 0
                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            // do something with the individual "issues"
                            final String userId = issue.getKey();
                            String exerciseKey = "exercises";
                            if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                                exerciseKey = "exercises_eng";
                            }

                            FirebaseDatabase.getInstance().getReference().child(exerciseKey).orderByChild("name").equalTo(exName).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                            ExerciseFirebaseDb exercise = issue.getValue(ExerciseFirebaseDb.class);
                                            FinishedExerciseFirebaseDb finishedExercise = new FinishedExerciseFirebaseDb(today,userId,Integer.parseInt(issue.getKey()),(int)seconds/1000,exercise.getName());
                                            FirebaseDatabase.getInstance().getReference().child("finished_exercises").push().setValue(finishedExercise);

                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            String message="Egzersiz başarıyla tamamlandı";
            if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
                message = "Exercise completed succesfully";
            }
            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
            ExerciseDetailsActivity.this.finish();


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        elapsedTime = 0;
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Keys.elapsedTime += System.currentTimeMillis() - startTime;
        //Log.d(TAG,"PAUSED - TIMER VALUE: "+(int)(elapsedTime));
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mFirebaseDBHelper.insertUsageStatistics(activity,(Keys.elapsedTime/1000));
                Keys.elapsedTime=0;
            }
        });
        t.start();

    }


    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
    }

    public void showInfo(){
        String title="Hatırlatma";
        String message="Egzersiz yaparken başla ve bitir butonuna basmayı unutmayınız.";
        String buttonText = "Tamam";
        if(FunctionsHelper.IsLanguageEnglish(getApplicationContext())){
            title = "Reminder";
            message="Please do not forget to press start and stop button before starting exercise.";
            buttonText = "OK";
        }
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
