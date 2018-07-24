package com.ceng.muhendis.muneckexercises.model;

/**
 * Created by muhendis on 31.03.2018.
 */

public class FinishedExerciseFirebaseDb {
    String date,pid_date,pid,ex_name;
    int eid,duration;

    public FinishedExerciseFirebaseDb() {
    }

    public FinishedExerciseFirebaseDb(String date, String pid, int eid, int duration,String ex_name) {
        this.date = date;
        this.pid = pid;
        this.eid = eid;
        this.duration = duration;
        this.ex_name = ex_name;
        this.pid_date = pid+"_"+date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEx_name(String ex_name) {
        this.ex_name = ex_name;
    }

    public void setPid_date(String pid_date) {
        this.pid_date = pid_date;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public String getPid_date() {
        return pid_date;
    }

    public String getPid() {
        return pid;
    }

    public int getEid() {
        return eid;
    }

    public int getDuration() {
        return duration;
    }

    public String getEx_name() {
        return ex_name;
    }
}
