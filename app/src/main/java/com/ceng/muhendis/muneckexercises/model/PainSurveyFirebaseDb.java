package com.ceng.muhendis.muneckexercises.model;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by muhendis on 29.03.2018.
 */

public class PainSurveyFirebaseDb {
    String pid;
    int painLevel;
    boolean isBeforeTreatment;

    public PainSurveyFirebaseDb() {
    }

    public PainSurveyFirebaseDb(String pid, int painLevel, boolean isBeforeTreatment) {
        this.pid = pid;
        this.painLevel = painLevel;
        this.isBeforeTreatment = isBeforeTreatment;
    }

    public String getPid() {
        return pid;
    }

    public boolean isBeforeTreatment() {
        return isBeforeTreatment;
    }

    public void setPainLevel(int painLevel) {
        this.painLevel = painLevel;
    }

    public int getPainLevel() {
        return painLevel;

    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setBeforeTreatment(boolean beforeTreatment) {
        isBeforeTreatment = beforeTreatment;
    }
}
