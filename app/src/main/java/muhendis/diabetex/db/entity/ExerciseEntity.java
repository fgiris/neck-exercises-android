package muhendis.diabetex.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import muhendis.diabetex.model.Exercise;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by muhendis on 23.01.2018.
 * This model has care plan exercises
 */


@Entity(tableName = "exercise")

public class ExerciseEntity implements Exercise {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "user_id")
    private int uid;

    @ColumnInfo(name = "eid")
    private int eid;

    @ColumnInfo(name = "pid")
    private int pid;

    @ColumnInfo(name = "name")
    private String exName;

    @ColumnInfo(name = "exp")
    private String exp;

    @ColumnInfo(name = "photo")
    private String photo;

    @ColumnInfo(name = "video")
    private String video;

    @ColumnInfo(name = "daily_rep")
    private int dailyRep;

    @ColumnInfo(name = "weekly_rep")
    private int weeklyRep;

    @ColumnInfo(name = "set")
    private int set;

    @ColumnInfo(name = "rep")
    private int rep;

    public ExerciseEntity(int uid, int eid, int pid, String exName, String exp, String photo, String video, int dailyRep, int weeklyRep, int set, int rep, int duration, int rest) {
        this.uid = uid;
        this.eid = eid;
        this.pid = pid;
        this.exName = exName;
        this.exp = exp;
        this.photo = photo;
        this.video = video;
        this.dailyRep = dailyRep;
        this.weeklyRep = weeklyRep;
        this.set = set;
        this.rep = rep;
        this.duration = duration;
        this.rest = rest;
    }

    public int getDuration() {

        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @ColumnInfo(name = "duration")
    private int duration;


    public void setDailyRep(int dailyRep) {
        this.dailyRep = dailyRep;
    }

    public void setWeeklyRep(int weeklyRep) {
        this.weeklyRep = weeklyRep;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getDailyRep() {

        return dailyRep;
    }

    public int getWeeklyRep() {
        return weeklyRep;
    }

    public int getSet() {
        return set;
    }

    public int getRep() {
        return rep;
    }

    public int getRest() {
        return rest;
    }

    @ColumnInfo(name = "rest")
    private int rest;

    public void setId(int id) {
        this.id = id;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public int getId() {

        return id;
    }
    @Override
    public int getUid() {
        return uid;
    }
    @Override
    public int getEid() {
        return eid;
    }
    @Override
    public String getExName() {
        return exName;
    }
    @Override
    public String getExp() {
        return exp;
    }
    @Override
    public String getPhoto() {
        return photo;
    }
    @Override
    public String getVideo() {
        return video;
    }
}
