package com.ceng.muhendis.muneckexercises.adapters;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceng.muhendis.muneckexercises.DownloadImageTask;
import com.ceng.muhendis.muneckexercises.ExerciseDetailsActivity;
import com.ceng.muhendis.muneckexercises.Keys;
import com.ceng.muhendis.muneckexercises.R;
import com.ceng.muhendis.muneckexercises.helpers.FunctionsHelper;
import com.ceng.muhendis.muneckexercises.model.ExerciseFirebaseDb;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import muhendis.diabetex.db.entity.ExerciseEntity;

/**
 * Created by muhendis on 27.01.2018.
 */

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder>  {

    private final String TAG = "ExerciseListAdapter";
    private static ExerciseListAdapter mExerciseListAdapter;
    private final String CURRENT_USER="CURRENT_USER";
    public int currenPid;
    private static Context context;

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private final TextView exName,exRep,exSet,exDuration;
        private int mSection,mExerciseListId;
        private String exVideoLink,exPhotoLink,exExp,exRest;
        private final CardView mCardView;
        private final ImageView exImage;
        private boolean multiSelect = false;
        private ArrayList<Integer> selectedItems = new ArrayList<Integer>();
        private ActionMode mActionMode;
        private ExerciseListAdapter mExerciseListAdapter;
        private ImageView mHappy,mUnhappy;
        private TextView setsStaticText,repsStaticText;


        private ExerciseViewHolder(View itemView, ExerciseListAdapter mExerciseListAdapter) {
            super(itemView);
            this.mExerciseListAdapter = mExerciseListAdapter;
            exName = itemView.findViewById(R.id.exName);
            exSet = itemView.findViewById(R.id.exMainSet);
            exRep = itemView.findViewById(R.id.exMainRep);
            exDuration = itemView.findViewById(R.id.exDuration);
            exImage = itemView.findViewById(R.id.exImage);
            mCardView = itemView.findViewById(R.id.exerciseCard);
            setsStaticText = itemView.findViewById(R.id.setsStaticText);
            repsStaticText = itemView.findViewById(R.id.repsStaticText);


            implementClickListener(itemView);
        }

        private void implementClickListener(final View itemView)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), ExerciseDetailsActivity.class);
                    intent.putExtra(Keys.EX_NAME,exName.getText());
                    intent.putExtra(Keys.EX_EXP,exExp);
                    intent.putExtra(Keys.EX_REP,exRep.getText());
                    intent.putExtra(Keys.EX_SET,exSet.getText());
                    intent.putExtra(Keys.EX_VIDEO,exVideoLink);
                    intent.putExtra(Keys.EX_PICTURE,exPhotoLink);
                    intent.putExtra(Keys.EX_REST,exRest);
                    intent.putExtra(Keys.SECTION_NUMBER,mSection);
                    intent.putExtra(Keys.EX_LIST_ID,mExerciseListId);

                    v.getContext().startActivity(intent);
                    //Log.d(TAG,"Clicked when action mode is null--"+getAdapterPosition());

                }
            });

        }


    }


    private final LayoutInflater mInflater;
    private List<ExerciseEntity> mExercises; // Cached copy of exercises
    private List<ExerciseFirebaseDb> mExercisesFirebase;
    private boolean buttonClicked= false;
    private int mSectionNumber;
    private SparseBooleanArray mSelectedItemsIds,mDownloadedImagesIDs,mBitmapImageSetIds;
    private HashMap<Integer,Boolean> mDownloadedImageIdsMap,mBitmapSetIdsMap;
    private List<Integer> indexChange;

    public ExerciseListAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mSelectedItemsIds = new SparseBooleanArray();
        mDownloadedImagesIDs=new SparseBooleanArray();
        mBitmapImageSetIds=new SparseBooleanArray();
        mDownloadedImageIdsMap = new HashMap<>();
        mBitmapSetIdsMap = new HashMap<>();

        mExerciseListAdapter = this;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recylerview_exercise_item, parent, false);
        return new ExerciseViewHolder(itemView,mExerciseListAdapter);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        if (mExercisesFirebase != null) {
            ExerciseFirebaseDb current = mExercisesFirebase.get(position);
            holder.exName.setText(current.getName());
            holder.exSet.setText(String.valueOf(current.getSet()));
            holder.exRep.setText(String.valueOf(current.getRep()));
            holder.exDuration.setText(String.valueOf(current.getDuration())+" sn");
            holder.exVideoLink = current.getVideo_link();
            holder.exPhotoLink = current.getPhoto_link();
            holder.exExp = current.getExp();
            holder.exRest = String.valueOf(current.getRest());
            holder.mSection=mSectionNumber;
            holder.mExerciseListId = current.getEid();

            if(FunctionsHelper.IsLanguageEnglish(context)){
                holder.setsStaticText.setText("Sets:");
                holder.repsStaticText.setText("Reps:");
                holder.exDuration.setText(String.valueOf(current.getDuration())+" s");
            }




            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "marmaraegzersiz/images"+File.separator+current.getEid()+".jpg");
            if(f.exists())
            {
                Bitmap bMap = BitmapFactory.decodeFile(f.getPath());

                final int maxSize = 960;
                int outWidth;
                int outHeight;
                int inWidth = bMap.getWidth();
                int inHeight = bMap.getHeight();
                if(inWidth > inHeight){
                    outWidth = maxSize;
                    outHeight = (inHeight * maxSize) / inWidth;
                } else {
                    outHeight = maxSize;
                    outWidth = (inWidth * maxSize) / inHeight;
                }

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bMap, outWidth, outHeight, false);


                holder.exImage.setImageBitmap(resizedBitmap);
                //mBitmapSetIdsMap.put(current.getEid(),true);
            }
            else
            {
                //if(mDownloadedImageIdsMap.get(current.getEid())==null)
                {
                    new DownloadImageTask(holder.exImage,current.getEid()).execute(current.getPhoto_link());
                    //mDownloadedImageIdsMap.put(current.getEid(),true);
                    //mBitmapSetIdsMap.put(current.getEid(),true);
                }
            }




            /*holder.mCardView.setBackgroundColor(mSelectedItemsIds.get(position) ? Color.BLUE
                            : Color.RED);*/

            //Log.d(TAG,"ON BIND VIEW HOLDER ICERSINDE Position:"+position);


        } else {
            //Log.d(TAG,"DATA YOK");

            // Covers the case of data not being ready yet.
            //holder.programHeader.setText("Egzersiziniz bulunmamaktadır.");

        }
    }

    public void setExercisesFirebase(List<ExerciseFirebaseDb> exercises,int sectionNumber,List<Integer> indexChange){
        mExercisesFirebase = exercises;
        mSectionNumber = sectionNumber;
        this.indexChange = indexChange;
        notifyDataSetChanged();
    }



    public void setExercises(List<ExerciseEntity> exercises){
        mExercises = exercises;
        notifyDataSetChanged();
    }

    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        if (mExercisesFirebase != null)
        {
            return mExercisesFirebase.size();
        }

        else return 0;
    }
}