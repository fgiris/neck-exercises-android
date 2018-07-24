package com.ceng.muhendis.muneckexercises.model;

/**
 * Created by muhendis on 8.04.2018.
 */

public class MobileAppSatisfactionSurveyFirebaseDb {
    String pid;
    int satisfactionLevel;

    public MobileAppSatisfactionSurveyFirebaseDb() {
    }

    public MobileAppSatisfactionSurveyFirebaseDb(String pid, int satisfactionLevel) {
        this.pid = pid;
        this.satisfactionLevel = satisfactionLevel;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getSatisfactionLevel() {
        return satisfactionLevel;
    }

    public void setSatisfactionLevel(int satisfactionLevel) {
        this.satisfactionLevel = satisfactionLevel;
    }
}
