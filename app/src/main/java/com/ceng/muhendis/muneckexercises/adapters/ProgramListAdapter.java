package com.ceng.muhendis.muneckexercises.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ceng.muhendis.muneckexercises.ExercisesActivity;
import com.ceng.muhendis.muneckexercises.R;

import java.text.SimpleDateFormat;
import java.util.List;

import muhendis.diabetex.db.entity.ProgramEntity;

/**
 * Created by muhendis on 27.01.2018.
 */

public class ProgramListAdapter extends RecyclerView.Adapter<ProgramListAdapter.ProgramViewHolder> {

    private final String TAG = "ProgramListAdapter";
    private final String PID_KEY = "ProgramId";

    class ProgramViewHolder extends RecyclerView.ViewHolder {
        private final TextView programHeader,programDoctorName,programStartDate,programFinishDate,programDuration,programCompletePercentage;
        private final ProgressBar programCompleteProgress;
        private final Button mStartButton;
        private ProgramViewHolder(final View itemView) {
            super(itemView);
            programHeader = itemView.findViewById(R.id.programHeader);
            programDoctorName= itemView.findViewById(R.id.programDoctorName);
            programStartDate= itemView.findViewById(R.id.programStartDate);
            programFinishDate= itemView.findViewById(R.id.programFinishDate);
            programDuration= itemView.findViewById(R.id.programDuration);
            programCompletePercentage= itemView.findViewById(R.id.programCompletePercentage);
            programCompleteProgress = itemView.findViewById(R.id.programCompleteProgress);
            mStartButton = itemView.findViewById(R.id.startButton);

        }
    }

    private final LayoutInflater mInflater;
    private List<ProgramEntity> mPrograms; // Cached copy of exercises

    public ProgramListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public ProgramViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ProgramViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProgramViewHolder holder, int position) {
        if (mPrograms != null) {
            final ProgramEntity current = mPrograms.get(position);
            holder.programHeader.setText(current.getProgramName());
            holder.programDoctorName.setText(current.getDoctorName());
            // Take the start and finish date from db and format them
            Log.d(TAG,"DATE: "+current.getStartDate());
            String startDate = new SimpleDateFormat("dd/MM/yyyy").format(current.getStartDate());
            String finishDate = new SimpleDateFormat("dd/MM/yyyy").format(current.getFinishDate());
            holder.programStartDate.setText(startDate);
            holder.programFinishDate.setText(finishDate);
            holder.programDuration.setText(String.valueOf(current.getDuration())+" dk");
            holder.programCompletePercentage.setText("%"+String.valueOf(current.getCompletePercentage()));

            holder.mStartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(v.getContext(), ExercisesActivity.class);
                    myIntent.putExtra(PID_KEY,current.getId());
                    v.getContext().startActivity(myIntent);
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.programCompleteProgress.setProgress(current.getCompletePercentage(),true);
            }

        } else {
            // Covers the case of data not being ready yet.
            holder.programHeader.setText("Egzersiziniz bulunmamaktadır.");
        }
    }

    public void setPrograms(List<ProgramEntity> programs){
        mPrograms = programs;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPrograms != null)
            return mPrograms.size();
        else return 0;
    }
}