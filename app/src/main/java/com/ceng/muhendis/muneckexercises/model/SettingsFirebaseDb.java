package com.ceng.muhendis.muneckexercises.model;

import java.util.Map;

/**
 * Created by muhendis on 1.04.2018.
 */

public class SettingsFirebaseDb {
    String pid;
    Map<String,Boolean> notificationDay,notificationTime;

    public SettingsFirebaseDb() {
    }

    public SettingsFirebaseDb(String pid, Map<String, Boolean> notificationDay, Map<String, Boolean> notificationTime) {
        this.pid = pid;
        this.notificationDay = notificationDay;
        this.notificationTime = notificationTime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Map<String, Boolean> getNotificationDay() {
        return notificationDay;
    }

    public void setNotificationDay(Map<String, Boolean> notificationDay) {
        this.notificationDay = notificationDay;
    }

    public Map<String , Boolean> getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Map<String, Boolean> notificationTime) {
        this.notificationTime = notificationTime;
    }
}
