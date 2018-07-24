package com.ceng.muhendis.muneckexercises.model;

/**
 * Created by muhendis on 30.03.2018.
 */

public class ExerciseFirebaseDb{
    int duration,rep,rest,set,eid;
    String name,exp,photo_link,video_link;

    public ExerciseFirebaseDb() {
    }

    public ExerciseFirebaseDb(int duration, int rep, int rest, int set, String name, String exp, String photo_link, String video_link,int eid) {
        this.duration = duration;
        this.rep = rep;
        this.rest = rest;
        this.set = set;
        this.name = name;
        this.exp = exp;
        this.photo_link = photo_link;
        this.video_link = video_link;
        this.eid = eid;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getDuration() {
        return duration;
    }

    public int getRep() {
        return rep;
    }

    public int getRest() {
        return rest;
    }

    public int getSet() {
        return set;
    }

    public String getName() {
        return name;
    }

    public String getExp() {
        return exp;
    }

    public String getPhoto_link() {
        return photo_link;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public void setPhoto_link(String photo_link) {
        this.photo_link = photo_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }


}
