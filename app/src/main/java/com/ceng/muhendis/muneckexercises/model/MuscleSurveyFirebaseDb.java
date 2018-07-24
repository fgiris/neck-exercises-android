package com.ceng.muhendis.muneckexercises.model;

import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by muhendis on 29.03.2018.
 */

public class MuscleSurveyFirebaseDb {
    int questionNumber;
    boolean isBeforeTreatment;
    String pid;
    List radioGroupList;

    public MuscleSurveyFirebaseDb() {
    }

    public MuscleSurveyFirebaseDb(int questionNumber, boolean isBeforeTreatment, String pid, ArrayList<View> radioGroupList) {
        this.questionNumber = questionNumber;
        this.isBeforeTreatment = isBeforeTreatment;
        this.pid = pid;
        String[] radioGroupArray = new String[9];
        for(int i=0;i<9;i++)
        {
            if((((RadioGroup)radioGroupList.get(i)).getCheckedRadioButtonId())%2==1)
                radioGroupArray[i] = "1";
            else
                radioGroupArray[i]  = "2";
        }
         this.radioGroupList = new ArrayList<String>(Arrays.asList(radioGroupArray));

    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void setBeforeTreatment(boolean beforeTreatment) {
        isBeforeTreatment = beforeTreatment;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setRadioGroupList(List radioGroupList) {
        this.radioGroupList = radioGroupList;
    }

    public List getRadioGroupList() {
        return radioGroupList;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public boolean isBeforeTreatment() {
        return isBeforeTreatment;
    }

    public String getPid() {
        return pid;
    }

}
