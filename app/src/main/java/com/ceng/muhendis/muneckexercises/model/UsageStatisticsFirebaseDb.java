package com.ceng.muhendis.muneckexercises.model;

/**
 * Created by muhendis on 8.04.2018.
 */

public class UsageStatisticsFirebaseDb {
    String pid,date,pid_date;
    int seconds;

    public UsageStatisticsFirebaseDb() {
    }

    public UsageStatisticsFirebaseDb(String pid, String date, int seconds) {
        this.pid = pid;
        this.date = date;
        this.seconds = seconds;
        this.pid_date = pid+"_"+date;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
